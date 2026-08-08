package com.przemas230.dietaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.przemas230.dietaapp.ui.PlaceholderScreen
import com.przemas230.dietaapp.ui.RecipeListScreen
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dieta App") },
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
            composable(Screen.Recipes.route) { RecipeListScreen() }
            composable(Screen.Shopping.route) { PlaceholderScreen(Screen.Shopping.label) }
            composable(Screen.Planner.route) { PlaceholderScreen(Screen.Planner.label) }
            composable(Screen.Progress.route) { PlaceholderScreen(Screen.Progress.label) }
            composable(Screen.Pantry.route) { PlaceholderScreen(Screen.Pantry.label) }
            composable(Screen.Settings.route) { PlaceholderScreen(Screen.Settings.label) }
        }
    }
}
