# FR-6: Profil użytkownika i wyliczanie zapotrzebowania kalorycznego

**Obszar:** Personalizacja i cele dietetyczne  
**Status:** Zaimplementowane

## Opis
W Ustawieniach użytkownik podaje płeć, wiek, wzrost, wagę obecną i docelową, poziom aktywności fizycznej oraz cel (redukcja/utrzymanie/budowanie masy). Na tej podstawie aplikacja liczy BMR (wzór Mifflin-St Jeor), następnie TDEE (BMR × współczynnik aktywności), a potem koryguje wynik pod kątem wybranego celu.

## Kryteria akceptacji
- Zmiana dowolnego pola profilu i zapisanie przelicza dzienny cel kaloryczny.
- Wynik jest widoczny w Ustawieniach i w nagłówku aplikacji.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
