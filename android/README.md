# Dieta App — Android (Kotlin / Jetpack Compose)

Natywna wersja Android tej samej aplikacji, podłączona do TEGO SAMEGO
projektu Firebase (`dieta-app-323b4`) co wersja webowa w tym repozytorium —
kolekcje `users/{uid}`, `recipes/{id}`, `publicProfiles/{uid}` będą docelowo
wspólne między obiema wersjami, bez migracji.

Obie wersje (web i Android) rozwijane są równolegle, z celem braku różnic
funkcjonalnych między nimi. **`PARITY.md`** w tym folderze śledzi dokładnie,
które funkcje z wersji webowej mają już odpowiednik tutaj, a które jeszcze
czekają — sprawdź tam, zanim zapytasz "czy X już jest w Androidzie".

## Status: nawigacja + przepisy + test Firebase + Spiżarnia/Zakupy (lokalnie)

Co już działa:
- Pełny szkielet projektu Gradle/Kotlin/Compose.
- 229 przepisów wyeksportowanych z web-owego `index.html` do
  `app/src/main/assets/recipes.json` (ten sam kształt danych).
- Ekran listy przepisów: kategorie (Śniadania/Obiady/Kolacje/Deser —
  "Śniadania" łączy `sniadania` i `drugie`, tak jak w wersji webowej, patrz
  `Functional requirements/FR-74.md`), wyszukiwanie, rozwijanie karty po
  kliknięciu.
- Nawigacja dolna (`NavigationBar` + `NavHost`) z pięcioma zakładkami
  odpowiadającymi `nav.bottom` z wersji webowej — Przepisy/Zakupy/Planer/
  Postęp/Spiżarnia — plus przycisk Ustawień w górnym pasku (jak w wersji
  webowej, gdzie Ustawienia też nie są zakładką dolną, tylko ikoną w
  nagłówku).
- Zakładka Ustawienia: na razie tylko test połączenia z Firebase — logowanie
  anonimowe (jak `onAuthStateChanged` w web-owym `index.html`) + zapis i
  odczyt pola `debugPing` w `users/{uid}` w TYM SAMYM projekcie Firebase
  (`dieta-app-323b4`). To potwierdzi, że Firestore realnie działa stąd,
  zanim dobuduję resztę synchronizacji. **Wymaga dodania
  `android/app/google-services.json`** (patrz sekcja "Podłączenie Firebase"
  niżej) — bez tego pliku przycisk "Testuj Firebase" pokaże czytelny błąd
  po polsku zamiast crashować aplikację.
- Zakładki Spiżarnia i Lista zakupów: dodawanie/usuwanie produktów, zmiana
  ilości (Spiżarnia rozróżnia "produkt" z ilością+jednostką i "przyprawę"
  z poziomem Brak/Mało/Wystarczy — jak w wersji webowej), odhaczanie
  kupionych na liście zakupów. **Dane są na razie WYŁĄCZNIE lokalne, w
  pamięci** (znikają po zamknięciu apki) — trwały zapis i synchronizacja
  między urządzeniami to dopiero krok 6 (patrz "Co dalej"). Lista kategorii
  produktów to na razie uproszczony, ręczny zestaw (7 kategorii) — pełna
  baza kategoryzacji składników z wersji webowej jest dużo większa i
  zostanie dociągnięta razem z synchronizacją.

Czego jeszcze NIE ma (kolejne kroki, patrz "Co dalej"):
- Trwałego zapisu i reszty synchronizacji (profil, spiżarnia, zakupy,
  planer itd.) — na razie tylko jedno testowe pole `debugPing` zapisuje się
  do chmury, Spiżarnia/Zakupy trzymane są tylko w pamięci procesu.
- Automatycznego uzupełniania listy zakupów z planera (jak w wersji
  webowej) — na razie dodawanie tylko ręczne.
- Rzeczywistej zawartości pozostałych zakładek (Planer, Postęp) — na razie
  same placeholdery.

**Nie mogłem skompilować ani uruchomić samej aplikacji** (ekranów, nawigacji,
Compose) — środowisko, w którym to piszę, nie ma dostępu do repozytorium
Maven Google (`dl.google.com`), z którego pochodzi Android Gradle Plugin,
AndroidX i Compose. **Wyjątek: moduł `logic/` (patrz "Testy automatyczne"
niżej) faktycznie się kompiluje i jego testy faktycznie przechodzą** — bo
korzysta wyłącznie z Maven Central, który jest dostępny. Reszta (ekrany,
nawigacja, wiązanie z Compose/Firebase) jest napisana starannie, standardowymi,
dobrze udokumentowanymi wzorcami, ale **pierwsza rzecz do zrobienia to
otworzyć to w Android Studio i sprawdzić, czy się buduje i uruchamia**.

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

