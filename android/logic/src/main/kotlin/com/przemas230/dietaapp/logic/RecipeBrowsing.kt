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

// Port of GLUTEN_KEYWORDS/isGlutenFree from index.html — substring match
// against ingredients+name, lowercase. Orientation only, not a certified
// analysis (see FR-8's own caveat).
private val GLUTEN_KEYWORDS = listOf(
    "chleb", "bułk", "pęczak", "peczak", "bulgur", "jęczmien", "jeczmien",
    "orkiszow", "tortilla", "wafle żytnie", "wafle gryczane", "kuskus",
    "kasza manna", "makaron",
)

// Port of DAIRY_KEYWORDS/isLactoseFree from index.html — a dairy ingredient
// passes only if its own text explicitly says "bez laktozy".
private val DAIRY_KEYWORDS = listOf(
    "mleko", "mleka", "śmietan", "smietan", "jogurt", "twaróg", "twarog",
    "serek", "ser ", "fetę", "feta", "maślank", "maslank", "kefir",
    "mascarpone", "mozzarell", "parmezan",
)

/**
 * Pure port of RecipeViewModel.recompute() from the app module — filters by
 * category (with the sniadania+drugie merge), by a search term matched
 * against the recipe name or any ingredient, and (when the profile's
 * corresponding toggle is on) by the same gluten/lactose heuristics as
 * index.html's isGlutenFree/isLactoseFree (FR-8).
 */
object RecipeBrowsing {
    fun isGlutenFree(recipe: Recipe): Boolean {
        val text = (recipe.ingredients.joinToString(" ") + " " + recipe.name).lowercase()
        return GLUTEN_KEYWORDS.none { text.contains(it) }
    }

    fun isLactoseFree(recipe: Recipe): Boolean = recipe.ingredients.all { ingredient ->
        val text = ingredient.lowercase()
        val isDairy = DAIRY_KEYWORDS.any { text.contains(it) }
        !isDairy || text.contains("bez laktozy")
    }

    fun visibleRecipes(
        all: List<Recipe>,
        category: String,
        searchTerm: String,
        glutenFree: Boolean = false,
        lactoseFree: Boolean = false,
    ): List<Recipe> {
        val matchesCategory: (Recipe) -> Boolean = { recipe ->
            if (category == "sniadania") recipe.cat == "sniadania" || recipe.cat == "drugie"
            else recipe.cat == category
        }
        val term = searchTerm.trim().lowercase()
        return all.filter { recipe ->
            matchesCategory(recipe) &&
                (term.isEmpty() ||
                    recipe.name.lowercase().contains(term) ||
                    recipe.ingredients.any { it.lowercase().contains(term) }) &&
                (!glutenFree || isGlutenFree(recipe)) &&
                (!lactoseFree || isLactoseFree(recipe))
        }
    }
}
