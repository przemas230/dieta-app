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
