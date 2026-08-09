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
