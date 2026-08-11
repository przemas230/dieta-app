package com.przemas230.dietaapp.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.przemas230.dietaapp.logic.FavoriteDishSearch
import com.przemas230.dietaapp.logic.IngredientCanon
import com.przemas230.dietaapp.logic.MealTimeChoice

/**
 * Floating-lightbulb dish-idea search (Recipes tab only, user request
 * 2026-08-11): asks which meal this is for FIRST, then picks up to 5
 * favorite ingredients diversified across food categories
 * ([FavoriteDishSearch.pickDiverseIngredients]) and opens a Google search
 * for a real dish idea -- same `ACTION_VIEW` + `google.com/search?q=`
 * mechanism already used by the recipe-title tap (`RecipeListScreen.kt`).
 *
 * Distinct from FR-32's original inline "💡 Pomysł na danie" button (2
 * random favorites, in-app templated text, no search) which this floating
 * button replaces on Android -- see FR-32's revision history and
 * `PARITY.md` for why web keeps the old behavior for now (deliberate,
 * documented gap, not an oversight).
 */
@Composable
fun FavoriteDishIdeaDialog(favIngredients: Set<String>, onDismiss: () -> Unit) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.widthIn(max = 480.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "💡 Pomysł na danie",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = onDismiss) { Text("✕") }
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (favIngredients.isEmpty()) {
                    Text(
                        "Zaznacz gwiazdką ☆ ulubione składniki w przepisach, żeby skorzystać z tej funkcji.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                } else {
                    Text("Na jaki posiłek szukamy pomysłu?", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(10.dp))
                    MealTimeChoice.ALL.forEach { meal ->
                        Button(
                            onClick = {
                                val ingredients = FavoriteDishSearch.pickDiverseIngredients(
                                    favIngredients,
                                    categoryOf = { IngredientCanon.CANON_INFO[it]?.cat ?: "Inne" },
                                )
                                val query = Uri.encode(FavoriteDishSearch.buildSearchQuery(meal, ingredients))
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=$query")),
                                )
                                onDismiss()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                        ) { Text(meal.label) }
                    }
                }
            }
        }
    }
}
