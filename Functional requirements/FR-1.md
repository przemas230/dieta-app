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
