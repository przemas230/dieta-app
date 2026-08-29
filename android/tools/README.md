# `android/tools/` — narzędzia do sprawdzania rzeczy, których testy jednostkowe nie złapią

## `two_device_sync_check.py` — synchronizacja między dwoma urządzeniami

### Po co to istnieje

Najgorszy błąd, jaki ta aplikacja wypuściła (naprawiony 2026-08-29, patrz
FR-73/v8), polegał na tym, że **nic usuniętego w wersji webowej nigdy nie
docierało do chmury**. Zapis szedł przez `set(..., {merge:true})`, a Firestore
scala wtedy pola mapowe klucz po kluczu — mapa wysłana BEZ jakiegoś klucza nie
kasuje go na serwerze. Kolejny snapshot przynosił starą wartość z powrotem, a
aplikacja — całkiem poprawnie ze swojego punktu widzenia — uznawała ją za
zmianę z innego urządzenia i przywracała. Dotyczyło to każdego usuwania w
aplikacji. Żyło miesiącami.

**Żaden test jednostkowy nie mógł tego złapać.** Logika aplikacji była
poprawna na każdym kroku; błąd tkwił w tym, co Firestore robi z poprawnie
wyglądającym zapisem. Jedyne, co wykrywa tę klasę błędów, to dwaj prawdziwi
klienci na jednym koncie.

### Czego skrypt NIE zrobi

Nie zaloguje się za Ciebie. Oba urządzenia muszą być wcześniej zalogowane na
**to samo konto** — skrypt to sprawdza i zatrzymuje się z jasnym komunikatem,
jeśli nie są. Cała reszta jest zautomatyzowana.

### Co sprawdza

| krok | co dowodzi |
|---|---|
| 1. Kontrola | produkt dodany na A pojawia się na B — jeśli to nie przejdzie, synchronizacja w ogóle nie działa i kroki 2–3 nie miałyby znaczenia |
| 2. Usunięcie | „Usuń śledzenie" na A usuwa produkt na B **i produkt nie wraca**. To dokładnie objaw FR-73/v8: zmartwychwstanie następuje kilka sekund później, nie natychmiast, więc skrypt celowo czeka i patrzy ponownie |
| 3. Lista ukrytych | „Usuń na stałe" na A sprawia, że na B pojawia się przycisk „Przywróć usunięte produkty". Ten przycisk zależy WYŁĄCZNIE od `pantryHidden`, więc izoluje synchronizację FR-102 od zwykłej mapy spiżarni z kroku 2 |

### Jak uruchomić

```bash
# 1. Dwa urządzenia. Najwygodniej dwa OSOBNE AVD-y — wtedy zalogowanie
#    zostaje między uruchomieniami:
#      Android Studio → Device Manager → Create Device (np. "Medium_Phone_API_35_B")
#
#    Dwa razy ten sam AVD też zadziała, ale OBA wymagają wtedy -read-only,
#    a to znaczy, że stan (w tym zalogowanie) znika po zamknięciu:
emulator -avd Medium_Phone_API_35   -gpu swiftshader_indirect -read-only &
emulator -avd Medium_Phone_API_35_B -gpu swiftshader_indirect &

# 2. Zaloguj OBA na to samo konto (Ustawienia → Konto).

# 3. Uruchom:
python android/tools/two_device_sync_check.py --install android/dist/app-debug.apk
```

Kod wyjścia `0` = wszystko dotarło w obie strony, `1` = coś nie dotarło (z
nazwą kroku). Skrypt wypisuje, co robi, więc porażka mówi, **który przeskok**
się zepsuł.

### Samotest — czy skrypt nadal trafia w UI

```bash
python android/tools/two_device_sync_check.py --self-test
```

Wykonuje wszystkie kroki UI na **jednym** urządzeniu, bez logowania i bez
sprawdzania synchronizacji. Odpowiada na pytanie „czy skrypt nadal pasuje do
ekranów aplikacji?". Skrypt adresuje elementy po widocznym tekście polskim, więc
to najbardziej prawdopodobny sposób, w jaki się zepsuje — **uruchom go po
każdej zmianie ekranu Spiżarni**. Sprawdzanie synchronizacji, które po cichu
klika w zły element, zgłosiłoby błąd synchronizacji, który w rzeczywistości
jest nieaktualnym selektorem.

### Dwie pułapki, które ten skrypt ma już wbudowane

Obie kosztowały realny czas przy pisaniu i obie wyglądały jak błąd aplikacji:

1. **`uiautomator dump` zwraca tylko wierzchnie okno.** Dopóki okno dodawania
   produktu jest otwarte, wpisana nazwa znajduje się w jego polu tekstowym —
   więc „widzę nazwę" nie znaczy „kafelek istnieje". Skrypt czeka, aż okno
   zniknie, zanim uzna produkt za dodany.
2. **Dopasowanie po fragmencie tekstu bywa niejednoznaczne.** Tytuł okna to
   „➕ Dodaj własny produkt" i zawiera tekst własnego przycisku „Dodaj" —
   klikanie po fragmencie trafiało w tytuł. Etykiety, które są pełnymi
   napisami, są dopasowywane dokładnie (`exact=True`).

Trzecia, ogólniejsza zasada: **żadnych sztywnych współrzędnych.** Skrypt czyta
`text` ORAZ `content-desc` (ikona zębatki nie ma tekstu, tylko opis
„Ustawienia"). Kliknięcie po współrzędnych to jedyna rzecz, która potrafi po
cichu trafić w zły element i zamienić działającą aplikację w raport o błędzie.
