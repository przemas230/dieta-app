# Parytet funkcji: PWA (web) vs aplikacja Android (Kotlin)

Ta tabela śledzi, które wymagania funkcjonalne (`Functional requirements/FR-*.md`,
opisujące zachowanie wersji webowej) mają już odpowiednik w natywnej aplikacji
Android (`android/`). Aktualizowana przy każdej zmianie po obu stronach — jeśli
coś tu jest oznaczone ✅, a w Kotlinie realnie nie działa (bo np. nie zdążyłeś
jeszcze sprawdzić w Android Studio), popraw status na ⏳ do potwierdzenia.

**Legenda:** ✅ zaimplementowane i (na ile się dało) zweryfikowane · ⏳ zaimplementowane, czeka na sprawdzenie w Android Studio · ⬜ jeszcze nie rozpoczęte · N/D nie dotyczy natywnej aplikacji (mechanizm specyficzny dla PWA/Service Workera)

| FR | Wymaganie | Status w Android |
|---|---|---|
| FR-1 | Baza przepisów podzielona na 5 kategorii posiłków | ⏳ częściowo (patrz uwagi niżej) |
| FR-2 | Wyszukiwanie i filtrowanie przepisów | ⏳ częściowo (patrz uwagi niżej) |
| FR-3 | Karta przepisu — widok skrócony i rozwinięty | ⏳ zaimplementowane w ekranie listy przepisów, do sprawdzenia w Android Studio |
| FR-4 | Miniatura przepisu jako emoji głównego składnika | ⬜ nie rozpoczęte |
| FR-5 | Przycisk powrotu do góry listy przepisów | ✅ zaimplementowane i ręcznie zweryfikowane na emulatorze |
| FR-6 | Profil użytkownika i wyliczanie zapotrzebowania kalorycznego | ✅ zaimplementowane i ręcznie zweryfikowane na emulatorze |
| FR-7 | Podział dziennego celu kalorycznego na 5 posiłków | ✅ zaimplementowane jako część FR-6 (ProfileCalculations.calcTargets) |
| FR-8 | Filtr bez glutenu / bez laktozy | ✅ zaimplementowane i ręcznie zweryfikowane na emulatorze |
| FR-9 | Przełącznik rygoru niskiego indeksu glikemicznego | ⏳ częściowo (patrz uwagi niżej) |
| FR-10 | Docelowe proporcje makroskładników zależne od celu | ✅ zaimplementowane i ręcznie zweryfikowane na emulatorze |
| FR-11 | Wynik dopasowania przepisu do profilu (🎯) | ✅ zaimplementowane i ręcznie zweryfikowane na emulatorze |
| FR-12 | Modal wyjaśniający wyliczenia makro/IG/ŁG | ⬜ nie rozpoczęte |
| FR-13 | Piąta kategoria posiłku: Deser/Przekąska | ⏳ częściowo (patrz uwagi niżej) |
| FR-14 | Skalowanie rozmiaru interfejsu (UI scale) | ⬜ nie rozpoczęte |
| FR-15 | Oznaczanie dania jako ugotowane, z historią i ocenami | ⬜ nie rozpoczęte |
| FR-16 | Sprawdzenie stanu spiżarni dla konkretnego przepisu | ⬜ nie rozpoczęte |
| FR-17 | Ocena dania po ugotowaniu (gwiazdki) | ⬜ nie rozpoczęte |
| FR-18 | Planer tygodniowy z 5 slotami posiłków dziennie | ⬜ nie rozpoczęte |
| FR-19 | Wybór innego slotu posiłkowego z poziomu karty przepisu | ⬜ nie rozpoczęte |
| FR-20 | Skalowanie wielkości porcji w planerze | ⬜ nie rozpoczęte |
| FR-21 | Losowe generowanie planu — cały tydzień lub pojedynczy dzień | ⬜ nie rozpoczęte |
| FR-22 | Czyszczenie planu — cały tydzień lub pojedynczy dzień | ⬜ nie rozpoczęte |
| FR-23 | „Ugotuj na 2 dni” — planowanie resztek po zwiększeniu porcji | ⬜ nie rozpoczęte |
| FR-24 | Proaktywna podpowiedź gotowania na kolejny dzień | ⬜ nie rozpoczęte |
| FR-25 | Budowanie listy zakupów ze składników przepisów | ⬜ nie rozpoczęte |
| FR-26 | Odhaczanie, udostępnianie i czyszczenie listy zakupów | ⏳ częściowo (patrz uwagi niżej) |
| FR-27 | Dodanie składników z całego tygodnia z Planera | ⬜ nie rozpoczęte |
| FR-28 | Śledzenie stanu spiżarni w kafelkach pogrupowanych kategoriami | ⏳ częściowo (patrz uwagi niżej) |
| FR-29 | Odmiana gramatyczna nazw produktów w spiżarni | ⬜ nie rozpoczęte |
| FR-30 | Zmiana kategorii i usuwanie śledzenia kafelka spiżarni | ⏳ częściowo (patrz uwagi niżej) |
| FR-31 | Skanowanie kodu kreskowego produktu | ⬜ nie rozpoczęte |
| FR-32 | Podpowiedź „🏺 masz w spiżarni” i „Pomysł na danie z ulubionych składników” | ⬜ nie rozpoczęte |
| FR-33 | Globalny przycisk szybkiego dodania przekąski/dania z każdego miejsca | ⬜ nie rozpoczęte |
| FR-34 | Automatyczne szacowanie kalorii przekąski z bazy 336 produktów | ⬜ nie rozpoczęte |
| FR-35 | Emotikonki przy rozpoznanych składnikach/przekąskach | ⬜ nie rozpoczęte |
| FR-36 | Dzienny pierścień kalorii w nagłówku ze zjadanymi posiłkami | ⬜ nie rozpoczęte |
| FR-37 | Śledzenie nawodnienia — pełny widok i kompaktowy pasek w nagłówku | ⬜ nie rozpoczęte |
| FR-38 | Powiadomienia z szybkimi akcjami do liczenia wody | ⬜ nie rozpoczęte |
| FR-39 | Cykliczne przypomnienie o piciu wody | ⬜ nie rozpoczęte |
| FR-40 | Śledzenie wagi z wykresem | ⬜ nie rozpoczęte |
| FR-41 | Historia kalorii z bilansem tygodniowym | ⬜ nie rozpoczęte |
| FR-42 | Serie (streaks) i historia aktywności | ⬜ nie rozpoczęte |
| FR-43 | Pasek filtrów i kategorii przyklejony pod nagłówkiem | ⬜ nie rozpoczęte |
| FR-44 | Automatyczne chowanie/pokazywanie nagłówka na przewijanie (tylko Przepisy) | ⬜ nie rozpoczęte |
| FR-45 | Ręczne zwijanie/rozwijanie nagłówka ma pierwszeństwo nad automatyką | ⬜ nie rozpoczęte |
| FR-46 | Zabezpieczenie przed przypadkowym zamknięciem aplikacji (Android „Wstecz”) | ⬜ nie rozpoczęte |
| FR-47 | Brak migotania (FOUC) domyślnych danych profilu przy odświeżeniu | ⬜ nie rozpoczęte |
| FR-48 | Wybór motywu kolorystycznego aplikacji | ⬜ nie rozpoczęte |
| FR-49 | Motyw „Polaroid” z kartami w stylu odbitek natychmiastowych | ⬜ nie rozpoczęte |
| FR-50 | Redukcja animacji (prefers-reduced-motion) | ⬜ nie rozpoczęte |
| FR-51 | Instalowalna aplikacja PWA z ikoną i manifestem | N/D — mechanizm specyficzny dla PWA (instalacja/Service Worker/cache), nie dotyczy natywnej appki |
| FR-52 | Cache offline przez Service Worker ze strategią stale-while-revalidate | N/D — mechanizm specyficzny dla PWA (instalacja/Service Worker/cache), nie dotyczy natywnej appki |
| FR-53 | Ręczne wymuszenie aktualizacji i diagnostyka powiadomień | N/D — mechanizm specyficzny dla PWA (instalacja/Service Worker/cache), nie dotyczy natywnej appki |
| FR-54 | Kopie zapasowe wersji plików aplikacji w repozytorium | N/D — mechanizm specyficzny dla PWA (instalacja/Service Worker/cache), nie dotyczy natywnej appki |
| FR-55 | Ocenianie przepisów przesunięciem karty (lubię / nie lubię) | ⬜ nie rozpoczęte |
| FR-56 | Duży, balonowy napis podczas oceniania przesunięciem | ⬜ nie rozpoczęte |
| FR-57 | Trwałe oznaczenie oceny i ranking sort | ⬜ nie rozpoczęte |
| FR-58 | Dodawanie składników z konkretnego dnia na liście zakupów | ⬜ nie rozpoczęte |
| FR-59 | Wyśrodkowane okienka modalne, na pełną dostępną szerokość | ⬜ nie rozpoczęte |
| FR-60 | Warunkowe wyświetlanie „Złotych zasad przy Hashimoto i insulinooporności” | ⬜ nie rozpoczęte |
| FR-61 | Wybór stylu oceniania kart przesunięciem w Ustawieniach | ⬜ nie rozpoczęte |
| FR-62 | Mini kalendarzyk bieżącego tygodnia na liście zakupów | ⬜ nie rozpoczęte |
| FR-63 | Motywy „Fluent” i „Kafelki” inspirowane Windows 11 / Metro | ⬜ nie rozpoczęte |
| FR-64 | Orientacyjne wartości mikroskładników (wapń, wit. D, B12) w okienku wyliczeń | ⬜ nie rozpoczęte |
| FR-65 | Własna, opcjonalna nazwa użytkownika w aplikacji | ⬜ nie rozpoczęte |
| FR-66 | Dodawanie własnych przepisów przez użytkownika | ⬜ nie rozpoczęte |
| FR-67 | Ocena gwiazdkowa i komentarz przy przepisie | ⬜ nie rozpoczęte |
| FR-68 | Ustawienia gospodarstwa domowego i przepisów społeczności (stan przejściowy) | ⬜ nie rozpoczęte |
| FR-69 | Logowanie w chmurze (anonimowe, Google, e-mail i hasło) | ⬜ nie rozpoczęte |
| FR-70 | Licznik nawodnienia w nagłówku — pojedyncze klikalne kropelki | ⬜ nie rozpoczęte |
| FR-71 | Zakładki w Ustawieniach — Konto, Wygląd, Przypomnienia, Ulubione | ⬜ nie rozpoczęte |
| FR-72 | Wymuszenie ustawienia profilu przy pierwszym uruchomieniu | ⬜ nie rozpoczęte |
| FR-73 | Synchronizacja danych osobistych w chmurze między urządzeniami | ⬜ nie rozpoczęte |
| FR-74 | Wspólna zakładka „Śniadania” na liście przepisów, osobne sloty w Planerze | ⏳ zaimplementowane w ekranie listy przepisów, do sprawdzenia w Android Studio |
| FR-75 | Widok kafelkowy listy zakupów z brakującymi ilościami | ⬜ nie rozpoczęte |
| FR-76 | Przepisy społeczności oraz przeglądana lista użytkowników i profili | ⬜ nie rozpoczęte |
| FR-77 | Komentarze wielu użytkowników pod przepisem, z paginacją | ⬜ nie rozpoczęte |
| FR-78 | Pełna synchronizacja stanu z prawdziwym scalaniem zmian (3-way merge) | ⬜ nie rozpoczęte |
| FR-79 | Wylogowanie z urządzenia | ⬜ nie rozpoczęte |

