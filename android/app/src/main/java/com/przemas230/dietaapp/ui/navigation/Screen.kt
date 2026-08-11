package com.przemas230.dietaapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Mirrors the web app's bottom nav (index.html, `nav.bottom` — Przepisy,
 * Zakupy, Planer, Postęp, Spiżarnia) plus Settings, which in the web app
 * lives behind a gear icon in the header rather than the bottom nav — same
 * placement here (top bar action, not a 6th bottom tab).
 */
sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Recipes : Screen("recipes", "Przepisy", Icons.Filled.Restaurant)
    data object Shopping : Screen("shopping", "Zakupy", Icons.Filled.ShoppingCart)
    data object Planner : Screen("planner", "Planer", Icons.Filled.CalendarMonth)
    data object Progress : Screen("progress", "Postęp", Icons.Filled.TrendingUp)
    data object Pantry : Screen("pantry", "Spiżarnia", Icons.Filled.Inventory2)
    data object Settings : Screen("settings", "Ustawienia", Icons.Filled.Settings)
    // FR-76: reached via a button in Ustawienia, not the bottom nav -- see
    // BOTTOM_NAV_SCREENS below (Settings itself is also left out of it).
    data object UserList : Screen("userList", "Użytkownicy społeczności", Icons.Filled.Group)
    data object UserProfile : Screen("userProfile/{uid}", "Profil użytkownika", Icons.Filled.Person) {
        fun routeFor(uid: String) = "userProfile/$uid"
    }
}

val BOTTOM_NAV_SCREENS = listOf(
    Screen.Recipes,
    Screen.Shopping,
    Screen.Planner,
    Screen.Progress,
    Screen.Pantry,
)
