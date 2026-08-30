# Wymagania funkcjonalne — Dieta App

Zbiorczy dokument wszystkich wymagań funkcjonalnych aplikacji, spisany retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac (stan na 2026-08-03). Każde wymaganie ma też własny plik `FR-<numer>.md` w tym folderze — ten plik służy do przeglądania wszystkiego naraz. Zasady utrzymania i rewizji opisane są w `README.md`.

## Spis treści

### Przepisy i przeglądanie
- [FR-1: Baza przepisów podzielona na 5 kategorii posiłków](#fr-1-baza-przepisów-podzielona-na-5-kategorii-posiłków)
- [FR-2: Wyszukiwanie i filtrowanie przepisów](#fr-2-wyszukiwanie-i-filtrowanie-przepisów)
- [FR-3: Karta przepisu — widok skrócony i rozwinięty](#fr-3-karta-przepisu--widok-skrócony-i-rozwinięty)
- [FR-4: Miniatura przepisu jako emoji głównego składnika](#fr-4-miniatura-przepisu-jako-emoji-głównego-składnika)
- [FR-5: Przycisk powrotu do góry listy przepisów](#fr-5-przycisk-powrotu-do-góry-listy-przepisów)
- [FR-66: Dodawanie własnych przepisów przez użytkownika](#fr-66-dodawanie-własnych-przepisów-przez-użytkownika)
- [FR-74: Wspólna zakładka „Śniadania” na liście przepisów, osobne sloty w Planerze](#fr-74-wspólna-zakładka-śniadania-na-liście-przepisów-osobne-sloty-w-planerze)
- [FR-95: Wyszukiwanie AI (Gemini) na kartach przepisów + wyszukiwanie tylko na rozwiniętej karcie](#fr-95-wyszukiwanie-ai-gemini-na-kartach-przepisów--wyszukiwanie-tylko-na-rozwiniętej-karcie)

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
- [FR-64: Orientacyjne wartości mikroskładników (wapń, wit. D, B12) w okienku wyliczeń](#fr-64-orientacyjne-wartości-mikroskładników-wapń-wit-d-b12-w-okienku-wyliczeń)

### Gotowanie i historia
- [FR-15: Oznaczanie dania jako ugotowane, z historią i ocenami](#fr-15-oznaczanie-dania-jako-ugotowane-z-historią-i-ocenami)
- [FR-16: Sprawdzenie stanu spiżarni dla konkretnego przepisu](#fr-16-sprawdzenie-stanu-spiżarni-dla-konkretnego-przepisu)
- [FR-17: Ocena dania po ugotowaniu (gwiazdki)](#fr-17-ocena-dania-po-ugotowaniu-gwiazdki)
- [FR-93: Podpowiedzi zamienników składników w spiżarni](#fr-93-podpowiedzi-zamienników-składników-w-spiżarni)

### Planer tygodniowy
- [FR-18: Planer tygodniowy z 5 slotami posiłków dziennie](#fr-18-planer-tygodniowy-z-5-slotami-posiłków-dziennie)
- [FR-19: Wybór innego slotu posiłkowego z poziomu karty przepisu](#fr-19-wybór-innego-slotu-posiłkowego-z-poziomu-karty-przepisu)
- [FR-20: Skalowanie wielkości porcji w planerze](#fr-20-skalowanie-wielkości-porcji-w-planerze)
- [FR-21: Losowe generowanie planu — cały tydzień lub pojedynczy dzień](#fr-21-losowe-generowanie-planu--cały-tydzień-lub-pojedynczy-dzień)
- [FR-22: Czyszczenie planu — cały tydzień lub pojedynczy dzień](#fr-22-czyszczenie-planu--cały-tydzień-lub-pojedynczy-dzień)
- [FR-23: „Ugotuj na 2 dni” — planowanie resztek po zwiększeniu porcji](#fr-23-ugotuj-na-2-dni--planowanie-resztek-po-zwiększeniu-porcji)
- [FR-24: Proaktywna podpowiedź gotowania na kolejny dzień](#fr-24-proaktywna-podpowiedź-gotowania-na-kolejny-dzień)
- [FR-86: Podgląd przepisu z poziomu Planera](#fr-86-podgląd-przepisu-z-poziomu-planera)
- [FR-90: Kopiowanie planu jednego dnia na inny dzień](#fr-90-kopiowanie-planu-jednego-dnia-na-inny-dzień)
- [FR-91: Cofnij (Undo) usunięcie dania z „Dzisiejszy Planer”](#fr-91-cofnij-undo-usunięcie-dania-z-dzisiejszy-planer)
- [FR-92: Udostępnianie / eksport planu tygodnia](#fr-92-udostępnianie--eksport-planu-tygodnia)
- [FR-97: Znacznik stanu spiżarni na kartach „Dzisiejszy Planer”](#fr-97-znacznik-stanu-spiżarni-na-kartach-dzisiejszy-planer)
- [FR-103: Stopniowany gest przesuwania na kartach „Dzisiejszy Planer”](#fr-103-stopniowany-gest-przesuwania-na-kartach-dzisiejszy-planer)
- [FR-104: Gest „zrobione/zjedzone” także na kartach dni tygodnia](#fr-104-gest-zrobionezjedzone-także-na-kartach-dni-tygodnia)
- [FR-109: Przeniesienie zaplanowanego dania na inny dzień](#fr-109-przeniesienie-zaplanowanego-dania-na-inny-dzień)
- [FR-105: Dowolna wielkość zjedzonej porcji](#fr-105-dowolna-wielkość-zjedzonej-porcji)
- [FR-107: Zapamiętana wielkość porcji dla danego dania](#fr-107-zapamiętana-wielkość-porcji-dla-danego-dania)
- [FR-100: Podsumowanie odżywcze zaplanowanego tygodnia](#fr-100-podsumowanie-odżywcze-zaplanowanego-tygodnia)
- [FR-110: Realizacja tygodnia — ile z planu faktycznie zjedzone](#fr-110-realizacja-tygodnia--ile-z-planu-faktycznie-zjedzone)
- [FR-111: „Ugotuj na dwa dni” bezpośrednio z wiersza Planera](#fr-111-ugotuj-na-dwa-dni-bezpośrednio-z-wiersza-planera)

### Lista zakupów
- [FR-25: Budowanie listy zakupów ze składników przepisów](#fr-25-budowanie-listy-zakupów-ze-składników-przepisów)
- [FR-26: Odhaczanie, udostępnianie i czyszczenie listy zakupów](#fr-26-odhaczanie-udostępnianie-i-czyszczenie-listy-zakupów)
- [FR-27: Dodanie składników z całego tygodnia z Planera](#fr-27-dodanie-składników-z-całego-tygodnia-z-planera)
- [FR-106: Propozycja przeniesienia zakupów do spiżarni](#fr-106-propozycja-przeniesienia-zakupów-do-spiżarni)
- [FR-58: Dodawanie składników z konkretnego dnia na liście zakupów](#fr-58-dodawanie-składników-z-konkretnego-dnia-na-liście-zakupów)
- [FR-62: Mini kalendarzyk bieżącego tygodnia na liście zakupów](#fr-62-mini-kalendarzyk-bieżącego-tygodnia-na-liście-zakupów)
- [FR-75: Widok kafelkowy listy zakupów z brakującymi ilościami](#fr-75-widok-kafelkowy-listy-zakupów-z-brakującymi-ilościami)
- [FR-99: Wyszukiwanie na liście zakupów](#fr-99-wyszukiwanie-na-liście-zakupów)

### Spiżarnia
- [FR-28: Śledzenie stanu spiżarni w kafelkach pogrupowanych kategoriami](#fr-28-śledzenie-stanu-spiżarni-w-kafelkach-pogrupowanych-kategoriami)
- [FR-29: Odmiana gramatyczna nazw produktów w spiżarni](#fr-29-odmiana-gramatyczna-nazw-produktów-w-spiżarni)
- [FR-30: Zmiana kategorii i usuwanie śledzenia kafelka spiżarni](#fr-30-zmiana-kategorii-i-usuwanie-śledzenia-kafelka-spiżarni)
- [FR-31: Skanowanie kodu kreskowego produktu](#fr-31-skanowanie-kodu-kreskowego-produktu)
- [FR-32: Podpowiedź „🏺 masz w spiżarni” i „Pomysł na danie z ulubionych składników”](#fr-32-podpowiedź-🏺-masz-w-spiżarni-i-pomysł-na-danie-z-ulubionych-składników)
- [FR-102: Trwałe usuwanie produktu ze spiżarni](#fr-102-trwałe-usuwanie-produktu-ze-spiżarni)
- [FR-108: Ostrzeżenie, że produktu nie starczy na zaplanowane dania](#fr-108-ostrzeżenie-że-produktu-nie-starczy-na-zaplanowane-dania)

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
- [FR-83: Edycja wcześniej wpisanej wagi i historii kalorii](#fr-83-edycja-wcześniej-wpisanej-wagi-i-historii-kalorii)
- [FR-94: Śledzenie okna postu przerywanego (intermittent fasting)](#fr-94-śledzenie-okna-postu-przerywanego-intermittent-fasting)
- [FR-101: Dni kalendarzowe liczone lokalnie, nie w UTC](#fr-101-dni-kalendarzowe-liczone-lokalnie-nie-w-utc)

### Nagłówek i nawigacja
- [FR-43: Pasek filtrów i kategorii przyklejony pod nagłówkiem](#fr-43-pasek-filtrów-i-kategorii-przyklejony-pod-nagłówkiem)
- [FR-44: Automatyczne chowanie/pokazywanie nagłówka na przewijanie (tylko Przepisy)](#fr-44-automatyczne-chowaniepokazywanie-nagłówka-na-przewijanie-tylko-przepisy)
- [FR-45: Ręczne zwijanie/rozwijanie nagłówka ma pierwszeństwo nad automatyką](#fr-45-ręczne-zwijanierozwijanie-nagłówka-ma-pierwszeństwo-nad-automatyką)
- [FR-46: Zabezpieczenie przed przypadkowym zamknięciem aplikacji (Android „Wstecz”)](#fr-46-zabezpieczenie-przed-przypadkowym-zamknięciem-aplikacji-android-wstecz)
- [FR-47: Brak migotania (FOUC) domyślnych danych profilu przy odświeżeniu](#fr-47-brak-migotania-fouc-domyślnych-danych-profilu-przy-odświeżeniu)
- [FR-59: Wyśrodkowane okienka modalne, na pełną dostępną szerokość](#fr-59-wyśrodkowane-okienka-modalne-na-pełną-dostępną-szerokość)
- [FR-70: Licznik nawodnienia w nagłówku — pojedyncze klikalne kropelki](#fr-70-licznik-nawodnienia-w-nagłówku--pojedyncze-klikalne-kropelki)
- [FR-88: Planer jako pierwsza zakładka nawigacji](#fr-88-planer-jako-pierwsza-zakładka-nawigacji)

### Wygląd i motywy
- [FR-48: Wybór motywu kolorystycznego aplikacji](#fr-48-wybór-motywu-kolorystycznego-aplikacji)
- [FR-49: Motyw „Polaroid” z kartami w stylu odbitek natychmiastowych](#fr-49-motyw-polaroid-z-kartami-w-stylu-odbitek-natychmiastowych)
- [FR-50: Redukcja animacji (prefers-reduced-motion)](#fr-50-redukcja-animacji-prefers-reduced-motion)
- [FR-61: Wybór stylu oceniania kart przesunięciem w Ustawieniach](#fr-61-wybór-stylu-oceniania-kart-przesunięciem-w-ustawieniach)
- [FR-63: Motywy „Fluent” i „Kafelki” inspirowane Windows 11 / Metro](#fr-63-motywy-fluent-i-kafelki-inspirowane-windows-11--metro)
- [FR-87: Motyw „Klinika” — czcionka i układ, nie tylko kolory](#fr-87-motyw-klinika--czcionka-i-układ-nie-tylko-kolory)
- [FR-96: Wypełnianie kolorem kafelka „Pozostało” w Planerze](#fr-96-wypełnianie-kolorem-kafelka-pozostało-w-planerze)

### PWA i działanie offline
- [FR-51: Instalowalna aplikacja PWA z ikoną i manifestem](#fr-51-instalowalna-aplikacja-pwa-z-ikoną-i-manifestem)
- [FR-52: Cache offline przez Service Worker ze strategią stale-while-revalidate](#fr-52-cache-offline-przez-service-worker-ze-strategią-stale-while-revalidate)
- [FR-53: Ręczne wymuszenie aktualizacji i diagnostyka powiadomień](#fr-53-ręczne-wymuszenie-aktualizacji-i-diagnostyka-powiadomień)
- [FR-54: Kopie zapasowe wersji plików aplikacji w repozytorium](#fr-54-kopie-zapasowe-wersji-plików-aplikacji-w-repozytorium)

### Ocenianie i ranking przepisów
- [FR-55: Ocenianie przepisów przesunięciem karty (lubię / nie lubię)](#fr-55-ocenianie-przepisów-przesunięciem-karty-lubię--nie-lubię)
- [FR-56: Duży, balonowy napis podczas oceniania przesunięciem](#fr-56-duży-balonowy-napis-podczas-oceniania-przesunięciem)
- [FR-57: Trwałe oznaczenie oceny i ranking sort](#fr-57-trwałe-oznaczenie-oceny-i-ranking-sort)
- [FR-67: Ocena gwiazdkowa i komentarz przy przepisie](#fr-67-ocena-gwiazdkowa-i-komentarz-przy-przepisie)
- [FR-84: Scalenie oceniania przepisu w jeden mechanizm](#fr-84-scalenie-oceniania-przepisu-w-jeden-mechanizm)
- [FR-77: Komentarze wielu użytkowników pod przepisem, z paginacją](#fr-77-komentarze-wielu-użytkowników-pod-przepisem-z-paginacją)

### Konto i współdzielenie
- [FR-65: Własna, opcjonalna nazwa użytkownika w aplikacji](#fr-65-własna-opcjonalna-nazwa-użytkownika-w-aplikacji)
- [FR-68: Ustawienia gospodarstwa domowego i przepisów społeczności (stan przejściowy)](#fr-68-ustawienia-gospodarstwa-domowego-i-przepisów-społeczności-stan-przejściowy)
- [FR-69: Logowanie w chmurze (anonimowe, Google, e-mail i hasło)](#fr-69-logowanie-w-chmurze-anonimowe-google-e-mail-i-hasło)
- [FR-98: Kopia zapasowa danych do pliku (eksport i import)](#fr-98-kopia-zapasowa-danych-do-pliku-eksport-i-import)

### Ustawienia
- [FR-71: Zakładki w Ustawieniach — Konto, Wygląd, Przypomnienia, Ulubione](#fr-71-zakładki-w-ustawieniach--konto-wygląd-przypomnienia-ulubione)

### Ustawienia / Profil
- [FR-72: Wymuszenie ustawienia profilu przy pierwszym uruchomieniu](#fr-72-wymuszenie-ustawienia-profilu-przy-pierwszym-uruchomieniu)

### Konto i chmura
- [FR-73: Synchronizacja danych osobistych w chmurze między urządzeniami](#fr-73-synchronizacja-danych-osobistych-w-chmurze-między-urządzeniami)
- [FR-76: Przepisy społeczności oraz przeglądana lista użytkowników i profili](#fr-76-przepisy-społeczności-oraz-przeglądana-lista-użytkowników-i-profili)
- [FR-85: Zatwierdzanie przepisów społeczności z poziomu aplikacji + „Moje przepisy”](#fr-85-zatwierdzanie-przepisów-społeczności-z-poziomu-aplikacji--moje-przepisy)
- [FR-78: Pełna synchronizacja stanu z prawdziwym scalaniem zmian (3-way merge)](#fr-78-pełna-synchronizacja-stanu-z-prawdziwym-scalaniem-zmian-3-way-merge)
- [FR-79: Wylogowanie z urządzenia](#fr-79-wylogowanie-z-urządzenia)
- [FR-80: Dzień tygodnia przy składniku na liście zakupów](#fr-80-dzień-tygodnia-przy-składniku-na-liście-zakupów)
- [FR-81: Propozycja przeliczenia planu i listy zakupów po zapisaniu profilu](#fr-81-propozycja-przeliczenia-planu-i-listy-zakupów-po-zapisaniu-profilu)
- [FR-82: Widoczna wersja aplikacji w Ustawieniach](#fr-82-widoczna-wersja-aplikacji-w-ustawieniach)
- [FR-89: Reset wszystkich danych na koncie](#fr-89-reset-wszystkich-danych-na-koncie)

---

## Analiza spójności i wykluczeń

Przegląd wymagań pod kątem wzajemnych sprzeczności. Żadna z poniższych par nie okazała się logiczną sprzecznością — w każdym przypadku jeden mechanizm ma jasno określone pierwszeństwo albo oba działają w niezależnych kontekstach. Jeden punkt oznaczono jako świadomie zaakceptowaną niespójność UX (nie błąd), do rozważenia w przyszłości.

1. **FR-44 (auto-chowanie nagłówka na przewijanie) vs FR-45 (ręczne zwijanie ma pierwszeństwo).** Rozstrzygnięcie: ręczne działanie użytkownika zawsze wygrywa i zamraża automatykę aż do wejścia na zakładkę Przepisy od nowa albo ręcznego rozwinięcia. Zweryfikowano dodatkowo, że otwarcie i zamknięcie okienka modalnego (FR-12 i inne) nie powinno móc obejść tego zamrożenia — pierwotnie mogło (dwukrotnie, dwoma różnymi mechanizmami), naprawiono ostatecznie poprawką pomijającą zbędne przełączenie widoku przy zamykaniu modala przyciskiem „X” (patrz historia rewizji FR-45).
2. **FR-9 (kara za wysoki IG w wyniku dopasowania) vs FR-11 (wyświetlanie plakietki „podwyższony IG” na karcie).** Nie wykluczają się — to dwie strony tego samego przełącznika: plakietka istnieje właśnie dla osób, które świadomie wyłączyły rygor niskiego IG i chcą mimo to widzieć tę informację.
3. **FR-3 (stuknięcie rozwija kartę) vs FR-55 (przesunięcie karty ocenia danie).** Ten sam obszar dotykowy obsługuje dwa różne gesty. Rozstrzygnięcie: blokada osi ruchu (pierwsze przekroczenie progu 10px decyduje, czy to gest poziomy-ocena czy pionowy-przewijanie), a stuknięcie bez żadnego znaczącego ruchu liczy się jako rozwinięcie karty — pod warunkiem że w międzyczasie nie przewinęła się też sama strona (patrz rewizja FR-3).
4. **FR-8 (filtr bez glutenu/laktozy) vs kompletność FR-1..FR-3.** Filtr jest jawnie opisany w aplikacji jako orientacyjny (bazuje na oznaczeniach składników w tekście przepisu, nie na certyfikowanej analizie). To ograniczenie, nie sprzeczność — nie ma wymagania gwarantującego 100% trafność, więc nic tu się nie wyklucza.
5. **FR-23 („Ugotuj na 2 dni”, przesunięcie +2 dni, wymaga ręcznej skali ≥2×) vs FR-24 (proaktywna podpowiedź, przesunięcie +1 dzień, automatyczna wg słów kluczowych).** To jedyny punkt oznaczony jako **świadomie zaakceptowana niespójność UX**, nie błąd: oba mechanizmy działają niezależnie i żaden nie nadpisuje danych bez jawnej akcji użytkownika, ale różne przesunięcie czasowe (2 dni vs 1 dzień) między dwoma podobnymi w założeniu funkcjami może być mylące. Do rozważenia w przyszłej rewizji: ujednolicić przesunięcie albo jasno zróżnicować nazewnictwo obu mechanizmów.
6. **FR-42 (limit 20 wpisów historii aktywności) vs pozostałe funkcje korzystające z pełnej historii (FR-40 wykres wagi, FR-41 historia kalorii).** Nie wykluczają się — limit 20 jest wyłącznie ograniczeniem WYŚWIETLANIA jednej konkretnej listy (dziennik aktywności), nie ogranicza danych źródłowych używanych przez inne wykresy/funkcje.
7. **FR-34 (baza 336 przekąsek) vs FR-35 (emotikonki przy rozpoznanych produktach).** Częściowe pokrycie, nie sprzeczność: nie każda z 336 pozycji bazy kalorycznej ma dziś przypisaną emotikonkę w osobnej tabeli `CANON_INFO` — brak emotikonki nie blokuje rozpoznania kalorii (FR-34 działa w pełni niezależnie od FR-35), po prostu nazwa pojawia się bez sufiksu. Możliwe rozszerzenie w przyszłości.
8. **FR-60 (widoczność „Złotych zasad” tylko przy rygorze niskiego IG) vs FR-9 (przełącznik rygoru niskiego IG).** Nie wykluczają się — FR-60 to bezpośrednia konsekwencja FR-9: karta jest po prostu ukrywana, gdy FR-9 jest wyłączone. Jedno wymaganie steruje drugim, bez sprzeczności.
9. **FR-61 (wybór stylu oceniania: balonowa czcionka / kolorowa karta) vs FR-48 (wybór motywu kolorystycznego).** Nie wykluczają się — to dwa niezależne ustawienia. FR-61 celowo działa tak samo w każdym z jedenastu motywów z FR-48, w tym Polaroid (FR-49) i Kafelki (FR-63).
10. **FR-49 (kształt kart Polaroid) vs FR-63 (kształt kart motywu Kafelki).** Nie wykluczają się — oba wymagania modyfikują kształt/strukturę kart przepisów, ale są aktywne wyłącznie w ramach własnego, wzajemnie wykluczającego się wyboru motywu (FR-48 pozwala wybrać tylko jeden motyw naraz), więc nigdy nie są aktywne jednocześnie.
11. **FR-62 (kalendarzyk tygodnia na liście zakupów) vs FR-58 (przyciski dodawania per-dzień).** Nie wykluczają się — to komplementarne, niezależne elementy tego samego widoku: FR-58 to akcja (dodawanie), FR-62 to wyłącznie odczyt/wizualizacja aktualnego stanu listy względem planu. Zmiana wywołana przez FR-58 natychmiast odświeża wskaźniki z FR-62.
12. **FR-99 (wyszukiwanie na liście zakupów) vs FR-26 (udostępnianie i czyszczenie listy).** Potencjalna pułapka: filtr zawęża to, co widać, więc akcje działające „na liście” mogłyby zacząć działać na przefiltrowanym podzbiorze bez ostrzeżenia. Rozstrzygnięcie: filtr jest wyłącznie sposobem PATRZENIA na listę i nie wpływa na żadną akcję — `buildListText()` (udostępnianie/kopiowanie) eksportuje całą listę, a „Usuń odhaczone”/„Wyczyść całą listę” działają na pełnym zbiorze, niezależnie od aktywnej frazy. Zweryfikowane testem: przy aktywnym filtrze zawężającym widok do 1 pozycji udostępnianie nadal zwraca wszystkie 5.
13. **FR-99 (wyszukiwanie na liście zakupów) vs FR-75 (widok kafelkowy listy).** Nie wykluczają się — oba widoki renderują ten sam `state.shopping`, więc filtr jest stosowany raz, wspólnie dla obu, i przełączenie widoku przy aktywnej frazie nie gubi filtrowania.
14. **FR-98 (kopia zapasowa do pliku) vs FR-73 (synchronizacja z chmurą) i FR-89 (reset danych konta).** Nie wykluczają się, ale świadomie się pokrywają: zakres eksportu to dokładnie `SYNCED_STATE_KEYS`, czyli ten sam zbiór, który wędruje do chmury — kopia obejmująca cokolwiek innego rozjeżdżałaby się z tym, co przenosi zalogowanie na drugim urządzeniu. Import celowo przechodzi przez to samo `refreshUiAfterSync()`, co dane przychodzące z chmury, żeby nie powstała druga, równoległa ścieżka „przeładuj wszystko po podmianie stanu”. Względem FR-89 kopia jest zabezpieczeniem: reset konta jest nieodwracalny po stronie chmury, ale plik zapisany wcześniej pozwala odtworzyć dane.
15. **FR-101 (lokalne dni kalendarzowe) vs FR-83 (edycja wcześniejszych dni) i FR-38 (licznik wody z powiadomień).** Względem FR-83: świadomie NIE ma migracji danych zapisanych przed naprawą — wpisu źle przypisanego przez błąd UTC nie da się odróżnić od wpisu, który użytkownik celowo przypisał do wcześniejszego dnia korzystając z FR-83, więc „naprawianie” historii byłoby zgadywaniem na danych, których nie wolno ruszać. Względem FR-38: Service Worker i aplikacja MUSZĄ liczyć klucz dnia identycznie, bo licznik wody jest zapisywany w jednym miejscu, a odczytywany w drugim — obie implementacje zostały zmienione w tej samej turze i celowo mają w kodzie wzajemne odwołania, żeby nie rozjechały się przy kolejnej zmianie.

16. **FR-103 (stopniowany gest na kartach Planera) vs FR-15 (oznaczanie dania jako ugotowane) i FR-36 (oznaczanie jako zjedzone).** Nie wykluczają się — FR-103 to wyłącznie SKRÓT do tych samych dwóch zapisów, nie osobny stan. Krótkie przesunięcie w prawo woła dokładnie ten sam kod co przycisk „✅ Zrobione dzisiaj” z karty przepisu (wpis w historii gotowania + odjęcie ze spiżarni), a długie — ten sam `setEaten` co checkbox w Postępie. Rozstrzygnięcia: (a) powtórzone „zrobione” tego samego dnia jest ignorowane, żeby skrót nie mógł odjąć składników dwa razy; (b) „zjedzone” NIE oznacza automatycznie „zrobione” — można zjeść coś, czego się nie gotowało, więc te dwa stany zostają niezależne; (c) połowa porcji jest z punktu widzenia FR-36 nadal „zjedzone” (`done:true`), tylko z polem `portion`, więc wszystkie starsze odczyty stanu (checkbox w Postępie, seria dni, podsumowania) działają bez zmian, a tylko sumowanie kcal zna ułamek.
17. **FR-102 (trwałe usuwanie produktu ze spiżarni) vs FR-28 (kafelki wyliczane ze wszystkich przepisów).** Napięcie realne: FR-28 celowo NIE przechowuje listy kafelków, tylko wylicza ją z bazy przepisów, więc „usunięcie” kafelka nie ma czego skasować. Rozstrzygnięcie: FR-102 nie zmienia FR-28, tylko dokłada listę wykluczeń (`pantryHidden`) filtrowaną w jednym miejscu przy budowaniu listy kafelków. Skutek uboczny do zapamiętania: jeśli w przyszłości dojdzie przepis ze składnikiem, który użytkownik kiedyś ukrył, kafelek NIE pojawi się — to celowe (wybór użytkownika wygrywa z bazą przepisów), a „↩️ Przywróć usunięte produkty” jest wyjściem awaryjnym.
18. **FR-103 (znacznik „zrobione dzisiaj” na karcie Planera) vs FR-101 (dni liczone lokalnie).** Wpisy historii gotowania zapisują pełny znacznik czasu `toISOString()`, którego część datowa to dzień UTC — a `todayStr()` od FR-101 zwraca dzień LOKALNY. Rozstrzygnięcie: `cookedTodayIndex()` NIE porównuje pierwszych 10 znaków znacznika, tylko parsuje go i formatuje lokalnie (`localDateStr`), żeby „dzisiaj” znaczyło tu to samo co we wszystkich innych kluczach dat. Bez tego plakietka „🍳 Zrobione” gasłaby i zapalała się o północy czasu UTC, czyli o 01:00/02:00 w Polsce.

19. **FR-106 (propozycja przeniesienia zakupów do spiżarni) vs FR-15 (oznaczanie dania jako ugotowane).** Nie wykluczają się, ale łatwo je pomylić — i pierwotny pomysł na FR-106 mylił je wprost. Odhaczenie listy zakupów znaczy „kupiłem”, a nie „ugotowałem”: oznaczenie „zrobione” ODJĘŁOBY ze spiżarni to, co użytkownik właśnie kupił. Rozstrzygnięcie: FR-106 wyłącznie NAPEŁNIA spiżarnię, a ugotowanie zostaje tam, gdzie było — w geście z FR-103. Obie funkcje spotykają się dopiero na spiżarni: FR-106 ją wypełnia, FR-103 z niej odejmuje.
20. **FR-107 (zapamiętana porcja) vs FR-105 (dowolna porcja).** FR-107 nie dokłada żadnego nowego zapisu — czyta pole `portion`, które FR-105 i tak zapisuje. Rozstrzygnięcie kolejności: wartość już zapisana na dziś ma pierwszeństwo przed nawykiem (użytkownik poprawia konkretny wpis, a nie pyta o statystykę), a nawyk przed całą porcją. Nawyk nie jest zgłaszany przy jednym wystąpieniu ani gdy wynosi całą porcję — inaczej podpowiedź pojawiałaby się przy każdym daniu i przestałaby cokolwiek znaczyć.

21. **FR-108 (ostrzeżenie o braku) vs FR-16 (znacznik „🏺 N/M w spiżarni”).** Odpowiadają na dwa różne pytania i dlatego nie zastępują się nawzajem: FR-16 mówi o OBECNOŚCI składnika („czy jest jakikolwiek ryż”), FR-108 o ILOŚCI („czy starczy go na to, co zaplanowane”). Produkt może być policzony przez FR-16 jako „mam” i jednocześnie zgłoszony przez FR-108 jako niewystarczający — to nie sprzeczność, tylko dwa poziomy szczegółowości tej samej informacji.
22. **FR-108 vs FR-15/FR-103 (odejmowanie ze spiżarni po ugotowaniu).** Muszą być czytane razem, inaczej ostrzeżenie kłamie. Danie oznaczone jako zrobione JUŻ odjęło swoje składniki, więc policzenie go nadal jako „potrzebne” pokazałoby brak dokładnie po zjedzeniu obiadu. Rozstrzygnięcie: FR-108 pomija posiłki zrobione w swoim dniu, a cofnięcie oznaczenia (FR-103) przywraca i stan spiżarni, i ostrzeżenie.
23. **FR-108 vs FR-106 (propozycja przeniesienia zakupów do spiżarni).** Domykają tę samą pętlę z dwóch stron: FR-106 wypełnia spiżarnię tym, co kupione, a FR-108 mówi, kiedy tego przestaje wystarczać. Zaakceptowanie propozycji FR-106 potrafi więc wyciszyć ostrzeżenie FR-108 bez żadnego dodatkowego kroku — i tak ma być.

24. **FR-109 (przeniesienie dania) vs FR-90 (kopiowanie planu dnia).** Łatwo pomylić, bo obie przestawiają tydzień, ale robią co innego: kopiowanie POWIELA cały dzień i świadomie NADPISUJE cel (użytkownik potwierdza to w oknie), FR-109 przenosi JEDEN slot i nigdy nie nadpisuje — zajęty dzień zamienia się z nim miejscami. Różnica jest zamierzona: przy kopiowaniu całego dnia strata jest widoczna od razu (cały dzień się zmienia), przy pojedynczym slocie nie byłoby po niej śladu.
25. **FR-109 vs FR-103/FR-104 (gest zrobione/zjedzone).** Przycisk „📅” leży na karcie, która jest też polem gestu, więc jest — tak jak „✕” — wyłączony ze startu przeciągnięcia; przeciąganie od małej, jednoznacznej kontrolki nic nie znaczy. Uwaga na przyszłość: przeniesienie dania NIE przenosi wpisów „zjedzone”/„zrobione”, bo te są przypisane do konkretnej DATY, a nie do slotu — przesunięcie planu na inny dzień nie zmienia tego, co się danego dnia zjadło.
26. **FR-110 (realizacja tygodnia) vs FR-100 (podsumowanie zaplanowanego tygodnia).** Stoją na jednej karcie i celowo mierzą dwie różne rzeczy: FR-100 uśrednia po dniach ZAPLANOWANYCH (bez względu na datę), FR-110 liczy wyłącznie dni DO DZIŚ. Gdyby ujednolicić zakresy, jedna z tych liczb przestałaby znaczyć to, po co powstała — średnia stałaby się chwiejna na początku tygodnia, albo realizacja zawsze niska.
27. **FR-111 vs FR-23 vs FR-24 (trzy drogi do „ugotuj na dwa dni”).** Wszystkie trzy zapisują dokładnie ten sam stan (`isLeftover=true`, bazowa skala porcji) i żadna nie nadpisuje cudzego wyboru bez akcji użytkownika — nie są ze sobą sprzeczne, różnią się tylko wyzwalaczem i celem: FR-23 ręczny/skala≥2×/`dzień+2`, FR-24 automatyczny/słowo kluczowe/`dzień+1`, FR-111 ręczny/bez warunków wstępnych/dowolny dzień z listy. Zobacz też FR-111 vs FR-109 niżej.
28. **FR-111 vs FR-109 (dwa różne pickery dnia).** Współdzielą tę samą listę-dni-z-etykietami jako wzorzec UI, ale robią coś przeciwnego: FR-109 PRZENOSI (zamienia, gdy dzień zajęty), FR-111 DODAJE (i dlatego dzień zajęty jest tam nieklikalny, nie zamienny) — nigdy nie modyfikują tego samego wpisu jednocześnie.

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
- **Android (od 2026-08-11, v2):** pigułki kategorii żyją we WŁASNYM,
  osobnym zwijanym panelu, niezależnym od nagłówka (FR-44/45) — domyślnie
  zwinięty (pokazuje tylko aktualnie wybraną kategorię w jednej linii z
  strzałką ⌄/⌃), rozwijany własnym dotknięciem, dokładnie jak nagłówek się
  zwija/rozwija, ale NIE dzieli z nim stanu ani zachowania przy
  przewijaniu — panel kategorii zostaje osiągalny nawet gdy nagłówek
  automatycznie się zwinie przy scrollu (FR-44). Web bez zmian (pigułki
  kategorii nadal częścią tego samego zwijanego paska co pole wyszukiwania
  i pozostałe filtry) — świadoma rozbieżność, patrz `android/PARITY.md`.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-11, Android): Na życzenie użytkownika, pigułki kategorii
  wydzielone z głównego zwijanego paska (współdzielonego z nagłówkiem
  wody/kalorii przez `headerExpanded`) do własnego, niezależnego panelu —
  patrz zaktualizowane kryteria akceptacji. `RecipeListScreen.kt`: nowy
  `categoryPanelExpanded` (domyślnie `false`), osobny od `headerExpanded`.
  `./gradlew :app:assembleDebug :app:testDebugUnitTest :logic:test`
  przechodzi. **Nie zweryfikowane na żywo** — wymaga sprawdzenia w Android
  Studio.

---

# FR-2: Wyszukiwanie i filtrowanie przepisów

**Obszar:** Przepisy i przeglądanie  
**Status:** Zaimplementowane

## Opis
Nad listą przepisów znajduje się pole wyszukiwania (po nazwie dania i składnikach) oraz zestaw przełączników: tylko ulubione przepisy (⭐), tylko z ulubionymi składnikami (🌟), tylko dania możliwe do zrobienia z tego, co jest w spiżarni (🏺), sortowanie wg dopasowania do profilu (🎯), sortowanie rankingowe wg oceny (❤️), sortowanie wg oceny gwiazdkowej (🏆), tylko przepisy dodane przez użytkowników (🧑‍🍳) oraz rozwijana lista z progiem oceny gwiazdkowej („Dowolna ocena” / „★ 3+” / „★ 4+” / „★ 5”).

Przełącznik „tylko przepisy dodane przez użytkowników” pokazuje pozycje z `state.myRecipes` (własne przepisy, przycisk „➕ Dodaj swój przepis”, patrz FR-66) ORAZ zatwierdzone przepisy społeczności dodane przez innych użytkowników (`source: "community"`, patrz FR-76), z pominięciem 229 wbudowanych przepisów aplikacji.

Filtr progu oceny pokazuje wyłącznie przepisy, których ocena gwiazdkowa (⭐ „Oceń i skomentuj”, patrz FR-67) jest równa lub wyższa od wybranego progu; przepisy bez żadnej oceny są wtedy ukrywane. Dopóki synchronizacja danych między urządzeniami (`docs/FIREBASE_MIGRATION_PLAN.md`) nie jest ukończona, ocena widoczna w filtrze to ocena własna z tego urządzenia — po dokończeniu synchronizacji stanie się to bez zmian w tym mechanizmie średnią oceną od wszystkich użytkowników (ten sam kształt danych, patrz komentarz przy `recipeReviews` w kodzie).

## Kryteria akceptacji
- Wpisanie tekstu w polu wyszukiwania zawęża listę w czasie rzeczywistym.
- Przełączniki są niezależne i można je łączyć (np. tylko ulubione + sortowanie wg dopasowania, albo tylko przepisy użytkowników + próg oceny — łączenie tych dwóch pokaże pustą listę, jeśli własny przepis nie ma jeszcze żadnej oceny).
- Pasek filtrów jest przyklejony (sticky) pod nagłówkiem i zawsze widoczny podczas przewijania (patrz FR-43).
- Wybranie progu oceny „Dowolna ocena” wyłącza ten filtr całkowicie.
- **Android (od 2026-08-11, v4):** pole wyszukiwania jest domyślnie
  kompaktowe — sama ikona „🔍” (plus aktualny termin wyszukiwania i „✕” do
  wyczyszczenia, TYLKO gdy wyszukiwanie jest aktywne). Dotknięcie ikony
  (lub aktywnego terminu) otwiera okno z polem tekstowym u góry (te same
  kryteria dopasowania co dotychczas — nazwa dania LUB dowolny fragment
  tekstu składnika) oraz przewijalną listą WSZYSTKICH unikalnych nazw
  składników występujących w znanych przepisach, filtrowaną na bieżąco
  wpisywanym tekstem — dotknięcie pozycji na liście od razu wyszukuje po
  tym dokładnym składniku. Web bez zmian (pełnowymiarowe pole zawsze
  widoczne) — świadoma rozbieżność, patrz `android/PARITY.md`.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-08): Dodano przełącznik „tylko przepisy użytkowników” (🧑‍🍳) i filtr progu oceny gwiazdkowej, na życzenie użytkownika ("rozbuduj funkcję filtrowania, dodaj opcje żeby wyświetlać tylko przepisy dodane przez użytkowników albo tylko z określoną oceną dania ustaloną na podstawie ocen od różnych użytkowników").
- **v3** (2026-08-08): Przełącznik „tylko przepisy użytkowników” objął też zatwierdzone przepisy społeczności dodane przez INNYCH użytkowników, po wdrożeniu FR-76 (wcześniej pokazywał tylko własne przepisy).
- **v4** (2026-08-11, Android): Na życzenie użytkownika, pole wyszukiwania
  zastąpione kompaktową ikoną „🔍” + oknem dropdown z listą składników —
  patrz zaktualizowane kryteria akceptacji. Nowe
  `RecipePantryMatching.uniqueIngredientNames()` (logic, z testami JUnit)
  + `RecipeViewModel.uniqueIngredientNames()` + `IngredientSearchDialog`
  w `RecipeListScreen.kt`. `./gradlew :app:assembleDebug
  :app:testDebugUnitTest :logic:test` przechodzi. **Nie zweryfikowane na
  żywo** — wymaga sprawdzenia w Android Studio.
- **v5** (2026-08-11, Android): Użytkownik poprosił o "filtrowanie po
  daniach, które są zaznaczone jako podoba się to dla mnie" — dane już
  istniały (ocena gwiazdkowa ≥4 JEST "polubione", ten sam próg co
  obramowanie karty i etykieta przesunięcia „❤️ Podoba się to dla mnie!”),
  ale nie było do tego dedykowanego skrótu pod tą nazwą — próg oceny
  („★4+”/„★5”) to jedno-wyborowe pole radio, nie przełącznik. Dodano
  osobny, niezależny przełącznik „❤️ Podoba się” (Android) filtrujący
  `stars >= 4`, obok „⭐ Ulubione”. Web bez zmian (ma tę samą funkcjonalność
  pod postacią progu oceny „★4+”) — świadoma rozbieżność w samej
  PREZENTACJI (nie w danych), patrz `android/PARITY.md`. `./gradlew
  :app:assembleDebug :app:testDebugUnitTest :logic:test` przechodzi. **Nie
  zweryfikowane na żywo** — wymaga sprawdzenia w Android Studio.
- **v6** (2026-08-28, Web only): Wyszukiwanie przestało być wrażliwe na
  polskie znaki diakrytyczne. **Realny błąd, znaleziony przypadkiem** przy
  dodawaniu wyszukiwania na liście zakupów (FR-99): filtr przepisów
  porównywał surowe, tylko zmniejszone do małych liter napisy, więc
  wpisanie „roszponka” dawało ZERO wyników mimo istniejącego przepisu
  „Omlet z awokado, pomidorkami i roszponką” — potwierdzone pomiarowo na
  całej bazie 229 przepisów przed poprawką. To szczególnie dotkliwe na
  telefonie, gdzie każdy ogonek wymaga przytrzymania klawisza, czyli
  dokładnie wtedy, gdy ludzie je pomijają. Naprawione przez zastosowanie
  istniejącej funkcji `foldDiacritics()` (używanej już wcześniej przy
  dopasowywaniu kanonicznych nazw składników) po obu stronach porównania —
  zarówno do nazwy przepisu, jak i do listy składników. Zweryfikowane na
  żywo (headless Chromium) w obie strony: zapytanie bez ogonków znajduje
  przepis z ogonkami i odwrotnie. CACHE_NAME→v110, `versions/v110/`.
- **v7** (2026-08-28, Web only): Pole wyszukiwania przepisów dostało
  przycisk „✕” czyszczący frazę, widoczny tylko gdy coś jest wpisane —
  dla spójności z wyszukiwaniem na liście zakupów (FR-99), które dostało
  go przy okazji powstania. Na telefonie opróżnienie pola inaczej oznacza
  zaznacz-wszystko-i-usuń. Zweryfikowane na żywo: przycisk ukryty na
  starcie, pojawia się po wpisaniu frazy (1 pasujący przepis), a
  kliknięcie czyści pole i przywraca pełną listę (106 kart w tej
  kategorii). CACHE_NAME→v113, `versions/v113/`.


- **v8** (2026-08-29, PORT NA ANDROIDA): odporność na polskie znaki
  diakrytyczne dodana też po stronie Kotlina. Audyt z 2026-08-28 wykazał ten
  sam błąd w CZTERECH miejscach (wyszukiwarka przepisów w `RecipeBrowsing`,
  podpowiedzi przekąsek w `MainActivity`, filtr składników w
  `RecipeListScreen`, filtr ulubionych składników w `SettingsScreen`) —
  wszystkie wiernie przeniesione z weba razem z błędem. Wspólna funkcja
  (`PolishText`) zamiast czterech poprawek, żeby piąte pole wyszukiwania
  dostało to za darmo. Świadomie NIE przez `java.text.Normalizer` z NFD:
  „ł” nie ma formy rozłożonej, więc ta droga cicho zostawia najczęstszy
  polski znak — czyli dokładnie tę literę, która jest w „brokuł”, „żółty”
  i „masło”.

---

# FR-3: Karta przepisu — widok skrócony i rozwinięty

**Obszar:** Przepisy i przeglądanie  
**Status:** Zaimplementowane

## Opis
Każdy przepis wyświetlany jest jako karta z nazwą, czasem przygotowania, kalorycznością i skrótowymi znacznikami (np. podwyższony IG, dopasowanie do celu). Domyślnie karta jest zwinięta. Rozwijanie jest jednoetapowe (patrz v10 — do 2026-08-23 było dwuetapowe): jedno wyraźne, stacjonarne stuknięcie w zwiniętą kartę od razu rozwija pełną listę składników, sposób przygotowania i przyciski akcji, po czym karta automatycznie się pozycjonuje na ekranie. Zwinięcie rozwiniętej karty jest natomiast możliwe WYŁĄCZNIE przez rozwinięcie innej karty (co automatycznie zwija poprzednią) — ponowne dotknięcie tej samej, już rozwiniętej karty nie robi nic (patrz v8: dawniej zamykało jednym stuknięciem, uznane za uciążliwe).

Rozwinięta treść karty na Androidzie ma wyglądać jak na webie w większości szczegółów (pogrubiony pasek czasu/kalorii/dopasowania i przyciski akcji, ciasne jednowierszowe wiersze składników bez wymuszonego minimalnego rozmiaru dotykowego Material na każdym wierszu — patrz v7), ale ze świadomymi, udokumentowanymi różnicami na wyraźną prośbę użytkownika: etykiety sekcji „Składniki”/„Przygotowanie” WIDOCZNE tylko na Androidzie (web ich nie ma, patrz v7 vs v9), i miniaturka dania po PRAWEJ stronie karty na Androidzie tak, żeby tytuł zaczynał się od lewej krawędzi jak na webie (patrz v9).

## Kryteria akceptacji
- Karta w stanie zwiniętym pokazuje tylko nagłówek i podstawowe metadane — WYŁĄCZNIE przyciski „✅ Zrobione” i „📅 Zaplanuj” (i „🗑️ Usuń” dla własnych przepisów) są widoczne od razu; przycisk „🛒 Dodaj do listy zakupów” pojawia się dopiero po rozwinięciu karty (patrz v6).
- Rozwinięcie karty odbywa się WYŁĄCZNIE przez wyraźne, stacjonarne stuknięcie — nie przez przypadkowe zatrzymanie przewijania listy (patrz historia rewizji poniżej, FR-44 i v10 — ta ochrona musi działać niezależnie od tego, czy rozwinięcie jest jedno- czy dwuetapowe).
- Jedno stuknięcie zwiniętej karty (v10 — od 2026-08-23; wcześniej v4-v9 wymagały dwóch stuknięć) od razu ją rozwija.
- Dotknięcie JUŻ rozwiniętej karty (jej samej, nie innej) nic nie robi — nie zwija jej (v8).
- Tylko jedna karta na liście może być rozwinięta jednocześnie.
- Po rozwinięciu karty ekran automatycznie przewija się tak, żeby cała rozwinięta karta wylądowała na środku widocznego obszaru — użytkownik nie musi ręcznie doprzewijać, żeby zobaczyć składniki i sposób przygotowania. Przewinięcie następuje PO zakończeniu animacji rozwijania karty (nie w trakcie), żeby wyśrodkowanie trafiało na docelową, już powiększoną wysokość karty, a nie na jej wysokość sprzed rozwinięcia. Jeśli rozwinięta karta jest WYŻSZA niż widoczny obszar ekranu, wyśrodkowanie zastępowane jest wyrównaniem górnej krawędzi karty do góry widoku (samo wyśrodkowanie ucinałoby wtedy tytuł/początek karty poza ekranem).

## Uwagi
Zrewidowane w rundzie z 2026-08-03: pierwotna wersja pozwalała, by dotknięcie kończące przewijanie listy (bardzo mały ruch palca przy jednoczesnym przewinięciu strony przez inercję) było błędnie odczytane jako stuknięcie i rozwijało kartę, co powodowało 'skakanie' ekranu. Naprawiono porównując pozycję przewijania strony w momencie dotknięcia i puszczenia — jeśli strona przewinęła się w tym czasie, gest NIE liczy się jako stuknięcie, nawet jeśli sam palec poruszył się nieznacznie. Patrz też FR-44.

Ten mechanizm (porównanie pozycji scrolla na dotknięciu vs puszczeniu) jest,
i pozostaje po v10, JEDYNĄ ochroną przed przypadkowym rozwinięciem —
dwuetapowe otwieranie wprowadzone w v4 było osobną, niezależną decyzją UX
(wygoda przeglądania), nie mechanizmem antyprzypadkowym, więc jego usunięcie
w v10 nie osłabia ochrony przed przypadkowym dotknięciem podczas scrolla.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-03): Doprecyzowano zachowanie na podstawie zgłoszonej poprawki — patrz sekcja "Uwagi" powyżej.
- **v3** (2026-08-08): Dodano automatyczne wyśrodkowywanie rozwiniętej karty na ekranie, na życzenie użytkownika ("karta z przepisem na którą klikniemy [powinna] wyśrodkowywać się na ekranie... użytkownik nie musi sam jej przesuwać").
- **v4** (2026-08-11): Rozbito otwieranie na dwa etapy, na wyraźną prośbę użytkownika ("zmień żeby wysrodkowywalo kafelek dopiero po kliknięciu na niego a dopiero po drugim kliknięciu żeby go rozwijało i wysrodkowywalo albo jak się nie mieści na ekranie to żeby był wyświetlony od góry") — pierwsze stuknięcie tylko centruje, drugie rozwija; dodano też wariant "wyrównaj do góry" dla kart wyższych niż ekran, zamiast zawsze centrować (co ucinałoby górę zbyt wysokiej karty). Zamknięcie nadal jednym stuknięciem. Zaimplementowane na web w tej rundzie; port na Android odłożony (patrz `android/PARITY.md`).
- **v5** (2026-08-23, Android): v4 doportowane na Android, na życzenie użytkownika ("zacznij ... fr-3"). `RecipeListScreen.kt`'s nowy `pendingCenterRecipeId` (port web'owego `pendingCenterCard`) — pierwsze stuknięcie zwiniętej karty ją centruje bez rozwijania, drugie stuknięcie TEJ SAMEJ karty rozwija; stuknięcie innej karty pomiędzy resetuje na nową kartę. Nowa wspólna funkcja `centerOrTopAlignScrollDelta` (port `scrollCardIntoView`) — wyrównanie do góry zamiast centrowania, gdy rozwinięta karta jest wyższa niż widoczny obszar; użyta zarówno przy centrowaniu (krok 1), jak i przy rozwinięciu (krok 2, wcześniej brakowało tego wariantu nawet w istniejącym centrowaniu). Zamknięcie nadal jednym stuknięciem. `./gradlew :app:compileDebugKotlin` przechodzi; zweryfikowane bezpośrednio na emulatorze (Medium_Phone_API_35): pierwsze stuknięcie zostawiło kartę zwiniętą, drugie stuknięcie w to samo miejsce w pełni ją rozwinęło, kolejne pojedyncze stuknięcie natychmiast ją zwinęło.
- **v6** (2026-08-23, Web + Android): Użytkownik zgłosił, że przycisk „🛒 Dodaj do listy zakupów” — dotąd zawsze widoczny, także na zwiniętej karcie — "przypadkowo się klika, jak chce się rozwinąć [kartę]", bo siedział tuż pod nagłówkiem, w tym samym miejscu co obszar tap-to-expand. Naprawione przeniesieniem przycisku z zawsze-widocznego paska akcji do wnętrza rozwijanej treści karty (widoczny WYŁĄCZNIE po rozwinięciu, na samym dole, obok nowego widżetu stanu spiżarni — patrz FR-16/v4). „✅ Zrobione”/„📅 Zaplanuj”/„🗑️ Usuń” zostają bez zmian w zawsze-widocznym pasku — użytkownik zgłosił problem wyłącznie z przyciskiem zakupów. Web: `.add-btn` przeniesiony z `.card-actions` do `.card-collapsible-inner` w `recipeCard()` (`index.html`). Android: `TextButton` przeniesiony z `RecipeCard` do `RecipeCardBody`'s `if (expanded)` (`RecipeListScreen.kt`), nowe parametry `isAddedToShopping`/`onToggleAddToShopping`. Zweryfikowane na żywo na obu platformach: zwinięta karta nie pokazuje już przycisku zakupów, rozwinięcie odsłania go na dole razem z widżetem stanu spiżarni.
- **v7** (2026-08-23, Android): Po v6 użytkownik przysłał zrzuty ekranu z obu platform i zgłosił, że mimo strukturalnej zgodności (oba miejsca na miejscu) karty dalej "są dwiema różnymi kartami" — Androidowa wyglądała mniej kompaktowo, z mniej wyrazistą (niepogrubioną) typografią niż webowa, którą wskazał jako wzorzec ("bardziej podoba mi się wygląd karty w aplikacji PWA... jest bardziej kompaktowa, ma ładniejsze czcionki i pogrubienia"). Zbadane źródło: (1) `RecipeCardBody` miała własne nagłówki „Składniki”/„Przygotowanie”, których web NIE ma — usunięte. (2) Każdy wiersz składnika owijał gwiazdkę-przełącznik w `TextButton`, który (mimo `contentPadding` ustawionego na zero) i tak wymusza Material3'ową minimalną wysokość dotykową ~40dp na wiersz — dużo więcej niż web'owy, bezstylowy `<button class="ing-fav">` (`font-size:14px`, brak wymuszonego rozmiaru). To był GŁÓWNY powód mniejszej gęstości. Naprawione zastąpieniem `TextButton` zwykłym klikalnym `Text` (`Modifier.clickable`, bez wymuszania rozmiaru), wiersze składników spięte w `Column` z `Arrangement.spacedBy(4.dp)` (port web'owego `.ingredients li{margin-bottom:4px}`). (3) Pasek czasu/kalorii/dopasowania, tekst przycisku „🛒 Dodaj...”, „⭐ Oceń i skomentuj” i licznik widżetu spiżarni dostały `fontWeight = FontWeight.SemiBold`/`Bold` (port web'owych `font-weight:600`/`700`, które Compose'owe domyślne style przycisków/`bodySmall` nie odtwarzały). (4) Tytuł dostał węższy `lineHeight` (dopasowany do web'owego `line-height:1.3` zamiast szerokiego domyślnego Material3). Makro-wiersz i tekst przygotowania dostały `onSurfaceVariant` (przygaszony), jak web'owy `var(--muted)`. Zero zmian w logice/danych — czysto typograficzno-strukturalna korekta. `./gradlew :app:compileDebugKotlin :app:assembleDebug` przechodzi; zweryfikowane bezpośrednio na emulatorze (Medium_Phone_API_35): rozwinięta karta wyraźnie ciaśniejsza (6 składników "Szakszuki" mieści się w połowie poprzedniej wysokości), pogrubienia widoczne na pasku meta i przyciskach.
- **v8** (2026-08-23, Web + Android): Na wyraźną prośbę użytkownika ("gdy karta przepisu jest otwarta to ponowne kliknięcie niech jej nie zwija bo to uciążliwe w używaniu, zarówno w Web jak i kotlin") — dotknięcie już rozwiniętej karty (jej samej, całe ciało + chevron "Składniki i przygotowanie") stało się no-opem zamiast natychmiastowego zwinięcia. Web: `handleCardTap()` w `index.html` wraca wcześnie bez wołania `setCardExpanded(card, false)`, gdy `card.classList.contains("expanded")`. Android: `RecipeListScreen.kt`'s `onToggleExpanded`'s `when(recipe.id){ expandedRecipeId -> {...} }` gałąź zamieniona na pusty blok. Jedyny sposób zwinięcia karty to teraz rozwinięcie innej (już istniejący mechanizm auto-zwijania poprzedniej). Zweryfikowane na żywo na obu platformach: wielokrotne dotknięcie rozwiniętej karty (w tym na samym tekście metadanych, nie tylko chevronie) zostawia ją rozwiniętą.
- **v9** (2026-08-23, Android): Po v7/v8 użytkownik przysłał kolejne zrzuty ekranu i doprecyzował dwa punkty specyficzne dla Androida. (1) "w kotlin brakuje na karcie napisu składniki i przygotowanie" — v7 usunęła te etykiety, żeby dopasować się 1:1 do weba; użytkownik jednak chce je z powrotem NA ANDROIDZIE konkretnie (web zostaje bez zmian, świadoma rozbieżność udokumentowana w Opisie). (2) "przenieś tez ikonkę z miniaturka dania na karcie danie na prawą stronę w kotlin żeby od lewej strony karty już się zaczynał tekst tak jak w webowej wersji" — dotąd `RecipeCard`'s zewnętrzny `Row` miał miniaturkę 48dp PRZED tekstową kolumną (`Box(thumb); Spacer; Column{RecipeCardBody}`), przeciwnie niż web'owy `.card-head` (tytuł flex:1 po lewej, `.card-head-side` z miniaturką po prawej). Zamienione kolejnością: `Column{RecipeCardBody}` teraz pierwsza (tytuł startuje od lewej krawędzi karty), `Box(thumb)` po niej. `./gradlew :app:compileDebugKotlin :app:assembleDebug` przechodzi; zweryfikowane bezpośrednio na emulatorze: etykiety "Składniki"/"Przygotowanie" ponownie widoczne, miniaturka (np. jajko/awokado) po prawej stronie karty na liście zwiniętej i rozwiniętej.
- **v10** (2026-08-23, Web + Android): Użytkownik zgłosił, że dwuetapowe
  otwieranie z v4 ("teraz sie wysrodkowuje na ekranie") jest zbędnym
  tarciem i poprosił o powrót do jednego stuknięcia = pełne rozwinięcie,
  z zastrzeżeniem że nadal ma być jakaś ochrona przed przypadkowym
  rozwinięciem podczas przewijania. Research potwierdził, że ta ochrona
  to od zawsze OSOBNY mechanizm od dwuetapowości (patrz Uwagi) — na
  webie już istniejący (`startScrollY`/`scrollMoved` w
  `attachSwipeRating()`'s `finish()`, `index.html`), na Androidzie
  DOTĄD NIEISTNIEJĄCY (poprzednio polegał wyłącznie na wbudowanym
  touch-slop Compose). Web: `handleCardTap()` uproszczony — usunięta
  gałąź `pendingCenterCard` (arm-and-center-only na pierwsze stuknięcie),
  jedno stuknięcie od razu woła `setCardExpanded(card, true)` (v8's
  no-op na już rozwiniętej karcie zostaje); `wasTap`/`scrollMoved` w
  `attachSwipeRating()` pozostają bez zmian jako jedyna ochrona.
  Android: `pendingCenterRecipeId` i jego `LaunchedEffect` usunięte z
  `RecipeListScreen.kt`, `onToggleExpanded` uproszczony do dwóch gałęzi
  (już rozwinięta → no-op; inaczej → `expandedRecipeId = recipe.id`
  wprost); NOWY scroll-guard dodany na miejscu zwykłego `.clickable` —
  porównanie pozycji listy (`LazyListState`) w momencie dotknięcia i
  puszczenia, port web'owego `startScrollY`/`scrollMoved`, koegzystujący
  z istniejącym `detectHorizontalDragGestures` (swipe-to-rate). Post-
  expand auto-centrowanie (v3/v5) zostaje bez zmian na obu platformach.
  Zweryfikowane bezpośrednio w przeglądarce i na emulatorze: pojedyncze,
  stacjonarne stuknięcie od razu rozwija kartę i centruje ją na ekranie;
  dotknięcie kończące przewijanie listy (fling) nie rozwija karty na
  żadnej z platform.

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
- **v2** (2026-08-23, Android): Użytkownik poprosił o dodanie tego przycisku do Kotlina ("do wersji kotlin dodaj button przewijania do góry listy przepisów"), nie wiedząc że już istniał w kodzie od wcześniej — okazało się, że był całkowicie niewidoczny: `MainActivity.kt`'s `Scaffold`'s `floatingActionButton` slot stawia „💡”/„📖” w tym samym rogu (`BottomEnd`) co ten przycisk, w OSOBNEJ kompozycji renderowanej NAD zawartością ekranu — oba trafiały w dokładnie ten sam punkt, „💡” całkowicie zasłaniając przycisk powrotu do góry pod sobą. Naprawione przeniesieniem na przeciwny róg (`BottomStart`) w `RecipeListScreen.kt`, żeby nie kolidował niezależnie od tego, ile przycisków wyrośnie w tamtym rogu. Zweryfikowane bezpośrednio na emulatorze: po przewinięciu listy przepisów przycisk widoczny w lewym dolnym rogu.

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
Dzienny cel kaloryczny jest rozdzielany na pięć posiłków wg stałych proporcji: śniadanie 340/1500, II śniadanie 260/1500, obiad 420/1500, kolacja 280/1500, deser/przekąska 200/1500 (proporcje, nie sztywne wartości — skalują się z dziennym celem).

## Kryteria akceptacji
- Suma pięciu proporcji wynosi dokładnie 1.
- Target dla każdej kategorii przeliczany jest przy każdej zmianie profilu.
- Nieplanowanie posiłku w kategorii Deser/Przekąska nie zaburza pozostałych czterech targetów (patrz FR-13).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-09): Poprawiono nieaktualne proporcje (v1 opisywało 370/280/450/300/100, sprzed dodania kategorii Deser/Przekąska jako pełnoprawnego piątego slotu). Rzeczywisty kod (`MEAL_RATIOS` w `index.html`) od dłuższego czasu używa 340/260/420/280/200 — dokument tylko dogania stan faktyczny, zauważone przy portowaniu tego wymagania do Androida (`android/logic/ProfileCalculations.kt`).

---

# FR-8: Filtr bez glutenu / bez laktozy

**Obszar:** Personalizacja i cele dietetyczne  
**Status:** Zaimplementowane

## Opis
Dwa niezależne przełączniki w Ustawieniach pozwalają ukryć dania zawierające gluten (pieczywo, kasze glutenowe) lub nabiał bez wyraźnie oznaczonej wersji „bez laktozy” — zarówno z listy przepisów (Przepisy), jak i z każdego źródła doboru dania w Planerze (losowanie całego tygodnia/dnia/pojedynczego slotu, ręczny wybór dania ze slotu).

## Kryteria akceptacji
- Włączenie filtra ukrywa pasujące przepisy natychmiast po zapisaniu ustawień w widoku Przepisy.
- Włączenie filtra dotyczy też Planera: „🎲 Wygeneruj losowo cały tydzień”, „🎲 Losuj ten dzień”, „🔁 losuj inne danie” na pojedynczym slocie oraz ręczny picker dania dla slotu nigdy nie proponują dania niespełniającego aktywnego filtra.
- Filtr jest jawnie opisany jako orientacyjny, nie medyczny — nie gwarantuje 100% poprawności dla każdego przepisu.

## Uwagi
Ograniczenie znane i udokumentowane w samej aplikacji: filtr bazuje na oznaczeniach składników, nie na certyfikowanej analizie, więc nie wyklucza się logicznie z FR-1..FR-3, ale nie należy go traktować jako gwarancji bezpieczeństwa zdrowotnego.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-22): Naprawiono realny błąd znaleziony podczas manualnego przejścia checklisty QA — filtr od zawsze działał WYŁĄCZNIE w widoku Przepisy (`isGlutenFree`/`isLactoseFree` stosowane tylko tam). Cały Planer (`recipesByCat()`, używana przez `fittingPool()` i ręczny picker slotu) budował pulę kandydatów bez żadnego sprawdzenia tych dwóch przełączników, więc auto-generowanie tygodnia/dnia/slotu i ręczny wybór dania regularnie proponowały dania z glutenem/laktozą mimo aktywnego filtra. Naprawione przez zastosowanie tych samych predykatów wewnątrz `recipesByCat()` (z zabezpieczeniem: jeśli filtr zostawiłby pustą pulę dla kategorii, funkcja cofa się do pełnej listy tej kategorii zamiast zwracać pustkę, która wywaliłaby resztę Planera wyjątkiem) — patrz `android/PARITY.md`'s notatka z tej daty po pełen opis i zweryfikowanie w przeglądarce.

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
- Udział tej kategorii w dziennym celu (200/1500) jest wydzielony z pozostałych czterech, a nie dodany na wierzch — dzienny cel kaloryczny się nie zmienia.
- Docelowe makroskładniki slotu deseru muszą być kalibrowane względem RZECZYWISTEJ kaloryczności przepisów w tej kategorii, tak by wynik dopasowania (🎯, FR-7) miał realną szansę wypaść wysoko dla co najmniej części przepisów, dla każdej kombinacji celu diety — nie może systemowo lądować na 0% dla całej kategorii.

## Uwagi
Zrewidowane 2026-08-07: wykryto (przy okazji niepowiązanej weryfikacji), że udział 100/1500 (~6,7% dnia, czyli 100-140 kcal zależnie od celu) był około dwukrotnie mniejszy niż rzeczywista kaloryczność 16 przepisów tej kategorii (średnio 218 kcal, zakres 169-314 kcal) — różnica była tak duża, że wzór dopasowania z FR-7 zawsze lądował na 0% dla KAŻDEGO deseru, przy KAŻDYM celu diety z rygorem niskiego IG i bez niego przy celu "Redukcja", a średnio ~10% przy "Utrzymanie". Skorygowano udział do 200/1500 (~13,3% dnia), zabierając różnicę proporcjonalnie z pozostałych czterech posiłków (dzienny cel kaloryczny bez zmian) — po korekcie desery dopasowują się sensownie (średnio 36-54%, maksymalnie 65-77% w zależności od celu) bez pogorszenia dopasowania pozostałych kategorii.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-07): Skorygowano udział kaloryczny slotu deseru (100/1500 → 200/1500) po wykryciu, że powodował systemowe 0% dopasowania dla całej kategorii — patrz sekcja "Uwagi" i dodane kryterium akceptacji.

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
Na dole rozwiniętej karty przepisu — po sposobie przygotowania, PRZED sekcją oceny/komentarzy (patrz v5) — znajduje się widżet „🏺 Stan spiżarni” pokazujący OD RAZU, bez dodatkowego stuknięcia, ile z składników przepisu jest już w spiżarni — licznik „X / Y składników” oraz pasek postępu (wypełnienie w kolorze akcentu na przygaszonym tle innego odcienia, proporcjonalne do pokrycia). Stuknięcie w cały widżet otwiera to samo okienko co dawniej, stylizowane jak karta przepisu (te same zaokrąglone rogi, cień, tło), w którym każdy składnik ma osobny wiersz z wyraźnym stanem posiadania („Brak w spiżarni” / „🏺 …”) oraz dużym przyciskiem „Mam to” do oznaczenia/odznaczenia go w spiżarni, plus osobny przycisk dodania pojedynczego składnika do listy zakupów.

## Kryteria akceptacji
- Karta przepisu pokazuje pokrycie spiżarni (liczbę i pasek postępu) OD RAZU po rozwinięciu, bez konieczności stukania w cokolwiek.
- Pasek postępu ma wyraźnie odróżnialne wypełnienie (odcień akcentu) i tło (przygaszony/neutralny odcień) — nie jest jednolitym kolorem.
- Widżet stanu spiżarni znajduje się zaraz PO sposobie przygotowania, PRZED rzędem „⭐ Oceń | 💬 Komentarze” i rzędem „🛒 Dodaj do listy | ✅ Zrobione | 📅 Zaplanuj” (v5).
- Stuknięcie w widżet otwiera okienko szczegółowe: wizualnie przypomina kartę przepisu, nie generyczną szufladę z drobnymi elementami.
- Każdy wiersz w okienku szczegółowym ma jeden, duży, łatwo trafialny przycisk zmieniający stan posiadania (min. wysokość dotykowa 34px).
- Zmiana stanu w tym okienku natychmiast odzwierciedla się zarówno w zakładce Spiżarnia, jak i w liczniku/pasku postępu na karcie.

## Uwagi
Zrewidowane w rundzie z 2026-08-03: poprzednia wersja miała stłoczony, jednowierszowy układ (tekst składnika + malutka plakietka + dwa małe przyciski obok siebie), trudny do trafienia kciukiem — przeprojektowano na czytelny układ dwuwierszowy z osobnym, dużym przyciskiem akcji.

Zrewidowane ponownie 2026-08-03: sam przycisk-wyzwalacz na karcie przepisu był stylistycznie samym napisem bez tła/obramowania, co czyniło go trudnym do trafienia — dodano pełny styl przycisku.

Zrewidowane 2026-08-23 (v4): na wyraźną prośbę użytkownika ("miało od razu pokazywać co jest a czego nie ma i pokazywać innym odcieniem pasek postępu, przenieś też to pole na dół karty") zwykły przycisk-wyzwalacz zastąpiony inline'owym podsumowaniem pokrycia (licznik + pasek postępu) i przeniesiony z góry rozwiniętej karty (zaraz po makroskładnikach) na sam dół — patrz Historia rewizji.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-03): Doprecyzowano zachowanie na podstawie zgłoszonej poprawki — patrz sekcja "Uwagi" powyżej.
- **v3** (2026-08-03): Dodano stylizację przycisku-wyzwalacza na karcie przepisu — patrz sekcja "Uwagi" powyżej.
- **v4** (2026-08-23, Web + Android): Przycisk-wyzwalacz zastąpiony inline'owym widżetem (licznik „X / Y składników” + pasek postępu, ten sam mechanizm liczenia co istniejący `pantryMatch`/`pantryCoverageRatio` na webie i `pantryItems.containsKey` na Androidzie — zero nowej logiki dopasowania), przeniesiony na sam dół rozwiniętej karty, tuż przed przyciskiem dodania do listy zakupów. Stuknięcie w widżet nadal otwiera dokładnie to samo okienko szczegółowe co wcześniej (`openPantryModal`/`PantryCheckDialog`), bez zmian w jego zawartości ani działaniu. Web: `.pantry-status-btn` w `index.html` (reużywa istniejące `.weight-bar-track`/`.weight-bar-fill` dla spójnego wyglądu paska z ekranem Postęp). Android: `Card` z `LinearProgressIndicator` w `RecipeCardBody` (`RecipeListScreen.kt`). Zweryfikowane na żywo na obu platformach (przeglądarka + emulator): licznik i pasek aktualizują się natychmiast po oznaczeniu składnika jako posiadanego w okienku szczegółowym, bez ponownego otwierania karty.
- **v5** (2026-08-23, Web + Android): Na wyraźną prośbę użytkownika ("oceń i skomentuj oraz komentarze użytkowników też daj do jednej linii, poniżej stanu spiżarni ale nad dodaj do listy zakupów, zrobione i zaplanuj") widżet przesunięty WYŻEJ (zaraz po sposobie przygotowania, zamiast po sekcji ocen/komentarzy), a poniżej niego dwa nowe rzędy: „⭐ Oceń | 💬 Komentarze” obok siebie, potem „🛒 Dodaj do listy | ✅ Zrobione | 📅 Zaplanuj” obok siebie (patrz FR-3/v6 dla historii przeniesienia przycisku zakupów). Sam widżet (licznik/pasek/kliknięcie→okienko) bez zmian funkcjonalnych. Zweryfikowane na żywo na obu platformach.

# FR-17: Ocena dania po ugotowaniu (gwiazdki)

**Obszar:** Gotowanie i historia  
**Status:** Zaimplementowane

## Opis
**Od FR-84 (2026-08-11), ta funkcja w opisanej poniżej formie już NIE ISTNIEJE** — była jedną z trzech osobnych ocen scalonych w jedną (FR-67). Historia gotowania (FR-15) jest teraz czystym logiem DAT „✅ Zrobione”, bez własnej oceny za każdy wpis. Zamiast tego pokazuje przycisk „⭐ Oceń to danie” (albo „⭐ Twoja ocena: X/5 (zmień)”), otwierający dokładnie to samo okienko oceny co przycisk pod przepisem (FR-67) i plakietka na karcie (FR-57). Reszta tego dokumentu (poniżej) opisuje ORYGINALNE, już nieaktualne zachowanie — zachowane dla historii, patrz FR-84 po aktualny opis.

~~W historii gotowania (FR-15) każdy wpis można ocenić w skali gwiazdkowej, niezależnie od globalnej oceny lubię/nie lubię (FR-55). Pięć gwiazdek jest rozłożonych równo na całą dostępną szerokość wiersza wpisu (nie stłoczonych po jednej stronie), każda z wystarczająco dużym obszarem dotykowym.~~

## Kryteria akceptacji (nieaktualne, patrz FR-84)
- ~~Ocena gwiazdkowa jest przypisana do konkretnego wpisu historii (daty ugotowania), nie do przepisu jako całości.~~
- ~~5 gwiazdek rozciąga się na pełną szerokość wiersza (odstępy równe, nie zbite razem po lewej), każda z minimalną wysokością dotykową ok. 34px.~~

## Uwagi
Zrewidowane 2026-08-07: gwiazdki były wcześniej stłoczone po lewej stronie wiersza (mały, ciasny obszar klikania) — data i przycisk usuwania wpisu przeniesiono do osobnej górnej linijki, a gwiazdki dostały całą szerokość wiersza dla siebie.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-07): Gwiazdki rozciągnięte na pełną szerokość wiersza dla łatwiejszego trafienia — patrz sekcja "Uwagi".
- **v3** (2026-08-11): Scalone z FR-55/FR-57/FR-67 w jeden mechanizm oceniania — per-wpisowa ocena gwiazdkowa opisana wyżej PRZESTAŁA ISTNIEĆ, historia gotowania jest teraz czystym logiem dat z linkiem do jedynego, wspólnego okienka oceny. Patrz FR-84.

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
- Przeskalowana lista składników używa poprawnej polskiej odmiany przy
  liczbie całkowitej (2 jajka → 3× → „6 jajek”, a nie „6 jajka”).
- Składniki podane w jednostkach (gramy, mililitry, łyżeczki, kromki)
  zmieniają wyłącznie liczbę — ich treść pozostaje nietknięta.
- Wynik ułamkowy zostawia oryginalne brzmienie składnika z przepisu.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-28, Web only): Przeskalowana lista składników odmienia
  teraz nazwę zgodnie z nową liczbą. Wcześniej skalowanie podmieniało
  wyłącznie LICZBĘ, więc „2 jajka” przy 3× czytało się „6 jajka”, a przy
  0,5× „1 jajka” — obie formy niepoprawne po polsku i widoczne na każdej
  przeskalowanej karcie przepisu. Aplikacja miała już pełną tabelę odmian
  (używaną przez spiżarnię i listę zakupów), tylko skalowanie z niej nie
  korzystało. Dopasowanie następuje po CAŁYM tekście po liczbie i wyłącznie
  względem tej tabeli — dzięki temu „jajka” i „tortilla” są odmieniane, a
  „g piersi z kurczaka” czy „łyżeczka oliwy” zostają nietknięte.
  **Świadomie tylko dla liczb całkowitych**: liczebnik ułamkowy wymaga po
  polsku dopełniacza liczby pojedynczej („0,5 jajka”, „1,5 banana”), formy
  której ta tabela nie zawiera — pierwsza wersja poprawki, odmieniająca
  także ułamki regułą jeden/kilka/wiele, produkowała „0,5 jajko” i
  „1,5 banany”, czyli zamieniała jeden błąd na inny; przy ułamku zostaje
  więc oryginalne brzmienie z przepisu, które było już poprawne.
  Zweryfikowane na żywo (headless Chromium) na dziewięciu wzorcach
  składników × pięciu skalach: poprawnie „1 jajko”/„6 jajek”/„12 jajek”,
  „6 tortilli”, „6 bananów”/„9 bananów”, przy nietkniętych wierszach
  jednostkowych. CACHE_NAME→v113, `versions/v113/`.


- **v3** (2026-08-29, PORT NA ANDROIDA): skalowanie porcji odmienia teraz
  rzeczownik także w Kotlinie. `PlannerOperations.scaleIngredientText`
  podmieniał wyłącznie LICZBĘ, więc „2 jajka” przy 3× czytało się „6 jajka”,
  a przy 0,5× „1 jajka” — tabela odmian była już przeniesiona w
  `PantryDisplay`, tylko nie była stamtąd wołana. Dopasowanie na CAŁYM
  pozostałym tekście i wyłącznie do tej tabeli, co czyni je bezpiecznym:
  „jajka” i „awokado” są w tabeli, a „g piersi z kurczaka” czy „łyżeczka
  oliwy” nie, więc linia zaczynająca się od jednostki zostaje nietknięta.
  Ułamki celowo bez odmiany — polski bierze tam dopełniacz liczby
  pojedynczej („0,5 jajka”), formy której tabela one/few/many nie zna.

---

# FR-21: Losowe generowanie planu — cały tydzień lub pojedynczy dzień

**Obszar:** Planer tygodniowy  
**Status:** Zaimplementowane na obu platformach (cofanie dla obu wariantów — dnia i całego tygodnia — patrz Historia rewizji v3/v4)

## Opis
Przycisk „🎲 Wygeneruj losowo cały tydzień” losuje dania dla wszystkich 7 dni × 5 kategorii z puli pasujących do profilu. Dodatkowo każda karta dnia ma własny przycisk „🎲 Losuj ten dzień”, generujący losowy plan tylko dla tego jednego dnia, bez naruszania pozostałych dni.

Obie akcje można cofnąć — po wygenerowaniu pojawia się powiadomienie z przyciskiem „Cofnij”, przywracającym dokładnie poprzedni plan (dania, skale porcji, flagi resztek). Losowanie pojedynczego dnia wykonuje się od razu, bez okienka potwierdzenia; losowanie całego tygodnia nadal pyta o potwierdzenie przed wykonaniem, a cofnięcie jest tam dodatkowym zabezpieczeniem.

## Kryteria akceptacji
- Pula losowania uwzględnia dopasowanie do profilu (ta sama logika co FR-11).
- Losowanie POJEDYNCZEGO dnia wykonuje się natychmiast, bez okienka potwierdzenia, i pokazuje powiadomienie z „Cofnij”.
- Losowanie CAŁEGO tygodnia nadal wymaga potwierdzenia przed wykonaniem, a po wykonaniu również pokazuje „Cofnij”.
- Kliknięcie „Cofnij” przywraca dokładnie ten sam plan (razem ze skalami porcji i flagami resztek), jaki był przed losowaniem — odpowiednio dla jednego dnia albo dla całego tygodnia.
- Zignorowanie powiadomienia pozostawia wylosowany plan.

## Uwagi
Web: `structuredClone()` na trzech równoległych mapach PRZED nadpisaniem
(dla jednego dnia — tylko jego wpisy; dla całego tygodnia — całe
`state.planner`/`plannerScale`/`plannerLeftover`), przekazane jako
domknięcie do `toast(msg, undoLabel, onUndo)` (mechanizm z FR-91).

Decyzja projektowa (2026-08-28): `confirm()` zostaje TYLKO dla akcji o
zasięgu całego tygodnia (35 slotów naraz) — tam przerwanie użytkownika jest
uzasadnione, a cofnięcie chroni przed zbyt szybkim kliknięciem „OK”. Dla
akcji o zasięgu jednego dnia samo cofnięcie jest lepsze niż pytanie:
nie przerywa pracy, a chroni też po fakcie. Ta sama zasada zastosowana w
FR-22.

Android: `PlannerViewModel.replaceDay`/`replaceAll` przyjmują migawkę
wziętą PRZED nadpisaniem (per dzień albo cały `WeekPlan`), przekazaną jako
domknięcie do `onShowUndoSnackbar(msg, label, onUndo)` — ten sam mechanizm,
którego Planer używa od FR-91/FR-109/FR-111, zero nowej infrastruktury.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-28, Web only): Dodane cofanie dla obu wariantów losowania; `confirm()` usunięty z wariantu jednodniowego, zachowany dla całotygodniowego. Zmiana z własnej rekomendacji, razem z FR-22 (patrz tam pełne uzasadnienie niespójności, którą to naprawia). Zweryfikowane na żywo (headless Chromium): wylosowanie pustego dnia wypełniło 5 kategorii, „Cofnij” przywróciło go do pustego; wylosowanie całego tygodnia wypełniło dzień 0 i dzień 5, „Cofnij” przywróciło dzień 0 do pierwotnego `{"sniadania":"S1"}` i dzień 5 do pustego — czyli cofnięcie działa na całej strukturze, nie tylko na dniu, który akurat był widoczny. CACHE_NAME→v106, `versions/v106/`.


- **v3** (2026-08-29, PORT NA ANDROIDA): „🎲 Losuj ten dzień” oferuje teraz
  „Cofnij” także w aplikacji natywnej. Migawka dnia robiona jest w momencie
  POTWIERDZENIA, nie naciśnięcia przycisku, więc dzień zmieniony przy
  otwartym okienku potwierdzenia też przywraca się poprawnie. Przywracany
  jest cały dzień naraz (`PlannerViewModel.replaceDay`), więc wracają też
  skala porcji i znaczniki resztek, a nie same identyfikatory przepisów.

- **v4** (2026-08-30, DOKOŃCZENIE PORTU): v3 przeniósł tylko wariant
  jednodniowy — „🎲 Wygeneruj losowo cały tydzień” nadal cichcem nadpisywał
  wszystkie 35 slotów bez żadnej drogi powrotu, mimo że web ma tam cofnięcie
  od v2. Naprawione tym samym wzorcem co v3, tylko na całym `WeekPlan`
  (`PlannerViewModel.replaceAll`) zamiast jednego dnia. `./gradlew
  :logic:test :app:assembleDebug` przechodzi, NIE zweryfikowane wizualnie na
  emulatorze.

---

# FR-22: Czyszczenie planu — cały tydzień lub pojedynczy dzień

**Obszar:** Planer tygodniowy  
**Status:** Zaimplementowane (cofanie zamiast potwierdzenia — v2 — na razie Web-only, patrz Uwagi)

## Opis
Oprócz generowania, każda karta dnia ma przycisk „🗑️ Wyczyść ten dzień”, kasujący zaplanowane dania tylko dla tego jednego dnia, niezależnie od pozostałych dni tygodnia.

Na webie (v2) operacja wykonuje się od razu, bez blokującego okienka potwierdzenia — zamiast tego pokazuje powiadomienie z przyciskiem „Cofnij”, przywracającym dokładnie poprzedni stan tego dnia (dania, skale porcji i flagi resztek). To ten sam wzorzec, co przy usuwaniu pojedynczego dania z „Dzisiejszego Planera” (FR-91).

## Kryteria akceptacji
- Czyszczenie jednego dnia nie wpływa na pozostałe dni.
- Web (v2): operacja wykonuje się natychmiast, bez okienka `confirm()`.
- Web (v2): po wyczyszczeniu pojawia się powiadomienie z przyciskiem „Cofnij”; kliknięcie go przywraca dokładnie ten sam plan dnia, jaki był przed wyczyszczeniem (razem ze skalami porcji i flagami resztek).
- Web (v2): zignorowanie powiadomienia pozostawia dzień wyczyszczony.

## Uwagi
Web: `structuredClone()` na trzech równoległych mapach tego dnia
(`state.planner[di]`/`plannerScale[di]`/`plannerLeftover[di]`) PRZED
skasowaniem, przekazane jako domknięcie do `toast(msg, undoLabel, onUndo)`
(mechanizm dodany w FR-91). Zero nowej infrastruktury.

Decyzja projektowa (2026-08-28): `confirm()` zamieniony na cofnięcie tylko
dla akcji o zasięgu JEDNEGO dnia. Czyszczenie/generowanie CAŁEGO tygodnia
(FR-21) zachowuje potwierdzenie ORAZ dostaje cofnięcie — przy 35 slotach
naraz przerwanie użytkownika jest uzasadnione, a cofnięcie jest tam
zabezpieczeniem przed zbyt szybkim kliknięciem „OK”, nie zamiennikiem
pytania.

**v2 świadomie Web-only na razie** — ta sesja pracuje w środowisku bez
dostępu do `api.foojay.io` (toolchain JDK dla Gradle, błąd 403 przy
`:app:compileDebugKotlin`), więc port do Compose nie może tu zostać ani
skompilowany, ani przetestowany; odłożone do sesji z realnym dostępem do
Gradle/emulatora, odnotowane w `android/PARITY.md`.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-28, Web only): Blokujące `confirm()` zamienione na natychmiastową akcję + „Cofnij” w powiadomieniu. Zmiana z własnej rekomendacji (użytkownik: „dodawaj swoje rekomendowane zmiany jak i refactoringi”): mechanizm cofania istniał od FR-91, ale był podpięty tylko pod JEDNĄ akcję, mimo że dwie inne, znacznie bardziej destrukcyjne (czyszczenie i losowanie całego dnia), wciąż używały natywnego okienka — niespójność UX i słabsze zabezpieczenie, bo `confirm()` chroni przed pomyłką tylko zanim ją popełnisz, a nie po. Zweryfikowane na żywo (headless Chromium): wyczyszczenie dnia z zaplanowanym śniadaniem, potwierdzone `state.planner[0] === {}`, kliknięcie „Cofnij” przywróciło `{"sniadania":"S1"}`. CACHE_NAME→v106, `versions/v106/`.


- **v3** (2026-08-29, PORT NA ANDROIDA): „🗑️ Wyczyść ten dzień” oferuje
  teraz „Cofnij” także w aplikacji natywnej — ta sama migawka całego dnia co
  w FR-21/v3. Pusty dzień nie pokazuje propozycji cofnięcia, bo nie ma czego
  cofać.

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

# FR-86: Podgląd przepisu z poziomu Planera

**Obszar:** Planer
**Status:** Zaimplementowane i zweryfikowane na emulatorze (Android), Android-only

## Opis
W każdym wypełnionym slocie posiłku w Planerze (obok istniejących
przycisków skali porcji i „🔁” losowania od nowa) dodano przycisk „👁️”,
który otwiera okno podglądu przypisanego przepisu — bez opuszczania
Planera i bez zmiany przypisania (to osobna akcja od dotknięcia samej
nazwy dania, które nadal otwiera picker do ZMIANY przepisu, FR-19).

Okno podglądu pokazuje:
- Nazwę dania — dotknięcie otwiera wyszukiwanie Google
  (`"{nazwa} przepis"`), dokładnie tak samo jak dotknięcie tytułu na karcie
  przepisu na karcie Przepisy.
- Czas przygotowania i kalorie — PRZELICZONE na aktualną skalę porcji tego
  slotu (np. „1.5×”), nie bazowe wartości przepisu.
- Listę składników — również przeliczoną na aktualną skalę porcji (ten sam
  mechanizm co dodawanie dnia do listy zakupów).
- Sposób przygotowania (pełny tekst).

Świadomie NIE zawiera (w odróżnieniu od pełnej karty przepisu na karcie
Przepisy): sprawdzenia stanu spiżarni, oceniania/komentarzy, ulubionych
składników, usuwania — to lekki, tylko-do-odczytu podgląd, nie duplikat
pełnej karty przepisu.

## Kryteria akceptacji
- Przycisk „👁️” widoczny tylko przy WYPEŁNIONYCH slotach (tak samo jak
  przyciski skali i „🔁”) — puste sloty („— wybierz danie —”) go nie mają.
- Otwarcie podglądu NIE zmienia przypisania w slocie ani nie otwiera
  pickera zmiany dania.
- Kalorie i lista składników w podglądzie odzwierciedlają aktualną skalę
  porcji tego konkretnego slotu, nie bazowe 1× wartości przepisu.
- Dotknięcie nazwy dania w podglądzie otwiera przeglądarkę z wyszukiwaniem
  Google.

## Uwagi
Świadoma, udokumentowana rozbieżność web/Android (patrz `android/PARITY.md`)
— funkcja dodana wyłącznie w sesji dotyczącej Kotlina; port do
`index.html` (Planer webowy nie ma dziś żadnego odpowiednika) pozostaje do
rozważenia w osobnej turze.

Nowy `RecipePreviewDialog` w `PlannerScreen.kt` celowo NIE jest oparty na
`RecipeCard`/`RecipeCardBody` z `RecipeListScreen.kt` — te są zbyt ściśle
powiązane z ViewModelami karty Przepisy (przesuwanie do oceniania,
sprawdzanie spiżarni, recenzje, komentarze, ulubione), żeby dało się je
sensownie wykorzystać z poziomu Planera bez podstawiania dziesiątek
zaślepek. Reużyte zostały tylko: mechanizm wyszukiwania Google (identyczny
`Intent`/`Uri`) oraz istniejące czyste funkcje skalowania
(`PlannerOperations.scaleIngredients`/`scaledKcal`, już używane przy
dodawaniu dnia do listy zakupów).

## Historia rewizji
- **v1** (2026-08-11, Android): Pierwsza wersja, na wyraźną prośbę
  użytkownika ("dodaj też możliwość podgladniecia przepisu z poziomu
  planera bo teraz da się zmienić ale już podejrzeć jak przygotować się
  nie da"). `./gradlew :app:assembleDebug :app:testDebugUnitTest
  :logic:test` przechodzi. **Nie zweryfikowane na żywo** — wymaga
  sprawdzenia w Android Studio (otworzyć podgląd wypełnionego slotu ze
  zmienioną skalą porcji, potwierdzić że kalorie/składniki są przeliczone,
  dotknąć nazwy i potwierdzić że otwiera się wyszukiwanie Google).
- **v2** (2026-08-23, Android): Zweryfikowane bezpośrednio na emulatorze
  (lokalna sesja Claude Code, `Medium_Phone_API_35`, `adb`) — wszystkie 4
  kryteria akceptacji potwierdzone na żywo. Wygenerowano losowy plan
  tygodnia (przycisk „🎲 Wygeneruj losowo cały tydzień”), Śniadanie
  poniedziałku wylosowało „Kanapka z pastą jajeczną na bułce pszennej” ze
  skalą 1.25×; dotknięcie „👁️” otworzyło podgląd pokazujący „375 kcal
  (porcja 1.25×)” i składniki poprawnie przeliczone (1,5 bułka pszenna,
  2,5 jajka, 1,5 łyżka jogurtu — dokładnie bazowa ilość × 1.25), bez
  żadnego elementu pełnej karty przepisu (spiżarnia/oceny/ulubione).
  Dotknięcie podkreślonego tytułu otworzyło Chrome z zamiarem
  wyszukiwania (potwierdzone dwukrotnie). Po powrocie do aplikacji okno
  podglądu i przypisanie slotu były DOKŁADNIE takie same jak przed
  dotknięciem tytułu — otwarcie podglądu/wyszukiwarki nie zmieniło
  przypisania. Puste sloty (przed wygenerowaniem planu) nie miały „👁️” —
  potwierdzone na zrzucie ekranu sprzed losowania. Status podniesiony z
  ⏳ na ✅.

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
**Status:** Zaimplementowane na obu platformach (cofanie dla obu akcji kasujących — patrz Historia rewizji v3/v4)

## Opis
Pozycje na liście można odhaczyć jako kupione. Listę można udostępnić przez systemowy arkusz udostępniania / SMS / WhatsApp / skopiowanie do schowka, usunąć same odhaczone pozycje albo wyczyścić całą listę.

Obie akcje kasujące można cofnąć — po wykonaniu pojawia się powiadomienie z przyciskiem „Cofnij”, przywracającym usunięte pozycje. „Usuń odhaczone” działa od razu (bez pytania), „Wyczyść całą listę” nadal pyta o potwierdzenie, a cofnięcie jest tam dodatkowym zabezpieczeniem.

## Kryteria akceptacji
- Odhaczenie pozycji nie usuwa jej z listy, tylko oznacza wizualnie.
- „Usuń odhaczone” i „Wyczyść całą listę” to dwie osobne, jednoznacznie opisane akcje.
- „Usuń odhaczone” pokazuje powiadomienie z liczbą usuniętych pozycji i przyciskiem „Cofnij”; cofnięcie przywraca dokładnie te pozycje (razem z ilościami i powiązaniami z przepisami).
- „Usuń odhaczone” przy braku odhaczonych pozycji pokazuje komunikat i nie robi nic więcej (nie oferuje cofania niczego).
- „Wyczyść całą listę” nadal wymaga potwierdzenia, a po wykonaniu oferuje „Cofnij”, które przywraca zarówno listę zakupów, jak i oznaczenia „dodane z przepisu” (`recipeAdded` na webie / pochodne od `items` na Androidzie, sterujące etykietą „✓ Na liście zakupów” na kartach przepisów).

## Uwagi
Web: `structuredClone()` na `state.shopping` (i dodatkowo `state.recipeAdded`
przy czyszczeniu całej listy) PRZED skasowaniem, przekazany jako domknięcie
do `toast(msg, undoLabel, onUndo)` — mechanizm dodany w FR-91, tu użyty
ponownie bez nowej infrastruktury.

Decyzja projektowa (2026-08-28), ta sama zasada co w FR-21/FR-22:
akcja cząstkowa, wykonywana w toku pracy („Usuń odhaczone”), nie przerywa
pytaniem, tylko oferuje cofnięcie; akcja o zasięgu całej listy zachowuje
potwierdzenie ORAZ dostaje cofnięcie.

Android: ten sam wzorzec migawki, przekazanej jako domknięcie do
`onShowUndoSnackbar(msg, label, onUndo)` (mechanizm od FR-91/FR-109/FR-111),
zero nowej infrastruktury.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-28, Web only): Dodane cofanie obu akcji kasujących. Zmiana z własnej rekomendacji, znaleziona przy przeglądzie kodu pod kątem spójności z FR-21/v2 i FR-22/v2: **„Usuń odhaczone” był najgorszym przypadkiem w całej aplikacji** — nieodwracalne kasowanie BEZ potwierdzenia I BEZ cofania, więc jedno przypadkowe stuknięcie na długiej liście (realnie zgłaszane były listy po 87 pozycji) po cichu niszczyło pracę bez żadnej drogi powrotu. Przy okazji dodany brakujący przypadek brzegowy: przy zerowej liczbie odhaczonych pozycji przycisk pokazuje teraz komunikat zamiast udawać, że coś zrobił. Zweryfikowane na żywo (headless Chromium), cztery przypadki osobno: usunięcie 2 z 3 pozycji (nieodhaczona nietknięta), cofnięcie przywracające komplet, przypadek „nic nie odhaczone” (lista nietknięta, komunikat pokazany, cofanie nieoferowane) oraz wyczyszczenie całej listy z cofnięciem przywracającym też `recipeAdded`. CACHE_NAME→v107, `versions/v107/`.


- **v3** (2026-08-29, PORT NA ANDROIDA): „Wyczyść całą listę” oferuje teraz
  „Cofnij” także w aplikacji natywnej. Migawka robiona przed czyszczeniem
  przywraca prawdziwą listę — ilości, odhaczenia i powiązania z przepisami,
  które decydują o tym, czy przepis „jest na liście” — a nie same nazwy.

- **v4** (2026-08-30, DOKOŃCZENIE PORTU): v3 przeniósł tylko „Wyczyść całą
  listę” — „Usuń kupione” (odpowiednik web'owego „Usuń odhaczone”) wciąż
  kasował natychmiast i bez żadnej drogi powrotu, czyli dokładnie ten sam
  najgorszy przypadek, który v2 naprawił na webie. Naprawione tym samym
  wzorcem: migawka przed kasowaniem, „Cofnij” w Snackbarze, a przy zerowej
  liczbie odhaczonych pozycji — Toast zamiast udawanej akcji. `./gradlew
  :logic:test :app:assembleDebug` przechodzi, NIE zweryfikowane wizualnie na
  emulatorze.

---

# FR-27: Dodanie składników z całego tygodnia z Planera

**Obszar:** Lista zakupów  
**Status:** Zaimplementowane

## Opis
Przycisk w zakładce Zakupy zbiorczo dodaje do listy wszystkie składniki wszystkich dań zaplanowanych na bieżący tydzień w Planerze, z uwzględnieniem ustawionej skali porcji każdego dania.

Jeśli TO SAMO danie jest zaplanowane więcej niż raz w tym samym tygodniu (np. ten sam przepis na śniadanie we wtorek i w piątek), każde takie zaplanowanie liczy się OSOBNO — składniki sumują się (2 jajka na danie × 2 zaplanowania = 4 jajka na liście), a nie tylko raz. Dopiero danie, które było już na liście PRZED tym dodawaniem (np. dodane wcześniej ręcznie z karty przepisu, albo z poprzedniego uruchomienia tego samego przycisku), jest pomijane — to zapobiega podwajaniu przy powtórnym kliknięciu tego samego przycisku, nie przy powtórzeniu dania w tym samym tygodniu.

## Kryteria akceptacji
- Dania już wcześniej dodane do listy (z osobnej, wcześniejszej akcji) nie są duplikowane przy ponownym kliknięciu przycisku.
- To samo danie zaplanowane na WIĘCEJ NIŻ JEDEN dzień w tym samym tygodniu dodaje swoje składniki tyle razy, ile razy jest zaplanowane w tym tygodniu — ilości się sumują, druga (i kolejna) obecność tego samego dania NIE jest traktowana jak duplikat do pominięcia.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-11): Naprawiono realny błąd zgłoszony przez użytkownika
  ("w zakładce zakupy jak masz na liscie banana np. we wtorek i w piątek
  to musisz wziąć pod uwagę ze to do każdego dania potrzebny jest banan
  wiec jeśli na liscie zakupów znajduja się zakupy na te dwa dni to
  potrzebne są dwa banany"). Poprzednia implementacja (na obu platformach)
  sprawdzała "czy to danie jest już na liście" NA BIEŻĄCO w trakcie
  przechodzenia po dniach tygodnia — więc gdy to samo danie pojawiało się
  drugi raz w tym samym tygodniu, było traktowane jak duplikat i całkowicie
  pomijane, mimo że powinno dodać swoje składniki po raz drugi. Naprawione
  robieniem jednego zrzutu "co już jest na liście" PRZED rozpoczęciem
  całego dodawania (dnia lub tygodnia), zamiast sprawdzania na bieżąco —
  każde zaplanowanie w BIEŻĄCEJ operacji liczy się osobno, a stan sprzed
  operacji nadal poprawnie zapobiega duplikatom przy powtórnym kliknięciu.
  Dotyczyło też przycisku "Dodaj składniki z tego dnia" (jeśli to samo
  danie zajmuje dwa sloty posiłków tego samego dnia) i przepływu FR-81.

---

# FR-28: Śledzenie stanu spiżarni w kafelkach pogrupowanych kategoriami

**Obszar:** Spiżarnia  
**Status:** Zaimplementowane (cofanie wyczyszczenia — v2 — na razie Web-only, patrz Historia rewizji)

## Opis
Zakładka Spiżarnia pokazuje kafelki produktów pogrupowane w kategorie (Nabiał, Warzywa, Owoce, Mięso/ryby/jajka, Strączki i orzechy, Pieczywo i zboża, Przyprawy, Inne). Górna połowa kafelka dodaje jednostkę, dolna odejmuje. Przyprawy śledzone są poziomem (Mało/Wystarczy/Dużo), nie liczbą sztuk. Kafelki w każdej kategorii układają się w siatkę rozciągającą się na pełną dostępną szerokość ekranu (równa liczba kolumn dopasowana do szerokości, kafelki równo rozciągnięte), a nie w luźno zawijany rząd o stałej szerokości kafelka z nierówną przerwą na końcu.

## Kryteria akceptacji
- Każda kategoria kończy się kafelkiem „➕ Dodaj własny” do ręcznego dodania produktu spoza bazy przepisów.
- Siatka kafelków wypełnia całą dostępną szerokość ekranu, bez dużej pustej przestrzeni po prawej stronie ostatniego kafelka w wierszu.
- Kafelki w tym samym wierszu mają równą szerokość niezależnie od liczby kolumn wynikającej z szerokości ekranu.
- Przycisk „🗑️ Wyczyść całą spiżarnię” (na obu platformach) usuwa śledzenie WSZYSTKICH produktów i przypraw na raz, po potwierdzeniu — jak pojedyncze „Usuń śledzenie”, ale dla całej spiżarni jednocześnie. Nie kasuje własnych kafelków (`customTiles`) ani zmienionych kategorii/jednostek (`pantryCategoryOverride`/`pantryUnitOverride`) — te wracają do stanu nieśledzonego, tak samo jak każdy inny kafelek po usunięciu śledzenia, ale pozostają zdefiniowane/widoczne.
- Dotknięcie górnej/dolnej połowy kafelka liczonego w mililitrach (ml) dodaje/odejmuje domyślnie 50 ml (nie 100 ml — tylko waga w gramach zostaje przy skoku 100).
- Przytrzymanie ŚLEDZONEGO kafelka produktu liczonego w gramach lub mililitrach pokazuje, obok zmiany kategorii, także opcję zmiany tego skoku +/- z listy gotowych wartości; wybrana wartość obowiązuje dla tego konkretnego produktu do czasu kolejnej zmiany.

## Uwagi
Zgłoszony 2026-08-11: użytkownik zgłosił, że aplikacja "zacina się" po dodaniu kilku produktów do spiżarni z rzędu — pierwsze dotknięcia działały, kolejne przestawały reagować na chwilę. Przyczyna: dotknięcie kafelka spiżarni odświeżało (renderPantry/renderShop/renderRecipes) TRZY pełne widoki na raz, w tym listę 229+ przepisów i listę zakupów, nawet gdy użytkownik wcale na nie akurat nie patrzył — kilka szybkich dotknięć kumulowało ten koszt i blokowało główny wątek na chwilę. Naprawione: dotknięcie kafelka odświeża teraz tylko faktycznie widoczne widoki; pozostałe (Przepisy, Zakupy) odświeżają się same przy najbliższym wejściu na tę zakładkę zamiast na każde dotknięcie kafelka. Zweryfikowane bezpośrednio w przeglądarce (podmiana funkcji renderujących w celu policzenia wywołań) — jedno dotknięcie kafelka spiżarni: 0 wywołań renderRecipes/renderShop (wcześniej: po 1 każde, na KAŻDE dotknięcie).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-03): Doprecyzowano układ siatki kafelków na pełną szerokość ekranu (zamiast luźnego zawijania o stałej szerokości) — patrz Opis i Kryteria akceptacji.
- **v3** (2026-08-11): Naprawiono realny błąd wydajności powodujący zacinanie się aplikacji przy kilku szybkich dotknięciach kafelków z rzędu — patrz sekcja "Uwagi" powyżej. Brak zmiany zachowania funkcjonalnego, wyłącznie poprawka wydajności.
- **v4** (2026-08-11): Dodano przycisk „🗑️ Wyczyść całą spiżarnię” na obu platformach (web i Android, w tej samej turze), na wyraźną prośbę użytkownika ("dodaj opcji czyszczenia całej spiżarni w obydwu wersjach kotlin i html").
- **v5** (2026-08-23, Web + Android): Na wyraźną prośbę użytkownika ("dla artykułów liczonych w ml zmniejsz skok z 100 do 50 ml, a najlepiej dodaj opcje zmieniania skoku po przytrzymaniu kafelka") -- `tileStep("volume")` zmienione ze 100 na 50 (waga zostaje przy 100). Dodana per-produktowa nadpisywana wartość skoku, ustawiana z listy gotowych opcji w tym samym oknie długiego przytrzymania co zmiana kategorii. Web: nowa mapa `state.pantryStepOverride` (ten sam wzorzec co `pantryUnitOverride`/`pantryCategoryOverride`, dopisana do `SYNCED_STATE_KEYS`/`MAP_MERGE_KEYS`), `effectiveStep()` sprawdza override przed `tileStep()`. Android: świadomie prostsze -- pole `stepOverride` bezpośrednio na `PantryItem.Product` zamiast osobnej trwałej mapy (ginie po pełnym usunięciu śledzenia + ponownym dodaniu, tak jak już wcześniej brak portu zmiany jednostki przez długie przytrzymanie, patrz `android/PARITY.md`). `./gradlew :logic:test :app:compileDebugKotlin` przechodzi (test `PantryTilesTest` zaktualizowany na nową wartość domyślną). Zweryfikowane na żywo na obu platformach: nowo śledzony produkt w ml startuje na 50 ml, okno długiego przytrzymania pokazuje siatkę gotowych wartości z aktualną zaznaczoną, wybór innej trwale zmienia skok +/- tego kafelka.
- **v2** (2026-08-28, Web only): „🗑️ Wyczyść całą spiżarnię” zachowuje
  potwierdzenie (akcja o zasięgu całej spiżarni), ale dodatkowo pokazuje
  powiadomienie z przyciskiem „Cofnij”, przywracającym wszystkie usunięte
  pozycje. Domknięcie audytu akcji destrukcyjnych rozpoczętego w FR-21/v2,
  FR-22/v2 i FR-26/v2 — ta sama zasada: pełny zakres → potwierdzenie ORAZ
  cofnięcie. Cofnięcie dopisuje też własny wpis do historii aktywności
  (FR-42), żeby log odzwierciedlał, co się faktycznie stało, zamiast
  zostawiać samo „Wyczyszczono całą spiżarnię”. Przywracane jest wyłącznie
  `state.pantry` — mapy `pantryUnitOverride`/`pantryCategoryOverride`/
  `pantryStepOverride` i `customTiles` nie są przez ten przycisk kasowane,
  więc są na miejscu i stosują się do przywróconych pozycji. Zweryfikowane
  na żywo (headless Chromium): 2 pozycje + ustawiony override jednostki →
  wyczyszczenie (0 pozycji, override nietknięty) → „Cofnij” → obie pozycje
  z powrotem, override nadal ten sam. CACHE_NAME→v109, `versions/v109/`.


- **v3** (2026-08-29, PORT NA ANDROIDA): „🗑️ Wyczyść całą spiżarnię”
  oferuje teraz „Cofnij” także w aplikacji natywnej, z migawką sprzed
  czyszczenia, więc wracają realne ilości, a nie puste kafelki.

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
- **v2** (2026-08-11, Android): Użytkownik zgłosił "zepsuło się w spiżarni
  menu po przytrzymaniu produktu, nic się nie dzieje". Rzeczywista
  przyczyna: `PantryTile`'s `Modifier.pointerInput(name)` w
  `PantryScreen.kt` był kluczowany WYŁĄCZNIE po `name`, więc jego
  korutyna wykrywania gestów nie restartowała się, gdy `entry`/`category`
  zmieniały się dla tego samego kafelka — trzymała się domknięcia
  (closure) `onLongPress`/`onTap` przechwyconego przy PIERWSZYM złożeniu
  tego kafelka. Konkretnie: kafelek zaczyna niewidoczny/nieśledzony
  (`entry == null`), jego `onLongPress` przechwytuje to (`if (entry !=
  null) ...` — no-op); w momencie gdy użytkownik dotyka górnej połowy, by
  zacząć śledzenie (`entry` staje się nie-`null`), wywołujący przekazuje
  NOWE domknięcie z aktualnym `entry` — ale ponieważ `name` się nie
  zmienił, JUŻ DZIAŁAJĄCA korutyna nigdy go nie przechwytuje i wciąż woła
  ORYGINALNE (trwale no-op) domknięcie — więc przytrzymanie kafelka
  dodanego wcześniej w tej samej sesji nic nie robiło, aż do zniszczenia i
  odbudowania siatki (np. wyjście i powrót na Spiżarnię) dawało mu
  przypadkowo świeży start. Naprawione `rememberUpdatedState` dla
  `onTap`/`onLongPress` — korutyna gestów zawsze czyta AKTUALNE domknięcie
  przy każdym nowym geście, bez potrzeby restartu `pointerInput`.
  Odtworzone i potwierdzone naprawione na żywo na emulatorze (dodanie
  kafelka + natychmiastowe przytrzymanie tego samego kafelka w tej samej
  sesji, przed poprawką menu się nie pojawiało, po poprawce pojawia się
  poprawnie). `./gradlew :app:assembleDebug :app:testDebugUnitTest
  :logic:test` przechodzi.

- **v4** (2026-08-29): Menu po przytrzymaniu kafelka dostało pozycję
  „❌ Usuń produkt ze spiżarni na stałe” — patrz FR-102, gdzie opisana jest
  cała ta funkcja. Dwie zmiany dotykające bezpośrednio TEGO wymagania:
  (a) na Androidzie menu otwiera się teraz dla KAŻDEGO kafelka, nie tylko
  śledzonego (`onLongPress = { actionTarget = ... }` bez warunku
  `entry != null`) — wcześniej akurat dla kafelków, które najbardziej chce
  się usunąć (nieśledzone, wyliczone z bazy przepisów), menu w ogóle nie
  dawało się otworzyć; (b) istniejąca pozycja „🗑️ Usuń śledzenie
  (wyzeruj stan)” zostaje bez zmian i nadal robi dokładnie to co robiła —
  nowa pozycja jej nie zastępuje, tylko dokłada mocniejszy wariant.

---

# FR-31: Skanowanie kodu kreskowego produktu

**Obszar:** Spiżarnia  
**Status:** Wyłączone (nie jest rozwijane)

## Opis
Przycisk w Spiżarni uruchamia podgląd z kamery urządzenia do skanowania kodu kreskowego produktu jako alternatywna metoda dodania go do śledzenia.

Na życzenie użytkownika funkcja jest wyłączona w obu wersjach (web i Android)
i nie jest obecnie rozwijana. W wersji web przycisk „📷 Skanuj kod kreskowy
produktu” jest ukryty (`display:none`) — reszta implementacji (modal, obsługa
kamery, `BarcodeDetector`) zostaje nietknięta w kodzie na wypadek ponownego
włączenia w przyszłości, po prostu nieosiągalna z UI. W wersji Android
funkcja nigdy nie została rozpoczęta (patrz `android/PARITY.md`) — pozostaje
świadomie nierozpoczęta, a nie tylko zaległa.

## Kryteria akceptacji
- Przycisk uruchamiający skaner nie jest widoczny w interfejsie Spiżarni (żadna platforma).
- (Nieaktywne, zachowane dla dokumentacji na wypadek ponownego włączenia): zamknięcie skanera, także przez systemowy przycisk „Wstecz” na Androidzie, zatrzymuje strumień kamery, nie zostawia go działającego w tle.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-10): Funkcja wyłączona na wyraźną prośbę użytkownika — nie jest potrzebna i nie będzie rozwijana na ten moment. Przycisk ukryty w web, nic nie zmienia się w Androidzie (funkcja tam nigdy nie była rozpoczęta).

---

# FR-32: Podpowiedź „🏺 masz w spiżarni” i „Pomysł na danie z ulubionych składników”

**Obszar:** Spiżarnia  
**Status:** Zaimplementowane

## Opis
Lista składników na karcie przepisu pokazuje, które pozycje są już w spiżarni.

Osobne wejście „💡 Pomysł na danie z ulubionych składników” proponuje danie
na podstawie ulubionych składników użytkownika:
- **Web (bez zmian):** przycisk inline pod paskiem filtrów, otwiera okno z
  wygenerowanym z 2 losowych ulubionych składników szablonowym tekstem
  (np. „Sałatka z {a} i {b}”), z możliwością wylosowania innej propozycji
  bez zamykania okna.
- **Android (od 2026-08-11, v2):** floating przycisk „💡” widoczny WYŁĄCZNIE
  na karcie Przepisy (zastąpił dawny przycisk inline). Po kliknięciu:
  1. Jeśli użytkownik nie ma żadnych ulubionych składników — komunikat z
     instrukcją, jak je zaznaczyć (gwiazdka ☆ przy składniku w przepisie).
  2. W przeciwnym razie: najpierw pytanie o typ posiłku (Śniadanie / Obiad /
     Kolacja / Deser).
  3. Po wyborze: dobiera do 5 ulubionych składników, ZRÓŻNICOWANYCH pod
     względem kategorii — z każdej kategorii innej niż Warzywa/Owoce bierze
     co najwyżej JEDEN składnik (żeby nie wyszło danie z samych różnych
     kasz albo z samych różnych mąk), natomiast z Warzyw i Owoców może
     wziąć dowolnie wiele (dopuszczalna np. sałatka z kilku warzyw albo
     deser z kilku owoców). Jeśli ulubione składniki nie obejmują
     wystarczająco zróżnicowanych kategorii, żeby dobrać 5 — zwraca mniej,
     zamiast łamać regułę różnorodności.
  4. Otwiera wyszukiwanie Google (`https://www.google.com/search?q=...`,
     ten sam mechanizm co dotknięcie tytułu przepisu) z zapytaniem „przepis
     na {posiłek} z {lista składników}”.

## Kryteria akceptacji
- Web: bez zmian względem v1 — przycisk inline, min. 2 ulubione składniki
  wymagane, tekst szablonowy, reroll bez zamykania okna.
- Android: floating „💡” widoczny tylko na Przepisach, znika na innych
  kartach. Dialog zawsze pyta o posiłek PRZED wygenerowaniem propozycji
  (nie odwrotnie). Żadna kategoria inna niż Warzywa/Owoce nie występuje
  więcej niż raz w wybranych składnikach. Kliknięcie typu posiłku od razu
  otwiera przeglądarkę z wyszukiwaniem Google i zamyka dialog.

## Uwagi
Świadoma, udokumentowana rozbieżność web/Android (patrz `android/PARITY.md`)
— użytkownik poprosił o tę zmianę wyłącznie w sesji dotyczącej Kotlina;
port analogicznej zmiany do `index.html` pozostaje do rozważenia w
osobnej turze.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-11, Android): Na życzenie użytkownika, przycisk inline
  zastąpiony floating przyciskiem „💡” widocznym tylko na Przepisach;
  algorytm zmieniony z „2 losowe składniki + szablonowy tekst” na „do 5
  składników zróżnicowanych po kategoriach + wyszukiwanie Google”, z
  pytaniem o typ posiłku (Śniadanie/Obiad/Kolacja/Deser) PRZED
  wygenerowaniem propozycji. Nowa logika w `logic/.../FavoriteDishSearch.kt`
  (zastąpił `DishIdeaGenerator.kt`, usunięty razem z testem), z testami
  JUnit (`FavoriteDishSearchTest.kt`) sprawdzającymi m.in. że żadna
  kategoria poza Warzywa/Owoce nie pojawia się więcej niż raz. Nowy
  `ui/FavoriteDishIdeaDialog.kt`, floating FAB dodany w `MainActivity.kt`
  obok istniejącego "➕" (gated na `Screen.Recipes.route`). Web NIE
  zmieniony w tej turze — patrz Uwagi. `./gradlew :app:assembleDebug
  :app:testDebugUnitTest :logic:test` przechodzi. **Nie zweryfikowane na
  żywo** — wymaga sprawdzenia w Android Studio (dodać kilka ulubionych
  składników z różnych kategorii, dotknąć 💡, wybrać posiłek, potwierdzić
  że otwiera się przeglądarka z sensownym zapytaniem).
- **v2** (2026-08-28, Web only): Filtr listy ulubionych składników
  (Ustawienia → Ulubione) przestał być wrażliwy na polskie znaki
  diakrytyczne — ten sam błąd i ta sama naprawa co w FR-2/v6 i FR-34/v3,
  znalezione podczas przeglądu wszystkich miejsc porównujących surowe
  napisy. Przed poprawką wpisanie „platki owsiane”, „salata” czy „chleb
  zytni” nie znajdowało niczego, mimo że wszystkie trzy są na liście.
  Zweryfikowane na żywo (headless Chromium): każde z tych zapytań zwraca
  teraz właściwy kafelek, tak samo jak wersja z ogonkami.
  CACHE_NAME→v111, `versions/v111/`.

---

# FR-33: Globalny przycisk szybkiego dodania przekąski/dania z każdego miejsca

**Obszar:** Szybkie dodawanie i przekąski  
**Status:** Zaimplementowane

## Opis
Zielony przycisk „➕” w nagłówku, widoczny na każdej zakładce (w tym w Planerze), otwiera okienko dodania przekąski lub dodatkowego dania niezależnie od tego, którą część aplikacji użytkownik akurat przegląda.

Dodatkowo na zakładce „Postęp” (główna zakładka aplikacji) znajduje się pływający okrągły przycisk „➕” w prawym dolnym rogu ekranu (nad dolną nawigacją), otwierający dokładnie to samo okienko co przycisk w nagłówku — dzięki temu dodanie czegoś do dziennika nie wymaga sięgania do nagłówka, gdy jest się już na tej zakładce.

## Kryteria akceptacji
- Dodana pozycja pojawia się natychmiast w dziennym bilansie kalorycznym w nagłówku i w zakładce Postępy.
- Pływający przycisk na zakładce Postęp jest widoczny wyłącznie, gdy ta zakładka jest aktywna, i znika przy przełączeniu na inną zakładkę.
- Oba przyciski („➕” w nagłówku i pływający na Postępie) używają tego samego okienka i tej samej logiki dodawania — nie ma dwóch niezależnych implementacji.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-08): Dodano pływający przycisk („FAB”) na zakładce Postęp, na życzenie użytkownika ("dodaj jeszcze pływający button z takim menu wyświetlany na zakładce postęp bo to powinna być główna zakładka").

---

# FR-34: Automatyczne szacowanie kalorii przekąski z bazy 336 produktów

**Obszar:** Szybkie dodawanie i przekąski  
**Status:** Zaimplementowane

## Opis
Formularz dodawania przekąski przyjmuje wolny tekst (np. „1 banan”, „150g ryżu”, „prince polo”) i automatycznie szacuje kalorie na podstawie bazy `SNACK_NUTRITION_DB` (336 pozycji): dla produktów liczonych sztukowo mnoży kaloryczność jednej sztuki przez podaną liczbę, dla pozostałych przelicza z kaloryczności na 100g wg podanej lub typowej gramatury. Jeśli produkt nie zostanie rozpoznany, pole kalorii pozostaje puste do ręcznego uzupełnienia.

Podczas wpisywania nazwy (od 2. wpisanego znaku) pod polem pojawia się lista podpowiedzi — nazwy produktów z bazy zaczynające się od wpisanego tekstu, a w dalszej kolejności te, które go zawierają w środku nazwy (maks. 8 pozycji, z ikoną emoji produktu, jeśli rozpoznana). Kliknięcie podpowiedzi wpisuje pełną nazwę produktu w pole i automatycznie uzupełnia szacowane kalorie — nie trzeba wpisywać całej nazwy ani naciskać Enter. Lista chowa się automatycznie po wybraniu podpowiedzi albo po opuszczeniu pola.

## Kryteria akceptacji
- Rozpoznanie NIE wymaga podania gramatury — bez niej używana jest typowa porcja.
- Podanie gramatury/liczby sztuk zawsze nadpisuje typową wartość dokładnym przeliczeniem.
- Baza pokrywa owoce, warzywa, nabiał, mięso/wędliny, pieczywo/kasze, orzechy/strączki, napoje, słodycze i popularne dania gotowe/restauracyjne.
- Każda pozycja bazy jest zweryfikowana automatycznym testem jako faktycznie rozpoznawalna po wpisaniu (nie tylko obecna w słowniku).
- Podpowiedzi pojawiają się dopiero od 2 znaków wpisanego tekstu (żeby nie zalewać użytkownika setkami wyników przy jednej literze).
- Lista podpowiedzi jest dostępna zarówno w oknie otwartym z nagłówka, jak i z pływającego przycisku na zakładce Postęp (FR-33) — to ten sam formularz.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-08): Dodano listę podpowiedzi podczas wpisywania, na życzenie użytkownika ("jak użytkownik zaczyna pisać to niech system podpowiada co można wpisać i nie czeka na całą nazwę i enter").
- **v3** (2026-08-28, Web only): Podpowiedzi przestały być wrażliwe na
  polskie znaki diakrytyczne. **Realny błąd**, znaleziony przy okazji
  naprawy tego samego problemu w wyszukiwarce przepisów (FR-2/v6) —
  szukanie kolejnych miejsc porównujących surowe napisy ujawniło, że
  **89 z 336 nazw w bazie zawiera ogonek**, a lista podpowiedzi
  porównywała `n.startsWith(q)`/`n.includes(q)` bez normalizacji: wpisanie
  „jablko” dawało ZERO podpowiedzi, mimo że „jabłko” jest w bazie
  (potwierdzone pomiarowo na próbce: „jablko”, „chleb zytni”, „bulka
  pszenna”, „ogorek”, „borowki / jagody”, „twarog bez laktozy” — każde
  zwracało 0 trafień przed poprawką). Dotkliwe zwłaszcza tutaj, bo cała ta
  funkcja istnieje po to, żeby NIE trzeba było wpisywać pełnej,
  dokładnej nazwy. Naprawione istniejącą funkcją `foldDiacritics()` po obu
  stronach porównania, z zachowaniem dotychczasowej kolejności wyników
  (najpierw dopasowania od początku nazwy, potem zawierające frazę w
  środku). Zweryfikowane na żywo (headless Chromium) przez realny przepływ
  UI: „jablko” zwraca teraz „🍎 jabłko / jabłko suszone / 🧃 sok jabłkowy”,
  identycznie jak „jabłko”. CACHE_NAME→v111, `versions/v111/`.

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
Nagłówek pokazuje pierścień postępu dziennego spożycia kalorii względem celu, listę zaplanowanych posiłków dnia z możliwością przesunięcia wiersza w prawo, by oznaczyć posiłek jako zjedzony, oraz podsumowanie zjedzone/pozostało. Pierścień jest podwójny — zewnętrzny łuk (pomarańczowy) to kalorie zjedzone/cel, wewnętrzny łuk (niebieski) to dzisiejsze nawodnienie/8 szklanek, w jednym wspólnym pierścieniu (patrz FR-37). Po lewej stronie pierścienia widoczna jest liczba pozostałych do wykorzystania kalorii.

## Kryteria akceptacji
- Przesunięcie wiersza posiłku poniżej progu cofa się do pozycji wyjściowej bez oznaczenia.
- Oznaczenie/odznaczenie posiłku natychmiast aktualizuje ZARÓWNO pierścień (zewnętrzny łuk), jak i podsumowanie zjedzone/pozostało oraz liczbę pozostałych kalorii przy pierścieniu.
- Dotknięcie kropelki/kubeczka nawodnienia w nagłówku natychmiast aktualizuje wewnętrzny (niebieski) łuk pierścienia.
- Jeśli posiłek w Planerze ma ustawioną skalę porcji (FR-20/21) inną niż 1×, oznaczenie go jako zjedzony musi zapisać kaloryczność PRZESKALOWANĄ (np. porcja 1,3× przepisu 300 kcal → 390 kcal), a nie bazową kaloryczność przepisu — zarówno w wierszu posiłku (liczba przy nazwie dania), jak i w sumie zjedzone/pozostało oraz w samym pierścieniu.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-10): Naprawiono rozbieżność między tym dokumentem a rzeczywistym kodem — mimo że Opis/Kryteria od zawsze opisywały pierścień jako odzwierciedlający SPOŻYCIE (i aktualizujący się przy oznaczeniu posiłku jako zjedzonego), faktyczny kod liczył pierścień z `todaysPlannedKcal()` (zaplanowane dania z Planera), całkowicie niezależnie od `state.eaten` — oznaczenie/odznaczenie posiłku zmieniało tylko linijkę „Zjedzone/Zostało” pod pierścieniem, nie sam pierścień. Przy okazji szerszej restylizacji obu wersji aplikacji pod nowy design system (patrz `android/PARITY.md`), na wyraźne życzenie użytkownika (dopasowanie do referencyjnych zrzutów ekranu, gdzie pierścień jednoznacznie pokazuje zjedzone/pozostałe kalorie), pierścień faktycznie przeliczono na `dailyEatenKcal(dziś)` — teraz zachowanie kodu W KOŃCU zgadza się z tym, co ten dokument opisywał od v1.
- **v3** (2026-08-11): Naprawiono błąd zgłoszony przez użytkownika ("Domowy batonik owocowo-orzechowy przepis pokazuje inna kalorycznośc w przepisach a inna w plenerze i na kółeczku które liczy dzienne spożycie") — WYŁĄCZNIE po stronie Android. Wersja webowa od zawsze poprawnie zapisywała skalowaną kaloryczność przy oznaczeniu posiłku jako zjedzony (`plannedRecipeFor` → `scaleRecipe(...).kcal`). Android natomiast w `HeaderKcalPanel`/`KcalMealRow` (`MainActivity.kt`) przekazywał do przełącznika zjedzenia bazową, NIEskalowaną kaloryczność przepisu (`recipe?.kcal`), całkowicie ignorując `meal.scale` — mimo że dokładnie ta sama, już istniejąca i już przetestowana funkcja (`PlannerOperations.scaledKcal`) była od dawna poprawnie używana dla sumy „Razem” w Planerze. Skutek: przy dowolnym posiłku ze skalą porcji różną od 1× pierścień/„Zjedzone” na Androidzie zaniżał lub zawyżał rzeczywiste spożycie. Naprawiono przekazując wszędzie `PlannerOperations.scaledKcal(recipe, meal.scale)`; przy okazji dodano wyświetlanie tej przeskalowanej wartości przy nazwie dania w wierszu posiłku (np. „Bananowe lody z mrożonego banana (389 kcal)”), dogrywając do formatu, jaki web miał od zawsze. Zweryfikowane bezpośrednio na emulatorze: odznaczenie i ponowne oznaczenie posiłku zapisało poprawną, przeskalowaną wartość („Zjedzone: 389 kcal” zamiast wcześniejszych 314 kcal sprzed naprawy). Szerszy przegląd innych pozycji menu (Przepisy/Planer/pierścień) nie ujawnił żadnego innego błędu obliczeniowego — pozostałe rozbieżności liczbowe między ekranami są zamierzone z projektu (Przepisy pokazują bazową kaloryczność przepisu, wiersz posiłku w nagłówku pokazuje BUDŻET kategorii, nie kaloryczność przepisu).

---

# FR-37: Śledzenie nawodnienia — pełny widok i kompaktowy pasek w nagłówku

**Obszar:** Śledzenie postępów  
**Status:** Zaimplementowane

## Opis
Zakładka Postępy pokazuje interaktywny rząd 8 „kubeczków” (rysowana ikonka, wypełniona = zaznaczona) do zaznaczenia dziennego spożycia wody. W nagłówku (widocznym nawet po jego zwinięciu) pokazywany jest dodatkowo kompaktowy pasek tych samych ikonek kubeczka z liczbą (np. „3/8”), który po dotknięciu dodaje kolejną szklankę i jest zsynchronizowany z pełnym widokiem. Ten sam licznik napędza też wewnętrzny (niebieski) łuk podwójnego pierścienia kalorii w nagłówku (patrz FR-36) — dotknięcie kubeczka w którymkolwiek z trzech miejsc (nagłówek, Postępy, pierścień) natychmiast odświeża pozostałe dwa.

## Kryteria akceptacji
- Zmiana w jednym miejscu (nagłówek, pełny widok lub wewnętrzny łuk pierścienia) natychmiast odzwierciedla się w pozostałych.
- Licznik resetuje się automatycznie o północy (nowy dzień = nowy licznik).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-11): Kropelki 💧/⚪ zastąpiono rysowanym kubeczkiem (ikona SVG w web, `Canvas` w Android) — czysto wizualna zmiana, zachowanie dotknięcia bez zmian. Przy okazji przeprojektowania nagłówka pod podwójny pierścień (FR-36/v2) ten sam licznik nawodnienia zaczął też napędzać wewnętrzny łuk pierścienia, więc dodano trzecie zsynchronizowane miejsce do opisu i kryteriów.

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
**Status:** Zaimplementowane (cofanie wyczyszczenia historii — v2 — na razie Web-only, patrz Historia rewizji)

## Opis
Aplikacja liczy serie kolejnych dni spełniających kryteria (np. pełne nawodnienie, spożycie w granicach celu) oraz prowadzi dziennik aktywności (dodania/usunięcia z listy zakupów i spiżarni). Domyślnie pokazywanych jest 20 najnowszych wpisów historii z przyciskiem „Pokaż całą historię (N)”; działa też filtr po zakresie dat, który ignoruje limit 20 i pokazuje wszystkie pasujące wpisy.

## Kryteria akceptacji
- Limit 20 dotyczy WYŁĄCZNIE domyślnego widoku bez aktywnego filtra dat — cała historia jest zawsze zachowana w danych.
- Filtrowanie po dacie i limit „20 najnowszych” nie wykluczają się — filtr dat nadpisuje limit, nie odwrotnie.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-28, Web only): „Wyczyść całą historię aktywności”
  zachowuje potwierdzenie, ale dodatkowo pokazuje powiadomienie z
  przyciskiem „Cofnij”, przywracającym wszystkie wpisy. Domknięcie audytu
  akcji destrukcyjnych rozpoczętego w FR-21/v2, FR-22/v2 i FR-26/v2 — ta
  sama zasada dla akcji o pełnym zakresie. Zweryfikowane na żywo (headless
  Chromium): wyczyszczenie historii (0 wpisów) → „Cofnij” → wszystkie wpisy
  z powrotem, w tej samej kolejności. CACHE_NAME→v109, `versions/v109/`.


- **v3** (2026-08-29, PORT NA ANDROIDA): „Wyczyść" przy historii aktywności
  oferuje teraz „Cofnij" także w aplikacji natywnej — migawka listy robiona
  przed wyczyszczeniem, więc wracają wszystkie wpisy w oryginalnej
  kolejności, a nie przybliżenie odtworzone z tego, co zdarzyło się później.
  Pusta historia nie proponuje cofnięcia, bo nie ma czego cofać. Domyka to
  audyt akcji destrukcyjnych z FR-21/v3, FR-22/v3, FR-26/v3 i FR-28/v3 po
  stronie Androida. Zweryfikowane na emulatorze: wpis „Spiżarnia: kurczak
  (pierś) (−1)" → „Wyczyść" → historia pusta + powiadomienie „Wyczyszczono
  historię aktywności (1)" → „Cofnij" → wpis z powrotem.

  **Uwaga metodologiczna do przyszłych weryfikacji**: przy pierwszych
  próbach cofnięcie wyglądało na niedziałające, bo między zrzutem drzewa UI
  (`uiautomator dump` potrafi zająć kilka sekund) a dotknięciem przycisku
  powiadomienie zdążyło zniknąć — a dotknięcie trafiało wtedy w pole daty
  POD nim (widać to w logcacie: otwierała się klawiatura). Testując
  powiadomienia z akcją, trzeba wykonać pokazanie i dotknięcie w JEDNYM
  wywołaniu `adb shell` z krótkim `sleep`, inaczej mierzy się czas własnego
  narzędzia, a nie zachowanie aplikacji.

---

# FR-43: Pasek filtrów i kategorii przyklejony pod nagłówkiem

**Obszar:** Nagłówek i nawigacja  
**Status:** Zaimplementowane

## Opis
Na widoku Przepisy pasek kategorii i filtrów (FR-2, w tym pole wyszukiwania) jest position:sticky, zadokowany bezpośrednio pod nagłówkiem — pozostaje widoczny podczas przewijania listy niezależnie od tego, czy nagłówek jest akurat zwinięty czy rozwinięty. Wysokość nagłówka jest śledzona na bieżąco (ResizeObserver), by pasek zawsze przylegał do jego aktualnej krawędzi, także w trakcie animacji zwijania. Od 2026-08-11 cały ten pasek (pole wyszukiwania + pigułki kategorii + wszystkie przełączniki filtrów) chowa się i pokazuje RAZEM z nagłówkiem (FR-44/FR-45's te same zasady: tylko blisko góry listy, chowa się niżej niezależnie od kierunku przewijania) — wcześniej pasek zostawał zawsze widoczny (tylko przesuwał się wyżej wraz ze zwijaniem nagłówka), co razem z samym nagłówkiem zajmowało zbyt dużo ekranu.

## Kryteria akceptacji
- Pasek nigdy nie zachodzi na treść nagłówka ani nie zostawia szpary między nimi, niezależnie od stanu zwinięcia.
- Poziomy pasek kategorii (pigułki) przewija się bez widocznego paska przewijania pod żadnym pozorem, mimo że mieści się w jednej linii (patrz historia rewizji).
- Cały pasek (pole wyszukiwania, pigułki kategorii, przełączniki filtrów) chowa się/pokazuje w dokładnie tym samym momencie co nagłówek — nigdy nie widać jednego bez drugiego ani z opóźnieniem.

## Uwagi
Zrewidowane 2026-08-03: pierwotna wersja paska pigułek kategorii nie ukrywała natywnego paska przewijania przeglądarki, co po przypięciu paska na stałe pod nagłówkiem stało się szczególnie widoczne i przeszkadzające. Naprawiono przez `scrollbar-width:none` / ukrycie paska WebKit, zachowując przewijanie dotykiem.

Zrewidowane 2026-08-11, na wyraźną prośbę użytkownika ("szukajka na stronie z przepisami niech się zachowuje tak samo jak header, bo zasłania za dużo ekranu niech się chowa i pokazuje tylko u góry"): dodano współdzielone chowanie z nagłówkiem. Web: czysto CSS-owe (`header.app-top.collapsed ~ main .recipes-sticky-bar{max-height:0;opacity:0}`), więc pasek nigdy nie może rozjechać się z nagłówkiem — nie ma osobnego stanu JS do synchronizowania. Android: `RecipeListScreen` dostał nowy parametr `headerExpanded: Boolean` (mirror `MainActivity`'s własnego `headerExpanded`), owijający pole wyszukiwania + pigułki + filtry w `AnimatedVisibility`.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-03): Doprecyzowano zachowanie na podstawie zgłoszonej poprawki — patrz sekcja "Uwagi" powyżej.
- **v3** (2026-08-11): Cały pasek teraz chowa się/pokazuje razem z nagłówkiem (FR-44/FR-45) zamiast zostawać zawsze widoczny — patrz sekcja "Uwagi" powyżej.

---

# FR-44: Automatyczne chowanie/pokazywanie nagłówka na przewijanie (tylko Przepisy)

**Obszar:** Nagłówek i nawigacja  
**Status:** Zaimplementowane

## Opis
Na widoku Przepisy nagłówek pokazuje się automatycznie WYŁĄCZNIE blisko samej góry listy (kilkadziesiąt pikseli od y=0). Gdziekolwiek niżej zostaje cały czas schowany, niezależnie od kierunku przewijania — przewinięcie w górę, ale nie z powrotem do samej góry, już go NIE pokazuje. Jedyny sposób, żeby zobaczyć nagłówek niżej na liście, to ręczne rozwinięcie (patrz FR-45). Na pozostałych zakładkach nagłówek jest domyślnie zwinięty i nie reaguje automatycznie na przewijanie w ogóle.

## Kryteria akceptacji
- Wejście na zakładkę Przepisy zawsze resetuje nagłówek do stanu rozwiniętego i wznawia normalne zachowanie automatyczne, kasując wcześniejsze ręczne zablokowanie (patrz FR-45).
- Przewinięcie w dół poza strefę bliską góry chowa nagłówek.
- Przewinięcie w górę, które NIE sięga z powrotem strefy bliskiej góry, nie przywraca nagłówka — nie ma już "pokaż na przewinięcie w górę gdziekolwiek na liście".
- Powrót do samej góry listy (y bliskie 0) zawsze pokazuje nagłówek ponownie, automatycznie, bez potrzeby ręcznej interwencji.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-11): Zmieniono z zachowania kierunkowego (pokaż przy przewinięciu w górę gdziekolwiek na liście) na "tylko blisko samej góry", na wyraźną prośbę użytkownika ("górny header nawet na stronie głównej z przepisami niech tylko rozwijają się na górze listy z przepisami jak już się jest niżej to niech będzie cały czas schowany dopóki użytkownik sam nie wymusi rozwinięcia"). Kierunkowe zachowanie (nieopisane wcześniej osobną rewizją tego dokumentu, tylko komentarzem w kodzie) samo było wcześniejszą, świadomą odpowiedzią na zgłoszenie "nie rozwija się, chyba że jestem na samej górze" — użytkownik zdecydował się teraz na odwrót, świadomie akceptując, że nagłówek nie pokaże się automatycznie niżej na liście.

---

# FR-45: Ręczne zwijanie/rozwijanie nagłówka ma pierwszeństwo nad automatyką

**Obszar:** Nagłówek i nawigacja  
**Status:** Zaimplementowane

## Opis
Nagłówek można ręcznie zwinąć/rozwinąć dotknięciem całego paska z nazwą aplikacji (poza samymi przyciskami-ikonami w rogu, które zachowują swoje własne działanie). Zamiast osobnego, oprawionego w kwadrat przycisku strzałki, stan zwinięcia sygnalizuje subtelna strzałeczka (chevron) osadzona bezpośrednio przy nazwie aplikacji w nagłówku — obraca się o 180° w zależności od stanu, nie stanowi osobnego, oddzielnie klikalnego elementu. KAŻDA ręczna zmiana (zarówno zwinięcie, jak i rozwinięcie) zamraża automatyczne pokazywanie/chowanie na przewijaniu (FR-44), dopóki użytkownik sam nie zmieni tego ponownie ręcznie albo nie wejdzie na zakładkę Przepisy od nowa — inaczej ręczne rozwinięcie niżej na liście (gdzie automatyka z FR-44 domyślnie chce nagłówek schowany) zostałoby natychmiast cofnięte przez kolejne przewinięcie, co czyniłoby "wymuszenie rozwinięcia" bezcelowym.

## Kryteria akceptacji
- Dotknięcie ikon w rogu nagłówka (⚙️ ustawienia, ➕ szybkie dodawanie) NIGDY nie uruchamia dodatkowo zwijania/rozwijania paska nazwy pod spodem.
- Strzałeczka przy nazwie aplikacji wizualnie odzwierciedla aktualny stan (obrócona, gdy nagłówek zwinięty), ale sama nie jest osobnym przyciskiem — kliknięcie działa przez cały pasek nazwy zgodnie z powyższym opisem.
- Otwarcie i zamknięcie dowolnego okienka modalnego (również przyciskiem „X” w rogu okienka) NIE cofa ręcznego zwinięcia nagłówka.
- Ręczne rozwinięcie nagłówka podczas przewinięcia niżej na liście przepisów (gdzie FR-44's automatyka domyślnie trzyma go schowanym) zostaje widoczne i NIE jest natychmiast cofane przez kolejne przewinięcie — trwa, dopóki użytkownik sam go nie zwinie albo nie wróci do zakładki Przepisy od nowa.

## Uwagi
Zrewidowane 2026-08-03 (v2): znaleziono i naprawiono błąd, w którym otwarcie okienka „ℹ️” (FR-12) potrafiło samoczynnie rozwinąć ręcznie zwinięty nagłówek. Ówczesną łatką było zablokowanie przewijania tła strony (`overflow-y:hidden`) na czas otwartego okienka — adresowała ona wiarygodny, ale nietrafny scenariusz ("przeciekające" przewijanie spod okienka uruchamiające automatykę z FR-44).

Zrewidowane ponownie 2026-08-03 (v3): usterka nadal występowała przy zamykaniu okienka przyciskiem „X”. Rzeczywista przyczyna: zamknięcie okienka usuwa klasę „show” synchronicznie, co uruchamia mechanizm cofania sztucznego wpisu w historii przeglądarki (`history.back()`) dodanego przy otwarciu okienka (ochrona przed przypadkowym wyjściem z aplikacji przyciskiem "wstecz", patrz FR-27/FR-12). Zanim zdarzenie `popstate` wywołane przez to `history.back()` faktycznie się uruchamiało, okienko było już zamknięte — więc obsługa `popstate` mylnie traktowała to jak nawigację między widokami i wywoływała przełączenie widoku na "Przepisy", które przy okazji bezwarunkowo resetowało stan zwinięcia nagłówka. Naprawiono flagą pomijającą to zbędne przełączenie widoku, gdy `history.back()` jest wywoływane tylko w celu posprzątania historii po zamkniętym już oknie.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-03): Doprecyzowano zachowanie na podstawie zgłoszonej poprawki (blokada przewijania tła) — patrz sekcja "Uwagi" powyżej.
- **v3** (2026-08-03): Usunięto osobny, oprawiony przycisk zwijania/rozwijania na rzecz subtelnej strzałeczki przy nazwie aplikacji; opisano prawdziwą przyczynę ponownego rozwijania nagłówka po zamknięciu okienka przyciskiem „X” oraz jej ostateczną poprawkę — patrz sekcja "Uwagi" powyżej.
- **v4** (2026-08-11): Rozszerzono "zamrożenie automatyki" z samego ręcznego zwinięcia na KAŻDĄ ręczną zmianę (też rozwinięcie) — konieczna konsekwencja FR-44/v2 (auto-pokazywanie zawężone do "tylko blisko samej góry"): bez tego rozszerzenia ręczne rozwinięcie niżej na liście zostałoby natychmiast cofnięte przez najbliższe przewinięcie, bo automatyka i tak chciałaby tam nagłówek schowany.

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
Ustawienia pozwalają wybrać jeden z jedenastu motywów wizualnych (domyślny zielony, jasny, różowy, ciemny, zbiory, cytrusowy, miętowy, jagodowa noc, polaroid, Fluent, Kafelki), z których każdy definiuje własną paletę kolorów, pary fontów i krzywe animacji dopasowane do charakteru motywu. Motywy „Fluent” i „Kafelki” (patrz FR-63) nawiązują odpowiednio do stylistyki Windows 11 i klasycznego interfejsu kafelkowego Windows (Metro).

## Kryteria akceptacji
- Zmiana motywu jest natychmiastowa i zapisywana w profilu.
- Kolor paska statusu przeglądarki (`theme-color`) jest zsynchronizowany z wybranym motywem.
- Wszystkie zaokrąglone przyciski aplikacji (`.btn`) mają promień zaokrąglenia 16px (podniesiony z 12px na życzenie użytkownika — bardziej "półokrągły", nowoczesny wygląd), niezależnie od wybranego motywu.
- Karty (`.card`) mają promień zaokrąglenia 26px, niezależnie od wybranego motywu.
- Motywy „Zielony (domyślny)” i „Ciemny” dzielą TĘ SAMĄ tożsamość kolorystyczną nagłówka i akcentu (ciemnozielony gradient nagłówka, musztardowy akcent) — różnią się WYŁĄCZNIE tłem/kartami/tekstem (jasne vs. prawie czarne), nie osobną paletą marki. To jedyna para motywów o tej własności — pozostałe 9 motywów mają każdy własną, niezależną tożsamość.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-03): Dodano motywy „Fluent” i „Kafelki” (łącznie jedenaście motywów, patrz FR-63) oraz globalne pogrubienie zaokrąglenia przycisków — patrz Opis i Kryteria akceptacji.
- **v3** (2026-08-10): Odświeżono paletę motywu „Zielony (domyślny)” i „Ciemny” na życzenie użytkownika, na podstawie referencyjnych zrzutów ekranu innej aplikacji do liczenia kalorii (część szerszej restylizacji obu wersji aplikacji pod wspólny design system — patrz `android/PARITY.md`). Nagłówek: ciemnoleśny zielony gradient `#1B5E3F -> #2E7D5B` (wcześniej `#1F6B5C -> #123D34`). Akcent: żywy musztardowy `#F5A623` (wcześniej przygaszony brązowy `#C98A3E`). „Ciemny” przestał mieć własny, odrębny stonowany zielony akcent (`#2F9078`/`#E0A559`) — dostał TEN SAM zielono-musztardowy charakter co „Zielony (domyślny)”, różniąc się już tylko tłem/kartami (prawie czarne `#0D0D0D`/`#1A1A1A` zamiast wcześniejszego stonowanego `#151A18`/`#1E2523`). Karty dostały mocniej zaokrąglone rogi (16px -> 26px, nowe kryterium akceptacji). Pozostałych 9 motywów NIE dotknięto.

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
Service Worker cache'uje zasoby aplikacji, serwując wersję z pamięci podręcznej natychmiast, a w tle sprawdzając i podmieniając na nowszą wersję z sieci, jeśli jest dostępna. Numer wersji cache (`CACHE_NAME`) jest podnoszony przy KAŻDEJ turze dotykającej `index.html`/`sw.js`/`manifest.json` — bez wyjątków dla zmian ocenianych jako "nieistotne" dla samego mechanizmu cache'owania — i od v85 zawsze zgodny z numerem folderu `versions/vNN` tej tury (jeden wspólny licznik, nie dwa osobne).

## Kryteria akceptacji
- Zmiana kontrolera Service Workera (nowa wersja przejęła kontrolę) wywołuje jednorazowe automatyczne odświeżenie strony, by nowa wersja była widoczna od razu, a nie dopiero przy drugim otwarciu.
- `CACHE_NAME` w `sw.js` jest zawsze równy numerowi najnowszego folderu `versions/vNN` — nigdy nie zostaje "w tyle" nawet o jedną turę, niezależnie od tego, czy dana zmiana subiektywnie wygląda na wymagającą wymuszonego odświeżenia cache'u (patrz FR-82, gdzie ten numer pełni rolę widocznego dla użytkownika potwierdzenia wersji, nie tylko wewnętrznego cache-bustera).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-23): Naprawiono zgłoszony błąd użytkownika ("wersja PWA cały
  czas pokazuje v57 w opcjach") — `CACHE_NAME` przestał być podnoszony po
  wersji v77 (Service Worker v51), mimo że numeracja `versions/vNN` doszła
  do v84 (w tym v84's jawna, ale błędna decyzja "CACHE_NAME zostaje bez
  zmian, bo te poprawki nie zmieniają struktury danych" — trafne dla
  mechaniki cache'u, ale mijające się z FR-82's właściwym celem: widoczne
  potwierdzenie wersji dla użytkownika). Podniesiony do `dieta-app-v85`,
  scalony z numeracją `versions/vNN` na stałe zamiast dwóch rozjeżdżających
  się liczników — patrz zaktualizowane Kryteria akceptacji.
- **v3** (2026-08-24): Naprawiono zgłoszony błąd użytkownika ("aplikacja
  wisi kilkanaście sekund po zalogowaniu") — konsola przeglądarki (dostęp
  przez DevTools użytkownika, nie przez zdalne narzędzie) pokazała
  powtarzający się nieobsłużony wyjątek `Failed to execute 'put' on
  'Cache': Request scheme 'chrome-extension' is unsupported` w `sw.js`'s
  `fetch` handlerze, obok błędu we WŁASNYM skrypcie zupełnie INNEGO
  zainstalowanego rozszerzenia przeglądarki, które wyglądało na próbujące
  się resetować w pętli. `sw.js`'s `fetch` listener przechwytywał
  DOSŁOWNIE każde żądanie strony (niezależnie od metody/schematu URL) i
  bezwarunkowo próbował `caches.open(...).then(cache => cache.put(...))`
  na wyniku — co rzuca wyjątek dla dowolnego żądania spoza http(s) (np.
  zasobu `chrome-extension://` wstrzykniętego przez inne rozszerzenie do
  tej strony), a `cache.put` nie miał żadnego `.catch()`, więc każde takie
  odrzucenie leciało jako nieobsłużone. Jeśli to inne rozszerzenie
  rzeczywiście ponawiało próby w pętli, każda ponowna próba przechodziła
  przez ten sam, wadliwy kod, potęgując obciążenie karty. Naprawione:
  `fetch` listener teraz od razu `return`uje (nie wywołuje
  `event.respondWith` wcale) dla każdego żądania, które nie jest zwykłym
  GET-em po http(s) — więc takie żądania idą prosto do sieci, bez
  dotykania Cache API w ogóle — a `cache.put(...)` dostał też własny
  `.catch(()=>{})` jako dodatkowe zabezpieczenie. **Nie jest pewne, czy to
  w pełni tłumaczy całe "kilkanaście sekund zawieszenia"** — prawdziwym
  źródłem obciążenia może być głównie to INNE rozszerzenie, na które nie
  ma tu wpływu; ta poprawka usuwa jedyną część problemu, którą kod tej
  aplikacji faktycznie kontroluje (własne nieobsłużone odrzucenia
  Promise'ów przy każdym takim żądaniu). `CACHE_NAME` podniesiony do
  `dieta-app-v92` zgodnie z Kryteriami akceptacji (zawsze równy numerowi
  bieżącego folderu `versions/vNN`).

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
Na liście przepisów kartę można przesunąć w prawo (❤️ „podoba się”) lub w lewo („nie podoba się”). Gest wykorzystuje blokadę osi: dopiero przekroczenie progu ruchu w jednym kierunku „zamyka” gest na oś poziomą (ocena) albo pionową (zwykłe przewijanie listy) — więc przewijanie strony nigdy nie jest przechwytywane jako próba oceny.

**Od FR-84 (2026-08-11)**: przesunięcie nie ustawia już osobnej binarnej flagi lubię/nie lubię — jest skrótem do TEJ SAMEJ oceny gwiazdkowej co FR-67: w prawo = 5★, w lewo = 1★, z zachowaniem ewentualnego istniejącego komentarza. Zobacz FR-84 po pełny opis scalenia.

## Kryteria akceptacji
- Przesunięcie poniżej progu zatwierdzenia (90px) wraca do pozycji wyjściowej bez zapisania oceny.
- Sam gest oceniania nigdy nie blokuje zwykłego przewijania listy w pionie.
- Przesunięcie w prawo/lewo ustawia gwiazdki (5★/1★) w tym samym miejscu, które czyta/pokazuje okienko „⭐ Oceń i skomentuj” (FR-67) — patrz FR-84.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-11): Scalone z FR-17/FR-57/FR-67 w jeden mechanizm oceniania — patrz FR-84.

---

# FR-56: Duży, balonowy napis podczas oceniania przesunięciem

**Obszar:** Ocenianie i ranking przepisów  
**Status:** Zaimplementowane

## Opis
Podczas przesuwania karty w trakcie oceniania (FR-55), na środku EKRANU (nie karty) pojawia się rosnący wraz z siłą przesunięcia napis „Podoba się to dla mnie!” albo „Nie podoba się to dla mnie!”, w dużej, zaokrąglonej czcionce z efektem liter jak z grubego, pękatego balonu (baloniki do zwierzątek): gruby kolorowy kontur niosący kształt litery, jasny pastelowy wypełniacz, jasna smuga u góry i cień u dołu budujące wrażenie okrągłej, napompowanej rurki. Napis jest jednym wspólnym elementem, niezależnym od pozycji/przesunięcia (transform) karty — stoi nieruchomo na środku ekranu, podczas gdy karta przesuwa się pod nim. Napis znika po puszczeniu karty. Domyślnie sama karta NIE zmienia koloru/obramowania podczas przesuwania — feedback wizualny niesie wyłącznie napis (patrz FR-61: styl można zmienić w Ustawieniach).

## Kryteria akceptacji
- Rozmiar napisu rośnie proporcjonalnie do siły przesunięcia (od ok. 70% do 120% skali bazowej).
- Napis nie blokuje interakcji z kartą (pointer-events wyłączone) i nie wpływa na próg zatwierdzenia oceny.
- Napis pozostaje wizualnie nieruchomy w centrum ekranu podczas całego gestu przesuwania — nie przesuwa się razem z kartą, nawet gdy karta jest przewinięta poza centrum widoku.
- Kontur liter jest wyraźnie gruby/pękaty (nie cienka linia) — spójny z efektem „balonika-zwierzątka” opisanym powyżej.
- W domyślnym stylu „Balonowa czcionka” karta pod napisem pozostaje w swoim normalnym kolorze — nie jest tintowana na zielono/czerwono.
- Alternatywny styl „Kolorowa karta” (wybierany w Ustawieniach, patrz FR-61) przywraca klasyczne kolorowe obramowanie/poświatę karty podczas przesuwania, niezależnie od napisu.

## Uwagi
Zrewidowane 2026-08-03 (v2): pierwsza wersja używała zwykłego pogrubionego tekstu z gradientowym wypełnieniem w jednolitym kolorze i ZAWSZE tintowała też całą kartę na zielono/czerwono. Na prośbę użytkownika: (1) zmieniono treść napisów, (2) przeprojektowano wygląd liter na bardziej dosłowny efekt „balonika-zwierzątka” (gruby kontur + jasny cienki wypełniacz zamiast jednolitego gradientu), (3) tintowanie całej karty przeniesiono do osobnego, opcjonalnego stylu wybieranego w Ustawieniach (FR-61), a nowym domyślnym zachowaniem jest sam napis bez kolorowania karty.

Zrewidowane 2026-08-03 (v3, treść napisów): pierwsza poprawka (v3 poniżej) zmieniła treść na „Lubię to!”/„Nie lubię!” — to nie było tym, o co prosił użytkownik. Poprawiono na dokładnie zgłoszoną treść: „Podoba się to dla mnie!” / „Nie podoba się to dla mnie!”.

Zrewidowane 2026-08-03 (v5): napis był dotąd elementem potomnym karty i dziedziczył jej transform (przesunięcie), więc wizualnie "jechał" razem z kartą zamiast stać w miejscu — niezgodnie z intencją użytkownika. Zamieniono z elementu per-karta na jeden globalny element `position:fixed`, sterowany tym samym poziomem intensywności przesunięcia, ale pozycjonowany niezależnie na środku ekranu. Przy okazji pogrubiono kontur liter („pękate” na życzenie użytkownika — poprzednia grubość konturu wydawała się zbyt cienka).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-03): Doprecyzowano zachowanie na podstawie zgłoszonej poprawki — patrz sekcja "Uwagi" powyżej.
- **v3** (2026-08-03): Zmieniono treść napisów na „Lubię to!”/„Nie lubię!”, przeprojektowano na efekt "balonika-zwierzątka" i wydzielono tintowanie karty do osobnego, opcjonalnego stylu (FR-61).
- **v4** (2026-08-03): Treść napisów poprawiona na dokładnie zgłoszoną wersję „Podoba się to dla mnie!”/„Nie podoba się to dla mnie!”, po tym jak v3 nie trafiła w to, o co prosił użytkownik — patrz zaktualizowana sekcja "Uwagi".
- **v5** (2026-08-03): Napis odpięty od transformu karty i wyśrodkowany niezależnie na ekranie zamiast na karcie; kontur liter pogrubiony dla bardziej "pękatego" wyglądu — patrz zaktualizowana sekcja "Uwagi".

---

# FR-57: Trwałe oznaczenie oceny i ranking sort

**Obszar:** Ocenianie i ranking przepisów  
**Status:** Zaimplementowane

## Opis
Oceniona karta zachowuje kolorowe obramowanie z boku i małą plakietkę.

**Od FR-84 (2026-08-11)**: plakietka pokazuje teraz „★N” (liczbę gwiazdek z FR-67) zamiast dawnego 👍/👎, a dotknięcie jej otwiera okienko oceny zamiast kasować ocenę jednym dotknięciem — spójnie z tym, że to już jeden, wspólny mechanizm oceniania (usuwanie oceny nadal jest możliwe, przyciskiem „Usuń ocenę” w tym okienku). Kolor obramowania: 4-5★ zielone, 1-2★ czerwone, 3★/brak neutralne. Osobny przełącznik sortowania „❤️ Ranking” został USUNIĘTY jako redundantny z „🏆 Ocena” (FR-67) — po scaleniu obie robiłyby dokładnie to samo. Zobacz FR-84 po pełny opis.

## Kryteria akceptacji
- Ocenione karty NIE znikają z listy (świadoma różnica względem klasycznego 'Tindera' z pojedynczym stosem kart) — Przepisy to przewijalna lista wielu dań, nie stos pojedynczych kart.
- Plakietka pokazuje aktualną liczbę gwiazdek i jest widoczna tylko, gdy przepis ma jakąkolwiek ocenę.
- Dotknięcie plakietki otwiera okienko oceny — nie kasuje oceny bez potwierdzenia.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-11): Scalone z FR-17/FR-55/FR-67 w jeden mechanizm oceniania (plakietka ★N, osobny przełącznik sortowania usunięty jako redundantny) — patrz FR-84.

---

# FR-58: Dodawanie składników z konkretnego dnia na liście zakupów

**Obszar:** Lista zakupów  
**Status:** Zaimplementowane

## Opis
Zakładka Zakupy pokazuje, obok istniejącego przycisku dodania składników z całego tygodnia (FR-25/FR-27), pasek 7 kart — po jednej na dzień tygodnia. Dwa pierwsze dostępne dni są etykietowane względem dzisiejszej daty jako „Dziś” i „Jutro”/„Pojutrze” (obliczane na bieżąco z rzeczywistej daty systemowej), pozostałe pokazują skrócone nazwy dni tygodnia. Każda karta jednocześnie (a) opisuje wprost po polsku swój aktualny stan względem Planera na ten dzień, i (b) jest przyciskiem: stuknięcie dodaje do listy zakupów składniki wszystkich dań zaplanowanych w Planerze na ten jeden dzień, z uwzględnieniem ustawionej skali porcji (FR-20). Stany karty: „—” (wyszarzona, brak zaplanowanych dań tego dnia), „Dodaj” (są zaplanowane dania, żadne jeszcze nie na liście), „X/Y” z cienkim paskiem postępu (część dań na liście), „Gotowe” z pełnym paskiem (wszystkie zaplanowane dania tego dnia już na liście). Dzisiejszy dzień ma dodatkowo wyróżnioną obwódkę.

To jeden, samowystarczalny widżet zastępujący dwa wcześniejsze, osobno renderowane obok siebie elementy: sam rząd przycisków dodawania (opisany pierwotnie w tym FR) oraz osobny, czysto informacyjny "mini kalendarzyk" z pierścieniami wypełnienia (FR-62) — użytkownik zgłosił, że oba obok siebie były niejasne (dwa podobne wizualnie rzędy dla tych samych 7 dni, jeden klikalny i jeden nie, bez jasnego wytłumaczenia co przedstawiają same pierścienie).

## Kryteria akceptacji
- Etykiety „Dziś”/„Jutro”/„Pojutrze” zawsze odpowiadają rzeczywistemu dzisiejszemu dniowi tygodnia, nie stałemu indeksowi.
- Kliknięcie karty dodaje składniki TYLKO z wybranego dnia, nie z całego tygodnia.
- Dania już wcześniej dodane do listy nie są duplikowane (ta sama logika co FR-25) — kliknięcie karty w stanie „Gotowe” jest bezpieczne (idempotentne) i pokazuje komunikat, że wszystko już jest na liście, zamiast dodawać cokolwiek ponownie.
- Stan karty („—” / „Dodaj” / „X/Y” z paskiem / „Gotowe”) jest czytelny sam z siebie, bez potrzeby najeżdżania/przytrzymywania — dokładny opis liczbowy (X/Y dań) dostępny dodatkowo w atrybucie tytułu przy najechaniu/przytrzymaniu.
- Pusty dzień (bez zaplanowanych dań) pokazuje odpowiedni komunikat po kliknięciu, zamiast cichego braku reakcji.

## Uwagi
Zrewidowane 2026-08-07: połączono z FR-62 w jeden widżet po zgłoszeniu użytkownika, że dwa osobne, wizualnie podobne rzędy nad listą zakupów (ten rząd przycisków + informacyjne pierścienie z FR-62) były nieczytelne. Zamiast abstrakcyjnego częściowo wypełnionego pierścienia (wymagającego tooltipa, by zrozumieć co przedstawia) każda karta opisuje swój stan wprost tekstem po polsku plus prostym liniowym paskiem postępu, i sama pełni funkcję przycisku dodawania — jeden widżet zamiast dwóch.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie polecenia użytkownika.
- **v2** (2026-08-07): Połączono z osobnym widżetem "mini kalendarzyka" (FR-62) w jeden pasek kart dni, jednocześnie informacyjny i klikalny — patrz sekcja "Uwagi".

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

# FR-62: Mini kalendarzyk bieżącego tygodnia na liście zakupów

**Obszar:** Lista zakupów  
**Status:** Połączone z FR-58 (patrz Uwagi) — funkcjonalność nadal istnieje, w innej formie

## Opis (historyczne, jak pierwotnie zaimplementowane)
Na górze zakładki Zakupy, pod podsumowaniem listy, wyświetlał się mini kalendarzyk obejmujący wyłącznie bieżący tydzień (poniedziałek–niedziela, zgodnie z układem dni w Planerze). Każdy dzień pokazywał pierścień (SVG), który wypełniał się proporcjonalnie do tego, ile z zaplanowanych na ten dzień dań (Planer) miało już swoje składniki dodane do listy zakupów — np. jeśli z 4 zaplanowanych na dany dzień posiłków 2 miały składniki na liście, pierścień był wypełniony w połowie. Renderowany jako osobny rząd, TUŻ NAD osobnym rzędem przycisków dodawania per dzień (FR-58).

## Uwagi
Zrewidowane 2026-08-07: użytkownik zgłosił, że ten pierścień był nieczytelny ("nie wiadomo o co chodzi") — wymagał najechania/przytrzymania, by zrozumieć co przedstawia, a wizualnie prawie nakładał się na osobny rząd przycisków dodawania (FR-58) tuż pod spodem, sprawiając wrażenie dwóch niepowiązanych widżetów dla tych samych 7 dni. Połączono oba w jeden widżet w ramach FR-58: ten sam odczyt stanu (ile dań danego dnia jest już na liście względem Planera) jest teraz częścią jednej klikalnej karty dnia, opisanego wprost tekstem po polsku ("Dodaj" / "X/Y" z liniowym paskiem postępu / "Gotowe") zamiast abstrakcyjnym częściowo wypełnionym pierścieniem. Ten plik zostaje jako historyczny zapis pierwotnej wersji — właściwa, aktualna specyfikacja tej funkcjonalności znajduje się teraz w FR-58.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania na podstawie polecenia użytkownika.
- **v2** (2026-08-07): Połączone z FR-58 w jeden widżet po zgłoszeniu nieczytelności — patrz sekcja "Uwagi". Właściwa specyfikacja przeniesiona do FR-58.

---

# FR-63: Motywy „Fluent” i „Kafelki” inspirowane Windows 11 / Metro

**Obszar:** Wygląd i motywy  
**Status:** Zaimplementowane

## Opis
Dwa dodatkowe motywy wizualne (patrz FR-48), inspirowane dwiema odrębnymi erami stylistyki Windows, każdy ze strukturalnie odmiennym traktowaniem kart, nie tylko inną paletą kolorów:

- **Fluent** — nawiązanie do Windows 11 (Fluent Design/Mica): stonowana neutralna szarość tła, stłumiony niebiesko-fioletowy akcent, zaokrąglone karty o miękkim, niskokontrastowym wyglądzie, płynne, spowalniające krzywe animacji (`cubic-bezier(.16,1,.3,1)`).
- **Kafelki** (Metro) — nawiązanie do klasycznego, kafelkowego interfejsu Windows: mocne, nasycone barwy (kobalt, magenta, turkus), płaskie karty przepisów z ostrymi (nie zaokrąglonymi) rogami i kolorowym pionowym paskiem akcentu z boku, kafelki spiżarni bez cienia z ostrymi rogami, szybkie, mechaniczne krzywe animacji (`cubic-bezier(.4,0,.2,1)`, krótszy czas trwania).

## Kryteria akceptacji
- Oba motywy definiują pełną paletę kolorów (w tym `--danger`, `--star-off`) oraz własne pary fontów nagłówek/treść/mono.
- Motyw „Kafelki” zauważalnie zmienia KSZTAŁT elementów (karty przepisów, kafelki spiżarni) — ostre rogi, brak cienia na kartach nierozwiniętych, kolorowy pasek akcentu — a nie wyłącznie kolorystykę, analogicznie do motywu Polaroid (FR-49).
- Motyw „Fluent” zachowuje zaokrąglone kształty spójne z resztą motywów, różniąc się przede wszystkim paletą i tempem animacji.
- Oba motywy są w pełni wybieralne i zapisywane tak samo jak pozostałe motywy (FR-48).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania na podstawie polecenia użytkownika ("zainspiruj się motywem z windows11 oraz windows taki z kafelkami").

---

# FR-64: Orientacyjne wartości mikroskładników (wapń, wit. D, B12) w okienku wyliczeń

**Obszar:** Personalizacja i cele dietetyczne  
**Status:** Zaimplementowane

## Opis
W okienku „Jak policzono” (FR-12), pod rozkładem kaloryczności składników, dla przepisów zawierających choć jeden składnik z rozpoznanej listy (nabiał bez laktozy, ryby, jajka, rośliny strączkowe, zielone warzywa, nasiona) pokazują się trzy plakietki z orientacyjną zawartością wapnia, witaminy D i B12 na porcję. Te trzy mikroskładniki wybrano celowo — są one najłatwiejsze do niedoboru przy diecie bez laktozy, którą stosuje każdy profil w tej aplikacji (jeden lub oba filtry FR-8 mogą być wyłączone, ale sama app jest budowana z myślą o bezpiecznym zastąpieniu nabiału). To nie jest wyliczenie wyczerpujące — tylko składniki z rozpoznanej listy są uwzględniane, reszta przepisu jest pomijana w tym pomiarze.

## Kryteria akceptacji
- Plakietki (🦴 Wapń, ☀️ Wit. D, 🥩 B12) pojawiają się tylko gdy przepis zawiera co najmniej jeden rozpoznany składnik — w przeciwnym razie sekcja mikroskładników nie jest pokazywana wcale (nie pokazuje zer).
- Wartości skalują się z ilością składnika dokładnie tak samo jak `baseKcal` w rozkładzie kaloryczności (ta sama jednostka co `calc[]`).
- Zastrzeżenie o orientacyjności (fortyfikacja napojów roślinnych zależy od marki, nie każdy składnik przepisu jest uwzględniony) jest widoczne raz, w ogólnej (domyślnie zwiniętej) legendzie z FR-12, a nie powtarzane przy każdym przepisie.

## Uwagi
Spisane retrospektywnie 2026-08-07: funkcjonalność istniała już w kodzie z wcześniejszej rundy prac, ale nie miała własnego wpisu FR — użytkownik zapytał, czy mikroskładniki są pokazywane "przy szczegółach", co przy weryfikacji potwierdziło się jako TAK (już zaimplementowane), stąd ten wpis dokumentuje istniejące zachowanie zamiast opisywać nową zmianę.

## Historia rewizji
- **v1** (2026-08-07): Pierwsza wersja wymagania, spisana retrospektywnie — funkcjonalność już istniała w aplikacji, brakowało tylko jej opisu w tym folderze.

---

# FR-65: Własna, opcjonalna nazwa użytkownika w aplikacji

**Obszar:** Konto i współdzielenie  
**Status:** Zaimplementowane

## Opis
Ustawienia → karta „👤 Konto” pozwala wpisać dowolną, opcjonalną nazwę (pseudonim) wyświetlaną w aplikacji — np. w nagłówku, przed dotychczasowym podsumowaniem profilu diety. Nazwa jest całkowicie niezależna od jakiegokolwiek konta zewnętrznego (Google): nie jest pobierana automatycznie z niczego, nie jest wymagana, i można ją zmienić lub zostawić puste w każdej chwili — celem jest umożliwienie personalizacji bez wymuszania utraty anonimowości.

## Kryteria akceptacji
- Pole nazwy zapisuje się natychmiast przy wpisywaniu (bez osobnego przycisku „Zapisz”), niezależnie od formularza profilu diety.
- Puste pole nie pokazuje niczego dodatkowego w nagłówku (brak wymuszonego placeholdera typu „Gość”).
- Wpisana nazwa przetrwa odświeżenie strony (persystencja w tym samym magazynie co reszta stanu aplikacji).
- Reset profilu diety (przycisk „Domyślne” w karcie „Twój profil”) NIE kasuje nazwy użytkownika — to osobne dane, niezwiązane z parametrami diety.

## Uwagi
Spisane 2026-08-07: pierwszy, samodzielnie już działający element szerszej prośby o konta użytkowników, logowanie Google (opcjonalne) i współdzielone gospodarstwo domowe — reszta tamtej prośby wymaga założenia projektu Firebase i jest opisana jako plan techniczny w `docs/FIREBASE_MIGRATION_PLAN.md` (poza zakresem tego folderu, który opisuje wyłącznie już wdrożone zachowanie). Nazwa dodana w tej rundzie jest zaprojektowana tak, by dokładnie odpowiadać przyszłemu polu `displayName` w tamtym planie — nic nie trzeba tu będzie przerabiać przy właściwej integracji.

## Historia rewizji
- **v1** (2026-08-07): Pierwsza wersja wymagania na podstawie polecenia użytkownika.

---

# FR-66: Dodawanie własnych przepisów przez użytkownika

**Obszar:** Przepisy i przeglądanie  
**Status:** Zaimplementowane (edycja własnego przepisu — v7 — na razie Web-only, patrz Uwagi)

## Opis
Przycisk „➕ Dodaj swój przepis” w zakładce Przepisy otwiera formularz (nazwa, kategoria, czas przygotowania, składniki — jeden na linię, sposób przygotowania, kalorie, opcjonalnie białko/węglowodany/tłuszcz). Zapisany przepis trafia do `state.myRecipes` i od razu jest pełnoprawnym przepisem: pojawia się na liście przepisów swojej kategorii oznaczony plakietką „✍️ Twój przepis”, można go zaplanować (Planer), dodać do listy zakupów, sprawdzić jego składniki względem spiżarni, oznaczyć jako zrobiony (z historią i oceną) oraz ocenić gwiazdkowo (FR-67) — dokładnie tak samo jak którykolwiek z 229 wbudowanych przepisów.

Własny przepis można też EDYTOWAĆ (v7): przycisk „✏️ Edytuj” obok „🗑️ Usuń”
na karcie otwiera ten sam formularz, wypełniony obecnymi wartościami, i
zapisuje zmiany **zachowując identyfikator przepisu**. To istotne, bo
wszystko, co odwołuje się do przepisu, jest kluczowane po ID: jego ocena
gwiazdkowa i komentarz, historia gotowania oraz sloty w Planerze. Wcześniej
jedynym sposobem na poprawienie literówki, złej liczby kalorii czy
brakującego składnika było usunięcie i dodanie od nowa — co nadawało nowy
identyfikator i po cichu odłączało wszystkie te powiązania.

Pola makroskładników są opcjonalne i wypełniają się automatycznie w miarę wpisywania składników: formularz na bieżąco parsuje każdą linię składnika (rozpoznając ilość i gramaturę tak samo jak reszta aplikacji przy dodawaniu do spiżarni/listy zakupów) i sumuje wartości z osobnej bazy odżywczej (~90 najpopularniejszych składników). Pod polem widać, ile składników zostało rozpoznane. Ręczne wpisanie wartości w pole kalorii/białka/węgli/tłuszczu ma pierwszeństwo — od tego momentu auto-obliczanie przestaje nadpisywać akurat to pole, więc użytkownik zawsze może poprawić wynik, a nie tylko go zaakceptować.

## Kryteria akceptacji
- Formularz wymaga: nazwy, przynajmniej jednego składnika, dodatniej liczby kalorii (przy braku ręcznej wartości i nierozpoznanych składnikach walidacja ustawia fokus na polu kalorii z jasnym komunikatem, zamiast tylko ciche powiadomienie na dole ekranu). Kategoria, czas i sposób przygotowania mają rozsądne wartości domyślne, jeśli pozostawione puste.
- Zapisany przepis jest natychmiast widoczny na liście przepisów, w wybranej kategorii, z plakietką odróżniającą go od wbudowanych.
- Własny przepis działa identycznie jak wbudowany we WSZYSTKICH miejscach odwołujących się do przepisów po ID: Planer, lista zakupów, sprawdzenie spiżarni, historia gotowania, wyszukiwanie, filtrowanie, sortowanie.
- Własny przepis można usunąć bezpośrednio z karty (przycisk „🗑️ Usuń”, z potwierdzeniem) — usunięcie nie wpływa na wcześniej dodane wpisy historii gotowania czy pozycje na liście zakupów pochodzące z tego przepisu.
- Wpisanie składnika z rozpoznawalną ilością/gramaturą (np. „150 g piersi z kurczaka”) automatycznie dolicza jego kalorie i makroskładniki do sumy przepisu; nierozpoznane składniki (rzadkie/nietypowe nazwy) są pomijane w sumie, a formularz jasno informuje ile z wpisanych linii zostało rozpoznanych.
- Ręczna edycja pola kalorii/białka/węglowodanów/tłuszczu zatrzymuje automatyczne nadpisywanie TEGO konkretnego pola do końca sesji formularza (nowe otwarcie formularza resetuje ten stan).
- Web (v7): karta własnego przepisu ma przycisk „✏️ Edytuj” obok „🗑️ Usuń”.
- Web (v7): formularz otwarty do edycji jest wypełniony obecnymi wartościami przepisu (nazwa, kategoria, czas, składniki, sposób przygotowania, źródło inspiracji, kalorie i makro), ma tytuł „✏️ Edytuj swój przepis” i przycisk „Zapisz zmiany”.
- Web (v7): zapisanie zmian NIE tworzy drugiego przepisu i NIE zmienia identyfikatora — ocena, komentarz, historia gotowania i zaplanowane sloty pozostają powiązane z przepisem.
- Web (v7): po zapisaniu zmian Planer i lista zakupów natychmiast pokazują nową nazwę i kalorie, bez potrzeby odświeżania.
- Web (v7): otwarcie formularza przyciskiem „➕ Dodaj swój przepis” po wcześniejszej edycji działa jak dodawanie nowego (puste pola, nowy identyfikator), a nie jak kolejna edycja.

## Uwagi
Edycja (v7) świadomie oznacza pola makro jako „ustawione ręcznie” przy
otwarciu formularza — zapisane wartości SĄ własnymi liczbami użytkownika,
więc auto-kalkulator nie powinien ich nadpisywać tylko dlatego, że
formularz został ponownie otwarty.

Edytowany przepis jest ponownie publikowany w społeczności (`pushCommunityRecipe`)
— dokument jest kluczowany po ID przepisu, więc edycja nadpisuje go w
miejscu zamiast tworzyć duplikat. Wraca przy tym do `status:"pending"`,
świadomie: moderator zatwierdził poprzednią treść, a nie tę po zmianie.

**v7 (edycja) świadomie Web-only na razie** — ta sesja pracuje w środowisku
bez dostępu do `api.foojay.io` (toolchain JDK dla Gradle, błąd 403 przy
`:app:compileDebugKotlin`), więc port do Compose nie może tu zostać ani
skompilowany, ani przetestowany; odłożone do sesji z realnym dostępem do
Gradle/emulatora, odnotowane w `android/PARITY.md`.

Spisane 2026-08-07: pierwszy, w pełni lokalny element szerszej prośby o możliwość dodawania przepisów przez użytkowników z myślą o przyszłej społeczności — reszta (przepisy widoczne dla INNYCH użytkowników, moderacja) wymaga chmury i jest opisana w `docs/FIREBASE_MIGRATION_PLAN.md` jako `source: "community"` z polem `status`. Ten sam przepis, dodany dziś lokalnie jako `source: "custom"`, jest strukturalnie gotowy stać się przepisem społecznościowym po podłączeniu Firebase, bez zmiany kształtu danych.

Technicznie: wprowadzono `allRecipes()` (łączy 229 wbudowanych przepisów z `state.myRecipes`) i `findRecipeById(id)`, zastępując bezpośrednie odwołania do stałej tablicy `RECIPES` we wszystkich miejscach, gdzie przepis jest wyszukiwany po ID lub filtrowany po kategorii.

Zrewidowane 2026-08-08: dodano automatyczne obliczanie kalorii/makroskładników z wpisanych składników (`INGREDIENT_MACRO_DB`, `estimateRecipeMacrosFromText`) po zgłoszeniu, że ręczne wpisywanie wszystkich wartości było zarówno uciążliwe, jak i ryzykowne dla rzetelności danych — użytkownik bez wiedzy żywieniowej mógł łatwo wpisać nieprawdziwe liczby. Przy okazji poprawiono czytelność walidacji formularza (fokus na brakującym polu zamiast tylko cichego komunikatu).

## Historia rewizji
- **v1** (2026-08-07): Pierwsza wersja wymagania na podstawie polecenia użytkownika.
- **v2** (2026-08-08): Dodano automatyczne obliczanie makroskładników z listy składników oraz poprawiono czytelność walidacji — patrz "Uwagi" i zaktualizowane kryteria akceptacji.
- **v3** (2026-08-08): Przewidywanie z "Uwag" ("gotowy stać się przepisem społecznościowym po podłączeniu Firebase, bez zmiany kształtu danych") zrealizowane — patrz FR-76.
- **v4** (2026-08-11, Android): Na życzenie użytkownika, przycisk „➕ Dodaj
  swój przepis” przeniesiony z inline (pod paskiem filtrów) na floating
  przycisk „📖” w prawym dolnym rogu, widoczny tylko na Przepisach, obok
  analogicznego floating „💡” z FR-32/v2. Sam formularz i zachowanie po
  zapisaniu — bez zmian. Web NIE zmieniony w tej turze — świadoma
  rozbieżność, patrz `android/PARITY.md`. Zatwierdzanie przepisów z
  poziomu aplikacji (nie tylko konsoli Firebase) opisane w nowym FR-85.
- **v5** (2026-08-25, Web): Dwie zmiany na życzenie użytkownika. (1) Nowe,
  opcjonalne pole „Źródło inspiracji” w formularzu „➕ Dodaj swój przepis”
  — jeśli wypełnione, `recipe.inspirationSource` pokazuje się jako „💡
  Inspiracja: …” na końcu karty przepisu (pod sposobem przygotowania).
  Świadomie TYLKO dla nowo dodawanych własnych przepisów, na wyraźne
  pytanie zwrotne do użytkownika — NIE dopisywano fikcyjnych/zgadywanych
  źródeł inspiracji do 229 wbudowanych przepisów aplikacji, bo nie ma dla
  nich prawdziwych danych, a zmyślanie źródeł dla istniejących dań
  wprowadzałoby w błąd. Pole przechodzi też przez `pushCommunityRecipe`/
  `sanitizeCommunityRecipeDoc` (escapowane, max 200 znaków), więc widoczne
  jest też innym użytkownikom oglądającym zatwierdzony przepis
  społecznościowy (FR-76), nie tylko autorowi. (2) Karta przepisu (KAŻDA,
  nie tylko własne) dostała jawne przyciski „🔎 Google”/„▶️ YouTube”
  wyszukujące pełną nazwę dania w nowej karcie — wcześniej istniało tylko
  ukryte wyszukiwanie Google po kliknięciu w sam tytuł przepisu, bez
  żadnego widocznego przycisku/afordancji. Oba mechanizmy zweryfikowane
  lokalnie w Chrome (nie tylko składniowo).
- **v6** (2026-08-25, Android): Port v5 na Androida. `Recipe.inspirationSource`
  (nullable, `:logic`), `CustomRecipeOperations.Input.inspirationSourceText`
  + `build()` ustawiające pole (trim → null gdy puste, ta sama semantyka
  co web). `AddCustomRecipeDialog` (RecipeListScreen.kt) dostał pole
  „Źródło inspiracji” po polu sposobu przygotowania. Karta przepisu
  (`RecipeCardBody`) pokazuje „💡 Inspiracja: …” gdy ustawione oraz dwa
  `OutlinedButton` „🔎 Google”/„▶️ YouTube” (dla KAŻDEGO przepisu, jak na
  web) otwierające wyszukiwarkę/YouTube przez `Intent(ACTION_VIEW)`.
  `./gradlew :app:assembleDebug :logic:test` przeszły; wizualna weryfikacja
  w Android Studio/na urządzeniu jeszcze ⏳ (patrz `android/PARITY.md`).
- **v7** (2026-08-28, Web only): Dodana EDYCJA własnego przepisu. Zmiana z
  własnej rekomendacji, po przeglądzie funkcji pod kątem luk: aplikacja
  pozwalała własny przepis dodać i usunąć, ale nie poprawić — a
  delete-and-re-add nadaje nowy identyfikator, więc po cichu osierocał
  ocenę gwiazdkową, komentarz, historię gotowania i sloty w Planerze
  wskazujące na stary przepis. Ten sam modal obsługuje teraz oba tryby
  (`editingRecipeId`), przycisk „✏️ Edytuj” dołożony obok „🗑️ Usuń” na
  karcie własnego przepisu. Zweryfikowane na żywo (headless Chromium):
  przepis z oceną 5★+komentarzem, wpisem historii gotowania i zaplanowanym
  slotem został wyedytowany (zmiana nazwy, kalorii i listy składników) —
  identyfikator, ocena, komentarz, historia i slot w Planerze pozostały
  nietknięte, liczba przepisów nie wzrosła, a Planer natychmiast pokazał
  nową nazwę i „444 kcal”; osobno sprawdzone, że kolejne otwarcie
  formularza przyciskiem „➕ Dodaj” działa jak czyste dodawanie (puste
  pola, nowy identyfikator, 2 przepisy w sumie). CACHE_NAME→v109,
  `versions/v109/`.

---

# FR-67: Ocena gwiazdkowa i komentarz przy przepisie

**Obszar:** Ocenianie i ranking przepisów  
**Status:** Zaimplementowane

## Opis
Każda karta przepisu (wbudowanego lub własnego, FR-66) ma przycisk „⭐ Oceń i skomentuj”, umieszczony na samym dole rozwiniętej karty (pod składnikami i sposobem przygotowania — patrz FR-77), otwierający okienko z 5 dużymi gwiazdkami (ta sama, pełnoszerokia stylistyka co ocena w historii gotowania, FR-17) i opcjonalnym polem komentarza tekstowego (do 300 znaków). Zapisana ocena i komentarz pokazują się bezpośrednio na karcie przepisu: liczba gwiazdek w etykiecie przycisku oraz treść komentarza pod opisem przygotowania. Osobny przycisk sortowania (🏆) w pasku narzędzi zakładki Przepisy sortuje listę wg tej oceny, malejąco. Bezpośrednio pod przyciskiem oceny znajduje się rozwijana sekcja komentarzy od innych użytkowników, opisana osobno w FR-77.

**Od FR-84 (2026-08-11)**: to już NIE jest osobny mechanizm od gestu przesunięcia karty (FR-55/57) ani od oceny po ugotowaniu (FR-17) — to JEDYNY mechanizm oceniania w aplikacji. Przesunięcie karty w prawo/lewo to teraz tylko szybki skrót ustawiający tę samą ocenę (5★/1★), a „⭐ Oceń to danie” w historii gotowania otwiera dokładnie to samo okienko. Zobacz FR-84 po pełny opis scalenia.

## Kryteria akceptacji
- Ocena wymaga wybrania od 1 do 5 gwiazdek; próba zapisu bez wybrania gwiazdek pokazuje komunikat i nie zapisuje niczego.
- Komentarz jest w pełni opcjonalny.
- Ponowne otwarcie okienka dla już ocenionego przepisu pokazuje wcześniej wybraną liczbę gwiazdek i treść komentarza, gotowe do edycji.
- Przycisk „Usuń ocenę” czyści zarówno gwiazdki, jak i komentarz dla danego przepisu.
- Sortowanie 🏆 działa niezależnie od sortowania wg dopasowania (🎯, FR-7) — użytkownik może mieć włączone dowolne z nich, ostatnio kliknięte ma pierwszeństwo (ta sama zasada co między pozostałymi przełącznikami sortowania). Osobny przełącznik rankingu polubień (❤️, dawny FR-57) został usunięty jako redundantny po scaleniu — patrz FR-84.

## Uwagi
Spisane 2026-08-07: dane zapisywane lokalnie w `state.recipeReviews[recipeId] = {stars, comment, at}`, w kształcie odpowiadającym dokumentowi `recipes/{id}/ratings/{uid}` z planu Firebase (`docs/FIREBASE_MIGRATION_PLAN.md`) — jeden dokument na oceniającego na przepis. Przy jednym lokalnym użytkowniku „Twoja ocena” i „średnia ocena” to dziś ten sam numer; po podłączeniu chmury stanie się to prawdziwą, wieloosobową średnią bez zmiany kształtu danych.

Podczas implementacji wykryto i naprawiono błąd w logice przełącznika gwiazdek w okienku oceny: podwójny, nadmiarowy zapis stanu zaznaczenia (raz wewnątrz funkcji renderującej, raz zaraz po jej wywołaniu) powodował, że kliknięcie gwiazdki cofało własną zmianę, blokując zapisanie jakiejkolwiek oceny.

Zrewidowane 2026-08-08: `recipes/{id}/ratings/{uid}` z powyższego akapitu przestało być tylko teoretycznym docelowym kształtem — FR-77 faktycznie zapisuje tam każdą ocenę (dla zalogowanych na prawdziwe konto), więc komentarze pod przepisem są już dziś prawdziwie wieloosobowe, niezależnie od sortowania 🏆 (które nadal patrzy tylko na lokalną ocenę tego urządzenia — patrz `docs/FIREBASE_MIGRATION_PLAN.md`).

## Historia rewizji
- **v1** (2026-08-07): Pierwsza wersja wymagania na podstawie polecenia użytkownika.
- **v2** (2026-08-08): Przeniesiono przycisk na dół karty i dodano pod nim wielo-użytkownikowy wątek komentarzy — patrz FR-77 i sekcja "Uwagi".
- **v3** (2026-08-11): Stało się JEDYNYM mechanizmem oceniania w aplikacji — scalone z FR-55/FR-57 (przesunięcie karty) i FR-17 (ocena po ugotowaniu). Patrz FR-84.

---

# FR-68: Ustawienia gospodarstwa domowego i przepisów społeczności (stan przejściowy)

**Obszar:** Konto i współdzielenie  
**Status:** Częściowo zaimplementowane (świadomie) — patrz też FR-69

## Opis
Ustawienia zawierają dwie karty przygotowujące grunt pod pełną funkcjonalność opisaną w `docs/FIREBASE_MIGRATION_PLAN.md`, każda z jasno innym poziomem gotowości:

1. **„🌍 Przepisy społeczności”** — działający, zapisujący się przełącznik `state.communityRecipesEnabled`. Dziś nie ma żadnego efektu widocznego dla użytkownika (nie ma jeszcze przepisów od innych osób do pokazania), ale jest w pełni funkcjonalny technicznie i nie będzie wymagał żadnej zmiany, gdy tylko pojawią się pierwsze zatwierdzone przepisy społecznościowe.
2. **„☁️ Konto w chmurze”** — od czasu podłączenia prawdziwego projektu Firebase (FR-69) w pełni działająca karta logowania (anonimowe/Google/e-mail). To, co NADAL nie działa, to współdzielenie danych między kontami/gospodarstwem domowym — logowanie zabezpiecza tożsamość konta, ale spiżarnia/lista zakupów/planer są nadal wyłącznie lokalne na urządzeniu.

## Kryteria akceptacji
- Przełącznik przepisów społeczności zapisuje się i persystuje między sesjami, niezależnie od tego, że nie ma jeszcze żadnego efektu widocznego (brak przepisów społecznościowych do pokazania).
- Karta konta w chmurze NIE sugeruje więcej niż faktycznie robi: nie ma żadnego formularza "dołącz do gospodarstwa"/"udostępnij spiżarnię", dopóki synchronizacja danych między kontami nie zostanie faktycznie wdrożona (kolejny krok w `docs/FIREBASE_MIGRATION_PLAN.md`). Świadoma decyzja: fałszywie działający formularz wprowadzałby w błąd.
- Własne przepisy użytkownika (FR-66) działają już dziś, NIEZALEŻNIE od przełącznika przepisów społeczności — ten przełącznik dotyczy wyłącznie przepisów od INNYCH osób.

## Uwagi
Spisane 2026-08-07 na podstawie prośby o logowanie Google (opcjonalne), wspólne gospodarstwo domowe ze współdzieloną spiżarnią i listą zakupów oraz przepisy społecznościowe z ocenami/komentarzami. Świadomy zakres tamtej rundy: zbudować i realnie przetestować wszystko, co da się zrobić bez zewnętrznego backendu (FR-65 nazwa użytkownika, FR-66 własne przepisy, FR-67 oceny/komentarze, przełącznik tutaj), a resztę opisać jako konkretny, wykonalny plan zamiast budować nieprawdziwie działający interfejs.

Zrewidowane 2026-08-08: następnego dnia użytkownik faktycznie założył projekt Firebase — karta „Konto w chmurze” z tego wpisu przestała być tylko informacyjna i stała się prawdziwym mechanizmem logowania, opisanym szczegółowo w nowym FR-69. Ten wpis pozostaje jako zapis pierwotnego, świadomie ograniczonego zakresu tamtej rundy.

## Historia rewizji
- **v1** (2026-08-07): Pierwsza wersja wymagania na podstawie polecenia użytkownika.
- **v2** (2026-08-08): Uwzględniono podłączenie prawdziwego Firebase (FR-69) — karta logowania przestała być czysto informacyjna, patrz "Uwagi".

---

# FR-69: Logowanie w chmurze (anonimowe, Google, e-mail i hasło)

**Obszar:** Konto i współdzielenie  
**Status:** Zaimplementowane i potwierdzone działające na produkcji (logowanie); synchronizacja danych — jeszcze nie

## Opis
Aplikacja korzysta z prawdziwego projektu Firebase (Authentication + Firestore). Każde urządzenie loguje się automatycznie i bez pytania jako użytkownik anonimowy (Firebase Anonymous Auth) przy pierwszym uruchomieniu — to nie zmienia dotychczasowego, w pełni lokalnego działania aplikacji, tylko nadaje jej stabilny, gotowy na przyszłość identyfikator.

W Ustawieniach → „☁️ Konto w chmurze” są dwie jasno rozdzielone ścieżki:
- **„Pierwsze urządzenie / nowe konto”** — „Połącz z kontem Google” / „Połącz (nowe konto)” (e-mail). Obie **łączą** (linkują) istniejące anonimowe konto zamiast zakładać nowe od zera — dane, które w przyszłości będą już zsynchronizowane z tym kontem, nie giną w momencie pierwszego logowania.
- **„Masz już konto?”** — „Zaloguj się na konto Google” / „Zaloguj się (istniejące konto)” (e-mail). Prawdziwe logowanie (`signInWithPopup`/`signInWithEmailAndPassword`), nie łączenie — to jest ścieżka dla KAŻDEGO kolejnego urządzenia (np. telefonu po tym, jak konto zostało już założone na komputerze), bo linkowanie działa tylko raz na konto: próba "połączenia" z kontem, które już istnieje gdzie indziej, kończy się błędem `auth/credential-already-in-use`.

Jeden adres e-mail nie może być użyty do założenia dwóch osobnych kont (raz przez Google, raz hasłem) — wymusza to ustawienie projektu Firebase „jedno konto na adres e-mail”. Jeśli użytkownik i tak spróbuje "połączyć" konto, które już istnieje (typowy błąd na drugim urządzeniu, zanim zauważy osobny przycisk logowania), aplikacja wykrywa tę kolizję i proponuje zalogowanie się do istniejącego konta zamiast pokazania suchego błędu — w obu miejscach, dla Google i dla e-maila.

Jeśli Firebase jest niedostępny (brak internetu, zablokowany dostęp do serwerów Google, błąd wczytania SDK) — cała sekcja logowania grzecznie się chowa, pokazuje jasny komunikat, a reszta aplikacji działa dokładnie tak jak przed podłączeniem Firebase, bez żadnego wyjątku/awarii.

## Kryteria akceptacji
- Brak ekranu logowania blokującego korzystanie z aplikacji — logowanie anonimowe dzieje się w tle, automatycznie.
- Cztery osobne, jasno opisane przyciski: połącz z Google, zaloguj się przez Google, połącz e-mailem, zaloguj się e-mailem — nie tylko "połącz", żeby logowanie na drugim/kolejnym urządzeniu (na już istniejące konto) było możliwe bez natrafiania na błąd jako jedyną drogę do informacji, że trzeba się zalogować, a nie połączyć.
- Próba połączenia (linkowania) kontem/e-mailem już zajętym przez inne konto pokazuje czytelny komunikat po polsku i pyta, czy zalogować się do tego istniejącego konta — dla obu metod (Google i e-mail).
- Karta „Konto w chmurze” pokazuje aktualny stan: niedostępność Firebase, brak logowania, lub zalogowanie (i którą metodą).
- Jasna informacja przy przyciskach logowania o tym, co zalogowanie się na już istniejące konto na nowym urządzeniu robi z danymi tego urządzenia (pobiera i NADPISUJE danymi z konta — patrz FR-73, mechanizm synchronizacji danych osobistych).
- Całkowity brak dostępu do Firebase (np. zablokowana sieć) nie powoduje błędu JS ani nie psuje żadnej innej funkcji aplikacji — zweryfikowane w środowisku z faktycznie zablokowanym dostępem do serwerów Firebase.

## Uwagi
Spisane 2026-08-08, w dniu założenia prawdziwego projektu Firebase (`dieta-app-323b4`) przez użytkownika, zgodnie z checklistą z `docs/FIREBASE_MIGRATION_PLAN.md`. To wdraża wyłącznie warstwę logowania z tamtego planu — synchronizacja właściwych danych (spiżarnia, lista zakupów, planer) między urządzeniami i osobami w gospodarstwie domowym to kolejny, jeszcze nie zaimplementowany krok z tego samego planu.

Zrewidowane tego samego dnia po pierwszym prawdziwym teście: użytkownik połączył konto Google na komputerze, po czym nie mógł zalogować się tym samym kontem na telefonie — pierwsza wersja miała tylko przyciski "Połącz" (linkowanie), które z definicji działa tylko raz na konto. Dodano osobne, zawsze widoczne przyciski logowania (`signInWithPopup`/`signInWithEmailAndPassword`) dla kolejnych urządzeń, plus tę samą kolizję-z-propozycją-logowania dla przycisku "Połącz z kontem Google" (wcześniej miała to tylko wersja e-mailowa).

Testowanie: środowisko deweloperskie miało zablokowany sieciowo dostęp do `gstatic.com`/serwerów Firebase (polityka sieciowa piaskownicy), więc rzeczywiste logowanie nie mogło zostać zweryfikowane automatycznie od strony dewelopera. Prawdziwe logowanie Google zostało już potwierdzone jako działające przez użytkownika na produkcyjnej domenie (`przemas230.github.io`, dodanej do autoryzowanych domen Firebase po napotkaniu błędu `auth/unauthorized-domain`); nowe przyciski logowania na drugim urządzeniu wymagają analogicznej ręcznej weryfikacji przez użytkownika.

Zrewidowane ponownie 2026-08-08: użytkownik zgłosił błąd logowania e-mailem/hasłem na konto, które w rzeczywistości zostało założone wyłącznie przez Google (nigdy nie miało ustawionego hasła) — Firebase zwraca w takim przypadku niejasny `auth/invalid-credential`. Dodano czytelny komunikat sugerujący użycie logowania Google zamiast e-maila/hasła, gdy taki błąd wystąpi.

## Historia rewizji
- **v1** (2026-08-08): Pierwsza wersja wymagania na podstawie polecenia użytkownika.
- **v2** (2026-08-08): Dodano osobne przyciski logowania (nie tylko łączenia) po zgłoszeniu, że drugie urządzenie nie mogło zalogować się na już istniejące konto — patrz "Uwagi".
- **v3** (2026-08-08): Doprecyzowano komunikat błędu `auth/invalid-credential` dla kont bez ustawionego hasła — patrz "Uwagi".
- **v4** (2026-08-08): Zaktualizowano opis konsekwencji logowania na nowym urządzeniu po wdrożeniu synchronizacji danych osobistych (FR-73) — logowanie już nie "tylko potwierdza tożsamość", tylko realnie pobiera i nadpisuje dane tego urządzenia danymi z konta.

---

# FR-70: Licznik nawodnienia w nagłówku — pojedyncze klikalne kropelki

**Obszar:** Nagłówek i nawigacja  
**Status:** Zaimplementowane

## Opis
Pasek kropelek w nagłówku (widoczny na każdej zakładce, niezależnie od tego, czy nagłówek jest zwinięty) pokazuje dzisiejsze nawodnienie i pozwala je zmieniać bezpośrednio stamtąd, bez przechodzenia do zakładki Postęp. Każda z 8 kropelek jest osobnym punktem klikalnym: kliknięcie kropelki nr `i` ustawia licznik na `i`, a ponowne kliknięcie tej samej (już ustawionej) kropelki cofa licznik o jedną szklankę — dokładnie ten sam mechanizm „ustaw poziom kliknięciem” co kwadraciki wody na zakładce Postęp (`renderWater`).

## Kryteria akceptacji
- Kliknięcie dowolnej kropelki zmienia licznik na wartość odpowiadającą jej pozycji (1-8).
- Kliknięcie kropelki dokładnie na aktualnym poziomie cofa licznik o jedną szklankę (nie zeruje go całkowicie).
- Zmiana w nagłówku natychmiast odzwierciedla się w widoku kwadracików na Postępie i odwrotnie.
- Nie ma stanu, z którego nie da się cofnąć przypadkowego dodatkowego kliknięcia — każda wartość 0-8 jest osiągalna wprost, bez zawijania.

## Uwagi
Zrewidowane 2026-08-08: pierwotna wersja traktowała cały pasek kropelek jako jeden przycisk wyłącznie zwiększający licznik (z zawinięciem do zera dopiero po przekroczeniu 8) — przypadkowe podwójne kliknięcie nie dało się cofnąć inaczej niż dochodząc aż do pełnych 8 szklanek. Naprawiono, rozbijając pasek na 8 niezależnie klikalnych kropelek z tą samą logiką co już dobrze działające kwadraciki na Postępie.

## Historia rewizji
- **v1** (2026-08-08): Pierwsza wersja wymagania, spisana po naprawie zgłoszonego błędu.

---

# FR-71: Zakładki w Ustawieniach — Konto, Wygląd, Przypomnienia, Ulubione

**Obszar:** Ustawienia
**Status:** Zaimplementowane

## Opis
Widok Ustawień ma na samej górze poziomy przełącznik zakładek (styl taki sam
jak pigułki kategorii przepisów): „👤 Konto”, „🎨 Wygląd”, „💧 Przypomnienia”,
„⭐ Ulubione”. Każda zakładka pokazuje tylko powiązane z nią karty ustawień
zamiast jednej długiej listy do przewijania.

Zakładka „👤 Konto” (domyślnie otwarta przy każdym wejściu w Ustawienia)
zawiera, w tej kolejności: kartę „⚙️ Twój profil” (zaczynającą się od pola
„Twoja nazwa w aplikacji”, a dalej płeć/wiek/wzrost/waga/cel/aktywność/filtry
dietetyczne), kartę „☁️ Konto w chmurze” i kartę „🌍 Przepisy społeczności”.

Zakładka „🎨 Wygląd” zawiera kartę „🎨 Wygląd aplikacji” (motyw, skala UI).

Zakładka „💧 Przypomnienia” zawiera kartę przypomnienia o piciu wody oraz
kartę diagnostyki powiadomień o wodzie.

Zakładka „⭐ Ulubione” zawiera kartę „⭐ Ulubione składniki”.

## Kryteria akceptacji
- Kliknięcie pigułki zakładki pokazuje wyłącznie panel tej zakładki, ukrywając
  pozostałe trzy.
- Przy każdym otwarciu widoku Ustawień (z dolnej nawigacji) domyślnie aktywna
  jest zakładka „Konto” — stan poprzednio wybranej zakładki nie jest
  pamiętany między otwarciami.
- Pole „Twoja nazwa w aplikacji” znajduje się fizycznie na początku karty
  „Twój profil”, a nie w osobnej karcie nad nią.
- Zmiana zakładek nie resetuje niezapisanych zmian w formularzu profilu.

## Historia rewizji
- **v1** (2026-08-08): Pierwsza wersja wymagania — przebudowa Ustawień z
  jednej długiej listy kart na cztery tematyczne zakładki, na życzenie
  użytkownika ("zrób z tego jakiś kompaktowy wygląd... żeby łatwo się można
  było przełączać pomiędzy ustawieniami wyglądu aplikacji, konta,
  przypomnień i ulubionych rzeczy").

---

# FR-72: Wymuszenie ustawienia profilu przy pierwszym uruchomieniu

**Obszar:** Ustawienia / Profil
**Status:** Zaimplementowane

## Opis
Przy zupełnie pierwszym, świeżym uruchomieniu aplikacji (brak wcześniej
zapisanego stanu) profil użytkownika NIE jest już cicho wypełniany
przykładowymi domyślnymi danymi (płeć, wiek, wzrost, waga, cel). Zamiast
tego profil dostaje flagę `configured = false`, dopóki użytkownik
samodzielnie nie uzupełni i nie zapisze tych danych w Ustawieniach.

Dopóki profil nie jest skonfigurowany:
- Pola wieku/wzrostu/wagi/wagi docelowej w formularzu Ustawień są puste
  (zamiast pokazywać przykładowe liczby), z podpowiedziami w placeholderach.
- Pod formularzem widnieje komunikat zachęcający: „👋 Uzupełnij swoje dane
  powyżej i zapisz, żeby dopasować dietę do siebie.”
- Nagłówek aplikacji zamiast pełnych statystyk (płeć, wiek, cel kaloryczny…)
  pokazuje zaproszenie „👋 Ustaw swój profil w Ustawieniach, aby dopasować
  dietę do siebie”.
- Znaczek dopasowania do profilu (🎯) na kartach przepisów nie jest w ogóle
  pokazywany (dopasowanie nie ma sensu bez realnych danych użytkownika).

Po pierwszym zapisaniu formularza profilu flaga `configured` ustawia się na
`true` na stałe i aplikacja od tego momentu zachowuje się jak dotychczas
(pełne statystyki w nagłówku, znaczek 🎯 na przepisach).

Użytkownicy, którzy korzystali z aplikacji przed wprowadzeniem tej zmiany,
mają swój zapisany profil automatycznie oznaczony jako już skonfigurowany —
ta zmiana dotyczy wyłącznie zupełnie nowych instalacji i w żaden sposób nie
wpływa na istniejących użytkowników ani ich zapisane dane.

## Kryteria akceptacji
- Świeża instalacja (czysty `localStorage`) startuje z `profile.configured
  === false` i wewnętrznymi wartościami domyślnymi zachowanymi tylko do
  celów obliczeniowych (nigdy nie pokazywanymi użytkownikowi jako realne
  dane).
- Zapisanie formularza Ustawień (przycisk „Zapisz”) ustawia
  `configured = true` niezależnie od tego, czy użytkownik zmienił wszystkie
  pola, czy tylko część.
- Przycisk „Resetuj” w Ustawieniach przywraca wartości domyślne, ale od razu
  oznacza profil jako skonfigurowany (`configured: true`) — reset nie ma
  cofać użytkownika do stanu „pierwsze uruchomienie”.
- Wcześniej zapisane profile (bez pola `configured` w danych) są przy
  wczytaniu stanu automatycznie traktowane jako `configured: true`.

## Historia rewizji
- **v1** (2026-08-08): Pierwsza wersja wymagania, na życzenie użytkownika
  ("przy pierwszym otwarciu konta wyczyść domyślne ustawienia płci wieku
  wagi itp żeby ktoś musiał sam sobie ustawić zanim dopasuje dietę").
- **v2** (2026-08-24, Web + Android): Naprawiono lukę w oryginalnym
  wymaganiu — v1 gwarantowała puste pola tylko dla wieku/wzrostu/wagi/
  wagi docelowej (patrz Opis), ale pola płci/aktywności/celu od zawsze
  pokazywały domyślnie zaznaczoną opcję (Kobieta/Lekko aktywny/Redukcja
  masy ciała) nawet dla `configured: false`, bo natywny `<select>`/enum
  Kotlina zawsze ma JAKĄŚ wartość, w odróżnieniu od tekstowego pola liczb,
  które może być po prostu puste. Zgłoszone przez użytkownika po
  przetestowaniu FR-89's przycisku resetu konta — reset sprawiał wrażenie
  "nie w pełni zadziałał", bo formularz dalej pokazywał "Kobieta"
  zaznaczoną. Naprawione symetrycznie na obu platformach: web dostał
  pusty placeholder `<option value="" disabled>Wybierz…</option>` w
  `setSex`/`setActivity`/`setGoal`, ustawiany gdy `!p.configured`; Android
  (`ProfileCard` w `SettingsScreen.kt`) zmienił lokalny stan `sex`/
  `activity`/`goal` z nie-nullowalnego na `Sex?`/`ActivityLevel?`/`Goal?`,
  inicjalizowany na `null` gdy `!profile.configured`, dokładnie tym samym
  wzorcem co już istniejące pola liczbowe. Zapisanie formularza bez
  wybrania tych pól nadal działa (kryterium akceptacji "niezależnie od
  tego, czy user zmienił wszystkie pola" pozostaje w mocy) — spada na te
  same domyślne wartości co wcześniej, tylko już nie POKAZUJE ich jako
  rzekomo wybranych, dopóki user faktycznie czegoś nie kliknie.

---

# FR-73: Synchronizacja danych osobistych w chmurze między urządzeniami

**Obszar:** Konto i chmura
**Status:** Zaimplementowane

## Opis
Po zalogowaniu na prawdziwe konto (Google lub e-mail/hasło — patrz FR-69),
w odróżnieniu od domyślnego logowania anonimowego, aplikacja automatycznie
zapisuje w Firestore i na bieżąco synchronizuje między wszystkimi
urządzeniami zalogowanymi tym samym kontem następujące dane osobiste:

- nazwę w aplikacji (`displayName`),
- profil diety (płeć/wiek/wzrost/waga/cel/filtry, `profile`),
- spiżarnię wraz z ręcznymi nadpisaniami jednostki/kategorii (`pantry`,
  `pantryUnitOverride`, `pantryCategoryOverride`),
- ulubione przepisy i ulubione składniki (`favorites`, `favIngredients`),
- własne dodane przepisy (`myRecipes`, patrz FR-66),
- oceny i recenzje przepisów (`recipeReviews`, `recipeRating`, patrz FR-67),
- niestandardowe kafelki spiżarni (`customTiles`),
- motyw, skalę interfejsu i styl etykiety oceniania przesunięciem
  (`theme`, `uiScale`, `swipeRatingStyle`),
- przełącznik „Pokazuj przepisy dodane przez innych użytkowników”
  (`communityRecipesEnabled`).

Mechanizm: każde wywołanie `saveState()` (czyli każdy zapis do
`localStorage`, tak jak dotychczas) dodatkowo, z 1,5-sekundowym opóźnieniem
(żeby nie wysyłać osobnego zapisu do chmury przy każdym pojedynczym
kliknięciu), zapisuje powyższy wycinek stanu do dokumentu
`users/{uid}` w Firestore — ale TYLKO gdy użytkownik jest zalogowany na
prawdziwe (nie anonimowe) konto. Jednocześnie aplikacja nasłuchuje zmian
tego samego dokumentu na żywo (`onSnapshot`) — zmiana wprowadzona na
jednym zalogowanym urządzeniu pojawia się na pozostałych automatycznie,
bez potrzeby ręcznego odświeżania, również po powrocie do sieci po pracy
offline (dzięki wcześniej włączonemu trybowi offline Firestore,
`enablePersistence`).

Pierwsze zalogowanie na dane konto (dokument `users/{uid}` jeszcze nie
istnieje w chmurze) wysyła obecny lokalny stan urządzenia jako punkt
startowy („jednorazowa migracja”, zgodnie z `docs/FIREBASE_MIGRATION_PLAN.md`
punkt 7 checklisty). Zalogowanie się na już ISTNIEJĄCE konto (dokument już
ma dane z innego urządzenia) pobiera dane z chmury na to urządzenie —
to pobranie ZASTĘPUJE dotychczasowe lokalne dane tego urządzenia, a nie
scala ich pole po polu; to świadomy wybór (ostatni zapis w chmurze wygrywa
całym dokumentem), a nie próba automatycznego rozwiązywania konfliktów
między dwoma niezależnie używanymi urządzeniami.

## Kryteria akceptacji
- Użytkownik zalogowany wyłącznie anonimowo (domyślny stan przy pierwszym
  uruchomieniu) NIGDY nie wysyła ani nie odbiera danych z chmury — zachowanie
  identyczne jak przed wprowadzeniem tej funkcji.
- Wielokrotne szybkie zmiany stanu (np. kilka kliknięć pod rząd) skutkują
  JEDNYM zapisem do chmury po ustaniu aktywności, nie osobnym zapisem na
  każdą zmianę.
- Odebranie z chmury danych identycznych z już posiadanymi lokalnie (np.
  echo własnego zapisu) NIE wywołuje ponownego renderowania interfejsu ani
  powiadomienia — tylko rzeczywista zmiana wartości to robi.
- **AKTUALIZACJA (FR-78, 2026-08-08):** dziennik zjedzonych posiłków, historia
  wagi/nawodnienia, log aktywności, historia gotowania, planer tygodniowy i
  lista zakupów — pierwotnie świadomie wyłączone z tej rundy (patrz niżej) —
  ZOSTAŁY jednak dołączone do synchronizacji na wyraźne życzenie użytkownika,
  wraz z prawdziwym trójstronnym scalaniem zmian (3-way merge) zamiast
  prostego nadpisywania całego pola. Zobacz FR-78 po pełny opis mechanizmu
  scalania i okienka sprzeczności. Oryginalne uzasadnienie wyłączenia
  (poniżej) pozostaje jako kontekst historyczny — model `households/*` dla
  PRAWDZIWIE wieloosobowej, jednoczesnej edycji tej samej listy przez kilka
  osób w tym samym momencie nadal nie istnieje i jest osobnym, przyszłym
  krokiem.
- Włączenie przełącznika „Pokazuj przepisy społeczności” synchronizuje samą
  WARTOŚĆ przełącznika między urządzeniami, ale nie powoduje jeszcze
  pokazania przepisów dodanych przez innych użytkowników — do tego
  potrzebna jest osobna, wciąż niezaimplementowana baza przepisów
  społeczności z moderacją (patrz `docs/FIREBASE_MIGRATION_PLAN.md`).

- Aplikacja webowa NIE włącza już własnego, offline'owego cache'u Firestore
  w IndexedDB (`enablePersistence`) — nie jest do niczego potrzebny, bo
  cały `state` jest już niezależnie i synchronicznie zapisywany do
  `localStorage` (`saveState()`) przy każdej zmianie. Jedyny efekt uboczny:
  pojedyncze zapytania `.get()` (np. lista użytkowników) nie serwują się
  już z lokalnego cache'u offline — ale mają własny, twardy limit czasu
  (12s, FR-76/v2), więc offline nadal kończą się czytelnym błędem zamiast
  zawieszenia (patrz Historia rewizji v7).

## Uwagi
Rzeczywisty zapis/odczyt z prawdziwego Firestore można zweryfikować tylko
na urządzeniu z dostępem do sieci Google/Firebase. Logika synchronizacji
(wybór synchronizowanych pól, debouncing, scalanie tylko zmienionych pól,
zachowanie przy nowym/istniejącym koncie) została zweryfikowana
automatycznie z podstawionym (mockowanym) klientem Firestore; rzeczywiste
działanie między dwoma prawdziwymi urządzeniami wymaga sprawdzenia przez
użytkownika.

Zgłoszony 2026-08-25 (web): po wdrożeniu FR-76/v3 (limit+debounce na
przepisach społeczności) użytkownik zgłosił, że aplikacja nadal się zacina,
tylko rzadziej. Ponowny CPU profiling przez zdalny debugger na telefonie
użytkownika wykazał ten sam rodzaj wewnętrznego obciążenia Firestore SDK,
teraz powracający regularnie co ~30-45s — skorelowany z okresowym
odświeżaniem kanału transportu WebChannel (normalne zachowanie protokołu
long-polling), nie z jakością sieci. Przyczyna: `enablePersistence
({synchronizeTabs:true})` — tryb "multi-tab" offline cache w IndexedDB —
wymagał kosztownej koordynacji międzykartowej przez IndexedDB przy każdym
takim odświeżeniu. Usunięty całkowicie (patrz Historia rewizji v7).

## Historia rewizji
- **v1** (2026-08-08): Pierwsza wersja wymagania — naprawia zgłoszony błąd
  ("po zalogowaniu na dwóch urządzeniach nie zsynchronizowało mi nazwy
  użytkownika ani spiżarni, ani żadnych ustawień jak chociażby to żeby
  pokazywało przepisy innych użytkowników"), realizując punkt 6 checklisty
  z `docs/FIREBASE_MIGRATION_PLAN.md`.
- **v2** (2026-08-08): Rozszerzono zakres synchronizacji na listę zakupów,
  planer i pozostałe wcześniej wyłączone pola, z prawdziwym scalaniem
  zmian — patrz FR-78.
- **v3** (2026-08-24, Android): Naprawiono zgłoszenie użytkownika, że dane
  między Web a Androidem na tym samym koncie Google rozjeżdżają się mimo
  synchronizacji w chmurze. Znaleziony rzeczywisty błąd: ulubione przepisy
  (przycisk ⭐/❤️ na karcie przepisu — pole `favorites`, wymienione w Opisie
  tego FR-73 od samego początku obok `favIngredients`) były w Androidzie
  poprawnie zaimplementowane (FR-2) i poprawnie zapisywane trwale na dysku
  lokalnie, ale `CloudSyncCoordinator.kt` — jedyny mechanizm faktycznie
  rozmawiający z Firestore — nigdy tego pola nie wysyłał ani nie odbierał.
  Skutek: przepis oznaczony jako ulubiony na jednym urządzeniu NIGDY nie
  pojawiał się na drugim, niezależnie od tego, ile razy zsynchronizowało się
  cokolwiek innego — cichy, trwały rozjazd tego jednego pola, nie utrata
  danych przy nadpisaniu. Naprawione dopisaniem `favoriteRecipes` do
  `lastKnownFields`, śledzenia zmienionych pól przy wypychaniu i odbierania
  zdalnych zmian w `CloudSyncCoordinator.kt` — dokładnie ten sam wzorzec co
  pozostałe 16 już zsynchronizowanych pól, z reużyciem tego samego kodeka
  (`encodeFavIngredients`/`decodeFavIngredients`), którego już używała
  synchronizacja lokalna. `versionCode` 72→73, `versionName` 0.1.71→0.1.72.
  `./gradlew :logic:test :app:assembleDebug` przechodzi. Zweryfikowane na
  emulatorze (Medium_Phone_API_35): dotknięcie ☆→★→☆ na karcie przepisu nie
  powoduje crasha, stan lokalny poprawnie się odwraca. **Nie zweryfikowane
  na żywo dwoma prawdziwymi urządzeniami na tym samym koncie Google
  jednocześnie** — wymaga ręcznego sprawdzenia przez użytkownika (ten sam
  rodzaj ograniczenia co FR-78/v12-v14 wyżej: to środowisko może realnie
  kompilować/uruchamiać Kotlin, ale nie ma jak samodzielnie zalogować się na
  prawdziwe konto Google na dwóch urządzeniach naraz). Pełny opis w
  `android/PARITY.md`.
- **v4** (2026-08-24, w toku): użytkownik przeprowadził pierwszy realny test
  dwóch urządzeń na żywo (wylogowanie z obu, zalogowanie TYLKO na Androida,
  ustawienie diety + wygenerowanie posiłków/listy zakupów, potem zalogowanie
  na to samo konto na web) i zgłosił, że kompletnie NIC się nie
  zsynchronizowało — ani nazwa użytkownika, ani dieta, żadne pole — co jest
  poważniejszym objawem niż pojedynczy brakujący klucz naprawiony w v3.
  Zgłosił też, że web po zalogowaniu na tym konkretnym urządzeniu wisi/nie
  odpowiada przez kilkanaście sekund, czego nie zauważył wcześniej na
  przeglądarce na komputerze. Zbadano hipotezę, czy v3 (dodanie pola
  `favorites` do `mergeFields`) mogło spowodować regresję przez odrzucenie
  CAŁEGO zapisu przez reguły bezpieczeństwa Firestore (gdyby reguła miała
  allowlistę dozwolonych pól) — sprawdzono udokumentowaną regułę w
  `docs/FIREBASE_MIGRATION_PLAN.md` (`allow read, write: if request.auth !=
  null && request.auth.uid == uid`, bez żadnej allowlisty pól), więc to
  mało prawdopodobne, O ILE wdrożona reguła faktycznie odpowiada
  udokumentowanej — nie ma jak tego sprawdzić z tego środowiska (reguły są
  wklejane ręcznie w konsoli Firebase, nie ma ich w repozytorium). Znaleziony
  i naprawiony realny brak w kodzie NIEZALEŻNIE od przyczyny źródłowej: zapis
  do Firestore w `CloudSyncCoordinator.kt` (`try/catch` wokół `.set(...)`)
  całkowicie połykał każdy wyjątek bez logowania — więc jeśli zapis faktycznie
  się nie udawał (np. permission-denied), nie było ŻADNEGO śladu w logach,
  tylko cisza. To samo dotyczyło błędu nasłuchiwania (`addSnapshotListener`'s
  drugi parametr, dotąd ignorowany jako `_`). Oba miejsca dostały teraz
  `Log.w("CloudSyncCoordinator", ...)` z pełnym wyjątkiem — nie zmienia to
  zachowania synchronizacji, tylko daje `adb logcat`/Android Studio Logcat
  realny ślad następnym razem, gdy coś się nie uda. **To NIE jest jeszcze
  potwierdzona naprawa właściwej przyczyny** — przyczyna zgłoszenia z tej
  wersji wciąż nieznana, wymaga odtworzenia z podłączonym Logcat (filtr
  `CloudSyncCoordinator`) i/lub sprawdzenia w konsoli Firebase, czy dokument
  `users/{uid}` w ogóle powstał i jakie pola zawiera. `versionCode` 73→74,
  `versionName` 0.1.72→0.1.73. `./gradlew :logic:test :app:compileDebugKotlin`
  przechodzi.
- **v5** (2026-08-24): Użytkownik odtworzył scenariusz z v4 z podłączonym
  `adb logcat` (filtr `CloudSyncCoordinator`) i sprawdzonym dokumentem w
  konsoli Firebase — w przeciwieństwie do v4 tym razem dokument
  `users/{uid}` istniał i był bogato wypełniony (profil, spiżarnia,
  ulubione, historia gotowania), więc ogólny mechanizm zapisu/odczytu
  działa. Znaleziono jednak realny, potwierdzony błąd powodujący TRWAŁĄ
  UTRATĘ DANYCH na prawdziwym koncie: pole `history` (dziennik akcji
  spiżarni/zakupów, FR-42) miało w chmurze 200 wpisów zebranych przez
  tygodnie na webie; po tym jednym teście na Androidzie skurczyło się do
  7 wpisów (tylko z dzisiejszej sesji na Androidzie). Przyczyna: web
  (`index.html`'s `addLog()`) zapisuje `ts` jako string ISO-8601
  (`new Date().toISOString()`), a Android (`CloudSyncCodec.decodeActivityLog`)
  wymagał liczby (`epochMillis`) i CICHO ODRZUCAŁ każdy wpis z `ts` w
  postaci stringa (`mapNotNull` + rzutowanie `as? Number`). Odczyt
  prawdziwej, 200-elementowej historii z Firestore dawał więc pustą listę
  (nie `null` — kod traktował to jako poprawny, kompletny wynik), którą
  `CloudSyncCoordinator` aplikował lokalnie i zapisywał jako nowy punkt
  odniesienia synchronizacji (`lastKnownFields`). Każda kolejna akcja w
  spiżarni na Androidzie dopisywała się do tej (już pustej) lokalnej listy,
  która przez to różniła się od punktu odniesienia — i ponieważ `history`
  jest wypychane jako CAŁKOWITE nadpisanie pola (`SetOptions.mergeFields`
  na poziomie całego pola, nie scalanie elementów tablicy, w odróżnieniu od
  `eaten`/`waterHistory`), ten push trwale nadpisał prawdziwą, 200-wpisową
  historię z Firestore garścią nowych wpisów z Androida. Naprawione: (1)
  `encodeActivityLog` zapisuje teraz `ts` w tym samym formacie co web
  (string ISO-8601, dokładnie jak `toISOString()`); (2) `decodeActivityLog`
  akceptuje ZARÓWNO string ISO, jak i starą liczbową postać (kompatybilność
  wsteczna z wpisami już zapisanymi przez Androida przed tą poprawką).
  Dodano dwa testy regresyjne w `CloudSyncCodecTest.kt` (dekodowanie
  wpisu zapisanego przez web, dekodowanie starego wpisu liczbowego).
  **Utracone przez ten błąd ~193 wpisy historii NIE zostały odzyskane
  przez tę poprawkę** — poprawka zapobiega POWTÓRZENIU się utraty danych,
  nie cofa już wykonanego nadpisania (Firestore nie ma włączonego
  Point-in-Time Recovery dla tego projektu, więc odzyskanie nie jest
  możliwe z poziomu konsoli). Przy okazji potwierdzono w logach osobny,
  mniejszy błąd: pojedynczy push pola `displayName` zakończył się
  wyjątkiem `LeftCompositionCancellationException` (coroutine
  `CloudSyncCoordinator`'a został anulowany, bo hostujący go composable
  opuścił kompozycję w trakcie oczekiwania na sieć, prawdopodobnie podczas
  przejścia ekranu logowania) — nie naprawione w tej rundzie, opisane jako
  osobna, otwarta obserwacja. `versionCode` 74→75, `versionName`
  0.1.73→0.1.74. `./gradlew :logic:test :app:assembleDebug` przechodzi.
  **Nie zweryfikowane jeszcze na żywo** — wymaga powtórzenia scenariusza
  (wylogowanie obu, zalogowanie tylko na Androidzie, edycja spiżarni) z
  nową wersją, żeby potwierdzić że `history` po tej poprawce poprawnie
  scala się zamiast się nadpisywać.
- **v6** (2026-08-24): Podczas testowania v5 na żywo (drugie fizyczne
  urządzenie Android) użytkownik zgłosił nowy, realny objaw: zalogował się
  na web, uzupełnił profil, potem otworzył Androida na tym samym koncie —
  Android pokazał STARE dane, nie te właśnie wpisane na web. Sprawdzenie
  dokumentu w konsoli Firebase ujawniło, że pole `profile` zawierało
  DWA równoległe zestawy kluczy naraz: `heightCm: 189` (stary zapis
  Androida) OBOK `height: 178` (świeży zapis web), podobnie
  `weightKg`/`weight`, `targetWeightKg`/`targetWeight`. Przyczyna:
  `CloudSyncCodec.encodeProfile`/`decodeProfile` od zawsze używały
  WŁASNYCH, Kotlinowych nazw pól (`heightCm`/`weightKg`/`targetWeightKg`)
  i nazw enumów (`sex: "MEZCZYZNA"`, `activity: "LEKKO_AKTYWNY"`,
  `goal: "BUDOWANIE"`), podczas gdy `index.html` od zawsze zapisuje
  `height`/`weight`/`targetWeight` oraz `sex: "m"/"k"`,
  `activity: "1.2".."1.725"` (sam współczynnik jako string),
  `goal: "loss"/"maintain"/"gain"` — z całego obiektu `profile` tylko
  `age`/`glutenFree`/`lactoseFree`/`strictLowGI`/`configured` miały
  identyczne nazwy po obu stronach. Ponieważ oba pushe używają Firestore
  `{merge:true}` (scalanie PO LIŚCIACH ścieżek, nie całego mapa naraz),
  te różnie nazwane pola nigdy się nie nadpisywały wzajemnie — po prostu
  cicho współistniały w tym samym dokumencie w nieskończoność, a
  `decodeProfile` Androida czytał WYŁĄCZNIE własne nazwy pól, więc nigdy
  nie zauważał edycji z web. To oznacza, że synchronizacja profilu
  między Web a Androidem prawdopodobnie NIGDY realnie nie działała dla
  płci/wzrostu/wagi/wagi docelowej/aktywności/celu — tylko dla tych 5
  pól, które przypadkiem miały tę samą nazwę po obu stronach. Naprawione:
  `encodeProfile`/`decodeProfile` używają teraz DOKŁADNIE tych samych
  nazw pól i formatów wartości co `index.html` (nowe funkcje mapujące
  `sexToWeb`/`sexFromWeb`, `goalToWeb`/`goalFromWeb`,
  `activityToWeb`/`activityFromWeb`), ten sam wzorzec naprawy co `ts` w
  v5. Dodane testy regresyjne w `CloudSyncCodecTest.kt` (dekodowanie
  mapy w dokładnym kształcie web, kodowanie do dokładnego kształtu web).
  `versionCode` 76→77, `versionName` 0.1.75→0.1.76. `./gradlew
  :logic:test :app:assembleDebug` przechodzi. **Nie zweryfikowane jeszcze
  na żywo** — wymaga powtórzenia: uzupełnić profil na web, otworzyć
  Androida na tym samym koncie, sprawdzić że pokazuje te same dane (nie
  stare). Istniejące, już zdesynchronizowane duplikaty pól (`heightCm`
  obok `height` itp.) w dokumentach kont, które już mają ten problem,
  NIE są automatycznie sprzątane przez tę poprawkę — sam kod przestaje
  pogłębiać rozjazd, ale stare, osierocone Kotlin-owe klucze zostają w
  dokumencie, dopóki ktoś ich nie wyczyści (np. przyciskiem resetu konta,
  FR-89).
- **v7** (2026-08-25, Web only): Usunięto `fbDb.enablePersistence
  ({synchronizeTabs:true})` (Firestore's własny, "multi-tab" offline cache
  w IndexedDB) — patrz sekcja "Uwagi" powyżej po pełne uzasadnienie
  znalezione zdalnym CPU profilingiem. Android NIE dostał odpowiadającej
  zmiany: natywny SDK Firestore tam ma domyślną trwałość opartą o SQLite w
  jednym procesie, bez koncepcji "multi-tab" ani jej narzutu koordynacji
  przez IndexedDB — to specyficzny koszt web'owej implementacji w
  przeglądarce, świadomie udokumentowana rozbieżność w `android/PARITY.md`.

- **v8** (2026-08-29, Web only — REALNY BUG UTRATY ZMIAN): usuwanie
  czegokolwiek w wersji webowej nigdy nie docierało do chmury, więc po
  chwili wracało. Zgłoszenie: „w Web nie udawało mi się wyłączyć śledzenia
  produktu, po chwili po skasowaniu dalej wracało do starej wartości”.
  Przyczyna: `pushStateToCloud()` zapisywał dokument przez
  `set(..., {merge:true})`, a Firestore przy `merge:true` scala pola
  mapowe (`pantry`, `shopping`, `planner`, `eaten`, `favorites`, …)
  KLUCZ PO KLUCZU. Skasowanie klucza lokalnie (`delete state.pantry[x]`)
  wysyłało więc mapę BEZ tego klucza, a scalanie traktuje „brak klucza”
  jako „nie ruszaj”, nie jako „skasuj”. Serwer zachowywał starą pozycję,
  kolejny snapshot ją przysyłał, a ponieważ lokalna baza scalania
  (`_lastSyncedSnapshot`) była już po skasowaniu, `computeMergedSyncState`
  poprawnie odczytywał to jako „zdalne dodanie” i przywracał wpis.
  Dotyczyło KAŻDEGO usuwania w aplikacji (śledzenie w spiżarni, pozycje
  listy zakupów, sloty planera), nie tylko spiżarni — po prostu w
  spiżarni zmartwychwstały wpis widać najbardziej. Naprawione zamianą na
  `set(payload, {mergeFields: Object.keys(payload)})`: każde wymienione
  pole zapisuje się jako CAŁA wartość (tak jak `update({pantry:{...}})`),
  więc mapa, która straciła klucz, traci go też na serwerze; pola
  spoza listy pozostają nietknięte — czyli ściśle bezpieczniej niż
  `{merge:true}`. Android był od początku odporny (`CloudSyncCoordinator`
  zawsze używał `SetOptions.mergeFields`), stąd bug wyłącznie webowy.
  Przy okazji do synchronizowanych pól dołączył `pantryHidden` (FR-102).

---

# FR-74: Wspólna zakładka „Śniadania” na liście przepisów, osobne sloty w Planerze

**Obszar:** Przepisy i przeglądanie
**Status:** Zaimplementowane

## Opis
Na liście przepisów (kategorie/pigułki nad listą) „Śniadania” i „II Śniadanie”
pokazują się jako JEDNA wspólna zakładka „🍳 Śniadania”, zawierająca przepisy
z obu wewnętrznych kategorii (`cat: "sniadania"` i `cat: "drugie"`)
wymieszane w jednej liście. Przepisy przypisane do `cat: "drugie"` mają na
karcie dodatkowy znaczek „🥪 II Śniadanie”, żeby dało się je odróżnić od
zwykłych śniadań mimo wspólnej listy.

Planer tygodniowy NIE jest tym objęty — nadal ma pięć osobnych,
niezmienionych slotów dziennych (Śniadanie, II Śniadanie, Obiad, Kolacja,
Deser/Przekąska), bo to tam rozróżnienie ma realne znaczenie (dwa różne
posiłki tego samego dnia). Formularz „➕ Dodaj swój przepis” też zachowuje
pełny wybór z pięciu kategorii — kategoria przepisu w danych źródłowych się
nie zmienia, zmienia się tylko sposób GRUPOWANIA ich do przeglądania.

## Kryteria akceptacji
- Pasek kategorii nad listą przepisów pokazuje 4 pigułki (Śniadania, Obiady,
  Kolacje, Deser/Przekąska), nie 5.
- Wybranie pigułki „Śniadania” pokazuje przepisy z OBU kategorii źródłowych
  (`sniadania` i `drugie`) razem, posortowane/filtrowane tak samo jak
  pozostałe zakładki (wyszukiwanie, ulubione, dopasowanie do profilu itd. z
  FR-2 działają identycznie na połączonej liście).
- Sortowanie „🎯 dopasowanie do profilu” liczy dopasowanie KAŻDEGO przepisu
  względem WŁASNEGO celu makro dla jego rzeczywistej kategorii (śniadanie
  albo II śniadanie osobno) — połączenie zakładek do przeglądania nie
  spłaszcza precyzji tego wyliczenia.
- Planer tygodniowy (`CATS`, 5 kategorii) oraz formularz dodawania własnego
  przepisu pozostają całkowicie niezmienione.
- Przepis z `cat: "drugie"` ma widoczny znaczek „🥪 II Śniadanie” na karcie,
  niezależnie od tego, gdzie jest wyświetlany.
- Zapisanie nowego własnego przepisu w kategorii „II Śniadanie” przełącza
  widok z powrotem na wspólną zakładkę „Śniadania” (a nie na nieistniejącą
  już osobną zakładkę), gdzie nowy przepis od razu widać.

## Historia rewizji
- **v1** (2026-08-08): Pierwsza wersja wymagania, na życzenie użytkownika
  ("połącz w przepisach pierwsze i drugie śniadanie, po co to rozdzielać,
  tylko w planerze potrzebujemy tego jako dwóch slotów ale lista z
  przepisami może być jedna").

---

# FR-75: Widok kafelkowy listy zakupów z brakującymi ilościami

**Obszar:** Lista zakupów
**Status:** Zaimplementowane

## Opis
Zakładka Zakupy ma przełącznik widoku nad listą: „📃 Lista” (domyślny,
istniejący widok wierszy z checkboxami) i „🏺 Kafelki (jak w spiżarni)”.
Oba widoki pokazują te same dane (`state.shopping`), pogrupowane tak samo
(wg `classify()`/`GROUP_ORDER`) — różni się tylko forma prezentacji.

Widok kafelkowy renderuje każdą pozycję jako kafelek w stylu identycznym z
kafelkami spiżarni (ikona + nazwa produktu, ten sam komponent wizualny co w
zakładce Spiżarnia). Znaczek na kafelku pokazuje wartość UJEMNĄ — ile danego
produktu jeszcze brakuje względem tego, co jest już w spiżarni, żeby
przygotować wszystkie zaplanowane dania wymagające tego składnika (np.
„−200 g” oznacza: potrzebujesz jeszcze 200 g więcej niż masz). Jeśli
spiżarnia w pełni pokrywa potrzebną ilość, kafelek pokazuje zamiast tego
„✓”. Stuknięcie kafelka oznacza pozycję jako kupioną/niekupioną — dokładnie
ta sama akcja co zaznaczenie checkboxa w widoku listy, więc oba widoki są
zawsze w pełni zsynchronizowane (nie ma dwóch niezależnych źródeł prawdy).

## Kryteria akceptacji
- Przełączenie widoku nie zmienia zawartości listy zakupów, tylko sposób
  jej wyświetlenia.
- Ilość „brakuje” liczona jest jako: (ilość potrzebna na liście zakupów) −
  (ilość w spiżarni w tej samej jednostce), nigdy poniżej „✓” (brak
  ujemnych wartości „na plusie” — nadwyżka w spiżarni po prostu daje „✓”,
  a nie np. „+300 g”).
- Jeśli jednostka pozycji w spiżarni różni się od jednostki na liście
  zakupów (np. spiżarnia ma „szt.”, a lista zakupów potrzebuje „g”), kafelek
  traktuje to jak brak pokrycia w spiżarni (pokazuje pełną potrzebną ilość
  jako brakującą) — ten sam ostrożny fallback co istniejący znacznik „🏺
  masz” w widoku listy.
- Oznaczenie kafelka jako kupionego w widoku kafelkowym widać natychmiast
  jako odhaczony checkbox po przełączeniu z powrotem na widok listy, i
  odwrotnie.

## Historia rewizji
- **v1** (2026-08-08): Pierwsza wersja wymagania, na życzenie użytkownika
  ("na zakładce zakupy daj możliwość przełączania widoku na taki jak w
  spiżarni żeby było widać ikonki tego co trzeba kupić, niech będą na
  minus jak czegoś brakuje").
- **v2** (2026-08-25, Android): użytkownik zgłosił, że pozycje na liście
  zakupów (oba widoki: lista i kafelki) miały wprawdzie plakietki
  kategorii przy każdej pozycji, ale same pozycje były wymieszane, nie
  pogrupowane w widoczne sekcje jak na webie (nagłówki typu "NABIAŁ BEZ
  LAKTOZY"). `ShoppingScreen.kt` grupuje teraz oba widoki po kategorii
  (`PantryTiles.categoryAndEmoji` + `CATEGORY_ORDER`, ten sam schemat co
  `ShoppingOperations.buildShareText()` już używał dla tekstu do
  udostępnienia — nie wymyślono drugiej klasyfikacji) z nagłówkiem sekcji
  przed każdą grupą. `./gradlew :app:assembleDebug :logic:test` przechodzi.
  `versionCode` 80→81, `versionName` 0.1.79→0.1.80. **Nie zweryfikowane
  wizualnie na emulatorze.**

---

# FR-76: Przepisy społeczności oraz przeglądana lista użytkowników i profili

**Obszar:** Konto i chmura
**Status:** Zaimplementowane (wymaga wdrożenia reguł bezpieczeństwa Firestore w konsoli — patrz `docs/FIREBASE_MIGRATION_PLAN.md`)

## Opis
Rozszerzenie synchronizacji z chmury (FR-73) o dane WSPÓLNE/publiczne, widoczne dla innych zalogowanych użytkowników, nie tylko dla właściciela konta:

**Przepisy społeczności.** Zapisanie własnego przepisu (przycisk „➕ Dodaj swój przepis”) — jeśli użytkownik jest zalogowany na prawdziwe (nie anonimowe) konto — dodatkowo publikuje jego kopię w kolekcji `recipes/{id}` ze statusem `"pending"`. Przepis od razu widać LOKALNIE u autora (tak jak dotychczas, przez `state.myRecipes`, niezależnie od statusu). Dopiero po ręcznym zatwierdzeniu statusu na `"approved"` w konsoli Firebase (jedyny mechanizm moderacji — brak panelu w aplikacji) przepis zaczyna się pokazywać u INNYCH użytkowników, którzy mają włączony przełącznik „🌍 Pokazuj przepisy dodane przez innych użytkowników” w Ustawieniach. Taki przepis ma na karcie znaczek „🌍 [pseudonim autora]” zamiast „✍️ Twój przepis”. Usunięcie własnego przepisu usuwa też jego kopię w chmurze. Przełącznik „🧑‍🍳 tylko przepisy użytkowników” na liście przepisów obejmuje teraz zarówno własne, jak i zatwierdzone przepisy społeczności.

**Lista użytkowników i profile.** Przycisk „👥 Przeglądaj użytkowników” (Ustawienia → „🌍 Przepisy społeczności”) otwiera listę pseudonimów wszystkich osób, które kiedykolwiek zalogowały się na prawdziwe konto, z datą ostatniego logowania (aktualizowaną automatycznie przy każdym uruchomieniu aplikacji zalogowanym). Kliknięcie osoby na liście otwiera jej profil: pseudonim, data ostatniego logowania, lista jej zatwierdzonych przepisów społeczności oraz lista przepisów, które oceniła/skomentowała (patrz FR-77). Profil NIE pokazuje adresu e-mail, danych profilu diety, spiżarni ani ulubionych — wyłącznie te jawnie publiczne informacje.

## Kryteria akceptacji
- Wszystko powyżej działa WYŁĄCZNIE dla zalogowanych na prawdziwe konto (nie anonimowe) — logowanie anonimowe nigdy nie publikuje ani nie widzi danych innych osób.
- Nowo dodany przepis społeczności jest niewidoczny dla innych użytkowników, dopóki status nie zostanie ręcznie zmieniony na `"approved"` w konsoli Firebase — reguły bezpieczeństwa Firestore jawnie uniemożliwiają autorowi samodzielną zmianę własnego statusu.
- Dane wpisywane przez innych użytkowników (nazwa przepisu, składniki, sposób przygotowania, pseudonim autora) są zawsze oczyszczane (`escapeHtml`) przed wstawieniem do strony — żadna karta przepisu społeczności ani wpis na liście użytkowników nie może wykonać obcego kodu (ochrona przed XSS).
- Nieprawidłowe/niekompletne dane w dokumencie przepisu społeczności (zła kategoria, brakujące liczby) nie psują aplikacji — `sanitizeCommunityRecipeDoc()` podstawia bezpieczne wartości domyślne zamiast pozwolić na `NaN`/nieistniejącą kategorię.
- Bez wdrożonych reguł bezpieczeństwa w konsoli Firebase te funkcje nie pokazują ani nie zapisują niczego (Firestore w trybie produkcyjnym domyślnie odrzuca dostęp) — nie jest to błąd aplikacji, tylko oczekiwany, bezpieczny stan „jeszcze nie skonfigurowane”.
- Rzeczywiste działanie (widoczność między dwoma kontami, aktualizacja daty logowania) wymaga weryfikacji na urządzeniu z dostępem do internetu, po wdrożeniu reguł — środowisko deweloperskie nie ma dostępu do serwerów Firebase.
- Zapytanie o listę użytkowników/profil, które nie zdąży się rozstrzygnąć (np. brak sieci) w rozsądnym czasie, pokazuje czytelny komunikat błędu zamiast wisieć na „Wczytywanie…” bez końca.
- Nasłuch na przepisy społeczności jest ograniczony (`.limit(300)`) i debounce'owany (3s, poza pierwszym snapshotem po podłączeniu, stosowanym natychmiast) — seria szybkich zdarzeń (zmiana w kolekcji przez innego użytkownika, ponowne połączenie strumienia Firestore po zaniku sieci) nie może wielokrotnie z rzędu przebudowywać lokalnego cache'u ani odpalać re-renderu za każdym razem — Web i Android identycznie (patrz Historia rewizji v3).

## Uwagi
Zgłoszony 2026-08-11 (web): użytkownik zgłosił, że przeglądanie listy użytkowników zawiesza się na „Wczytywanie…” bez końca (podczas gdy natywna aplikacja Android — zaimplementowana kilka godzin wcześniej tego samego dnia — poprawnie pokazuje pusty/błędny stan). Przyczyna: zapytanie Firestore w stanie faktycznie offline (bez pasującego zbuforowanego wyniku) może wisieć w nieskończoność, nie rozstrzygając się ani powodzeniem, ani błędem — `.catch()` istniał już wcześniej, ale nigdy się nie uruchamiał, bo obietnica po prostu nigdy się nie rozstrzygała. Naprawione dodaniem twardego limitu czasu (12 sekund) na oba zapytania (lista i profil) — po przekroczeniu limitu pokazuje się czytelny komunikat błędu zamiast nieskończonego "Wczytywanie…".

Zgłoszony 2026-08-25 (web): użytkownik poprosił o podłączenie zdalnego debugera Chrome (adb + wireless debugging) do swojego telefonu, żeby zdiagnozować powtarzające się zacinanie się aplikacji webowej. CPU profiling na żywej sesji wykazał, że >60% z 15-sekundowej próbki procesora szło w wewnętrzne operacje Firestore SDK (`comparator`/`insert`/`remove`/`fixUp` drzewa czerwono-czarnego lokalnego indeksu + transakcje IndexedDB), a nie w kod aplikacji (226ms) — przyczyną był `refreshCommunityRecipesSubscription()` nasłuchujący na żywo CAŁĄ kolekcję `recipes` bez limitu ani debounce, przebudowujący pełny lokalny indeks przy każdej zmianie w kolekcji przez KTÓREGOKOLWIEK użytkownika i przy każdym ponownym połączeniu strumienia Firestore po zaniku sieci. Ten sam nasłuch, bez limitu/debounce, istniał identycznie w Androidzie (`CommunityCoordinator.kt`) — naprawiony równolegle.

## Historia rewizji
- **v1** (2026-08-08): Pierwsza wersja wymagania, na życzenie użytkownika
  ("chciałbym żeby można było przeglądać listę dań dodanych przez
  użytkowników oraz listę użytkowników, po kliknięciu na nazwę użytkownika
  w jego profilu będzie można podejrzeć tylko login, oraz datę ostatniego
  logowania, ewentualnie ulubione przepisy bądź oceniane komentowane
  przepisy").
- **v2** (2026-08-11): Naprawiono nieskończone „Wczytywanie…” przy braku szybkiej odpowiedzi z Firestore — patrz sekcja "Uwagi" powyżej.
- **v3** (2026-08-25): Naprawiono realne zacinanie się aplikacji (potwierdzone CPU profilingiem przez zdalny debugger na telefonie użytkownika) — nasłuch przepisów społeczności ograniczony `.limit(300)` i debounce'owany 3s (poza pierwszym snapshotem), identycznie Web i Android — patrz sekcja "Uwagi" powyżej.

---

# FR-77: Komentarze wielu użytkowników pod przepisem, z paginacją

**Obszar:** Ocenianie i ranking przepisów
**Status:** Zaimplementowane (wymaga wdrożenia reguł bezpieczeństwa Firestore w konsoli — patrz `docs/FIREBASE_MIGRATION_PLAN.md`)

## Opis
Przycisk „⭐ Oceń i skomentuj” (FR-67) przeniesiono z góry rozwiniętej karty przepisu na sam dół, pod składniki i sposób przygotowania. Bezpośrednio pod nim znajduje się rozwijany przycisk „💬 Komentarze innych użytkowników”. Po rozwinięciu pokazuje się lista do 3 komentarzy (autor + gwiazdki + treść komentarza, jeśli podana), pobrana na żywo z Firestore (`recipes/{id}/ratings`, ta sama kolekcja, do której zapisuje zapisanie własnej oceny). Przycisk „Pokaż więcej” pod listą doczytuje kolejne, tym razem po 10 komentarzy na raz, aż do wyczerpania wszystkich ocen tego przepisu.

Zapisanie własnej oceny/komentarza (FR-67) publikuje ją — jeśli użytkownik jest zalogowany na prawdziwe konto — jednocześnie w dwóch miejscach: `recipes/{id}/ratings/{uid}` (widoczne w komentarzach pod TYM przepisem) oraz `publicProfiles/{uid}/reviewedRecipes/{id}` (widoczne w publicznym profilu tego użytkownika, patrz FR-76). Usunięcie własnej oceny usuwa oba wpisy.

## Kryteria akceptacji
- Lista komentarzy jest domyślnie zwinięta — trzeba jawnie kliknąć „💬 Komentarze innych użytkowników”, żeby ją zobaczyć i pobrać.
- Pierwsze rozwinięcie pokazuje maksymalnie 3 komentarze; każde kolejne kliknięcie „Pokaż więcej” doczytuje kolejnych maksymalnie 10, aż serwer zwróci mniej niż żądano (koniec listy — przycisk znika).
- Gdy chmura jest niedostępna (brak sieci albo `firebaseReady===false`), sekcja komentarzy pokazuje czytelny komunikat po polsku zamiast pustej listy albo błędu w konsoli.
- Brak jakichkolwiek komentarzy dla danego przepisu pokazuje zachętę „bądź pierwszą osobą, która oceni to danie”, nie pusty ekran.
- Treść komentarza i pseudonim autora są zawsze oczyszczane (`escapeHtml`/`sanitizeRatingDoc`) przed wstawieniem do strony — to pierwsze miejsce w aplikacji renderujące dowolny tekst wpisany przez INNE urządzenie, więc ochrona przed XSS jest tu krytyczna (patrz też FR-76).
- Zapisanie/usunięcie własnej oceny odświeża od razu widoczną listę komentarzy na tej karcie, jeśli jest akurat rozwinięta (nie trzeba ręcznie odświeżać strony).
- Przycisk „⭐ Oceń i skomentuj” działa dokładnie tak samo jak wcześniej (ten sam modal) — zmieniło się tylko jego położenie na karcie.

## Historia rewizji
- **v1** (2026-08-08): Pierwsza wersja wymagania, na życzenie użytkownika
  ("przycisk oceń i skomentuj przenieś na sam dół kafelka z możliwością
  rozwinięcia tego i zobaczenia domyślnie 3 komentarzy a po show more/pokaż
  więcej żeby doczytywało powiedzmy po 10 komentarzy do tego przepisu jeśli
  takie będą").

---

# FR-85: Zatwierdzanie przepisów społeczności z poziomu aplikacji + „Moje przepisy”

**Obszar:** Przepisy i przeglądanie
**Status:** Zaimplementowane (Android), Android-only

## Opis
Rozszerzenie moderacji przepisów społeczności (FR-76/FR-68) o dwie nowe
sekcje w Ustawieniach → Konto, dostępne **tylko w aplikacji Android**:

1. **„🧑‍🍳 Moje przepisy”** — widoczna dla każdego użytkownika, który dodał
   przynajmniej jeden własny przepis („📖 Dodaj swój przepis”, FR-66).
   Lista jego przepisów wraz ze statusem moderacji pobranym z Firestore:
   „⏳ Czeka na zatwierdzenie” / „✅ Zatwierdzony” / „❌ Odrzucony” / „☁️
   Synchronizowanie…” (dopóki publikacja jeszcze nie dotarła do chmury).
2. **„🛡️ Zatwierdzanie przepisów społeczności”** — widoczna WYŁĄCZNIE dla
   konta `przemas230@gmail.com` (sprawdzane po zalogowanym e-mailu). Lista
   wszystkich przepisów ze statusem `"pending"` w całej bazie, z
   przyciskami „✅ Zatwierdź” i „❌ Odrzuć” przy każdym. Zatwierdzenie
   ustawia `status: "approved"` (przepis staje się widoczny dla innych
   użytkowników z włączonym przełącznikiem „Przepisy społeczności”, FR-68);
   odrzucenie ustawia `status: "rejected"` (dokument NIE jest kasowany, więc
   autor widzi w swoim „Moje przepisy”, że został odrzucony).

Wcześniej (FR-76) jedynym sposobem zatwierdzenia przepisu była ręczna
edycja pola `status` w konsoli Firebase — ta droga nadal działa (reguły
bezpieczeństwa nie blokują edycji z konsoli), ale nie jest już jedyną.

## Kryteria akceptacji
- „Moje przepisy” nie pokazuje się, jeśli użytkownik nie ma żadnych
  własnych przepisów (`myRecipes` puste).
- „🛡️ Zatwierdzanie…” pokazuje się WYŁĄCZNIE gdy `AuthState.SignedIn.email
  == "przemas230@gmail.com"` — dla każdego innego konta (w tym innych
  prawdziwych kont Google/e-mail) karta jest całkowicie niewidoczna, nie
  tylko wyszarzona.
- Zatwierdzenie/odrzucenie aktualizuje Firestore natychmiast (bez
  potwierdzenia/dialogu — pojedyncze kliknięcie, symetrycznie do
  dotychczasowej ręcznej edycji w konsoli) i przepis znika z listy
  oczekujących (żywy nasłuch `status == "pending"`, nie odświeżanie ręczne).
- Bez wklejonej zaktualizowanej reguły Firestore (patrz Uwagi) obie karty
  bezpiecznie pokazują pusty/nieaktywny stan zamiast crasha — ten sam
  wzorzec co reszta funkcji społecznościowych (FR-68/76/77).

## Uwagi
**Wymaga zaktualizowanej reguły bezpieczeństwa Firestore dla
`recipes/{recipeId}`** — dodano trzeci przypadek do `allow read`/`allow
update`, sprawdzający `request.auth.token.email ==
"przemas230@gmail.com"`. Pełna reguła w
`docs/FIREBASE_MIGRATION_PLAN.md`, sekcja z regułami Firestore — **użytkownik
musi ją ręcznie wkleić w konsoli Firebase**, tak samo jak przy
poprzednich funkcjach społecznościowych; do tego czasu karta moderacji
bezpiecznie pokazuje pustą listę (odczyt `status == "pending"` po prostu
nic nie zwróci dla kont bez uprawnień).

Świadoma, udokumentowana rozbieżność web/Android (patrz `android/PARITY.md`)
— funkcja dodana wyłącznie w sesji dotyczącej Kotlina; port do
`index.html` pozostaje do rozważenia w osobnej turze.

Sprawdzanie uprawnień moderatora po e-mailu (nie po uid) jest celowe — to
jedyny sposób, żeby zarówno reguła Firestore, jak i klient, rozpoznały to
samo konto niezależnie od metody logowania (Google vs e-mail/hasło mogą w
teorii dać różne uid dla tego samego adresu, jeśli konto kiedyś zmieniło
metodę logowania).

## Historia rewizji
- **v3** (2026-08-22, Android): Po wklejeniu reguły Firestore z v2 użytkownik
  zgłosił, że przycisk „Zatwierdź” dalej "nie reaguje" — ale tym razem BEZ
  żadnego Toastu w ogóle (poprzednio pokazywał `PERMISSION_DENIED`). Skoro
  Toast pokazuje się TYLKO przy porażce (v2), brak Toastu najprawdopodobniej
  oznacza, że zapis w rzeczywistości się UDAŁ — reguła zadziałała, przepis
  zniknął z listy oczekujących, ale użytkownik nie dostał żadnego
  pozytywnego potwierdzenia, więc odczytał to jako "nic się nie stało".
  Dodano `onSuccess` obok `onFailure` w `SettingsScreen.kt` — teraz
  zatwierdzenie/odrzucenie zawsze pokazuje Toast ("✅ Zatwierdzono" /
  "❌ Odrzucono" przy sukcesie, treść błędu przy porażce), więc wynik akcji
  nigdy nie jest niejednoznaczny. `./gradlew :app:assembleDebug` przechodzi.
  versionCode 65/versionName 0.1.64, `android/dist/` zsynchronizowane.
  **Nie zweryfikowane na żywo** — czeka na potwierdzenie użytkownika czy
  Toast sukcesu faktycznie się teraz pokazuje.
- **v2** (2026-08-22, Android): Użytkownik zgłosił, że „Zatwierdź” nic nie
  robi — na koncie moderatora widoczny jeden oczekujący przepis, ale
  kliknięcie przycisku nie ma żadnego efektu. Przyczyna w kodzie:
  `approveRecipe`/`rejectRecipe` (`RecipeModerationCoordinator.kt`) łapały
  KAŻDY wyjątek z zapisu do Firestore i go po cichu ignorowały (celowy
  „best-effort” wzorzec, żeby brak wklejonej reguły nie crashował karty) —
  ale to też ukrywało odmowę zapisu (`PERMISSION_DENIED`) w nieodróżnialny
  sposób od „przycisk nic nie robi”, bez żadnej wskazówki co naprawić.
  Naprawione: obie funkcje zwracają teraz `Result<Unit>`, logują błąd
  (`Log.w`) i wywołujący kod w `SettingsScreen.kt` pokazuje `Toast` z
  treścią błędu przy porażce zapisu. To NIE naprawia samej przyczyny (jeśli
  regułą jest brak wklejonej reguły Firestore z „Uwagi” wyżej, to nadal
  trzeba ją wkleić) — ale zamienia niewidoczną, niediagnozowalną ciszę w
  widoczny komunikat, więc następna próba pokaże dokładnie co Firestore
  odrzucił. Najbardziej prawdopodobna przyczyna źródłowa: widoczność
  oczekującego przepisu w karcie moderacji NIE dowodzi, że reguła
  moderatora (`request.auth.token.email == "przemas230@gmail.com"`) jest
  wklejona — jeśli to WŁASNY przepis użytkownika (submitted do testów),
  odczyt przechodzi przez starszą klauzulę `authorUid == uid`, a `update`
  wciąż blokuje starsza reguła autora (`request.resource.data.status ==
  resource.data.status` — auto-nie-może-sam-zatwierdzić), bo klauzula
  moderatora z `docs/FIREBASE_MIGRATION_PLAN.md` nigdy nie została
  wklejona w konsoli. `./gradlew :app:assembleDebug :logic:test`
  przechodzi (264 testy). versionCode 64/versionName 0.1.63,
  `android/dist/` zsynchronizowane. **Nie zweryfikowane na żywo** — czeka
  na ponowną próbę użytkownika z nową wersją i przekazanie treści Toastu.
- **v1** (2026-08-11, Android): Pierwsza wersja, na wyraźną prośbę
  użytkownika ("tylko konto przemas230@gmail.com będzie mogło zatwierdzać
  przepisy dodaj mi w ustawieniach taką opcję"). Nowe
  `ui/RecipeModerationViewModel.kt` + `ui/RecipeModerationCoordinator.kt`
  (Firestore listenery: `authorUid == uid` dla "Moje przepisy",
  `status == "pending"` tylko dla moderatora) + `MyRecipesCard`/
  `RecipeModerationCard` w `SettingsScreen.kt`. Przy okazji naprawiony
  pre-istniejący błąd: `SettingsScreen`'s `recipeViewModel` nigdy nie było
  jawnie przekazywane z `MainActivity.kt`, więc domyślny `viewModel()`
  faktycznie tworzył OSOBNĄ instancję scopowaną do ekranu Ustawień,
  niezależną od współdzielonej używanej przez resztę aplikacji — bez tej
  poprawki "Moje przepisy" pokazywałoby pustą/nieaktualną listę.
  `./gradlew :app:assembleDebug :app:testDebugUnitTest :logic:test`
  przechodzi. **Nie zweryfikowane na żywo** — wymaga (1) wklejenia
  zaktualizowanej reguły Firestore w konsoli Firebase, (2) sprawdzenia w
  Android Studio na koncie `przemas230@gmail.com` oraz na innym koncie
  (żeby potwierdzić że karta moderacji faktycznie się nie pokazuje).

---

# FR-78: Pełna synchronizacja stanu z prawdziwym scalaniem zmian (3-way merge)

**Obszar:** Konto i chmura
**Status:** Zaimplementowane (wymaga wdrożonych reguł bezpieczeństwa Firestore — patrz FR-73/FR-76)

## Opis
Rozszerzenie FR-73 (synchronizacja danych osobistych) na WSZYSTKIE pozostałe pola stanu aplikacji, zgodnie z wyraźnym życzeniem użytkownika ("wszystko ma się zapisywać do chmury i być z niej odczytywane"). Do listy synchronizowanych pól dołączono: listę zakupów (`shopping`, `recipeAdded`), planer tygodniowy (`planner`, `plannerScale`, `plannerLeftover`), historię gotowania (`cooked`), dziennik posiłków (`eaten`), historię nawodnienia i dzisiejszy stan wody (`waterHistory`, `water`), historię wagi (`weights`), log aktywności (`history`) oraz ustawienia przypomnień o wodzie (`waterNotifEnabled`, `waterReminder`, `household`).

Ponieważ te pola (zwłaszcza lista zakupów i planer) mogą być edytowane niezależnie na dwóch urządzeniach — szczególnie gdy jedno z nich było offline — naiwne „ostatni zapis wygrywa" nadpisywałoby całe pole (np. całą listę zakupów) wersją z drugiego urządzenia, gubiąc zmiany. Zamiast tego wprowadzono prawdziwe **trójstronne scalanie (3-way merge)**:

- Aplikacja pamięta `state._lastSyncedSnapshot` — ostatni stan, co do którego to urządzenie i chmura były zgodne.
- Przy każdej synchronizacji porównywane są trzy wersje: punkt wyjścia (`_lastSyncedSnapshot`), aktualny stan lokalny i stan z chmury.
- Dla pól typu „mapa" (lista zakupów, spiżarnia, planer, historia gotowania, ulubione, oceny, własne przepisy itd.) porównanie odbywa się PER POZYCJA: jeśli lokalnie dodano/zmieniono jedną pozycję, a w chmurze inną, obie zmiany zostają scalone bez utraty żadnej z nich.
- Prawdziwa sprzeczność (dokładnie ta sama pozycja zmieniona różnie w obu miejscach) domyślnie rozstrzyga się na korzyść wersji z chmury, ale użytkownik widzi ją w okienku „🔄 Synchronizacja z chmury" z możliwością przywrócenia swojej wersji dla tej konkretnej pozycji.
- Dla pól skalarnych (np. cały obiekt profilu, motyw) porównanie jest całościowe — sprzeczność zgłasza się tylko, gdy oba urządzenia rzeczywiście zmieniły dokładnie to samo pole na różne wartości.
- Porównanie „czy coś się zmieniło" jest prawdziwie strukturalne (rekurencyjne, niezależne od kolejności kluczy w obiekcie) — NIE porównaniem tekstu `JSON.stringify()`. Prawdziwy Firestore (w odróżnieniu od uproszczonego zamiennika używanego w testach) nie gwarantuje zachowania kolejności kluczy przy odczycie dokumentu, więc porównanie tekstowe dawało fałszywe różnice dla niemal każdej pozycji mapy (np. całej spiżarni) nawet gdy nic naprawdę się nie zmieniło.
- Okienko „🔄 Synchronizacja z chmury" pokazuje jedną, czytelną pozycję na KAŻDĄ zmienioną rzecz — zarówno prawdziwą sprzeczność, jak i zwykłą zmianę wyłącznie z chmury — jako prostą różnicę „📱 u mnie" / „☁️ w chmurze" (albo, dla spiżarni, jedną linię z deltą, np. „mleko: 1 l → 0 l (zużyto 1 l)"), z dwoma przyciskami: „✅ Zaakceptuj i zmień" (zostawia już zastosowaną wersję z chmury) i „↩️ Odrzuć i zostaw moje dane" (przywraca to, co było na tym urządzeniu). Okienko pokazuje też, z jakiego urządzenia pochodzi zmiana z chmury (krótka, autogenerowana etykieta typu „Windows • Chrome", zapisywana tylko lokalnie na danym urządzeniu, nigdy synchronizowana). Świadomie NIE pokazuje surowej listy „dodano/usunięto/zmieniono" pogrupowanej po polu — pierwsza wersja tego pokazu była na tyle nieczytelna (m.in. `JSON.stringify()`-podobny zrzut dla mniej typowych pól), że użytkownik poprosił o jej usunięcie na rzecz samej różnicy per pozycja (patrz Historia rewizji, v3).
- Okienko pojawia się WYŁĄCZNIE dla prawdziwych sprzeczności (obie strony zmieniły dokładnie to samo). Zwykła zmiana z chmury bez sprzeczności (np. stan spiżarni zmieniony na innym urządzeniu, wpis wagi dodany gdzie indziej) scala się automatycznie i po cichu — bez okienka, tylko z krótkim toastem „☁️ Zsynchronizowano dane z chmury". Wcześniej (do 2026-08-10) okienko pojawiało się też dla KAŻDEJ, nawet bezkonfliktowej zmiany w spiżarni — przy regularnym używaniu dwóch urządzeń naraz (np. telefon + komputer) oznaczało to, że okienko wyskakiwało niemal za każdym dotknięciem drugiego urządzenia, co się okazało na tyle męczące, że użytkownik poprosił o zawężenie go do rzeczywistych sprzeczności (patrz Historia rewizji, v4).

**Wyjątek — pierwsza synchronizacja nowego urządzenia.** Jeśli urządzenie nigdy wcześniej nie synchronizowało się z danym kontem (`_lastSyncedSnapshot === null`, np. świeża instalacja albo pierwsze logowanie na już istniejące konto), nie ma żadnej wspólnej historii do porównania — w tym jednym przypadku dane z chmury po prostu ZASTĘPUJĄ dane lokalne w całości, bez okienka sprzeczności (bo nic tu naprawdę nie jest „sprzeczne", tylko urządzenie nie miało jeszcze żadnych danych tego konta).

## Kryteria akceptacji
- Dodanie różnych pozycji do listy zakupów na dwóch urządzeniach (jedno offline w międzyczasie) skutkuje po synchronizacji listą zawierającą OBIE pozycje, nie tylko jedną.
- Zmiana DOKŁADNIE tej samej pozycji na obu urządzeniach inaczej (np. różne ilości tego samego produktu) pokazuje w okienku jedną pozycję z opisem: nazwa pola, wersja z chmury, wersja lokalna, przyciski „Zaakceptuj i zmień"/„Odrzuć i zostaw moje dane".
- Kliknięcie „Odrzuć i zostaw moje dane" dla jednej pozycji w okienku nie cofa scalenia pozostałych, niesprzecznych zmian.
- Pierwsze zalogowanie na nowym urządzeniu na już istniejące konto pobiera dane z konta bez pokazywania okienka sprzeczności.
- Dane, których dane urządzenie nigdy nie miało zmienionych (identyczne z ostatnim znanym stanem), nigdy nie generują fałszywej sprzeczności — sprzeczność zgłasza się WYŁĄCZNIE, gdy obie strony faktycznie zmieniły tę samą rzecz inaczej. W szczególności: pozycja o identycznej zawartości, ale odczytana z Firestore z innym wewnętrznym uporządkowaniem pól, NIE jest traktowana jako zmieniona.
- Zmiana ilości/poziomu jednej pozycji spiżarni na innym urządzeniu (bez sprzeczności z lokalną wersją) pokazuje w okienku synchronizacji pozycję z opisaną deltą (ubyło/przybyło ile), a nie tylko nową wartość — i wskazuje, z jakiego urządzenia pochodzi zmiana.
- Okienko synchronizacji NIE zawiera osobnej, surowej listy wszystkich dodanych/usuniętych/zmienionych pozycji pogrupowanej po polu — każda zmieniona rzecz to dokładnie jedna czytelna pozycja z różnicą „u mnie” vs „w chmurze”.
- Po zastosowaniu scalenia (z ewentualnymi ręcznymi poprawkami z okienka sprzeczności) urządzenie odsyła scalony wynik do chmury, żeby oba urządzenia zgadzały się co do finalnego stanu.
- Zmiana z chmury bez prawdziwej sprzeczności (żadna lokalna edycja tej samej pozycji) NIE otwiera okienka „🔄 Synchronizacja z chmury" — scala się automatycznie, z samym toastem „☁️ Zsynchronizowano dane z chmury" jako feedbackiem. Okienko otwiera się WYŁĄCZNIE gdy `conflicts.length>0`.
- W pasku nagłówka (obok ikony ustawień) widoczne jest małe kółeczko synchronizacji, WYŁĄCZNIE na koncie zalogowanym na prawdziwe konto: kręci się, gdy trwa zapis do chmury LUB czeka w kolejce (debounce), znika, gdy nie ma żadnej oczekującej/trwającej synchronizacji, i zmienia kolor na czerwonawy, jeśli ostatnia próba synchronizacji się nie powiodła. Wylogowanie/przejście na konto anonimowe zawsze je chowa, nawet jeśli akurat była w toku.
- Jeśli w ciągu 25 sekund nastąpi więcej niż 12 prób zaplanowania synchronizacji (niezależnie od przyczyny), synchronizacja z chmurą zatrzymuje się CAŁKOWICIE na resztę sesji przeglądarki (do odświeżenia strony) — ikonka pokazuje błąd, w konsoli pojawia się czytelny komunikat, a użytkownik widzi krótki toast z instrukcją odświeżenia. Kolejne próby po zatrzymaniu są całkowicie ignorowane (nie zliczają się dalej, nie planują żadnej pracy).
- Pojedynczy zapis do chmury, który nie zakończy się w ciągu 15 sekund, przełącza ikonkę na stan błędu zamiast kręcić się bez końca — jeśli ten sam zapis mimo to później faktycznie się powiedzie, ikonka poprawnie wraca do stanu "zsynchronizowano".

## Uwagi
Świadomie POZA zakresem: prawdziwie jednoczesna edycja (dwa urządzenia online w tej samej chwili, edytujące dokładnie to samo pole) może w rzadkich przypadkach nadal wygenerować krótkotrwałą niespójność, zanim obie strony się zsynchronizują — to nie jest pełnoprawna baza danych z transakcjami, tylko scalanie oparte na porównaniu trzech snapshotów przy każdej zmianie dokumentu. Dla użytku 1-2 osobowego gospodarstwa domowego to wystarczające; prawdziwie współbieżna edycja wielu osób tej samej wspólnej listy to docelowo zadanie dla modelu `households/*` (patrz `docs/FIREBASE_MIGRATION_PLAN.md`, wciąż niezaimplementowany).

„Kto" zmienił dane to na razie etykieta URZĄDZENIA (przeglądarka + system operacyjny wykryte z User-Agent), nie tożsamość OSOBY — to jedno konto Firebase może być używane z kilku przeglądarek/urządzeń przez tę samą osobę, więc etykieta mówi „które urządzenie", nie „kto z rodziny". Prawdziwe rozróżnianie wielu OSÓB na wspólnym koncie to ten sam, wciąż niezaimplementowany model `households/*` co wyżej.

Rzeczywiste działanie między dwoma prawdziwymi urządzeniami wymaga weryfikacji na urządzeniach z dostępem do internetu — środowisko deweloperskie nie ma dostępu do serwerów Firebase. Logika scalania (dodawanie/usuwanie/zmiana pozycji, wykrywanie sprzeczności, zachowanie przy pierwszej synchronizacji, przywracanie lokalnej wersji) została w pełni zweryfikowana automatycznie z podstawionym Firestore.

**Android świadomie implementuje UPROSZCZONY wariant, nie ten opis wprost** — na wyraźną prośbę użytkownika (patrz Historia rewizji, v8) last-write-wins PER POLE zamiast pełnego 3-way merge z oknem konfliktu opisanym wyżej. Szczegóły w `android/PARITY.md`, notatka „FR-78 (uproszczony port, 2026-08-11)”.

## Historia rewizji
- **v1** (2026-08-08): Pierwsza wersja wymagania, na życzenie użytkownika
  ("teraz znów widzę że lista zakupów się nie synchronizuje a tak nie może
  być, wszystko ma się zapisywać do chmury i być z niej odczytywane, w
  przypadku offline niech się zapisuje a potem merguje zmiany, niech pyta
  skąd zmergować zmiany... stworzy listę co doda a co usunie i co zmieni
  się po synchro, wtedy użytkownik sam może zdecydować czy to prawda czy
  nie").
- **v2** (2026-08-10): Naprawiono błąd zgłoszony przez użytkownika
  ("teraz pokazuje całą długą listę wszystkich produktów w których stan
  obecny i stan z chmury są takie same") — porównanie `deepEqualJson`
  oparte o `JSON.stringify()` było wrażliwe na kolejność kluczy, a
  prawdziwy Firestore tej kolejności nie gwarantuje, więc niemal każda
  pozycja rejestrowała się jako "zmieniona" nawet gdy była identyczna.
  Zamieniono na prawdziwe porównanie strukturalne. Przy okazji dodano
  etykietę urządzenia źródła zmiany oraz wyróżnioną sekcję zmian w
  spiżarni z konkretną deltą (ubyło/przybyło), pokazywaną też bez
  prawdziwej sprzeczności — patrz Opis i Kryteria akceptacji.
- **v3** (2026-08-10): Przebudowano samo okienko na wyraźną prośbę
  użytkownika ("w okienku synchronizacji daruj wyświetlanie historii
  zmian, jest to w tak nieczytelnej formie że... pokaż tylko różnicę
  pomiędzy danymi na danym urządzeniu a na tym co w chmurze i przycisk
  zaakceptuj i zmień albo odrzuć i pozostaw moje dane"). Usunięto osobne,
  pogrupowane po polu listy „➕ Dodane"/„➖ Usunięte"/„✏️ Zmienione"
  (dawały nieczytelny, czasem `JSON.stringify()`-podobny zrzut dla mniej
  typowych pól). Zastąpiono jedną, ujednoliconą listą pozycji — sprzeczność
  i zwykła zmiana z chmury renderują się identycznie, jako różnica „u mnie"
  vs „w chmurze" (spiżarnia dostaje jedną linię z deltą zamiast dwóch
  osobnych wartości) — z dwoma przyciskami per pozycja: „✅ Zaakceptuj i
  zmień" (jawne potwierdzenie już zastosowanej wersji z chmury) i „↩️ Odrzuć
  i zostaw moje dane" (to samo działanie co dawniejsze „Przywróć moją
  wersję", teraz dostępne dla KAŻDEJ zmiany, nie tylko prawdziwych
  sprzeczności). Sam mechanizm scalania (kolejność `merged`/`conflicts`/
  `remoteChanges`, moment zapisu do `state`) nie zmienił się — to wyłącznie
  przebudowa warstwy prezentacji tego samego wyniku.
- **v4** (2026-08-10): Na wyraźną prośbę użytkownika, po tym jak zaczął
  równolegle testować wersję Android ("to potwierdzanie zostaw tylko w
  przypadku bycia offline i potem połączenia bo to jest męczące tak co
  chwila patrzeć") — okienko przestało pojawiać się automatycznie dla
  bezkonfliktowych zmian w spiżarni (dawny warunek `conflicts.length ||
  pantryRemoteChanges.length` zmieniony na wyłącznie `conflicts.length`).
  Przy używaniu dwóch urządzeń naraz (np. telefon Android + przeglądarka)
  praktycznie KAŻDA zmiana w spiżarni z telefonu wcześniej otwierała
  okienko na drugim urządzeniu, mimo że nie było czego rozstrzygać — merge
  i tak zastosował się bezkolizyjnie. Teraz taka zmiana scala się po cichu,
  z samym toastem „☁️ Zsynchronizowano dane z chmury"; okienko zostaje
  zarezerwowane dla sytuacji, gdy oba urządzenia naprawdę zmieniły to samo
  (typowo: jedno było offline i oba zdążyły dotknąć tej samej pozycji przed
  ponownym połączeniem). Sam algorytm scalania (`computeMergedSyncState`)
  nie zmienił się — to wyłącznie zawężenie tego, kiedy wynik scalania
  wymaga decyzji użytkownika, a kiedy stosuje się automatycznie.
- **v5** (2026-08-11): Naprawiono poważny błąd zgłoszony przez użytkownika
  ("coś się zacina HTML po jakichkolwiek zmianach potem się zawiesza i nie
  da się nic zrobić") — po każdej zmianie stanu na urządzeniu zalogowanym na
  prawdziwe konto aplikacja wpadała w nieskończoną pętlę synchronizacji:
  własny, jeszcze niepotwierdzony przez serwer zapis odbijał się z powrotem
  przez `onSnapshot` jako rzekoma zmiana zdalna, co uruchamiało kolejny
  zapis, ten znów się odbijał, i tak bez końca (co ~1,5 s, bez ograniczenia
  w czasie) — karta przeglądarki stopniowo się zacinała i w końcu
  przestawała reagować. Naprawione dwoma zmianami: (1) `attachUserDocListener`'s
  `onSnapshot` pomija teraz snapshoty z `metadata.hasPendingWrites===true`
  (własny, jeszcze niepotwierdzony zapis) zamiast przekazywać je do
  scalania — dokładnie ten sam warunek, jaki natywna aplikacja Android
  miała od samego początku (`CloudSyncCoordinator.kt`); (2)
  `applyRemoteSyncedState` wysyła kolejną synchronizację
  (`scheduleCloudPush()`) tylko wtedy, gdy scalony wynik faktycznie różni
  się od tego, co przyszło z chmury (czyli było coś lokalnego jeszcze
  nie wysłanego), zamiast bezwarunkowo po KAŻDEJ odebranej zmianie, co
  wcześniej samo w sobie już wystarczało do podtrzymania pętli niezależnie
  od tego, czy cokolwiek naprawdę się zmieniło.
- **v6** (2026-08-11): Dodano widoczny wskaźnik trwającej synchronizacji
  (małe kółeczko w pasku nagłówka) na wyraźną prośbę użytkownika
  ("dokoduj małe kręcące się kółeczko gdzieś na górze na pasku
  informujące o trwającej synchronizacji żeby było widać kiedy aplikacja
  jest online i up to date") — zgłoszoną przy okazji opisu spowolnienia
  po dodaniu produktu do spiżarni (patrz FR-28's własna notatka o
  rzeczywistej przyczynie tamtego spowolnienia — samego mechanizmu
  synchronizacji akurat nie dotyczyła, ale wskaźnik i tak jest
  użyteczny do odróżnienia "trwa synchronizacja" od innych przyczyn
  chwilowego opóźnienia w przyszłości).
- **v7** (2026-08-11): Pilna poprawka po zgłoszeniu, że aplikacja NADAL się
  zacina/zawiesza mimo v5 ("dalej po kilku operacjach się zawiesza
  aplikacja html pwa... widać że cały czas się kręci ikonka synchronizacji
  w html, napraw to jest najważniejsze teraz, nie da się nic zrobić bo
  apka wisi, ogranicz to jakoś, najgorzej jest w spiżarni"). Zweryfikowano
  bezpośrednio (pobranie plików z przemas230.github.io), że produkcja już
  serwuje poprawkę z v5 — najbardziej prawdopodobna przyczyna to długo
  otwarta karta przeglądarki, która nie zdążyła jeszcze wykryć nowej
  wersji Service Workera. Niezależnie od tego dodano TWARDY wyłącznik
  bezpieczeństwa (patrz Kryteria akceptacji: limit 12 prób/25s zatrzymuje
  synchronizację na resztę sesji + limit czasu 15s na pojedynczy zapis) —
  celowo niezależny od tego, czy v5 w pełni usunęła pierwotną przyczynę,
  bo to twardy limit na WSZYSTKIE możliwe przyczyny zapętlenia, nie kolejna
  próba naprawienia dokładnie tego samego mechanizmu. Wzmocniono też
  wykrywanie aktualizacji Service Workera o sprawdzanie co 10 minut w tle
  (oprócz przy wczytaniu strony i powrocie do karty), żeby długo otwarta
  karta też sama się uleczyła. Zweryfikowane bezpośrednio w przeglądarce:
  symulacja 20 szybkich prób synchronizacji z rzędu poprawnie zatrzymuje
  mechanizm po 13. próbie.
- **v8** (2026-08-11): Użytkownik zapytał wprost, czy dla Androida
  wystarczyłoby last-write-wins oparte na tym, co faktycznie się zmieniło
  ("a tutaj nie da się zrobić też że ostatni zapis wygrywa? masz chyba
  jakiś log rzeczy zrobionych klikniętych dodanych odjętych, to może na
  tej podstawie by się dało"), zamiast pełnego portu 3-way merge z oknem
  konfliktu opisanego wyżej. Zaimplementowano w Kotlinie: każdy zapis do
  chmury ogranicza się teraz WYŁĄCZNIE do pól, które faktycznie zmieniły
  się lokalnie od ostatniej znanej zgodności z Firestore (nie do statycznej
  listy wszystkich pól jak dotychczas) — to eliminuje realne ryzyko, że
  urządzenie A nadpisze świeżą zmianę pola X z urządzenia B swoją
  nieaktualną lokalną kopią tego samego pola tylko dlatego, że A akurat
  zapisywało niezwiązaną zmianę pola Y. Nie ma jednak okna konfliktu — jeśli
  oba urządzenia edytują DOKŁADNIE to samo pole, zanim zobaczą nawzajem
  swoje zmiany, wygrywa cicho to, które dotrze do serwera jako ostatnie.
  Pełny opis w `android/PARITY.md`.
- **v9** (2026-08-11): Naprawiono poważne zgłoszenie użytkownika ("w pwa
  wszystko zacina się przez tą synchronizację, kręci i kręci, jak wyłączę
  internet to działa normalnie... dalej jest to samo nawet gorzej,
  synchronizuje dopiero przy wyjściu z aplikacji i na wejściu bo tak co
  chwila to widzę że nie da rady w czasie rzeczywistym"). Rzeczywista
  przyczyna: KAŻDE zdarzenie `onSnapshot` odpalało od razu pełne 29-polowe
  porównanie scalające ORAZ pełne przebudowanie listy 229 kart przepisów,
  niezależnie od tego, czy zmiana dotyczyła czegokolwiek związanego z
  przepisami — przy aktywnym drugim urządzeniu zdarzenia te potrafiły
  przychodzić co kilka sekund. Naprawione: (1) odbiór zmian z chmury jest
  teraz debounce'owany (1,2 s) tak jak wysyłanie, więc kilka szybkich
  zdarzeń synchronizacji scala się w jedno zastosowanie najnowszych danych;
  (2) przebudowanie listy przepisów uruchamia się tylko, gdy faktycznie
  zmieniło się coś na nią wpływające (spiżarnia/ulubione/własne
  przepisy/oceny/przełącznik społeczności), nie przy KAŻDEJ zmianie (np.
  wpisie wagi). Synchronizacja pozostaje real-time — świadomie NIE
  przerzucono na "tylko przy starcie/zamknięciu", mimo że użytkownik
  zasugerował to jako alternatywę, bo cel (płynność) dało się osiągnąć bez
  utraty korzyści z prawdziwego syncu w tle. `versions/v78/`, Service
  Worker v51→v52.
- **v10** (2026-08-11): Naprawiono realny błąd utraty danych zgłoszony przez
  użytkownika ("w kotlin nie zapamiętuje mi w ustawieniach że jestem
  mężczyzną, przełącza mi na kobietę... cel zmieniałem a teraz widzę znów z
  defaultu wstawił"), najprawdopodobniej odsłonięty przez v8 (usunięcie
  "przypadkowego samoleczenia" z pełnego nadpisywania wszystkich pól przy
  każdym zapisie). Przyczyna: baza porównawcza `lastKnownFields` żyła
  wyłącznie w pamięci, resetując się do pustej przy każdym restarcie
  aplikacji — jeśli użytkownik zamknął aplikację zanim edycja zdążyła się
  wypchnąć do chmury, pierwszy odebrany snapshot po restarcie (ze STARĄ
  wartością) wyglądał jak nowa zmiana i cicho nadpisywał świeżą lokalną
  edycję. Naprawione trwałym zapisem tej bazy na dysk (`CloudSyncBaselineStore`,
  per konto), wczytywanym PRZED dopuszczeniem synchronizacji z Firestore do
  startu. Zweryfikowane bezpośrednio na emulatorze: edycja profilu +
  wymuszone zamknięcie aplikacji w środku okna debounce + ponowne
  uruchomienie (dwukrotnie) — wartość poprawnie przetrwała i pozostała
  stabilna. Pełny opis w `android/PARITY.md`.
- **v11** (2026-08-11): Mimo poprawki v9, użytkownik zgłosił, że web nadal
  odczuwalnie "wczytuje konto" bez przerwy przy aktywnym drugim urządzeniu
  ("ta synchronizacja w apce webowej musi być wykonywana jakoś pod spodem
  bo nie da się tak pracować... rozwiąż to raz a porządnie... może to być
  robione raz na jakiś czas"). Znaleziono dodatkową przyczynę: KAŻDA
  bezkolizyjna zmiana z chmury (czyli niemal każdy cykl synchronizacji przy
  aktywnym drugim urządzeniu) pokazywała toast „☁️ Zsynchronizowano dane z
  chmury" — pojawiający się co kilka sekund, wyglądał jak ciągłe
  przeładowywanie konta, mimo że nic nie wymagało uwagi użytkownika (mały
  wskaźnik synchronizacji w nagłówku już to pokrywał bez przeszkadzania).
  Naprawione: (1) zwykłe, bezkolizyjne synchronizacje są teraz zawsze ciche
  (bez toastu) — okno sprzeczności nadal się pokazuje dla prawdziwych
  konfliktów, to wystarczający sygnał samo w sobie; (2) okno debounce'a na
  odbiór zmian z chmury wydłużone z 1,2 s do 3 s, celowo traktując
  propagację zmian z INNYCH urządzeń jako "wkrótce, w tle", a nie
  natychmiastową — lokalne zmiany nadal zapisują się do localStorage od
  razu (synchronicznie, niezależnie od tego mechanizmu) i wypychają się do
  chmury na własnym, niezmienionym 1,5-sekundowym debounsie. Zweryfikowane
  bezpośrednio w przeglądarce: symulowana bezkolizyjna zmiana zdalna
  zastosowała się poprawnie bez pokazania toastu. Service Worker v54→v55.
- **v12** (2026-08-11, Android): Naprawiono zgłoszenie użytkownika ("za
  każdym razem wraca mi jakaś defaultowa dieta, mimo że jestem zalogowany
  do konta Google"). Dwie znalezione przyczyny w `CloudSyncCoordinator.kt`,
  obie w mechanizmie `lastKnownFields`/`CloudSyncBaselineStore` wdrożonym w
  v10: (a) `CloudSyncBaselineStore` nie miało metody czyszczenia, więc
  "Wyczyść dane lokalne" (FR-79) resetowało wszystkie ViewModele do
  domyślnych wartości, ale zostawiało bazę porównawczą nietkniętą — kolejne
  logowanie na TO SAMO konto widziało nadchodzący snapshot z Firestore jako
  "już znany" (bo zgadzał się z bazą) i nigdy go nie stosowało na (teraz
  domyślnym) stanie lokalnym, trwale. To dokładnie pasuje do "za każdym
  razem" — raz uszkodzona baza sama się nie naprawia. Naprawione nową
  `CloudSyncBaselineStore.clear()`, wołaną na starcie `onClearLocalData` w
  `MainActivity.kt`, analogicznie do web'owego `state._lastSyncedSnapshot =
  null` przy wylogowaniu. (b) zapis bazy na dysk nie miał debounce, podczas
  gdy `LocalPersistenceCoordinator` zapisuje tę samą pociągniętą z chmury
  wartość z 500ms opóźnieniem — zabicie procesu w tym oknie tuż po realnym
  pobraniu danych mogło zostawić bazę już zgodną z Firestore, a plik
  lokalnego stanu wciąż ze starą wartością, odtwarzając ten sam objaw przy
  kolejnym uruchomieniu. Naprawione opóźnieniem zapisu bazy o 600ms, żeby
  zapis lokalnego stanu niezawodnie wygrywał wyścig. `./gradlew
  :app:assembleDebug :app:testDebugUnitTest :logic:test` przechodzi. Pełny
  opis w `android/PARITY.md`. **Nie zweryfikowane na żywo** — wymaga
  ręcznego odtworzenia w Android Studio (zalogować się, "Wyloguj i wyczyść
  dane", zalogować się ponownie na to samo konto, sprawdzić że wraca
  prawdziwy profil/plan zamiast domyślnego).
- **v13** (2026-08-11, Android): Użytkownik zgłosił, że mimo v12 problem
  nadal występuje ("mimo wyczyszczenia danych dalej wróciły poprzednie
  ustawienia, czyli kobieta mimo że był wybrany mężczyzna i reszta
  ustawień"). Znaleziona głębsza, poważniejsza przyczyna tego samego
  objawu: `onClearLocalData` resetowało wszystkie ViewModele SYNCHRONICZNIE,
  w tym samym kliknięciu co `signOut()`, ale `AuthStateListener` (jedyny
  mechanizm przestawiający `uid` w `CloudSyncCoordinator` na inny niż
  prawdziwe konto) odpala się asynchronicznie. Jeśli nie zdążył odpalić się
  na czas, `CloudSyncCoordinator` był wciąż skomponowany z PRAWDZIWYM
  `uid`, więc jego efekt push mógł wypchnąć świeżo zresetowany domyślny
  profil (Kobieta) do prawdziwego dokumentu Firestore konta, realnie
  NADPISUJĄC dane użytkownika w chmurze, a nie tylko nie odświeżając ich
  lokalnie — kolejne logowanie poprawnie pociągało z powrotem tę już
  uszkodzoną kopię. Naprawione usunięciem synchronicznego resetu z
  handlera kliknięcia: reset ViewModeli przeniesiony do
  `LaunchedEffect(authState, pendingLocalDataClear)`, który czeka aż
  `authState` faktycznie przestanie być `SignedIn`, zanim tknie
  którykolwiek ViewModel. `versionCode` 54→55, `versionName`
  0.1.53→0.1.54. `./gradlew :app:assembleDebug :app:testDebugUnitTest
  :logic:test` przechodzi. Pełny opis w `android/PARITY.md`. **Nadal nie
  zweryfikowane na żywo** — wymaga ręcznego odtworzenia w Android Studio z
  prawdziwym kontem Google.
- **v14** (2026-08-11, web): Użytkownik zgłosił, że po "Wyloguj i wyczyść
  dane" + ponownym zalogowaniu na to samo konto web dalej się "wiesza"
  ("wylogowałem się z webowej wersji pwa, wyczyściłem dane i zalogowałem
  się ponownie z nadzieją że mi pokaże ustawienia diety profilu spiżarni i
  wszystkiego tak jak na aplikacji kotlin... dalej się wiesza aplikacja
  webowa po zalogowaniu"). Dwie znalezione przyczyny w `index.html`. (a)
  Pierwszy odebrany snapshot z Firestore po zalogowaniu przechodził przez
  TO SAMO 3-sekundowe opóźnienie (`applyRemoteSyncedStateTimer`, v11) co
  zwykłe synchronizacje w tle — a Firestore typowo dostarcza DWA snapshoty
  z rzędu zaraz po zalogowaniu (jeden natychmiast z lokalnego cache SDK,
  drugi po potwierdzeniu z serwera), każdy z nich RESETUJĄCY to opóźnienie
  na nowo (`clearTimeout`+`setTimeout`), więc realny czas oczekiwania na
  prawdziwe dane mógł sięgać 5-6+ sekund — czytane jako "aplikacja się
  zawiesiła". Naprawione: pierwszy snapshot dla danego uid stosuje się
  TERAZ NATYCHMIAST (`hasAppliedFirstSnapshotForCurrentUid`), bez żadnego
  opóźnienia; opóźnienie 3s zostaje wyłącznie dla DRUGIEGO i kolejnych
  snapshotów (ochrona przed miganiem przy aktywnym drugim urządzeniu,
  niezmieniona). (b) Poważniejsza, realna przyczyna utraty danych, ten
  sam rodzaj błędu co Android v12/v13 wyżej, ale nigdy nie naprawiony po
  stronie web: `scheduleCloudPush()`/`pushStateToCloud()` nie miały ŻADNEJ
  blokady na to, czy urządzenie w ogóle zdążyło odebrać dane konta z
  Firestore — `saveState()` (wołane praktycznie przy każdej mutacji stanu,
  w tym potencjalnie podczas normalnego renderu/inicjalizacji tuż po
  zalogowaniu, ZANIM pierwszy callback `onSnapshot` w ogóle zdążył się
  wykonać) bezwarunkowo wołało `scheduleCloudPush()` — więc push mógł
  realnie dotrzeć do Firestore i NADPISAĆ prawdziwe dane konta wciąż-
  domyślnym, sprzed-synchronizacji stanem lokalnym tego urządzenia, chwilę
  po zalogowaniu. Naprawione nową zmienną `canPushToCloud` (domyślnie
  `false`, ustawiana `true` dopiero gdy urządzenie faktycznie usłyszy od
  Firestore — prawdziwy pierwszy snapshot ZASTOSOWANY albo potwierdzone że
  dokumentu jeszcze nie ma), sprawdzaną na starcie `scheduleCloudPush()`.
  Przy okazji dodano przycisk „🔄 Synchronizuj teraz” w Ustawieniach →
  Konto w chmurze (obok wylogowania), na wyraźną prośbę użytkownika ("dodaj
  może przycisk synch żeby ręcznie synchronizować i już") — wymusza
  pobranie z Firestore z pominięciem lokalnego cache SDK (`{source:
  "server"}`) i ponowną próbę wysyłki. `versions/v83/`, Service Worker
  v56→v57. Składnia zweryfikowana (`node -e "new Function(...)"` na
  każdym bloku `<script>`) — **nie zweryfikowane na żywo w przeglądarce**,
  wymaga sprawdzenia przez użytkownika (wylogować, wyczyścić dane,
  zalogować ponownie na to samo konto, potwierdzić że prawdziwe dane
  pojawiają się od razu bez wielosekundowego opóźnienia i że nie zostały
  po drodze nadpisane domyślnymi).

---

# FR-79: Wylogowanie z urządzenia

**Obszar:** Konto i chmura
**Status:** Zaimplementowane

## Opis
Karta „☁️ Konto w chmurze" w Ustawieniach ma, dla zalogowanego (nie anonimowego) konta, przycisk „🚪 Wyloguj się z tego urządzenia". Kliknięcie pyta o potwierdzenie, a następnie — osobnym pytaniem — czy oprócz wylogowania wyczyścić też dane zapisane lokalnie na tym urządzeniu (spiżarnię, listę zakupów, planer, ulubione itd.).

- Wybór „nie czyść" (domyślny, odpowiadający Anuluj w drugim pytaniu): dane lokalne zostają na urządzeniu, ale przestają się synchronizować, dopóki użytkownik nie zaloguje się ponownie — myślane pod scenariusz „loguję się z powrotem na to samo konto za chwilę" albo pod celowe zachowanie kopii lokalnej.
- Wybór „wyczyść": lokalny `localStorage` aplikacji jest usuwany i zastępowany świeżymi wartościami domyślnymi (dokładnie jak przy zupełnie nowej instalacji) — myślane pod scenariusz współdzielonego urządzenia, na którym kolejna osoba zaloguje się swoim kontem i nie powinna widzieć ani przypadkiem przejąć danych poprzedniego użytkownika.

W obu przypadkach aplikacja odłącza nasłuchiwanie synchronizacji (`users/{uid}` i przepisy społeczności), po czym wywołuje `firebase.auth().signOut()` — co automatycznie uruchamia nowe, świeże logowanie anonimowe (ten sam mechanizm co przy zupełnie pierwszym uruchomieniu aplikacji).

## Kryteria akceptacji
- Przycisk widoczny wyłącznie, gdy użytkownik jest zalogowany na prawdziwe konto (Google lub e-mail) — nie pojawia się przy logowaniu wyłącznie anonimowym.
- Po wylogowaniu aplikacja automatycznie i bez pytania loguje się ponownie jako nowy użytkownik anonimowy — użytkownik nigdy nie zostaje w stanie „niezalogowany całkowicie", tak jak przy pierwszym uruchomieniu.
- Wybór „nie czyść danych" zachowuje wszystkie lokalne dane, ale resetuje wewnętrzny punkt odniesienia synchronizacji (`_lastSyncedSnapshot`), żeby ponowne zalogowanie na TO SAMO konto poprawnie zaczęło od pełnego pobrania danych konta, a nie od potencjalnie mylącego porównania z nieaktualnym stanem sprzed wylogowania.
- Wybór „wyczyść dane" przywraca aplikację do stanu identycznego z zupełnie nową instalacją (patrz FR-72 — profil ponownie nieskonfigurowany, itd.).
- Kliknięcie przycisku bez połączenia z Firebase pokazuje komunikat „Chmura niedostępna" zamiast błędu.

## Historia rewizji
- **v1** (2026-08-08): Pierwsza wersja wymagania, na życzenie użytkownika
  ("brakuje przycisku wyloguj").

---

# FR-80: Dzień tygodnia przy składniku na liście zakupów

**Obszar:** Lista zakupów
**Status:** Zaimplementowane (web + Android)

## Opis
Każda pozycja na liście zakupów (widok listy i widok kafelkowy, FR-75) pokazuje w nawiasie za nazwą, na który dzień (lub dni) tygodnia jest potrzebny dany składnik — wyłącznie dla składników pochodzących z dań faktycznie zaplanowanych w Planerze (FR-58/FR-62's dodawanie "na dany dzień"/"na cały tydzień"). Pozwala to kupić tylko to, co potrzebne np. na dziś/jutro/pojutrze, bez konieczności kupowania od razu wszystkiego z listy. Etykieta używa tych samych skrótów co pasek dni na liście zakupów: „dziś"/„jutro"/„pojutrze" dla najbliższych dwóch dni, trzyliterowy skrót nazwy dnia dla pozostałych („pon", „wto"...). Jeśli ten sam składnik jest potrzebny na kilka dni (bo powtarza się w kilku zaplanowanych daniach), wszystkie te dni są wymienione razem, oddzielone przecinkami.

Ponieważ sklepy w niedzielę są zwyczajowo zamknięte, składnik potrzebny na niedzielę dostaje dodatkową, wyraźną adnotację „— sklepy nieczynne, kup wcześniej", zamiast cicho sugerować zakupy w dniu, w którym i tak nie da się ich zrobić.

Etykieta dnia jest w pełni WYLICZANA na bieżąco z aktualnego stanu Planera i tego, które przepisy są już na liście zakupów (`recipeAdded`/odpowiednik `contributions` w Kotlinie) — nie jest osobno zapisywana ani synchronizowana, więc nigdy nie może się rozjechać z rzeczywistym stanem planu. Składnik dodany bezpośrednio z karty przepisu (nie przez Planer) nie ma żadnej etykiety dnia — po prostu nie ma z czym jej powiązać.

## Kryteria akceptacji
- Etykieta dnia pojawia się WYŁĄCZNIE dla składników pochodzących z dania, które jest jednocześnie: (a) zaplanowane na dany dzień w Planerze, oraz (b) faktycznie znajduje się na liście zakupów (nie samo zaplanowanie, bez dodania do listy, nie generuje etykiety).
- Ten sam składnik potrzebny w kilku dniach pokazuje wszystkie te dni razem w jednej etykiecie, posortowane chronologicznie od dziś.
- Niedziela zawsze dostaje dodatkowy dopisek o zamkniętych sklepach, niezależnie od tego, z jakim innym dniem występuje razem w tej samej etykiecie.
- Etykieta dnia widoczna jest w obu widokach listy zakupów (lista i kafelki) oraz w tekście generowanym do udostępnienia (przycisk „Udostępnij"/SMS/WhatsApp/kopiuj).
- Odznaczenie/zaznaczenie pozycji jako kupionej albo zmiana planu w Planerze natychmiast aktualizuje widoczne etykiety dni bez potrzeby ręcznego odświeżenia.

## Historia rewizji
- **v1** (2026-08-11): Pierwsza wersja wymagania, na życzenie użytkownika
  ("na liście zakupów chciałbym żeby w nawiasie albo na kafelkach też w
  nawiasie po nazwie produktu pojawiał się dzień tygodnia na który
  potrzebny jest składnik, bo może np sobie użytkownik chcieć kupić tylko
  zakupy na jutro i pojutrze i dzisiaj a nie chce już dalej, pamiętaj że
  w niedzielę sklepy są nieczynne"). Zaimplementowane jednocześnie na
  web (`computeIngredientDays`/`formatIngredientDays` w index.html) i
  Android (`ShoppingOperations.computeIngredientDays`/`formatIngredientDays`
  w logic, z testami JUnit), zweryfikowane wizualnie na emulatorze.

---

# FR-81: Propozycja przeliczenia planu i listy zakupów po zapisaniu profilu

**Obszar:** Profil i dieta
**Status:** Zaimplementowane (web + Android)

## Opis
Po kliknięciu „Zapisz i dopasuj dietę” w Ustawieniach — po zwykłym zapisaniu profilu i przeliczeniu dziennych celów kalorycznych/makro, bez zmian — aplikacja pyta dodatkowo, czy wygenerować NOWY plan posiłków na cały tydzień dopasowany do właśnie zaktualizowanej diety (ten sam mechanizm losowego generowania co „🎲 Wygeneruj losowo cały tydzień” w Planerze, FR-21). Stary plan mógł zostać ułożony pod inne parametry (inny cel, inna waga, inna aktywność) i niekoniecznie nadal dobrze pasuje.

Jeśli użytkownik się zgodzi: cały tygodniowy plan zostaje nadpisany nowymi, dopasowanymi propozycjami dań, a dotychczasowa lista zakupów zostaje wyczyszczona (bo była ułożona pod STARY plan i już nie odzwierciedla rzeczywistości). Dopiero po tym aplikacja zadaje DRUGIE, osobne pytanie: czy dodać składniki nowo wygenerowanego planu do (teraz pustej) listy zakupów. To dwa osobne pytania, nie jedno połączone, bo dotyczą dwóch różnych, niezależnie odwracalnych konsekwencji (Planer vs Zakupy).

Jeśli użytkownik odmówi pierwszemu pytaniu (nie chce nowego planu), nic więcej się nie dzieje — profil i przeliczone cele kaloryczne i tak już zostały zapisane normalnie, jak zawsze.

## Kryteria akceptacji
- Pytanie o nowy plan pojawia się PO każdym kliknięciu „Zapisz i dopasuj dietę”, niezależnie od tego, czy dane w formularzu faktycznie się zmieniły względem poprzedniego zapisu.
- Odpowiedź „nie” na pierwsze pytanie nie zmienia ani planu, ani listy zakupów — zachowanie identyczne jak przed wprowadzeniem tej funkcji.
- Odpowiedź „tak” na pierwsze pytanie: cały tygodniowy Planer zostaje nadpisany nowym losowym planem dopasowanym do ŚWIEŻO zapisanego profilu (nie starego), a lista zakupów zostaje całkowicie wyczyszczona — dopiero potem pojawia się drugie pytanie.
- Drugie pytanie („dodać składniki do listy zakupów?”) pojawia się WYŁĄCZNIE, jeśli użytkownik zgodził się na pierwsze — nigdy samodzielnie.
- Odpowiedź „tak” na drugie pytanie dodaje składniki wszystkich dań z nowego planu do listy zakupów, tym samym mechanizmem co „Dodaj składniki z całego tygodnia” w zakładce Zakupy (FR-27), włącznie z etykietami dni z FR-80.

## Historia rewizji
- **v1** (2026-08-11): Pierwsza wersja wymagania, na życzenie użytkownika
  ("po zapisz i dopasuj dietę zadaj pytanie czy przekalkulować dietę, i
  listę zakupów i jeśli użytkownik się zgodzi to wyczyść listę zakupów i
  wstaw nowe sugerowane dania do planera, dopytać czy dodać nowe zakupy na
  listę zakupów"). Zaimplementowane jednocześnie na web (rozszerzenie
  `saveSettingsBtn`'s handlera, reużywające `fittingPool`/`idealScaleFor`
  z FR-21) i Android (rozszerzenie `ProfileCard` w SettingsScreen.kt,
  reużywające `PlannerViewModel.randomizeWeek`/`ShoppingViewModel.clearAll`/
  `addWeekPlan`), zweryfikowane wizualnie na emulatorze na żywo: zapisanie
  profilu poprawnie pokazało oba pytania po kolei i poprawnie wypełniło
  listę zakupów z etykietami dni.

---

# FR-82: Widoczna wersja aplikacji w Ustawieniach

**Obszar:** Konto i chmura
**Status:** Zaimplementowane (web + Android — Android miał to już od wcześniej)

## Opis
W Ustawieniach widoczna jest linijka pokazująca aktualną wersję aplikacji zainstalowaną NA TYM URZĄDZENIU, żeby użytkownik mógł naocznie sprawdzić, czy dana aktualizacja faktycznie dotarła, zamiast zgadywać po samym wyglądzie/działaniu aplikacji.

Web (karta „☁️ Konto w chmurze”, linijka „Wersja aplikacji: …”): odczytywana bezpośrednio z aktywnej pamięci podręcznej przeglądarki (nazwa wpisu w Cache Storage, ta sama, którą Service Worker sam nadaje i utrzymuje unikalną — patrz `sw.js`'s `CACHE_NAME`), a nie z osobno utrzymywanej stałej w `index.html` — dzięki temu linijka zawsze pokazuje faktyczny stan TEGO KONKRETNEGO urządzenia/karty, nawet jeśli ktoś zapomni ręcznie zaktualizować jakąś liczbę wersji gdzie indziej. Odświeża się automatycznie po przejęciu strony przez nowy Service Worker (`controllerchange`).

Android: karta „🔄 Aktualizacja aplikacji” w Ustawieniach już od wcześniej (część pierwotnego szkieletu aktualizacji APK) pokazuje „Zainstalowana wersja: X” na stałe, niezależnie od tego, czy użytkownik akurat sprawdza dostępność aktualizacji — ten FR tylko formalnie to udokumentował, bez zmian w kodzie Kotlin.

## Kryteria akceptacji
- Web: w Ustawieniach zawsze widoczna jest linijka z aktualną wersją/nazwą pamięci podręcznej tego urządzenia, bez potrzeby otwierania narzędzi deweloperskich.
- Web: po zaktualizowaniu Service Workera (np. odświeżenie strony po wdrożeniu nowej wersji) linijka aktualizuje się na nową wartość bez potrzeby ręcznego odświeżania widoku Ustawień.
- Android: „Zainstalowana wersja: X” widoczna zawsze w karcie aktualizacji, niezależnie od stanu sprawdzania aktualizacji.

## Historia rewizji
- **v1** (2026-08-11): Pierwsza wersja wymagania, na życzenie użytkownika
  ("pokazuj wersję aplikacji webowej wewnątrz aplikacji gdzieś żeby można
  było faktycznie zobaczyć czy był update"). Zaimplementowane na web
  (`versions/v77/`, Service Worker v50→v51); Android już to miał
  (`AppUpdateCard` w `SettingsScreen.kt`), więc tylko udokumentowane.
- **v2** (2026-08-23): Użytkownik zgłosił, że linijka "Wersja aplikacji"
  utknęła na "dieta-app-v57" mimo wielu kolejnych wydań — mechanizm
  odczytu (ten FR) działał poprawnie, ale źródło (`sw.js`'s `CACHE_NAME`)
  przestało być podnoszone od wersji v77. Naprawione w FR-52/v2 —
  `CACHE_NAME` scalony z numeracją `versions/vNN` na stałe, żeby ta linijka
  znowu spełniała swój cel (widoczne potwierdzenie, że dana zmiana
  faktycznie dotarła), patrz FR-52.md.

---

# FR-83: Edycja wcześniej wpisanej wagi i historii kalorii

**Obszar:** Postęp
**Status:** Zaimplementowane na obu platformach (waga i historia kalorii)

## Opis
Do tej pory zarówno wpisy wagi (FR-40), jak i dziennik zjedzonych posiłków (FR-33/34/36/41/42) dało się tylko DODAWAĆ — pomyłkę we wpisanej wartości można było naprawić jedynie nadpisując wpis z DZISIEJSZĄ datą, bez możliwości poprawienia błędu z wcześniejszego dnia ani cofnięcia się do przeszłości w ogóle.

**Waga**: pod wykresem w karcie „⚖️ Postęp wagi” lista ostatnich wpisów (do 15, od najnowszego), każdy z przyciskiem ✏️ (edycja wartości w tym samym miejscu, bez osobnego okna) i 🗑 (usunięcie, z potwierdzeniem). Edycja waliduje tak samo jak dodawanie (30-250 kg).

**Historia kalorii (web)**: karta „📆 Dzisiaj — co zjadłam” dostała nawigację dat (◀ / pole daty / ▶, zablokowane na przyszłość) — wybranie wcześniejszego dnia pokazuje DOKŁADNIE ten sam formularz (checkboxy zaplanowanych posiłków + lista przekąsek z możliwością dodania/usunięcia), tylko dla wybranej daty zamiast dzisiejszej. Wykres „📈 Historia kalorii” poniżej odzwierciedla zmiany natychmiast, bo oba czytają z tego samego `state.eaten[data]`.

## Kryteria akceptacji
- Zmiana wartości istniejącego wpisu wagi na inny dzień nie tworzy duplikatu ani nie usuwa pozostałych wpisów.
- Usunięcie wpisu wagi wymaga potwierdzenia.
- (Web) Cofnięcie się na wcześniejszy dzień w karcie „co zjadłam” pokazuje stan TEGO dnia (nie dzisiejszego), a zaznaczenie/odznaczenie posiłku lub dodanie/usunięcie przekąski zapisuje się pod właściwą datą, nie pod dzisiejszą.
- (Web) Nie da się przejść do dnia w przyszłości (przycisk „▶”/pole daty zablokowane na dzisiaj jako maksimum).
- (Web) Powrót do dzisiejszego dnia po edycji wcześniejszego pokazuje dzisiejszy stan bez żadnych zmian wprowadzonych przy edycji innego dnia.

## Uwagi
Android's `EatenViewModel` przebudowany (2026-08-23) z modelu "tylko dzisiaj"
(`Map<String, EatenEntry>` per kategoria) na pełną historię per data
(`Map<String, EatenDay>`, `EatenDay = {entries, snacks}`) — dokładnie ten sam
kształt co web'owe `state.eaten[data]`. Konkretnie zmienione:
`EatenViewModel` (nowy `days`/`selectedDate`, `toggleForDate`/
`addSnackForDate`/`removeSnackForDate`, `kcalHistory` teraz POCHODNA z
`days` zamiast osobno akumulowana), `CloudSyncCodec.encodeEaten`/
`decodeEaten` (kodują/dekodują WSZYSTKIE daty, nie tylko dzisiejszą),
`CloudSyncCoordinator` (pole "eaten" dołączyło do zwykłej grupy
whole-field-replace zamiast wąskiej ścieżki `eaten.$today` — bezpieczne
teraz, bo Android zna pełną historię tak jak web), `LocalPersistenceCoordinator`
(pole "kcalHistory" usunięte jako redundantne, skoro liczy się samo z
`days`), nowa karta `EatenHistoryCard` w `PostepScreen` (nawigacja dat
◀/▶ zablokowana na przyszłość, te same 5 kategorii co nagłówkowy panel +
lista przekąsek z dodawaniem/usuwaniem, disabled checkbox gdy nic nie
zaplanowano na dany dzień tygodnia w Planerze — identyczna logika co web'owe
`plannedRecipeFor`/`polIndexForDate`, tylko przez `LocalDate.dayOfWeek`
zamiast JS-owego `getDay()`). Nagłówkowy panel (`HeaderKcalPanel`, zawsze
"dzisiaj", swipe-to-eat) pozostał BEZ zmian — nowa karta w Postęp jest
osobnym, dodatkowym miejscem do edycji WCZEŚNIEJSZYCH dni, nie zastępuje go.
`./gradlew :logic:test`, `:app:compileDebugKotlin` i `:app:assembleDebug`
przechodzą; zweryfikowane bezpośrednio na emulatorze (Medium_Phone_API_35,
`adb`): nawigacja ◀ na 22.08.2026, dodanie przekąski (150 kcal) pod TĄ
datą ("Zjedzono tego dnia: 150/1480 kcal"), powrót ▶ na dziś pokazał
0/1480 kcal (edycja wczorajszego dnia nie wyciekła do dzisiejszego stanu),
a wykres "📈 Historia kalorii" i "Bilans ostatnich 7 dni" natychmiast
odzwierciedliły nowy wpis.

## Historia rewizji
- **v1** (2026-08-11): Pierwsza wersja wymagania, na życzenie użytkownika
  ("dodaj mozliwośc edytowania postępu wagi jak się wpisze zła wage to daj
  jakieś okno edycjy do zmiany wartości wpisanych, tak samo historie
  kalorii żeby można było edytować wstecz"). Waga zaimplementowana na obu
  platformach, zweryfikowana bezpośrednio (Android: emulator, edycja
  07.08.2026 z 73 na inną wartość zadziałała inline; web: `javascript_tool`
  w przeglądarce). Historia kalorii zaimplementowana na web (nawigacja dat
  w karcie trackera), zweryfikowana bezpośrednio w przeglądarce (dodanie
  przekąski do 2026-08-09 poprawnie odizolowane od stanu dzisiejszego dnia)
  — Android świadomie odłożony, patrz Uwagi.
- **v2** (2026-08-23): Historia kalorii doportowana na Android, na życzenie
  użytkownika ("zacznij FR-83, edycja historii kalorii"). Przebudowa modelu
  danych z "tylko dzisiaj" na pełną historię per data (szczegóły w Uwagach),
  nowa karta z nawigacją dat w Postęp. `./gradlew :logic:test` i
  `:app:assembleDebug` przechodzą; zweryfikowane bezpośrednio na emulatorze
  (patrz Uwagi).

---

# FR-84: Scalenie oceniania przepisu w jeden mechanizm

**Obszar:** Ocenianie i ranking przepisów
**Status:** Zaimplementowane (web + Android)

## Opis
Do tej pory istniały TRZY osobne sposoby oceniania dania: przesunięcie karty w prawo/lewo (FR-55/57, binarne „lubię”/„nie lubię”), osobna gwiazdka za KAŻDE pojedyncze „✅ Zrobione” w historii gotowania (FR-17), oraz jedna deliberatywna ocena gwiazdkowa 1-5 + komentarz pod przepisem (FR-67, już wcześniej synchronizowana ze społecznością — FR-77). Na wyraźną prośbę użytkownika ("scal w jedno system gwiazdek, oceny po zrobieniu dania oraz ocene i komentarz ktory mozna dodać pod przepisem, to jedno i to samo") wszystkie trzy są teraz JEDNYM mechanizmem: oceną z FR-67 (`recipeReviews[recipeId] = {stars, comment, at}`).

- **Przesunięcie karty** nadal działa dokładnie tak samo wizualnie (napis „Podoba się to dla mnie!”/„Nie podoba się to dla mnie!”, FR-56, bez zmian — użytkownik wprost poprosił, żeby to zostało) — ale teraz jest szybkim skrótem do tej samej oceny: w prawo ustawia 5★, w lewo 1★, zachowując ewentualny istniejący komentarz.
- **Historia gotowania** (✅ Zrobione) jest teraz czystym logiem DAT — bez własnej oceny za każdy wpis. Zamiast tego pokazuje przycisk „⭐ Oceń to danie” (albo „⭐ Twoja ocena: X/5 (zmień)”, jeśli już ocenione), który otwiera DOKŁADNIE to samo okienko co przycisk „⭐ Oceń i skomentuj” pod przepisem.
- **Plakietka w prawym górnym rogu karty** pokazuje Twoją ocenę jako „★N” (zamiast dawnego 👍/👎) — kliknięcie otwiera to samo okienko oceny (zamiast dawnego czyszczenia oceny jednym dotknięciem).
- Kolorowe obramowanie karty (zielone/czerwone) nadal się pojawia, teraz na podstawie gwiazdek: 4-5★ = zielone (odpowiednik dawnego „lubię”), 1-2★ = czerwone (dawne „nie lubię”), 3★ lub brak oceny = neutralne.
- Osobny przełącznik sortowania „❤️ Ranking” (dawny FR-57) został USUNIĘTY jako redundantny — po scaleniu robił dokładnie to samo co „🏆 Ocena” (sortowanie po gwiazdkach malejąco).

## Kryteria akceptacji
- Ocena ustawiona przesunięciem karty jest natychmiast widoczna w okienku „⭐ Oceń i skomentuj” tego samego przepisu (i odwrotnie) — to jeden, wspólny stan, nie dwa oddzielne.
- Oznaczenie dania jako zrobione (✅ Zrobione) NIE tworzy własnej, osobnej oceny — jedynym miejscem ustawienia gwiazdek jest teraz ujednolicone okienko oceny, niezależnie skąd zostało otwarte (plakietka, przycisk pod przepisem, historia gotowania).
- Kliknięcie plakietki ★N w rogu karty otwiera okienko oceny (nie kasuje oceny bez potwierdzenia).
- Istniejące dane sprzed tej zmiany nie giną po cichu: stare oceny lubię/nie lubię bez odpowiadającej im pełnej recenzji migrują jednorazowo do systemu gwiazdek (lubię → 5★, nie lubię → 1★) przy pierwszym wczytaniu po aktualizacji — nadpisanie NIGDY nie dotyczy przepisu, który ma już prawdziwą recenzję.

## Uwagi
Świadomie POZA zakresem tej zmiany: stare, indywidualne oceny gwiazdkowe przypisane do POSZCZEGÓLNYCH wpisów historii gotowania (FR-17, sprzed tej zmiany) NIE są migrowane do jednej łącznej oceny — nie ma jednoznacznego sposobu wybrania, który z wielu wpisów powinien "wygrać" jako nowa jedyna ocena, a to była funkcja rzadko używana. Dane w istniejących wpisach historii po prostu przestają być czytane/wyświetlane, nic nie ginie z samego pliku/dokumentu.

## Historia rewizji
- **v1** (2026-08-11): Pierwsza wersja wymagania, na życzenie użytkownika. Zaimplementowane jednocześnie na web (`state.recipeReviews`, `setRecipeStarsQuick`, migracja w `loadState()`) i Android (`RecipeViewModel.setRatingQuick`, migracja w `replaceRatings`) — zweryfikowane bezpośrednio: web przez `javascript_tool` w przeglądarce (przesunięcie ustawiło 5★, plakietka poprawnie pokazała „★5”, historia gotowania bez pola oceny, okienko oceny pokazało zapisaną wartość), Android na żywo na emulatorze (ten sam przebieg: „⭐ Oceń to danie” w historii otworzyło okienko, zapisanie 5★ pokazało plakietkę „★5” z zieloną obwódką karty). Zero crashy.

---

# FR-87: Motyw „Klinika” — czcionka i układ, nie tylko kolory

**Obszar:** Wygląd aplikacji (wszystkie 5 zakładek), Android + Web
**Status:** Zaimplementowane na obu platformach (wariant dzień + noc)

## Opis
Dodano 12. motyw kolorystyczny — „Klinika” (id `clinic`) — wybierany w
Ustawieniach obok pozostałych 11 (Zielony/domyślny, Jasny, Różowy, Ciemny,
Zbiory, Cytrusowy, Miętowy, Jagodowa noc, Polaroid, Fluent, Kafelki). W
odróżnieniu od tamtych, które są czystym portem palety kolorów `index.html`
(FR-48), „Klinika” ma WŁASNĄ czcionkę, WŁASNE promienie zaokrągleń i WŁASNY
układ na każdym z 5 ekranów — na wyraźną prośbę użytkownika, żeby nie był to
"po prostu kolejny motyw z innymi kolorkami".

Paleta v2 (2026-08-23): przestrojona na dokładny odpowiednik palety
"sage + stone" z zewnętrznego projektu `diet-chef-pro-75` (Lovable, patrz
Historia rewizji) — ciepły kremowy background (`#F9F7F5`), karty czysta
biel, akcent szałwiowy (`#6DA480`), tekst niemal czarny cieply (`#1E1B16`),
czerwień tylko dla ikon usuwania/błędów (`#E7000B`). Każda wartość to
realny token OKLCH z `diet-chef-pro-75`'s `src/styles.css`, przeliczony na
sRGB — nie kolor dobrany "na oko".

Czcionki: **Space Grotesk** (nagłówki/tytuły, 500–700) i **DM Sans**
(tekst/etykiety, 400–700) — zbindlowane jako pliki `.ttf` (fonty zmienne,
`res/font/space_grotesk_variable.ttf`/`dm_sans_variable.ttf`, licencja OFL,
`android/FONT_LICENSES.txt`), bez nowej zależności Gradle. Zastosowane
WYŁĄCZNIE gdy motyw „Klinika” jest aktywny (`ClinicTypography`/`ClinicShapes`
w `ui/theme/ClinicTheme.kt`, podpięte w `Theme.kt` warunkowo po `themeId`) —
pozostałych 11 motywów nadal używa systemowej czcionki i `AppShapes`, zero
zmian dla istniejących użytkowników innych motywów.

Układ (tylko gdy „Klinika” aktywna, ten sam stan/callbacki ViewModeli co
reszta motywów — zero zmian logiki):
- **Planer**: od v7 ekran zaczyna się od nowego dashboardu dnia (patrz
  Historia rewizji v7) — powitanie + data + wylogowanie, rząd trzech kart
  (CEL / cienki pierścień „zjedzone/cel” + sage prostokąt „POZOSTAŁO” /
  WODA), dekoracyjny pasek 7 dni tygodnia (dziś pierwsze i wyróżnione),
  sekcja „Dzisiejszy Planer” (karty dzisiejszych posiłków z × do usunięcia
  z planu, przerywany placeholder „+ [kategoria]” dla pustych slotów).
  Pod dashboardem, bez zmian: pasek bento z celem dziennym (kcal/białko/
  tłuszcz/węgle), karty dni z odznaką „Dziś”, wiersze posiłków jako
  zaokrąglone chipy z emoji-avatarem.
- **Przepisy**: kategorie jako od razu widoczny rząd chipów (zamiast panelu
  rozwijanego stukiem).
- **Zakupy**: wiersz z kolorowym badge kategorii produktu (`IngredientCanon.
  CANON_INFO.cat`), ikona kosza w kolorze `error`, pusty stan z ikoną 🛒.
- **Postęp**: kafelek wagi (aktualna/zmiana 30-dniowa/cel), licznik wody
  jako pełne kółka z przyciskami +/-.
- **Spiżarnia**: kategorie jako akordeon (stuknięcie w nagłówek
  zwija/rozwija, domyślnie wszystkie rozwinięte) — gesty dodawania/
  odejmowania/zmiany kategorii na kafelku (`PantryTile`) całkowicie
  nietknięte.
- **Dolny pasek nawigacji (v2, wszystkie 5 zakładek)**: zamiast dokowanego
  Material3 `NavigationBar` — "pływająca pigułka" z widocznym marginesem od
  krawędzi ekranu, aktywna zakładka podświetlona wypełnionym szałwiowym
  kółkiem wokół ikony — port dolnego paska z `diet-chef-pro-75`, na wyraźną
  prośbę użytkownika ("podobały mi się też w lovable karty na dole...
  że nie były osadzone na dole tylko jakby nad ekranem"). `FloatingBottomNav`
  w `MainActivity.kt`, gated na `AppThemes.isClinicFamily(LocalDietaThemeId.
  current)`.
- **Nagłówek (v3, kolko kalorii + posiłki dnia)**: v2 przestroiła TYLKO
  paletę koloru wypełnienia istniejącego nagłówka — sam nagłówek nadal był
  jednolitym blokiem koloru z białym tekstem, jak w pozostałych 11
  motywach. Użytkownik trafnie to wychwycił ("nie widzę zmian w Nagłówek...
  jest tak naprawdę w tym motywie tylko karty na dole"). Naprawione: dla
  Klinika/Klinika (noc) nagłówek (`TopAppBar` + `HeaderWaterRow` +
  `HeaderKcalPanel`) stoi teraz na jasnym/ciemnym tle strony (`background`),
  a sam panel kalorii/wody renderuje się jako WŁASNA, uniesiona karta
  (`Card`, zaokrąglenie `extraLarge`, cień) z ciemnym/jasnym tekstem zamiast
  białego na wypełnieniu — dokładnie jak w artefakcie-podglądzie. Pierścień
  kalorii przefarbowany na `primary` (szałwia), pierścień nawodnienia na
  `tertiary` (przygaszony błękit) zamiast na sztywno wpisanych pomarańczu/
  niebieskim z pozostałych motywów.
- **Nagłówek (v7, usunięcie globalnego pierścienia kcal/wody)**: od v7
  globalny nagłówek (`header.app-top` na webie, `TopAppBar`+
  `HeaderKcalPanel` na Androidzie) nie pokazuje już pierścienia kcal/wody
  ani listy dzisiejszych posiłków dla Klinika/Klinika (noc) — cała ta
  treść (i logika: `onToggleEaten`, `eatenEntries`, `snacks`) przeniosła
  się do nowego dashboardu Plannera opisanego wyżej. Pozostałych 11
  motywów globalny nagłówek nie zmienia się w ogóle.
- **"Klinika (noc)" — nowy, 13. motyw**: osobny, wybieralny ciemny wariant
  (id `clinic_dark`) obok jasnej "Klinika", na wyraźną prośbę użytkownika
  ("zrob klinika dzien i noc motyw taki jak w propozycji"). Ten sam akcent
  szałwiowy co w wersji dziennej (diet-chef-pro-75's własny tryb ciemny też
  zostawia `--primary` bez zmian), reszta palety z dark-mode tokenów OKLCH
  tego projektu. Dzieli DOKŁADNIE ten sam `ClinicTypography`/`ClinicShapes`/
  `FloatingBottomNav`/nagłówek-kartę co wersja dzienna — `AppThemes.
  isClinicFamily(id)` rozpoznaje oba warianty jednym miejscem zamiast
  osobnych porównań `== "clinic"` rozsianych po 8 plikach ekranów.

## Kryteria akceptacji
- Wybranie motywu „Klinika” w Ustawieniach zmienia paletę, czcionkę I układ
  jednocześnie na wszystkich 5 zakładkach.
- Wybranie dowolnego z pozostałych 11 motywów daje DOKŁADNIE taki sam
  wygląd jak przed tą zmianą (ten sam `AppShapes`/systemowa czcionka/układ).
- Dolny pasek nawigacji (v2) "pływa" z widocznym marginesem od krawędzi
  ekranu WYŁĄCZNIE gdy „Klinika” (dzień LUB noc) jest aktywna; wszystkie
  pozostałe motywy zachowują dokowany `NavigationBar` bez zmian.
- Nagłówek (kółko kalorii + posiłki dnia) renderuje się jako uniesiona
  karta na jasnym/ciemnym tle strony WYŁĄCZNIE dla Klinika/Klinika (noc);
  pozostałe motywy zachowują jednolity blok koloru z białym tekstem bez
  zmian.
- Wybranie „Klinika (noc)” daje ciemne tło/karty z tym samym akcentem
  szałwiowym co „Klinika”, ten sam font/kształt/układ/pływający pasek.
- (Web) Oba warianty Klinika dostępne w Ustawieniach → Wygląd, dają
  DOKŁADNIE ten sam efekt wizualny co Android (paleta, fonty, naglówek-
  karta, pływający pasek), pozostałych 11 motywów bajt-w-bajt niezmienione.
- (Web) Przewinięcie listy przepisów przy aktywnej Klinice poprawnie zwija
  nagłówek (bez pustej, czarnej dziury zamiast niego — patrz Historia
  rewizji).
- (Web) Planer/Zakupy/Postęp/Spiżarnia dają przy aktywnej Klinice ten sam
  układ co Android (bento paski, kolorowe badge kategorii, akordeon
  kategorii spiżarni, licznik wody jako kółka +/-); pozostałych 11 motywów
  te ekrany bajt-w-bajt niezmienione.
- Przy aktywnej Klinice/Klinika (noc) globalny nagłówek NIE pokazuje
  pierścienia kcal/wody ani listy dzisiejszych posiłków — ta treść żyje
  wyłącznie w nowym dashboardzie na górze zakładki Planer (toggle
  „zjedzone”, usuwanie przekąsek itd. nadal działają, tylko stamtąd).
  Pozostałych 11 motywów globalny nagłówek zachowuje pierścień kcal/wody
  bez zmian.
- Dashboard Plannera (powitanie/karty CEL-POZOSTAŁO-WODA/pasek dni/
  „Dzisiejszy Planer”) renderuje się WYŁĄCZNIE dla Klinika/Klinika (noc);
  pozostałych 11 motywów zakładka Planer wygląda identycznie jak przed
  tą zmianą (patrz też FR-88 — kolejność samej zakładki w nawigacji
  zmienia się dla wszystkich motywów, ale nie jej zawartość).
- `./gradlew :app:assembleDebug :logic:test` przechodzi.

## Uwagi
v1-v3 były świadomą, udokumentowaną rozbieżnością web/Android — funkcja
dodana wyłącznie w sesjach dotyczących Kotlina, `index.html` nie miał
odpowiednika motywu „Klinika”. Doportowane na web w v4 (paleta/fonty/
naglówek-kartę/pływający pasek) i v5 (pozostałe 4 bespoke layouty: Planer,
Zakupy, Postęp, Spiżarnia) — od teraz web i Android mają dokładnie ten sam
wygląd Klinika/Klinika (noc) na wszystkich 5 zakładkach.

Paleta v2 (2026-08-23) i "pływający" pasek nawigacji zostały wyciągnięte z
zewnętrznego, wygenerowanego przez Lovable projektu `diet-chef-pro-75`
(github.com/przemas230/diet-chef-pro-75), pokazanego przez użytkownika jako
inspiracja wizualna po komentarzu "wygląd bardziej nowoczesny... ale
funkcjonalnie moje aplikacje są lepsiejsze". Zanim cokolwiek zostało
przerobione w kodzie, przygotowany został artefakt-podgląd (jedna karta
przepisu w obu stylach, prawdziwe dane) do zatwierdzenia kierunku przez
użytkownika — dopiero po jego "bardzo dobry kierunek... jedziemy dalej"
wdrożono to do `AppThemes.kt`/`MainActivity.kt`. honey/plum (drugorzędne
akcenty schematu `AppThemeDef`) nie mają wprost odpowiednika w Lovable
(tam paleta jest niemal monochromatyczna: krem + jedna zieleń szałwiowa) —
dobrane jako stonowane, spójne warianty (ciepła glina / przygaszony błękit),
tylko po to, żeby dwie kategorie posiłków obok siebie dało się odróżnić.

`logic/.../AppThemesTest.kt`'s test na "dokładnie 11 motywów 1:1 z
index.html" zaktualizowany, by jawnie udokumentować `clinic` jako jedyny
zamierzony wyjątek (patrz komentarz w teście) — pozostałych 11 nadal musi
się zgadzać z web co do joty.

## Historia rewizji
- **v1** (2026-08-22, Android): Pierwsza wersja. Pierwotna prośba
  użytkownika o "przeprojektowanie zgodnie z panującymi trendami" (styl
  medyczny/kliniczny, bento grid, Space Grotesk/DM Sans) doprecyzowana po
  jego uwadze "jak planujesz dodać kolejny motyw tylko inne kolorki to
  możesz sobie wogóle darować" — stąd fonty/kształty/układ per-motyw, nie
  globalna zmiana. `./gradlew :app:assembleDebug :logic:test` przechodzi.
  **Nie zweryfikowane wizualnie na emulatorze** — wymaga sprawdzenia w
  Android Studio (wybrać „Klinika” w Ustawieniach, przejść przez wszystkie
  5 zakładek, potwierdzić że reszta motywów wygląda bez zmian).
- **v2** (2026-08-23, Android): Paleta przestrojona na dokładny odpowiednik
  `diet-chef-pro-75` (patrz Uwagi) i dodany "pływający" dolny pasek
  nawigacji, na wyraźną prośbę użytkownika po zatwierdzeniu artefaktu-
  podglądu kierunku. `AppThemes.kt`'s `clinic` wpis (kolory), `ClinicTheme.
  kt`'s `ClinicShapes` (large 22dp→24dp, extraLarge 24dp→28dp, bliżej
  Lovable's `--radius: 1.25rem` skali), nowy `FloatingBottomNav` w
  `MainActivity.kt`. `./gradlew :app:compileDebugKotlin :app:assembleDebug`
  przechodzi; zweryfikowane bezpośrednio na emulatorze (Medium_Phone_API_35):
  paleta, czcionki i pływający pasek z podświetloną aktywną zakładką
  potwierdzone na żywo na liście przepisów i rozwiniętej karcie.
- **v3** (2026-08-23, Android): Użytkownik zgłosił, że v2 przestroiła TYLKO
  wypełnienie istniejącego nagłówka, nie sam jego wygląd ("jest tak
  naprawdę w tym motywie tylko karty na dole"), i poprosił o osobny wariant
  dzień/noc ("zrob klinika dzien i noc motyw taki jak w propozycji").
  Nagłówek (`TopAppBar`/`HeaderWaterRow`/`HeaderKcalPanel`/`KcalMealRow`/
  `WaterCupIcon` w `MainActivity.kt`) przebudowany na jasne/ciemne tło
  strony + panel kalorii/wody jako własna uniesiona karta, pierścienie
  przefarbowane na `primary`/`tertiary` (patrz Opis). Nowy 13. motyw
  „Klinika (noc)” (id `clinic_dark`) w `AppThemes.kt`, kolory z dark-mode
  OKLCH tokenów `diet-chef-pro-75`, ten sam akcent szałwiowy co dzień.
  Nowa `AppThemes.isClinicFamily(id)` zastąpiła 12 rozsianych porównań
  `== "clinic"` w 8 plikach ekranów (`MainActivity.kt`, `PostepScreen.kt`,
  `PlannerScreen.kt`, `PantryScreen.kt`, `RecipeListScreen.kt`,
  `ShoppingScreen.kt`, `Theme.kt`) — jedno miejsce do zmiany, gdyby
  przybył trzeci wariant Klinika. `AppThemesTest.kt` zaktualizowany (13
  motywów, `clinic_dark` jako trzeci `isDark`, nowy test
  `isClinicFamily`). `./gradlew :logic:test :app:assembleDebug` przechodzi;
  zweryfikowane bezpośrednio na emulatorze: nagłówek jako karta na jasnym
  tle (Klinika) potwierdzony, przełączenie na „Klinika (noc)” dało ciemne
  tło/karty z tym samym akcentem na całej aplikacji (nagłówek, lista
  przepisów, pływający pasek, floating buttons), przełączenie na inny
  (niekliniczny) motyw poprawnie wróciło do dokowanego paska — potwierdza
  że gating działa w obie strony.
- **v4** (2026-08-23, Web): Użytkownik poprosił o natychmiastowy port na
  web ("przenieś od razu do Web"). `index.html`: nowe `:root[data-theme=
  "clinic"]`/`"clinic_dark"]` bloki (te same wartości hex co `AppThemes.
  kt`), Space Grotesk/DM Sans dograne do istniejącego, współdzielonego
  linku Google Fonts (web już ładował różne fonty per motyw dla Fluent/
  Metro — ten sam, sprawdzony mechanizm, nie nowy). Nowa zmienna
  `--ring-track` (kolor NIEwypełnionej części pierścienia — wcześniej "na
  sztywno" `rgba(255,255,255,.20)` wpisane w atrybut SVG, jednakowe dla
  wszystkich motywów, bo nagłówek był zawsze ciemny; teraz zmienna, żeby
  Klinika mogła to nadpisać) — zero zmian renderowania dla pozostałych 11
  motywów (dokładnie ta sama wartość liczbowa). Strukturalne nadpisania
  (`:is([data-theme="clinic"], [data-theme="clinic_dark"])`) dla nagłówka-
  karty i pływającego paska, analogiczne do już istniejącego wzorca
  Polaroid/Kafelki. **Realny błąd znaleziony i naprawiony podczas
  weryfikacji w przeglądarce** (nie tylko wizualnie — dopiero po
  przewinięciu listy): nowa reguła nagłówka-karty ustawiająca
  `max-height` miała DOKŁADNIE taką samą specyficzność CSS co istniejąca
  reguła zwijania nagłówka przy scrollu (`.collapsed .header-collapsible
  {max-height:0}`) i wygrywała z nią przez kolejność w pliku — po
  przewinięciu w dół zostawała pusta, czarna dziura zamiast poprawnie
  zwiniętego nagłówka. Naprawione usunięciem `max-height` z nowej reguły
  (istniejący mechanizm zwijania działa poprawnie bez tego, konflikt był
  całkowicie zbędny). `CACHE_NAME` podniesiony na `dieta-app-v86` (FR-52's
  zasada), backup pre-edit `index.html`/`sw.js` w `versions/v86/`.
  Zweryfikowane bezpośrednio w przeglądarce (lokalny serwer, oba warianty,
  w tym scroll-collapse po naprawie) — nie tylko kompilacją/kodem.
- **v5** (2026-08-23, Web): Po zatwierdzeniu v4 użytkownik poprosił o
  dokończenie portu ("kontynuuj resztę rzeczy" → "Tak, doportuj wszystkie 4
  układy na web"). `index.html`: `renderPlannerBento()`/`.clinic-bento`/
  `.bento-tile` (reużyte też przez Postęp) — pasek celu dziennego nad
  listą dni Planera, karty dni przełączone na wariant `.cdc-*` z awatarem/
  odznaką „Dziś”/kcal (klik odznaki skali/regeneracji ma `e.stopPropagation
  ()`, żeby nie otwierał też pickera dnia — DOM nie konsumuje zdarzeń
  automatycznie jak zagnieżdżone `clickable` w Compose). `clinicCatBadge()`
  w `renderShop()` — kolorowy badge kategorii pod nazwą produktu z tego
  samego `CANON_INFO.cat`, którego web już używał dla kafelków spiżarni
  (zero nowej kategoryzacji); przekreślenie zaznaczonej pozycji celowo
  dotyka tylko tekstu (`.label-text`), nie odznaki. `renderWater()` —
  gałąź Klinika renderuje rząd kółek z przyciskami –/+ (`setWaterCount()`,
  wydzielone z powtarzającej się logiki zapis+rerender+powiadomienie)
  zamiast rzędu emoji kropelek. `renderWeightBento()` w `renderWeights()` —
  3 kafelki (Aktualnie/Zmiana 30 dni/Cel), zmiana 30-dniowa liczona tą samą
  metodą co `PostepScreen.kt` (najstarszy wpis odległy o ≥30 dni od
  najnowszego, albo pierwszy wpis jeśli historia krótsza). `renderPantry()`
  — nagłówki kategorii jako akordeon (`clinicCollapsedPantryCats` — Set w
  pamięci, nie w `state`, jak `remember` po stronie Androida), z licznikiem
  pozycji i strzałką ⌃/⌄; domyślnie wszystko rozwinięte, więc nic się nie
  zmienia wizualnie, dopóki ktoś czegoś nie zwinie. Każdy z 4 fragmentów
  zweryfikowany bezpośrednio w przeglądarce (lokalny serwer + claude-in-
  chrome) w obu wariantach Klinika, w tym realne interakcje (klik plusa
  nawodnienia, zwinięcie kategorii spiżarni, wybór dania w pickerze
  Planera) — nie tylko wizualnie statycznie. `CACHE_NAME` podniesiony na
  `dieta-app-v87`, backup pre-edit `index.html`/`sw.js` w `versions/v87/`.
- **v6** (2026-08-23, Web + Android): Użytkownik zgłosił realny błąd kontrastu ("motyw jasny klinika jest w niektórych momentach aż za jasny, nie widać np przycisku opcji na headerze"). Znalezione i potwierdzone bezpośrednio w przeglądarce i na emulatorze: pierścień kalorii/wody w nagłówku (kółko z liczbą zjedzonych kcal) był praktycznie niewidoczny w jasnej Klinice — kolor "toru" (niewypełnionej części) był `surfaceVariant`/`var(--line)`, DOKŁADNIE ten sam token, którego już używa obramowanie/tło samej karty nagłówka — na białym tle różnica była za mała, by okiem odróżnić pierścień od karty pod nim, zwłaszcza przy 0% wypełnienia (świeży dzień). Naprawione użyciem przygaszonej wersji koloru TEKSTU (`onSurfaceVariant`/`--muted`, już zaprojektowanego z realnym kontrastem względem tła) zamiast koloru linii/obramowania: Android — `MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)` w `HeaderKcalPanel` (`MainActivity.kt`); web — `--ring-track` dla `clinic`/`clinic_dark` zmienione z `var(--line)` na dosłowny `rgba()` odpowiadający `--muted` przy tej samej alfie (custom property nie da się alpha-blendować przez `var()` bez znajomości jego RGB). Zweryfikowane bezpośrednio w przeglądarce i na emulatorze: pierścień wyraźnie widoczny (jasnoszary tor) na obu platformach, w tym przy 0% wypełnienia.
- **v7** (2026-08-23, Web + Android): Na prośbę użytkownika (dwa zrzuty
  ekranu — nowy styl pierścienia kcal, i opis nowego dashboardu Plannera)
  przeprojektowany ekran Planera dla Klinika/Klinika (noc): dawny podwójny
  pierścień kcal+woda w globalnym nagłówku (`HeaderKcalPanel`/`.kcal-row`)
  usunięty stamtąd i zastąpiony nowym, pojedynczym cienkim pierścieniem
  „zjedzone/cel” (bez podziałek, wypełnia się jak tarcza zegara) + sage
  prostokątem „POZOSTAŁO”/kcal — to nowy środkowy element rzędu trzech
  kart CEL/POZOSTAŁO/WODA na górze zakładki Planer, nie w nagłówku.
  Dashboard dodaje też: powitanie („Cześć, {imię}”) + datę + wylogowanie,
  dekoracyjny pasek 7 dni tygodnia (dziś pierwsze w kolejności i
  wizualnie wyróżnione ciemniejszą ramką — NIE przełącza, który dzień
  pokazuje sekcja poniżej), i „Dzisiejszy Planer” (karty dzisiejszych
  posiłków, × usuwa przypisanie z planu, przerywany placeholder
  „+ [kategoria]” dla pustych slotów — ta sekcja to przeniesienie/
  restyling tej samej logiki co dawny `KcalMealRow`/`.kcal-meal-list`
  z nagłówka, nie nowa logika biznesowa). Istniejący pasek bento celu
  dziennego i pełna lista 7 dni (`DailyTargetBento`/`DayCardClinic`,
  `.clinic-bento`/`.cdc-*`) zostają BEZ ZMIAN, tylko niżej pod nowym
  dashboardem. Pasek dni jest świadomie dekoracyjny (nie funkcjonalny
  przełącznik) i pełna lista 7 dni świadomie zostaje jako jedyny sposób
  edycji innych dni niż dziś — oba ustalenia z użytkownikiem przed
  implementacją (AskUserQuestion), żeby nie usuwać istniejącej
  funkcjonalności edycji dowolnego dnia. Zobacz też FR-88 dla globalnej
  (wszystkie motywy) zmiany kolejności zakładek nawigacji, ustalonej przy
  tej samej okazji, ale udokumentowanej osobno, bo nie jest specyficzna
  dla Kliniki.
- **v8** (2026-08-25, Web only): Użytkownik zgłosił sześć osobnych usterek/
  próśb naraz w motywie Klinika/Klinika (noc), wszystkie zweryfikowane
  wizualnie lokalnie w Chrome (nie tylko składniowo):
  1. Ikona synchronizacji z chmurą i ikona ustawień w nagłówku były białe
     (`color:#fff`, w tym `#settingsBtn` na sztywno w atrybucie `style`) —
     niewidoczne na jasnym tle, ten sam błąd co v6 naprawił dla pierścienia
     kcal, tylko w innych elementach, które v6 nie objął. Poprawione na
     `var(--text)`; `#settingsBtn` przeniesiony z inline `style` na klasę
     `.header-settings-btn`, żeby dało się go nadpisać per motyw bez
     `!important`.
  2. Klinika ustawiona jako motyw domyślny dla nowych instalacji (wszystkie
     `state.theme || "teal"` fallbacki → `"clinic"`; istniejące konta z już
     zapisanym motywem — bez zmian).
  3. Kwadrat "CEL" usunięty z rzędu kart dashboardu Plannera (v7) —
     środkowa karta POZOSTAŁO (pierścień + kcal) rozciąga się w lewo w to
     miejsce (`grid-template-columns` 2 kolumny zamiast 3, proporcja
     2.3fr/1fr).
  4. Pasek 7 dni (v7, dekoracyjny) rozciągnięty na pełną szerokość
     (`flex:1` zamiast stałej szerokości ze scrollem); dzisiejszy dzień
     pokazuje pełną nazwę (np. "Wtorek") zamiast 2-literowego skrótu jak
     pozostałe dni, z większym `flex-grow` na jego własnym miejscu w
     rzędzie żeby to zmieściło; ramka na dzisiejszym dniu (v7) bez zmian.
  5. Przycisk "Wyloguj się" (🚪, v7's `pd-signout`) ukryty z dashboardu
     Plannera (nadal dostępny w Ustawieniach) — CSS `display:none`, nie
     usunięty z DOM-u/JS-a. Globalny nagłówek (ikonka + "Dieta App") w
     Klinice/Klinice (noc) pokazuje teraz to samo, co dashboard Plannera
     już pokazywał: datę + "Cześć, {imię}!" (nowe współdzielone
     `clinicDateLabel()`/`clinicGreetingText()`, użyte w obu miejscach,
     żeby treść nigdy się nie rozjechała). Przy tej okazji naprawiony
     dodatkowy, wcześniej niewidoczny błąd: `applyTheme()` nie wywoływało
     `renderHeader()`/`renderPlannerDashboard()`, więc przełączenie motywu
     zostawiało starą zawartość nagłówka/dashboardu na ekranie aż do
     następnej niepowiązanej akcji — nieszkodliwe, dopóki treść nagłówka
     nie zależała od motywu, ale teraz zależy.
  6. Aktywna zakładka dolnego paska nawigacji: ikonka znikała w wypełnionym
     zielonym kółku (zielona ikonka na zielonym tle). Przyczyna: CSS
     celował w `.ic i`, element który już nie istnieje w DOM-ie po starcie
     — `lucide.createIcons()` (wywoływane raz, na końcu strony) zamienia
     każde `<i data-lucide="...">` na `<svg>`, więc selektor nigdy nie
     trafiał i kolor ikony spadał kaskadowo z `nav.bottom button.active`
     (teal) zamiast z tej reguły (biały). Poprawione na `.ic svg`.

  Web only — Android ma osobną, natywną implementację motywu Klinika
  (`AppThemes.kt`/`PlannerScreen.kt`) i wymaga osobnego sprawdzenia, czy
  te same sześć usterek tam występuje, zanim zostaną tam naprawione tym
  samym wzorcem; świadomie odłożone w tej turze, zamiast piętrzyć
  niezweryfikowane zmiany w Kotlinie bez możliwości sprawdzenia na
  emulatorze w tej samej sesji co wszystko inne. Narusza to literalnie
  kryterium akceptacji "web i Android dają dokładnie ten sam wygląd" z
  wcześniejszych wersji tego FR — udokumentowane tu jawnie jako znany,
  tymczasowy rozjazd, nie przeoczenie (patrz `android/PARITY.md`).
- **v9** (2026-08-25, Web only): Tego samego dnia, po zobaczeniu v8 na
  żywo (dwa zrzuty ekranu telefonu), użytkownik poprosił o cztery kolejne
  poprawki:
  1. Nagłówek (v8 wypełnił go datą + powitaniem, duplikując pd-header)
     zamiast tego ukryty CAŁKOWICIE — zostały tylko ikonka ustawień i
     plusik, wyrównane do prawej (`header.app-top h1{display:none}` +
     `.header-title-row{justify-content:flex-end}`), "żeby nie zaburzał
     wyglądowi strony". `renderHeader()` wrócił do wcześniejszej,
     jednolitej (bez rozgałęzienia na motyw) treści `#headerSub` — i tak
     niewidocznej, skoro `h1` jest ukryte; `clinicDateLabel()`/
     `clinicGreetingText()` (v8) zostały jako współdzielone funkcje, nadal
     używane przez `renderPlannerDashboard()`.
  2. Zgłoszony (zrzut ekranu) błąd: pływający pasek dolnej nawigacji
     wyglądał na ucięty na długiej, przewijanej liście zakupów (87
     pozycji). Przyczyna: `main{padding:14px 14px 6px}` — te same 6px
     dolnego marginesu dla WSZYSTKICH motywów — nie starczały żeby
     ostatnie pozycje listy nie chowały się pod pływającą "pigułką"
     Kliniki (własna wysokość + 12px odstępu od krawędzi + safe area).
     Naprawione osobnym, większym marginesem (`calc(100px + env(safe-
     area-inset-bottom))`) TYLKO dla Kliniki/Kliniki (noc) — pozostałych
     11 motywów (dokowany pasek, inna wysokość) nikt nie zgłosił jako
     zepsute, więc ich `main` zostaje bez zmian.
  3. Dotknięcie kwadracika "WODA" na karcie Planer (v7) nie robiło nic —
     był to zwykły, nieinteraktywny tekst. Dodany floating panel
     (`#waterPickerOverlay`, ten sam wzorzec `.modal-overlay.center` co
     reszta modali w apce) z DOKŁADNIE tym samym interaktywnym
     wybieraniem (kółka do stuknięcia, +/-), które już istniało w
     `#waterRow` (zakładka Postęp) — logika budowania tego widżetu
     wydzielona do współdzielonej `renderWaterRow(container)`, żeby nie
     duplikować kodu w dwóch miejscach. `setWaterCount()` odświeża teraz
     też panel (jeśli otwarty) i `renderPlannerDashboard()` (żeby sama
     karta WODA na Planerze też się aktualizowała na bieżąco, nie tylko
     po zamknięciu panelu).
  4. Zielony przycisk "🎲 Wygeneruj losowo cały tydzień" (siedzący nad
     paskiem bento/listą dni) przeniesiony na sam dół karty Planer, pod
     listę dni — TYLKO dla Kliniki/Kliniki (noc) (`renderPlanner()`
     przenosi realny węzeł DOM-u, `#plannerAutoPlanRow`, na koniec
     `#view-planner` dla tego motywu i z powrotem na oryginalne miejsce
     dla pozostałych 11, więc przełączanie motywu tam i z powrotem bez
     przeładowania strony działa poprawnie w obie strony).

  Wszystkie 4 zweryfikowane lokalnie w Chrome: zrzuty ekranu (nagłówek
  ukryty, przycisk przeniesiony) + bezpośrednie sprawdzenie stanu DOM/CSS
  (`getComputedStyle`, kolejność dzieci `#view-planner` dla obu wariantów
  motywu, kliknięcie kółka w panelu wody i potwierdzenie że karta WODA na
  Planerze zaktualizowała się na żywo bez zamykania panelu). Web only, z
  tego samego powodu co v8 — Android nadal czeka na osobny przegląd tych
  poprawek na emulatorze (patrz `android/PARITY.md`).
- **v10** (2026-08-25, Android): użytkownik zauważył, że dwa poprzednie
  commity (v8, v9) nie miały odpowiednika w Androidzie ("nie widzę zmian
  na androidzie ostatnich i przedostatnich, nie zaktualizowałeś wersji
  albo nie dokodowałeś tego samego") — port wszystkich 10 poprawek z v8+v9
  do Kotlina w tej turze, zamiast dalej odkładać. Zanim cokolwiek
  napisane, sprawdzone (subagentem badawczym, bez zmian w kodzie), która
  część z 10 poprawek MA odpowiadający błąd w natywnym Compose UI, a
  która była specyficznie web'owym problemem bez odpowiednika:
  - **(1) kolor ikon w headerze i (6) aktywna zakładka nawigacji — BEZ
    ZMIAN, bo Android nigdy nie miał tych błędów**: `MainActivity.kt`'s
    `TopAppBar`/`FloatingBottomNav` już od początku używały
    `MaterialTheme.colorScheme.*` (kontener/tekst/ikony), nie żadnego
    odpowiednika web'owego zaszytego na sztywno `color:#fff` w
    `style="..."` — więc kontrast był zawsze poprawny; podobnie aktywna
    ikonka w kółku nawigacji miała od zawsze osobne, kontrastowe tokeny
    (`colorScheme.primary` dla tła kółka, `colorScheme.onPrimary` dla
    ikony), nie ten sam token dla obu jak web'owy martwy CSS selektor
    powodował.
  - **(2) motyw domyślny → Klinika**: `AppThemes.DEFAULT_ID` (jedno
    źródło prawdy, `logic/.../AppThemes.kt`) `"teal"` → `"clinic"` —
    automatycznie ogarnia `ThemeViewModel`, `DietaAppTheme`,
    `LocalDietaThemeId` i reset-do-domyślnego (wszystkie referencjonują tę
    samą stałą symbolicznie, nie duplikują wartości). `AppThemesTest.kt`'s
    test na fallback nieznanego id zaktualizowany (asercja na stałą, nie
    na sztywne `"teal"`, żeby nie mogło znów po cichu się rozjechać).
  - **(3) usunięty "Cel", karta POZOSTAŁO rozciągnięta**: `PlannerScreen.kt`'s
    `PlannerDashboard` — usunięty `DashboardStatCard(label="Cel"...)`,
    waga środkowej karty 1.5f→2.3f (odpowiednik web'owego `.pd-cards`
    `2.3fr 1fr`, dawne `1f+1.5f=2.5f` Cel+Pozostało teraz w jednej karcie).
  - **(4) dzisiejszy dzień pełną nazwą**: dawne `DAYS_PL[di].take(2)` dla
    KAŻDEGO dnia zastąpione `if (isToday) DAYS_PL[di] else
    DAYS_PL[di].take(2)` — pasek dni w Compose i tak już jest
    `horizontalScroll`, więc "rozciągnięcie na pełną szerokość" (web) nie
    miało tu odpowiednika do portowania — Compose'owy Row już się
    dopasowuje naturalnie, nie trzeba było nic zmieniać w layoutcie.
  - **(5) wylogowanie ukryte + nagłówek ukryty**: `IconButton(onSignOut)`
    z `Icons.Filled.Logout` usunięty z `PlannerDashboard`'s nagłówkowego
    `Row` (parametr `onSignOut` zostaje niewykorzystany, na wypadek
    odwrócenia, tak jak web'owe `display:none` zamiast usunięcia z DOM-u).
    `MainActivity.kt`'s `TopAppBar`'s `title` lambda zwraca wcześnie
    (`return@TopAppBar`) dla `isClinicHeader` — port od razu KOŃCOWEGO
    stanu z web'a (nagłówek całkiem ukryty), pomijając pośredni stan v8
    (nagłówek = data+powitanie), którego Android nigdy nie dostał i nie
    musiał dostawać, żeby zaraz go cofać w tej samej turze.
  - **(2 z v9) ucięty pasek nawigacji na długich listach — BEZ ZMIAN,
    Android nigdy nie miał tego błędu**: `Scaffold`'s trailing lambda
    (`) { padding -> NavHost(..., modifier = Modifier.padding(padding))
    }`) rezerwuje realną przestrzeń layoutu pod top/bottom bary dla CAŁEGO
    `NavHost`-a — architektonicznie odporne na tę klasę błędu (web'owy
    problem był `position:fixed` floating pill z ręcznie utrzymywanym w
    CSS odstępem, który się rozjechał; Compose'owy `Scaffold` nigdy nie
    pozwala treści renderować się pod paskiem w pierwszej kolejności).
  - **(3 z v9) panel wyboru wody po dotknięciu karty WODA**: `DashboardStatCard`
    dostał opcjonalny `onClick`, karta "Woda" otwiera teraz `Dialog` z
    dokładnie tym samym widżetem kółek +/- co już istniał w `PostepScreen.kt`'s
    Klinika-owej karcie wody (skopiowany wzorzec, nie duplikacja przez
    nową logikę ViewModelu) — nowe `onWaterTap`/`onWaterSetCount`
    callbacki przewleczone `PlannerScreen`→`PlannerDashboard`, spięte w
    `MainActivity.kt` z istniejącym `WaterViewModel.tapDroplet`/`setCount`.
    Dzięki Compose'owej reaktywności (`waterCount` pochodzi z
    `collectAsState()` w `MainActivity`) karta WODA aktualizuje się na
    żywo automatycznie, bez ręcznego wywoływania "renderuj ponownie" jak
    na webie.
  - **(4 z v9) przycisk losowania na dole**: wyodrębniony do nowego
    `AutoPlanWeekButton` (był inline w jednym miejscu, teraz wołany z
    dwóch), `LazyColumn`'owy `item{}` z tym przyciskiem umieszczony PRZED
    `itemsIndexed(DAYS_PL)` dla pozostałych 11 motywów (bez zmian) i PO
    nim dla Kliniki/Kliniki (noc) — ten sam efekt co web'owe przenoszenie
    węzła DOM-u, tylko przez warunkowe umieszczenie w drzewie Compose
    zamiast manipulacji istniejącym elementem.

  `./gradlew :app:assembleDebug :logic:test :app:testDebugUnitTest`
  przechodzi (zero nowych błędów/regresji w istniejących testach, w tym
  zaktualizowanym `AppThemesTest`). `versionCode` 79→80, `versionName`
  0.1.78→0.1.79, zweryfikowane `aapt dump badging` PRZED skopiowaniem do
  `dist/`, `android/dist/` zsynchronizowane. **Nie zweryfikowane wizualnie
  na emulatorze** — tylko kompilacją i testami jednostkowymi, bez
  interaktywnego sprawdzenia UI (klikalności, wyglądu dialogu wody,
  faktycznego układu kart) na żywym/symulowanym urządzeniu w tej turze;
  czeka na potwierdzenie przez użytkownika po zainstalowaniu przez
  "Sprawdź aktualizację".
- **v11** (2026-08-25, Android): użytkownik zgłosił, że kafelek "Zmiana
  (30 dni)" w bento Kliniki (`PostepScreen.kt`) czasem pokazywał wiele
  miejsc po przecinku (surowe odejmowanie `Double` na wagach jest podatne
  na artefakty zmiennoprzecinkowe, np. "2.1999999999999957" zamiast
  "2.2"). Zaokrąglone do 2 miejsc po przecinku w miejscu wyliczenia
  (`kotlin.math.round(...*100)/100.0`), zanim trafi do `formatKg()`.
  `./gradlew :app:assembleDebug :logic:test` przechodzi. **Nie
  zweryfikowane wizualnie na emulatorze.**
- **v12** (2026-08-25, Web): W tej samej rundzie próśb co FR-66/v5, trzy
  kolejne zmiany specyficzne dla karty Planer w Klinice/Klinice (noc):
  1. Ikonka ustawień + plusik w headerze wyrównane wizualnie do rzędu z
     "Wtorek, 25 Sierpnia / Cześć, {imię}!" na karcie Planer, zamiast
     siedzieć w osobnym pasku nad nią. `header.app-top` zwinięty do
     wysokości 0 (dalej `position:sticky`, dalej klikalny — NIE
     `display:none`), ikony pozycjonowane przez `.header-title-row`'s
     własny padding tak, żeby wizualnie nakładały się na tę samą linię co
     tekst pod nimi. `main`'s górny margines zwiększony do 100px, żeby
     pozostałych 4 zakładek (bez tego tekstu do wyrównania) treść nie
     chowała się pod pływającymi ikonami — potwierdzone wcześniej (podczas
     testów) realnym błędem: licznik "N pozycji" na Zakupach chował się
     częściowo pod ikoną ustawień, zanim dodano tę poprawkę.
  2. Karty w "Dzisiejszym Planerze": dotknięcie przełączało dotąd
     bezpośrednio "zjedzone" — zmienione tak, że PRZESUNIĘCIE (w lewo LUB
     w prawo, oba kierunki przełączają, w odróżnieniu od kierunkowego
     swipe'a nagłówka z FR-36) przełącza "zjedzone" (nowa
     `attachPdMealCardSwipe()`, ten sam wzorzec axis-lock/tap-vs-swipe co
     `attachSwipeRating()` już używał dla kart na Przepisach), a zwykłe
     dotknięcie otwiera teraz podgląd przepisu (`openRecipePreviewModal()`)
     — nowy modal (`#recipePreviewOverlay`) renderujący TEN SAM
     `recipeCard()` co zakładka Przepisy (ulubione, lista zakupów, oceny,
     komentarze, nowe przyciski Google/YouTube z FR-66/v5 — wszystko),
     otwarty od razu rozwinięty, zamiast budować drugi, uboższy widok
     podglądu. Odpowiednik Androidowego FR-86 ("podgląd przepisu z
     Planera"), którego web nigdy wcześniej nie miał.
  3. "Dzisiejszy Planer" (nagłówek + karty POZOSTAŁO/WODA + pasek dni +
     dzisiejsze posiłki, budowane przez `renderPlannerDashboard()`) razem
     z kafelkami kcal/białko/tłuszcz/węgle (`#plannerBento`) owinięte w
     nowy `#plannerTodayWrap` (`display:flex; flex-direction:column;
     min-height:calc(100vh - 200px)`), kafelki przypięte do samego dołu
     (`margin-top:auto`) — lista 7 dni tygodnia wymaga teraz przewinięcia,
     zamiast siedzieć bezpośrednio pod dzisiejszymi kartami. Pozostałych
     11 motywów (oba elementy owijające puste dla nich) bez zmian
     wizualnych. `renderPlanner()`'s przenoszenie przycisku "🎲 Wygeneruj
     losowo" (FR-87/v9) zaktualizowane, żeby wstawiać się WZGLĘDEM nowego
     wrappera, nie bezpośrednio względem `#plannerBento` (który przestał
     być bezpośrednim dzieckiem `#view-planner`).

  Wszystkie 3 zweryfikowane lokalnie w Chrome: zrzuty ekranu + pomiary
  `getBoundingClientRect` (wyrównanie ikon, brak kolizji z treścią
  pozostałych zakładek, pozycja kafelków względem wysokości okna) +
  symulowane gesty `PointerEvent` (swipe w obu kierunkach faktycznie
  przełącza "zjedzone" bez otwierania podglądu; zwykłe dotknięcie otwiera
  podgląd z widocznymi przyciskami Google/YouTube). CACHE_NAME→v98,
  `versions/v98/`. **Android nadal NIE dostał odpowiadających zmian** —
  świadomie odłożone razem z resztą motywu Klinika z tej sesji (patrz
  `android/PARITY.md`).

  Osobna, PIĄTA prośba z tej samej rundy — pływający pasek nawigacji
  zmienia pozycję po wejściu na kartę Zakupy — NIE została jeszcze
  naprawiona: wymaga pomiaru na żywym telefonie (podejrzenie: dynamiczne
  chowanie/pokazywanie paska adresu w mobilnym Chrome przy różnicach
  wysokości/przewijalności strony między zakładkami, nie da się tego
  wiarygodnie zdiagnozować w desktopowym Chrome), a połączenie adb z
  wcześniejszej sesji zdalnego debugowania tego dnia wygasło w trakcie tej
  tury. Odłożone do ponownego podłączenia telefonu.
- **v13** (2026-08-25, Web): Po ponownym podłączeniu adb (nowe parowanie —
  poprzednie połączenie z v12 wygasło) naprawiona PIĄTA, ostatnia prośba z
  tej rundy: pływający pasek dolnej nawigacji zmieniał widoczną pozycję
  między zakładkami. Zmierzone bezpośrednio na żywym telefonie przez
  Chrome DevTools Protocol (nie zgadywane): `window.innerHeight` różniło
  się między zakładkami (499px na Planerze, 508px na Zakupach — realna
  9px różnica, zgodna z automatycznym chowaniem się paska adresu Chrome na
  dłuższych/bardziej przewijalnych stronach), podczas gdy `100dvh`
  (zmierzone tym samym sposobem) zostawało stałe na 499px, zgodnie z
  `visualViewport.height`, na obu zakładkach. `bottom:Npx` na
  `position:fixed` w mobilnym Chrome okazało się zakotwiczone do tego
  NIESTABILNEGO `innerHeight`, więc odstęp paska od faktycznie widocznego
  dołu ekranu kurczył się niepostrzeżenie — potwierdzone: 12px na
  Planerze, ~2px na Zakupach. Poprawka: `bottom:calc(12px + ...)` →
  `top:calc(100dvh - 12px - env(safe-area-inset-bottom) - 63px)` (63px =
  własna wysokość paska). Trzy kolejne kandydujące formuły przetestowane
  NA ŻYWYM URZĄDZENIU przed wyborem tej — w tym `position:sticky` (bez
  zmiany) i korekta `bottom` o `(100vh - 100dvh)` (nie zadziałała, bo
  `100vh` mierzone jawnym elementem DOM okazało się RÓWNE `100dvh`, mimo
  że `position:fixed`'s własne kotwiczenie `bottom` używa jeszcze innej,
  niestabilnej wartości — stąd rozwiązanie przez `top` zamiast korekty
  `bottom`, które omija tę niejednoznaczność całkowicie) — dopiero
  czwarta próba dała identyczną pozycję (co do ułamka piksela) na
  wszystkich 5 zakładek. CACHE_NAME→v99, `versions/v99/`. **Android
  świadomie NIE dostał odpowiadającej zmiany** — ten konkretny problem
  jest specyficzny dla `position:fixed` w mobilnym Chrome, Compose'owy
  `Scaffold` zarządza pozycją dolnego paska inaczej (patrz FR-87/v10's
  uzasadnienie dla "ucięty pasek nawigacji" — ten sam wniosek dotyczy i
  tego przypadku), ale wymaga osobnego potwierdzenia, że Android faktycznie
  nie ma żadnego analogicznego efektu, zanim zostanie to formalnie uznane
  za zamknięte.
- **v14** (2026-08-25, Web + Android): Użytkownik zgłosił, że przesuwanie
  kart w "Dzisiejszym Planerze" (v12) w ogóle nie działało na jego
  telefonie mimo że działało w symulacji, że pełnoekranowa sekcja (v12)
  zostawiała brzydkie puste pole nad kafelkami makro, i poprosił o
  wizualne podświetlenie w trakcie przesuwania + dyskretne strzałki-
  podpowiedzi (na kartach Planera ORAZ na istniejącym już geście
  "podoba się/nie podoba się" na kartach przepisów). Cztery poprawki:
  1. **Naprawiony realny błąd**: `.pd-meal-card` nie miało
     `touch-action:pan-y` — przeglądarka mobilna przejmowała gest jako
     scroll strony zanim JS (`attachPdMealCardSwipe()`) zdążył go
     obsłużyć, dokładnie ten sam mechanizm, który `.kcal-meal-row`/
     `.card.recipe-card.swipeable` już miały z tego samego powodu.
     Symulowane `PointerEvent`y w testach desktopowych nie łapały tego,
     bo desktopowy Chrome nie ma tej samej natywnej hi-jack-gestu logiki
     co mobilny.
  2. **Zmiana z toggle na kierunkowy gest**: prawo=zawsze zjedzone,
     lewo=zawsze niezjedzone (`setEaten(today, cat, dx > 0)`), zamiast
     poprzedniego `!wasEaten` niezależnego od kierunku — dokładnie tak,
     jak użytkownik opisał oczekiwane działanie.
  3. **Podświetlenie w trakcie przesuwania**: `card.style.backgroundColor`
     ustawiane na żywo w `pointermove` (zielone `rgba(60,170,110,...)` w
     prawo, czerwone `rgba(190,70,60,...)` w lewo, rosnące z
     `Math.abs(dx)/SWIPE_COMMIT`) — te same kolory co
     `attachSwipeRating()`'s już istniejący "like"/"dislike" glow, nie
     nowy schemat.
  4. **Dyskretne strzałki-podpowiedzi**: `.pd-meal-card::after{content:"↔"}`
     i `.card.recipe-card.swipeable::before/::after{content:"‹"/"›"}` —
     czysto wizualne (`pointer-events:none`), nie przechwytują żadnych
     zdarzeń.
  5. **Naprawiony wygląd pełnoekranowej sekcji**: zamiast jednej dużej
     pustej przerwy nad kafelkami makro (`margin-top:auto` na samych
     kafelkach), `#plannerDashboard{flex:1}` + `.pd-today-list{flex:1}` +
     każda karta posiłku `{flex:1}` rozciągają 5 wierszy posiłków tak, by
     same wypełniły dostępną przestrzeń — ekran wygląda w pełni
     wykorzystany zamiast pustego pola. Przy okazji poprawiony też
     `min-height:calc(100vh-200px)` z v12 na `100dvh` (ten sam błąd
     niestabilnego `vh` co FR-87/v13 znalazło i naprawiło dla paska
     nawigacji, przeoczony wtedy w tym jednym miejscu).

  Port na Android w tej samej turze (patrz `android/PARITY.md`) — po
  wyraźnym zgłoszeniu użytkownika, że kolejne rundy web nie miały
  odpowiednika na Androidzie ("znowu widzę zmiany tylko na Web").
  CACHE_NAME→v100, `versions/v100/`. **Poprawka touch-action wymaga
  potwierdzenia przez użytkownika na prawdziwym telefonie** — mobilny
  dotyk zachowuje się inaczej niż jakakolwiek symulacja dostępna z tej
  sesji.
- **v15** (2026-08-25, Android): Faktyczny port punktów 1-4 z v14 na
  Androida (v14 zapowiadał go "w tej samej turze", ale kod nie był jeszcze
  skompilowany/przetestowany w momencie zapisu tamtej rewizji — ta rewizja
  to potwierdzenie, że rzeczywiście powstał i przeszedł build).
  `PlannerScreen.kt`'s karty posiłków w `PlannerDashboard` dostały
  `Animatable` offset + `detectHorizontalDragGestures` (ten sam wzorzec co
  już istniejący gest "podoba/nie podoba się" w `RecipeListScreen.kt`,
  FR-55/61) z kierunkowym `onSetEaten(cat, dx > 0, ...)` (nie toggle),
  żywym podświetleniem tła (`dragTint`, zielony/czerwony, ten sam odcień co
  web) oraz dyskretną strzałką „↔” widoczną gdy karta nie jest przesuwana.
  Zwykłe dotknięcie (bez przesunięcia) otwiera `RecipePreviewDialog`
  zamiast przełączać zjedzone — jak na web. `RecipeListScreen.kt`'s
  istniejący gest oceny dostał analogiczne strzałki „‹”/„›” (czerwona/
  zielona, `alpha=0.3`). Nowa metoda `EatenViewModel.setEaten(cat, eaten,
  kcal, name)` (kierunkowa, obok istniejącego `toggle()`) wpięta przez
  `MainActivity.kt` → `PlannerScreen`/`PlannerDashboard`'s nowy parametr
  `onSetEaten`.

  Punkt 5 z v14 (pełnoekranowa sekcja "Dzisiejszy Planer" z kafelkami
  makro przypiętymi na dole) **świadomie NIE ma odpowiednika na
  Androidzie** — architektura tam jest fundamentalnie inna: cały ekran
  Planera to JEDNA przewijalna `LazyColumn` z `PlannerDashboard` (dziś) +
  paskiem celu kcal/makro + wszystkimi 7 kartami dni jako kolejne elementy
  listy, nie osobny, w pełni ekranowy widok "dziś" jak na web. Compose
  `LazyColumn` mierzy każdy `item{}` naturalną wysokością treści — nie ma
  tu odpowiednika CSS-owego "brzydkiego pustego pola nad kafelkami", bo
  nic nigdy nie próbowało rozciągać się na `100dvh`. Rozciągnięcie
  `PlannerDashboard` na pełną wysokość ekranu wymagałoby przeprojektowania
  całej zakładki Planer w osobny widok "dziś" + oddzielną nawigację do
  pozostałych dni tygodnia — realna zmiana architektury UX, nie port 1:1,
  więc odłożone do osobnej, jawnej decyzji użytkownika zamiast robione
  przy okazji tej rundy.

  `./gradlew :app:assembleDebug` i `./gradlew :logic:test` przeszły
  (versionCode 82, versionName 0.1.81, `android/dist/` zaktualizowane).
  Wizualna/dotykowa weryfikacja gestów na prawdziwym urządzeniu — jak przy
  v14 na web — jeszcze ⏳, nieskompilowany kod nie dowodzi, że gest
  "czuje się" dobrze w praktyce (patrz `android/PARITY.md`).
- **v16** (2026-08-25, Web + Android): Użytkownik przesłał zrzuty ekranu z
  telefonu (Android) i z Web pokazujące, że v14/v15 nie do końca zadziałały:
  (1) nad ikonami "+"/ustawień w nagłówku wciąż było widoczne puste pole na
  OBU platformach — na Web odstępy `header-title-row`/`main` zmniejszone z
  56px/100px do 10px/54px (ta sama wzajemna relacja, tylko bliżej góry); na
  Androidzie źródło było inne — `TopAppBar`'s własna, stała minimalna
  wysokość nadal rezerwowała miejsce mimo pustego `title` (bo
  `return@TopAppBar` usuwa tylko TREŚĆ, nie samą wysokość paska) —
  zastąpiony lekkim, niestandardowym `Row` (tylko `WindowInsets.statusBars`
  + 4dp) dla Kliniki, `TopAppBar` zostaje bez zmian dla pozostałych 11
  motywów. (2) "Dzisiejszy Planer" nadal nie wypełniał całego ekranu na
  ŻADNEJ platformie: Web — stała w `calc(100dvh - 200px)` (`#plannerTodayWrap`)
  była wyliczona względem starego 100px odstępu nagłówka, poprawiona na
  110px zgodnie z nowym 10px; Android — v15 uznał to za świadome N/D
  (`LazyColumn` bez odpowiednika CSS-owego pustego pola) — **ta decyzja
  odwrócona na wyraźną prośbę użytkownika z tej rundy**: `PlannerDashboard`
  jako pierwszy `item{}` dostał `Modifier.fillParentMaxHeight()`
  (`LazyItemScope`'owy odpowiednik `min-height:100dvh`), z listą posiłków
  jako `Column(weight(1f))` i `DailyTargetBento` przeniesionym DO ŚRODKA
  (poprzednio osobny `item{}` niżej) tak żeby wylądował przypięty na dole
  wypełnionej kolumny — dokładnie ten sam wzorzec co web'owy flex:1. (3)
  Nowość na obu platformach: 7 kart dni tygodnia pod "Dzisiejszym Planerem"
  TEŻ dostało pełnoekranową wysokość (Web: `.clinic-day-card{min-height:
  calc(100dvh - 110px)}`; Android: `DayCardClinic`'s wywołanie w
  `itemsIndexed` dostało ten sam `Modifier.fillParentMaxHeight()`). (4)
  Nowość na obu platformach: pasek 7 dni na karcie "Dzisiejszy Planer"
  (dotąd czysto dekoracyjny, świadomie NIE przełączający który dzień widać
  w dashboardzie — patrz v7) można teraz kliknąć, żeby przewinąć do
  pełnoekranowej karty wybranego dnia, wyśrodkowanej na ekranie (Web:
  `chip.scrollIntoView({behavior:"smooth", block:"center"})`; Android:
  `LazyListState.animateScrollToItem(1 + di)` przez nowy `onDayJump`
  callback z `PlannerDashboard` do `PlannerScreen`).

  Przy okazji naprawiony osobny, REALNY błąd Androida zgłoszony w tej samej
  turze: podświetlenie karty w trakcie przesuwania (swipe, z v15) było
  całkowicie niewidoczne na prawdziwym urządzeniu. Przyczyna: Compose'owy
  `Card`/`Surface` zawsze maluje WŁASNE `containerColor` NAD tłem
  narysowanym przez jakikolwiek zewnętrzny modyfikator `.background()`
  przekazany w `modifier` — pierwsza wersja z v15 ustawiała
  `dragTint` właśnie tak (niewidocznie). Naprawione przez wtopienie
  `dragTint` w `colors = CardDefaults.cardColors(containerColor =
  dragTint.compositeOver(MaterialTheme.colorScheme.surface))` — jedyne
  miejsce, przez które faktycznie widoczne tło Card da się zmienić z
  zewnątrz. Zweryfikowane na emulatorze (`Medium_Phone_API_35`, świeży
  profil) realnym gestem `adb shell input touchscreen swipe` z pomiarem w
  trakcie przesuwania (zrzut ekranu w połowie gestu pokazał kartę
  przesuniętą + zielone podświetlenie; po puszczeniu — kcal/pierścień
  zaktualizowane, nazwa dania przekreślona). Kliknięcie paska dni
  potwierdzone jako scrollujące do właściwej, pełnoekranowej karty
  (zrzut ekranu po kliknięciu "Śr" pokazuje kartę Środy wypełniającą
  ekran). Web: wszystkie 4 punkty zmierzone `getBoundingClientRect`/
  `getComputedStyle` w lokalnym Chrome (nie tylko wizualnie); kliknięcie
  paska dni potwierdzone jako poprawnie wywołujące `scrollIntoView` z
  właściwym celem (przechwycone monkey-patchem `Element.prototype.
  scrollIntoView`) — sama animacja płynnego przewijania nie dała się
  zaobserwować w TEJ sesji, bo karta Chrome była w tle (`document.hidden
  === true`), a Chrome usypia animacje przewijania w niewidocznych kartach;
  nie jest to błąd aplikacji.

  CACHE_NAME→v101, `versions/v101/`. Android: `versionCode` 82→83,
  `versionName` 0.1.81→0.1.82, `./gradlew :app:assembleDebug :logic:test`
  przechodzą, `aapt dump badging` zweryfikowany przed kopią do `dist/`.
- **v17** (2026-08-25, Web + Android): Kolejna, duża runda próśb w jednej
  turze. (1) Android: ikony "+"/ustawień na Planerze przeniesione z
  osobnego rzędu NA wspólny wiersz z datą/powitaniem ("zrównaj dzień
  tygodnia/datę i Cześć... z plusikiem i kołem zębatym... w jednej linii")
  — `MainActivity.kt`'s `headerActions` lambda wyniesiona POZA `topBar`
  (żeby `PlannerScreen`'s `content` też mógł ją wywołać), `TopAppBar`
  całkiem pominięty dla Kliniki na trasie Planera (renderowany normalnie
  na pozostałych 11 motywach I na pozostałych zakładkach Kliniki — tam
  nie ma z czym go scalić), `PlannerDashboard`'s data/greeting `Column`
  zamieniona na `Row` z `headerActions()` po prawej. (2) Web + Android:
  kafelek kcal-pierścień/"Pozostało" i kafelek "Woda" powiększone ×1.5
  (padding/czcionki/rozmiar pierścienia) — web: `.pd-card`/`.pd-ring-wrap`/
  itd.; Android: `Box(size=72.dp)` (było 48dp), `strokeWidth=4.5.dp` (było
  3dp), analogiczne czcionki jawne zamiast `MaterialTheme.typography.*`
  (potrzebne większe wartości niż najbliższy gotowy styl). Mieści się w
  pełni na ekranie na obu platformach bez zmian w logice pełnoekranowego
  wypełnienia — elastyczna lista posiłków poniżej po prostu absorbuje
  różnicę, ten sam mechanizm co wcześniej. (3) Web + Android: podgląd
  przepisu z "Dzisiejszego Planera" ubogacony. Web: usunięty redundantny
  `<h3>🍽️ Podgląd przepisu</h3>`, nazwa przepisu (już w karcie, jako link)
  przejmuje rolę tytułu, oszczędzając rząd miejsca — `.modal-head` dla
  tego jednego modala dostał `justify-content:flex-end` inline (nie
  zmieniona wspólna klasa używana przez inne modale). Android: karta
  podglądu była "bardzo uboga" względem web/Przepisów — dodane: wynik
  dopasowania (`RecipeMatching.matchScore`, dane już dostępne przez
  `profile`+`macroTargets` — zero nowych ViewModeli), odznaka źródła
  (własny/społecznościowy), pełny wiersz makro (B/W/T/błonnik/IG/ŁG, port
  `RecipeCardBody`'s formatu), przycisk "Dodaj do listy zakupów"
  (`shoppingViewModel`, już parametr `PlannerScreen`). Świadomie NIE
  pełna `RecipeCard`/`RecipeCardBody` — gwiazdka ulubionych, sprawdzenie
  spiżarni, historia gotowania, oceny i komentarze wymagałyby wpięcia
  kilku dodatkowych ViewModeli (`pantryViewModel`, `recipeViewModel`,
  `recipeCommentsViewModel`, `favoriteIngredientsViewModel`) do
  `PlannerScreen`, którego dziś tam nie ma — świadomie odłożone jako
  osobna, większa runda (patrz `android/PARITY.md`), a nie cicho pominięte.
  (4) Android: karty przepisów na zakładce Przepisy były zauważalnie
  niższe niż web — przyczyna: `RecipeCardBody` w stanie zwiniętym nie
  renderowała ŻADNEGO odpowiednika web's zawsze-widocznego
  `.expand-toggle` przycisku ("Składniki i przygotowanie" + strzałka) —
  dodany brakujący wiersz (bez własnego `onClick`, cała karta ma już
  jeden nadrzędny `.clickable{onToggleExpanded()}`), przywracając
  brakującą wysokość I widoczną podpowiedź rozwijalności, której
  wcześniej zupełnie nie było. (5) Web + Android: ikona wody zmieniona z
  kubka/kufla (z uszkiem) na klasyczną kroplę ("zamiast kubeczków wody...
  zmień na kropelki w każdym możliwym temacie") — web: `cupIconSvg()`'s
  wewnętrzny SVG `<path>` (funkcja/klasy CSS NIE przemianowane, czysto
  kosmetyczna zmiana kształtu); Android: `WaterCupIcon`'s `Canvas`
  rysowanie zmienione z prostokąta+uszka (`drawArc`) na łzowaty kształt
  (dwie krzywe Beziera). Świadomie NIE dotyczy kółek wyboru wody w
  motywie Klinika (`.clinic-water-circle`/Compose `CircleShape` — to nie
  są "kubeczki" w dosłownym sensie, inny, już abstrakcyjny kształt).

  Zweryfikowane na emulatorze (`Medium_Phone_API_35`, świeży profil,
  `pm clear`) zrzutami ekranu po każdej zmianie: ikony w jednym rzędzie z
  datą/powitaniem, wyraźnie większy pierścień/kafelek wody, wzbogacony
  podgląd przepisu (wynik dopasowania 89%, pełny wiersz makro, przycisk
  zakupów) na dwóch różnych przepisach, wyższe karty na Przepisach z
  widocznym wierszem "Składniki i przygotowanie". Web zweryfikowany w
  lokalnym Chrome: powiększony pierścień (52px→78px potwierdzone
  `getBoundingClientRect`), zniknięcie nagłówka modala (zrzut ekranu).
  `versionCode` 83→84, `versionName` 0.1.82→0.1.83. CACHE_NAME→v102,
  `versions/v102/`.

# FR-88: Planer jako pierwsza zakładka nawigacji

**Obszar:** Nagłówek i nawigacja, Android + Web
**Status:** Zaimplementowane na obu platformach

## Opis
Dolny pasek nawigacji (Przepisy, Zakupy, Planer, Postęp, Spiżarnia) zmienia
kolejność na: **Planer, Przepisy, Zakupy, Postęp, Spiżarnia** — Planer staje
się pierwszą zakładką i jednocześnie domyślnym ekranem otwieranym zaraz po
uruchomieniu aplikacji, zamiast Przepisów. Zmiana dotyczy WSZYSTKICH 13
motywów kolorystycznych jednakowo — to zmiana kolejności/domyślnego
ekranu współdzielonej infrastruktury nawigacji, nie osobny wygląd per
motyw (w odróżnieniu od FR-87, który opisuje bespoke wygląd samej
zawartości Planera wyłącznie dla motywu Klinika/Klinika (noc)).

Powód: użytkownik chce, żeby pierwsze co widać po otwarciu aplikacji to
plan dnia (ile zjedzone/ile zostało, co zaplanowane na dziś), a nie lista
przepisów.

## Kryteria akceptacji
- Dolny pasek nawigacji pokazuje zakładki w kolejności: Planer, Przepisy,
  Zakupy, Postęp, Spiżarnia — identycznie na Android i Web, we wszystkich
  13 motywach.
- Po uruchomieniu aplikacji (świeże wejście, nie powrót z tła) domyślnie
  aktywny jest Planer, nie Przepisy.
- Powrót przyciskiem „Wstecz”/gestem systemowym z Planera (jako ekranu
  startowego) zachowuje się tak jak dotychczas zachowywał się dla
  Przepisów jako ekranu startowego (FR-46) — nie zamyka aplikacji
  przypadkowo.
- Sama zawartość/wygląd Planera dla 11 motywów innych niż Klinika jest
  bajt-w-bajt identyczna jak przed tą zmianą — zmienia się wyłącznie
  KOLEJNOŚĆ, w jakiej się do niej trafia.

## Historia rewizji
- **v1** (2026-08-23, Web + Android): Pierwsza wersja, na wyraźną prośbę
  użytkownika przy okazji przeprojektowania dashboardu Plannera dla
  Kliniki (FR-87). Web: `index.html` — przycisk `data-view="planner"`
  przeniesiony na początek `nav.bottom`, sekcja `#view-planner` domyślnie
  `active` zamiast `#view-recipes`, trzy twardo wpisane fallbacki
  `"recipes"` w routingu historii (`switchView`/`popstate`/initial state)
  zmienione na `"planner"`. Android: `Screen.Planner` przeniesiony na
  początek `BOTTOM_NAV_SCREENS` (`ui/navigation/Screen.kt`) —
  napędza jednocześnie zwykły `NavigationBar` i Klinikowy
  `FloatingBottomNav`; `startDestination` w `MainActivity.kt` zmieniony
  na `Screen.Planner.route`.

- **v19** (2026-08-29): Karta dzisiejszego dnia na LIŚCIE dni tygodnia
  przestała być pełnoekranowa — jest teraz dokładnie takiej wielkości jak
  pozostałe sześć. Zgłoszenie: „na stronie głównej planer na samej górze
  dzisiejszy dzień ładnie pokazuje się na całym ekranie a niżej w dniach
  tygodnia nie ma potrzeby żeby dzisiejszy dzień też był rozciągnięty na
  całą stronę, zrób go takiego jak pozostałe dni”. Sekcja „Dzisiejszy
  Planer” na górze widoku (`#plannerTodayWrap` / `PlannerDashboard`) i tak
  jest już pełnoekranowym widokiem dzisiaj, więc druga pełnoekranowa karta
  niżej oznaczała przewijanie przez ten sam dzień dwa razy. Usunięte:
  reguła `.clinic-day-card.today{min-height:calc(100dvh - 110px)}` (web) i
  `Modifier.fillParentMaxHeight()` dla `day == todayIndex` (Android).
  Dzisiejszy dzień nadal jest wyróżniony (obwódka w kolorze akcentu na
  webie, plakietka „Dziś” + mocniejszy cień na Androidzie), skrót
  „kliknij dzień na pasku” działa bez zmian. Zweryfikowane wizualnie na
  emulatorze (karta „Sobota” z plakietką „Dziś” tej samej wysokości co
  „Niedziela”, obie widoczne na jednym ekranie).

- **v20** (2026-08-29, Android only — REALNY BUG UKŁADU): włączenie opcji
  „Wypełniaj kolorem w miarę zjadania posiłków” (FR-88) rozciągało kafelek
  „POZOSTAŁO” na całą wysokość ekranu, spychając z niego całą listę
  posiłków. Zgłoszenie ze zrzutem ekranu: „w Android jak się włączy opcje
  żeby kafelek pozostało się kolorował to rozciąga go niepotrzebnie na
  całą stronę”. Przyczyna: pasek wypełnienia był prawdziwym dzieckiem
  `Box` z `Modifier.fillMaxHeight()`, a ten kafelek siedzi wewnątrz
  `PlannerDashboard`, któremu kolumna nadrzędna daje
  `fillParentMaxHeight()` — ograniczenie wysokości docierające do dziecka
  to więc CAŁY pozostały ekran, o który dziecko grzecznie poprosiło.
  Naprawione: wypełnienie rysowane przez `Modifier.drawBehind { drawRect(...) }`
  (maluje po płaskim tle, pod tekstem) zamiast układem — nie bierze
  udziału w mierzeniu, więc kafelek ma dokładnie ten sam rozmiar co przy
  opcji wyłączonej. Wersja webowa nie miała tego błędu: tam
  `.pd-remaining-fill` jest `position:absolute`, czyli też poza układem.
  Zweryfikowane na emulatorze (opcja włączona, 345/1480 kcal → jaśniejszy
  pas na ~23% szerokości kafelka, wysokość bez zmian).

---

# FR-89: Reset wszystkich danych na koncie

**Obszar:** Konto i chmura
**Status:** Zaimplementowane

## Opis
Karta „☁️ Konto w chmurze” w Ustawieniach ma, dla zalogowanego (nie
anonimowego) konta, przycisk „🗑️ Resetuj wszystkie dane na koncie” — obok
istniejącego przycisku wylogowania (FR-79).

W odróżnieniu od FR-79's „wyloguj + wyczyść dane lokalne” (które NIE dotyka
dokumentu w Firestore — stare dane w chmurze wracają przy następnym
zalogowaniu na to samo konto na jakimkolwiek urządzeniu), ten przycisk:

- **nie wylogowuje** — użytkownik zostaje zalogowany na to samo konto,
- czyści lokalny stan tego urządzenia do świeżych wartości domyślnych
  (tak jak przy zupełnie nowej instalacji),
- **nadpisuje w całości** (nie scala) dokument `users/{uid}` w Firestore
  świeżymi domyślnymi wartościami wszystkich synchronizowanych pól
  (profil, spiżarnia, lista zakupów, planer, ulubione, własne przepisy,
  oceny, historia gotowania, motyw, skala interfejsu, ustawienia itd.).

Po zresetowaniu każde inne urządzenie zalogowane na to samo konto zobaczy
świeży, pusty stan po najbliższej synchronizacji — nie tylko urządzenie,
z którego wykonano reset.

Kliknięcie przycisku otwiera okienko potwierdzenia z jasnym ostrzeżeniem,
że operacja jest nieodwracalna. Przycisk potwierdzenia w tym okienku jest
zablokowany przez 5 sekund (widoczne odliczanie), żeby nie dało się
kliknąć go od razu przez przypadek zaraz po otwarciu okienka.

## Kryteria akceptacji
- Przycisk widoczny wyłącznie, gdy użytkownik jest zalogowany na prawdziwe
  konto (Google lub e-mail) — nie pojawia się przy logowaniu wyłącznie
  anonimowym, tak samo jak przycisk wylogowania (FR-79).
- Okienko potwierdzenia jasno mówi, że operacja jest nieodwracalna i
  dotyczy WSZYSTKICH danych konta (nie tylko tego urządzenia).
- Przycisk potwierdzenia w okienku jest wyłączony (disabled) i pokazuje
  odliczanie („Poczekaj (5)…” → … → „Poczekaj (1)…”) przez dokładnie 5
  sekund od otwarcia okienka, dopiero potem staje się klikalny.
- Po potwierdzeniu: lokalny stan urządzenia wraca do domyślnych wartości
  (profil nieskonfigurowany jak przy FR-72, spiżarnia/lista/planer/
  ulubione puste, motyw domyślny itd.) I dokument `users/{uid}` w
  Firestore zostaje w całości zastąpiony (nie scalony) tymi samymi
  domyślnymi wartościami.
- Użytkownik pozostaje zalogowany na to samo konto po operacji — to NIE
  jest wylogowanie.
- Anulowanie okienka (przycisk „Anuluj” albo zamknięcie w inny sposób)
  nie zmienia niczego.

## Uwagi
Dokument `users/{uid}` obejmuje pola zarządzane WYŁĄCZNIE przez web
(`myRecipes`, `customTiles`, `recipeReviews`, `pantryUnitOverride`,
`pantryCategoryOverride`, `pantryStepOverride`, `recipeAdded`,
`waterNotifEnabled`, `waterReminder`, `household`), dla których Android
nie ma własnego modelu domenowego/ViewModelu. Żeby reset wykonany z
Androida był PRAWDZIWIE pełnym resetem konta (a nie tylko „wszystkiego,
co Android akurat śledzi”), port Android dodatkowo nadpisuje te konkretne
pola bezpośrednio przez Firestore (`update()` na tych ścieżkach), z
dokładnie tymi samymi domyślnymi kształtami co świeży stan web'a
(`loadState()`'s fallback object w `index.html`) — patrz
`android/PARITY.md` po pełny opis.

## Historia rewizji
- **v1** (2026-08-24): Pierwsza wersja wymagania, na życzenie użytkownika
  ("chciałbym też żeby była możliwość zresetowania danych na koncie, dodaj
  nowy przycisk resetuj wszystkie dane użytkownika z okienkiem
  potwierdzenia i koniecznością poczekania 5 sekund zanim wyczyści") —
  zgłoszone bezpośrednio po serii incydentów utraty/rozjazdu danych między
  Web a Androidem tego samego dnia (patrz FR-73/v4-v5), jako sposób na
  prosty "świeży start" na koncie zamiast ręcznego czyszczenia dokumentu
  w konsoli Firebase.
- **v2** (2026-08-24, Android): Użytkownik przetestował przycisk na żywo
  (drugie fizyczne urządzenie) i zgłosił, że mimo resetu formularz
  profilu dalej pokazywał "Kobieta" zaznaczoną oraz przykładowe liczby —
  reset sprawiał wrażenie niepełnego. Przyczyna: `resetAccountData` w
  `MainActivity.kt` wywoływał `profileViewModel.resetToDefault()`, ta sama
  metoda co istniejący przycisk "Domyślne" WEWNĄTRZ formularza profilu —
  a ta metoda CELOWO ustawia `configured: true` (zgodnie z FR-72's
  Kryterium akceptacji dla TEGO konkretnego przycisku: "reset nie ma
  cofać użytkownika do stanu pierwsze uruchomienie"). Dla pełnego resetu
  konta to zachowanie jest odwrotne od zamierzonego — reset konta MA
  cofnąć do stanu pierwszego uruchomienia. Naprawione: nowa metoda
  `ProfileViewModel.resetToUnconfigured()` (`_profile.value = Profile()`,
  czyli `configured: false` z domyślnego konstruktora), użyta zamiast
  `resetToDefault()` wyłącznie w `resetAccountData`. Przy okazji naprawiono
  też głębszą, ogólniejszą lukę w samym FR-72 — patrz FR-72's v2: pola
  płci/aktywności/celu od zawsze pokazywały domyślnie zaznaczoną opcję
  niezależnie od `configured`, w odróżnieniu od pól liczbowych.
  `versionCode` 77→78, `versionName` 0.1.76→0.1.77. Odpowiadająca poprawka
  na webie: puste placeholdery w `setSex`/`setActivity`/`setGoal` (patrz
  FR-72's v2), `dieta-app-v93`. `./gradlew :logic:test :app:assembleDebug`
  przechodzi.

---

# FR-90: Kopiowanie planu jednego dnia na inny dzień

**Obszar:** Planer, Android + Web
**Status:** Zaimplementowane na obu platformach (kierunek „na inne dni” — v2 — na razie Web-only, patrz Uwagi)

## Opis
Na każdej karcie dnia w Planerze (obok istniejących „🎲 Losuj ten dzień” /
„🗑️ Wyczyść ten dzień”) jest przycisk „📋 Kopiuj plan z innego dnia”.
Otwiera picker z listą pozostałych 6 dni tygodnia — dni bez żadnego
zaplanowanego posiłku są wyszarzone/nieklikalne. Wybranie dnia źródłowego
NADPISUJE cały plan dnia docelowego (wszystkie 5 kategorii posiłków,
razem ze skalą porcji i flagą „resztki”) planem z wybranego dnia.

Web dostał też (v2) przeciwny kierunek: „📤 Kopiuj ten dzień na inne dni” —
stojąc na dniu, którego plan już Ci odpowiada, zaznaczasz checkboxami
dowolną liczbę innych dni i kopiujesz go na wszystkie naraz, jednym
potwierdzeniem, zamiast osobno otwierać picker dla każdego dnia
docelowego. To dokładniej odpowiada oryginalnej motywacji tej funkcji
(patrz niżej) niż sam v1.

Dodane po badaniu, czego najczęściej brakuje w aplikacjach dietetycznych —
powtarzalny tygodniowy plan (np. te same śniadania w pon-pt) to częsta
skarga na ręczne, powtarzalne klikanie tych samych dań.

## Kryteria akceptacji
- Przycisk „📋 Kopiuj plan z innego dnia” widoczny na każdej karcie dnia,
  niezależnie od motywu.
- Picker pokazuje 6 pozostałych dni tygodnia; dzień bez żadnego
  zaplanowanego posiłku jest wyłączony (nieklikalny).
- Wybranie dnia źródłowego kopiuje WSZYSTKIE 5 kategorii posiłków (razem
  ze skalą porcji i flagą resztek) do dnia docelowego, nadpisując to, co
  tam wcześniej było.
- Operacja jest natychmiastowa (bez osobnego potwierdzenia — to samo
  zachowanie co „🎲 Losuj ten dzień” dla pojedynczego dnia).
- Web (v2): przycisk „📤 Kopiuj ten dzień na inne dni” wyłączony/wyszarzony,
  gdy dzień, na którym stoisz, jest pusty (nie ma czego kopiować).
- Web (v2): modal pozwala zaznaczyć dowolną liczbę pozostałych 6 dni
  (checkboxy); potwierdzenie kopiuje plan źródłowego dnia na WSZYSTKIE
  zaznaczone dni naraz, z podsumowującym toastem wymieniającym nazwy dni.
- Web (v2): próba potwierdzenia bez zaznaczonego żadnego dnia pokazuje
  komunikat „Zaznacz co najmniej jeden dzień” i nie zamyka modala.
- `./gradlew :logic:test :app:compileDebugKotlin` przechodzi.

## Uwagi
Android: `PlannerOperations.copyDay(plan, fromDay, toDay)` (`:logic`,
testowalne jednostkowo) — trywialne, bo `PlannedMeal` już łączy
recipeId+scale+isLeftover w jedną wartość na slot. Web: `openCopyDayModal()`
używa `structuredClone()` do skopiowania TRZECH równoległych map
(`state.planner`/`plannerScale`/`plannerLeftover`) na raz, bo web (w
odróżnieniu od Androida) trzyma te trzy pola osobno zamiast w jednym
obiekcie. `openCopySpreadModal()` (v2) robi to samo w pętli po zaznaczonych
dniach.

**v2 (kierunek „na inne dni”) świadomie Web-only na razie** — port do
Androida wymagałby nietrywialnej zmiany UI w Compose (stan multi-select
zamiast serii prostych `onClick`), a ta sesja pracuje w środowisku bez
dostępu do `api.foojay.io` (toolchain JDK dla Gradle, potwierdzone błędem
403 przy `:app:compileDebugKotlin` — patrz FR-95/v2's ten sam problem),
więc nie da się jej tu skompilować ani przetestować jednostkowo. Zamiast
piętrzyć kolejny niezweryfikowany krok w Kotlinie bez szansy na sprawdzenie
w tej samej turze (zasada z CLAUDE.md), odłożone do sesji z realnym
dostępem do Gradle/emulatora — odnotowane w `android/PARITY.md`.

## Historia rewizji
- **v1** (2026-08-26): Pierwsza wersja, część większej nocnej rundy „co
  najmniej 5 nowych funkcji” (użytkownik: „zrób to od razu w razie czego
  będziemy cofać zmiany”). Zweryfikowane kompilacją i testami
  jednostkowymi (`:logic:test`, `:app:compileDebugKotlin`) oraz składniowo
  na webie (`node --check` na wyekstrahowanym JS) — **nie zweryfikowane
  wizualnie/interaktywnie w przeglądarce ani na emulatorze w tej turze**.
- **v2** (2026-08-28, Web only): Dodany przeciwny kierunek kopiowania —
  „📤 Kopiuj ten dzień na inne dni” — na wyraźną prośbę użytkownika o
  dalszą rozbudowę funkcji Plannera. Nowy modal `copySpreadOverlay` z
  checkboxami (zamiast pojedynczych przycisków-do-natychmiastowego-kopiowania
  jak w v1, bo tu trzeba jawnie potwierdzić wybór wielu celów naraz).
  Zweryfikowane na żywo (headless Chromium, w pełni offline): przycisk
  poprawnie wyłączony dla pustego dnia źródłowego, kopiowanie na 2
  zaznaczone dni potwierdzone przez odczyt `state.planner` po operacji
  (trzeci, niezaznaczony dzień pozostał nietknięty), pusty wybór poprawnie
  blokowany komunikatem zamiast cichego no-opa. CACHE_NAME→v105,
  `versions/v105/`. Android: bez zmian w tej turze, patrz Uwagi.

---

# FR-91: Cofnij (Undo) usunięcie dania z „Dzisiejszy Planer”

**Obszar:** Planer (motyw Klinika), Android + Web
**Status:** Zaimplementowane na obu platformach

## Opis
Usunięcie dania z sekcji „Dzisiejszy Planer” (×/przycisk usuwania przy
danej kategorii) pokazuje teraz powiadomienie z akcją „Cofnij”, zamiast
usuwać bezpowrotnie od razu. Kliknięcie „Cofnij” przywraca DOKŁADNIE ten
sam wpis (przepis, skalę porcji, flagę „resztki”), który został usunięty.

Android: pierwsze użycie komponentu Snackbar w tej aplikacji —
`SnackbarHostState` podpięty na poziomie `Scaffold`, generyczny callback
`onShowUndoSnackbar(message, actionLabel, onUndo)` przewleczony do
`PlannerScreen`.

Web: rozszerzona funkcja `toast(msg, undoLabel, onUndo)` — opcjonalny
przycisk „Cofnij” w powiadomieniu, dłuższy czas widoczności (5s zamiast
1.8s) gdy akcja cofnięcia jest dostępna.

## Kryteria akceptacji
- Usunięcie dania z „Dzisiejszy Planer” pokazuje powiadomienie z
  przyciskiem „Cofnij”.
- Kliknięcie „Cofnij” przywraca dokładnie ten sam przepis, tę samą skalę
  porcji i tę samą flagę „resztki”, jakie miał usunięty wpis — nie tylko
  sam przepis z domyślną skalą 1×.
- Zignorowanie powiadomienia (bez kliknięcia „Cofnij”) pozostawia danie
  usunięte, tak jak dotychczas.
- `./gradlew :logic:test :app:compileDebugKotlin` przechodzi.

## Uwagi
Podczas implementacji na Androidzie znaleziona i naprawiona realna pułapka
(przed jakimkolwiek błędem kompilacji/testu): naiwna implementacja
przywracania przez `setMeal(...)` + osobne `planLeftover(...)` cichcem
gubiła skalę porcji, bo `planLeftover()` zawsze na sztywno ustawia
`scale=1.0`. Naprawione nową, atomową metodą `PlannerViewModel.restoreMeal
(day, cat, meal: PlannedMeal)`, zapisującą wszystkie trzy pola naraz z
jednego przechwyconego obiektu. Web'owa implementacja sprawdzona i NIE ma
tego błędu — trzy równoległe mapy (`planner`/`plannerScale`/
`plannerLeftover`) są tam ustawiane niezależnie, bez ryzyka nadpisania.

## Historia rewizji
- **v1** (2026-08-26): Pierwsza wersja, część większej nocnej rundy „co
  najmniej 5 nowych funkcji”. Zweryfikowane kompilacją i testami
  jednostkowymi oraz składniowo na webie. **Nie zweryfikowane
  wizualnie/interaktywnie** (w tym samo pojawienie się/zachowanie
  Snackbara na Androidzie) w tej turze.

---

# FR-92: Udostępnianie / eksport planu tygodnia

**Obszar:** Planer, Android + Web
**Status:** Zaimplementowane na obu platformach

## Opis
Karta Planer ma teraz przyciski do udostępnienia całego zaplanowanego
tygodnia jako zwykły tekst (dzień → kategoria → nazwa dania, ze skalą
porcji jeśli inna niż 1×) — na wzór już istniejącego udostępniania listy
zakupów (FR-26).

Web: trzy przyciski — „📤 Udostępnij plan tygodnia” (natywny arkusz
udostępniania systemu przez `navigator.share`, czyli Messenger/Signal/
SMS/e-mail/cokolwiek użytkownik ma na telefonie; na desktopie i wszędzie
tam, gdzie API nie jest dostępne, kopiuje do schowka), „🟢 WhatsApp”
(otwiera `wa.me` z gotowym tekstem) i „📋 Kopiuj plan tygodnia” (schowek).

Android: „📤 Udostępnij plan” (natywny arkusz udostępniania,
`Intent.ACTION_SEND`, ten sam wzorzec co istniejący przycisk „Udostępnij”
na Zakupach) i „📋 Kopiuj” (schowek systemowy).

Dodane po badaniu, czego najczęściej brakuje w aplikacjach dietetycznych —
przesłanie tygodniowego planu osobie robiącej zakupy/gotującej to częsta
potrzeba, dotąd niemożliwa bez ręcznego przepisywania.

## Kryteria akceptacji
- Pusty tydzień (bez żadnego zaplanowanego posiłku) daje czytelny komunikat
  zamiast pustego/mylącego tekstu.
- Wygenerowany tekst zawiera każdy dzień z co najmniej jednym zaplanowanym
  posiłkiem, z ikoną kategorii, nazwą dania i skalą porcji (jeśli ≠ 1×).
- Dni bez żadnego zaplanowanego posiłku są pomijane w wygenerowanym tekście.
- Kopiowanie do schowka pokazuje potwierdzenie (toast/Toast).
- Web (v2): „📤 Udostępnij plan tygodnia” otwiera natywny arkusz
  udostępniania, jeśli przeglądarka wspiera `navigator.share`.
- Web (v2): jeśli `navigator.share` nie jest dostępne, ten sam przycisk
  kopiuje plan do schowka i pokazuje potwierdzenie — nigdy nie jest martwy.
- Web (v2): anulowanie arkusza udostępniania przez użytkownika nie pokazuje
  żadnego błędu.
- `./gradlew :logic:test :app:compileDebugKotlin` przechodzi.

## Historia rewizji
- **v1** (2026-08-26): Pierwsza wersja, część większej nocnej rundy „co
  najmniej 5 nowych funkcji”. `PlannerOperations.buildWeekPlanText()`
  (Android, `:logic`) i `buildWeekPlanText()` (web) generują identyczny
  kształt tekstu. Zweryfikowane kompilacją i testami jednostkowymi oraz
  składniowo na webie. **Nie zweryfikowane wizualnie/interaktywnie**
  (w tym rzeczywiste otwarcie WhatsApp/arkusza udostępniania) w tej turze.
- **v2** (2026-08-28, Web only): Web dostał natywny arkusz udostępniania
  (`navigator.share`), którego Android miał od v1 (`Intent.ACTION_SEND`) —
  czyli **zamknięcie realnej luki w parytecie, przeoczonej przy v1**:
  webowa wersja oferowała tylko WhatsApp i schowek, więc wysłanie planu
  przez Messenger, Signal, SMS czy e-mail wymagało ręcznego wklejania,
  mimo że przeglądarki na telefonach udostępniają dokładnie ten sam
  systemowy arkusz co Android natywnie. Przy okazji refaktoring: logika
  „udostępnij natywnie albo skopiuj do schowka” wyjęta z handlera przycisku
  listy zakupów (jedyne miejsce, gdzie na webie istniała) do współdzielonej
  `shareOrCopyText(title, text, fallbackMsg)`, używanej teraz przez oba
  ekrany — zamiast kopiować ten sam `if(navigator.share)` po raz drugi.
  Zweryfikowane na żywo (headless Chromium) OBIE ścieżki osobno: z
  podstawionym `navigator.share` (arkusz dostaje poprawny tytuł i tekst,
  schowek nietknięty) i z usuniętym `navigator.share` (fallback kopiuje do
  schowka i pokazuje potwierdzenie). CACHE_NAME→v107, `versions/v107/`.

---

# FR-93: Podpowiedzi zamienników składników w spiżarni

**Obszar:** Sprawdzanie spiżarni przy przepisie, Android + Web
**Status:** Zaimplementowane na obu platformach

## Opis
W oknie „sprawdź co masz” (otwieranym z karty przepisu) — dla każdego
składnika, którego NIE ma w spiżarni — pokazuje się teraz dyskretna
podpowiedź: co z TEJ SAMEJ kategorii składników (`CANON_INFO.cat`, np.
„Nabiał”, „Warzywa”) już jest w spiżarni, jako potencjalny zamiennik
(np. brakuje „śmietana”, ale jest „jogurt naturalny” — oba to „Nabiał”).

Świadomie NIE ma stałej tabeli zamienników 1:1 (np. „masło → margaryna”)
— istniejące grupowanie kategorii już daje użyteczne, sensowne
podpowiedzi bez ryzyka niedopasowanych/nieistniejących kluczy kanonicznych
między platformami, i automatycznie uwzględnia to, co użytkownik REALNIE
ma w spiżarni, a nie sztywną, generyczną listę.

## Kryteria akceptacji
- Składnik obecny w spiżarni: bez zmian (zwykły status „🏺 …”).
- Składnik nieobecny w spiżarni, gdy w spiżarni jest coś z tej samej
  kategorii: pokazuje się podpowiedź „🔁 Masz w spiżarni (ta sama
  kategoria): …” z maks. 3 nazwami.
- Składnik nieobecny w spiżarni, gdy w spiżarni NIE MA nic z tej samej
  kategorii: bez podpowiedzi (bez zmian względem stanu sprzed tej funkcji).
- Podpowiedź aktualizuje się na żywo po dodaniu/usunięciu pozycji w
  spiżarni bez zamykania okna.
- `./gradlew :logic:test :app:compileDebugKotlin` przechodzi.

## Historia rewizji
- **v1** (2026-08-26): Pierwsza wersja, część większej nocnej rundy „co
  najmniej 5 nowych funkcji”. Zweryfikowane kompilacją i testami
  jednostkowymi oraz składniowo na webie. **Nie zweryfikowane
  wizualnie/interaktywnie** w tej turze.

---

# FR-94: Śledzenie okna postu przerywanego (intermittent fasting)

**Obszar:** Ustawienia + ekran „Dziś”, Android + Web
**Status:** Zaimplementowane na obu platformach (lokalnie, bez synchronizacji w chmurze na Androidzie — patrz Uwagi)

## Opis
Nowa, domyślnie WYŁĄCZONA opcja w Ustawieniach: śledzenie okna postu
przerywanego (np. klasyczny 16:8). Użytkownik ustawia godziny „okna
jedzenia” (np. od 12:00 do 20:00, jako liczby godzin 0-23) — wszystko poza
tym oknem to „okno postu”.

Po włączeniu, na ekranie „Dziś” (pod podsumowaniem zjedzonych kalorii)
pojawia się status: „🍽️ Okno jedzenia — post zacznie się o HH:00” albo
„⏳ Okno postu — jedzenie od HH:00”, w zależności od aktualnej godziny.
Okno może „zawijać się” przez północ (np. 20-4).

Dodane po badaniu, czego najczęściej brakuje w aplikacjach dietetycznych —
post przerywany / time-restricted eating to jedna z najczęściej
wymienianych, brakujących funkcji.

## Kryteria akceptacji
- Wyłączone (domyślnie): brak jakiegokolwiek statusu na ekranie „Dziś”.
- Włączone, aktualna godzina w oknie jedzenia: status „🍽️ Okno jedzenia —
  post zacznie się o HH:00”.
- Włączone, aktualna godzina poza oknem jedzenia: status „⏳ Okno postu —
  jedzenie od HH:00”.
- Godzina początku ≥ godzina końca traktowana jako okno zawijające się
  przez północ (np. 20-4 = je się 20:00-04:00).
- Zmiana godzin w Ustawieniach natychmiast aktualizuje status na „Dziś”.
- Web (v5): osobny, domyślnie WYŁĄCZONY przełącznik „Powiadamiaj o otwarciu
  i zamknięciu okna jedzenia” — włączenie śledzenia samo w sobie nie
  zaczyna niczego wysyłać.
- Web (v5): włączenie przełącznika bez zgody na powiadomienia prosi o nią,
  a przy odmowie cofa przełącznik i mówi wprost, że opcja nie zadziała.
- Web (v5): dokładnie dwa powiadomienia na dobę — jedno przy otwarciu, jedno
  przy zamknięciu okna; ta sama granica nigdy nie jest ogłaszana dwa razy.
- Web (v5): powiadomienie wysyłane jest tylko w ciągu 10 minut od granicy;
  później (telefon był wyłączony, karta spała) jest pomijane, zamiast
  ogłaszać z opóźnieniem zdarzenie sprzed godzin.
- Web (v5): zmiana godzin okna kasuje pamięć „już ogłoszone dzisiaj”, więc
  nowa granica może zostać ogłoszona tego samego dnia.
- `./gradlew :logic:test :app:compileDebugKotlin` przechodzi.

## Uwagi
Web: status renderuje się w dwóch miejscach zależnie od motywu — dla 11
„zwykłych” motywów do `#fastingStatus` (wewnątrz nagłówka, `renderFastingStatus()`),
dla Klinika/Klinika (noc) do własnego dashboardu (`renderPlannerDashboard()`,
pod „Cześć, {imię}!”) — patrz **v3** niżej, dlaczego to rozdzielenie jest
konieczne. Obie ścieżki liczą status tą samą funkcją `computeFastingStatus()`,
żeby nie utrzymywać dwóch kopii tej samej logiki zawijania przez północ.

Świadoma decyzja o zakresie: na Androidzie to ustawienie jest
LOKALNE-TYLKO (`FastingViewModel` + `LocalPersistenceCoordinator`, NIE
`CloudSyncCoordinator`) — ten sam wzorzec co `RemainingKcalFillViewModel`
(FR-96): preferencja wyświetlania/harmonogramu, nie dane warte
trójstronnego scalania między urządzeniami. Na webie jest częścią
`SYNCED_STATE_KEYS` (synchronizowane), bo tam koszt dodania jednego
kolejnego pola do już istniejącego mechanizmu synchronizacji jest
znikomy — asymetria świadoma, nie przeoczenie, patrz `android/PARITY.md`.

Logika okna (`isInEatingWindow`) współdzielona z już istniejącym
mechanizmem przypomnienia o piciu wody (`WaterReminderScheduling.
isActiveMinute`, Android) — ten sam algorytm zawijania przez północ, nie
duplikat.

## Historia rewizji
- **v1** (2026-08-26): Pierwsza wersja, część większej nocnej rundy „co
  najmniej 5 nowych funkcji”. Zweryfikowane kompilacją i testami
  jednostkowymi oraz składniowo na webie. **Nie zweryfikowane
  wizualnie/interaktywnie** w tej turze.
- **v2** (2026-08-26): Naprawiony realny błąd Androida znaleziony podczas
  wizualnej weryfikacji na emulatorze: status postu był podpięty WYŁĄCZNIE
  do `MainActivity.kt`'s `HeaderKcalPanel`, które renderuje się tylko
  `if (!isClinicHeader)` — a Klinika jest domyślnym motywem aplikacji od
  FR-87/v10, więc status nigdy się nie pokazywał w praktyce (na żadnym
  urządzeniu z domyślnymi ustawieniami). Naprawione tym samym wzorcem co
  FR-96 (który już poprawnie dostał tę samą Klinika-specyficzną ścieżkę):
  `fastingEnabled`/`fastingWindowStart`/`fastingWindowEnd` doprowadzone
  przez `PlannerScreen` do `PlannerDashboard`, status renderowany pod
  „Cześć, {imię}!” tak jak opisano w Kryteriach akceptacji. Zweryfikowane
  WIZUALNIE na emulatorze (`Medium_Phone_API_35`) — status „⏳ Okno postu —
  jedzenie od 12:00” faktycznie widoczny na ekranie Planer po włączeniu
  ustawienia. `./gradlew :logic:test :app:assembleDebug` przechodzą.
  `versionCode` 84→85, `versionName` 0.1.83→0.1.84.
- **v3** (2026-08-28): Naprawiony ten sam, dokładnie analogiczny błąd na
  Webie — nigdy nie odkryty wcześniej, bo v1 zweryfikowano tylko kompilacją
  i składniowo, nie interaktywnie. Znaleziony podczas sesji uruchamiającej
  `index.html` na żywo (headless Chromium, w pełni offline) w celu
  sprawdzenia 8 funkcji z nocnej rundy FR-90–97: status renderował się
  poprawnie do `#fastingStatus`, ale ten element leży wewnątrz
  `.header-collapsible`, którą CSS Klinika/Klinika (noc) chowa całkowicie
  (`display:none`) — a Klinika jest domyślnym motywem webowej aplikacji od
  FR-87/v8, więc status nigdy nie był widoczny w praktyce na domyślnych
  ustawieniach, tak samo jak wcześniej na Androidzie przed v2. Naprawione
  tym samym wzorcem: logika wydzielona do współdzielonej
  `computeFastingStatus()`, wywołanej też w `renderPlannerDashboard()`
  (Klinika), status renderowany pod „Cześć, {imię}!” — plus nowy,
  osobny styl `.pd-fasting-status` (żeby kolory pasowały do jasnej karty
  Klinika i jej wariantów Ocean/Terakota, zamiast reużywać biało-na-
  -przezroczystym `.fasting-status` dobrane pod ciemny nagłówek pozostałych
  11 motywów). Oba listenery zmiany ustawień (`setFastingEnabled`,
  `setFastingStart`/`setFastingEnd`) dostały dodatkowe wywołanie
  `renderPlannerDashboard()` obok istniejącego `renderFastingStatus()`, tak
  by zmiana natychmiast aktualizowała status też w Klinice. Zweryfikowane
  na żywo (nie tylko składniowo): oba stany (okno jedzenia/okno postu)
  sprawdzone zrzutem ekranu na domyślnym motywie Klinika, regresja
  sprawdzona na motywie nie-Klinika (status nadal w starym miejscu, bez
  zmian). `node -e "new Function(...)"` na obu blokach `<script>` przechodzi.
  CACHE_NAME→v104, `versions/v104/`. Android: bez zmian (już naprawione w v2).
- **v4** (2026-08-28, Web only): Status odświeża się teraz sam z upływem
  czasu. Wcześniej był przeliczany wyłącznie przy okazji jakiegoś innego
  renderowania, więc aplikacja zostawiona otwarta potrafiła długo po
  zamknięciu okna jedzenia dalej twierdzić „🍽️ Okno jedzenia” — czyli
  funkcja, której cała wartość polega na pokazywaniu AKTUALNEGO stanu,
  pokazywała stan nieaktualny. Dodany wspólny „tik zegara” (co 60 s), który
  aktualizuje tekst i styl statusu **w miejscu**, bez przebudowywania
  dashboardu — świadomie, bo pełne renderowanie co minutę przerywałoby gest
  przesuwania karty i resetowało pozycję przewijania. Ten sam tik wykrywa
  też zmianę doby (patrz FR-101) i dopiero wtedy robi pełne odświeżenie.
  Zweryfikowane na żywo (headless Chromium) z podstawionym zegarem:
  przejście 22:00 → 14:00 → 22:00 poprawnie przełącza tekst i klasę
  „post/jedzenie”, wyłączenie opcji usuwa element, ponowne włączenie
  przywraca go. CACHE_NAME→v112, `versions/v112/`.
- **v5** (2026-08-28, Web only): Dodane powiadomienia o granicach okna
  jedzenia — ostatnia pozycja z listy rekomendacji zaproponowanej
  użytkownikowi. Sam status na ekranie wymaga, żeby na niego spojrzeć, a
  cała wartość okna czasowego polega na tym, żeby wiedzieć, KIEDY się
  zmienia. Opcja jest osobnym, domyślnie wyłączonym przełącznikiem obok
  godzin (włączenie samego śledzenia nie zaczyna niczego wysyłać) i
  korzysta z tego samego tiku zegara co v4 — bez nowego mechanizmu
  harmonogramowania.

  Decyzje warte odnotowania: (1) logika „czy teraz wypada powiadomić”
  została wydzielona do czystej funkcji `fastingNotificationDue(fasting,
  nowDate, todayKey)`, przyjmującej czas jako argument — bo ścieżka
  `showNotification()` wymaga Service Workera i zgody, których nie da się
  odtworzyć w headlessowej weryfikacji używanej w tym projekcie, natomiast
  wszystkie reguły decydujące o wysyłce dają się przetestować i to w nich
  mieszkałyby błędy; (2) 10-minutowe okno tolerancji — powiadomienie
  „okno jedzenia właśnie się otworzyło” wysłane trzy godziny po fakcie
  jest gorsze niż brak powiadomienia; (3) `lastNotified` zapisywane jest
  PRZED próbą wysłania — gdy wysyłka zawiedzie (cofnięta zgoda, brak SW),
  użytkownik po prostu nic nie dostaje, zamiast dostawać ponawianą próbę
  co minutę przez całe okno tolerancji.

  Uwaga o synchronizacji: `fasting` należy do `SYNCED_STATE_KEYS`, więc
  `lastNotified` wędruje między urządzeniami — w praktyce oznacza to, że
  daną granicę ogłosi jedno urządzenie, a nie każde z osobna. Uznane za
  zachowanie pożądane (brak podwójnych powiadomień na telefonie i
  laptopie), nie za efekt uboczny.

  Zweryfikowane na żywo (headless Chromium): 14 przypadków logiki
  decyzyjnej — dokładna godzina granicy, wnętrze i koniec okna tolerancji,
  minuta przed, druga granica tego samego dnia, powtórka tego samego dnia
  (pomijana), ta sama granica następnego dnia (wysyłana), wyłączony
  przełącznik, wyłączony tracker, okno 24-godzinne (start==koniec),
  okno zawijające się przez północ, brak obiektu konfiguracji; plus pełna
  ścieżka przełącznika w Ustawieniach: odmowa zgody cofa przełącznik i
  pokazuje komunikat, zgoda go akceptuje i zeruje pamięć ogłoszeń,
  wyłączenie działa, a formularz odtwarza zapisany stan.
  CACHE_NAME→v114, `versions/v114/`.

---

# FR-95: Wyszukiwanie AI (Gemini) na kartach przepisów + wyszukiwanie tylko na rozwiniętej karcie

**Obszar:** Karta przepisu (Przepisy, Planer), Android + Web
**Status:** Zaimplementowane na obu platformach

## Opis
Karta przepisu (zwinięta lub rozwinięta, wszędzie gdzie się pojawia —
Przepisy, podgląd z Planera) ma teraz trzeci przycisk akcji, „✨ Gemini”,
obok istniejących Google/YouTube. Otwiera od razu gotową odpowiedź AI
(Google Search „AI Mode”, `udm=50`) z promptem proszącym o szczegółowy,
krok-po-kroku przepis na dokładnie to danie (z jego nazwą) — patrz **v2**
niżej, dlaczego to Google Search zamiast bezpośrednio gemini.google.com.

Jednocześnie: kliknięcie w SAM TYTUŁ przepisu (które otwiera wyszukiwanie
Google) jest teraz aktywne WYŁĄCZNIE gdy karta jest rozwinięta — na
zwiniętej karcie kliknięcie tytułu tylko rozwija kartę (jak reszta karty),
nie otwiera już wyszukiwania w tle.

## Kryteria akceptacji
- Przycisk „✨ Gemini” widoczny obok Google/YouTube na każdej karcie
  przepisu, niezależnie od stanu zwinięcia.
- Kliknięcie „✨ Gemini” od razu pokazuje odpowiedź AI (bez dodatkowego
  ręcznego wysłania/potwierdzenia) z promptem zawierającym nazwę dania i
  prośbę o szczegółowy przepis krok po kroku.
- Kliknięcie tytułu na ZWINIĘTEJ karcie: nie otwiera wyszukiwania Google
  (tylko normalne rozwinięcie karty).
- Kliknięcie tytułu na ROZWINIĘTEJ karcie: otwiera wyszukiwanie Google
  dla nazwy dania, jak dotychczas.
- `./gradlew :logic:test :app:compileDebugKotlin` przechodzi.

## Historia rewizji
- **v1** (2026-08-26): Pierwsza wersja obu zmian naraz (użytkownik
  poprosił o nie w tym samym zdaniu, ta sama część UI karty). Zweryfikowane
  kompilacją i testami jednostkowymi oraz składniowo na webie. **Nie
  zweryfikowane wizualnie/interaktywnie** w tej turze.
- **v2** (2026-08-28): Użytkownik zgłosił, że przycisk „✨ Gemini” w
  praktyce „otwiera tylko stronę do wpisania tekstu” — `gemini.google.com/
  app?q=` faktycznie tylko WYPEŁNIA pole czatu Gemini, nigdy go nie
  wysyła, więc każde kliknięcie i tak wymagało ręcznego wysłania
  wiadomości, ciche unieważnienie sensu przycisku „jedno kliknięcie,
  gotowy przepis”. Brak udokumentowanego parametru auto-wysyłki dla
  czatowego UI Gemini (i nie da się wstrzyknąć skryptu do strony w innej
  domenie z poziomu `window.open`/`Intent.ACTION_VIEW`) — zamiast tego
  przełączone na Google Search „AI Mode” (`udm=50`): ten sam model
  (Gemini) pod spodem, ale strona wyników wyszukiwania odpowiada od razu
  po wczytaniu jak każde inne wyszukiwanie, bez niczego do kliknięcia.
  Etykieta przycisku bez zmian („✨ Gemini”) — to wciąż odpowiedź Gemini,
  tylko inny kształt URL-a. Web: `data-search-gemini` handler w
  `index.html`. Android: `RecipeListScreen.kt`'s analogiczny `OutlinedButton`
  — **niezweryfikowane kompilacją w tej sesji** (środowisko zdalne bez
  dostępu do `api.foojay.io`/toolchainów Gradle, potwierdzone błędem 403
  przy `:app:compileDebugKotlin`; zmiana jest jednoliniowa i mechaniczna,
  ale czeka na potwierdzenie kompilacją/wizualnie w prawdziwym Android
  Studio lub lokalnej sesji z pełnym dostępem do sieci). Web zweryfikowany
  na żywo (headless Chromium, przechwycone `window.open`): przycisk
  konstruuje `https://www.google.com/search?q=...&udm=50` z poprawnym,
  zakodowanym promptem.

---

# FR-96: Wypełnianie kolorem kafelka „Pozostało” w Planerze

**Obszar:** Planer (motyw Klinika), Ustawienia, Android + Web
**Status:** Zaimplementowane na obu platformach (lokalnie, bez synchronizacji w chmurze na Androidzie — patrz Uwagi)

## Opis
Nowa, domyślnie WYŁĄCZONA opcja w Ustawieniach → Wygląd: kafelek
„POZOSTAŁO” na dashboardzie Planera (motyw Klinika) wypełnia się kolorem
proporcjonalnie do zjedzonych dziś kalorii — ta sama idea, co pierścień
kalorii obok niego, tylko na prostokątnym kafelku.

## Kryteria akceptacji
- Wyłączone (domyślnie): kafelek „POZOSTAŁO” wygląda dokładnie tak jak
  przed tą funkcją (jednolite tło).
- Włączone: kafelek wypełnia się półprzezroczystym nakładanym paskiem od
  lewej, szerokość = (zjedzone kcal / cel kcal) × 100%, aktualizowana na
  żywo przy zmianie zjedzonych posiłków.
- Włączenie/wyłączenie w Ustawieniach natychmiast zmienia wygląd kafelka
  bez przeładowania.
- `./gradlew :logic:test :app:compileDebugKotlin` przechodzi.

## Uwagi
Android: zaimplementowane przez zwykły `Box` (nie `Card`/`Surface`) z
osobnym nakładanym `Box` — `Card`/`Surface`'s własny `containerColor`
maluje SIĘ NAD zewnętrznym modyfikatorem `.background()`, znany pułapek
z wcześniejszej rundy (swipe-tint), świadomie ominięty tu tym samym
sposobem.

Świadoma decyzja o zakresie: na Androidzie to ustawienie jest
LOKALNE-TYLKO (`RemainingKcalFillViewModel` + `LocalPersistenceCoordinator`,
NIE `CloudSyncCoordinator`) — czysta preferencja wyświetlania, nie dane
warte trójstronnego scalania między urządzeniami. Na webie jest częścią
`SYNCED_STATE_KEYS` (synchronizowane).

## Historia rewizji
- **v1** (2026-08-26): Pierwsza wersja. Zweryfikowane kompilacją i testami
  jednostkowymi oraz składniowo na webie. **Nie zweryfikowane
  wizualnie/interaktywnie** w tej turze.

---

# FR-97: Znacznik stanu spiżarni na kartach „Dzisiejszy Planer”

**Obszar:** Planer (motyw Klinika), Android + Web
**Status:** Zaimplementowane na obu platformach (klikalność znacznika — v2 — na razie Web-only, patrz Uwagi)

## Opis
Na kartach dań w sekcji „Dzisiejszy Planer” (dużo wolnego miejsca na
karcie) dodany mały znacznik: ile z potrzebnych składników danego dania
jest już w spiżarni, a ile trzeba dokupić (np. „🏺 4/6 w spiżarni”).

Na webie (v2) znacznik jest dodatkowo klikalny — stuknięcie otwiera od razu
okno „sprawdź co masz” (FR-16) dla tego dania, czyli szczegółową listę
składników z informacją, których brakuje, podpowiedziami zamienników
(FR-93) i przyciskami „🛒”/„+ Mam to”. Dotąd znacznik pokazywał tylko
liczbę, a żeby zobaczyć KTÓRYCH składników brakuje, trzeba było
stuknąć kartę, otworzyć podgląd przepisu i dopiero stamtąd wejść w stan
spiżarni.

## Kryteria akceptacji
- Każda karta dania w „Dzisiejszy Planer” pokazuje znacznik „🏺 N/M w
  spiżarni”, gdzie M = liczba składników przepisu, N = ile z nich jest w
  spiżarni.
- Znacznik aktualizuje się na żywo po zmianie zawartości spiżarni, bez
  konieczności odświeżenia ekranu.
- Puste sloty (bez zaplanowanego dania) nie pokazują znacznika.
- Web (v2): stuknięcie znacznika otwiera okno „sprawdź co masz” dla tego
  dania (a NIE podgląd przepisu, który otwiera stuknięcie reszty karty).
- Web (v2): stuknięcie dowolnego innego miejsca karty nadal otwiera podgląd
  przepisu, bez zmian względem v1.
- `./gradlew :logic:test :app:compileDebugKotlin` przechodzi.

## Uwagi
Web: znacznik zmieniony ze `<span>` na `<button>` z pełnym resetem stylu
(bez tła/obramowania), żeby wyglądał identycznie jak wcześniej, plus
kropkowane podkreślenie — ta sama konwencja „ten tekst coś otwiera”, co
istniejące `.recipe-title`. Handler wywołuje istniejące `openPantryModal(r)`
(FR-16), zero nowej logiki. Nadrzędny handler karty (podgląd przepisu)
dostał `closest("[data-pd-pantry-check]")` do listy wyjątków, obok już
istniejącego wyjątku dla przycisku usuwania.

**v2 (klikalność) świadomie Web-only na razie** — ta sesja pracuje w
środowisku bez dostępu do `api.foojay.io` (toolchain JDK dla Gradle,
błąd 403 przy `:app:compileDebugKotlin`), więc port do Compose nie może tu
zostać ani skompilowany, ani przetestowany; odłożone do sesji z realnym
dostępem do Gradle/emulatora, odnotowane w `android/PARITY.md`.

## Historia rewizji
- **v1** (2026-08-26): Pierwsza wersja. Wykorzystuje istniejące
  dopasowywanie składnik→spiżarnia (`RecipePantryMatching`/`pantryMatch`),
  zero nowej logiki kategoryzacji. Zweryfikowane kompilacją i testami
  jednostkowymi oraz składniowo na webie. **Nie zweryfikowane
  wizualnie/interaktywnie** w tej turze.
- **v2** (2026-08-28, Web only): Znacznik zrobiony klikalnym — na wyraźną
  prośbę użytkownika („zrób punkt 1, klikalny znacznik spiżarni”), z
  wcześniejszej listy rekomendacji: sam licznik „4/6” mówi ILE brakuje, ale
  nie CZEGO, a droga do tej informacji wiodła przez dwa dodatkowe
  stuknięcia mimo że dane były już policzone w tym samym miejscu.
  Zweryfikowane na żywo (headless Chromium): stuknięcie znacznika otwiera
  `pantryModalOverlay` z 6 wierszami składników (a nie podgląd przepisu),
  stuknięcie nazwy dania nadal otwiera podgląd przepisu (a nie spiżarnię) —
  oba kierunki potwierdzone osobno, żeby wykluczyć przechwycenie zdarzenia
  przez nadrzędny handler karty. CACHE_NAME→v106, `versions/v106/`.

---

# FR-98: Kopia zapasowa danych do pliku (eksport i import)

**Obszar:** Ustawienia → Konto, Web
**Status:** Zaimplementowane na webie (Android — nieprzeniesione, patrz Uwagi)

## Opis
W Ustawieniach → Konto jest karta „💾 Kopia zapasowa danych” z dwoma
przyciskami:

- **„⬇️ Zapisz kopię zapasową do pliku”** — zapisuje wszystkie dane
  użytkownika (profil, spiżarnia, lista zakupów, planer, ulubione, własne
  przepisy, oceny, historia wagi i kalorii, ustawienia) do jednego pliku
  JSON na urządzeniu, o nazwie `dieta-app-kopia-RRRR-MM-DD.json`.
- **„⬆️ Wczytaj kopię zapasową z pliku”** — wczytuje wcześniej zapisany
  plik i ZASTĘPUJE nim obecne dane w aplikacji, po pokazaniu daty, z
  której pochodzi kopia, i poproszeniu o potwierdzenie.

Działa niezależnie od logowania i od synchronizacji z chmurą — to jedyna
ścieżka odzyskania danych, która nie zależy od tego, czy chmura działa.

Powód dodania (2026-08-28): aplikacja nie miała ŻADNEGO sposobu na
wydostanie danych z siebie. Wszystko żyje w `localStorage` i opcjonalnie w
Firestore, a historia tego projektu pokazuje, dlaczego to za mało: FR-73
przeszedł kilka rund realnych awarii synchronizacji, FR-89 dodał przycisk
kasujący wszystkie dane na koncie, a zalogowanie się na drugim urządzeniu
ZASTĘPUJE dane lokalne zamiast je scalać (opisane wprost w karcie „Konto w
chmurze”). W każdym z tych scenariuszy plik, który użytkownik trzyma u
siebie, jest ostatnią linią obrony.

## Kryteria akceptacji
- Eksport zapisuje plik JSON o nazwie zawierającej datę eksportu.
- Wyeksportowany plik zawiera znacznik formatu (`format`, `version`),
  datę eksportu (`exportedAt`) i wszystkie dane użytkownika.
- Import z poprawnego pliku odtwarza dane dokładnie w stanie z momentu
  eksportu (sprawdzone m.in. na profilu, spiżarni, planerze, ulubionych i
  historii wagi).
- Import prosi o potwierdzenie i pokazuje datę, z której pochodzi kopia,
  ZANIM cokolwiek nadpisze.
- Import pliku, który nie jest kopią zapasową Dieta App, pokazuje
  komunikat i NIE zmienia obecnych danych.
- Import pliku z uszkodzonym JSON-em pokazuje komunikat i NIE zmienia
  obecnych danych.
- Import pliku z nowszą wersją formatu niż obsługiwana pokazuje komunikat
  i NIE zmienia obecnych danych.
- Wybranie tego samego pliku dwa razy pod rząd działa za drugim razem tak
  samo jak za pierwszym.

## Uwagi
Zakres eksportu to dokładnie `SYNCED_STATE_KEYS` — ta sama lista, którą
aplikacja synchronizuje z chmurą. Świadoma decyzja: kopia obejmująca
cokolwiek innego niż ten zbiór z definicji rozjeżdżałaby się z tym, co
przenosi zalogowanie się na drugim urządzeniu. Celowo NIE jest to zrzut
całego obiektu `state`, który trzyma też pola pochodne i sesyjne, mające
sens tylko na tym urządzeniu.

Import stosuje wyłącznie klucze, które aplikacja zna (`SYNCED_STATE_KEYS`):
starsza kopia zostawia nowsze pola w obecnym stanie zamiast kasować je do
`undefined`, a nieznany klucz z ręcznie zmodyfikowanego pliku jest
ignorowany, zamiast wstrzykiwać się do `state`.

Po imporcie interfejs odświeża się przez istniejące `refreshUiAfterSync()`
(mechanizm z FR-73) — ta sama ścieżka, którą aplikacja i tak stosuje po
otrzymaniu danych z chmury, więc nie powstaje drugi, równoległy sposób
„przeładuj wszystko po podmianie stanu”.

`URL.revokeObjectURL` jest wywoływane z opóźnieniem, a nie natychmiast —
część przeglądarek mobilnych przekazuje blob do menedżera pobierania
asynchronicznie i unieważnienie w tym samym cyklu potrafi anulować
pobieranie, zanim się zacznie.

**Android: nieprzeniesione.** Ta sesja pracuje w środowisku bez dostępu do
`api.foojay.io` (toolchain JDK dla Gradle, błąd 403 przy
`:app:compileDebugKotlin`), więc kodu w Kotlinie nie da się tu
skompilować ani przetestować. Port będzie wymagał `ACTION_CREATE_DOCUMENT`
/ `ACTION_OPEN_DOCUMENT` (Storage Access Framework) zamiast blobu i linku
`download` — czyli nie jest to przepisanie 1:1, tylko osobny kawałek pracy.
Odnotowane w `android/PARITY.md`.

## Historia rewizji
- **v1** (2026-08-28, Web only): Pierwsza wersja. Zmiana z własnej
  rekomendacji, po przeglądzie funkcji pod kątem luk (użytkownik: „dodawaj
  swoje rekomendowane zmiany”). Zweryfikowane na żywo (headless Chromium,
  z przechwyceniem realnego pobierania pliku): eksport → wyczyszczenie
  danych w aplikacji → import tego samego pliku → wszystkie sprawdzane pola
  (nazwa użytkownika, spiżarnia, planer, ulubione, historia wagi) wróciły
  identyczne; osobno sprawdzone trzy ścieżki odrzucenia (plik niebędący
  kopią, uszkodzony JSON, nowsza wersja formatu) — każda pokazała właściwy
  komunikat i zostawiła dane nietknięte. CACHE_NAME→v108, `versions/v108/`.


- **v2** (2026-08-29, PORT NA ANDROIDA): kopia zapasowa do pliku działa też
  w aplikacji natywnej. **Nie jest to przepisanie 1:1** — na Androidzie nie
  ma pobierania bloba, więc zapis i odczyt idą przez Storage Access
  Framework (`ACTION_CREATE_DOCUMENT` / `ACTION_OPEN_DOCUMENT`), a
  użytkownik sam wybiera miejsce pliku. FORMAT pliku jest identyczny
  (`BackupFile`), więc kopia zrobiona na telefonie wczytuje się w
  przeglądarce i odwrotnie — co jest w zasadzie sensem posiadania tego po
  obu stronach.

  Import przechodzi przez DOKŁADNIE tę samą ścieżkę co zwykłe uruchomienie
  aplikacji (`applyLocalSnapshot`, wydzielone z
  `LocalPersistenceCoordinator`) — dwie implementacje „przywróć wszystko z
  mapy” to dwa miejsca, w których można zapomnieć o polu przy następnej
  zmianie, a kopia, która po cichu pomija pole, jest gorsza niż jej brak, bo
  wygląda na udaną. Każdy powód odrzucenia pliku ma własny komunikat i
  ŻADEN nie rusza danych już w aplikacji.

  Zweryfikowane na emulatorze: zapis do `/sdcard/Download/` z poprawnie
  podpowiedzianą nazwą `dieta-app-kopia-2026-08-29.json`, plik 6,3 kB z
  właściwą kopertą `{format, version, exportedAt, data}`, a następnie odczyt
  tego samego pliku — okienko potwierdzenia pokazało datę „29.08.2026,
  09:49”, a po wczytaniu plan i podsumowanie tygodnia były nietknięte.


- **v3** (2026-08-29): kopia zapasowa dostała test obiegu „eksport → import
  → wszystko wróciło”, uruchamiany przy każdym budowaniu zamiast raz, ręcznie.

  To było zabezpieczenie, którego tej funkcji brakowało. Kopia, która po
  cichu gubi pole, jest gorsza niż jej brak, bo wygląda na udaną —
  dowiadujesz się dopiero wtedy, gdy jej potrzebujesz.

  Żeby dawało się to testować bez Androida, decyzje wyprowadzone zostały z
  modułu `app` do `BackupEnvelope` w `logic/`: kształt koperty i cała
  walidacja (czy to nasz plik, czy nie jest z nowszej wersji). W `BackupFile`
  zostało wyłącznie to, czego nie da się przenieść — `org.json` i Storage
  Access Framework.

  `BackupRoundTripTest` składa ładunek DOKŁADNIE tak, jak składa go aplikacja
  (`CloudSyncCodec.encodeAll` plus te same pola dodatkowe, które dokłada
  `LocalPersistenceCoordinator`), pakuje, odczytuje i dekoduje pole po polu —
  w tym `pantryHidden` (FR-102) i ułamkową porcję (FR-105), która musi wrócić
  jako ta sama ćwiartka, a nie całość.

  Kroku „Mapa → tekst JSON → Mapa” test nie powtarza (wymagałby `org.json`,
  czyli Androida) — zamiast tego sprawdza, że ładunek jest JSON-BEZPIECZNY, bo
  to jedyne miejsce, w którym ten krok może zgubić dane: przyszłe pole
  trzymające enum, klasę danych albo `Set` zakodowałoby się „pomyślnie” i
  wróciło bezużyteczne. Sam wykrywacz też ma test — asercja, która nigdy nie
  może paść, jest gorsza niż jej brak.

  Pokryte są też trzy ścieżki odrzucenia (nie nasz plik / wersja z
  przyszłości / brak `data`) oraz zgodność wstecz: starsza kopia bez nowszych
  pól wczytuje to, co ma, zamiast kasować resztę.

---

# FR-99: Wyszukiwanie na liście zakupów

**Obszar:** Lista zakupów, Web
**Status:** Zaimplementowane na webie (Android — nieprzeniesione, patrz Uwagi)

## Opis
Nad listą zakupów jest pole „Szukaj na liście zakupów…”, filtrujące
pozycje po nazwie w miarę pisania. Filtr działa tak samo w obu widokach
listy (klasycznym „📃 Lista” i kafelkowym „🏺 Kafelki”), bo oba renderują
te same dane.

Wyszukiwanie ignoruje polskie znaki diakrytyczne — „zolty” znajduje
„żółty ser”. (Uwaga historyczna: przy dodawaniu tej funkcji okazało się, że
wyszukiwanie PRZEPISÓW takiej odporności NIE miało — zostało to naprawione
osobno, patrz FR-2/v6.)

Licznik pozycji nad listą pokazuje przy aktywnym filtrze „N z M pozycji”,
żeby nigdy nie przeczył temu, co widać na ekranie, ale jednocześnie było
widać, że reszta listy nadal istnieje. Obok pola jest przycisk „✕”
czyszczący filtr (widoczny tylko, gdy filtr jest aktywny).

Powód dodania (2026-08-28): realne listy zakupów w tej aplikacji bywają
długie — lista 87-pozycyjna została odnotowana przy okazji debugowania
FR-87/v9 — a pozycje są pogrupowane po kategoriach, czyli w kolejności
przydatnej w sklepie, ale nie do szukania konkretnej rzeczy. Odpowiedź na
pytanie „czy dodałem już mleko?” wymagała przewinięcia całej listy.

## Kryteria akceptacji
- Wpisanie tekstu filtruje listę do pozycji, których nazwa zawiera ten
  tekst.
- Filtrowanie ignoruje polskie znaki diakrytyczne w obie strony.
- Licznik pozycji pokazuje „N z M pozycji” przy aktywnym filtrze i samo
  „M pozycji”, gdy filtr jest pusty.
- Brak dopasowań pokazuje czytelny komunikat z wpisaną frazą, a nie pustą
  listę.
- Widok kafelkowy respektuje ten sam filtr co widok listy.
- Przycisk „✕” czyści filtr i przywraca pełną listę; jest widoczny tylko
  przy aktywnym filtrze.
- Pusta lista zakupów (bez żadnej pozycji) nadal pokazuje swój dotychczasowy
  komunikat zachęcający do dodania składników — a nie komunikat o braku
  wyników wyszukiwania.
- Udostępnianie i kopiowanie listy (FR-26) eksportuje CAŁĄ listę, nie
  przefiltrowany widok.

## Uwagi
Fraza wyszukiwania celowo NIE jest zapisywana w `state` (a więc nie trafia
do `SYNCED_STATE_KEYS` ani do chmury): to przejściowy sposób patrzenia na
listę, a nie jej część — synchronizowanie go oznaczałoby, że jedno
urządzenie może zostawić listę na drugim w tajemniczo przefiltrowanym
stanie.

Rozdzielenie `allKeys` (pełna lista) od `keys` (przefiltrowana) w
`renderShop()` jest celowe: komunikat „lista jest pusta” musi zależeć od
tej pierwszej, a komunikat „nic nie pasuje” od drugiej — inaczej pusta
lista pokazywałaby użytkownikowi, że jego wyszukiwanie nic nie znalazło,
zamiast podpowiedzieć, jak w ogóle dodać pierwsze składniki.

`buildListText()` (udostępnianie/kopiowanie) świadomie pomija filtr —
filtr służy do znalezienia czegoś na ekranie, a nie do wybrania, co wysłać
osobie robiącej zakupy.

**Android: nieprzeniesione.** Ta sesja pracuje w środowisku bez dostępu do
`api.foojay.io` (toolchain JDK dla Gradle, błąd 403 przy
`:app:compileDebugKotlin`), więc kodu w Kotlinie nie da się tu
skompilować ani przetestować. Odnotowane w `android/PARITY.md`.

## Historia rewizji
- **v1** (2026-08-28, Web only): Pierwsza wersja. Zmiana z własnej
  rekomendacji. Zweryfikowane na żywo (headless Chromium), siedem
  przypadków osobno: brak filtra (5 pozycji, „✕” ukryty), filtr „mle”
  (1 z 5, właściwa pozycja), filtr „zolty” znajdujący „żółty ser”
  (niewrażliwość na diakrytyki), brak dopasowań (komunikat z frazą),
  widok kafelkowy respektujący filtr (1 kafelek), wyczyszczenie filtra
  przywracające pełną listę i puste pole, oraz `buildListText()` przy
  aktywnym filtrze zwracający wszystkie 5 pozycji. CACHE_NAME→v108,
  `versions/v108/`.


- **v2** (2026-08-29, PORT NA ANDROIDA): wyszukiwarka listy zakupów działa
  też w aplikacji natywnej — pole nad listą, licznik „N z M pozycji”,
  przycisk „✕” widoczny tylko przy aktywnym filtrze, odporność na polskie
  znaki (przez `PolishText`, patrz FR-2/v8). Zachowany podział na `items`
  (pełna lista) i `visibleItems` (przefiltrowana) z tego samego powodu co na
  webie: „lista jest pusta” i „nic nie pasuje” muszą zostać dwoma różnymi
  komunikatami. Fraza nie jest zapisywana ani synchronizowana. Zweryfikowane
  na emulatorze: po dodaniu 13 pozycji wpisanie „jajk” dało nagłówek
  „Lista zakupów (1 z 13)”.

---

# FR-100: Podsumowanie odżywcze zaplanowanego tygodnia

**Obszar:** Planer tygodniowy, Web
**Status:** Zaimplementowane na webie (Android — nieprzeniesione, patrz Uwagi)

## Opis
Pod przyciskami udostępniania, nad listą dni, Planer pokazuje kartę
„📊 Zaplanowany tydzień” z podsumowaniem całego zaplanowanego tygodnia:

- średnia liczba kalorii na dzień (duża liczba),
- plakietka porównująca tę średnią z dziennym celem: „w celu (X kcal)”
  przy odchyleniu do ±50 kcal, w przeciwnym razie „+N / −N kcal vs cel X”,
- z ilu zaplanowanych dni i ilu dań liczona jest ta średnia,
- średnie dzienne makroskładniki (białko / węglowodany / tłuszcz).

Karta nie pojawia się wcale, dopóki w tygodniu nie ma ani jednego
zaplanowanego dania.

Powód dodania (2026-08-28): Planer pokazywał sumy kaloryczne dla
pojedynczych dni, ale nic o tygodniu jako całości — więc odpowiedź na
pytanie „czy tydzień, który właśnie ułożyłem, trzyma się mojego celu?”
wymagała ręcznego sumowania siedmiu kart dni.

## Kryteria akceptacji
- Pusty tydzień (bez ani jednego zaplanowanego dania): karta nie jest
  renderowana w ogóle (nie pusta ramka, nie zera).
- Średnia dzienna liczona jest po dniach ZAPLANOWANYCH, nie po siedmiu.
- Podsumowanie uwzględnia skalę porcji (2× danie liczy się podwójnie).
- Plakietka pokazuje „w celu”, gdy średnia mieści się w ±50 kcal od celu
  dziennego; w przeciwnym razie pokazuje kierunek i wielkość odchylenia.
- Karta podaje, z ilu dni i ilu dań policzono średnią.
- Jeśli część zaplanowanych dań nie ma podanych makroskładników, karta
  mówi wprost, z ilu dań policzono makro; jeśli żadne ich nie ma —
  informuje o tym zamiast pokazywać zera.
- Karta aktualizuje się przy każdej zmianie planu (dodanie/usunięcie dania,
  zmiana skali porcji, losowanie, czyszczenie, kopiowanie dnia).

## Uwagi
Uśrednianie po dniach zaplanowanych, a nie po siedmiu, jest świadomą
decyzją: tydzień zaplanowany w połowie pokazywałby średnią dwukrotnie
zaniżoną i wyglądałby jak głodówka, choć jest po prostu nieskończonym
planem. Z tego samego powodu karta zawsze podaje, z ilu dni liczy.

Makro sumowane są wyłącznie z dań, które je mają (własne przepisy
użytkownika mogą ich nie mieć — FR-66 traktuje je jako opcjonalne), a
liczba takich dań jest pokazywana obok — żeby częściowy wynik nigdy nie
udawał pełnego.

Karta jest celowo umieszczona POZA `#plannerTodayWrap` — ten kontener to
starannie wymierzona, pełnoekranowa sekcja „dziś” (FR-87/v16), więc
cokolwiek dodanego w środku zmieniłoby tamten układ. Style używają
wyłącznie zmiennych motywu (`--text`/`--muted`/`--line`/`--teal-pale`
itd.), więc karta dziedziczy wszystkie 13 motywów bez własnych reguł
per-motyw.

**Android: nieprzeniesione.** Ta sesja pracuje w środowisku bez dostępu do
`api.foojay.io` (toolchain JDK dla Gradle, błąd 403 przy
`:app:compileDebugKotlin`), więc kodu w Kotlinie nie da się tu
skompilować ani przetestować. Odnotowane w `android/PARITY.md`.

## Historia rewizji
- **v1** (2026-08-28, Web only): Pierwsza wersja. Zmiana z własnej
  rekomendacji. Zweryfikowane na żywo (headless Chromium): pusty tydzień nie
  renderuje nic; 3 zaplanowane dni po 2 dania dają średnią liczoną po
  dniach zaplanowanych (710 kcal), a nie po siedmiu (co dałoby mylące
  304 kcal) — obie wartości policzone i porównane w teście; zwiększenie
  skali porcji jednego dania podniosło sumę tygodnia (2130→2520 kcal);
  plakietka odchylenia dostała właściwą klasę (`wps-under` przy średniej
  poniżej celu). Sprawdzone też wizualnie zrzutem ekranu na pełnym,
  5-dniowym planie w domyślnym motywie Klinika. CACHE_NAME→v110,
  `versions/v110/`.


- **v2** (2026-08-29, PORT NA ANDROIDA): karta „📊 Zaplanowany tydzień”
  dodana nad listą dni w aplikacji natywnej. Liczenie wydzielone do
  `WeekPlanSummary` w module `logic` i pokryte testami — obie decyzje, na
  których stoi wiarygodność tej liczby (średnia po dniach ZAPLANOWANYCH, nie
  po siedmiu; makra tylko z dań, które je mają, wraz z licznikiem), są tam
  zapisane jako testy, a nie tylko jako komentarz. Zweryfikowane na
  emulatorze: przy 5 daniach w 3 dniach karta pokazała „489 kcal · −991 kcal
  vs cel 1480 · średnio na dzień, z 3 zaplanowanych dni (5 dań)”.

---

# FR-101: Dni kalendarzowe liczone lokalnie, nie w UTC

**Obszar:** Śledzenie postępów / cała aplikacja (przekrojowe), Web + Android
**Status:** Zaimplementowane na webie; **na Androidzie POTWIERDZONE JAKO NIENAPRAWIONE** — patrz Uwagi

## Opis
Wszystkie klucze dat w aplikacji (`RRRR-MM-DD`) oznaczają **lokalny dzień
kalendarzowy użytkownika**, a nie dzień w strefie UTC. Dotyczy to każdego
miejsca, w którym aplikacja pyta „który dziś jest dzień?”: licznika wody,
zjedzonych posiłków, historii kalorii, serii (streaks), nawigacji po
wcześniejszych dniach w zakładce Postęp oraz licznika wody obsługiwanego
przez Service Worker w powiadomieniach.

To wymaganie spisane zostało przy okazji naprawy błędu (2026-08-28) —
wcześniej daty były wyliczane z `toISOString()`, czyli w UTC. Polska jest
UTC+1 (zima) / UTC+2 (lato), więc:

- **`todayStr()` zwracało WCZORAJ** między lokalną północą a 01:00/02:00.
  Szklanka wody albo posiłek zapisane o 00:30 trafiały do poprzedniego dnia,
  a doba nie „przeskakiwała” o północy tylko z opóźnieniem.
- **`addDaysToDateStr()` było przesunięte o dzień ZAWSZE**, niezależnie od
  pory — budowało lokalną północ i odczytywało z niej datę UTC, która na
  wschód od Greenwich jest wciąż dniem poprzednim. W praktyce, zmierzone w
  przeglądarce ustawionej na Europe/Warsaw: strzałka „poprzedni dzień” w
  zakładce Postęp przeskakiwała z 28.08 na **26.08** (pomijając 27.08), a
  strzałka „następny dzień” **nie działała w ogóle** — użytkownik cofnięty
  w przeszłość nie mógł wrócić do dziś bez przeładowania aplikacji.

Te same funkcje zachowywały się poprawnie w strefie UTC i w strefach
zachodnich (potwierdzone testem w Europe/Warsaw, UTC, America/New_York) —
dlatego błąd mógł tu przetrwać niezauważony, mimo że dotyczył całej
polskojęzycznej grupy użytkowników tej aplikacji.

## Kryteria akceptacji
- Klucz dnia zwracany przez aplikację odpowiada dacie z lokalnego
  kalendarza użytkownika o każdej porze doby, w tym tuż po północy.
- Przejście o N dni w tył/przód daje dokładnie N dni różnicy, w każdej
  strefie czasowej.
- Przechodzenie po dniach działa poprawnie przez granice miesiąca i roku
  (31.08 → 01.09, 31.12.2026 → 01.01.2027 i z powrotem).
- Strzałki „poprzedni/następny dzień” w zakładce Postęp przesuwają się o
  dokładnie jeden dzień i pozwalają wrócić do dnia dzisiejszego; przycisk
  „następny” jest nieaktywny, gdy pokazywany jest dzień dzisiejszy.
- Service Worker (licznik wody z powiadomień) używa DOKŁADNIE tego samego
  klucza dnia co aplikacja — inaczej jedna doba rozjeżdżałaby się na dwa
  osobne wpisy.

## Uwagi
Naprawione przez odczyt lokalnych składowych daty (`getFullYear()`/
`getMonth()`/`getDate()`) zamiast konwersji przez UTC. Nie ma tu „poprawnego
przesunięcia UTC”, które można by zamiast tego zastosować — kluczowana jest
własna doba kalendarzowa użytkownika, więc każda konwersja stref jest
z definicji błędem.

Dane zapisane PRZED tą poprawką pozostają tam, gdzie były: wpis zrobiony
o 00:30 jest nadal przypisany do poprzedniego dnia. Świadomie nie ma
migracji — nie da się odróżnić wpisu źle przypisanego przez ten błąd od
wpisu, który użytkownik celowo przypisał do wcześniejszego dnia (FR-83
pozwala edytować przeszłe dni), więc „naprawianie” historii byłoby
zgadywaniem na danych, których nie wolno ruszać.

**Android: ten sam błąd WYSTĘPUJE i jest nienaprawiony.** Pierwotnie
zapisałem tu przypuszczenie, że Kotlin jest bezpieczny, bo `LocalDate.now()`
jest z definicji lokalne — **przypuszczenie okazało się błędne po
sprawdzeniu kodu**. Android nie używa domyślnego `LocalDate.now()`, tylko
JAWNIE wymusza UTC (`LocalDate.now(ZoneOffset.UTC)`) w ośmiu miejscach:

| Plik | Linia | Co trzyma |
|---|---|---|
| `ui/WaterViewModel.kt` | 40 | data licznika wody |
| `ui/WeightViewModel.kt` | 19 | data wpisu wagi |
| `ui/EatenViewModel.kt` | 116 | `todayUtc()` — data zjedzonych posiłków i przekąsek |
| `ui/PostepScreen.kt` | 94 | „dziś” na ekranie Postęp |
| `ui/PostepScreen.kt` | 450 | godzina wpisu w historii aktywności |
| `data/WaterNotificationStore.kt` | 86, 92 | data licznika wody z powiadomień |
| `logic/ActivityLogOperations.kt` | 16 | filtrowanie historii po dacie |
| `logic/CloudSyncCodec.kt` | 569 | `todayUtcDateString()` |

**Na Androidzie jest to nawet gorsze niż było na webie**, bo prowadzi do
wewnętrznej NIESPÓJNOŚCI w obrębie jednego ekranu: `PlannerScreen.kt`
(linie 214 i 761) używa poprawnego, lokalnego `LocalDate.now()` do
wyznaczenia dzisiejszego dnia tygodnia i daty w nagłówku — podczas gdy
`EatenViewModel`/`WaterViewModel` liczą „dziś” w UTC. Między lokalną
północą a 01:00/02:00 Planer pokazuje więc JUŻ nowy dzień, a licznik
zjedzonych kalorii i wody wciąż zapisuje do poprzedniego.

Naprawa jest mechaniczna (zamiana `ZoneOffset.UTC` na
`ZoneId.systemDefault()` w tych ośmiu miejscach, plus przemianowanie
`todayUtc()`/`todayUtcDateString()`, żeby nazwa nie kłamała), ale
**świadomie NIE została wykonana w tej sesji**: środowisko nie ma dostępu
do `api.foojay.io` (toolchain JDK dla Gradle, błąd 403 przy
`:app:compileDebugKotlin`), więc nie dałoby się jej ani skompilować, ani
przetestować, a zasada z `CLAUDE.md` mówi wprost, żeby nie piętrzyć
niezweryfikowanych zmian w Kotlinie. Uwaga przy naprawie: `CloudSyncCodec`
bierze udział w synchronizacji między urządzeniami, więc zmiana klucza dnia
tam wymaga rozważenia, czy dane już zsynchronizowane pod kluczem UTC nie
powinny zostać potraktowane osobno. Odnotowane w `android/PARITY.md`.

## Historia rewizji
- **v1** (2026-08-28, Web only): Pierwsza wersja — spisana razem z naprawą
  błędu opisanego wyżej. Naprawione trzy funkcje w `index.html`
  (`todayStr`, `dateStrDaysAgo`, `addDaysToDateStr`, przez wspólne
  `localDateStr`) i jedna w `sw.js` (`todayStr`). Zweryfikowane na żywo
  (headless Chromium) w PIĘCIU strefach czasowych — Europe/Warsaw, UTC,
  America/New_York, Asia/Tokyo, Pacific/Auckland — z identycznymi wynikami,
  w tym na granicach miesiąca i roku; osobno, z zamrożonym zegarem na
  00:30 czasu warszawskiego, potwierdzone że `todayStr()` zwraca już
  właściwy (nowy) dzień; oraz przez realny przepływ UI: sześć kliknięć
  strzałek w zakładce Postęp przechodzi 29→28→27→26→27→28→29 i poprawnie
  wyłącza przycisk „następny” na dniu dzisiejszym.
  CACHE_NAME→v112, `versions/v112/`.
- **v2** (2026-08-28): Uzupełniona sekcja o Androidzie — **skorygowane
  błędne przypuszczenie z v1**. v1 zakładało, że Kotlin jest odporny na ten
  błąd, bo `LocalDate.now()` jest lokalne; przegląd kodu (bez kompilacji,
  która w tym środowisku jest niemożliwa) wykazał, że Android w ośmiu
  miejscach JAWNIE wymusza `ZoneOffset.UTC`, a w dodatku robi to
  niespójnie — `PlannerScreen` używa czasu lokalnego, więc tuż po północy
  Planer i licznik kalorii pokazują różne dni. Dokładne lokalizacje i
  planowana naprawa wypisane w Uwagach wyżej.


- **v2** (2026-08-29, PORT NA ANDROIDA): błąd opisany w v1 jako
  „POTWIERDZONY, NIENAPRAWIONY na Androidzie” został naprawiony. Android
  wymuszał `ZoneOffset.UTC` w ośmiu miejscach (`WaterViewModel`,
  `WeightViewModel`, `EatenViewModel.todayUtc`, `PostepScreen` ×2,
  `WaterNotificationStore` ×2, `ActivityLogOperations`,
  `CloudSyncCodec.todayUtcDateString`), podczas gdy `PlannerScreen` liczył
  dzień lokalnie — czyli aplikacja była niespójna SAMA ZE SOBĄ: tuż po
  północy Planer pokazywał już nowy dzień, a licznik wody i kalorii
  zapisywał do poprzedniego. To gorzej niż pierwotny błąd webowy, gdzie
  przynajmniej wszyscy mylili się w tę samą stronę.

  Naprawione przez jedną wspólną funkcję (`AppDates`) zamiast ośmiu
  niezależnych wywołań — o to właśnie chodzi, żeby następna funkcja z kluczem
  daty nie mogła po cichu wybrać innej strefy. Serializacja ZNACZNIKÓW CZASU
  (`CloudSyncCodec`'s ISO `…Z`, wspólna z web'owym `toISOString()`) celowo
  zostaje w UTC — chwila to chwila, niezależnie od tego, gdzie się ją czyta.
  `CloudSyncCodec.todayUtcDateString()` przemianowane na `todayDateString()`
  i przestawione na dzień lokalny, bo zasila pole `date` licznika wody i
  ścieżkę `waterHistory.<data>` — dopóki się rozjeżdżały, szklanka wody
  zapisana po lokalnej północy lądowała pod innym kluczem na każdej
  platformie.

---

# FR-102: Trwałe usuwanie produktu ze spiżarni

**Obszar:** Spiżarnia, Android + Web
**Status:** Zaimplementowane na obu platformach

## Opis
Menu po przytrzymaniu kafelka w Spiżarni dostało nową, ostatnią pozycję:
**„❌ Usuń produkt ze spiżarni na stałe”**. Do tej pory najbardziej
„usuwającą” opcją było „🗑️ Usuń śledzenie (wyzeruj stan)”, które kasuje
tylko zapisany stan, ale zostawia kafelek w siatce — bo lista kafelków nie
jest przechowywana, tylko wyliczana od nowa ze wszystkich składników
wszystkich przepisów przy każdym renderowaniu. Z punktu widzenia
użytkownika wyglądało to więc tak, że produktu **nie da się usunąć wcale**
(zgłoszenie z 2026-08-29: „nie da się usunąć produktu ze spiżarni
całkowicie, dodaj taką opcję”).

Nowa opcja kasuje stan ORAZ zapamiętuje nazwę kanoniczną produktu na
liście ukrytych (`state.pantryHidden` na webie, `PantryStore.loadHidden`
na Androidzie), która jest odfiltrowywana przy budowaniu listy kafelków —
dzięki temu kafelek naprawdę znika i nie wraca przy następnym wejściu na
ekran.

Żeby nie był to ruch bez odwrotu, na górze Spiżarni pojawia się przycisk
**„↩️ Przywróć usunięte produkty (N)”** — widoczny tylko wtedy, gdy
faktycznie jest co przywracać. Ponowne dodanie produktu ręcznie
(„➕ Dodaj własny” albo „Mam to” w oknie sprawdzania spiżarni pod
przepisem) też automatycznie zdejmuje go z listy ukrytych.

Lista ukrytych produktów synchronizuje się między urządzeniami tak samo
jak reszta danych (nowy klucz `pantryHidden`, po stronie webu dopisany do
`SYNCED_STATE_KEYS` i `MAP_MERGE_KEYS`, więc dwa urządzenia ukrywające
różne produkty scalają się per pozycja, a nie „całą listą”).

## Kryteria akceptacji
- Przytrzymanie DOWOLNEGO kafelka (śledzonego i nieśledzonego) otwiera
  menu akcji — wcześniej na Androidzie otwierało się tylko dla śledzonych,
  czyli akurat nie dla tych kafelków, które najbardziej chce się usunąć.
- Menu zawiera „❌ Usuń produkt ze spiżarni na stałe”; wybór pokazuje
  pytanie potwierdzające, a po potwierdzeniu kafelek znika z siatki.
- Usunięty produkt nie wraca po ponownym wejściu na ekran Spiżarni ani po
  restarcie aplikacji, mimo że nadal jest składnikiem jakichś przepisów.
- Gdy jest co najmniej jeden ukryty produkt, na górze Spiżarni widać
  „↩️ Przywróć usunięte produkty (N)”; przycisk przywraca wszystkie jako
  nieśledzone kafelki (bez stanu).
- Ręczne dodanie produktu o tej samej nazwie („Dodaj własny”, „Mam to”)
  zdejmuje go z listy ukrytych.
- Ukrycie zrobione na jednym urządzeniu dociera do drugiego przez zwykłą
  synchronizację konta (patrz FR-73 i jego rewizja v8).
- `./gradlew :logic:test :app:assembleDebug` przechodzi.

## Historia rewizji
- **v1** (2026-08-29): Pierwsza wersja, obie platformy w tej samej turze.
  Na webie istniała wcześniej wąska wersja tej funkcji — „❌ Usuń ten
  kafelek na stałe” działające WYŁĄCZNIE dla kafelków dodanych ręcznie
  przez użytkownika (`state.customTiles`); została zastąpiona wersją
  działającą dla każdego kafelka. Zweryfikowane testami jednostkowymi
  (`PantryOperationsTest`, `CloudSyncCodecTest`) i kompilacją; weryfikacja
  wizualna na emulatorze — patrz `android/PARITY.md`.

---

# FR-103: Stopniowany gest przesuwania na kartach „Dzisiejszy Planer”

**Obszar:** Planer (motyw Klinika), Android + Web
**Status:** Zaimplementowane na obu platformach

## Opis
Gest przesuwania karty dania w sekcji „Dzisiejszy Planer” został
przebudowany: o wyniku decyduje teraz **odległość** przesunięcia, a nie
tylko jego kierunek.

| Gest | Wynik |
|---|---|
| → krótkie przesunięcie w prawo | 🍳 **Zrobione** — wpis w historii gotowania + odjęcie składników ze spiżarni |
| →→ długie przesunięcie w prawo | 🍽️ **Zjedzone** — cała porcja |
| ← krótkie przesunięcie w lewo | ½ **Zjedzone w połowie** — liczy się połowa kcal |
| ←← długie przesunięcie w lewo | ↩️ **Cofnij wszystko** — nie zjedzone + cofnięcie „zrobione” (składniki wracają do spiżarni) |

Poprzednia wersja (FR-87/v14) miała tylko dwa wyniki — dowolne
przesunięcie w prawo = zjedzone, w lewo = nie zjedzone. Gest niósł więc
jeden bit informacji, podczas gdy Planer śledzi trzy niezależne rzeczy
(czy zrobione / czy zjedzone / ile zjedzone). Stąd zgłoszenie z
2026-08-29: „coś nie do końca łapię mi przesuwanie w prawo i w lewo dań w
dzisiejszym planerze” — nie było jak powiedzieć „ugotowałem, ale jeszcze
nie zjadłem”, ani jak poprawić przypadkowe przesunięcie.

W trakcie przesuwania karta **na bieżąco nazywa** akcję, którą wykona po
puszczeniu palca (pigułka z napisem „🍳 Zrobione” / „🍽️ Zjedzone” /
„½ Zjedzone w połowie” / „↩️ Cofnij wszystko”), a jej tło stopniowo
nasyca się kolorem tej akcji, więc widać, że gest „domyka się” głębiej.
Pod nagłówkiem „Dzisiejszy Planer” jest jednolinijkowa ściągawka z
czterema wynikami — bez niej gest nie byłby odkrywalny.

Stan karty (w odróżnieniu od tego, co gest *zrobi*) pokazują znaczniki:
przekreślona nazwa dania = zjedzone w całości, plakietka „½ Zjedzone w
połowie” = połowa porcji (nazwa NIE jest przekreślona — danie nie jest
skończone), plakietka „🍳 Zrobione” = danie ma dziś wpis w historii
gotowania. Przy połowie porcji kafelek kcal pokazuje „300 / 600 kcal”.

Wprowadzenie połowy porcji wymagało rozszerzenia zapisu zjedzonego
posiłku o pole `portion` (0–1) obok istniejących `done`/`kcal`/`name`.
Wpisy zapisane wcześniej (i przez urządzenia z wcześniejszą wersją) nie
mają tego pola i czytają się jako pełna porcja, więc historia kalorii
sprzed tej zmiany nie zmienia się ani o kcal.

## Kryteria akceptacji
- Przesunięcie karty poniżej progu (~36 dp) nie robi nic i nie pokazuje
  żadnej etykiety.
- Krótkie przesunięcie w prawo dodaje wpis „zrobione dzisiaj” i odejmuje
  składniki ze spiżarni — dokładnie to samo, co przycisk „✅ Zrobione
  dzisiaj” na karcie przepisu.
- Powtórzone krótkie przesunięcie w prawo na tym samym daniu tego samego
  dnia NIE odejmuje składników drugi raz — pokazuje komunikat, że danie
  jest już oznaczone jako zrobione.
- Długie przesunięcie w prawo oznacza całą porcję jako zjedzoną
  (pierścień kcal rośnie o pełne kcal dania).
- Krótkie przesunięcie w lewo oznacza połowę porcji: pierścień kcal
  rośnie o połowę kcal dania, karta pokazuje plakietkę „½”.
- Długie przesunięcie w lewo czyści kartę: danie nie jest zjedzone, a
  jeśli było dziś oznaczone jako zrobione — wpis znika, a składniki
  wracają do spiżarni.
- Wszystkie cztery akcje są idempotentne i niezależne od kolejności.
- Zwykłe stuknięcie karty nadal otwiera podgląd przepisu (bez zmian).
- Odczyt kcal dnia dla wpisów bez pola `portion` jest identyczny jak
  przed zmianą.
- `./gradlew :logic:test :app:assembleDebug` przechodzi
  (`PlannerSwipeTest`, `EatenOperationsTest`, `CookHistoryOperationsTest`).

## Historia rewizji
- **v1** (2026-08-29): Pierwsza wersja, obie platformy w tej samej turze.
  Zastępuje dwustanowy gest z FR-87/v14. Mapowanie odległość→akcja
  wydzielone do czystej, testowanej jednostkowo logiki (`PlannerSwipe` w
  `android/logic/`, `pdSwipeAction()` na webie), żeby żywa etykieta, żywe
  tło i obsługa puszczenia palca nie mogły się rozjechać co do znaczenia
  bieżącego gestu.


- **v2** (2026-08-29, PRZEBUDOWA po odklikaniu v1): gest przestał wybierać
  akcję na podstawie ODLEGŁOŚCI przesunięcia, a zaczął przechodzić KROK po
  kroku przez cykl życia dania. Zgłoszenie: „jedno przesunięcie to zrobione
  i wtedy podświetla na zielono że gotowe do zjedzenia a drugie takie samo
  przesunięcie niech skreśla i wyszarza delikatnie jako oznaczenie że
  zjedzone (…) cofnięcie niech cofa odejmowanie zarówno kalorii tak jak
  teraz jak i rzeczy do spiżarni”.

  Nowy model: `nic → zrobione → zjedzone` w prawo, dokładnie odwrotnie w
  lewo. Krok naprzód odejmuje (składniki przy „zrobione”, kalorie przy
  „zjedzone”), krok wstecz oddaje dokładnie to, co jego odpowiednik zabrał.
  Karta na bieżąco nazywa krok, który wykona, a gdy w danym kierunku nie ma
  już dokąd iść, mówi to wprost („✓ już zjedzone” / „— nic do cofnięcia”)
  zamiast przesuwać się bez efektu.

  **Wygląd stanu** (też z prośby): „zrobione” = zielone tło karty (gotowe do
  zjedzenia), „zjedzone” = przekreślona nazwa i delikatnie wyszarzona cała
  karta.

  **Czułość** („żeby to przesuwanie było bardziej czułe”): jeden próg
  zamiast dwóch, i to niski (30 dp). Skoro jeden krok to jeden krok
  niezależnie od tego, jak daleko pojedzie palec, nie ma powodu wymagać
  długiego przeciągnięcia — wystarczy odróżnić przesunięcie od stuknięcia.

  **Naprawiony realny błąd trafiania w gest (Web)**: `pointerdown`
  ignorował gest zaczęty na DOWOLNYM `<button>`, a znacznik spiżarni
  (FR-97) stał się przyciskiem w ŚRODKU karty — więc przeciąganie
  rozpoczęte gdziekolwiek koło środka po cichu nic nie robiło i trzeba było
  łapać kartę przy krawędzi. Dokładnie to zgłosił użytkownik („muszę od
  krawędzi złapać ten prostokąt (…) niech działa przynajmniej od połowy
  kafelka”). Teraz wyjątkiem jest tylko przycisk „✕” (usuń z planu);
  stuknięcie znacznika spiżarni nadal działa, bo stuknięcie nigdy nie
  przekracza progu blokady osi.

  **Naprawiony przesuwający się dolny pasek**: przesuwana karta wyjeżdżała
  poza szerokość widoku, przez co dokument dostawał poziome przewijanie i
  wszystko zakotwiczone do okna — w tym pływający dolny pasek nawigacji —
  jechało razem z palcem. Na webie naprawione przycięciem listy
  (`overflow-x: clip`), na Androidzie `Modifier.clipToBounds()` na slocie
  karty. Karta nadal przejeżdża pełny dystans, po prostu nie może już
  poszerzyć niczego wokół siebie.

  **Połowa porcji wyprowadzona z gestu** do przytrzymania — patrz FR-105.
  Zweryfikowane na emulatorze: cztery kolejne przesunięcia dały 0/1480 →
  zielona karta „🍳 Zrobione” → 345/1480 z przekreśleniem i wyszarzeniem →
  z powrotem, a dolny pasek nie drgnął.


- **v3** (2026-08-29): gest odróżnia teraz stuknięcie, które się
  „poślizgnęło”, od świadomego krótkiego przeciągnięcia. Prawdziwy palec
  nigdy nie stuka idealnie nieruchomo — na telefonie trzymanym w jednej ręce
  rutynowo przesuwa się o 20–40 px — a obie te karty mają własną akcję na
  stuknięcie (podgląd przepisu / wybór dania). Bez zabezpieczenia niechlujne
  stuknięcie mogło przesunąć danie o krok, a to jedyny rodzaj pomyłki, który
  realnie kosztuje użytkownika: odjęcie składników ze spiżarni, o które nie
  prosił.

  Sam dystans tego nie rozdziela (nałożenie wypada dokładnie w paśmie
  30–60 px), a sam czas też nie: zdecydowany „flick” kończy się w 80–100 ms,
  więc reguła „krótkie naciśnięcie = stuknięcie” połykałaby dokładnie ten gest,
  który robi się odruchowo, gdy się już funkcji ufa. Stąd trzy pasma:

  | dystans | czas | wynik |
  |---|---|---|
  | < 30 dp | dowolny | nic |
  | 30–60 dp | < 150 ms | stuknięcie (gest zignorowany) |
  | 30–60 dp | ≥ 150 ms | krok |
  | ≥ 60 dp | dowolny | krok |

  Ta sama funkcja (`PlannerSwipe.commitDirection` / `pdCommitDirection`)
  decyduje o żywej etykiecie I o puszczeniu palca, więc karta nie może
  obiecać kroku, którego potem odmówi.

  Zweryfikowane na emulatorze i w Chrome, po trzy przypadki na platformę:
  105 px w 90 ms → nic; te same 105 px w 350 ms → „Zrobione”; 260 px w 60 ms
  → „Zjedzone”. Testy jednostkowe (`PlannerSwipeTest`) pokrywają wszystkie
  trzy pasma plus własność, że zabezpieczenie tylko ZAWĘŻA — nigdy nie
  zatwierdza tam, gdzie sam dystans by nie zatwierdził.

---

# FR-104: Gest „zrobione/zjedzone” także na kartach dni tygodnia

**Obszar:** Planer (motyw Klinika), Android + Web
**Status:** Zaimplementowane na obu platformach

## Opis
Ten sam krokowy gest, który FR-103 wprowadził w sekcji „Dzisiejszy Planer”,
działa teraz również na wierszach posiłków w **kartach dni tygodnia** niżej
na ekranie Planera. Przesunięcie w prawo przesuwa danie o krok naprzód
(nic → zrobione → zjedzone), w lewo — o krok wstecz, z cofnięciem tego, co
krok naprzód odjął (składniki ze spiżarni, kalorie z licznika).

Zgłoszenie: „dodaj też gest na kartach dni jak proponujesz” (2026-08-29).
Wcześniej, żeby oznaczyć wczorajszy obiad jako zjedzony, trzeba było iść do
zakładki Postęp i przełączać checkboxy — gest istniał wyłącznie dla dzisiaj.

**Most między tygodniem a kalendarzem.** Planer jest powtarzalnym szablonem
tygodniowym indeksowanym 0–6 (poniedziałek–niedziela), a „zjedzone” i
„zrobione” są zapisywane per konkretna DATA. Te dwie rzeczy muszą się gdzieś
spotkać — robią to w jednym miejscu (`dateForDayIndex()` na webie,
`dateForDayIndex` w `PlannerScreen` na Androidzie): dzień o indeksie N
oznacza „ten dzień tygodnia w tygodniu zawierającym dzisiaj”. Data jest
liczona z lokalnych składowych, nie z UTC — patrz FR-101.

Puste sloty (bez zaplanowanego dania) zachowują dotychczasowe zachowanie:
stuknięcie otwiera wybór dania, gest ich nie dotyczy, bo nie mają stanu, po
którym można się przesuwać.

## Kryteria akceptacji
- Wiersz posiłku w karcie dnia z zaplanowanym daniem reaguje na przesunięcie
  w prawo/lewo tak samo jak karta w „Dzisiejszym Planerze”.
- Stan pokazany na karcie dnia i w „Dzisiejszym Planerze” dla tego samego
  dania w tym samym dniu jest zawsze taki sam (oba czytają ten sam zapis,
  żaden nie trzyma własnej kopii).
- Oznaczenie „zrobione” na karcie np. wtorku zapisuje wpis w historii
  gotowania z datą wtorku tego tygodnia, nie dzisiejszą.
- Puste sloty nadal otwierają wybór dania stuknięciem i nie reagują na gest.
- Przesunięcie wiersza nie porusza niczego poza nim (patrz FR-103, ten sam
  problem z przycinaniem).
- Zwykłe stuknięcie wiersza z daniem nadal otwiera wybór dania — przesunięcie
  nie liczy się jako stuknięcie.
- `./gradlew :logic:test :app:assembleDebug` przechodzi.

## Historia rewizji
- **v1** (2026-08-29): Pierwsza wersja, obie platformy w tej samej turze.
  Wymagała uogólnienia zapisu „zrobione” z „dzisiaj” na dowolną datę
  (`cookedOnDateIndex`/`markRecipeCookedOnDate`/`undoCookedOnDate` na webie,
  `CookHistoryOperations.cookedOnDateIndex`/`addOnDate` +
  `RecipeViewModel.isCookedOn/markCookedOn/undoCookedOn` na Androidzie) oraz
  zapisu „zjedzone” (`EatenViewModel.setEatenOnDate`/`entriesForDate`).
  Zweryfikowane na emulatorze: przesunięcie w lewo na wierszu „Śniadanie” w
  karcie Soboty cofnęło danie ze stanu „zjedzone” do „zrobione”, a karta w
  „Dzisiejszym Planerze” pokazywała dokładnie ten sam stan przed i po.

---

# FR-105: Dowolna wielkość zjedzonej porcji

**Obszar:** Planer (motyw Klinika), Android + Web
**Status:** Zaimplementowane na obu platformach

## Opis
Przytrzymanie karty dania (w „Dzisiejszym Planerze” albo w karcie dnia
tygodnia) otwiera wybór, **ile z tego dania faktycznie zostało zjedzone**:
suwak 0–100% plus cztery gotowe wielkości (¼, ½, ¾, cała porcja). Licznik
kalorii liczy dokładnie tyle, ile zaznaczono — okienko pokazuje tę liczbę na
żywo, jeszcze przed zapisaniem.

Zgłoszenie: „dodaj opcje dowolnej porcji jak proponujesz” (2026-08-29).

Pole `portion` (0–1) istniało na wpisie zjedzonego posiłku od FR-103 — to
wymaganie dokłada wyłącznie brakujący interfejs do niego. Model danych i
sposób liczenia kalorii nie zmieniają się ani trochę, więc historia sprzed
tej zmiany zostaje bez zmian.

Wybranie 0% oznacza „nic nie zjedzone”, czyli kasuje oznaczenie zjedzenia —
a nie zapisuje „zjedzone, ale zero”. Bez tego karta mogłaby wylądować w
stanie, którego kroki gestu z FR-103 nie rozpoznają.

**Dlaczego przytrzymanie, a nie gest.** FR-103 w pierwszej wersji próbował
zmieścić „pół porcji” w przesunięciu w lewo. Użytkownik poprosił, żeby gest
znaczył jeden krok naprzód/wstecz, i słusznie: gest, który znaczy „jeden
krok”, nie może jednocześnie znaczyć „62% porcji” bez powrotu do zgadywania
odległości. Wielkość porcji jest z natury wartością ciągłą, więc dostała
element, który potrafi ją wyrazić — suwak.

## Kryteria akceptacji
- Przytrzymanie karty dania otwiera okienko z suwakiem i czterema gotowymi
  wielkościami; zwykłe stuknięcie nadal robi to, co robiło (podgląd przepisu
  w „Dzisiejszym Planerze”, wybór dania w karcie dnia).
- Okienko pokazuje na żywo procent i odpowiadającą mu liczbę kalorii.
- Zapisanie wartości > 0 oznacza danie jako zjedzone w tej części; licznik
  dnia rośnie dokładnie o tyle kalorii.
- Zapisanie 0% oznacza danie jako niezjedzone.
- Karta pokazuje plakietkę z wielkością porcji, gdy jest ona mniejsza niż
  cała (np. „¼ porcji zjedzone”), i kafelek kcal w formie „86 / 345 kcal”.
- Nazwy okrągłych ułamków (¼, ½, ¾, cała) są słowne; dowolna inna wartość
  z suwaka pokazuje się jako procent.
- Historia kalorii dla wpisów bez pola `portion` jest identyczna jak przed
  zmianą.
- `./gradlew :logic:test :app:assembleDebug` przechodzi (`PortionText`,
  `EatenOperationsTest`).

## Historia rewizji
- **v1** (2026-08-29): Pierwsza wersja, obie platformy w tej samej turze.
  Nazewnictwo wielkości porcji i przeliczanie kalorii wydzielone do wspólnej,
  testowanej logiki (`PortionText` w `android/logic/`, `PORTION_PRESETS`/
  `portionLabel()` na webie), żeby obie platformy mówiły to samo o tej samej
  liczbie. Zweryfikowane na emulatorze: przytrzymanie karty otworzyło suwak
  ustawiony na 100% · 345 kcal, wybór „¼ porcji” i zapis dał 86/1480 kcal na
  pierścieniu, plakietkę „¼ porcji zjedzone” i kafelek „86 / 345 kcal”.

---

# FR-106: Propozycja przeniesienia zakupów do spiżarni

**Obszar:** Lista zakupów, Android + Web
**Status:** Zaimplementowane na obu platformach

## Opis
Kiedy odhaczysz **ostatnią** pozycję potrzebną do jakiegoś dania, aplikacja
pokazuje powiadomienie: „Masz już wszystko na «nazwa dania»" z przyciskiem
**„Do spiżarni"**. Po jego naciśnięciu składniki tego dania trafiają do
spiżarni w ilościach z przepisu.

Do tej pory odhaczenie pozycji na liście zakupów przełączało wyłącznie
znacznik „kupione" — nic nie docierało do spiżarni. Spiżarnię trzeba było
wypełnić ręcznie, kafelek po kafelku, mimo że aplikacja właśnie dowiedziała
się, co zostało kupione. To odcinało dwie funkcje, które na spiżarni stoją:
znacznik „🏺 N/M w spiżarni" na kartach Planera i gest „🍳 Zrobione", który
odejmuje składniki.

**Dlaczego propozycja, a nie automat.** Kupienie to nie gotowanie, a rzeczy
bywają odkładane z powrotem na półkę. Ciche dopisywanie do spiżarni przy
każdym odhaczeniu byłoby i hałaśliwe, i czasem po prostu nieprawdziwe.
Powiadomienie pojawia się raz na danie — dokładnie w chwili, w której
komplet jest kompletny — i nic nie robi bez naciśnięcia.

**Dlaczego „do spiżarni", a nie „zrobione".** Pierwotny pomysł brzmiał: zapytać,
czy danie zostało ugotowane. Przy pisaniu okazał się niepoprawny: odhaczenie
listy zakupów znaczy „kupiłem", a nie „ugotowałem", a oznaczenie „zrobione"
ODJĘŁOBY ze spiżarni to, co użytkownik właśnie kupił. Właściwą akcją w tym
momencie jest napełnienie spiżarni; ugotowanie zostaje tam, gdzie było — w
geście na karcie Planera (FR-103).

## Kryteria akceptacji
- Propozycja pojawia się dokładnie wtedy, gdy odhaczenie było **ostatnim**
  brakującym składnikiem danego dania — nie przy każdym odhaczeniu.
- Składnik wspólny dla dwóch dań domyka tylko to danie, któremu nic już nie
  brakuje.
- Naciśnięcie „Do spiżarni" dodaje składniki w ilościach i jednostkach z
  przepisu, **tworząc** pozycje, których w spiżarni nie było.
- Powtórzone dodanie tego samego dania sumuje ilości, zamiast tworzyć
  duplikaty.
- Pozycja śledzona w spiżarni w innej jednostce niż przepis (spiżarnia w
  „szt.", przepis w gramach) zostaje nietknięta, a nie zgadywana.
- Danie, któremu nie zostało nic na liście, NIE jest zgłaszane jako kupione —
  usunięcie ostatniej pozycji to nie to samo co jej kupienie.
- Bez naciśnięcia przycisku nic się nie dzieje.

## Uwagi
Rozpoznanie „to danie jest kupione w całości" jest **wyprowadzone** z pola
`contributions`, które od dawna zapisuje, który przepis wstawił daną pozycję
na listę — nie doszedł żaden nowy zapis w stanie aplikacji.

Dodawanie do spiżarni celowo NIE korzysta z istniejącego
`restoreRecipeToPantry`/`restoreForRecipe`. Tamta funkcja tylko uzupełnia
pozycje, które już istnieją, bo służy do cofania odejmowania — a nie da się
odjąć od czegoś, czego nigdy nie było. Tutaj sytuacja jest odwrotna:
składniki warte dodania to dokładnie te, których użytkownik NIE miał.

## Historia rewizji
- **v1** (2026-08-29): Pierwsza wersja, obie platformy w tej samej turze.
  Logika wydzielona i pokryta testami (`ShoppingOperations.fullyBoughtRecipes`,
  `RecipePantryMatching.stockFromRecipe`). Zweryfikowane na żywo w Chrome:
  przepis o 5 składnikach zgłoszony jako kupiony dopiero po piątym odhaczeniu,
  przyjęcie propozycji utworzyło 5 pozycji w spiżarni z właściwymi ilościami i
  jednostkami, a powtórne dodanie podniosło ilość 150 → 300 zamiast tworzyć
  drugi wpis. Po stronie Androida: kompiluje się, logika ma testy, wpięcie jest
  lustrzane wobec zweryfikowanej wersji webowej — **ale przejście przez UI na
  emulatorze nie zostało dokończone** (dotknięcia checkboxów listy nie
  rejestrowały się w narzędziu; to ograniczenie sposobu testowania, nie
  stwierdzona wada funkcji).
- **2026-08-30**: dokończona weryfikacja UI na emulatorze (przerwana
  poprzednio). Przepis o 6 składnikach ("Szybki omlet kokosowy z cynamonem i
  borówkami") dodany do listy zakupów, odhaczony do końca — powiadomienie
  „Masz już wszystko na ...” pojawiło się dokładnie po ostatnim składniku, a
  „Do spiżarni” utworzyło/zaktualizowało wszystkie 6 pozycji z poprawnymi
  ilościami: borówki 1, jajka 1→3 (zsumowane z istniejącym wpisem, nie
  zdublowane), mąka 1, cynamon 0,5, olej kokosowy 1, wiórki kokosowe 1.
  Zachowanie identyczne z wersją webową. Bez zmian w kodzie — to była czysto
  domknięta weryfikacja.

---

# FR-107: Zapamiętana wielkość porcji dla danego dania

**Obszar:** Planer (motyw Klinika), Android + Web
**Status:** Zaimplementowane na obu platformach

## Opis
Okienko wyboru porcji (FR-105, otwierane przytrzymaniem karty dania) otwiera
się teraz tam, gdzie ta osoba **zwykle ląduje przy tym konkretnym daniu**, i
mówi to wprost: „📊 Zwykle zjadasz ½ porcji tego dania".

Kolejność, według której ustawia się suwak:

1. to, co już jest zapisane na dziś dla tego slotu (użytkownik poprawia
   wcześniejszy wpis),
2. zapamiętany nawyk dla tego dania,
3. cała porcja.

Dane już były — pole `portion` zapisuje się przy każdym użyciu okienka od
FR-105, a `name` mówi, jakiego dania dotyczył wpis. Nic nowego nie jest
przechowywane; to wymaganie tylko **odczytuje** to, co i tak było zapisywane.
Ktoś, kto stale zjada połowę danej kolacji, nie musi już mówić o tym za
każdym razem.

**Dwie świadome powściągliwości**, obie o tym, żeby nie być męczącym:

- **Potrzebne są co najmniej dwa zapisane posiłki.** Jedna połówka to
  okazja, nie nawyk — przedstawianie jej jako nawyku sprawiałoby wrażenie, że
  aplikacja zgaduje.
- **Cała porcja nigdy nie jest zgłaszana jako „zwykła".** To i tak wartość
  domyślna, więc mówienie tego na głos byłoby czystym szumem przy każdym
  daniu, które ktoś po prostu zjada w całości.

Przy remisie (np. dwa razy połowa i dwa razy ćwiartka) wygrywa wartość
**ostatnio** użyta — to, co ktoś zrobił poprzednim razem, jest lepszą
podpowiedzią niż wybór arbitralny.

## Kryteria akceptacji
- Po dwóch zapisanych połówkach tego samego dania okienko otwiera się na 50%
  i pokazuje podpowiedź.
- Jedno wystąpienie nie wystarcza — brak podpowiedzi, suwak na 100%.
- Danie zawsze zjadane w całości nie pokazuje podpowiedzi.
- Wpisy innych dań nie wpływają na to danie.
- Wpisy oznaczone jako niezjedzone są pomijane.
- Jeśli na dziś jest już zapisana porcja, okienko otwiera się na niej —
  poprawianie wpisu ma pierwszeństwo przed nawykiem.
- Podpowiedź nazywa okrągłe ułamki słownie (¼, ½, ¾), a każdą inną wartość
  procentowo.

## Historia rewizji
- **v1** (2026-08-29): Pierwsza wersja, obie platformy w tej samej turze.
  Logika wydzielona do `PortionHistory` (moduł `logic`) z ośmioma testami
  pokrywającymi próg dwóch wystąpień, pomijanie całej porcji, remis
  rozstrzygany ostatnim wystąpieniem, rozdzielenie dań i pomijanie wpisów
  niezjedzonych. Zweryfikowane na żywo w Chrome: po dwóch zapisanych
  połówkach okienko otworzyło się na „50% · 160 kcal" (połowa z 320) z
  podpowiedzią „Zwykle zjadasz ½ porcji tego dania"; nieznane danie nie
  zwraca nic. Po stronie Androida kompiluje się i korzysta z tej samej
  logiki — **wariant „z historią" nie był odklikany na emulatorze**, bo
  wymagałby wpisów z dwóch różnych dni, czego nie da się wyklikać bez
  przestawiania daty urządzenia.

---

# FR-108: Ostrzeżenie, że produktu nie starczy na zaplanowane dania

**Obszar:** Spiżarnia, Android + Web
**Status:** Zaimplementowane na obu platformach

## Opis
Spiżarnia mówi teraz wprost, czego **nie starczy** na dania zaplanowane do
końca tygodnia. Na górze ekranu pojawia się czerwona karta:

> ⚠️ Nie starczy na zaplanowane dania (1)
> skyr bez laktozy — masz 38 g, trzeba 150 g
> na 1 danie: Nocna owsianka białkowa z borówkami i migdałami

a sam kafelek takiego produktu dostaje czerwoną obwódkę i znacznik „⚠” przy
ilości — bo zanim użytkownik dojdzie wzrokiem do produktu, karta z góry
zdąży zniknąć za krawędzią ekranu.

Do tej pory brak wychodził na jaw dopiero przy gotowaniu. Znacznik
„🏺 N/M w spiżarni” na kartach Planera (FR-16) odpowiada na pytanie o
**obecność** („czy jest jakikolwiek ryż”), nigdy o **ilość** — więc 20 g ryżu
i 2 kg ryżu wyglądają identycznie aż do chwili, w której danie zostaje
oznaczone jako zrobione, a odejmowanie z FR-15 przycina stan do zera.

**Trzy świadome ograniczenia**, każde o tym, żeby nie krzyczeć na wyrost:

1. **Liczą się tylko produkty śledzone.** Składnik bez wpisu w spiżarni to
   nie brak, tylko coś, czego użytkownik nie śledzi — a takich kafelków jest
   około dwustu. Zgłaszanie ich przykryłoby tę garstkę, którą ktoś naprawdę
   prowadzi, i powtarzałoby to, co i tak robi lista zakupów.
2. **Liczą się tylko posiłki jeszcze przed nami.** Dni sprzed dzisiaj są
   zamknięte, a danie oznaczone już jako „🍳 zrobione” w swoim dniu **ma już
   odjęte** składniki (FR-15/FR-103) — policzenie go drugi raz wymyśliłoby
   brak z kolacji, która jest zjedzona.
3. **Niezgodne jednostki są pomijane, nie zgadywane.** Produkt śledzony w
   „szt." nie da się porównać z przepisem liczonym w gramach — ta sama
   ostrożna zasada, którą stosuje już `missingAfterPantry()` i znacznik z
   FR-16.

Przyprawy są wykluczone z definicji: Mało/Wystarczy/Dużo nie jest ilością,
więc nie ma od czego odejmować.

Kolejność na liście: **największy względny brak najpierw** — „nie ma nic, a
potrzebują tego trzy dania” waży więcej niż „jest 9 z 10 jajek”. Karta
domyślnie pokazuje trzy najgorsze pozycje; reszta jest o jedno stuknięcie
dalej, bo to ma być zachęta przed zakupami, a nie raport magazynowy.

## Kryteria akceptacji
- Produkt śledzony w ilości mniejszej niż suma potrzebna na dania
  zaplanowane od dziś do niedzieli pojawia się na karcie i ma oznaczony
  kafelek.
- Dokładnie wystarczająca ilość nie jest brakiem.
- Kilogramy i litry w spiżarni są porównywane z gramami/mililitrami przepisu
  (1 kg pokrywa 200 g).
- Skala porcji (1×–2×) zwiększa potrzebę proporcjonalnie.
- Ten sam składnik z dwóch dań sumuje się i wymienia oba dania; to samo
  danie zaplanowane dwa razy liczy się podwójnie, ale jest wymienione raz.
- Składnik bez wpisu w spiżarni nie jest zgłaszany.
- Przyprawa (poziom zamiast ilości) nie jest zgłaszana.
- Wpis w „szt." nie jest porównywany z gramami.
- Dni sprzed dzisiaj nie są liczone.
- Danie już oznaczone jako zrobione w swoim dniu nie jest liczone; cofnięcie
  tego oznaczenia przywraca zarówno stan spiżarni, jak i ostrzeżenie.
- Liczba dań jest odmieniona po polsku („na 1 danie”, „na 2 dania”,
  „na 5 dań”, „na 12 dań”, „na 22 dania”).

## Historia rewizji
- **v1** (2026-08-30): Pierwsza wersja, obie platformy w tej samej turze.
  Logika wydzielona do `PantryShortage` (moduł `logic`) z czternastoma
  testami pokrywającymi wszystkie trzy ograniczenia, przeliczanie kg/l,
  skalę porcji, sumowanie i sortowanie. Zweryfikowane na żywo w Chrome na
  prawdziwych danych: przy 38 g skyru i przepisie na 150 g karta pokazała
  „masz 38 g, trzeba 150 g”, kafelek dostał obwódkę i znacznik „⚠ 38 g”;
  uzupełnienie do 500 g, usunięcie śledzenia, zmiana jednostki na „szt."
  oraz oznaczenie dania jako zrobione — każde z osobna wyciszało ostrzeżenie
  (po ugotowaniu stan spadł do 0, a mimo to nie zgłoszono braku), a
  cofnięcie „zrobione” przywróciło i stan 38 g, i ostrzeżenie. Skala 2×
  podniosła potrzebę ze 150 g na 300 g.

---

# FR-109: Przeniesienie zaplanowanego dania na inny dzień

**Obszar:** Planer, Android + Web
**Status:** Zaimplementowane na obu platformach

## Opis
Przy każdym zaplanowanym daniu — na karcie „Dzisiejszy Planer" i w każdym
wierszu kart dni tygodnia — jest przycisk **📅**. Otwiera listę dni, na
które można to danie przenieść (w ten sam slot: śniadanie zostaje
śniadaniem).

Każdy dzień na liście jest podpisany tym, co już w tym miejscu ma:

> Poniedziałek
> **Wtorek ⇄ Pasta z wędzonego łososia i serka bez laktozy na chlebie**
> Środa

To jedyna rzecz, którą trzeba wiedzieć przed stuknięciem — dzień z „⇄"
oznacza **zamianę**, nie nadpisanie.

Do tej pory jedynym sposobem przesunięcia dania było usunięcie go i wybranie
od nowa z listy. Przy okazji ginęła **wielkość porcji** (1×–2×) i znacznik
**„🍱 resztki"**, bo nowy wybór zawsze startuje od wartości domyślnych. Tutaj
oba jadą razem z daniem.

**Dlaczego zamiana, a nie nadpisanie.** Nadpisanie po cichu skasowałoby
danie, które ktoś świadomie zaplanował, i po fakcie nie zostaje na ekranie
nic, po czym można by tę stratę zauważyć. Zamiana jest zawsze odwracalna
powtórzeniem tego samego ruchu — i zwykle właśnie o to chodzi przy
przestawianiu tygodnia („te dwa niech się zamienią miejscami"). Z tego samego
powodu „Cofnij" to dokładnie ten sam ruch w drugą stronę, a nie osobna
ścieżka odtwarzania.

Przeniesienie dnia na siebie samego albo pustego slotu nie robi nic.

## Kryteria akceptacji
- Przycisk 📅 pojawia się tylko przy slotach, które faktycznie mają danie.
- Przeniesienie na pusty dzień zostawia dzień źródłowy pusty.
- Przeniesienie na zajęty dzień zamienia dania miejscami — żadne nie ginie.
- Wielkość porcji i znacznik „resztki" wędrują razem z daniem (w obie strony
  przy zamianie).
- „Cofnij" przywraca stan sprzed przeniesienia, w tym po zamianie oba dania.
- Lista dni podpisuje zajęte dni nazwą dania, które tam stoi.
- Stuknięcie przycisku nie otwiera podglądu przepisu ani nie zmienia dania
  (nie „przecieka" do wiersza pod spodem), a przeciągnięcie zaczęte na tym
  przycisku nie uruchamia gestu zrobione/zjedzone.

## Historia rewizji
- **v1** (2026-08-30): Pierwsza wersja, obie platformy w tej samej turze.
  Logika w `PlannerOperations.moveMeal` (moduł `logic`) z pięcioma testami:
  przeniesienie ze skalą i znacznikiem resztek, zamiana zamiast nadpisania,
  powtórzenie ruchu jako cofnięcie zamiany, nietykanie innych slotów i dni,
  brak zmian przy pustym slocie i dniu na samego siebie. Zweryfikowane na
  żywo w Chrome (zamiana z zachowaniem skali 1,5× i znacznika resztek,
  cofnięcie, przeniesienie na pusty dzień, przyciski tylko przy zapełnionych
  slotach w obu wariantach kart dni) oraz na emulatorze Androida
  (okno „Przenieś na inny dzień" z podpisanymi dniami, zamiana z powiadomieniem
  „Zamieniono z Wtorek" i działającym „Cofnij" po obu stronach).

---

# FR-110: Realizacja tygodnia — ile z planu faktycznie zjedzone

**Obszar:** Planer (karta „📊 Zaplanowany tydzień"), Android + Web
**Status:** Zaimplementowane na obu platformach

## Opis
Na dole karty „📊 Zaplanowany tydzień" (FR-100) dochodzi jedna linia:

> ✅ Zrealizowane: 1 z 5 posiłków (20%)
> licząc dni do dziś włącznie — to, co jeszcze przed Tobą, nie liczy się na minus

Cała reszta tej karty mówi, jak tydzień **miał** wyglądać: średnie kalorie,
makro, liczba zaplanowanych dni. Nigdzie — ani tu, ani gdzie indziej — nie
było powiedziane, czy tak wyszło.

**Dwie decyzje sprawiają, że ta liczba jest w ogóle warta pokazania:**

1. **Liczą się tylko dni do dzisiaj włącznie.** Mierzenie względem całego
   tygodnia pokazywałoby w poniedziałek wieczorem „realizację 13%" komuś, kto
   trzyma się planu idealnie — czyli karałoby za kalendarz, a nie za cokolwiek,
   co ta osoba zrobiła.
2. **Liczą się tylko sloty zaplanowane.** Zjedzenie czegoś spoza planu nie jest
   niewykonaniem planu, a wliczanie tego pozwoliłoby, żeby dzień przekąsek
   czytał się jak dzień trzymania się planu.

Kiedy dla dni, które już były, nic nie było zaplanowane, linia nie pojawia się
w ogóle — zamiast pokazywać „0 z 0".

Powyżej 70% liczba jest wyróżniona kolorem — to nie ocena, tylko potwierdzenie
tego, co i tak widać.

## Kryteria akceptacji
- Linia liczy tylko sloty zaplanowane w dniach od poniedziałku do dziś
  włącznie.
- Danie zjedzone w dniu jeszcze przed nami nie podnosi liczby.
- Przekąska albo cokolwiek spoza planu nie podnosi liczby.
- Odznaczenie posiłku obniża liczbę z powrotem.
- Brak czegokolwiek zaplanowanego w dniach, które już były = brak linii.
- Procent jest zaokrąglony do pełnych jedności; 2 z 3 to 67%.

## Historia rewizji
- **v1** (2026-08-30): Pierwsza wersja, obie platformy w tej samej turze.
  Logika w `WeekPlanSummary.realization` (moduł `logic`) z pięcioma testami
  (liczenie wyłącznie do dziś, idealny poniedziałek jako 100% a nie ułamek
  tygodnia, nieliczenie posiłków spoza planu, brak wiersza gdy nic jeszcze nie
  było zaplanowane, zaokrąglanie). Zweryfikowane na żywo w Chrome: „0 z 4"
  → po zjedzeniu zaplanowanego obiadu „1 z 4 (25%)" → dodanie przekąski spoza
  planu niczego nie zmieniło → cofnięcie wróciło do „0 z 4"; przy udawanym
  poniedziałku (dania zaplanowane tylko na późniejsze dni) funkcja zwróciła
  `null`, czyli brak wiersza. Na emulatorze Androida karta pokazała
  „✅ Zrealizowane: 1 z 5 posiłków (20%)" wraz z wierszem wyjaśniającym.

---

# FR-111: „Ugotuj na dwa dni” bezpośrednio z wiersza Planera

**Obszar:** Planer, Android + Web
**Status:** Zaimplementowane na obu platformach

## Opis
Przy każdym zaplanowanym daniu — na karcie „Dzisiejszy Planer” i w każdym
wierszu kart dni tygodnia — jest przycisk 🍱, obok istniejącego 📅
(FR-109). Otwiera listę dni, na które można dodać to samo danie jako
resztki (bazowa wielkość porcji, znacznik „🍱 resztki”), bez ponownego
dodawania go do listy zakupów.

Zajęte dni są pokazane w tej liście — z tym, co już tam mają — ale
nieklikalne. W odróżnieniu od 📅 (który przy zajętym dniu zamienia dania
miejscami) tu nie ma niczym zamieniać: to danie ZOSTAJE też na swoim
pierwotnym miejscu, więc jedyna bezpieczna opcja przy zajętym dniu to
w ogóle nic nie robić, a nie nadpisać czyjś świadomy wybór.

**Dlaczego osobny przycisk, skoro „ugotuj na dwa dni” już istniało.**
FR-23 robi dokładnie to samo pod spodem, ale dociera do tego inaczej:
trzeba wejść w szczegóły dania i podnieść skalę porcji do ×2 (albo trafić
na danie rozpoznane słowem kluczowym), a cel jest sztywno ustawiony na
„dwa dni później”. To działa, ale zgłoszony problem brzmiał „nie widzę tej
opcji” — bo faktycznie nie widać jej z samego Planera, dopóki nie
zwiększysz porcji. FR-111 to ten sam mechanizm (`planLeftover`), tylko
wystawiony jako jedno stuknięcie z poziomu wiersza, z wyborem DOWOLNEGO
dnia zamiast sztywnego +2 — patrz „Uwagi” niżej po pełne rozróżnienie od
FR-23 i FR-24.

## Kryteria akceptacji
- Przycisk 🍱 pojawia się przy każdym zaplanowanym daniu, w tych samych
  miejscach co przycisk 📅.
- Otwiera listę wszystkich dni oprócz bieżącego, z etykietą tego, co dany
  dzień już ma w tym samym slocie.
- Dzień z czymś już zaplanowanym w tym slocie jest pokazany, ale
  NIEKLIKALNY — stuknięcie w niego nic nie robi.
- Wybranie pustego dnia dodaje to samo danie w bazowej (×1) wielkości
  porcji, ze znacznikiem „🍱 resztki”, na wybrany dzień — bez usuwania ani
  zmiany oryginalnego wpisu.
- Nie tworzy nowej pozycji na liście zakupów (to samo danie, ta sama
  partia zakupów).
- Akcja ma „Cofnij”.

## Uwagi
Trzy mechanizmy „ugotuj na dwa dni” współistnieją, każdy z innym
wyzwalaczem:
- **FR-23** — ręczny, wymaga podniesienia skali porcji do ×2 lub więcej
  (albo dania rozpoznanego jak w FR-24), cel zawsze `dzień+2`.
- **FR-24** — automatyczny, tylko dla dań rozpoznanych po nazwie jako
  „dobrze się odgrzewające”, cel zawsze `dzień+1`, tylko gdy tamten slot
  jest pusty.
- **FR-111** (to wymaganie) — ręczny, jedno stuknięcie z samego wiersza
  Planera, bez wymogu skalowania porcji ani rozpoznawania nazwy, cel
  DOWOLNY dzień wybrany z listy (jak FR-109).

Wszystkie trzy zapisują dokładnie to samo pod spodem (`planLeftover` w
Androidzie / te same trzy pola stanu Plannera w web) i żadne nie
nadpisuje cudzego wyboru bez akcji użytkownika — nie są ze sobą sprzeczne,
tylko oferują trzy różne drogi do tego samego rezultatu. Zobacz też
FR-109 (na którego wzorcu UI — lista dni z etykietami — oparte jest to
wymaganie) — różnica jest w tym, że FR-109 PRZENOSI/ZAMIENIA (bo to ten
sam plan przełożony na inny dzień), a FR-111 DODAJE (bo to nadal to samo
gotowanie, tylko podwojona partia).

## Historia rewizji
- **v1** (2026-08-30): Pierwsza wersja, obie platformy w tej samej turze.
  `PlannerOperations.cookForTwoDays` (Android, `logic/`, cztery nowe testy
  JUnit) i `cookForTwoDays`/`openCookForTwoDaysModal` (web) — oba
  reużywają istniejący `planLeftover`/plannerLeftover-pisanie zamiast
  duplikować logikę FR-23.
