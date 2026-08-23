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
- **v2** (2026-08-23, Android): Użytkownik poprosił o dodanie tego przycisku do Kotlina ("do wersji kotlin dodaj button przewijania do góry listy przepisów"), nie wiedząc że już istniał w kodzie od wcześniej — okazało się, że był całkowicie niewidoczny: `MainActivity.kt`'s `Scaffold`'s `floatingActionButton` slot stawia „💡”/„📖” w tym samym rogu (`BottomEnd`) co ten przycisk, w OSOBNEJ kompozycji renderowanej NAD zawartością ekranu — oba trafiały w dokładnie ten sam punkt, „💡” całkowicie zasłaniając przycisk powrotu do góry pod sobą. Naprawione przeniesieniem na przeciwny róg (`BottomStart`) w `RecipeListScreen.kt`, żeby nie kolidował niezależnie od tego, ile przycisków wyrośnie w tamtym rogu. Zweryfikowane bezpośrednio na emulatorze: po przewinięciu listy przepisów przycisk widoczny w lewym dolnym rogu.
