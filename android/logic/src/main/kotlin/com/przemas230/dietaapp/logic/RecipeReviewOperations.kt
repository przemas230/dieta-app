package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.RecipeReview

/**
 * FR-67: pure port of index.html's recipeReviewSaveBtn/recipeReviewDeleteBtn
 * handlers and the 🏆 sort toggle (`sortByReview`).
 */
object RecipeReviewOperations {
    /** Returns null (caller should reject, matching web's "Wybierz od 1 do 5 gwiazdek" toast) if stars is out of 1..5. */
    fun setReview(
        reviews: Map<String, RecipeReview>,
        recipeId: String,
        stars: Int,
        comment: String?,
        nowEpochMillis: Long,
    ): Map<String, RecipeReview>? {
        if (stars !in 1..5) return null
        val trimmedComment = comment?.trim()?.takeIf { it.isNotEmpty() }
        return reviews + (recipeId to RecipeReview(stars, trimmedComment, nowEpochMillis))
    }

    fun clearReview(reviews: Map<String, RecipeReview>, recipeId: String): Map<String, RecipeReview> = reviews - recipeId

    /** Descending by stars; unrated (no review) sorts as 0, i.e. last -- port of index.html's `stars(b.id) - stars(a.id)`. */
    fun sortByReview(recipes: List<Recipe>, reviews: Map<String, RecipeReview>): List<Recipe> =
        recipes.sortedByDescending { reviews[it.id]?.stars ?: 0 }
}
