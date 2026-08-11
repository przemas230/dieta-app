package com.przemas230.dietaapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Faza 1 planu wizualnego dopracowania (2026-08-11, C:\Users\Robert\.claude\plans\curried-tickling-fairy.md):
 * jeden spójny zestaw promieni zaokrągleń zamiast rozrzuconych literałów
 * (2dp/3dp/6dp/12dp/14dp/15dp używanych zamiennie dla wizualnie
 * równoważnych komponentów w różnych ekranach -- ustalone badaniem
 * przed napisaniem tego pliku).
 *
 * `MaterialTheme.shapes` NIE było dotąd nigdzie w tym module nadpisywane
 * (czysty domyślny Material3), mimo że `Card`/`Button`/inne stockowe
 * komponenty i tak z niego korzystają, gdy wywołanie nie podaje własnego
 * `shape=` (np. `RecipeCard`'s domyślna gałąź "else -> MaterialTheme.shapes.medium",
 * RecipeListScreen.kt) -- więc samo podłączenie tego obiektu w
 * `DietaAppTheme` ujednolica promienie kart/przycisków w całej aplikacji
 * bez dotykania każdego ekranu osobno. Miejsca, które dziś jawnie podają
 * własny `RoundedCornerShape(Ndp)` (np. motywy Polaroid/Kafelki, celowo
 * strukturalnie odmienne — patrz FR-49/FR-63) pozostają nietknięte, bo
 * mają to być inne kształty z definicji.
 */
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp),
)
