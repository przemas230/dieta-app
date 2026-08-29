package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.PlannedMeal
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.SpiceLevel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * FR-108. The three exclusions (untracked products, meals already behind us
 * or already cooked, unit categories that can't be compared) are the whole
 * reason the warning stays believable, so each one is pinned down here —
 * a false "kończy ci się ryż" is worse than no warning at all.
 */
class PantryShortageTest {
    private fun recipe(id: String, name: String, vararg ingredients: String) = Recipe(
        id = id,
        cat = "obiady",
        name = name,
        kcal = 500,
        time = "20 min",
        ingredients = ingredients.toList(),
        method = "Ugotuj",
        protein = null,
        carbs = null,
        fat = null,
        fiber = null,
        gi = null,
        gl = null,
    )

    /** Same pipeline the app keys the pantry by, so a canon-name change can't quietly desync the test from the code. */
    private fun canonOf(ingredient: String) = RecipePantryMatching.parseIngredient(ingredient).canonName

    private fun product(ingredient: String, quantity: Double, unit: String) =
        canonOf(ingredient) to PantryItem.Product(canonOf(ingredient), PantryCategory.INNE, quantity, unit)

    private fun plan(vararg slots: Triple<Int, String, PlannedMeal>): WeekPlan {
        val out = mutableMapOf<Int, MutableMap<String, PlannedMeal>>()
        slots.forEach { (day, cat, meal) -> out.getOrPut(day) { mutableMapOf() }[cat] = meal }
        return out
    }

    @Test
    fun `enough in stock reports nothing`() {
        val r = recipe("r1", "Ryż z kurczakiem", "200 g ryżu")
        val result = PantryShortage.compute(
            plan(Triple(2, "obiady", PlannedMeal("r1"))),
            mapOf("r1" to r),
            mapOf(product("200 g ryżu", 500.0, "g")),
            todayDayIndex = 0,
        )
        assertTrue(result.isEmpty())
    }

    @Test
    fun `exactly enough is not a shortage`() {
        val r = recipe("r1", "Ryż z kurczakiem", "200 g ryżu")
        val result = PantryShortage.compute(
            plan(Triple(2, "obiady", PlannedMeal("r1"))),
            mapOf("r1" to r),
            mapOf(product("200 g ryżu", 200.0, "g")),
            todayDayIndex = 0,
        )
        assertTrue(result.isEmpty())
    }

    @Test
    fun `too little reports how much is missing and for which dish`() {
        val r = recipe("r1", "Ryż z kurczakiem", "200 g ryżu")
        val result = PantryShortage.compute(
            plan(Triple(2, "obiady", PlannedMeal("r1"))),
            mapOf("r1" to r),
            mapOf(product("200 g ryżu", 50.0, "g")),
            todayDayIndex = 0,
        )
        assertEquals(1, result.size)
        val shortage = result.first()
        assertEquals(canonOf("200 g ryżu"), shortage.canonName)
        assertEquals(50.0, shortage.haveBase)
        assertEquals(200.0, shortage.neededBase)
        assertEquals(150.0, shortage.missingBase)
        assertEquals(listOf("Ryż z kurczakiem"), shortage.dishes)
    }

    @Test
    fun `kilograms in the pantry are compared in grams`() {
        val r = recipe("r1", "Ryż z kurczakiem", "200 g ryżu")
        val plenty = PantryShortage.compute(
            plan(Triple(2, "obiady", PlannedMeal("r1"))),
            mapOf("r1" to r),
            mapOf(product("200 g ryżu", 1.0, "kg")),
            todayDayIndex = 0,
        )
        assertTrue(plenty.isEmpty(), "1 kg covers 200 g")
    }

    @Test
    fun `untracked ingredients are never reported`() {
        val r = recipe("r1", "Ryż z kurczakiem", "200 g ryżu")
        val result = PantryShortage.compute(
            plan(Triple(2, "obiady", PlannedMeal("r1"))),
            mapOf("r1" to r),
            emptyMap(),
            todayDayIndex = 0,
        )
        assertTrue(result.isEmpty(), "no pantry entry means the user doesn't track it, not that it ran out")
    }

    @Test
    fun `a spice level is not a quantity`() {
        val r = recipe("r1", "Zupa", "10 g kurkumy")
        val canon = canonOf("10 g kurkumy")
        val result = PantryShortage.compute(
            plan(Triple(2, "obiady", PlannedMeal("r1"))),
            mapOf("r1" to r),
            mapOf(canon to PantryItem.Spice(canon, PantryCategory.PRZYPRAWY, SpiceLevel.MALO)),
            todayDayIndex = 0,
        )
        assertTrue(result.isEmpty())
    }

    @Test
    fun `a pantry entry in pieces is not compared against grams`() {
        val r = recipe("r1", "Omlet", "100 g jajek")
        val canon = canonOf("100 g jajek")
        val result = PantryShortage.compute(
            plan(Triple(2, "obiady", PlannedMeal("r1"))),
            mapOf("r1" to r),
            mapOf(canon to PantryItem.Product(canon, PantryCategory.INNE, 2.0, "szt.")),
            todayDayIndex = 0,
        )
        assertTrue(result.isEmpty(), "szt. vs g cannot be reconciled, so it is skipped rather than guessed")
    }

    @Test
    fun `days already behind us are not counted`() {
        val r = recipe("r1", "Ryż z kurczakiem", "200 g ryżu")
        val result = PantryShortage.compute(
            plan(Triple(1, "obiady", PlannedMeal("r1"))),
            mapOf("r1" to r),
            mapOf(product("200 g ryżu", 50.0, "g")),
            todayDayIndex = 3,
        )
        assertTrue(result.isEmpty())
    }

    @Test
    fun `a meal already cooked has already left the pantry`() {
        val r = recipe("r1", "Ryż z kurczakiem", "200 g ryżu")
        val result = PantryShortage.compute(
            plan(Triple(3, "obiady", PlannedMeal("r1"))),
            mapOf("r1" to r),
            mapOf(product("200 g ryżu", 50.0, "g")),
            todayDayIndex = 3,
            isCookedOn = { recipeId, day -> recipeId == "r1" && day == 3 },
        )
        assertTrue(result.isEmpty(), "FR-15 already subtracted it; counting it again invents a shortage")
    }

    @Test
    fun `a scaled portion needs proportionally more`() {
        val r = recipe("r1", "Ryż z kurczakiem", "200 g ryżu")
        val result = PantryShortage.compute(
            plan(Triple(2, "obiady", PlannedMeal("r1", scale = 2.0))),
            mapOf("r1" to r),
            mapOf(product("200 g ryżu", 300.0, "g")),
            todayDayIndex = 0,
        )
        assertEquals(1, result.size)
        assertEquals(400.0, result.first().neededBase)
        assertEquals(100.0, result.first().missingBase)
    }

    @Test
    fun `the same ingredient across two dishes adds up and names both`() {
        val r1 = recipe("r1", "Ryż z kurczakiem", "200 g ryżu")
        val r2 = recipe("r2", "Ryż na mleku", "150 g ryżu")
        val result = PantryShortage.compute(
            plan(
                Triple(2, "obiady", PlannedMeal("r1")),
                Triple(4, "kolacje", PlannedMeal("r2")),
            ),
            mapOf("r1" to r1, "r2" to r2),
            mapOf(product("200 g ryżu", 100.0, "g")),
            todayDayIndex = 0,
        )
        assertEquals(1, result.size)
        assertEquals(350.0, result.first().neededBase)
        assertEquals(listOf("Ryż z kurczakiem", "Ryż na mleku"), result.first().dishes)
    }

    @Test
    fun `the same dish planned twice is named once but needed twice`() {
        val r = recipe("r1", "Ryż z kurczakiem", "200 g ryżu")
        val result = PantryShortage.compute(
            plan(
                Triple(2, "obiady", PlannedMeal("r1")),
                Triple(5, "obiady", PlannedMeal("r1")),
            ),
            mapOf("r1" to r),
            mapOf(product("200 g ryżu", 100.0, "g")),
            todayDayIndex = 0,
        )
        assertEquals(400.0, result.first().neededBase)
        assertEquals(1, result.first().dishes.size)
    }

    @Test
    fun `the worst shortfall comes first`() {
        val r = recipe("r1", "Obiad", "200 g ryżu", "500 g kurczaka")
        val result = PantryShortage.compute(
            plan(Triple(2, "obiady", PlannedMeal("r1"))),
            mapOf("r1" to r),
            mapOf(
                product("200 g ryżu", 180.0, "g"),
                product("500 g kurczaka", 50.0, "g"),
            ),
            todayDayIndex = 0,
        )
        assertEquals(2, result.size)
        assertEquals(canonOf("500 g kurczaka"), result.first().canonName)
    }

    @Test
    fun `dish counts are inflected in Polish`() {
        assertEquals("na 1 danie", PantryShortage.dishCountLabel(1))
        assertEquals("na 2 dania", PantryShortage.dishCountLabel(2))
        assertEquals("na 5 dań", PantryShortage.dishCountLabel(5))
        assertEquals("na 12 dań", PantryShortage.dishCountLabel(12))
        assertEquals("na 22 dania", PantryShortage.dishCountLabel(22))
    }
}
