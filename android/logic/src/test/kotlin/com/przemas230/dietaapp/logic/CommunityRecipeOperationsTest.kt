package com.przemas230.dietaapp.logic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CommunityRecipeOperationsTest {

    @Test
    fun `sanitizeCommunityRecipeDoc keeps a fully valid document`() {
        val data = mapOf(
            "cat" to "obiady",
            "name" to "Kurczak curry",
            "time" to "30 min",
            "kcal" to 450,
            "ingredients" to listOf("kurczak", "curry", "ryż"),
            "method" to "Usmaż i dopraw.",
            "authorUid" to "uid123",
            "authorDisplayName" to "Kasia",
            "protein" to 30.0,
            "carbs" to 40.0,
            "fat" to 15.0,
            "fiber" to 5.0,
            "gi" to 55.0,
            "gl" to 22.0,
        )
        val recipe = CommunityRecipeOperations.sanitizeCommunityRecipeDoc(data, "doc1")
        assertEquals("doc1", recipe.id)
        assertEquals("obiady", recipe.cat)
        assertEquals("Kurczak curry", recipe.name)
        assertEquals(450, recipe.kcal)
        assertEquals(listOf("kurczak", "curry", "ryż"), recipe.ingredients)
        assertEquals("community", recipe.source)
        assertEquals("uid123", recipe.authorUid)
        assertEquals("Kasia", recipe.authorDisplayName)
        assertEquals(30.0, recipe.protein)
    }

    @Test
    fun `sanitizeCommunityRecipeDoc falls back to obiady for an unknown category`() {
        val data = mapOf("cat" to "not-a-real-category", "name" to "X")
        val recipe = CommunityRecipeOperations.sanitizeCommunityRecipeDoc(data, "doc2")
        assertEquals("obiady", recipe.cat)
    }

    @Test
    fun `sanitizeCommunityRecipeDoc defaults missing name, kcal and method instead of crashing`() {
        val recipe = CommunityRecipeOperations.sanitizeCommunityRecipeDoc(emptyMap(), "doc3")
        assertEquals("Przepis bez nazwy", recipe.name)
        assertEquals(0, recipe.kcal)
        assertEquals("—", recipe.method)
        assertEquals("Anonimowy użytkownik", recipe.authorDisplayName)
        assertTrue(recipe.ingredients.isEmpty())
    }

    @Test
    fun `sanitizeCommunityRecipeDoc reports no macros when only some are present`() {
        val data = mapOf("cat" to "deser", "protein" to 10.0, "carbs" to "not-a-number")
        val recipe = CommunityRecipeOperations.sanitizeCommunityRecipeDoc(data, "doc4")
        assertNull(recipe.protein)
        assertNull(recipe.carbs)
        assertNull(recipe.fat)
        assertNull(recipe.fiber)
    }

    @Test
    fun `sanitizeRatingDoc clamps stars into 1 to 5`() {
        assertEquals(1, CommunityRecipeOperations.sanitizeRatingDoc(mapOf("stars" to 0), "u1", null).stars)
        assertEquals(1, CommunityRecipeOperations.sanitizeRatingDoc(emptyMap(), "u1", null).stars)
        assertEquals(5, CommunityRecipeOperations.sanitizeRatingDoc(mapOf("stars" to 9), "u1", null).stars)
        assertEquals(3, CommunityRecipeOperations.sanitizeRatingDoc(mapOf("stars" to 3), "u1", null).stars)
    }

    @Test
    fun `sanitizeRatingDoc treats blank comment as null`() {
        val comment = CommunityRecipeOperations.sanitizeRatingDoc(mapOf("comment" to "   "), "u1", null).comment
        assertNull(comment)
    }

    @Test
    fun `sanitizeRatingDoc defaults missing displayName`() {
        val comment = CommunityRecipeOperations.sanitizeRatingDoc(emptyMap(), "u1", 123L)
        assertEquals("Anonimowy użytkownik", comment.displayName)
        assertEquals(123L, comment.createdAtMillis)
    }

    @Test
    fun `dedupeCommunityRecipes removes ids already present locally`() {
        val mine = listOf(recipe("shared-id"), recipe("only-mine"))
        val community = listOf(recipe("shared-id", source = "community"), recipe("only-community", source = "community"))
        val result = CommunityRecipeOperations.dedupeCommunityRecipes(mine, community)
        assertEquals(listOf("only-community"), result.map { it.id })
    }

    private fun recipe(id: String, source: String = "custom") = com.przemas230.dietaapp.data.Recipe(
        id = id,
        cat = "obiady",
        name = "Test",
        time = "10 min",
        kcal = 100,
        ingredients = listOf("x"),
        method = "y",
        protein = null,
        carbs = null,
        fat = null,
        fiber = null,
        gi = null,
        gl = null,
        source = source,
    )
}