## Uwagi do częściowych wpisów

- **FR-1** (5 kategorii): dane i kafelki kategorii są w Kotlinie, przepisy działają; zakładki Planer i Postęp to nadal placeholdery.
- **FR-2** (wyszukiwanie i filtrowanie): pole wyszukiwania i przełącznik sortowania wg 🎯 dopasowania (FR-11) działają; przełączniki ulubione/spiżarnia/ocena jeszcze nie.
- **FR-13** (5. kategoria Deser/Przekąska): kategoria istnieje i jest wybieralna, ale przepisy w niej nie mają jeszcze nic ekstra ponad pozostałe (to samo dotyczy wszystkich kategorii — brak jest jeszcze osobnych funkcji per-kategoria).
- **FR-26** (odhaczanie/udostępnianie/czyszczenie zakupów): odhaczanie i "usuń kupione" działają; udostępnianie listy (np. przez Intent) jeszcze nie ma. Dane są tylko lokalne w pamięci — bez trwałego zapisu, bez synchronizacji.
- **FR-28** (spiżarnia w kafelkach z kategoriami): jest lista z etykietą kategorii i rozróżnieniem produkt/przyprawa, ale nie w formie kafelków jak w wersji webowej, i kategorie to uproszczony ręczny zestaw 7 opcji zamiast pełnej bazy klasyfikacji składników. Dane tylko lokalne, bez synchronizacji.
- **FR-30** (zmiana kategorii i usuwanie kafelka): usuwanie działa; zmiana kategorii istniejącego produktu po dodaniu jeszcze nie (na razie kategorię wybiera się tylko przy dodawaniu).
- **Testy automatyczne**: reguły biznesowe stojące za FR-2 (wyszukiwanie/filtrowanie), FR-26 (odhaczanie/czyszczenie zakupów) i FR-28/FR-30 (mutacje spiżarni) są pokryte prawdziwie uruchamianymi testami JUnit w `android/logic/`. Patrz `android/README.md`, sekcja "Testy automatyczne".
- **Ekran Spiżarni ręcznie zweryfikowany na emulatorze (2026-08-09)**: dodawanie produktu i przyprawy, +/- ilości, auto-usuwanie produktu przy zejściu ilości do zera, jawne usuwanie (ikona kosza) i cykl poziomu przyprawy (Wystarczy → Brak → Mało → Wystarczy) — wszystko przetestowane klik po kliku, bez crasha aplikacji, zachowanie zgodne z `PantryOperations`. Status FR-28/FR-30 zostaje "częściowo" nie z powodu braku weryfikacji, tylko dlatego że sama funkcjonalność jest niepełna względem wersji webowej (brak kafelków, uproszczone kategorie, brak zmiany kategorii po dodaniu — patrz wyżej).
- **FR-6** (profil + BMR/TDEE): port `calcTargets` z `index.html` jeden-do-jednego (`ProfileCalculations.kt` w `logic/`, testy JUnit z ręcznie przeliczonymi wartościami). Formularz w Ustawieniach (płeć, wiek, wzrost, waga obecna/docelowa, aktywność, cel) + wynik w tej samej karcie + podsumowanie w nagłówku (współdzielony `ProfileViewModel` na poziomie `DietaAppRoot`, nie per-ekran, żeby oba miejsca zawsze zgadzały się co do stanu). Ręcznie zweryfikowane na emulatorze 2026-08-09: zapis (30/170/65/60 → 1540 kcal/dzień, śniadanie 350/II śniadanie 270/obiad 430/kolacja 290/deser 210) i reset do domyślnych (→ 1480 kcal/dzień) dają dokładnie te same liczby co ręczne przeliczenie wzoru Mifflin-St Jeor, zero crashy. Lokalny stan (bez trwałego zapisu/synchronizacji) — jak Spiżarnia/Zakupy, do czasu kroku 6. Makra (FR-10) i dopasowanie przepisu (FR-11) NIE są jeszcze zrobione — to osobne, kolejne FR-y.
- **FR-7**: `calcTargets` zwraca już wszystkie 5 targetów posiłkowych (patrz FR-6 wyżej) — przy okazji poprawiono nieaktualne proporcje w `Functional requirements/FR-7.md`/`ALL-REQUIREMENTS.md` (opisywały 370/280/450/300/100 sprzed dodania Deseru jako pełnoprawnej kategorii; realny kod w `index.html` już dawno używa 340/260/420/280/200).
- **FR-5** (przycisk powrotu do góry): `RecipeListWithScrollToTop` w `RecipeListScreen.kt` — FAB "⬆️" pojawia się po przewinięciu >400dp (albo poza pierwszy element listy) i scrolluje z animacją do indeksu 0. Ręcznie zweryfikowane na emulatorze 2026-08-09: pojawia się przy scrollu, znika z powrotem na górze, `animateScrollToItem` faktycznie płynnie przewija, zero crashy.
- **FR-8/FR-9** (filtry glutenu/laktozy/niskiego IG): `RecipeBrowsing.isGlutenFree`/`isLactoseFree` to jeden-do-jednego port `GLUTEN_KEYWORDS`/`DAIRY_KEYWORDS` z `index.html`, wpięte w `visibleRecipes` i sterowane checkboxami w karcie Profilu (`ProfileViewModel` współdzielony, więc filtr w Ustawieniach od razu wpływa na ekran Przepisów). Ręcznie zweryfikowane na emulatorze: wyszukanie "chleb" dawało kilka wyników bez filtra, "Brak przepisów spełniających kryteria" z aktywnym filtrem bezglutenowym, zero crashy. `strictLowGI` to na razie tylko pole w profilu i checkbox w UI, bez żadnego efektu — zacznie coś robić dopiero gdy powstanie `recipeMatchScore` (FR-11), które konsumuje ten flag do kary za wysoki ładunek glikemiczny.
- **FR-10** (proporcje makro): `ProfileCalculations.calcMacroTargets` — jeden-do-jednego port `MACRO_RATIOS`/`calcMacroTargets` z `index.html`. Wynik (białko/węgle/tłuszcz na dzień) doklejony do tego samego komunikatu "Dopasowano..." po zapisaniu profilu. Ręcznie zweryfikowane na emulatorze: domyślny profil (kobieta/redukcja) dał dokładnie 111 g białka / 130 g węglowodanów / 58 g tłuszczu — identycznie jak w teście JUnit, zero crashy.
- **FR-11** (dopasowanie 🎯): `RecipeMatching.matchScore` — jeden-do-jednego port `recipeMatchScore` z `index.html` (zbieżność B/W/T z targetem kategorii + kara GL zależna od `strictLowGI`/celu, testy JUnit z ręcznie przeliczonymi wartościami dla obu gałęzi kary). Plakietka "🎯 N%" na karcie przepisu + przełącznik "🎯 Dopasowanie" w pasku filtrów, sortujący malejąco (FR-2). Ręcznie zweryfikowane na emulatorze: plakietki pojawiły się na wszystkich kartach z pełnymi danymi odżywczymi, sortowanie dało poprawnie malejącą kolejność (73%, 73%, 72%, 71%...), zero crashy. Modal wyjaśniający wyliczenia (FR-12) jeszcze nie istnieje.

