package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.Recipe

data class RecipeCategory(val id: String, val label: String, val emoji: String)

/**
 * Same 4-tab grouping as the web app's browse view (FR-74): "Śniadania" and
 * "II Śniadanie" share one tab here too, even though they're still distinct
 * `cat` values in the data (the Planer/meal-slot distinction only matters
 * once there's a planner screen — this is just the recipe browser).
 */
val CATEGORIES = listOf(
    RecipeCategory("sniadania", "Śniadania", "🍳"),
    RecipeCategory("obiady", "Obiady", "🍲"),
    RecipeCategory("kolacje", "Kolacje", "🌙"),
    RecipeCategory("deser", "Deser / Przekąska", "🍰"),
)

/**
 * Pure port of RecipeViewModel.recompute() from the app module — filters by
 * category (with the sniadania+drugie merge) and by a search term matched
 * against the recipe name or any ingredient, case-insensitively.
 */
object RecipeBrowsing {
    fun visibleRecipes(all: List<Recipe>, category: String, searchTerm: String): List<Recipe> {
        val matchesCategory: (Recipe) -> Boolean = { recipe ->
            if (category == "sniadania") recipe.cat == "sniadania" || recipe.cat == "drugie"
            else recipe.cat == category
        }
        val term = searchTerm.trim().lowercase()
        return all.filter { recipe ->
            matchesCategory(recipe) &&
                (term.isEmpty() ||
                    recipe.name.lowercase().contains(term) ||
                    recipe.ingredients.any { it.lowercase().contains(term) })
        }
    }
}
