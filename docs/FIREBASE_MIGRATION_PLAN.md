# Plan: Firebase, konta, wspólne gospodarstwo domowe, społeczność

Ten dokument to techniczny plan, nie opis obecnego zachowania aplikacji (od
tego jest folder `Functional requirements/`). Spisany na podstawie prośby o
podłączenie Firebase, logowanie Google (opcjonalne), wspólną spiżarnię/listę
zakupów dla gospodarstwa domowego, przepisy dodawane przez użytkowników z
ocenami i komentarzami, oraz działanie offline z synchronizacją po
odzyskaniu sieci.

**AKTUALIZACJA 2026-08-08: projekt Firebase już istnieje** (`dieta-app-323b4`)
i logowanie (anonimowe/Google/e-mail) jest już podłączone i działa — patrz
FR-69 w `Functional requirements/`. Sekcje poniżej opisujące "co zrobić po
założeniu projektu" odnoszą się już tylko do TEGO, co jeszcze zostało:
głównie właściwa synchronizacja danych (spiżarnia/lista zakupów/planer) i
gospodarstwo domowe. Reszta tego dokumentu (model danych, reguły
bezpieczeństwa) jest nadal aktualnym planem na te pozostałe kroki.

## Co już zrobiono (działa dziś)

- **Nazwa użytkownika w aplikacji** (`state.displayName`, ustawienia →
  „👤 Konto"): dowolny, opcjonalny pseudonim, niezależny od jakiegokolwiek
  konta Google, pokazywany w nagłówku. To jest dokładnie ten sam pseudonim,
  którego trzeba będzie użyć jako `displayName` w przyszłym dokumencie
  użytkownika w Firestore — nic tu nie trzeba będzie przerabiać, tylko
  przenieść wartość z localStorage do bazy przy pierwszej synchronizacji.
- **Projekt Firebase założony** (`dieta-app-323b4`), SDK (Auth + Firestore,
  wariant compat) podłączone w `index.html`, prawdziwy `firebaseConfig`
  wpisany w kodzie (bezpieczne — to nie jest sekret).
- **Logowanie anonimowe** — każde urządzenie automatycznie i bez pytania
  loguje się jako użytkownik anonimowy przy starcie.
- **Logowanie Google i e-mail+hasło** (Ustawienia → „☁️ Konto w chmurze”) —
  obie metody `linkWithPopup`/`linkWithCredential` na istniejącym
  anonimowym koncie, z obsługą kolizji "ten e-mail już istnieje".
- **Firestore używane przez `saveState()`** dla danych OSOBISTYCH (profil,
  spiżarnia, ulubione, własne przepisy, oceny, ustawienia — pełna lista w
  Functional requirements/FR-73), gdy użytkownik jest zalogowany na
  prawdziwe (nie anonimowe) konto — z `enablePersistence` dla trybu
  offline i `onSnapshot` do synchronizacji na żywo między urządzeniami.
  Dane WSPÓLNE gospodarstwa domowego (planer/lista zakupów/historia
  gotowania) wciąż żyją wyłącznie w localStorage — to następny krok (patrz
  "Checklist" niżej, punkt 8).

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

## Reguły bezpieczeństwa Firestore

**AKTUALIZACJA 2026-08-08: `recipes/{id}`, `recipes/{id}/ratings/{uid}` i
`publicProfiles/{uid}` (+ `publicProfiles/{uid}/reviewedRecipes/{recipeId}`)
są już używane przez kod aplikacji** (patrz Functional requirements/FR-76,
FR-77) — poniższe reguły są GOTOWE DO WKLEJENIA (Firebase Console →
Firestore Database → Reguły), nie szkicem. Bez nich Firestore w trybie
produkcyjnym domyślnie ODRZUCA każdy odczyt/zapis do tych kolekcji — więc
najbardziej prawdopodobny efekt braku tego kroku to "funkcja nic nie
pokazuje/nie zapisuje", NIE wyciek danych (bezpieczne domyślne zachowanie).
`households/*` w tym szkicu wciąż czeka na punkt 8 checklisty niżej.

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {

    match /users/{uid} {
      allow read, write: if request.auth != null && request.auth.uid == uid;
    }

    match /publicProfiles/{uid} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == uid;

      match /reviewedRecipes/{recipeId} {
        allow read: if request.auth != null;
        allow write: if request.auth != null && request.auth.uid == uid;
      }
    }

    match /recipes/{recipeId} {
      allow read: if resource.data.status == "approved"
        || (request.auth != null && request.auth.uid == resource.data.authorUid);
      allow create: if request.auth != null
        && request.auth.uid == request.resource.data.authorUid
        && request.resource.data.status == "pending";
      // Autor może poprawić inne pola (np. literówkę), ale NIE może sam
      // zmienić status z "pending" na "approved" — to zatwierdza tylko Ty,
      // ręcznie w konsoli Firebase (edycja pola w konsoli nie przechodzi
      // przez te reguły, więc to nadal działa).
      allow update: if request.auth != null
        && request.auth.uid == resource.data.authorUid
        && request.resource.data.status == resource.data.status;
      allow delete: if request.auth != null && request.auth.uid == resource.data.authorUid;

      match /ratings/{uid} {
        allow read: if request.auth != null;
        allow write: if request.auth != null && request.auth.uid == uid;
      }
    }

    match /households/{hid} {
      allow read, write: if request.auth != null && request.auth.uid in resource.data.memberUids;
      match /{collection}/{docId} {
        allow read, write: if request.auth != null && request.auth.uid in
          get(/databases/$(database)/documents/households/$(hid)).data.memberUids;
      }
    }
  }
}
```

Priorytet dla `households/*` pozostaje szkicem, dopóki punkt 8 checklisty
(UI gospodarstwa) nie powstanie — nie ma dziś żadnego kodu, który by z
tego czytał/pisał, więc wklejenie samej reguły nic nie zmienia w
zachowaniu aplikacji, ale nie zaszkodzi na zapas.

## Przepisy społecznościowe — moderacja

**AKTUALIZACJA 2026-08-08: zaimplementowane** (patrz Functional
requirements/FR-76). Zgodnie z pierwotnym planem poniżej:

- ✅ `state.communityRecipesEnabled` (domyślnie `false`, synchronizowane w
  `users/{uid}` — FR-73) — gdy wyłączony, lista przepisów pokazuje tylko
  229 wbudowanych i własne (`state.myRecipes`).
- ✅ Gdy włączony (i użytkownik zalogowany na prawdziwe konto): dodatkowo
  pokazują się przepisy `source: "community"` ze `status: "approved"` —
  NIGDY automatycznie `"pending"`.
- ✅ Formularz "Dodaj swój przepis" zapisuje nowy dokument w `recipes/` ze
  `status: "pending"`, widoczny od razu tylko dla autora (lokalnie, przez
  `state.myRecipes` — niezależnie od statusu zatwierdzenia w chmurze).
- ✅ Zatwierdzanie `"pending" → "approved"`: ręczna zmiana pola w konsoli
  Firebase (Ty jako jedyny moderator) — reguły bezpieczeństwa wyżej
  jawnie NIE pozwalają autorowi samodzielnie zmienić własny status.
- ⬜ Jeszcze nie zrobione: sortowanie po ŚREDNIEJ ocenie od wszystkich
  użytkowników. Dziś sortowanie "🏆" nadal patrzy tylko na `state.
  recipeReviews` (Twoją własną ocenę na tym urządzeniu) — prawdziwa,
  zagregowana średnia wymagałaby osobnego pola `avgRating`/`ratingCount`
  na dokumencie przepisu, aktualizowanego np. Cloud Function przy każdej
  nowej ocenie (żeby nie czytać całej podkolekcji ocen tylko po to, by
  posortować listę). Komentarze pod przepisem (FR-77) DZIAŁAJĄ już w pełni
  wielo-użytkownikowo — to tylko SORTOWANIE listy wciąż patrzy lokalnie.

## Checklist

1. ✅ Firebase Console → nowy projekt → włącz Authentication (Anonymous +
   Google + e-mail/hasło) → włącz Firestore (tryb produkcyjny). *(zrobione
   2026-08-08, projekt `dieta-app-323b4`)*
2. ✅ Google Cloud Console → OAuth consent screen + Web client ID (Firebase
   zrobiło to automatycznie przy włączaniu logowania Google).
3. ✅ Dodano Firebase SDK (`firebase-app-compat`, `firebase-auth-compat`,
   `firebase-firestore-compat`) do `index.html` przez `<script>` z CDN
   (wybrano wariant "compat" zamiast modułów ES, żeby pasował do
   istniejącej architektury jednego wielkiego klasycznego `<script>`, bez
   przepisywania całej aplikacji na moduły).
4. ✅ Wklejono prawdziwy config (`apiKey`, `authDomain`, `projectId`, ...).
5. ✅ Logowanie: anonimowe na starcie + opcjonalne połączenie z Google lub
   e-mailem/hasłem (`linkWithPopup`/`linkWithCredential`), z obsługą
   kolizji "ten e-mail już istnieje". Patrz FR-69.
6. ✅ `saveState()` dodatkowo (z 1,5s debounce) zapisuje wycinek danych
   OSOBISTYCH (`users/{uid}`, patrz Functional requirements/FR-73) do
   Firestore, z `onSnapshot` nasłuchującym zmian na żywo. Dotyczy na razie
   tylko danych osobistych z listy w FR-73 (profil, spiżarnia, ulubione,
   własne przepisy, oceny, ustawienia) — dane WSPÓLNE gospodarstwa
   (planer/lista zakupów/historia gotowania) czekają na krok 8 poniżej,
   żeby nie synchronizować ich jako danych osobistych i nie wymagać potem
   migracji na inny model. *(zrobione 2026-08-08)*
7. ✅ Jednorazowa migracja zrobiona jako część kroku 6: pierwsze
   zalogowanie na dane konto (`users/{uid}` jeszcze nie istnieje w chmurze)
   wysyła obecny lokalny stan urządzenia jako punkt startowy. *(zrobione
   2026-08-08)*
8. ⬜ Dodaj UI gospodarstwa domowego: formularz "Dołącz do gospodarstwa" /
   "Utwórz gospodarstwo" (dopiero ma sens, gdy krok 6 faktycznie
   synchronizuje dane — inaczej byłby to formularz udający działanie,
   czego świadomie unikamy, patrz FR-68).
9. ✅ Przepisy społeczności, komentarze wielo-użytkownikowe i przeglądana
   lista użytkowników/profili zaimplementowane po stronie klienta (FR-76,
   FR-77). *(zrobione 2026-08-08)*
10. ⬜ **Wymagana Twoja akcja w konsoli Firebase, żeby krok 9 zadziałał:**
   wklej reguły bezpieczeństwa z sekcji wyżej (Firebase Console → Firestore
   Database → Reguły → zastąp całą treść → Opublikuj). Bez tego kroku
   Firestore w trybie produkcyjnym domyślnie ODRZUCA każdy odczyt/zapis do
   `recipes/*` i `publicProfiles/*` — funkcja po prostu nic nie pokaże ani
   nie zapisze (bezpieczne, ale niedziałające), dopóki reguły nie zostaną
   wklejone.

## Jeśli jednak przepiszesz na Kotlin / Android Studio

Ten sam model danych Firestore przenosi się bez zmian — Firestore ma
oficjalny SDK na Androida (Kotlin), więc kolekcje/dokumenty opisane wyżej
są identyczne niezależnie od tego, czy czyta je ta aplikacja webowa, czy
natywna aplikacja Kotlin. Praca włożona w zaprojektowanie modelu danych i
reguł bezpieczeństwa nie przepada przy zmianie technologii klienta — zmienia
się tylko język/framework UI, nie backend.
