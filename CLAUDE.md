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
