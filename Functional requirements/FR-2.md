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
