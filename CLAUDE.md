# Zasady pracy w tym repozytorium

## Backup i wersjonowanie plików

Przed każdą zmianą plików aplikacji (np. `index.html`, `manifest.json`, `sw.js`)
należy:

1. Sprawdzić najwyższy istniejący numer wersji w folderze `versions/`
   (foldery `v1`, `v2`, `v3`, ...).
2. Skopiować **aktualny stan (sprzed edycji) KAŻDEGO pliku, który zostanie w tej
   turze zmieniony** — nie tylko `index.html`. Jeśli w danej zmianie dotykany
   jest też np. `sw.js`, `manifest.json` czy ikony, ich stan sprzed edycji też
   trafia do `versions/v<N+1>/`, z zachowaniem oryginalnych nazw plików.
3. Dodać w tym samym folderze plik `RELEASE_NOTES.txt` z krótkim opisem
   zmian wprowadzanych w tej wersji (po polsku, kilka zdań/punktów —
   co się zmieniło i dlaczego, nie techniczny diff).
4. Dopiero potem wprowadzać właściwe zmiany w plikach źródłowych repozytorium
   (poza folderem `versions/`).

Folder `versions/` służy jako czytelny, ludzki backup obok historii gita —
umożliwia szybkie przywrócenie dowolnej wcześniejszej wersji plików bez
grzebania w `git log`. Nie zastępuje to normalnych commitów gita — commitować
należy zarówno zmiany w `versions/`, jak i w plikach aplikacji.

Numeracja wersji jest ciągła i rośnie z każdą znaczącą zmianą (nie z każdym
drobnym commitem technicznym typu literówka), o ile użytkownik nie zdecyduje
inaczej.

## Wymagania funkcjonalne (`Functional requirements/`)

Folder `Functional requirements/` zawiera pełny, ponumerowany (FR-1, FR-2, …)
zbiór wymagań funkcjonalnych aplikacji — zasady jego prowadzenia opisane są
w `Functional requirements/README.md`. W skrócie, przy każdej turze pracy
dotykającej funkcjonalności aplikacji:

1. Nowa funkcjonalność → nowy plik `FR-<kolejny numer>.md`, dopisany też do
   spisu treści i treści `ALL-REQUIREMENTS.md`.
2. Zmiana istniejącej funkcjonalności → zaktualizować `## Opis` i
   `## Kryteria akceptacji` tak, by opisywały aktualne zachowanie, oraz
   dopisać datowany wpis w `## Historia rewizji` (nie nadpisywać cicho
   poprzedniej wersji opisu) — w OBU miejscach: `FR-<numer>.md` i
   odpowiadającym fragmencie `ALL-REQUIREMENTS.md`.
3. Jeśli dwa wymagania zaczynają na siebie wpływać → dopisać punkt w sekcji
   "Analiza spójności i wykluczeń" w `ALL-REQUIREMENTS.md`.

Ten folder dokumentuje zachowanie aplikacji na poziomie wymagań, równolegle
do `versions/`, który dokumentuje stan plików źródłowych — `RELEASE_NOTES.txt`
danej wersji jest naturalnym źródłem do wypełnienia historii rewizji
odpowiadającego FR.

## Równoległy rozwój wersji webowej (PWA) i natywnej (Android/Kotlin)

Od 2026-08-08 repozytorium zawiera też szkielet natywnej aplikacji Android
w folderze `android/` (Kotlin/Jetpack Compose, podłączona do TEGO SAMEGO
projektu Firebase co wersja webowa). Użytkownik chce rozwijać obie wersje
równolegle, bez różnic funkcjonalnych między nimi. W praktyce:

1. Każda nowa funkcja albo zmiana zachowania dodawana do `index.html`
   powinna w tej samej turze pracy dostać odpowiadający port do `android/`
   (albo świadomą notatkę w `android/PARITY.md`, dlaczego jeszcze nie —
   np. bo poprzedni krok w Kotlinie nie został jeszcze potwierdzony przez
   użytkownika w Android Studio).
2. `android/PARITY.md` to źródło prawdy o tym, co jest, a co nie jest
   jeszcze przeniesione — aktualizować przy każdej zmianie po którejkolwiek
   stronie, żeby nigdy nie trzeba było zgadywać.
3. W środowiskach z zablokowanym dostępem do `dl.google.com` (repozytorium
   Maven Google, źródło Android Gradle Plugin/AndroidX/Compose/Firebase
   Android SDK) kod Kotlin/Gradle nie da się skompilować ani uruchomić
   (potwierdzone kiedyś błędem 403 przy rozwiązywaniu pluginu
   `com.android.application`) — w takim wypadku każda zmiana w `android/`
   jest z konieczności niezweryfikowana aż do sprawdzenia przez użytkownika
   w prawdziwym Android Studio. To NIE dotyczy jednak lokalnej sesji Claude
   Code na maszynie użytkownika (Windows, ten sam katalog co working
   directory) — tam `./gradlew` ma pełny dostęp do sieci i lokalnego cache'u
   Gradle/AndroidX i realnie kompiluje, linkuje i uruchamia testy JUnit dla
   `:logic` i `:app` (potwierdzone 2026-08-09: `./gradlew :app:assembleDebug`
   i `./gradlew test` przechodzą offline i online). W takiej sesji:
   kompilować i uruchamiać testy po każdej większej zmianie w `android/`
   zamiast zakładać, że weryfikacja nie jest możliwa; nadal pisać ostrożnie
   i jasno oznaczać w PARITY.md co czeka na potwierdzenie wizualne/manualne
   w Android Studio (⏳) vs co jest już potwierdzone (✅), bo sama udana
   kompilacja nie dowodzi poprawności UI/UX ani przepływów wymagających
   emulatora.
4. Nie piętrzyć wielu niezweryfikowanych kroków w Kotlinie na raz bez
   szansy na sprawdzenie między nimi — jeden błąd na wczesnym etapie cicho
   psuje wszystko zbudowane na nim później, a nie ma tu jak tego złapać
   samodzielnie. Po każdym większym kroku (nowy ekran, nowa integracja)
   warto zapytać/poczekać na potwierdzenie, zanim doda się kolejny.
