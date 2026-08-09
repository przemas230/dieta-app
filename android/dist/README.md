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
