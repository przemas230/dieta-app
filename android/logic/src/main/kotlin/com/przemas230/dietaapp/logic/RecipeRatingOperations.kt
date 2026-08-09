package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.Recipe

enum class RecipeRating { LIKE, DISLIKE }

/**
 * FR-55/57: pure port of index.html's like/dislike rating map
 * (state.recipeRatings[id] = "like"|"dislike") and the "❤️" ranking sort
 * (liked first, then unrated/"new", then disliked). Rated cards are never
 * removed from the list -- this is a persistent tag + sort key, not a
 * Tinder-style single-card stack.
 */
object RecipeRatingOperations {
    fun setRating(ratings: Map<String, RecipeRating>, recipeId: String, rating: RecipeRating): Map<String, RecipeRating> =
        ratings + (recipeId to rating)

    fun clearRating(ratings: Map<String, RecipeRating>, recipeId: String): Map<String, RecipeRating> =
        ratings - recipeId

    /** Liked first, then not-yet-rated, then disliked -- stable within each group. Same final order as index.html's rank()-descending sort, just ascending over inverted rank numbers here. */
    fun sortByRating(recipes: List<Recipe>, ratings: Map<String, RecipeRating>): List<Recipe> {
        fun rank(id: String) = when (ratings[id]) {
            RecipeRating.LIKE -> 0
            null -> 1
            RecipeRating.DISLIKE -> 2
        }
        return recipes.sortedBy { rank(it.id) }
    }
}
