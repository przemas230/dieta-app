package com.przemas230.dietaapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.logic.CATEGORIES
import com.przemas230.dietaapp.logic.ProfileCalculations
import com.przemas230.dietaapp.logic.RecipeMatching
import com.przemas230.dietaapp.logic.forCategory
import kotlinx.coroutines.launch

/**
 * Pure screen content — no own Scaffold/TopAppBar, since the app-level
 * Scaffold in MainActivity now owns the top bar and bottom navigation
 * shared across all tabs.
 */
@Composable
fun RecipeListScreen(profileViewModel: ProfileViewModel, viewModel: RecipeViewModel = viewModel()) {
    val recipes by viewModel.visibleRecipes.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchTerm by viewModel.searchTerm.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val profile by profileViewModel.profile.collectAsState()
    LaunchedEffect(profile.glutenFree, profile.lactoseFree) {
        viewModel.setDietaryFilters(profile.glutenFree, profile.lactoseFree)
    }

    var sortByMatch by remember { mutableStateOf(false) }
    val macroTargets = remember(profile) { ProfileCalculations.calcMacroTargets(profile) }
    val matchScores = remember(recipes, macroTargets, profile) {
        recipes.associate { it.id to RecipeMatching.matchScore(it, macroTargets.forCategory(it.cat), profile) }
    }
    val displayedRecipes = remember(recipes, sortByMatch, matchScores) {
        if (sortByMatch) recipes.sortedByDescending { matchScores[it.id] ?: -1 } else recipes
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchTerm,
            onValueChange = { viewModel.setSearchTerm(it) },
            label = { Text("Szukaj przepisu lub składnika…") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp),
        ) {
            items(CATEGORIES) { category ->
                FilterChip(
                    selected = category.id == selectedCategory,
                    onClick = { viewModel.selectCategory(category.id) },
                    label = { Text("${category.emoji} ${category.label}") },
                )
            }
            item {
                // FR-11/FR-2: sorts the visible list by 🎯 match-to-profile score, descending.
                FilterChip(
                    selected = sortByMatch,
                    onClick = { sortByMatch = !sortByMatch },
                    label = { Text("🎯 Dopasowanie") },
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        when {
            isLoading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Wczytywanie przepisów…")
            }
            displayedRecipes.isEmpty() -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Brak przepisów spełniających kryteria.")
            }
            else -> RecipeListWithScrollToTop(displayedRecipes, matchScores)
        }
    }
}

/**
 * FR-5: a "⬆️" FAB that appears once the list has scrolled roughly 400px
 * past the top (index.html's threshold) and animates back to the first
 * item on tap — same behavior as the web app's floating back-to-top button.
 */
@Composable
private fun RecipeListWithScrollToTop(recipes: List<Recipe>, matchScores: Map<String, Int?>) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val thresholdPx = with(density) { 400.dp.toPx() }
    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > thresholdPx
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(recipes, key = { it.id }) { recipe -> RecipeCard(recipe, matchScores[recipe.id]) }
        }
        AnimatedVisibility(
            visible = showButton,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        ) {
            FloatingActionButton(onClick = {
                coroutineScope.launch { listState.animateScrollToItem(0) }
            }) {
                Text("⬆️")
            }
        }
    }
}

@Composable
private fun RecipeCard(recipe: Recipe, matchScore: Int?) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(recipe.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            val matchSuffix = matchScore?.let { "   🎯 $it%" } ?: ""
            Text("⏱ ${recipe.time}   🔥 ${recipe.kcal} kcal$matchSuffix", style = MaterialTheme.typography.bodySmall)

            if (expanded) {
                Spacer(modifier = Modifier.height(10.dp))
                Text("Składniki", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                recipe.ingredients.forEach { ingredient ->
                    Text("• $ingredient", style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Przygotowanie", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                Text(recipe.method, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
