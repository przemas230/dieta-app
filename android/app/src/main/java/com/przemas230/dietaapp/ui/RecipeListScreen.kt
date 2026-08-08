package com.przemas230.dietaapp.ui

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
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.przemas230.dietaapp.data.Recipe

/**
 * Pure screen content — no own Scaffold/TopAppBar, since the app-level
 * Scaffold in MainActivity now owns the top bar and bottom navigation
 * shared across all tabs.
 */
@Composable
fun RecipeListScreen(viewModel: RecipeViewModel = viewModel()) {
    val recipes by viewModel.visibleRecipes.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchTerm by viewModel.searchTerm.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

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
        }

        Spacer(modifier = Modifier.height(4.dp))

        when {
            isLoading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Wczytywanie przepisów…")
            }
            recipes.isEmpty() -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Brak przepisów spełniających kryteria.")
            }
            else -> LazyColumn(
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(recipes, key = { it.id }) { recipe -> RecipeCard(recipe) }
            }
        }
    }
}

@Composable
private fun RecipeCard(recipe: Recipe) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(recipe.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("⏱ ${recipe.time}   🔥 ${recipe.kcal} kcal", style = MaterialTheme.typography.bodySmall)

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
