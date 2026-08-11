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
