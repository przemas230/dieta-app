# Debug APK do testów na telefonie

`app-debug.apk` w tym folderze to zawsze najnowsza, zbudowana i przeze mnie
zweryfikowana (kompiluje się + działa na emulatorze) wersja debugowa
aplikacji — nadpisywana przy każdym commicie, który zmienia coś w `android/`.
Ścieżka pliku jest stała, więc link poniżej zawsze wskazuje na najnowszą
wersję:

```
https://raw.githubusercontent.com/przemas230/dieta-app/main/android/dist/app-debug.apk
```

Pobierz ten link na telefonie (przeglądarka) i zainstaluj — Android poprosi
o zgodę na instalację z nieznanego źródła przy pierwszym razie (dla danej
przeglądarki/aplikacji plików), to normalne dla APK spoza Google Play.
Ponowne pobranie i instalacja z tego samego linku nadpisuje poprzednią
wersję (ten sam `applicationId`, ten sam podpis debug).

To jest build **debug** (niepodpisany kluczem produkcyjnym) — wystarczający
do klikania/testowania, nie do publikacji.

## Aktualizacja z poziomu aplikacji

Od wersji 0.1.4 aplikacja ma w Ustawieniach kartę "🔄 Aktualizacja aplikacji"
z przyciskiem "Sprawdź aktualizację" — nie trzeba już ręcznie wchodzić po
ten link. Mechanizm (`AppUpdateViewModel.kt`):

1. Pobiera `version.json` z tego folderu i porównuje `versionCode` z
   zainstalowaną wersją (`PackageManager`).
2. Jeśli jest nowsza — przycisk "Pobierz i zainstaluj" ściąga
   `app-debug.apk` do cache aplikacji i przekazuje go systemowemu
   instalatorowi przez `FileProvider`.
3. Android i tak pokaże własne okno potwierdzenia instalacji (tego nie da
   się pominąć dla zwykłej aplikacji spoza sklepu) — przy pierwszym razie
   może też zapytać o zgodę "Instaluj nieznane aplikacje" dla Dieta App,
   trzeba ją zaznaczyć raz w Ustawieniach systemowych.

**Ważne przy każdym release'u:** `version.json` w tym folderze trzeba
aktualizować RĘCZNIE razem z `app-debug.apk` i podbiciem
`versionCode`/`versionName` w `app/build.gradle.kts` — to trzy oddzielne
miejsca, które muszą się zgadzać, inaczej sprawdzanie aktualizacji będzie
kłamać (albo nie pokaże dostępnej aktualizacji, albo pokaże ją w kółko).
