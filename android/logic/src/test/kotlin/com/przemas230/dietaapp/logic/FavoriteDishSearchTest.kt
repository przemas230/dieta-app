package com.przemas230.dietaapp.logic

import kotlin.random.Random
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FavoriteDishSearchTest {

    private val categories = mapOf(
        "kasza gryczana" to "Pieczywo i zboża",
        "mąka owsiana" to "Pieczywo i zboża",
        "ryż" to "Pieczywo i zboża",
        "kurczak" to "Mięso, ryby, jajka",
        "sól" to "Przyprawy",
        "jogurt" to "Nabiał",
        "pomidor" to "Warzywa",
        "cebula" to "Warzywa",
        "banan" to "Owoce",
        "jabłko" to "Owoce",
    )
    private val categoryOf: (String) -> String = { categories[it] ?: "Inne" }

    @Test
    fun `empty favorites returns empty list`() {
        assertTrue(FavoriteDishSearch.pickDiverseIngredients(emptySet(), categoryOf).isEmpty())
    }

    @Test
    fun `never returns more than one pick from a non-flexible category when alternatives exist`() {
        val favorites = setOf("kasza gryczana", "mąka owsiana", "ryż", "kurczak", "sól")
        repeat(50) { seed ->
            val picked = FavoriteDishSearch.pickDiverseIngredients(favorites, categoryOf, random = Random(seed))
            val grainPicks = picked.count { categoryOf(it) == "Pieczywo i zboża" }
            assertTrue(grainPicks <= 1, "seed=$seed picked more than one grain: $picked")
        }
    }

    @Test
    fun `vegetables and fruits may both appear multiple times`() {
        val favorites = setOf("pomidor", "cebula", "banan", "jabłko", "kurczak")
        val picked = FavoriteDishSearch.pickDiverseIngredients(favorites, categoryOf, random = Random(7))
        val flexiblePicks = picked.count { categoryOf(it) in setOf("Warzywa", "Owoce") }
        assertTrue(flexiblePicks >= 2, "expected multiple veg/fruit picks, got: $picked")
    }

    @Test
    fun `caps at one pick when all favorites share a single non-flexible category`() {
        val favorites = setOf("kasza gryczana", "mąka owsiana")
        val picked = FavoriteDishSearch.pickDiverseIngredients(favorites, categoryOf, count = 5, random = Random(3))
        assertEquals(1, picked.size)
        assertTrue(picked[0] in favorites)
    }

    @Test
    fun `never duplicates an ingredient`() {
        val favorites = setOf("kasza gryczana", "mąka owsiana", "ryż", "kurczak", "sól", "jogurt", "pomidor", "cebula", "banan", "jabłko")
        repeat(20) { seed ->
            val picked = FavoriteDishSearch.pickDiverseIngredients(favorites, categoryOf, random = Random(seed))
            assertEquals(picked.size, picked.toSet().size)
        }
    }

    @Test
    fun `caps result at requested count`() {
        val favorites = setOf("kasza gryczana", "mąka owsiana", "ryż", "kurczak", "sól", "jogurt", "pomidor", "cebula", "banan", "jabłko")
        val picked = FavoriteDishSearch.pickDiverseIngredients(favorites, categoryOf, count = 5, random = Random(1))
        assertEquals(5, picked.size)
    }

    @Test
    fun `builds an accusative-correct search query`() {
        val query = FavoriteDishSearch.buildSearchQuery(MealTimeChoice.KOLACJA, listOf("pomidor", "kurczak"))
        assertEquals("przepis na kolację z pomidor, kurczak", query)
    }
}
