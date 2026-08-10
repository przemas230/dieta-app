package com.przemas230.dietaapp.data

/**
 * One ingredient's contribution to a recipe's kcal/macros — mirrors an
 * entry in index.html's `RECIPES[].calc` array (FR-12's "Skąd te liczby?"
 * breakdown). `qty` is how many of that ingredient's base unit went in;
 * `base*` is the per-unit nutrition, `kcal`/`p`/`c`/`f` is qty * base
 * already multiplied out (as authored in recipes.json, not recomputed here).
 */
data class RecipeCalcItem(
    val label: String,
    val unit: String,
    val qty: Double,
    val baseKcal: Int,
    val baseP: Double,
    val baseC: Double,
    val baseF: Double,
    val kcal: Int,
    val p: Double,
    val c: Double,
    val f: Double,
)

/**
 * Mirrors the recipe shape from the web app's `RECIPES` array (index.html) —
 * same field names, so anyone comparing the two sources doesn't have to
 * remember a translation.
 *
 * Lives in the plain :logic module (no Android dependency) so recipe
 * filtering logic can be unit-tested — see RecipeBrowsingTest.
 */
data class Recipe(
    val id: String,
    val cat: String,
    val name: String,
    val time: String,
    val kcal: Int,
    val ingredients: List<String>,
    val method: String,
    val protein: Double?,
    val carbs: Double?,
    val fat: Double?,
    val fiber: Double?,
    val gi: Double?,
    val gl: Double?,
    val calc: List<RecipeCalcItem> = emptyList(),
    // FR-66: "builtin" (loaded from recipes.json) or "custom" (added by the
    // user on this device, index.html's state.myRecipes) -- drives the
    // "✍️ Twój przepis" badge and the delete button on the recipe card.
    val source: String = "builtin",
)
