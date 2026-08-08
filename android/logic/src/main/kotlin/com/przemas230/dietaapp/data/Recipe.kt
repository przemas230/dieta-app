package com.przemas230.dietaapp.data

/**
 * Mirrors the recipe shape from the web app's `RECIPES` array (index.html) —
 * same field names, so anyone comparing the two sources doesn't have to
 * remember a translation. The per-ingredient "calc" breakdown from the web
 * app isn't modeled here yet (not needed until the macro-info screen is
 * built) but stays present in assets/recipes.json for when it is.
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
)
