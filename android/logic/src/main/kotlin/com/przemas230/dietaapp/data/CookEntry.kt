package com.przemas230.dietaapp.data

/**
 * One "✅ Zrobione" entry for a recipe (FR-15) — mirrors an item of
 * index.html's `state.cooked[recipeId]` ({date, rating}). `rating` is
 * null until the user taps a star (FR-17).
 */
data class CookEntry(
    val dateEpochMillis: Long,
    val rating: Int? = null,
)
