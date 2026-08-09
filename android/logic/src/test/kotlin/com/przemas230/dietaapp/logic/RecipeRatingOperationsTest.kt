package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.Recipe
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

private fun recipe(id: String) = Recipe(
    id = id,
    cat = "obiady",
    name = "Danie $id",
    time = "10 min",
    kcal = 400,
    ingredients = emptyList(),
    method = "",
    protein = null,
    carbs = null,
    fat = null,
    fiber = null,
    gi = null,
    gl = null,
)

class RecipeRatingOperationsTest {
    @Test
    fun `setRating stores a rating and clearRating removes it`() {
        var ratings = RecipeRatingOperations.setRating(emptyMap(), "r1", RecipeRating.LIKE)
        assertEquals(RecipeRating.LIKE, ratings["r1"])

        ratings = RecipeRatingOperations.clearRating(ratings, "r1")
        assertNull(ratings["r1"])
    }

    @Test
    fun `setRating overwrites an existing rating for the same recipe`() {
        var ratings = RecipeRatingOperations.setRating(emptyMap(), "r1", RecipeRating.LIKE)
        ratings = RecipeRatingOperations.setRating(ratings, "r1", RecipeRating.DISLIKE)
        assertEquals(RecipeRating.DISLIKE, ratings["r1"])
    }

    @Test
    fun `sortByRating orders liked, then unrated, then disliked, preserving relative order within each group`() {
        val recipes = listOf(recipe("a"), recipe("b"), recipe("c"), recipe("d"))
        val ratings = mapOf("a" to RecipeRating.DISLIKE, "c" to RecipeRating.LIKE)

        val sorted = RecipeRatingOperations.sortByRating(recipes, ratings)

        assertEquals(listOf("c", "b", "d", "a"), sorted.map { it.id })
    }

    @Test
    fun `sortByRating with no ratings leaves the original order unchanged`() {
        val recipes = listOf(recipe("a"), recipe("b"), recipe("c"))
        assertEquals(listOf("a", "b", "c"), RecipeRatingOperations.sortByRating(recipes, emptyMap()).map { it.id })
    }
}
