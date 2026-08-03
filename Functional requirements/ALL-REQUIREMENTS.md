# Wymagania funkcjonalne — Dieta App

Zbiorczy dokument wszystkich wymagań funkcjonalnych aplikacji, spisany retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac (stan na 2026-08-03). Każde wymaganie ma też własny plik `FR-<numer>.md` w tym folderze — ten plik służy do przeglądania wszystkiego naraz. Zasady utrzymania i rewizji opisane są w `README.md`.

## Spis treści

### Przepisy i przeglądanie
- [FR-1: Baza przepisów podzielona na 5 kategorii posiłków](#fr-1-baza-przepisów-podzielona-na-5-kategorii-posiłków)
- [FR-2: Wyszukiwanie i filtrowanie przepisów](#fr-2-wyszukiwanie-i-filtrowanie-przepisów)
- [FR-3: Karta przepisu — widok skrócony i rozwinięty](#fr-3-karta-przepisu--widok-skrócony-i-rozwinięty)
- [FR-4: Miniatura przepisu jako emoji głównego składnika](#fr-4-miniatura-przepisu-jako-emoji-głównego-składnika)
- [FR-5: Przycisk powrotu do góry listy przepisów](#fr-5-przycisk-powrotu-do-góry-listy-przepisów)

### Personalizacja i cele dietetyczne
- [FR-6: Profil użytkownika i wyliczanie zapotrzebowania kalorycznego](#fr-6-profil-użytkownika-i-wyliczanie-zapotrzebowania-kalorycznego)
- [FR-7: Podział dziennego celu kalorycznego na 5 posiłków](#fr-7-podział-dziennego-celu-kalorycznego-na-5-posiłków)
- [FR-8: Filtr bez glutenu / bez laktozy](#fr-8-filtr-bez-glutenu--bez-laktozy)
- [FR-9: Przełącznik rygoru niskiego indeksu glikemicznego](#fr-9-przełącznik-rygoru-niskiego-indeksu-glikemicznego)
- [FR-10: Docelowe proporcje makroskładników zależne od celu](#fr-10-docelowe-proporcje-makroskładników-zależne-od-celu)
- [FR-11: Wynik dopasowania przepisu do profilu (🎯)](#fr-11-wynik-dopasowania-przepisu-do-profilu-🎯)
- [FR-12: Modal wyjaśniający wyliczenia makro/IG/ŁG](#fr-12-modal-wyjaśniający-wyliczenia-makroigłg)
- [FR-13: Piąta kategoria posiłku: Deser/Przekąska](#fr-13-piąta-kategoria-posiłku-deserprzekąska)
- [FR-14: Skalowanie rozmiaru interfejsu (UI scale)](#fr-14-skalowanie-rozmiaru-interfejsu-ui-scale)

### Gotowanie i historia
- [FR-15: Oznaczanie dania jako ugotowane, z historią i ocenami](#fr-15-oznaczanie-dania-jako-ugotowane-z-historią-i-ocenami)
- [FR-16: Sprawdzenie stanu spiżarni dla konkretnego przepisu](#fr-16-sprawdzenie-stanu-spiżarni-dla-konkretnego-przepisu)
- [FR-17: Ocena dania po ugotowaniu (gwiazdki)](#fr-17-ocena-dania-po-ugotowaniu-gwiazdki)

### Planer tygodniowy
- [FR-18: Planer tygodniowy z 5 slotami posiłków dziennie](#fr-18-planer-tygodniowy-z-5-slotami-posiłków-dziennie)
- [FR-19: Wybór innego slotu posiłkowego z poziomu karty przepisu](#fr-19-wybór-innego-slotu-posiłkowego-z-poziomu-karty-przepisu)
- [FR-20: Skalowanie wielkości porcji w planerze](#fr-20-skalowanie-wielkości-porcji-w-planerze)
- [FR-21: Losowe generowanie planu — cały tydzień lub pojedynczy dzień](#fr-21-losowe-generowanie-planu--cały-tydzień-lub-pojedynczy-dzień)
- [FR-22: Czyszczenie planu — cały tydzień lub pojedynczy dzień](#fr-22-czyszczenie-planu--cały-tydzień-lub-pojedynczy-dzień)
- [FR-23: „Ugotuj na 2 dni” — planowanie resztek po zwiększeniu porcji](#fr-23-ugotuj-na-2-dni--planowanie-resztek-po-zwiększeniu-porcji)
- [FR-24: Proaktywna podpowiedź gotowania na kolejny dzień](#fr-24-proaktywna-podpowiedź-gotowania-na-kolejny-dzień)

### Lista zakupów
- [FR-25: Budowanie listy zakupów ze składników przepisów](#fr-25-budowanie-listy-zakupów-ze-składników-przepisów)
- [FR-26: Odhaczanie, udostępnianie i czyszczenie listy zakupów](#fr-26-odhaczanie-udostępnianie-i-czyszczenie-listy-zakupów)
- [FR-27: Dodanie składników z całego tygodnia z Planera](#fr-27-dodanie-składników-z-całego-tygodnia-z-planera)
- [FR-58: Dodawanie składników z konkretnego dnia na liście zakupów](#fr-58-dodawanie-składników-z-konkretnego-dnia-na-liście-zakupów)

### Spiżarnia
- [FR-28: Śledzenie stanu spiżarni w kafelkach pogrupowanych kategoriami](#fr-28-śledzenie-stanu-spiżarni-w-kafelkach-pogrupowanych-kategoriami)
- [FR-29: Odmiana gramatyczna nazw produktów w spiżarni](#fr-29-odmiana-gramatyczna-nazw-produktów-w-spiżarni)
- [FR-30: Zmiana kategorii i usuwanie śledzenia kafelka spiżarni](#fr-30-zmiana-kategorii-i-usuwanie-śledzenia-kafelka-spiżarni)
- [FR-31: Skanowanie kodu kreskowego produktu](#fr-31-skanowanie-kodu-kreskowego-produktu)
- [FR-32: Podpowiedź „🏺 masz w spiżarni” i „Pomysł na danie z ulubionych składników”](#fr-32-podpowiedź-🏺-masz-w-spiżarni-i-pomysł-na-danie-z-ulubionych-składników)

### Szybkie dodawanie i przekąski
- [FR-33: Globalny przycisk szybkiego dodania przekąski/dania z każdego miejsca](#fr-33-globalny-przycisk-szybkiego-dodania-przekąskidania-z-każdego-miejsca)
- [FR-34: Automatyczne szacowanie kalorii przekąski z bazy 336 produktów](#fr-34-automatyczne-szacowanie-kalorii-przekąski-z-bazy-336-produktów)
- [FR-35: Emotikonki przy rozpoznanych składnikach/przekąskach](#fr-35-emotikonki-przy-rozpoznanych-składnikachprzekąskach)

### Śledzenie postępów
- [FR-36: Dzienny pierścień kalorii w nagłówku ze zjadanymi posiłkami](#fr-36-dzienny-pierścień-kalorii-w-nagłówku-ze-zjadanymi-posiłkami)
- [FR-37: Śledzenie nawodnienia — pełny widok i kompaktowy pasek w nagłówku](#fr-37-śledzenie-nawodnienia--pełny-widok-i-kompaktowy-pasek-w-nagłówku)
- [FR-38: Powiadomienia z szybkimi akcjami do liczenia wody](#fr-38-powiadomienia-z-szybkimi-akcjami-do-liczenia-wody)
- [FR-39: Cykliczne przypomnienie o piciu wody](#fr-39-cykliczne-przypomnienie-o-piciu-wody)
- [FR-40: Śledzenie wagi z wykresem](#fr-40-śledzenie-wagi-z-wykresem)
- [FR-41: Historia kalorii z bilansem tygodniowym](#fr-41-historia-kalorii-z-bilansem-tygodniowym)
- [FR-42: Serie (streaks) i historia aktywności](#fr-42-serie-streaks-i-historia-aktywności)
- [FR-60: Warunkowe wyświetlanie „Złotych zasad przy Hashimoto i insulinooporności”](#fr-60-warunkowe-wyświetlanie-złotych-zasad-przy-hashimoto-i-insulinooporności)

### Nagłówek i nawigacja
- [FR-43: Pasek filtrów i kategorii przyklejony pod nagłówkiem](#fr-43-pasek-filtrów-i-kategorii-przyklejony-pod-nagłówkiem)
- [FR-44: Automatyczne chowanie/pokazywanie nagłówka na przewijanie (tylko Przepisy)](#fr-44-automatyczne-chowaniepokazywanie-nagłówka-na-przewijanie-tylko-przepisy)
- [FR-45: Ręczne zwijanie/rozwijanie nagłówka ma pierwszeństwo nad automatyką](#fr-45-ręczne-zwijanierozwijanie-nagłówka-ma-pierwszeństwo-nad-automatyką)
- [FR-46: Zabezpieczenie przed przypadkowym zamknięciem aplikacji (Android „Wstecz”)](#fr-46-zabezpieczenie-przed-przypadkowym-zamknięciem-aplikacji-android-wstecz)
- [FR-47: Brak migotania (FOUC) domyślnych danych profilu przy odświeżeniu](#fr-47-brak-migotania-fouc-domyślnych-danych-profilu-przy-odświeżeniu)
- [FR-59: Wyśrodkowane okienka modalne, na pełną dostępną szerokość](#fr-59-wyśrodkowane-okienka-modalne-na-pełną-dostępną-szerokość)

### Wygląd i motywy
- [FR-48: Wybór motywu kolorystycznego aplikacji](#fr-48-wybór-motywu-kolorystycznego-aplikacji)
- [FR-49: Motyw „Polaroid” z kartami w stylu odbitek natychmiastowych](#fr-49-motyw-polaroid-z-kartami-w-stylu-odbitek-natychmiastowych)
- [FR-50: Redukcja animacji (prefers-reduced-motion)](#fr-50-redukcja-animacji-prefers-reduced-motion)
- [FR-61: Wybór stylu oceniania kart przesunięciem w Ustawieniach](#fr-61-wybór-stylu-oceniania-kart-przesunięciem-w-ustawieniach)

### PWA i działanie offline
- [FR-51: Instalowalna aplikacja PWA z ikoną i manifestem](#fr-51-instalowalna-aplikacja-pwa-z-ikoną-i-manifestem)
- [FR-52: Cache offline przez Service Worker ze strategią stale-while-revalidate](#fr-52-cache-offline-przez-service-worker-ze-strategią-stale-while-revalidate)
- [FR-53: Ręczne wymuszenie aktualizacji i diagnostyka powiadomień](#fr-53-ręczne-wymuszenie-aktualizacji-i-diagnostyka-powiadomień)
- [FR-54: Kopie zapasowe wersji plików aplikacji w repozytorium](#fr-54-kopie-zapasowe-wersji-plików-aplikacji-w-repozytorium)

### Ocenianie i ranking przepisów
- [FR-55: Ocenianie przepisów przesunięciem karty (lubię / nie lubię)](#fr-55-ocenianie-przepisów-przesunięciem-karty-lubię--nie-lubię)
- [FR-56: Duży, balonowy napis podczas oceniania przesunięciem](#fr-56-duży-balonowy-napis-podczas-oceniania-przesunięciem)
- [FR-57: Trwałe oznaczenie oceny i ranking sort](#fr-57-trwałe-oznaczenie-oceny-i-ranking-sort)


---

## Analiza spójności i wykluczeń

Przegląd wymagań pod kątem wzajemnych sprzeczności. Żadna z poniższych par nie okazała się logiczną sprzecznością — w każdym przypadku jeden mechanizm ma jasno określone pierwszeństwo albo oba działają w niezależnych kontekstach. Jeden punkt oznaczono jako świadomie zaakceptowaną niespójność UX (nie błąd), do rozważenia w przyszłości.

1. **FR-44 (auto-chowanie nagłówka na przewijanie) vs FR-45 (ręczne zwijanie ma pierwszeństwo).** Rozstrzygnięcie: ręczne działanie użytkownika zawsze wygrywa i zamraża automatykę aż do wejścia na zakładkę Przepisy od nowa albo ręcznego rozwinięcia. Zweryfikowano dodatkowo, że otwarcie okienka modalnego (FR-12 i inne) nie powinno móc obejść tego zamrożenia — pierwotnie mogło, naprawiono blokadą przewijania tła (patrz historia rewizji FR-45).
2. **FR-9 (kara za wysoki IG w wyniku dopasowania) vs FR-11 (wyświetlanie plakietki „podwyższony IG” na karcie).** Nie wykluczają się — to dwie strony tego samego przełącznika: plakietka istnieje właśnie dla osób, które świadomie wyłączyły rygor niskiego IG i chcą mimo to widzieć tę informację.
3. **FR-3 (stuknięcie rozwija kartę) vs FR-55 (przesunięcie karty ocenia danie).** Ten sam obszar dotykowy obsługuje dwa różne gesty. Rozstrzygnięcie: blokada osi ruchu (pierwsze przekroczenie progu 10px decyduje, czy to gest poziomy-ocena czy pionowy-przewijanie), a stuknięcie bez żadnego znaczącego ruchu liczy się jako rozwinięcie karty — pod warunkiem że w międzyczasie nie przewinęła się też sama strona (patrz rewizja FR-3).
4. **FR-8 (filtr bez glutenu/laktozy) vs kompletność FR-1..FR-3.** Filtr jest jawnie opisany w aplikacji jako orientacyjny (bazuje na oznaczeniach składników w tekście przepisu, nie na certyfikowanej analizie). To ograniczenie, nie sprzeczność — nie ma wymagania gwarantującego 100% trafność, więc nic tu się nie wyklucza.
5. **FR-23 („Ugotuj na 2 dni”, przesunięcie +2 dni, wymaga ręcznej skali ≥2×) vs FR-24 (proaktywna podpowiedź, przesunięcie +1 dzień, automatyczna wg słów kluczowych).** To jedyny punkt oznaczony jako **świadomie zaakceptowana niespójność UX**, nie błąd: oba mechanizmy działają niezależnie i żaden nie nadpisuje danych bez jawnej akcji użytkownika, ale różne przesunięcie czasowe (2 dni vs 1 dzień) między dwoma podobnymi w założeniu funkcjami może być mylące. Do rozważenia w przyszłej rewizji: ujednolicić przesunięcie albo jasno zróżnicować nazewnictwo obu mechanizmów.
6. **FR-42 (limit 20 wpisów historii aktywności) vs pozostałe funkcje korzystające z pełnej historii (FR-40 wykres wagi, FR-41 historia kalorii).** Nie wykluczają się — limit 20 jest wyłącznie ograniczeniem WYŚWIETLANIA jednej konkretnej listy (dziennik aktywności), nie ogranicza danych źródłowych używanych przez inne wykresy/funkcje.
7. **FR-34 (baza 336 przekąsek) vs FR-35 (emotikonki przy rozpoznanych produktach).** Częściowe pokrycie, nie sprzeczność: nie każda z 336 pozycji bazy kalorycznej ma dziś przypisaną emotikonkę w osobnej tabeli `CANON_INFO` — brak emotikonki nie blokuje rozpoznania kalorii (FR-34 działa w pełni niezależnie od FR-35), po prostu nazwa pojawia się bez sufiksu. Możliwe rozszerzenie w przyszłości.
8. **FR-60 (widoczność „Złotych zasad” tylko przy rygorze niskiego IG) vs FR-9 (przełącznik rygoru niskiego IG).** Nie wykluczają się — FR-60 to bezpośrednia konsekwencja FR-9: karta jest po prostu ukrywana, gdy FR-9 jest wyłączone. Jedno wymaganie steruje drugim, bez sprzeczności.
9. **FR-61 (wybór stylu oceniania: balonowa czcionka / kolorowa karta) vs FR-48 (wybór motywu kolorystycznego).** Nie wykluczają się — to dwa niezależne ustawienia. FR-61 celowo działa tak samo w każdym z ośmiu motywów z FR-48, w tym Polaroid (FR-49).

---

# FR-1: Baza przepisów podzielona na 5 kategorii posiłków

**Obszar:** Przepisy i przeglądanie  
**Status:** Zaimplementowane

## Opis
Aplikacja przechowuje 229 przepisów, każdy przypisany do jednej z pięciu kategorii posiłków: Śniadania, II Śniadanie, Obiady, Kolacje, Deser/Przekąska. Kategoria decyduje m.in. o tym, gdzie przepis pojawia się w Planerze i jaki ma docelowy udział w dziennym bilansie kalorycznym.

## Kryteria akceptacji
- Każdy przepis ma dokładnie jedną kategorię (`cat`).
- Zakładka Przepisy pozwala filtrować listę wg kategorii przyciskami-pigułkami u góry.
- Kategoria Deser/Przekąska jest równoprawna z pozostałymi czterema (patrz FR-13).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-2: Wyszukiwanie i filtrowanie przepisów

**Obszar:** Przepisy i przeglądanie  
**Status:** Zaimplementowane

## Opis
Nad listą przepisów znajduje się pole wyszukiwania (po nazwie dania i składnikach) oraz zestaw przełączników: tylko ulubione przepisy (⭐), tylko z ulubionymi składnikami (🌟), tylko dania możliwe do zrobienia z tego, co jest w spiżarni (🏺), sortowanie wg dopasowania do profilu (🎯) i sortowanie rankingowe wg oceny (❤️).

## Kryteria akceptacji
- Wpisanie tekstu w polu wyszukiwania zawęża listę w czasie rzeczywistym.
- Przełączniki są niezależne i można je łączyć (np. tylko ulubione + sortowanie wg dopasowania).
- Pasek filtrów jest przyklejony (sticky) pod nagłówkiem i zawsze widoczny podczas przewijania (patrz FR-43).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-3: Karta przepisu — widok skrócony i rozwinięty

**Obszar:** Przepisy i przeglądanie  
**Status:** Zaimplementowane

## Opis
Każdy przepis wyświetlany jest jako karta z nazwą, czasem przygotowania, kalorycznością i skrótowymi znacznikami (np. podwyższony IG, dopasowanie do celu). Domyślnie karta jest zwinięta; stuknięcie w kartę rozwija pełną listę składników, sposób przygotowania i przyciski akcji.

## Kryteria akceptacji
- Karta w stanie zwiniętym pokazuje tylko nagłówek i podstawowe metadane.
- Rozwinięcie karty odbywa się WYŁĄCZNIE przez wyraźne, stacjonarne stuknięcie — nie przez przypadkowe zatrzymanie przewijania listy (patrz historia rewizji poniżej i FR-44).
- Tylko jedna karta na liście może być rozwinięta jednocześnie.

## Uwagi
Zrewidowane w rundzie z 2026-08-03: pierwotna wersja pozwalała, by dotknięcie kończące przewijanie listy (bardzo mały ruch palca przy jednoczesnym przewinięciu strony przez inercję) było błędnie odczytane jako stuknięcie i rozwijało kartę, co powodowało 'skakanie' ekranu. Naprawiono porównując pozycję przewijania strony w momencie dotknięcia i puszczenia — jeśli strona przewinęła się w tym czasie, gest NIE liczy się jako stuknięcie, nawet jeśli sam palec poruszył się nieznacznie. Patrz też FR-44.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-03): Doprecyzowano zachowanie na podstawie zgłoszonej poprawki — patrz sekcja "Uwagi" powyżej.
---

# FR-4: Miniatura przepisu jako emoji głównego składnika

**Obszar:** Przepisy i przeglądanie  
**Status:** Zaimplementowane

## Opis
Zamiast losowego zdjęcia z internetu, każda karta przepisu pokazuje emoji reprezentujące jej główny składnik, dobierane deterministycznie na podstawie tej samej tabeli kanonicznych nazw składników, która obsługuje spiżarnię.

## Kryteria akceptacji
- Miniatura nie wymaga połączenia z siecią ani zewnętrznego API.
- To samo danie zawsze pokazuje tę samą miniaturę.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-5: Przycisk powrotu do góry listy przepisów

**Obszar:** Przepisy i przeglądanie  
**Status:** Zaimplementowane

## Opis
Na dole widoku Przepisy znajduje się pływający przycisk „⬆️”, który po przewinięciu listy w dół pozwala natychmiast wrócić na sam początek strony.

## Kryteria akceptacji
- Przycisk jest niewidoczny, dopóki użytkownik nie przewinie strony poniżej progu ok. 400 px.
- Kliknięcie przewija stronę do pozycji 0 płynną animacją.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-6: Profil użytkownika i wyliczanie zapotrzebowania kalorycznego

**Obszar:** Personalizacja i cele dietetyczne  
**Status:** Zaimplementowane

## Opis
W Ustawieniach użytkownik podaje płeć, wiek, wzrost, wagę obecną i docelową, poziom aktywności fizycznej oraz cel (redukcja/utrzymanie/budowanie masy). Na tej podstawie aplikacja liczy BMR (wzór Mifflin-St Jeor), następnie TDEE (BMR × współczynnik aktywności), a potem koryguje wynik pod kątem wybranego celu.

## Kryteria akceptacji
- Zmiana dowolnego pola profilu i zapisanie przelicza dzienny cel kaloryczny.
- Wynik jest widoczny w Ustawieniach i w nagłówku aplikacji.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-7: Podział dziennego celu kalorycznego na 5 posiłków

**Obszar:** Personalizacja i cele dietetyczne  
**Status:** Zaimplementowane

## Opis
Dzienny cel kaloryczny jest rozdzielany na pięć posiłków wg stałych proporcji: śniadanie 370/1500, II śniadanie 280/1500, obiad 450/1500, kolacja 300/1500, deser/przekąska 100/1500 (proporcje, nie sztywne wartości — skalują się z dziennym celem).

## Kryteria akceptacji
- Suma pięciu proporcji wynosi dokładnie 1.
- Target dla każdej kategorii przeliczany jest przy każdej zmianie profilu.
- Nieplanowanie posiłku w kategorii Deser/Przekąska nie zaburza pozostałych czterech targetów (patrz FR-13).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-8: Filtr bez glutenu / bez laktozy

**Obszar:** Personalizacja i cele dietetyczne  
**Status:** Zaimplementowane

## Opis
Dwa niezależne przełączniki w Ustawieniach pozwalają ukryć z listy przepisów dania zawierające gluten (pieczywo, kasze glutenowe) lub nabiał bez wyraźnie oznaczonej wersji „bez laktozy”.

## Kryteria akceptacji
- Włączenie filtra ukrywa pasujące przepisy natychmiast po zapisaniu ustawień.
- Filtr jest jawnie opisany jako orientacyjny, nie medyczny — nie gwarantuje 100% poprawności dla każdego przepisu.

## Uwagi
Ograniczenie znane i udokumentowane w samej aplikacji: filtr bazuje na oznaczeniach składników, nie na certyfikowanej analizie, więc nie wyklucza się logicznie z FR-1..FR-3, ale nie należy go traktować jako gwarancji bezpieczeństwa zdrowotnego.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-9: Przełącznik rygoru niskiego indeksu glikemicznego

**Obszar:** Personalizacja i cele dietetyczne  
**Status:** Zaimplementowane

## Opis
Domyślnie aktywny przełącznik „Trzymaj się niskiego IG” obniża ocenę dopasowania (FR-11) dla dań o wyższym ładunku glikemicznym. Wyłączenie przełącznika usuwa tę karę z wyliczenia dopasowania — przydatne dla osób bez insulinooporności/cukrzycy.

## Kryteria akceptacji
- Domyślny stan: włączony.
- Wyłączenie nie zmienia samych danych IG/ŁG przepisu — zmienia tylko sposób liczenia wyniku dopasowania.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-10: Docelowe proporcje makroskładników zależne od celu

**Obszar:** Personalizacja i cele dietetyczne  
**Status:** Zaimplementowane

## Opis
Docelowy rozkład białka/węglowodanów/tłuszczu w gramach różni się w zależności od wybranego celu (redukcja / utrzymanie / budowanie masy) oraz kategorii posiłku, i jest używany jako punkt odniesienia przy liczeniu dopasowania przepisu (FR-11).

## Kryteria akceptacji
- Zmiana celu w profilu przelicza docelowe gramatury makroskładników dla wszystkich pięciu kategorii posiłków.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-11: Wynik dopasowania przepisu do profilu (🎯)

**Obszar:** Personalizacja i cele dietetyczne  
**Status:** Zaimplementowane

## Opis
Każdy przepis z danymi odżywczymi otrzymuje procentowy wynik dopasowania do profilu użytkownika, liczony na podstawie zbieżności B/W/T z targetem danej kategorii posiłku oraz (jeśli FR-9 jest włączone) kary za wysoki ładunek glikemiczny.

## Kryteria akceptacji
- Wynik jest widoczny na karcie przepisu jako plakietka „🎯 N%”.
- Przełącznik 🎯 w toolbarze sortuje listę malejąco wg tego wyniku.
- Przepisy bez kompletnych danych odżywczych nie otrzymują wyniku (traktowane jako `null`, nie jako 0%).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-12: Modal wyjaśniający wyliczenia makro/IG/ŁG

**Obszar:** Personalizacja i cele dietetyczne  
**Status:** Zaimplementowane

## Opis
Przycisk „ℹ️” przy linii makroskładników otwiera okienko pokazujące dokładny rozkład kaloryczności na poszczególne składniki przepisu (z bazową wartością na jednostkę i wkładem do całości), a pod spodem — domyślnie zwiniętą — ogólną legendę tłumaczącą metodologię (B/W/T, IG, ŁG).

## Kryteria akceptacji
- Obliczenia dla konkretnego przepisu są widoczne od razu po otwarciu, bez przewijania.
- Ogólna legenda/metodologia jest zwinięta domyślnie i rozwija się dopiero po dotknięciu.
- Otwarcie tego okienka NIE rozwija nagłówka, jeśli użytkownik wcześniej ręcznie go zwinął (patrz FR-45, historia rewizji).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-13: Piąta kategoria posiłku: Deser/Przekąska

**Obszar:** Personalizacja i cele dietetyczne  
**Status:** Zaimplementowane

## Opis
Piąty, zawsze dostępny slot posiłkowy (dodatkowy, nie zastępujący pozostałych czterech), z własną pulą przepisów. Pozostawienie go niezaplanowanym nie kosztuje nic — nagłówek i planer pokazują go po prostu jako pusty wiersz jak każdy inny nieprzypisany posiłek.

## Kryteria akceptacji
- 16 dedykowanych przepisów w kategorii `deser`, każdy bezglutenowy i bez laktozy.
- Udział tej kategorii w dziennym celu (100/1500) jest wydzielony z pozostałych czterech, a nie dodany na wierzch — dzienny cel kaloryczny się nie zmienia.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-14: Skalowanie rozmiaru interfejsu (UI scale)

**Obszar:** Personalizacja i cele dietetyczne  
**Status:** Zaimplementowane

## Opis
Suwak w Ustawieniach (zakres 0.7–1.3, krok 0.05) skaluje całą aplikację przez CSS `zoom`, obejmując też elementy `position:fixed` (nagłówek, dolna nawigacja, okienka modalne). Wartość domyślna jest dobierana automatycznie na podstawie szerokości ekranu (heurystyka: 1.0 przy ≥420px, 0.75 przy ≤360px, interpolacja pomiędzy).

## Kryteria akceptacji
- Zmiana suwaka natychmiast przeskalowuje cały interfejs.
- Wartość jest zapisywana w profilu i przywracana przy kolejnym uruchomieniu.
- Aplikacja jawnie informuje, że automatyczne dopasowanie to tylko przybliżenie (strona nie ma dostępu do rzeczywistej rozdzielczości fizycznej ani ustawień 'Rozmiar wyświetlacza' systemu).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-15: Oznaczanie dania jako ugotowane, z historią i ocenami

**Obszar:** Gotowanie i historia  
**Status:** Zaimplementowane

## Opis
Przycisk „✅ Zrobione” na karcie przepisu zawsze otwiera okienko historii gotowania tego dania (a nie od razu oznacza je jako zrobione). W okienku widoczna jest lista wcześniejszych wykonań z datami i ocenami gwiazdkowymi, przycisk dodania nowego wpisu „dzisiaj” oraz możliwość usunięcia błędnego wpisu.

## Kryteria akceptacji
- Kliknięcie „Zrobione” zawsze pokazuje najpierw historię — nie oznacza dania jako zrobione bez potwierdzenia.
- Liczba wcześniejszych wykonań jest widoczna na przycisku karty (np. „✅ Zrobione (3×)”).
- Ugotowanie dania odejmuje pasujące składniki ze stanu spiżarni (jeśli jednostki się zgadzają).

## Uwagi
Zrewidowane: pierwotnie zwykłe kliknięcie od razu oznaczało danie jako zrobione, a historia/oceny wymagały długiego przytrzymania — zmieniono, bo długie przytrzymanie było mało odkrywalne.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-03): Doprecyzowano zachowanie na podstawie zgłoszonej poprawki — patrz sekcja "Uwagi" powyżej.
---

# FR-16: Sprawdzenie stanu spiżarni dla konkretnego przepisu

**Obszar:** Gotowanie i historia  
**Status:** Zaimplementowane

## Opis
Przycisk „🏺 Sprawdź stan spiżarni dla tego dania” w rozwiniętej karcie otwiera okienko stylizowane jak karta przepisu (te same zaokrąglone rogi, cień, tło), w którym każdy składnik ma osobny wiersz z wyraźnym stanem posiadania („Brak w spiżarni” / „🏺 …”) oraz dużym przyciskiem „Mam to” do oznaczenia/odznaczenia go w spiżarni, plus osobny przycisk dodania pojedynczego składnika do listy zakupów.

## Kryteria akceptacji
- Okienko wizualnie przypomina kartę przepisu, nie generyczną szufladę z drobnymi elementami.
- Każdy wiersz ma jeden, duży, łatwo trafialny przycisk zmieniający stan posiadania (min. wysokość dotykowa 34px).
- Zmiana stanu w tym okienku natychmiast odzwierciedla się w zakładce Spiżarnia.

## Uwagi
Zrewidowane w rundzie z 2026-08-03: poprzednia wersja miała stłoczony, jednowierszowy układ (tekst składnika + malutka plakietka + dwa małe przyciski obok siebie), trudny do trafienia kciukiem — przeprojektowano na czytelny układ dwuwierszowy z osobnym, dużym przyciskiem akcji.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-03): Doprecyzowano zachowanie na podstawie zgłoszonej poprawki — patrz sekcja "Uwagi" powyżej.
---

# FR-17: Ocena dania po ugotowaniu (gwiazdki)

**Obszar:** Gotowanie i historia  
**Status:** Zaimplementowane

## Opis
W historii gotowania (FR-15) każdy wpis można ocenić w skali gwiazdkowej, niezależnie od globalnej oceny lubię/nie lubię (FR-55).

## Kryteria akceptacji
- Ocena gwiazdkowa jest przypisana do konkretnego wpisu historii (daty ugotowania), nie do przepisu jako całości.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-18: Planer tygodniowy z 5 slotami posiłków dziennie

**Obszar:** Planer tygodniowy  
**Status:** Zaimplementowane

## Opis
Zakładka Planer pokazuje siedem kart dni tygodnia, każda z pięcioma wierszami odpowiadającymi kategoriom posiłków (FR-1). Każdy wiersz pozwala wybrać konkretny przepis, zobaczyć/zmienić wielkość porcji i wylosować inną propozycję.

## Kryteria akceptacji
- Każdy dzień×kategoria przechowuje niezależnie: wybrany przepis, skalę porcji, flagę „resztki”.
- Suma kalorii zaplanowanych na dany dzień jest widoczna na dole karty dnia.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-19: Wybór innego slotu posiłkowego z poziomu karty przepisu

**Obszar:** Planer tygodniowy  
**Status:** Zaimplementowane

## Opis
Przycisk „📅 Zaplanuj” na karcie przepisu otwiera okienko, w którym można wybrać zarówno dzień tygodnia, JAK I kategorię posiłku (nie tylko dzień) — np. zaplanować danie obiadowe na kolację.

## Kryteria akceptacji
- Domyślnie zaznaczona jest kategoria macierzysta przepisu, ale użytkownik może ją swobodnie zmienić przed wyborem dnia.
- Wybór kategorii i dnia odbywa się na siatce przycisków w jednej linii/gridzie, bez paska przewijania (patrz FR-2/FR-43 dot. spójności stylu list poziomych).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-20: Skalowanie wielkości porcji w planerze

**Obszar:** Planer tygodniowy  
**Status:** Zaimplementowane

## Opis
Dla każdego zaplanowanego dania można zmienić mnożnik porcji (predefiniowane kroki skali), co proporcjonalnie przelicza kalorie i makroskładniki dania widoczne w planerze i w podsumowaniu dnia.

## Kryteria akceptacji
- Zmiana skali natychmiast aktualizuje sumę kalorii dnia i wpis w nagłówku.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-21: Losowe generowanie planu — cały tydzień lub pojedynczy dzień

**Obszar:** Planer tygodniowy  
**Status:** Zaimplementowane

## Opis
Przycisk „🎲 Wygeneruj losowo cały tydzień” losuje dania dla wszystkich 7 dni × 5 kategorii z puli pasujących do profilu. Dodatkowo każda karta dnia ma własny przycisk „🎲 Losuj ten dzień”, generujący losowy plan tylko dla tego jednego dnia, bez naruszania pozostałych dni.

## Kryteria akceptacji
- Losowanie całego tygodnia i losowanie pojedynczego dnia wymagają potwierdzenia (nadpisują istniejący plan odpowiednio całego tygodnia albo tylko tego dnia).
- Pula losowania uwzględnia dopasowanie do profilu (ta sama logika co FR-11).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-22: Czyszczenie planu — cały tydzień lub pojedynczy dzień

**Obszar:** Planer tygodniowy  
**Status:** Zaimplementowane

## Opis
Oprócz generowania, każda karta dnia ma przycisk „🗑️ Wyczyść ten dzień”, kasujący zaplanowane dania tylko dla tego jednego dnia (z potwierdzeniem), niezależnie od pozostałych dni tygodnia.

## Kryteria akceptacji
- Czyszczenie jednego dnia nie wpływa na pozostałe dni.
- Operacja wymaga potwierdzenia (nieodwracalna bez ponownego zaplanowania).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-23: „Ugotuj na 2 dni” — planowanie resztek po zwiększeniu porcji

**Obszar:** Planer tygodniowy  
**Status:** Zaimplementowane

## Opis
Gdy w oknie szczegółów zaplanowanego dania porcja jest skalowana ×2 lub więcej, pojawia się przycisk pozwalający zaplanować to samo danie (w bazowej wielkości, oznaczone jako resztki 🍱) dwa dni później, bez ponownego dodawania składników do listy zakupów.

## Kryteria akceptacji
- Przycisk jest widoczny przy skali ≥2× LUB gdy danie jest rozpoznane jako nadające się na przechowanie (patrz FR-24).
- Zaplanowanie resztek nie tworzy nowego wpisu na liście zakupów.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-24: Proaktywna podpowiedź gotowania na kolejny dzień

**Obszar:** Planer tygodniowy  
**Status:** Zaimplementowane

## Opis
Dania obiadowe/kolacyjne rozpoznane po nazwie jako typowo dobrze się odgrzewające (kasze, gulasze, zapiekanki, zupy, risotto itp.) są automatycznie oznaczane jako „nadające się na 2 dni”. Jeśli taki dzień ma puste miejsce w tej samej kategorii następnego dnia, planer sam pokazuje podpowiedź „🍱 Ugotowano na więcej dni w [dzień]: [danie] — powtórzyć jako resztki?” z przyciskiem stosującym ją jednym kliknięciem.

## Kryteria akceptacji
- Wykrywanie odbywa się heurystycznie po słowach kluczowych w nazwie dania, ograniczone do kategorii Obiady/Kolacje.
- Podpowiedź pojawia się tylko dla PUSTEGO slotu następnego dnia — nigdy nie nadpisuje istniejącego wyboru bez akcji użytkownika.

## Uwagi
Uwaga projektowa (patrz sekcja 'Analiza wykluczeń' w pliku zbiorczym): FR-23 przesuwa resztki o +2 dni (po ręcznym ustawieniu skali ≥2×), a FR-24 sugeruje przeniesienie na +1 dzień (automatycznie, wg słów kluczowych). To dwa NIEZALEŻNE mechanizmy o różnym przesunięciu czasowym — nie są sprzeczne funkcjonalnie (nie nadpisują się nawzajem, użytkownik zawsze musi kliknąć, by cokolwiek się stało), ale stanowią świadomie zaakceptowaną niespójność UX, do rozważenia w przyszłości.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-25: Budowanie listy zakupów ze składników przepisów

**Obszar:** Lista zakupów  
**Status:** Zaimplementowane

## Opis
Składniki przepisu można dodać do wspólnej listy zakupów pojedynczo (przycisk 🛒 przy składniku), całym przepisem (przycisk „Dodaj do listy zakupów” na karcie) albo zbiorczo dla całego dnia/tygodnia z Planera lub bezpośrednio z zakładki Zakupy (patrz FR-58). Pozycje o tej samej kanonicznej nazwie i jednostce sumują się. Nazwa produktu przy pozycji jest wyświetlana w poprawnej polskiej odmianie dopełniaczowej pasującej do poprzedzającej ją ilości i jednostki (np. „10 g migdałów”, „4 łyżki płatków owsianych”, „150 g dżemu”) — nie w formie mianownikowej wprost z tabeli nazw kanonicznych.

## Kryteria akceptacji
- Dodanie tego samego składnika z dwóch różnych przepisów tworzy jedną pozycję z sumą ilości.
- Usunięcie przepisu z listy odejmuje tylko jego udział, nie całą pozycję (jeśli inny przepis też jej używa).
- Dla pozycji liczonych sztukowo (`unitCat==="count"`) obowiązuje odmiana liczebnikowa jak w spiżarni (FR-29: jedna/kilka/wiele sztuk), a nie dopełniacz — np. „3 jajka”, nie „3 jajek”.
- Dla pozostałych jednostek (waga, objętość, łyżki itd.) używana jest forma dopełniacza z dedykowanej tabeli (`CANON_GENITIVE`), z bezpiecznym fallbackiem do nazwy kanonicznej, jeśli dany produkt nie ma jeszcze wpisanej formy.

## Uwagi
Zrewidowane 2026-08-03: pierwotnie nazwa produktu na liście zakupów była wyświetlana wprost z tabeli nazw kanonicznych (mianownik), co dawało niegramatyczne zestawienia typu „10g migdały” zamiast „10g migdałów”. Dodano osobną tabelę form dopełniaczowych (176 produktów) i funkcję `shopDisplayName()`, która wybiera właściwą odmianę w zależności od jednostki.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-03): Dodano wymaganie poprawnej odmiany dopełniaczowej nazw produktów na liście zakupów — patrz sekcja "Uwagi".
---

# FR-26: Odhaczanie, udostępnianie i czyszczenie listy zakupów

**Obszar:** Lista zakupów  
**Status:** Zaimplementowane

## Opis
Pozycje na liście można odhaczyć jako kupione. Listę można udostępnić przez SMS/WhatsApp/skopiowanie do schowka, usunąć same odhaczone pozycje albo wyczyścić całą listę.

## Kryteria akceptacji
- Odhaczenie pozycji nie usuwa jej z listy, tylko oznacza wizualnie.
- „Usuń odhaczone” i „Wyczyść całą listę” to dwie osobne, jednoznacznie opisane akcje.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-27: Dodanie składników z całego tygodnia z Planera

**Obszar:** Lista zakupów  
**Status:** Zaimplementowane

## Opis
Przycisk w zakładce Zakupy zbiorczo dodaje do listy wszystkie składniki wszystkich dań zaplanowanych na bieżący tydzień w Planerze, z uwzględnieniem ustawionej skali porcji każdego dania.

## Kryteria akceptacji
- Dania już wcześniej dodane do listy nie są duplikowane.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-28: Śledzenie stanu spiżarni w kafelkach pogrupowanych kategoriami

**Obszar:** Spiżarnia  
**Status:** Zaimplementowane

## Opis
Zakładka Spiżarnia pokazuje kafelki produktów pogrupowane w kategorie (Nabiał, Warzywa, Owoce, Mięso/ryby/jajka, Strączki i orzechy, Pieczywo i zboża, Przyprawy, Inne). Górna połowa kafelka dodaje jednostkę, dolna odejmuje. Przyprawy śledzone są poziomem (Mało/Wystarczy/Dużo), nie liczbą sztuk.

## Kryteria akceptacji
- Każda kategoria kończy się kafelkiem „➕ Dodaj własny” do ręcznego dodania produktu spoza bazy przepisów.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-29: Odmiana gramatyczna nazw produktów w spiżarni

**Obszar:** Spiżarnia  
**Status:** Zaimplementowane

## Opis
Nazwy produktów liczonych sztukowo (np. jajka, bułki) odmieniają się poprawnie po polsku w zależności od aktualnej liczby (np. „jajko” / „jajka” / „jajek”), wg standardowych reguł liczebnikowych polskiej odmiany (1 → mianownik l.poj.; końcówka 2–4 z wyjątkiem 12–14 → forma „kilka”; pozostałe → dopełniacz l.mn.). Nazwy składników z przepisów są dodatkowo sprowadzane do formy kanonicznej (np. „300g dżemu” w przepisie → produkt „dżem” w spiżarni), a nie zapisywane w przypadkowym przypadku gramatycznym z tekstu przepisu.

## Kryteria akceptacji
- Tabela form (`one`/`few`/`many`) pokrywa produkty faktycznie liczone sztukowo, nie przyprawy śledzone poziomem.
- Zmiana ilości kafelka natychmiast przelicza wyświetlaną formę gramatyczną.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-30: Zmiana kategorii i usuwanie śledzenia kafelka spiżarni

**Obszar:** Spiżarnia  
**Status:** Zaimplementowane

## Opis
Przytrzymanie kafelka otwiera wyśrodkowane okienko z opcjami: zmiana jednostki, zmiana kategorii przypisania produktu, usunięcie śledzenia. Tekst na kafelkach jest zablokowany przed przypadkowym zaznaczeniem podczas przytrzymywania.

## Kryteria akceptacji
- Zmiana kategorii przenosi kafelek do innej sekcji bez utraty zapisanej ilości.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-31: Skanowanie kodu kreskowego produktu

**Obszar:** Spiżarnia  
**Status:** Zaimplementowane

## Opis
Przycisk w Spiżarni uruchamia podgląd z kamery urządzenia do skanowania kodu kreskowego produktu jako alternatywna metoda dodania go do śledzenia.

## Kryteria akceptacji
- Zamknięcie skanera (także przez systemowy przycisk „Wstecz” na Androidzie) zatrzymuje strumień kamery, nie zostawia go działającego w tle.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-32: Podpowiedź „🏺 masz w spiżarni” i „Pomysł na danie z ulubionych składników”

**Obszar:** Spiżarnia  
**Status:** Zaimplementowane

## Opis
Lista składników na karcie przepisu pokazuje, które pozycje są już w spiżarni. Osobny przycisk „💡 Pomysł na danie z ulubionych składników” proponuje przepis maksymalizujący liczbę użytych ulubionych/posiadanych składników.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-33: Globalny przycisk szybkiego dodania przekąski/dania z każdego miejsca

**Obszar:** Szybkie dodawanie i przekąski  
**Status:** Zaimplementowane

## Opis
Zielony przycisk „➕” w nagłówku, widoczny na każdej zakładce (w tym w Planerze), otwiera okienko dodania przekąski lub dodatkowego dania niezależnie od tego, którą część aplikacji użytkownik akurat przegląda.

## Kryteria akceptacji
- Dodana pozycja pojawia się natychmiast w dziennym bilansie kalorycznym w nagłówku i w zakładce Postępy.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-34: Automatyczne szacowanie kalorii przekąski z bazy 336 produktów

**Obszar:** Szybkie dodawanie i przekąski  
**Status:** Zaimplementowane

## Opis
Formularz dodawania przekąski przyjmuje wolny tekst (np. „1 banan”, „150g ryżu”, „prince polo”) i automatycznie szacuje kalorie na podstawie bazy `SNACK_NUTRITION_DB` (336 pozycji): dla produktów liczonych sztukowo mnoży kaloryczność jednej sztuki przez podaną liczbę, dla pozostałych przelicza z kaloryczności na 100g wg podanej lub typowej gramatury. Jeśli produkt nie zostanie rozpoznany, pole kalorii pozostaje puste do ręcznego uzupełnienia.

## Kryteria akceptacji
- Rozpoznanie NIE wymaga podania gramatury — bez niej używana jest typowa porcja.
- Podanie gramatury/liczby sztuk zawsze nadpisuje typową wartość dokładnym przeliczeniem.
- Baza pokrywa owoce, warzywa, nabiał, mięso/wędliny, pieczywo/kasze, orzechy/strączki, napoje, słodycze i popularne dania gotowe/restauracyjne.
- Każda pozycja bazy jest zweryfikowana automatycznym testem jako faktycznie rozpoznawalna po wpisaniu (nie tylko obecna w słowniku).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-35: Emotikonki przy rozpoznanych składnikach/przekąskach

**Obszar:** Szybkie dodawanie i przekąski  
**Status:** Zaimplementowane

## Opis
Gdy wpisywany tekst (w formularzu przekąski) albo nazwa składnika (na karcie przepisu) zostanie rozpoznana i ma przypisaną emotikonkę w tabeli kanonicznych informacji o produkcie, jest ona doklejana po spacji za nazwą (np. „2 jajka 🥚”).

## Kryteria akceptacji
- Emotikonka pojawia się zarówno w podpowiedzi podczas wpisywania, jak i w zapisanym wpisie dziennika, jak i na liście składników karty przepisu.
- Brak przypisanej emotikonki nie blokuje działania — nazwa po prostu zostaje bez sufiksu.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-36: Dzienny pierścień kalorii w nagłówku ze zjadanymi posiłkami

**Obszar:** Śledzenie postępów  
**Status:** Zaimplementowane

## Opis
Nagłówek pokazuje pierścień postępu dziennego spożycia kalorii względem celu, listę zaplanowanych posiłków dnia z możliwością przesunięcia wiersza w prawo, by oznaczyć posiłek jako zjedzony, oraz podsumowanie zjedzone/pozostało.

## Kryteria akceptacji
- Przesunięcie wiersza posiłku poniżej progu cofa się do pozycji wyjściowej bez oznaczenia.
- Oznaczenie/odznaczenie posiłku natychmiast aktualizuje pierścień i podsumowanie.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-37: Śledzenie nawodnienia — pełny widok i kompaktowy pasek w nagłówku

**Obszar:** Śledzenie postępów  
**Status:** Zaimplementowane

## Opis
Zakładka Postępy pokazuje interaktywny rząd 8 „szklanek” do zaznaczenia dziennego spożycia wody. W nagłówku (widocznym nawet po jego zwinięciu) pokazywany jest dodatkowo kompaktowy pasek kropelek z liczbą (np. „💧💧💧⚪⚪⚪⚪⚪ 3/8”), który po dotknięciu dodaje kolejną szklankę i jest zsynchronizowany z pełnym widokiem.

## Kryteria akceptacji
- Zmiana w jednym miejscu (nagłówek lub pełny widok) natychmiast odzwierciedla się w drugim.
- Licznik resetuje się automatycznie o północy (nowy dzień = nowy licznik).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-38: Powiadomienia z szybkimi akcjami do liczenia wody

**Obszar:** Śledzenie postępów  
**Status:** Zaimplementowane

## Opis
Opcjonalne systemowe powiadomienie Androida z przyciskami „+1 💧” / „-1 ↩️” pozwala dodawać/odejmować szklanki wody bez otwierania aplikacji. Powiadomienie nie znika automatycznie po akcji, by umożliwić kolejne dotknięcia.

## Kryteria akceptacji
- Podwójne zdarzenie tego samego dotknięcia (znany błąd niektórych przeglądarek/Androida) jest odfiltrowywane w krótkim oknie czasowym, by nie liczyć jednego dotknięcia dwa razy.
- Dziennik ostatnich 20 zdarzeń powiadomienia jest dostępny w Ustawieniach do diagnostyki.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-39: Cykliczne przypomnienie o piciu wody

**Obszar:** Śledzenie postępów  
**Status:** Zaimplementowane

## Opis
Ustawienia pozwalają skonfigurować cykliczne przypomnienie (co N minut, w oknie godzin aktywne-od/aktywne-do) z możliwością odłożenia o 15 minut albo pominięcia do następnego zaplanowanego terminu z poziomu samego powiadomienia.

## Kryteria akceptacji
- Przypomnienia działają, dopóki aplikacja pozostaje otwarta (także w tle) — po całkowitym zamknięciu przeglądarki nie są wysyłane (ograniczenie platformy, jawnie opisane w interfejsie).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-40: Śledzenie wagi z wykresem

**Obszar:** Śledzenie postępów  
**Status:** Zaimplementowane

## Opis
Zakładka Postępy pozwala wpisać dzisiejszą wagę i pokazuje historię na wykresie liniowym względem celu wagowego z profilu, wraz z listą wcześniejszych wpisów.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-41: Historia kalorii z bilansem tygodniowym

**Obszar:** Śledzenie postępów  
**Status:** Zaimplementowane

## Opis
Wykres historii dziennego spożycia kalorii wraz z podsumowaniem bilansu tygodniowego względem celu.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-42: Serie (streaks) i historia aktywności

**Obszar:** Śledzenie postępów  
**Status:** Zaimplementowane

## Opis
Aplikacja liczy serie kolejnych dni spełniających kryteria (np. pełne nawodnienie, spożycie w granicach celu) oraz prowadzi dziennik aktywności (dodania/usunięcia z listy zakupów i spiżarni). Domyślnie pokazywanych jest 20 najnowszych wpisów historii z przyciskiem „Pokaż całą historię (N)”; działa też filtr po zakresie dat, który ignoruje limit 20 i pokazuje wszystkie pasujące wpisy.

## Kryteria akceptacji
- Limit 20 dotyczy WYŁĄCZNIE domyślnego widoku bez aktywnego filtra dat — cała historia jest zawsze zachowana w danych.
- Filtrowanie po dacie i limit „20 najnowszych” nie wykluczają się — filtr dat nadpisuje limit, nie odwrotnie.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-43: Pasek filtrów i kategorii przyklejony pod nagłówkiem

**Obszar:** Nagłówek i nawigacja  
**Status:** Zaimplementowane

## Opis
Na widoku Przepisy pasek kategorii i filtrów (FR-2) jest position:sticky, zadokowany bezpośrednio pod nagłówkiem — pozostaje widoczny podczas przewijania listy niezależnie od tego, czy nagłówek jest akurat zwinięty czy rozwinięty. Wysokość nagłówka jest śledzona na bieżąco (ResizeObserver), by pasek zawsze przylegał do jego aktualnej krawędzi, także w trakcie animacji zwijania.

## Kryteria akceptacji
- Pasek nigdy nie zachodzi na treść nagłówka ani nie zostawia szpary między nimi, niezależnie od stanu zwinięcia.
- Poziomy pasek kategorii (pigułki) przewija się bez widocznego paska przewijania pod żadnym pozorem, mimo że mieści się w jednej linii (patrz historia rewizji).

## Uwagi
Zrewidowane 2026-08-03: pierwotna wersja paska pigułek kategorii nie ukrywała natywnego paska przewijania przeglądarki, co po przypięciu paska na stałe pod nagłówkiem stało się szczególnie widoczne i przeszkadzające. Naprawiono przez `scrollbar-width:none` / ukrycie paska WebKit, zachowując przewijanie dotykiem.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-03): Doprecyzowano zachowanie na podstawie zgłoszonej poprawki — patrz sekcja "Uwagi" powyżej.
---

# FR-44: Automatyczne chowanie/pokazywanie nagłówka na przewijanie (tylko Przepisy)

**Obszar:** Nagłówek i nawigacja  
**Status:** Zaimplementowane

## Opis
Na widoku Przepisy nagłówek chowa się przy przewijaniu w dół i pokazuje przy przewijaniu w górę (lub blisko samej góry strony). Na pozostałych zakładkach nagłówek jest domyślnie zwinięty i nie reaguje automatycznie na przewijanie.

## Kryteria akceptacji
- Wejście na zakładkę Przepisy zawsze resetuje nagłówek do stanu rozwiniętego i wznawia normalne zachowanie automatyczne, kasując wcześniejsze ręczne zablokowanie (patrz FR-45).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-45: Ręczne zwijanie/rozwijanie nagłówka ma pierwszeństwo nad automatyką

**Obszar:** Nagłówek i nawigacja  
**Status:** Zaimplementowane

## Opis
Nagłówek można ręcznie zwinąć/rozwinąć zarówno dedykowanym przyciskiem strzałki, jak i dotknięciem całego paska z nazwą aplikacji (poza samymi przyciskami-ikonami w rogu, które zachowują swoje własne działanie). Ręczne zwinięcie zamraża automatyczne pokazywanie-na-przewijaniu (FR-44), dopóki użytkownik sam nie rozwinie nagłówka ponownie albo nie wejdzie na zakładkę Przepisy od nowa.

## Kryteria akceptacji
- Dotknięcie ikon w rogu nagłówka (➕ szybkie dodawanie, strzałka, ⚙️ ustawienia) NIGDY nie uruchamia dodatkowo zwijania/rozwijania paska nazwy pod spodem.
- Otwarcie dowolnego okienka modalnego NIE cofa ręcznego zwinięcia nagłówka — przewijanie tła strony jest blokowane na czas otwartego okienka, by uniemożliwić 'przeciekające' przewijanie spod okienka od przypadkowego uruchamiania automatyki z FR-44 (patrz historia rewizji).

## Uwagi
Zrewidowane 2026-08-03: znaleziono i naprawiono błąd, w którym otwarcie okienka „ℹ️” (FR-12) potrafiło samoczynnie rozwinąć ręcznie zwinięty nagłówek. Przyczyna: przewijanie tła strony na ekranie dotykowym mogło nadal działać spod otwartego okienka position:fixed, co uruchamiało zwykłą (poza tym kontekstem prawidłową) logikę z FR-44. Naprawiono blokadą przewijania strony (`overflow-y:hidden` na elemencie przewijanym) na cały czas, gdy dowolne okienko modalne jest otwarte.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-03): Doprecyzowano zachowanie na podstawie zgłoszonej poprawki — patrz sekcja "Uwagi" powyżej.
---

# FR-46: Zabezpieczenie przed przypadkowym zamknięciem aplikacji (Android „Wstecz”)

**Obszar:** Nagłówek i nawigacja  
**Status:** Zaimplementowane

## Opis
Przełączanie zakładek i otwieranie dowolnego okienka modalnego rejestruje wpis w historii przeglądarki (History API). Systemowy gest/przycisk „Wstecz” na Androidzie zamyka najpierw otwarte okienko albo cofa się o jedną zakładkę, zamiast od razu zamykać całą aplikację.

## Kryteria akceptacji
- Zamknięcie okienka inną drogą (przycisk X, dotknięcie tła) usuwa odpowiadający mu wpis historii, by kolejne „Wstecz” nie 'zjadło' niczego na pusto.
- Skaner kodów kreskowych (FR-31) ma dedykowaną obsługę zamknięcia przez „Wstecz”, zatrzymującą strumień kamery zamiast samej zmiany klasy CSS.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-47: Brak migotania (FOUC) domyślnych danych profilu przy odświeżeniu

**Obszar:** Nagłówek i nawigacja  
**Status:** Zaimplementowane

## Opis
Przy odświeżeniu strony nagłówek nie pokazuje na ułamek sekundy domyślnych wartości profilu (np. „Kobieta, 37 lat…”) przed podmianą na faktyczne dane zapisanego użytkownika.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-48: Wybór motywu kolorystycznego aplikacji

**Obszar:** Wygląd i motywy  
**Status:** Zaimplementowane

## Opis
Ustawienia pozwalają wybrać jeden z ośmiu motywów wizualnych (m.in. domyślny zielony, jasny, różowy, ciemny, zbiory, cytrusowy, miętowy, jagodowa noc, polaroid), z których każdy definiuje własną paletę kolorów, pary fontów i krzywe animacji dopasowane do charakteru motywu.

## Kryteria akceptacji
- Zmiana motywu jest natychmiastowa i zapisywana w profilu.
- Kolor paska statusu przeglądarki (`theme-color`) jest zsynchronizowany z wybranym motywem.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-49: Motyw „Polaroid” z kartami w stylu odbitek natychmiastowych

**Obszar:** Wygląd i motywy  
**Status:** Zaimplementowane

## Opis
Dedykowany motyw, w którym karty przepisów mają ostre (nie zaokrąglone) rogi, gruby biały margines na dole przypominający ramkę zdjęcia z aparatu natychmiastowego, delikatne losowe przekrzywienie (prostujące się po rozwinięciu karty) oraz odręczny styl tytułu dania.

## Kryteria akceptacji
- Efekt kart dotyczy WYŁĄCZNIE kart przepisów — pozostałe elementy interfejsu (przyciski, karty w innych zakładkach) używają wyłącznie palety kolorystycznej motywu, bez zmiany kształtu.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-50: Redukcja animacji (prefers-reduced-motion)

**Obszar:** Wygląd i motywy  
**Status:** Zaimplementowane

## Opis
Gdy system użytkownika ma włączone ograniczenie animacji, aplikacja wyłącza animacje wejścia widoków, przejścia kart i okienek modalnych.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-51: Instalowalna aplikacja PWA z ikoną i manifestem

**Obszar:** PWA i działanie offline  
**Status:** Zaimplementowane

## Opis
Aplikacja spełnia wymogi Progressive Web App (manifest.json, ikony 192/512px) i może zostać zainstalowana na ekranie głównym urządzenia jak natywna aplikacja.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-52: Cache offline przez Service Worker ze strategią stale-while-revalidate

**Obszar:** PWA i działanie offline  
**Status:** Zaimplementowane

## Opis
Service Worker cache'uje zasoby aplikacji, serwując wersję z pamięci podręcznej natychmiast, a w tle sprawdzając i podmieniając na nowszą wersję z sieci, jeśli jest dostępna. Numer wersji cache (`CACHE_NAME`) jest podnoszony przy każdej istotnej zmianie treści aplikacji, by wymusić odświeżenie.

## Kryteria akceptacji
- Zmiana kontrolera Service Workera (nowa wersja przejęła kontrolę) wywołuje jednorazowe automatyczne odświeżenie strony, by nowa wersja była widoczna od razu, a nie dopiero przy drugim otwarciu.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-53: Ręczne wymuszenie aktualizacji i diagnostyka powiadomień

**Obszar:** PWA i działanie offline  
**Status:** Zaimplementowane

## Opis
Ustawienia zawierają przycisk wymuszający sprawdzenie i pobranie najnowszej wersji Service Workera oraz podgląd ostatnich 20 zdarzeń dziennika powiadomień wodnych do diagnozowania problemów ze sterowaniem przyciskami powiadomienia.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-54: Kopie zapasowe wersji plików aplikacji w repozytorium

**Obszar:** PWA i działanie offline  
**Status:** Zaimplementowane

## Opis
Przed każdą zmianą plików aplikacji (index.html, manifest.json, sw.js, ikony) ich stan sprzed edycji jest kopiowany do kolejnego, rosnącego numeru folderu w `versions/`, wraz z plikiem `RELEASE_NOTES.txt` opisującym zmiany po polsku — niezależnie od standardowej historii commitów gita, jako czytelny, ludzki punkt przywracania.

## Kryteria akceptacji
- Numeracja wersji jest ciągła i rośnie z każdą znaczącą zmianą.
- Analogiczna konwencja (osobny wpis rewizji + krótki opis zmiany) obowiązuje teraz też dla folderu `Functional requirements/` — patrz README w tym folderze.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-55: Ocenianie przepisów przesunięciem karty (lubię / nie lubię)

**Obszar:** Ocenianie i ranking przepisów  
**Status:** Zaimplementowane

## Opis
Na liście przepisów kartę można przesunąć w prawo (❤️ „lubię”) lub w lewo (👎 „nie lubię”). Gest wykorzystuje blokadę osi: dopiero przekroczenie progu ruchu w jednym kierunku „zamyka” gest na oś poziomą (ocena) albo pionową (zwykłe przewijanie listy) — więc przewijanie strony nigdy nie jest przechwytywane jako próba oceny.

## Kryteria akceptacji
- Przesunięcie poniżej progu zatwierdzenia (90px) wraca do pozycji wyjściowej bez zapisania oceny.
- Sam gest oceniania nigdy nie blokuje zwykłego przewijania listy w pionie.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-56: Duży, balonowy napis podczas oceniania przesunięciem

**Obszar:** Ocenianie i ranking przepisów  
**Status:** Zaimplementowane

## Opis
Podczas przesuwania karty w trakcie oceniania (FR-55), na środku karty pojawia się rosnący wraz z siłą przesunięcia napis „Podoba się to dla mnie!” albo „Nie podoba się to dla mnie!”, w dużej, zaokrąglonej czcionce z efektem liter jak z cienkiego, skręcanego balonu (baloniki do zwierzątek): gruby kolorowy kontur niosący kształt litery, jasny pastelowy wypełniacz, jasna smuga u góry i cień u dołu budujące wrażenie okrągłej, napompowanej rurki. Napis znika po puszczeniu karty. Domyślnie sama karta NIE zmienia koloru/obramowania podczas przesuwania — feedback wizualny niesie wyłącznie napis (patrz FR-61: styl można zmienić w Ustawieniach).

## Kryteria akceptacji
- Rozmiar napisu rośnie proporcjonalnie do siły przesunięcia (od ok. 70% do 120% skali bazowej).
- Napis nie blokuje interakcji z kartą (pointer-events wyłączone) i nie wpływa na próg zatwierdzenia oceny.
- W domyślnym stylu „Balonowa czcionka” karta pod napisem pozostaje w swoim normalnym kolorze — nie jest tintowana na zielono/czerwono.
- Alternatywny styl „Kolorowa karta” (wybierany w Ustawieniach, patrz FR-61) przywraca klasyczne kolorowe obramowanie/poświatę karty podczas przesuwania, niezależnie od napisu.

## Uwagi
Zrewidowane 2026-08-03 (v2): pierwsza wersja używała zwykłego pogrubionego tekstu z gradientowym wypełnieniem w jednolitym kolorze i ZAWSZE tintowała też całą kartę na zielono/czerwono. Na prośbę użytkownika: (1) zmieniono treść napisów, (2) przeprojektowano wygląd liter na bardziej dosłowny efekt „balonika-zwierzątka” (gruby kontur + jasny cienki wypełniacz zamiast jednolitego gradientu), (3) tintowanie całej karty przeniesiono do osobnego, opcjonalnego stylu wybieranego w Ustawieniach (FR-61), a nowym domyślnym zachowaniem jest sam napis bez kolorowania karty.

Zrewidowane 2026-08-03 (v3, treść napisów): pierwsza poprawka (v3 poniżej) zmieniła treść na „Lubię to!”/„Nie lubię!” — to nie było tym, o co prosił użytkownik. Poprawiono na dokładnie zgłoszoną treść: „Podoba się to dla mnie!” / „Nie podoba się to dla mnie!”.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-03): Doprecyzowano zachowanie na podstawie zgłoszonej poprawki — patrz sekcja "Uwagi" powyżej.
- **v3** (2026-08-03): Zmieniono treść napisów na „Lubię to!”/„Nie lubię!”, przeprojektowano na efekt "balonika-zwierzątka" i wydzielono tintowanie karty do osobnego, opcjonalnego stylu (FR-61).
- **v4** (2026-08-03): Treść napisów poprawiona na dokładnie zgłoszoną wersję „Podoba się to dla mnie!”/„Nie podoba się to dla mnie!”, po tym jak v3 nie trafiła w to, o co prosił użytkownik — patrz zaktualizowana sekcja "Uwagi".
---

# FR-57: Trwałe oznaczenie oceny i ranking sort

**Obszar:** Ocenianie i ranking przepisów  
**Status:** Zaimplementowane

## Opis
Oceniona karta zachowuje kolorowe obramowanie z boku i małą plakietkę (👍/👎), którą można dotknąć, by skasować ocenę. Osobny przełącznik „❤️” sortuje listę: najpierw lubiane, potem nieocenione („nowe”), na końcu nielubiane.

## Kryteria akceptacji
- Ocenione karty NIE znikają z listy (świadoma różnica względem klasycznego 'Tindera' z pojedynczym stosem kart) — Przepisy to przewijalna lista wielu dań, nie stos pojedynczych kart.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
---

# FR-58: Dodawanie składników z konkretnego dnia na liście zakupów

**Obszar:** Lista zakupów  
**Status:** Zaimplementowane

## Opis
Zakładka Zakupy pokazuje, obok istniejącego przycisku dodania składników z całego tygodnia (FR-25/FR-27), rząd przycisków dla każdego dnia tygodnia. Dwa pierwsze dostępne dni są etykietowane względem dzisiejszej daty jako „Dziś” i „Jutro”/„Pojutrze” (obliczane na bieżąco z rzeczywistej daty systemowej), pozostałe pokazują zwykłe nazwy dni tygodnia. Kliknięcie dodaje do listy zakupów składniki wszystkich dań zaplanowanych w Planerze na ten jeden dzień, z uwzględnieniem ustawionej skali porcji (FR-20).

## Kryteria akceptacji
- Etykiety „Dziś”/„Jutro”/„Pojutrze” zawsze odpowiadają rzeczywistemu dzisiejszemu dniowi tygodnia, nie stałemu indeksowi.
- Kliknięcie przycisku dodaje składniki TYLKO z wybranego dnia, nie z całego tygodnia.
- Dania już wcześniej dodane do listy nie są duplikowane (ta sama logika co FR-25).
- Pusty dzień (bez zaplanowanych dań) pokazuje odpowiedni komunikat zamiast cichego braku reakcji.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie polecenia użytkownika.
---

# FR-59: Wyśrodkowane okienka modalne, na pełną dostępną szerokość

**Obszar:** Nagłówek i nawigacja  
**Status:** Zaimplementowane

## Opis
Wszystkie okienka modalne w aplikacji wyświetlają się jako wyśrodkowana na ekranie karta (`modal-overlay center` + `modal-sheet center-sheet`), a nie jako arkusz wysuwający się z dołu ekranu. Dotyczy to zarówno okienek, które od początku były wyśrodkowane (np. FR-12 „Skąd te liczby?”, FR-16 stan spiżarni, skaner kodów kreskowych), jak i pozostałych, które pierwotnie były arkuszem dolnym: wybór dania dla slotu Planera, „Pomysł na danie”, dodanie własnego produktu do spiżarni, akcje na kafelku spiżarni, historia gotowania dania. Wyśrodkowana karta wykorzystuje pełną dostępną szerokość ekranu (do 600px, z 16px marginesem od krawędzi z każdej strony), a nie sztywno wąski pasek na środku.

## Kryteria akceptacji
- Każdy element `.modal-overlay` w aplikacji ma klasę `center`, a jego wewnętrzny `.modal-sheet` klasę `center-sheet`.
- Szerokość okienka skaluje się z szerokością ekranu (margines tylko 16px z każdej strony), a nie jest ograniczona do wąskiego, stałego paska pozostawiającego duże puste marginesy po bokach.
- Wyśrodkowane okienko ma ograniczoną wysokość (`max-height:82vh`) i przewija się wewnętrznie, jeśli treść jest dłuższa niż ekran — nie ucina treści bez możliwości dotarcia do niej.
- Zamykanie (przycisk ✕, dotknięcie tła, systemowe „Wstecz” — FR-46) działa tak samo niezależnie od tego, że okienko jest teraz wyśrodkowane, nie przypięte do dołu.

## Uwagi
Zrewidowane 2026-08-03 (v2): pierwotny limit szerokości wyśrodkowanych okienek (`max-width:340px`, dobrany dawniej pod krótkie okienka potwierdzeń) po ujednoliceniu wszystkich okienek do stylu wyśrodkowanego (v1 tej rewizji) okazał się zdecydowanie za wąski dla okienek z bogatszą treścią (np. wybór dania/dnia w Planerze) — zrzut ekranu użytkownika pokazał wyraźne puste marginesy po bokach. Podniesiono limit do 600px (ten sam, co szerokość treści reszty aplikacji), więc okienko realnie wykorzystuje niemal całą szerokość ekranu telefonu.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie polecenia użytkownika ujednolicenia stylu wszystkich okienek modalnych.
- **v2** (2026-08-03): Poszerzono okienka do pełnej dostępnej szerokości ekranu — patrz sekcja "Uwagi".
---

# FR-60: Warunkowe wyświetlanie „Złotych zasad przy Hashimoto i insulinooporności”

**Obszar:** Śledzenie postępów  
**Status:** Zaimplementowane

## Opis
Karta „⭐ Złote zasady przy Hashimoto i insulinooporności” w zakładce Postępy wyświetla się TYLKO wtedy, gdy przełącznik „Trzymaj się niskiego indeksu glikemicznego” (FR-9, `profile.strictLowGI`) jest włączony — czyli gdy ustawienia diety faktycznie odpowiadają kontekstowi, dla którego te zasady mają sens. Gdy użytkownik jawnie wyłączył ten przełącznik (bo np. nie ma insulinooporności/cukrzycy), karta jest ukrywana jako nierelevantna.

## Kryteria akceptacji
- Domyślny stan przełącznika (`strictLowGI: true`) pokazuje kartę — zachowanie zgodne z dotychczasowym.
- Wyłączenie przełącznika w Ustawieniach i zapisanie profilu ukrywa kartę przy najbliższym wejściu na zakładkę Postępy.
- Ponowne włączenie przełącznika przywraca widoczność karty.

## Uwagi
Zrewidowane 2026-08-03: pierwotnie karta była pokazywana zawsze, niezależnie od ustawień diety, co dla osób bez insulinooporności/cukrzycy (a więc z wyłączonym rygorem niskiego IG) nie miało sensu. Powiązane z FR-9.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie polecenia użytkownika.
---

# FR-61: Wybór stylu oceniania kart przesunięciem w Ustawieniach

**Obszar:** Wygląd i motywy  
**Status:** Zaimplementowane

## Opis
Ustawienia (karta „🎨 Wygląd aplikacji”) zawierają przełącznik dwuopcyjny „🎈 Styl oceniania kart przesunięciem”: **Balonowa czcionka** (domyślny — patrz FR-56: tylko napis, karta nie zmienia koloru) i **Kolorowa karta** (klasyczne kolorowe obramowanie/poświata całej karty podczas przesuwania, tak jak w pierwszej wersji funkcji oceniania — FR-55/FR-56 sprzed rewizji). Wybór jest zapisywany niezależnie od wybranego motywu kolorystycznego (FR-48) — działa tak samo w każdym z ośmiu motywów, w tym Polaroid (FR-49).

## Kryteria akceptacji
- Domyślna wartość to `balloon`.
- Zmiana wyboru w Ustawieniach jest natychmiastowa i zapisywana w profilu.
- Wybór jest widoczny/wpływa na kartę wyłącznie podczas gestu przesuwania (FR-55) — nie zmienia niczego innego w wyglądzie karty w spoczynku.
- Zmiana motywu kolorystycznego (FR-48) nie resetuje ani nie zmienia wybranego stylu oceniania.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie polecenia użytkownika o wydzielenie kolorowania karty jako osobnej, opcjonalnej funkcji.
---