**Już naprawione:** "Incompatible JDK Version... The selected Kotlin version
(2.0.21) does not support JDK versions newer than 25" — Twój Android Studio
ma zainstalowany bardzo nowy JDK (25+), którego stara wersja Kotlina jeszcze
nie obsługiwała. Podbiłem Kotlina do 2.1.20 (root `build.gradle.kts`) —
zsynchronizuj projekt ponownie po `git pull`. Jeśli po tym Gradle zgłosi
osobny błąd o WŁASNEJ (nie Kotlina) niezgodności z JDK — to zwykle prościej
naprawić w Android Studio: **Settings → Build, Execution, Deployment → Build
Tools → Gradle → Gradle JDK**, wybierz tam starszy wbudowany JDK (17 albo 21)
zamiast podbijać samo Gradle.

## Testy automatyczne

Moduł `logic/` to zwykły moduł Kotlin/JVM (bez Androida/AndroidX) trzymający
logikę biznesową wyciągniętą z ekranów: filtrowanie przepisów
(`RecipeBrowsing`), operacje na spiżarni (`PantryOperations`) i liście
zakupów (`ShoppingOperations`) — te same reguły, które w `app/` wywołują
odpowiednie ViewModel-e (`RecipeViewModel`, `PantryViewModel`,
`ShoppingViewModel` są teraz tylko cienką "sklejką" ze StateFlow, delegującą
do `logic/`).

Dzięki temu, że `logic/` nie potrzebuje niczego z zablokowanego
`dl.google.com`, **jego testy JUnit naprawdę się kompilują i naprawdę
przechodzą w tym środowisku** — to jedyna część całego projektu Android,
którą osobiście zweryfikowałem, że działa (włącznie z sanity-checkiem: celowo
zepsułem jeden test, potwierdziłem że faktycznie failuje, potem cofnąłem).
22 testy w 3 klasach, wszystkie zielone.

**Jak uruchomić w Android Studio:** panel Gradle (z prawej) →
`DietaApp` → `logic` → `Tasks` → `verification` → `test`, albo z terminala
w folderze `android/`: `./gradlew :logic:test` (po tym, jak Android Studio
wygeneruje wrapper przy pierwszym otwarciu — patrz "Jak otworzyć" wyżej).
U Ciebie to powinno zadziałać bez żadnych sztuczek, bo masz normalny dostęp
do internetu — w moim środowisku musiałem tymczasowo wyłączyć moduł `app`
z builda, żeby ominąć blokadę `dl.google.com` (root `build.gradle.kts`
deklaruje pluginy Androida nawet z `apply false`, a to już wymaga
rozwiązania ich wersji).

**Czego to NIE testuje:** samych ekranów Compose (`RecipeListScreen`,
`PantryScreen`, `ShoppingScreen`, `SettingsScreen`) ani nawigacji czy
Firebase — to wymaga prawdziwego builda Androida (instrumentation tests albo
zwykłe ręczne sprawdzenie), czyli dokładnie tego, czego ja nie mogę tu zrobić.
Ale dzięki temu podziałowi reguły biznesowe (co się dzieje po kliknięciu, nie
jak to wygląda) są pokryte testami regresji już teraz, i będą rosły razem
z resztą aplikacji — każda nowa reguła w `PantryOperations`/
`ShoppingOperations`/`RecipeBrowsing` (albo kolejnych takich obiektach)
powinna dostać test w `logic/src/test/...` w tej samej turze pracy.

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

1. ✅ Nawigacja dolna (`NavigationBar` + `NavHost`) między pustymi na razie
   ekranami odpowiadającymi zakładkom z wersji webowej.
2. ⏳ Logowanie anonimowe + zapis/odczyt jednego prostego pola (`debugPing`)
   w `users/{uid}` — kod gotowy w zakładce Ustawienia, ale wymaga dodania
   `android/app/google-services.json`, żeby dało się to sprawdzić w Android
   Studio.
3. ⏳ Ekran Spiżarni i Listy zakupów — struktura najbliższa mapie
   `{nazwa: ilość}`, którą już zna kod webowy, ale na razie WYŁĄCZNIE
   lokalnie (bez trwałego zapisu i bez synchronizacji — to dopiero krok 6).
4. Planer tygodniowy.
5. Logowanie Google/e-mail, profil diety, dopasowanie makro.
6. Synchronizacja (odpowiednik `pushStateToCloud`/`applyRemoteSyncedState`
   z web-owego `index.html` — ten sam model 3-way merge, patrz
   `Functional requirements/FR-78.md`, tylko po stronie Kotlin/Firestore SDK).
7. Przepisy społeczności, lista użytkowników, komentarze (FR-76/FR-77).

Mów, który krok robimy dalej — zrobię go w całości, jak najbliżej gotowego
do sprawdzenia w Android Studio, zamiast zostawiać cię z niedokończonym
plikiem.
