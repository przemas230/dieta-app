# FR-2: Wyszukiwanie i filtrowanie przepisów

**Obszar:** Przepisy i przeglądanie  
**Status:** Zaimplementowane

## Opis
Nad listą przepisów znajduje się pole wyszukiwania (po nazwie dania i składnikach) oraz zestaw przełączników: tylko ulubione przepisy (⭐), tylko z ulubionymi składnikami (🌟), tylko dania możliwe do zrobienia z tego, co jest w spiżarni (🏺), sortowanie wg dopasowania do profilu (🎯) i sortowanie rankingowe wg oceny (❤️).

## Kryteria akceptacji
- Wpisanie tekstu w polu wyszukiwania zawęża listę w czasie rzeczywistym.
- Przełączniki są niezależne i można je łączyć (np. tylko ulubione + sortowanie wg dopasowania).
- Pasek filtrów jest przyklejony (sticky) pod nagłówkiem i zawsze widoczny podczas przewijania (patrz FR-43).

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
