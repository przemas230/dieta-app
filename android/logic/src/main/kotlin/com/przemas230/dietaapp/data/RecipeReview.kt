package com.przemas230.dietaapp.data

/**
 * FR-67: a 1-5 star rating + optional comment for a recipe -- mirrors
 * index.html's `state.recipeReviews[recipeId] = {stars, comment, at}`.
 * Distinct from RecipeRating (FR-55/57's swipe like/dislike, binary, no
 * comment) and from CookEntry.rating (FR-17's per-cook-session star,
 * one per "✅ Zrobione" entry) -- this is ONE deliberate review per recipe,
 * editable, meant as the seed of a future multi-user average (FR-77).
 */
data class RecipeReview(val stars: Int, val comment: String?, val atEpochMillis: Long)
