# FR-8: Filtr bez glutenu / bez laktozy

**Obszar:** Personalizacja i cele dietetyczne  
**Status:** Zaimplementowane

## Opis
Dwa niezależne przełączniki w Ustawieniach pozwalają ukryć dania zawierające gluten (pieczywo, kasze glutenowe) lub nabiał bez wyraźnie oznaczonej wersji „bez laktozy” — zarówno z listy przepisów (Przepisy), jak i z każdego źródła doboru dania w Planerze (losowanie całego tygodnia/dnia/pojedynczego slotu, ręczny wybór dania ze slotu).

## Kryteria akceptacji
- Włączenie filtra ukrywa pasujące przepisy natychmiast po zapisaniu ustawień w widoku Przepisy.
- Włączenie filtra dotyczy też Planera: „🎲 Wygeneruj losowo cały tydzień”, „🎲 Losuj ten dzień”, „🔁 losuj inne danie” na pojedynczym slocie oraz ręczny picker dania dla slotu nigdy nie proponują dania niespełniającego aktywnego filtra.
- Filtr jest jawnie opisany jako orientacyjny, nie medyczny — nie gwarantuje 100% poprawności dla każdego przepisu.

## Uwagi
Ograniczenie znane i udokumentowane w samej aplikacji: filtr bazuje na oznaczeniach składników, nie na certyfikowanej analizie, więc nie wyklucza się logicznie z FR-1..FR-3, ale nie należy go traktować jako gwarancji bezpieczeństwa zdrowotnego.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-22): Naprawiono realny błąd znaleziony podczas manualnego przejścia checklisty QA — filtr od zawsze działał WYŁĄCZNIE w widoku Przepisy (`isGlutenFree`/`isLactoseFree` stosowane tylko tam). Cały Planer (`recipesByCat()`, używana przez `fittingPool()` i ręczny picker slotu) budował pulę kandydatów bez żadnego sprawdzenia tych dwóch przełączników, więc auto-generowanie tygodnia/dnia/slotu i ręczny wybór dania regularnie proponowały dania z glutenem/laktozą mimo aktywnego filtra. Naprawione przez zastosowanie tych samych predykatów wewnątrz `recipesByCat()` (z zabezpieczeniem: jeśli filtr zostawiłby pustą pulę dla kategorii, funkcja cofa się do pełnej listy tej kategorii zamiast zwracać pustkę, która wywaliłaby resztę Planera wyjątkiem) — patrz `android/PARITY.md`'s notatka z tej daty po pełen opis i zweryfikowanie w przeglądarce.
