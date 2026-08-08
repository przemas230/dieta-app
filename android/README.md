# Dieta App — Android (Kotlin / Jetpack Compose)

Natywna wersja Android tej samej aplikacji, podłączona do TEGO SAMEGO
projektu Firebase (`dieta-app-323b4`) co wersja webowa w tym repozytorium —
kolekcje `users/{uid}`, `recipes/{id}`, `publicProfiles/{uid}` będą docelowo
wspólne między obiema wersjami, bez migracji.

## Status: pierwszy krok (lista przepisów, bez Firebase)

Co już działa:
- Pełny szkielet projektu Gradle/Kotlin/Compose.
- 229 przepisów wyeksportowanych z web-owego `index.html` do
  `app/src/main/assets/recipes.json` (ten sam kształt danych).
- Ekran listy przepisów: kategorie (Śniadania/Obiady/Kolacje/Deser —
  "Śniadania" łączy `sniadania` i `drugie`, tak jak w wersji webowej, patrz
  `Functional requirements/FR-74.md`), wyszukiwanie, rozwijanie karty po
  kliknięciu.

Czego jeszcze NIE ma (kolejne kroki, patrz "Co dalej"):
- Logowania i synchronizacji z Firebase (zależności są już dodane w Gradle,
  ale kod nigdzie ich jeszcze nie wywołuje).
- Pozostałych zakładek (Planer, Zakupy, Spiżarnia, Postęp, Ustawienia).
- Nawigacji dolnej między zakładkami.

**Nie mogłem tego skompilować ani uruchomić** — środowisko, w którym to
piszę, nie ma zainstalowanego Android SDK/Gradle/emulatora. Kod jest napisany
starannie, standardowymi, dobrze udokumentowanymi wzorcami (Compose + Material3
+ ViewModel + StateFlow), ale **pierwsza rzecz do zrobienia to otworzyć to w
Android Studio i sprawdzić, czy się buduje i uruchamia**.

## Jak otworzyć

1. Android Studio → **Open** → wskaż ten folder (`android/`), NIE cały
   `dieta-app`.
2. Jeśli Android Studio zapyta o brakujący Gradle Wrapper — zgódź się, żeby
   go wygenerował/naprawił automatycznie (celowo go tu nie ma, żeby uniknąć
   commitowania niesprawdzonego, potencjalnie niekompatybilnego pliku binarnego
   `gradle-wrapper.jar`).
3. Poczekaj na Gradle Sync (pasek postępu na dole).
4. Uruchom na emulatorze albo telefonie (zielony trójkąt **Run**).

Jeśli sync się nie powiedzie z powodu niezgodności wersji (Kotlin/AGP/Compose
BOM) — to najbardziej prawdopodobne miejsce błędu, bo nie miałem jak tego
sprawdzić. Android Studio zwykle podpowiada dokładnie, którą wersję podbić;
daj mi znać, jaki błąd pokazuje, a poprawię.

## Podłączenie Firebase (kiedy będziesz gotów/gotowa)

Pełna instrukcja krok po kroku (rejestrowanie aplikacji w konsoli Firebase,
pobranie `google-services.json`, dodanie do projektu) jest w przewodniku,
który dostałeś wcześniej w tej sesji — sekcja 2 i 3. W skrócie: pobrany plik
`google-services.json` wrzucasz do `android/app/google-services.json` — build
sam wykryje jego obecność i włączy odpowiedni plugin (`app/build.gradle.kts`
sprawdza to automatycznie, nie trzeba nic ręcznie odkomentowywać).

## Package name

`com.przemas230.dietaapp` — jeśli w Firebase Console zarejestrowałeś/aś już
inną aplikację Android z INNĄ nazwą pakietu (np. z wcześniejszej własnej
próby w Android Studio), powiedz mi jaka to nazwa i zmienię ją tutaj
(Android Studio ma do tego "Refactor → Rename Package", ale bezpieczniej
zrobić to raz, świadomie, niż zmieniać po fakcie).

## Co dalej

Kolejność, w jakiej sensownie budować resztę (każdy krok to coś, co da się
realnie sprawdzić w Android Studio, zanim przejdziemy do następnego):

1. Nawigacja dolna (`NavigationBar` + `NavHost`) między pustymi na razie
   ekranami odpowiadającymi zakładkom z wersji webowej.
2. Logowanie anonimowe + zapis/odczyt jednego prostego pola w `users/{uid}`
   (np. `displayName`) — potwierdzenie, że Firestore realnie działa z tym
   projektem.
3. Ekran Spiżarni i Listy zakupów — najbliższe strukturze mapy
   `{nazwa: ilość}`, którą już zna kod webowy.
4. Planer tygodniowy.
5. Logowanie Google/e-mail, profil diety, dopasowanie makro.
6. Synchronizacja (odpowiednik `pushStateToCloud`/`applyRemoteSyncedState`
   z web-owego `index.html` — ten sam model 3-way merge, patrz
   `Functional requirements/FR-78.md`, tylko po stronie Kotlin/Firestore SDK).
7. Przepisy społeczności, lista użytkowników, komentarze (FR-76/FR-77).

Mów, który krok robimy dalej — zrobię go w całości, jak najbliżej gotowego
do sprawdzenia w Android Studio, zamiast zostawiać cię z niedokończonym
plikiem.
