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

### Planer tygodniowy
- [FR-18: Planer tygodniowy z 5 slotami posiłków dziennie](#fr-18-planer-tygodniowy-z-5-slotami-posiłków-dziennie)
- [FR-19: Wybór innego slotu posiłkowego z poziomu karty przepisu](#fr-19-wybór-innego-slotu-posiłkowego-z-poziomu-karty-przepisu)
- [FR-20: Skalowanie wielkości porcji w planerze](#fr-20-skalowanie-wielkości-porcji-w-planerze)
- [FR-21: Losowe generowanie planu — cały tydzień lub pojedynczy dzień](#fr-21-losowe-generowanie-planu--cały-tydzień-lub-pojedynczy-dzień)
- [FR-22: Czyszczenie planu — cały tydzień lub pojedynczy dzień](#fr-22-czyszczenie-planu--cały-tydzień-lub-pojedynczy-dzień)
- [FR-23: „Ugotuj na 2 dni” — planowanie resztek po zwiększeniu porcji](#fr-23-ugotuj-na-2-dni--planowanie-resztek-po-zwiększeniu-porcji)
- [FR-24: Proaktywna podpowiedź gotowania na kolejny dzień](#fr-24-proaktywna-podpowiedź-gotowania-na-kolejny-dzień)
- [FR-86: Podgląd przepisu z poziomu Planera](#fr-86-podgląd-przepisu-z-poziomu-planera)

### Lista zakupów
- [FR-25: Budowanie listy zakupów ze składników przepisów](#fr-25-budowanie-listy-zakupów-ze-składników-przepisów)
- [FR-26: Odhaczanie, udostępnianie i czyszczenie listy zakupów](#fr-26-odhaczanie-udostępnianie-i-czyszczenie-listy-zakupów)
- [FR-27: Dodanie składników z całego tygodnia z Planera](#fr-27-dodanie-składników-z-całego-tygodnia-z-planera)
- [FR-58: Dodawanie składników z konkretnego dnia na liście zakupów](#fr-58-dodawanie-składników-z-konkretnego-dnia-na-liście-zakupów)
- [FR-62: Mini kalendarzyk bieżącego tygodnia na liście zakupów](#fr-62-mini-kalendarzyk-bieżącego-tygodnia-na-liście-zakupów)
- [FR-75: Widok kafelkowy listy zakupów z brakującymi ilościami](#fr-75-widok-kafelkowy-listy-zakupów-z-brakującymi-ilościami)

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
- [FR-83: Edycja wcześniej wpisanej wagi i historii kalorii](#fr-83-edycja-wcześniej-wpisanej-wagi-i-historii-kalorii)

### Nagłówek i nawigacja
- [FR-43: Pasek filtrów i kategorii przyklejony pod nagłówkiem](#fr-43-pasek-filtrów-i-kategorii-przyklejony-pod-nagłówkiem)
- [FR-44: Automatyczne chowanie/pokazywanie nagłówka na przewijanie (tylko Przepisy)](#fr-44-automatyczne-chowaniepokazywanie-nagłówka-na-przewijanie-tylko-przepisy)
- [FR-45: Ręczne zwijanie/rozwijanie nagłówka ma pierwszeństwo nad automatyką](#fr-45-ręczne-zwijanierozwijanie-nagłówka-ma-pierwszeństwo-nad-automatyką)
- [FR-46: Zabezpieczenie przed przypadkowym zamknięciem aplikacji (Android „Wstecz”)](#fr-46-zabezpieczenie-przed-przypadkowym-zamknięciem-aplikacji-android-wstecz)
- [FR-47: Brak migotania (FOUC) domyślnych danych profilu przy odświeżeniu](#fr-47-brak-migotania-fouc-domyślnych-danych-profilu-przy-odświeżeniu)
- [FR-59: Wyśrodkowane okienka modalne, na pełną dostępną szerokość](#fr-59-wyśrodkowane-okienka-modalne-na-pełną-dostępną-szerokość)
- [FR-70: Licznik nawodnienia w nagłówku — pojedyncze klikalne kropelki](#fr-70-licznik-nawodnienia-w-nagłówku--pojedyncze-klikalne-kropelki)

### Wygląd i motywy
- [FR-48: Wybór motywu kolorystycznego aplikacji](#fr-48-wybór-motywu-kolorystycznego-aplikacji)
- [FR-49: Motyw „Polaroid” z kartami w stylu odbitek natychmiastowych](#fr-49-motyw-polaroid-z-kartami-w-stylu-odbitek-natychmiastowych)
- [FR-50: Redukcja animacji (prefers-reduced-motion)](#fr-50-redukcja-animacji-prefers-reduced-motion)
- [FR-61: Wybór stylu oceniania kart przesunięciem w Ustawieniach](#fr-61-wybór-stylu-oceniania-kart-przesunięciem-w-ustawieniach)
- [FR-63: Motywy „Fluent” i „Kafelki” inspirowane Windows 11 / Metro](#fr-63-motywy-fluent-i-kafelki-inspirowane-windows-11--metro)
- [FR-87: Motyw „Klinika” — czcionka i układ, nie tylko kolory](#fr-87-motyw-klinika--czcionka-i-układ-nie-tylko-kolory)

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

---

# FR-3: Karta przepisu — widok skrócony i rozwinięty

**Obszar:** Przepisy i przeglądanie  
**Status:** Zaimplementowane

## Opis
Każdy przepis wyświetlany jest jako karta z nazwą, czasem przygotowania, kalorycznością i skrótowymi znacznikami (np. podwyższony IG, dopasowanie do celu). Domyślnie karta jest zwinięta. Rozwijanie jest dwuetapowe: pierwsze stuknięcie w zwiniętą kartę WYŁĄCZNIE przewija ją do widoku (wyśrodkowuje), nie rozwijając jej jeszcze — dopiero drugie stuknięcie tej samej, już wyśrodkowanej karty faktycznie rozwija pełną listę składników, sposób przygotowania i przyciski akcji (i ponownie ją pozycjonuje). Zwinięcie rozwiniętej karty z powrotem nadal działa jednym, natychmiastowym stuknięciem — dwuetapowość dotyczy wyłącznie otwierania.

## Kryteria akceptacji
- Karta w stanie zwiniętym pokazuje tylko nagłówek i podstawowe metadane.
- Rozwinięcie karty odbywa się WYŁĄCZNIE przez wyraźne, stacjonarne stuknięcie — nie przez przypadkowe zatrzymanie przewijania listy (patrz historia rewizji poniżej i FR-44).
- Pierwsze stuknięcie zwiniętej karty przewija ją do widoku, ale jej NIE rozwija. Drugie stuknięcie tej samej karty (bez stuknięcia innej karty pomiędzy) rozwija ją. Stuknięcie innej, zwiniętej karty pomiędzy tymi dwoma stuknięciami traktuje TĘ nową kartę jako pierwsze stuknięcie (nie rozwija poprzednio dotykanej).
- Tylko jedna karta na liście może być rozwinięta jednocześnie.
- Po rozwinięciu karty ekran automatycznie przewija się tak, żeby cała rozwinięta karta wylądowała na środku widocznego obszaru — użytkownik nie musi ręcznie doprzewijać, żeby zobaczyć składniki i sposób przygotowania. Przewinięcie następuje PO zakończeniu animacji rozwijania karty (nie w trakcie), żeby wyśrodkowanie trafiało na docelową, już powiększoną wysokość karty, a nie na jej wysokość sprzed rozwinięcia. Jeśli rozwinięta karta jest WYŻSZA niż widoczny obszar ekranu, wyśrodkowanie zastępowane jest wyrównaniem górnej krawędzi karty do góry widoku (samo wyśrodkowanie ucinałoby wtedy tytuł/początek karty poza ekranem).

## Uwagi
Zrewidowane w rundzie z 2026-08-03: pierwotna wersja pozwalała, by dotknięcie kończące przewijanie listy (bardzo mały ruch palca przy jednoczesnym przewinięciu strony przez inercję) było błędnie odczytane jako stuknięcie i rozwijało kartę, co powodowało 'skakanie' ekranu. Naprawiono porównując pozycję przewijania strony w momencie dotknięcia i puszczenia — jeśli strona przewinęła się w tym czasie, gest NIE liczy się jako stuknięcie, nawet jeśli sam palec poruszył się nieznacznie. Patrz też FR-44.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-03): Doprecyzowano zachowanie na podstawie zgłoszonej poprawki — patrz sekcja "Uwagi" powyżej.
- **v3** (2026-08-08): Dodano automatyczne wyśrodkowywanie rozwiniętej karty na ekranie, na życzenie użytkownika ("karta z przepisem na którą klikniemy [powinna] wyśrodkowywać się na ekranie... użytkownik nie musi sam jej przesuwać").
- **v4** (2026-08-11): Rozbito otwieranie na dwa etapy, na wyraźną prośbę użytkownika ("zmień żeby wysrodkowywalo kafelek dopiero po kliknięciu na niego a dopiero po drugim kliknięciu żeby go rozwijało i wysrodkowywalo albo jak się nie mieści na ekranie to żeby był wyświetlony od góry") — pierwsze stuknięcie tylko centruje, drugie rozwija; dodano też wariant "wyrównaj do góry" dla kart wyższych niż ekran, zamiast zawsze centrować (co ucinałoby górę zbyt wysokiej karty). Zamknięcie nadal jednym stuknięciem.

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
Przycisk „🏺 Sprawdź stan spiżarni dla tego dania” w rozwiniętej karcie otwiera okienko stylizowane jak karta przepisu (te same zaokrąglone rogi, cień, tło), w którym każdy składnik ma osobny wiersz z wyraźnym stanem posiadania („Brak w spiżarni” / „🏺 …”) oraz dużym przyciskiem „Mam to” do oznaczenia/odznaczenia go w spiżarni, plus osobny przycisk dodania pojedynczego składnika do listy zakupów. Sam przycisk otwierający okienko, na karcie przepisu, ma pełnoprawny wygląd przycisku (obramowanie, tło, zaokrąglone rogi, min. wysokość dotykowa), a nie samego napisu.

## Kryteria akceptacji
- Okienko wizualnie przypomina kartę przepisu, nie generyczną szufladę z drobnymi elementami.
- Każdy wiersz ma jeden, duży, łatwo trafialny przycisk zmieniający stan posiadania (min. wysokość dotykowa 34px).
- Zmiana stanu w tym okienku natychmiast odzwierciedla się w zakładce Spiżarnia.
- Przycisk „Sprawdź stan spiżarni” na karcie przepisu ma widoczne obramowanie i wypełnione tło (nie jest samym tekstem) oraz min. wysokość dotykową ok. 38px, na całą szerokość karty.

## Uwagi
Zrewidowane w rundzie z 2026-08-03: poprzednia wersja miała stłoczony, jednowierszowy układ (tekst składnika + malutka plakietka + dwa małe przyciski obok siebie), trudny do trafienia kciukiem — przeprojektowano na czytelny układ dwuwierszowy z osobnym, dużym przyciskiem akcji.

Zrewidowane ponownie 2026-08-03: sam przycisk-wyzwalacz na karcie przepisu był stylistycznie samym napisem bez tła/obramowania, co czyniło go trudnym do trafienia — dodano pełny styl przycisku (patrz Kryteria akceptacji).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-03): Doprecyzowano zachowanie na podstawie zgłoszonej poprawki — patrz sekcja "Uwagi" powyżej.
- **v3** (2026-08-03): Dodano stylizację przycisku-wyzwalacza na karcie przepisu — patrz sekcja "Uwagi" powyżej.

---

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

# FR-86: Podgląd przepisu z poziomu Planera

**Obszar:** Planer
**Status:** Zaimplementowane (Android), Android-only

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
**Status:** Zaimplementowane

## Opis
Zakładka Spiżarnia pokazuje kafelki produktów pogrupowane w kategorie (Nabiał, Warzywa, Owoce, Mięso/ryby/jajka, Strączki i orzechy, Pieczywo i zboża, Przyprawy, Inne). Górna połowa kafelka dodaje jednostkę, dolna odejmuje. Przyprawy śledzone są poziomem (Mało/Wystarczy/Dużo), nie liczbą sztuk. Kafelki w każdej kategorii układają się w siatkę rozciągającą się na pełną dostępną szerokość ekranu (równa liczba kolumn dopasowana do szerokości, kafelki równo rozciągnięte), a nie w luźno zawijany rząd o stałej szerokości kafelka z nierówną przerwą na końcu.

## Kryteria akceptacji
- Każda kategoria kończy się kafelkiem „➕ Dodaj własny” do ręcznego dodania produktu spoza bazy przepisów.
- Siatka kafelków wypełnia całą dostępną szerokość ekranu, bez dużej pustej przestrzeni po prawej stronie ostatniego kafelka w wierszu.
- Kafelki w tym samym wierszu mają równą szerokość niezależnie od liczby kolumn wynikającej z szerokości ekranu.
- Przycisk „🗑️ Wyczyść całą spiżarnię” (na obu platformach) usuwa śledzenie WSZYSTKICH produktów i przypraw na raz, po potwierdzeniu — jak pojedyncze „Usuń śledzenie”, ale dla całej spiżarni jednocześnie. Nie kasuje własnych kafelków (`customTiles`) ani zmienionych kategorii/jednostek (`pantryCategoryOverride`/`pantryUnitOverride`) — te wracają do stanu nieśledzonego, tak samo jak każdy inny kafelek po usunięciu śledzenia, ale pozostają zdefiniowane/widoczne.

## Uwagi
Zgłoszony 2026-08-11: użytkownik zgłosił, że aplikacja "zacina się" po dodaniu kilku produktów do spiżarni z rzędu — pierwsze dotknięcia działały, kolejne przestawały reagować na chwilę. Przyczyna: dotknięcie kafelka spiżarni odświeżało (renderPantry/renderShop/renderRecipes) TRZY pełne widoki na raz, w tym listę 229+ przepisów i listę zakupów, nawet gdy użytkownik wcale na nie akurat nie patrzył — kilka szybkich dotknięć kumulowało ten koszt i blokowało główny wątek na chwilę. Naprawione: dotknięcie kafelka odświeża teraz tylko faktycznie widoczne widoki; pozostałe (Przepisy, Zakupy) odświeżają się same przy najbliższym wejściu na tę zakładkę zamiast na każde dotknięcie kafelka. Zweryfikowane bezpośrednio w przeglądarce (podmiana funkcji renderujących w celu policzenia wywołań) — jedno dotknięcie kafelka spiżarni: 0 wywołań renderRecipes/renderShop (wcześniej: po 1 każde, na KAŻDE dotknięcie).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-03): Doprecyzowano układ siatki kafelków na pełną szerokość ekranu (zamiast luźnego zawijania o stałej szerokości) — patrz Opis i Kryteria akceptacji.
- **v3** (2026-08-11): Naprawiono realny błąd wydajności powodujący zacinanie się aplikacji przy kilku szybkich dotknięciach kafelków z rzędu — patrz sekcja "Uwagi" powyżej. Brak zmiany zachowania funkcjonalnego, wyłącznie poprawka wydajności.
- **v4** (2026-08-11): Dodano przycisk „🗑️ Wyczyść całą spiżarnię” na obu platformach (web i Android, w tej samej turze), na wyraźną prośbę użytkownika ("dodaj opcji czyszczenia całej spiżarni w obydwu wersjach kotlin i html").

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
**Status:** Zaimplementowane

## Opis
Przycisk „➕ Dodaj swój przepis” w zakładce Przepisy otwiera formularz (nazwa, kategoria, czas przygotowania, składniki — jeden na linię, sposób przygotowania, kalorie, opcjonalnie białko/węglowodany/tłuszcz). Zapisany przepis trafia do `state.myRecipes` i od razu jest pełnoprawnym przepisem: pojawia się na liście przepisów swojej kategorii oznaczony plakietką „✍️ Twój przepis”, można go zaplanować (Planer), dodać do listy zakupów, sprawdzić jego składniki względem spiżarni, oznaczyć jako zrobiony (z historią i oceną) oraz ocenić gwiazdkowo (FR-67) — dokładnie tak samo jak którykolwiek z 229 wbudowanych przepisów.

Pola makroskładników są opcjonalne i wypełniają się automatycznie w miarę wpisywania składników: formularz na bieżąco parsuje każdą linię składnika (rozpoznając ilość i gramaturę tak samo jak reszta aplikacji przy dodawaniu do spiżarni/listy zakupów) i sumuje wartości z osobnej bazy odżywczej (~90 najpopularniejszych składników). Pod polem widać, ile składników zostało rozpoznane. Ręczne wpisanie wartości w pole kalorii/białka/węgli/tłuszczu ma pierwszeństwo — od tego momentu auto-obliczanie przestaje nadpisywać akurat to pole, więc użytkownik zawsze może poprawić wynik, a nie tylko go zaakceptować.

## Kryteria akceptacji
- Formularz wymaga: nazwy, przynajmniej jednego składnika, dodatniej liczby kalorii (przy braku ręcznej wartości i nierozpoznanych składnikach walidacja ustawia fokus na polu kalorii z jasnym komunikatem, zamiast tylko ciche powiadomienie na dole ekranu). Kategoria, czas i sposób przygotowania mają rozsądne wartości domyślne, jeśli pozostawione puste.
- Zapisany przepis jest natychmiast widoczny na liście przepisów, w wybranej kategorii, z plakietką odróżniającą go od wbudowanych.
- Własny przepis działa identycznie jak wbudowany we WSZYSTKICH miejscach odwołujących się do przepisów po ID: Planer, lista zakupów, sprawdzenie spiżarni, historia gotowania, wyszukiwanie, filtrowanie, sortowanie.
- Własny przepis można usunąć bezpośrednio z karty (przycisk „🗑️ Usuń”, z potwierdzeniem) — usunięcie nie wpływa na wcześniej dodane wpisy historii gotowania czy pozycje na liście zakupów pochodzące z tego przepisu.
- Wpisanie składnika z rozpoznawalną ilością/gramaturą (np. „150 g piersi z kurczaka”) automatycznie dolicza jego kalorie i makroskładniki do sumy przepisu; nierozpoznane składniki (rzadkie/nietypowe nazwy) są pomijane w sumie, a formularz jasno informuje ile z wpisanych linii zostało rozpoznanych.
- Ręczna edycja pola kalorii/białka/węglowodanów/tłuszczu zatrzymuje automatyczne nadpisywanie TEGO konkretnego pola do końca sesji formularza (nowe otwarcie formularza resetuje ten stan).

## Uwagi
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

## Uwagi
Rzeczywisty zapis/odczyt z prawdziwego Firestore można zweryfikować tylko
na urządzeniu z dostępem do sieci Google/Firebase. Logika synchronizacji
(wybór synchronizowanych pól, debouncing, scalanie tylko zmienionych pól,
zachowanie przy nowym/istniejącym koncie) została zweryfikowana
automatycznie z podstawionym (mockowanym) klientem Firestore; rzeczywiste
działanie między dwoma prawdziwymi urządzeniami wymaga sprawdzenia przez
użytkownika.

## Historia rewizji
- **v1** (2026-08-08): Pierwsza wersja wymagania — naprawia zgłoszony błąd
  ("po zalogowaniu na dwóch urządzeniach nie zsynchronizowało mi nazwy
  użytkownika ani spiżarni, ani żadnych ustawień jak chociażby to żeby
  pokazywało przepisy innych użytkowników"), realizując punkt 6 checklisty
  z `docs/FIREBASE_MIGRATION_PLAN.md`.
- **v2** (2026-08-08): Rozszerzono zakres synchronizacji na listę zakupów,
  planer i pozostałe wcześniej wyłączone pola, z prawdziwym scalaniem
  zmian — patrz FR-78.

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

## Uwagi
Zgłoszony 2026-08-11 (web): użytkownik zgłosił, że przeglądanie listy użytkowników zawiesza się na „Wczytywanie…” bez końca (podczas gdy natywna aplikacja Android — zaimplementowana kilka godzin wcześniej tego samego dnia — poprawnie pokazuje pusty/błędny stan). Przyczyna: zapytanie Firestore w stanie faktycznie offline (bez pasującego zbuforowanego wyniku) może wisieć w nieskończoność, nie rozstrzygając się ani powodzeniem, ani błędem — `.catch()` istniał już wcześniej, ale nigdy się nie uruchamiał, bo obietnica po prostu nigdy się nie rozstrzygała. Naprawione dodaniem twardego limitu czasu (12 sekund) na oba zapytania (lista i profil) — po przekroczeniu limitu pokazuje się czytelny komunikat błędu zamiast nieskończonego "Wczytywanie…".

## Historia rewizji
- **v1** (2026-08-08): Pierwsza wersja wymagania, na życzenie użytkownika
  ("chciałbym żeby można było przeglądać listę dań dodanych przez
  użytkowników oraz listę użytkowników, po kliknięciu na nazwę użytkownika
  w jego profilu będzie można podejrzeć tylko login, oraz datę ostatniego
  logowania, ewentualnie ulubione przepisy bądź oceniane komentowane
  przepisy").
- **v2** (2026-08-11): Naprawiono nieskończone „Wczytywanie…” przy braku szybkiej odpowiedzi z Firestore — patrz sekcja "Uwagi" powyżej.

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

---

# FR-83: Edycja wcześniej wpisanej wagi i historii kalorii

**Obszar:** Postęp
**Status:** Zaimplementowane (waga: web + Android; historia kalorii: web — Android świadomie odłożone, patrz Uwagi)

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
Android ma dziś tylko WAGĘ w pełni zaportowaną. Historia kalorii na Androidzie architektonicznie NIE wspiera edycji wstecz bez większej przebudowy: `EatenViewModel._entries` trzyma stan WYŁĄCZNIE per kategoria posiłku (zawsze "dzisiaj"), a `_kcalHistory` to tylko POCHODNA suma dzienna, bez zapisanych pojedynczych zaznaczeń/przekąsek dla przeszłych dni — w odróżnieniu od web'a, gdzie `state.eaten[data]` od zawsze przechowuje pełny, edytowalny stan PER DATA. Dodanie tego wymagałoby zmiany kształtu `EatenEntry`/`_entries` na mapę data→kategoria→wpis, aktualizacji `EatenOperations`, `CloudSyncCoordinator`'s kodeka pola "eaten" i ekranu, który dziś renderuje tylko "dzisiaj" — porównywalne rozmiarem do osobnego, dedykowanego FR, świadomie odłożone zamiast pospiesznej, niedotestowanej przebudowy modelu danych w tej samej turze co inne zmiany (patrz CLAUDE.md o niepiętrzeniu wielu niezweryfikowanych kroków w Kotlinie na raz).

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

**Obszar:** Wygląd aplikacji (wszystkie 5 zakładek), Android
**Status:** Zaimplementowane (Android), Android-only

## Opis
Dodano 12. motyw kolorystyczny — „Klinika” (id `clinic`) — wybierany w
Ustawieniach obok pozostałych 11 (Zielony/domyślny, Jasny, Różowy, Ciemny,
Zbiory, Cytrusowy, Miętowy, Jagodowa noc, Polaroid, Fluent, Kafelki). W
odróżnieniu od tamtych, które są czystym portem palety kolorów `index.html`
(FR-48), „Klinika” ma WŁASNĄ czcionkę, WŁASNE promienie zaokrągleń i WŁASNY
układ na każdym z 5 ekranów — na wyraźną prośbę użytkownika, żeby nie był to
"po prostu kolejny motyw z innymi kolorkami".

Paleta: tło niemal białe (`#F8FAF9`), karty czysta biel, akcent szałwiowy/
miętowy (`#5B9279`/`#7FB6A6`), tekst ciemnoszary (`#1F2937`), czerwień tylko
dla ikon usuwania (`#EF4444`).

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
- **Planer**: pasek bento z celem dziennym (kcal/białko/tłuszcz/węgle),
  karty dni z odznaką „Dziś”, wiersze posiłków jako zaokrąglone chipy z
  emoji-avatarem.
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

## Kryteria akceptacji
- Wybranie motywu „Klinika” w Ustawieniach zmienia paletę, czcionkę I układ
  jednocześnie na wszystkich 5 zakładkach.
- Wybranie dowolnego z pozostałych 11 motywów daje DOKŁADNIE taki sam
  wygląd jak przed tą zmianą (ten sam `AppShapes`/systemowa czcionka/układ).
- `./gradlew :app:assembleDebug :logic:test` przechodzi.

## Uwagi
Świadoma, udokumentowana rozbieżność web/Android (patrz `android/
PARITY.md`) — funkcja dodana wyłącznie w sesji dotyczącej Kotlina, `index.
html` nie ma dziś odpowiednika motywu „Klinika”; port pozostaje do
rozważenia w osobnej turze, jeśli użytkownik zdecyduje że ma się pojawić
też w wersji webowej.

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

---
