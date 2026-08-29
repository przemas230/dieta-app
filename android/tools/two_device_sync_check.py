#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Cross-device sync check: two devices, one account, one automated run.

WHY THIS EXISTS
---------------
The worst bug this project has shipped (fixed 2026-08-29, see FR-73/v8) was
that NOTHING deleted in the web app ever reached the cloud: the push used
Firestore's `set(..., {merge:true})`, which deep-merges map fields key by
key, so a map sent WITHOUT a key does not lose that key on the server. The
next snapshot brought the old value back, and the app -- correctly, from its
own point of view -- treated it as a change from another device and restored
it. Every deletion in the app was affected. It lived for months.

No unit test could have caught it. The app's own logic was right at every
step; the bug was in what Firestore does with a correct-looking write. The
only thing that catches this class of bug is two real clients on one account.

WHAT IT CHECKS
--------------
  1. CONTROL      -- a product added on A appears on B.
                     (If this fails, sync is broken or the devices are not on
                     the same account, and steps 2-3 would be meaningless.)
  2. DELETION     -- removing that product's tracking on A removes it on B
                     AND it stays gone. This is the exact FR-73/v8 bug: the
                     resurrection happens a few seconds later, not instantly,
                     so the check deliberately waits and looks again.
  3. HIDDEN LIST  -- deleting a product "for good" on A makes B show
                     "Przywroc usuniete produkty". That button is driven ONLY
                     by `pantryHidden`, so it isolates FR-102's own sync from
                     the plain pantry map that step 2 already covered.

WHAT IT CANNOT DO
-----------------
It cannot sign in for you. Both devices must already be signed into the SAME
account before it runs -- it checks that and stops with a clear message if
not. Everything after that is automated.

USAGE
-----
    # two emulators (the second needs -read-only if it is the same AVD):
    emulator -avd Medium_Phone_API_35 -gpu swiftshader_indirect &
    emulator -avd Medium_Phone_API_35 -gpu swiftshader_indirect -read-only &

    python android/tools/two_device_sync_check.py
    python android/tools/two_device_sync_check.py --a emulator-5554 --b emulator-5556
    python android/tools/two_device_sync_check.py --install android/dist/app-debug.apk

