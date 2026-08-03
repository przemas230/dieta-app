# FR-3: Karta przepisu — widok skrócony i rozwinięty

**Obszar:** Przepisy i przeglądanie  
**Status:** Zaimplementowane

## Opis
Każdy przepis wyświetlany jest jako karta z nazwą, czasem przygotowania, kalorycznością i skrótowymi znacznikami (np. podwyższony IG, dopasowanie do celu). Domyślnie karta jest zwinięta; stuknięcie w kartę rozwija pełną listę składników, sposób przygotowania i przyciski akcji.

## Kryteria akceptacji
- Karta w stanie zwiniętym pokazuje tylko nagłówek i podstawowe metadane.
- Rozwinięcie karty odbywa się WYŁĄCZNIE przez wyraźne, stacjonarne stuknięcie — nie przez przypadkowe zatrzymanie przewijania listy (patrz FR-58/44 w historii rewizji).
- Tylko jedna karta na liście może być rozwinięta jednocześnie.

## Uwagi
Zrewidowane w rundzie z 2026-08-03: pierwotna wersja pozwalała, by dotknięcie kończące przewijanie listy (bardzo mały ruch palca przy jednoczesnym przewinięciu strony przez inercję) było błędnie odczytane jako stuknięcie i rozwijało kartę, co powodowało 'skakanie' ekranu. Naprawiono porównując pozycję przewijania strony w momencie dotknięcia i puszczenia — jeśli strona przewinęła się w tym czasie, gest NIE liczy się jako stuknięcie, nawet jeśli sam palec poruszył się nieznacznie. Patrz też FR-44.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-03): Doprecyzowano zachowanie na podstawie zgłoszonej poprawki — patrz sekcja "Uwagi" powyżej.
