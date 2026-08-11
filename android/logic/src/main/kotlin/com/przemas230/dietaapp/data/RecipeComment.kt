package com.przemas230.dietaapp.data

/**
 * FR-77: one entry from Firestore's `recipes/{id}/ratings` subcollection --
 * another user's published rating/comment on a recipe, as opposed to
 * RecipeReview which is this device's own, purely-local review. Mirrors
 * index.html's `sanitizeRatingDoc()` output shape.
 */
data class RecipeComment(
    val uid: String,
    val displayName: String,
    val stars: Int,
    val comment: String?,
    val createdAtMillis: Long?,
)
