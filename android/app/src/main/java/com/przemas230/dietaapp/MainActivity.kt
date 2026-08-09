package com.przemas230.dietaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.przemas230.dietaapp.logic.ProfileCalculations
import com.przemas230.dietaapp.ui.PantryScreen
import com.przemas230.dietaapp.ui.PlaceholderScreen
import com.przemas230.dietaapp.ui.ProfileViewModel
import com.przemas230.dietaapp.ui.RecipeListScreen
import com.przemas230.dietaapp.ui.SettingsScreen
import com.przemas230.dietaapp.ui.ShoppingScreen
import com.przemas230.dietaapp.ui.navigation.BOTTOM_NAV_SCREENS
import com.przemas230.dietaapp.ui.navigation.Screen
import com.przemas230.dietaapp.ui.theme.DietaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DietaAppTheme {
                DietaAppRoot()
            }
        }
    }
}

/**
 * App-level shell: one top bar (title + Ustawienia action, matching the web
 * app's header gear icon — see FR list in android/PARITY.md), one bottom
 * nav row mirroring index.html's `nav.bottom`, and a NavHost swapping the
 * screen content underneath both. Every tab besides Przepisy is currently
 * a PlaceholderScreen — see android/README.md "Co dalej" for the build order.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DietaAppRoot() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    // Created here (not inside SettingsScreen's default viewModel() param) so
    // it's scoped to the whole NavHost/Activity — the header subtitle below
    // and the profile form on the Ustawienia screen must share one instance,
    // otherwise Compose Navigation would hand each destination its own.
    val profileViewModel: ProfileViewModel = viewModel()
    val profile by profileViewModel.profile.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Dieta App")
                        val subtitle = if (!profile.configured) {
                            "👋 Ustaw swój profil w Ustawieniach, aby dopasować dietę do siebie"
                        } else {
                            val targets = ProfileCalculations.calcTargets(profile)
                            "${profile.sex.label}, ${profile.age} lat · ${profile.heightCm} cm · " +
                                "${formatWeight(profile.weightKg)} kg → cel ${formatWeight(profile.targetWeightKg)} kg · " +
                                "${profile.goal.headerLabel} · ~${targets.daily} kcal/dzień"
                        }
                        Text(
                            subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.Settings.route) { launchSingleTop = true }
                    }) {
                        Icon(Screen.Settings.icon, contentDescription = Screen.Settings.label)
                    }
                },
            )
        },
        bottomBar = {
            NavigationBar {
                BOTTOM_NAV_SCREENS.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                    )
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Recipes.route,
            modifier = Modifier.padding(padding),
        ) {
            composable(Screen.Recipes.route) { RecipeListScreen(profileViewModel = profileViewModel) }
            composable(Screen.Shopping.route) { ShoppingScreen() }
            composable(Screen.Planner.route) { PlaceholderScreen(Screen.Planner.label) }
            composable(Screen.Progress.route) { PlaceholderScreen(Screen.Progress.label) }
            composable(Screen.Pantry.route) { PantryScreen() }
            composable(Screen.Settings.route) { SettingsScreen(profileViewModel = profileViewModel) }
        }
    }
}

/** "67.0" -> "67", "67.5" -> "67.5" — matches how JS template literals print numbers. */
private fun formatWeight(value: Double): String =
    if (value == value.toLong().toDouble()) value.toLong().toString() else value.toString()