Exit code 0 = everything propagated, 1 = something did not (with the step
named). Prints what it is doing as it goes, so a failure says WHICH hop broke.
"""

import argparse
import os
import re
import subprocess
import sys
import tempfile
import time

PACKAGE = "com.przemas230.dietaapp"
ACTIVITY = f"{PACKAGE}/.MainActivity"

# How long a change may take to reach the other device. The app debounces its
# push by 1.5 s and Firestore adds a round trip, so anything under ~10 s would
# report false failures on a slow network.
PROPAGATE_TIMEOUT_S = 45
# How long to keep watching AFTER a deletion looks successful. The FR-73/v8
# bug did not stop the delete -- it let the value come back on the next
# snapshot, seconds later. A check that stops at "it disappeared" would have
# passed while the bug was live.
RESURRECTION_WATCH_S = 20
POLL_S = 2


def adb_path():
    override = os.environ.get("ADB")
    if override:
        return override
    local = os.environ.get("LOCALAPPDATA")
    if local:
        candidate = os.path.join(local, "Android", "Sdk", "platform-tools", "adb.exe")
        if os.path.exists(candidate):
            return candidate
    return "adb"


ADB = adb_path()


class Device:
    """One phone/emulator, addressed by text on screen rather than coordinates.

    Everything here looks elements up by their VISIBLE TEXT and taps the centre
    of what it found. Hard-coded coordinates were tried first and are not worth
    it: they break on every layout tweak, and a test that silently taps the
    wrong thing is worse than no test.
    """

    def __init__(self, serial, label):
        self.serial = serial
        self.label = label

    def sh(self, *args, timeout=60):
        return subprocess.run(
            [ADB, "-s", self.serial, *args],
            capture_output=True, text=True, timeout=timeout,
            env={**os.environ, "MSYS_NO_PATHCONV": "1"},
        )

    def shell(self, cmd, timeout=60):
        return self.sh("shell", cmd, timeout=timeout)

    def dump(self):
        """The current UI as XML text, or '' when the dump fails (mid-animation)."""
        self.shell("uiautomator dump /sdcard/_sync_check.xml")
        path = os.path.join(tempfile.gettempdir(), f"_sync_{self.serial}.xml")
        self.sh("pull", "/sdcard/_sync_check.xml", path)
        try:
            with open(path, encoding="utf-8") as handle:
                return handle.read()
        except OSError:
            return ""

    @staticmethod
    def _nodes(xml):
        """Every addressable label on screen, as (label, centre).

        Yields BOTH `text` and `content-desc`, because icon-only buttons have
        no text at all -- the settings gear is reachable only by its
        description ("Ustawienia"). Matching descriptions too is what lets
        this script avoid hard-coded coordinates entirely; a tap by
        coordinates is the one thing that can silently hit the wrong element
        and turn a passing app into a failing report.
        """
        for element in re.finditer(r"<node\b[^>]*>", xml):
            attrs = element.group(0)
            bounds = re.search(r'bounds="\[(\d+),(\d+)\]\[(\d+),(\d+)\]"', attrs)
            if not bounds:
                continue
            x1, y1, x2, y2 = (int(bounds.group(i)) for i in range(1, 5))
            centre = ((x1 + x2) // 2, (y1 + y2) // 2)
            for attr in ("text", "content-desc"):
                found = re.search(attr + r'="([^"]*)"', attrs)
                if found and found.group(1):
                    yield found.group(1), centre

    @staticmethod
    def _clickables(xml):
        """Bounds of every node that actually handles a tap."""
        boxes = []
        for element in re.finditer(r"<node\b[^>]*>", xml):
            attrs = element.group(0)
            if 'clickable="true"' not in attrs:
                continue
            bounds = re.search(r'bounds="\[(\d+),(\d+)\]\[(\d+),(\d+)\]"', attrs)
            if bounds:
                boxes.append(tuple(int(bounds.group(i)) for i in range(1, 5)))
        return boxes

    @classmethod
    def _tappable_point(cls, centre, xml):
        """Where to actually tap for a label found at `centre`.

        Compose labels are usually NOT the clickable node -- a bottom-nav item
        renders its icon and its text as two non-clickable children of one
        clickable parent. Tapping the label's own centre happens to work when
        the parent covers that pixel and silently does nothing when it does
        not, which is how the first version of this failed on the navigation
        bar. Taking the smallest clickable box that CONTAINS the label removes
        the guesswork: it is the element the user would be pressing.
        """
        x, y = centre
        containing = [b for b in cls._clickables(xml) if b[0] <= x <= b[2] and b[1] <= y <= b[3]]
        if not containing:
            return centre
        smallest = min(containing, key=lambda b: (b[2] - b[0]) * (b[3] - b[1]))
        return ((smallest[0] + smallest[2]) // 2, (smallest[1] + smallest[3]) // 2)

    def find(self, needle, xml=None, exact=False):
        """Centre of the first element matching `needle`, or None.

        `exact` matters more than it looks. Substring matching is the natural
        default, but several labels here are substrings of OTHER labels on the
        same screen -- the add dialog's title is "➕ Dodaj własny produkt",
        which contains its own confirm button's text "Dodaj". Matching by
        substring there taps the title and nothing happens, which is exactly
        how this script failed the second time. Anything that is a complete,
        known label is looked up exactly; only genuinely partial needles
        (emoji-prefixed rows, counters like "(1)") use substrings.

        Retries one dump when the first comes back empty: uiautomator returns
        nothing while a screen is mid-animation, and treating that as "the
        element is gone" would make every check racy.
        """
        if xml is None:
            xml = self.dump()
            if not xml:
                time.sleep(1.0)
                xml = self.dump()
        for text, centre in self._nodes(xml):
            if (text == needle) if exact else (needle in text):
                return centre
        return None

    def has(self, needle, xml=None, exact=False):
        return self.find(needle, xml, exact) is not None

    def _aim(self, needle, what, exact):
        xml = self.dump()
        centre = self.find(needle, xml, exact)
        if centre is None:
            time.sleep(1.0)
            xml = self.dump()
            centre = self.find(needle, xml, exact)
        if centre is None:
            raise Lookup(f"[{self.label}] nie znalazlem na ekranie: {what or needle!r}")
        return self._tappable_point(centre, xml)

    def tap(self, needle, what=None, exact=False):
        x, y = self._aim(needle, what, exact)
        self.shell(f"input tap {x} {y}")
        time.sleep(0.6)

    def long_press(self, needle, what=None, exact=False):
        x, y = self._aim(needle, what, exact)
        self.shell(f"input swipe {x} {y} {x} {y} 900")
        time.sleep(0.8)

    def type_text(self, value):
        self.shell(f'input text "{value}"')
        time.sleep(0.4)

    def wait_until(self, needle, present, timeout, exact=False):
        """Poll until `needle` is (or stops being) on screen. Returns how long it took, or None on timeout."""
        started = time.time()
        while time.time() - started < timeout:
            if self.has(needle, exact=exact) == present:
                return round(time.time() - started, 1)
            time.sleep(POLL_S)
        return None

    def open_app(self):
        """Launch and wait until the app is actually usable, not just started.

        Three different situations look identical from outside ("I can't see
        the navigation bar"), and they need opposite responses, so this tells
        them apart by whether our package appears in the dump at all:

          * app not in the foreground   -> start it (again)
          * our package, but no nav bar -> a dialog left over from an earlier
                                           run is covering it; back out of it
          * our package and the nav bar -> ready

        Getting this wrong is not harmless: an earlier version pressed BACK
        whenever the bar was missing, which included the seconds a cold start
        spends loading the recipe database -- so it backed out of the very app
        it had just launched, and then reported that the app "didn't come up".
        """
        self.shell(f"am start -n {ACTIVITY}")
        started = time.time()
        backs = 0
        restarts = 0
        while time.time() - started < 45:
            xml = self.dump()
            if self.has("Planer", xml, exact=True):
                time.sleep(0.5)
                return
            ours = f'package="{PACKAGE}"' in xml
            if not ours and restarts < 2:
                self.shell(f"am start -n {ACTIVITY}")
                restarts += 1
            elif ours and xml and backs < 3:
                # Our app, but something is on top of it.
                self.shell("input keyevent KEYCODE_BACK")
                backs += 1
            time.sleep(1.5)
        raise Lookup(f"[{self.label}] aplikacja nie wstala w 45s (brak dolnego paska)")

    def open_pantry(self):
        # Exact: pantry log entries read "Spiżarnia: <name> (+1)" and would
        # otherwise win over the navigation tab.
        self.tap("Spiżarnia", what="zakladka Spizarnia", exact=True)
        time.sleep(1.0)

    def scroll_until(self, needles, max_swipes=8):
        """Scroll the current screen until one of `needles` shows up.

        Necessary because the app's lists are virtualised: an element below
        the fold is not merely off-screen, it is absent from the dump
        entirely. Without this, "I can't see the sign-in card" and "this
        device isn't signed in" are indistinguishable -- and the script would
        report the wrong one.
        Returns the needle it found, or None after `max_swipes`.
        """
        for _ in range(max_swipes):
            xml = self.dump()
            for needle in needles:
                if self.has(needle, xml):
                    return needle
            self.shell("input swipe 540 1700 540 700 250")
            time.sleep(0.8)
        return None

    def app_installed(self):
        return PACKAGE in self.shell(f"pm list packages {PACKAGE}").stdout


class Lookup(Exception):
    """A UI element the script expected was not on screen."""


def log(msg):
    print(msg, flush=True)


def preflight(a, b, apk):
    log("== Sprawdzenie wstepne ==")
    for dev in (a, b):
        if apk:
            log(f"   [{dev.label}] instaluje {apk}")
            out = dev.sh("install", "-r", apk, timeout=300)
            if "Success" not in out.stdout:
                return f"[{dev.label}] instalacja nie powiodla sie: {out.stdout.strip() or out.stderr.strip()}"
        if not dev.app_installed():
            return (f"[{dev.label}] aplikacja nie jest zainstalowana. "
                    f"Uruchom ponownie z --install android/dist/app-debug.apk")
        dev.open_app()

    # Signed in? The Konto tab says so directly, and this is the one
    # precondition the script cannot fix by itself.
    for dev in (a, b):
        dev.tap("Ustawienia", what="ikona ustawien (zebatka)", exact=True)
        time.sleep(1.5)
        # The account card sits below the profile form, i.e. off-screen on a
        # phone -- and the list is virtualised, so it is not in the dump until
        # it is scrolled into view.
        found = dev.scroll_until(["Wyloguj się z tego urządzenia", "Zaloguj się"])
        if found == "Wyloguj się z tego urządzenia":
            log(f"   [{dev.label}] zalogowany ✓")
        elif found == "Zaloguj się":
            return (f"[{dev.label}] NIE jest zalogowany. Zaloguj OBA urzadzenia na to samo konto "
                    f"(Ustawienia → Konto) i uruchom skrypt ponownie — sam tego nie zrobie.")
        else:
            return (f"[{dev.label}] nie rozpoznaje ekranu Ustawien — otworz aplikacje recznie "
                    f"i sprawdz, czy dziala.")
        dev.open_app()
    return None


def add_custom_product(dev, name):
    """Adds a custom pantry product and waits until it is really a TILE.

    The name starts with 'aaa-' so it sorts to the top of the first category
    and is visible without scrolling -- the grid is virtualised, so an item
    further down simply is not in the dump.

    The wait at the end is not politeness, it is correctness: `uiautomator
    dump` returns only the TOPMOST window, so while the add dialog is still
    up, the typed name is found in its text FIELD. Without waiting for the
    dialog to go away, the next long-press would land on that field instead
    of the tile -- which is exactly how this script first failed.
    """
    dev.open_pantry()
    dev.tap("Dodaj własny", what="kafelek 'Dodaj wlasny'", exact=True)
    dev.tap("Nazwa produktu", what="pole nazwy", exact=True)
    dev.type_text(name)
    dev.tap("Dodaj", what="przycisk 'Dodaj'", exact=True)
    if dev.wait_until("Nazwa produktu", False, 15, exact=True) is None:
        raise Lookup(f"[{dev.label}] okno dodawania produktu sie nie zamknelo")
    if dev.wait_until(name, True, 15) is None:
        raise Lookup(f"[{dev.label}] kafelek '{name}' nie pojawil sie w siatce")


def self_test(args):
    """Runs every UI step this script performs, on ONE device, with no sync
    assertions and no account needed.

    Its job is to answer "does the script still match the app's screens?".
    A sync check that silently taps the wrong thing would report a sync
    failure that is really a stale selector -- and that is the most likely
    way this file rots, since it addresses the UI by visible Polish text.
    Run it after any change to the Spizarnia screen.
    """
    listed = subprocess.run([ADB, "devices"], capture_output=True, text=True).stdout
    serials = [line.split("\t")[0] for line in listed.splitlines()[1:] if line.strip().endswith("device")]
    serial = args.a or (serials[0] if serials else None)
    if not serial:
        log("BLAD: nie widze zadnego urzadzenia.")
        return 1

    dev = Device(serial, "SELF")
    log(f"Samotest na {serial} — sprawdzam, czy skrypt nadal trafia w UI.\n")
    if not dev.app_installed():
        log(f"BLAD: aplikacja nie jest zainstalowana. Uzyj --install android/dist/app-debug.apk")
        return 1
    if args.install:
        dev.sh("install", "-r", args.install, timeout=300)
    dev.open_app()

    name = f"aaa-selftest-{int(time.time())}"
    steps = []
    try:
        add_custom_product(dev, name)
        steps.append(("dodanie wlasnego produktu", dev.has(name)))

        dev.long_press(name, what=f"kafelek {name}")
        found_menu = dev.has("Usuń śledzenie")
        steps.append(("menu po przytrzymaniu kafelka", found_menu))
        dev.tap("Usuń śledzenie", what="pozycja 'Usun sledzenie'")
        time.sleep(1.0)
        steps.append(("usuniecie sledzenia zdejmuje kafelek", not dev.has(name)))

        add_custom_product(dev, name)
        dev.long_press(name, what=f"kafelek {name}")
        dev.tap("Usuń produkt ze spiżarni na stałe", what="pozycja 'Usun na stale'")
        confirm = dev.has("Usuń na stałe", exact=True)
        steps.append(("okno potwierdzenia usuniecia na stale", confirm))
        dev.tap("Usuń na stałe", what="potwierdzenie", exact=True)
        time.sleep(1.0)
        steps.append(("przycisk przywracania po usunieciu na stale", dev.has("Przywróć usunięte produkty")))

        dev.tap("Przywróć usunięte produkty")
        dev.tap("Przywróć", exact=True)
        time.sleep(1.0)
        steps.append(("przywrocenie ukrytych produktow", not dev.has("Przywróć usunięte produkty")))
    except Lookup as exc:
        log(f"BLAD: {exc}")
        steps.append(("(przerwane)", False))

    dev.shell("input keyevent KEYCODE_BACK")

    log("")
    ok = True
    for label, passed in steps:
        log(("  ✓ " if passed else "  ✗ ") + label)
        ok = ok and passed
    log("")
    if ok:
        log("SAMOTEST: skrypt trafia we wszystkie elementy UI ✓")
        return 0
    log("SAMOTEST: UI sie zmienilo — popraw selektory w tym skrypcie ✗")
    return 1


def main():
    parser = argparse.ArgumentParser(description="Sprawdza synchronizacje miedzy dwoma urzadzeniami na jednym koncie.")
    parser.add_argument("--a", help="serial urzadzenia A (zrodlo zmian)")
    parser.add_argument("--b", help="serial urzadzenia B (obserwator)")
    parser.add_argument("--install", metavar="APK", help="zainstaluj ten APK na obu przed testem")
    parser.add_argument("--timeout", type=int, default=PROPAGATE_TIMEOUT_S, help="ile sekund czekac na propagacje")
    parser.add_argument("--self-test", action="store_true",
                        help="sprawdz tylko, czy skrypt nadal trafia w UI (jedno urzadzenie, bez logowania)")
    args = parser.parse_args()

    if args.self_test:
        return self_test(args)

    listed = subprocess.run([ADB, "devices"], capture_output=True, text=True).stdout
    serials = [line.split("\t")[0] for line in listed.splitlines()[1:] if line.strip().endswith("device")]

    if args.a and args.b:
        serial_a, serial_b = args.a, args.b
    elif len(serials) == 2:
        serial_a, serial_b = serials
    else:
        log("BLAD: potrzebne sa DWA podlaczone urzadzenia (albo podaj --a/--b).")
        log(f"       adb widzi teraz: {serials or 'nic'}")
        log("       Drugi emulator z tego samego AVD wymaga flagi -read-only.")
        return 1

    a = Device(serial_a, "A")
    b = Device(serial_b, "B")
    log(f"Urzadzenie A (zmienia): {serial_a}")
    log(f"Urzadzenie B (obserwuje): {serial_b}\n")

    problem = preflight(a, b, args.install)
    if problem:
        log("\nBLAD: " + problem)
        return 1

    name = f"aaa-synctest-{int(time.time())}"
    failures = []

    try:
        # --- 1. CONTROL ---------------------------------------------------
        log(f"\n== 1/3 Kontrola: dodaje '{name}' na A ==")
        add_custom_product(a, name)
        if not a.has(name):
            log("   BLAD: produkt nie pojawil sie nawet na A — to nie jest problem synchronizacji.")
            return 1
        log("   [A] dodany ✓  czekam na B...")
        b.open_app()
        b.open_pantry()
        took = b.wait_until(name, True, args.timeout)
        if took is None:
            log(f"   [B] NIE zobaczyl produktu w ciagu {args.timeout}s ✗")
            log("       Synchronizacja w ogole nie dziala miedzy tymi urzadzeniami.")
            log("       Sprawdz: czy oba sa na TYM SAMYM koncie i czy maja siec.")
            return 1
        log(f"   [B] zobaczyl po {took}s ✓")

        # --- 2. DELETION (the FR-73/v8 bug) -------------------------------
        log("\n== 2/3 Usuniecie: 'Usun sledzenie' na A ==")
        a.open_pantry()
        a.long_press(name, what=f"kafelek {name}")
        a.tap("Usuń śledzenie", what="pozycja 'Usun sledzenie'")
        time.sleep(1.0)
        log("   [A] usuniete ✓  czekam az zniknie na B...")
        took = b.wait_until(name, False, args.timeout)
        if took is None:
            failures.append("2/3 usuniecie nie dotarlo na B")
            log(f"   [B] produkt WCIAZ tam jest po {args.timeout}s ✗  (to jest objaw FR-73/v8)")
        else:
            log(f"   [B] zniknal po {took}s ✓  — pilnuje przez {RESURRECTION_WATCH_S}s, czy nie wroci...")
            came_back = b.wait_until(name, True, RESURRECTION_WATCH_S)
            if came_back is not None:
                failures.append("2/3 usuniety produkt WROCIL na B")
                log(f"   [B] WROCIL po {came_back}s ✗  — dokladnie objaw FR-73/v8")
            else:
                log("   [B] nie wrocil ✓")

        # --- 3. HIDDEN LIST (FR-102) --------------------------------------
        log("\n== 3/3 Lista ukrytych (pantryHidden): 'Usun na stale' na A ==")
        add_custom_product(a, name)
        time.sleep(2)
        a.long_press(name, what=f"kafelek {name}")
        a.tap("Usuń produkt ze spiżarni na stałe", what="pozycja 'Usun na stale'")
        a.tap("Usuń na stałe", what="potwierdzenie 'Usun na stale'", exact=True)
        time.sleep(1.0)
        log("   [A] usuniete na stale ✓  czekam na przycisk przywracania na B...")
        b.open_pantry()
        took = b.wait_until("Przywróć usunięte produkty", True, args.timeout)
        if took is None:
            failures.append("3/3 pantryHidden nie dotarlo na B")
            log(f"   [B] brak przycisku 'Przywroc usuniete produkty' po {args.timeout}s ✗")
        else:
            log(f"   [B] przycisk pojawil sie po {took}s ✓")

    except Lookup as exc:
        log(f"\nBLAD: {exc}")
        log("      Ekran wyglada inaczej niz skrypt zaklada — sprawdz, czy aplikacja jest na wierzchu.")
        return 1
    finally:
        # Leave both devices as they were found, whatever happened above.
        try:
            a.open_pantry()
            if a.has("Przywróć usunięte produkty"):
                a.tap("Przywróć usunięte produkty")
                a.tap("Przywróć", exact=True)
                log("\n(sprzatanie: przywrocono ukryte produkty na A)")
        except Lookup:
            pass

    log("")
    if failures:
        log("WYNIK: NIE PRZESZLO ✗")
        for item in failures:
            log("  - " + item)
        return 1
    log("WYNIK: wszystko dotarlo w obie strony ✓")
    return 0


if __name__ == "__main__":
    sys.exit(main())
