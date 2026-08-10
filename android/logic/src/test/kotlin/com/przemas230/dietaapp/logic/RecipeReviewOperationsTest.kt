package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.RecipeReview
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RecipeReviewOperationsTest {

    private fun recipe(id: String) = Recipe(id, "obiady", "Danie $id", "10 min", 300, listOf("a"), "Zrób.", null, null, null, null, null, null)

    @Test
    fun `setReview rejects out-of-range stars`() {
        assertNull(RecipeReviewOperations.setReview(emptyMap(), "r1", 0, null, 1L))
        assertNull(RecipeReviewOperations.setReview(emptyMap(), "r1", 6, null, 1L))
    }

    @Test
    fun `setReview stores stars and trims blank comment to null`() {
        val result = RecipeReviewOperations.setReview(emptyMap(), "r1", 4, "  ", 1000L)
        assertEquals(RecipeReview(4, null, 1000L), result?.get("r1"))
    }

    @Test
    fun `setReview keeps a real comment trimmed`() {
        val result = RecipeReviewOperations.setReview(emptyMap(), "r1", 5, "  Świetne!  ", 1000L)
        assertEquals("Świetne!", result?.get("r1")?.comment)
    }

    @Test
    fun `clearReview removes only the targeted recipe`() {
        val reviews = mapOf("r1" to RecipeReview(3, null, 1L), "r2" to RecipeReview(5, null, 1L))
        val result = RecipeReviewOperations.clearReview(reviews, "r1")
        assertEquals(setOf("r2"), result.keys)
    }

    @Test
    fun `sortByReview sorts descending with unrated recipes last`() {
        val recipes = listOf(recipe("a"), recipe("b"), recipe("c"))
        val reviews = mapOf("a" to RecipeReview(2, null, 1L), "c" to RecipeReview(5, null, 1L))
        val sorted = RecipeReviewOperations.sortByReview(recipes, reviews)
        assertEquals(listOf("c", "a", "b"), sorted.map { it.id })
    }

    @Test
    fun `sortByReview is stable for equal ratings`() {
        val recipes = listOf(recipe("a"), recipe("b"))
        assertTrue(RecipeReviewOperations.sortByReview(recipes, emptyMap()).map { it.id } == listOf("a", "b"))
    }
}
