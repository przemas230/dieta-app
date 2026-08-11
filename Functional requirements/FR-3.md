# FR-3: Karta przepisu — widok skrócony i rozwinięty

**Obszar:** Przepisy i przeglądanie  
**Status:** Zaimplementowane

## Opis
Każdy przepis wyświetlany jest jako karta z nazwą, czasem przygotowania, kalorycznością i skrótowymi znacznikami (np. podwyższony IG, dopasowanie do celu). Domyślnie karta jest zwinięta. Rozwijanie jest dwuetapowe: pierwsze stuknięcie w zwiniętą kartę WYŁĄCZNIE przewija ją do widoku (wyśrodkowuje), nie rozwijając jej jeszcze — dopiero drugie stuknięcie tej samej, już wyśrodkowanej karty faktycznie rozwija pełną listę składników, sposób przygotowania i przyciski akcji (i ponownie ją pozycjonuje). Zwinięcie rozwiniętej karty z powrotem nadal działa jednym, natychmiastowym stuknięciem — dwuetapowość dotyczy wyłącznie otwierania.

## Kryteria akceptacji
- Karta w stanie zwiniętym pokazuje tylko nagłówek i podstawowe metadane.
- Rozwinięcie karty odbywa się WYŁĄCZNIE przez wyraźne, stacjonarne stuknięcie — nie przez przypadkowe zatrzymanie przewijania listy (patrz historia rewizji poniżej i FR-44).
- Pierwsze stuknięcie zwiniętej karty przewija ją do widoku, ale jej NIE rozwija. Drugie stuknięcie tej samej karty (bez stuknięcia innej karty pomiędzy) rozwija ją. Stuknięcie innej, zwiniętej karty pomiędzy tymi dwoma stuknięciami traktuje TĘ nową kartę jako pierwsze stuknięcie (nie rozwija poprzednio dotykanej).
- Tylko jedna karta na liście może być rozwinięta jednocześnie.
- Po rozwinięciu karty ekran automatycznie przewija się tak, żeby cała rozwinięta karta wylądowała na środku widocznego obszaru — użytkownik nie musi ręcznie doprzewijać, żeby zobaczyć składniki i sposób przygotowania. Przewinięcie następuje PO zakończeniu animacji rozwijania karty (nie w trakcie), żeby wyśrodkowanie trafiało na docelową, już powiększoną wysokość karty, a nie na jej wysokość sprzed rozwinięcia. Jeśli rozwinięta karta jest WYŻSZA niż widoczny obszar ekranu, wyśrodkowanie zastępowane jest wyrównaniem górnej krawędzi karty do góry widoku (samo wyśrodkowanie ucinałoby wtedy tytuł/początek karty poza ekranem).

## Uwagi
Zrewidowane w rundzie z 2026-08-03: pierwotna wersja pozwalała, by dotknięcie kończące przewijanie listy (bardzo mały ruch palca przy jednoczesnym przewinięciu strony przez inercję) było błędnie odczytane jako stuknięcie i rozwijało kartę, co powodowało 'skakanie' ekranu. Naprawiono porównując pozycję przewijania strony w momencie dotknięcia i puszczenia — jeśli strona przewinęła się w tym czasie, gest NIE liczy się jako stuknięcie, nawet jeśli sam palec poruszył się nieznacznie. Patrz też FR-44.

## Historia rewizji
- **v1** (2026-08-03): Pierwsza wersja wymagania, spisana retrospektywnie na podstawie poleceń użytkownika i release notes z dotychczasowych rund prac.
- **v2** (2026-08-03): Doprecyzowano zachowanie na podstawie zgłoszonej poprawki — patrz sekcja "Uwagi" powyżej.
- **v3** (2026-08-08): Dodano automatyczne wyśrodkowywanie rozwiniętej karty na ekranie, na życzenie użytkownika ("karta z przepisem na którą klikniemy [powinna] wyśrodkowywać się na ekranie... użytkownik nie musi sam jej przesuwać").
- **v4** (2026-08-11): Rozbito otwieranie na dwa etapy, na wyraźną prośbę użytkownika ("zmień żeby wysrodkowywalo kafelek dopiero po kliknięciu na niego a dopiero po drugim kliknięciu żeby go rozwijało i wysrodkowywalo albo jak się nie mieści na ekranie to żeby był wyświetlony od góry") — pierwsze stuknięcie tylko centruje, drugie rozwija; dodano też wariant "wyrównaj do góry" dla kart wyższych niż ekran, zamiast zawsze centrować (co ucinałoby górę zbyt wysokiej karty). Zamknięcie nadal jednym stuknięciem.
