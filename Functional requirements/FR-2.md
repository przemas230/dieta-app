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

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-08): Dodano przełącznik „tylko przepisy użytkowników” (🧑‍🍳) i filtr progu oceny gwiazdkowej, na życzenie użytkownika ("rozbuduj funkcję filtrowania, dodaj opcje żeby wyświetlać tylko przepisy dodane przez użytkowników albo tylko z określoną oceną dania ustaloną na podstawie ocen od różnych użytkowników").
- **v3** (2026-08-08): Przełącznik „tylko przepisy użytkowników” objął też zatwierdzone przepisy społeczności dodane przez INNYCH użytkowników, po wdrożeniu FR-76 (wcześniej pokazywał tylko własne przepisy).