## Jak to utrzymywać

1. Każda nowa funkcja dodana do `index.html` (wersja web) dostaje odpowiadający wpis/aktualizację tutaj.
2. Jeśli funkcja zostanie od razu przeniesiona też do Kotlina — status ⏳ (do potwierdzenia wizualnie/manualnie w Android Studio lub na emulatorze; od 2026-08-09 lokalna sesja Claude Code na maszynie użytkownika potrafi realnie skompilować i uruchomić testy `./gradlew :app:assembleDebug` / `./gradlew test`, więc błędy kompilacji łapiemy od razu tutaj — ⏳ oznacza już tylko brak weryfikacji UI/UX/emulatora, nie niepewność co do kompilowalności).
3. Jeśli sprawdzisz coś w Android Studio i działa — zmień ⏳ na ✅ (albo daj mi znać, zrobię to sam).
4. Jeśli sprawdzisz i NIE działa — zostaw jako ⏳ i opisz błąd, poprawię.
5. Jeśli nowa logika biznesowa da się wydzielić bez Androida (jak `PantryOperations`/`ShoppingOperations`/`RecipeBrowsing` w `android/logic/`) — dostaje testy JUnit w tej samej turze, żeby faktycznie zweryfikowane pozostawało zweryfikowane przy kolejnych zmianach (regresja), zamiast znów zgadywać. Patrz `android/README.md`, sekcja "Testy automatyczne".

