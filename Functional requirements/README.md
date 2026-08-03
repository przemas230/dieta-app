# Wymagania funkcjonalne — zasady prowadzenia tego folderu

Ten folder zawiera pełny zbiór wymagań funkcjonalnych aplikacji Dieta App, ponumerowanych kolejno **FR-1, FR-2, FR-3…**, spisanych retrospektywnie na podstawie poleceń użytkownika i `RELEASE_NOTES.txt` z folderu `versions/` z dotychczasowych rund prac.

## Struktura

- **`FR-<numer>.md`** — jeden plik na jedno wymaganie. Każdy zawiera: obszar, status, opis, kryteria akceptacji, ewentualne uwagi i historię rewizji.
- **`ALL-REQUIREMENTS.md`** — dokument zbiorczy: spis treści pogrupowany wg obszaru + analiza spójności/wykluczeń między wymaganiami + treść wszystkich FR jedna po drugiej, do przeglądania na raz.
- **`README.md`** (ten plik) — zasady prowadzenia folderu.

## Zasada numeracji

Numeracja jest ciągła i nigdy nie jest ponownie wykorzystywana. Wycofane/zastąpione wymaganie nie znika i nie zmienia numeru — dostaje status `Wycofane` albo `Zastąpione przez FR-N` i zostaje w miejscu, żeby historia była czytelna.

## Zasada rewizji (jak przy `versions/RELEASE_NOTES.txt`)

Gdy w kolejnej rundzie prac:

1. **Powstaje nowa funkcjonalność** → dopisujemy nowy plik `FR-<kolejny numer>.md`, dodajemy go do spisu treści i treści `ALL-REQUIREMENTS.md`, oraz do sekcji "Analiza spójności" w `ALL-REQUIREMENTS.md`, jeśli wchodzi w interakcję z istniejącym wymaganiem.
2. **Zmienia się istniejąca funkcjonalność** (naprawa błędu, zmiana zachowania) → **nie kasujemy** poprzedniego opisu bez śladu. Aktualizujemy sekcję `## Opis`/`## Kryteria akceptacji` tak, by opisywały AKTUALNE zachowanie, a w `## Uwagi` i `## Historia rewizji` dopisujemy nowy, datowany wpis (`- **vN** (YYYY-MM-DD): co się zmieniło i dlaczego`), zamiast nadpisywać poprzedni wpis. Ten sam plik aktualizujemy w obu miejscach: `FR-<numer>.md` i odpowiadający mu fragment `ALL-REQUIREMENTS.md`.
3. **Dwie funkcjonalności zaczynają na siebie wpływać** → dopisujemy punkt w sekcji "Analiza spójności i wykluczeń" w `ALL-REQUIREMENTS.md`, tłumacząc jak rozstrzygnięto ewentualny konflikt (albo dlaczego świadomie zostawiono niespójność UX do rozważenia później — tak jak w przypadku FR-23/FR-24).

## Status

Każde wymaganie ma jedno z: `Zaimplementowane`, `Częściowo zaimplementowane`, `Planowane`, `Wycofane`.

## Powiązanie z `versions/`

Ten folder dokumentuje **co aplikacja robi i dlaczego**, na poziomie wymagań. Folder `versions/` w głównym katalogu repo dokumentuje **stan plików źródłowych** przed każdą zmianą wraz z notatkami wydania po polsku. Oba mechanizmy działają równolegle i się uzupełniają — `RELEASE_NOTES.txt` danej wersji jest naturalnym źródłem do wypełnienia `## Historia rewizji` odpowiadającego wymagania.
