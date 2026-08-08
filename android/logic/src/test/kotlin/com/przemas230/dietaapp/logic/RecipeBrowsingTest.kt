package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.Recipe
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

private fun recipe(id: String, cat: String, name: String, ingredients: List<String> = emptyList()) = Recipe(
    id = id,
    cat = cat,
    name = name,
    time = "10 min",
    kcal = 300,
    ingredients = ingredients,
    method = "Wymieszaj.",
    protein = null,
    carbs = null,
    fat = null,
    fiber = null,
    gi = null,
    gl = null,
)

class RecipeBrowsingTest {
    private val recipes = listOf(
        recipe("1", "sniadania", "Owsianka z jagodami", listOf("płatki owsiane", "jagody", "mleko")),
        recipe("2", "drugie", "Kanapka z jajkiem", listOf("chleb", "jajko")),
        recipe("3", "obiady", "Kurczak z ryżem", listOf("kurczak", "ryż", "brokuł")),
        recipe("4", "kolacje", "Sałatka grecka", listOf("ogórek", "pomidor", "feta")),
        recipe("5", "deser", "Jogurt z owocami", listOf("jogurt naturalny", "jabłko")),
    )

    @Test
    fun `sniadania category includes both sniadania and drugie`() {
        val visible = RecipeBrowsing.visibleRecipes(recipes, "sniadania", "")
        assertEquals(setOf("1", "2"), visible.map { it.id }.toSet())
    }

    @Test
    fun `other categories match cat exactly`() {
        assertEquals(listOf("3"), RecipeBrowsing.visibleRecipes(recipes, "obiady", "").map { it.id })
        assertEquals(listOf("4"), RecipeBrowsing.visibleRecipes(recipes, "kolacje", "").map { it.id })
        assertEquals(listOf("5"), RecipeBrowsing.visibleRecipes(recipes, "deser", "").map { it.id })
    }

    @Test
    fun `search matches recipe name case-insensitively`() {
        val visible = RecipeBrowsing.visibleRecipes(recipes, "obiady", "KURCZAK")
        assertEquals(listOf("3"), visible.map { it.id })
    }

    @Test
    fun `search matches an ingredient, not just the name`() {
        val visible = RecipeBrowsing.visibleRecipes(recipes, "sniadania", "jajko")
        assertEquals(listOf("2"), visible.map { it.id })
    }

    @Test
    fun `search term is trimmed before matching`() {
        val visible = RecipeBrowsing.visibleRecipes(recipes, "obiady", "  kurczak  ")
        assertEquals(listOf("3"), visible.map { it.id })
    }

    @Test
    fun `blank search term with matching category returns everything in it`() {
        val visible = RecipeBrowsing.visibleRecipes(recipes, "sniadania", "   ")
        assertEquals(2, visible.size)
    }

    @Test
    fun `search term that matches nothing returns empty list`() {
        val visible = RecipeBrowsing.visibleRecipes(recipes, "obiady", "makaron")
        assertTrue(visible.isEmpty())
    }

    @Test
    fun `category that matches nothing returns empty list`() {
        val visible = RecipeBrowsing.visibleRecipes(recipes, "nieznana-kategoria", "")
        assertTrue(visible.isEmpty())
    }
}
