# Plan: Firebase, konta, wspólne gospodarstwo domowe, społeczność

Ten dokument to techniczny plan, nie opis obecnego zachowania aplikacji (od
tego jest folder `Functional requirements/`). Spisany na podstawie prośby o
podłączenie Firebase, logowanie Google (opcjonalne), wspólną spiżarnię/listę
zakupów dla gospodarstwa domowego, przepisy dodawane przez użytkowników z
ocenami i komentarzami, oraz działanie offline z synchronizacją po
odzyskaniu sieci.

Zakres tej rundy prac: **przygotowanie, bez prawdziwego Firebase**. Nie da
się podłączyć prawdziwego logowania Google ani synchronizacji bez
utworzenia projektu Firebase i klucza OAuth — to wymaga zalogowania się do
konsoli Firebase/Google Cloud własnym kontem Google, czego nie da się
zrobić automatycznie. Ten plik jest gotowym punktem startowym na jutro.

## Co już zrobiono w tej rundzie (działa dziś, bez Firebase)

- **Nazwa użytkownika w aplikacji** (`state.displayName`, ustawienia →
  „👤 Konto"): dowolny, opcjonalny pseudonim, niezależny od jakiegokolwiek
  konta Google, pokazywany w nagłówku. To jest dokładnie ten sam pseudonim,
  którego trzeba będzie użyć jako `displayName` w przyszłym dokumencie
  użytkownika w Firestore — nic tu nie trzeba będzie przerabiać, tylko
  przenieść wartość z localStorage do bazy przy pierwszym logowaniu.

## Dlaczego zaskakująco mało trzeba dopisywać "warstwy synchronizacji"

Cała obecna logika aplikacji operuje na jednym obiekcie `state` (patrz
`loadState()`/`saveState()` w `index.html`) z czytelnymi, już wydzielonymi
kolekcjami: `state.pantry`, `state.shopping`, `state.planner`, `state.cooked`,
`state.profile`, `state.history`, itd. To jest dokładnie ten sam kształt,
jakiego potrzebuje Firestore — każda z tych kolekcji może stać się osobną
kolekcją Firestore niemal 1:1, bez przeprojektowywania logiki biznesowej
(wyliczenia kcal/IG/dopasowania, generowanie listy zakupów itd. zostają
bez zmian — zmienia się tylko *skąd* `state` jest czytany/zapisywany).

Dodatkowo: **Firestore ma wbudowaną, darmową obsługę "offline-first, sync
po powrocie sieci"** (`enableIndexedDbPersistence` w wersji web, włączone
domyślnie w SDK na Androida/Kotlin) — to dokładnie wymagane zachowanie
("cała aplikacja ma działać offline a po podłączeniu do sieci się
synchronizować"). Nie trzeba pisać własnej kolejki synchronizacji ani
mechanizmu retry — Firestore robi to automatycznie, z rozsądnym
domyślnym rozwiązywaniem konfliktów (ostatni zapis wygrywa per pole
dokumentu). Jedyne co ewentualnie warto dopisać ręcznie w przyszłości to
timestamp `updatedAt` na rekordach współdzielonych (spiżarnia, lista
zakupów) — ale to drobna rzecz do dodania RAZEM z properną integracją
Firestore, nie osobno wcześniej (stąd nie zrobiono tego w tej rundzie —
dodawanie tych pól bez realnego mechanizmu, który by z nich korzystał,
byłoby martwym kodem).

## Proponowany model danych (Firestore)

```
users/{uid}
  displayName: string          // ten sam pseudonim co dziś w localStorage
  profile: {...}                // płeć/wiek/wzrost/waga/cel/filtry — dziś state.profile
  theme: string                 // per-urządzenie, nie per-gospodarstwo
  uiScale: number               // per-urządzenie
  householdId: string | null    // do którego gospodarstwa należy (jeśli dołączył)
  communityRecipesEnabled: bool // czy pokazywać przepisy dodane przez innych użytkowników

households/{householdId}
  name: string
  memberUids: string[]
  inviteCode: string             // krótki kod do dołączenia (patrz niżej)
  createdBy: uid
  createdAt: timestamp

households/{householdId}/pantry/{itemId}
  canonName, qty, unitCat, category, updatedAt, updatedByUid

households/{householdId}/shoppingList/{itemId}
  name, unitCat, qty, checked, contributions, updatedAt, updatedByUid

households/{householdId}/planner/{dayIndex}_{catId}
  recipeId, scale, updatedAt, updatedByUid

households/{householdId}/cookHistory/{entryId}
  recipeId, date, rating, cookedByUid

recipes/{recipeId}
  ...pola jak dziś w RECIPES (name, cat, ingredients, kcal, protein, ...)
  source: "built-in" | "community"
  authorUid: uid | null
  status: "approved" | "pending" | "rejected"   // moderacja

recipes/{recipeId}/ratings/{uid}
  stars: 1-5
  comment: string | null
  createdAt: timestamp
```

Uwaga o `households/*` vs `users/*`: dane WSPÓŁDZIELONE (spiżarnia, lista
zakupów, planer, historia gotowania) żyją pod gospodarstwem, nie pod
użytkownikiem — to jest właśnie sedno prośby "jak moja żona coś zje lub
przygotuje danie to mi też składniki z mojej spiżarni się odejmują". Dane
OSOBISTE (profil diety, motyw, skala UI, `communityRecipesEnabled`) zostają
per-użytkownik, bo różne osoby w gospodarstwie mogą mieć różne cele/diety
mimo wspólnej spiżarni.

## Logowanie: opcjonalne, nie wymagane

Zgodnie z prośbą ("na pewno dodaj możliwość zalogowania się do konta
google, nie konieczność ale możliwość"):

1. Domyślnie: **Firebase Anonymous Authentication** — każde urządzenie
   dostaje `uid` bez żadnego logowania, aplikacja działa dokładnie jak dziś
   (jeden użytkownik, jedno urządzenie, dane prywatne).
2. Opcjonalnie: przycisk „Połącz z kontem Google” w Ustawieniach →
   `linkWithPopup(auth.currentUser, googleProvider)` — to KLUCZOWA metoda:
   *linkuje* istniejące anonimowe konto z Google zamiast zakładać nowe, więc
   wszystkie dotychczasowe dane (spiżarnia, historia, ulubione) zostają przy
   tym samym `uid` i się nie gubią. To jedyny bezpieczny sposób na
   "logowanie opcjonalne" bez ryzyka utraty danych.
3. Zalogowanie kontem Google daje tylko: bezpieczne dane na wielu
   urządzeniach + możliwość dołączenia do gospodarstwa (dołączenie do
   `households/*` wymaga trwałego `uid`, którego nie da się bezpiecznie
   przenieść między telefonami bez logowania).

## Wspólne gospodarstwo domowe — dołączanie

Najprostszy, bezpieczny sposób bez własnego systemu zaproszeń e-mail:

1. Osoba zakładająca gospodarstwo dostaje krótki `inviteCode` (np. 6 znaków,
   wygenerowany raz przy tworzeniu `households/{id}`).
2. Druga osoba wpisuje kod w Ustawieniach → „Dołącz do gospodarstwa” →
   zapytanie Firestore po `inviteCode` → dopisanie jej `uid` do
   `memberUids` i ustawienie `users/{uid}.householdId`.
3. Od tego momentu czytanie/zapisywanie spiżarni, listy zakupów i planera
   w aplikacji przełącza się z danych osobistych na
   `households/{householdId}/...`.

## Reguły bezpieczeństwa Firestore (szkic)

```
match /households/{hid} {
  allow read, write: if request.auth.uid in resource.data.memberUids;
  match /{collection}/{docId} {
    allow read, write: if request.auth.uid in
      get(/databases/$(database)/documents/households/$(hid)).data.memberUids;
  }
}
match /users/{uid} {
  allow read, write: if request.auth.uid == uid;
}
match /recipes/{rid} {
  allow read: if resource.data.status == "approved" || request.auth.uid == resource.data.authorUid;
  allow create: if request.auth != null;
  allow update, delete: if request.auth.uid == resource.data.authorUid;
}
match /recipes/{rid}/ratings/{uid} {
  allow read: if true;
  allow write: if request.auth.uid == uid;
}
```

(Szkic do dopracowania w Firebase Console — priorytet: żaden użytkownik nie
może czytać/pisać do gospodarstwa, do którego nie należy.)

## Przepisy społecznościowe — moderacja

Zgodnie z prośbą o włącznik w ustawieniach:

- `users/{uid}.communityRecipesEnabled` (domyślnie `false`) — gdy
  wyłączony, lista przepisów pokazuje tylko 229 wbudowanych (`source:
  "built-in"`).
- Gdy włączony: dodatkowo pokazują się przepisy `source: "community"` ze
  `status: "approved"` — NIGDY automatycznie `"pending"`, żeby uniknąć
  pokazywania niesprawdzonych/spamowych zgłoszeń wszystkim.
- Formularz "Dodaj swój przepis" zapisuje nowy dokument w `recipes/` ze
  `status: "pending"`, widoczny od razu tylko dla autora.
- Zatwierdzanie `"pending" → "approved"`: na start wystarczy ręczna zmiana
  pola w konsoli Firebase (Ty jako jedyny "moderator"); docelowo prosty
  panel albo Cloud Function.
- Sortowanie po ocenie: średnia z `recipes/{id}/ratings/*` licząca się do
  osobnego pola `avgRating`/`ratingCount` na dokumencie przepisu
  (aktualizowanego np. Cloud Function przy każdej nowej ocenie, żeby nie
  trzeba było czytać całej podkolekcji ocen tylko po to, by posortować listę).

## Checklist na jutro (gdy założysz projekt Firebase)

1. Firebase Console → nowy projekt → włącz Authentication (Anonymous +
   Google) → włącz Firestore (tryb produkcyjny).
2. Google Cloud Console → OAuth consent screen + Web client ID (Firebase
   robi to częściowo automatycznie przy włączaniu logowania Google).
3. Dodaj Firebase SDK (`firebase-app`, `firebase-auth`, `firebase-firestore`)
   do `index.html` (CDN `<script type="module">` albo bundler, do wyboru).
4. Wklej config z konsoli (`apiKey`, `authDomain`, `projectId`, ...) —
   BEZPIECZNE do trzymania w kliencie, to nie są sekrety serwerowe.
5. Podmień wewnętrzne działanie `loadState()`/`saveState()`: zamiast
   `localStorage`, czytaj/pisz z Firestore (`onSnapshot` do
   nasłuchiwania zmian na żywo — ważne dla współdzielenia z drugą osobą w
   czasie rzeczywistym), z `enableIndexedDbPersistence()` dla trybu offline.
6. Jednorazowa migracja: przy pierwszym logowaniu, jeśli
   `users/{uid}` jeszcze nie istnieje, wgraj obecny lokalny `state` jako
   punkt startowy zamiast zaczynać od zera.
7. Dodaj UI: przycisk logowania Google, formularz "Dołącz do gospodarstwa"
   / "Utwórz gospodarstwo", przełącznik przepisów społecznościowych,
   formularz dodawania własnego przepisu, gwiazdki + komentarz przy
   przepisie.
8. Wdróż reguły bezpieczeństwa (szkic wyżej) — PRZED udostępnieniem komukolwiek.

## Jeśli jednak przepiszesz na Kotlin / Android Studio

Ten sam model danych Firestore przenosi się bez zmian — Firestore ma
oficjalny SDK na Androida (Kotlin), więc kolekcje/dokumenty opisane wyżej
są identyczne niezależnie od tego, czy czyta je ta aplikacja webowa, czy
natywna aplikacja Kotlin. Praca włożona w zaprojektowanie modelu danych i
reguł bezpieczeństwa nie przepada przy zmianie technologii klienta — zmienia
się tylko język/framework UI, nie backend.
