package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.RecipeCalcItem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

private fun calcItem(label: String, kcal: Int) = RecipeCalcItem(
    label = label,
    unit = "1 szt.",
    qty = 1.0,
    baseKcal = kcal,
    baseP = 0.0,
    baseC = 0.0,
    baseF = 0.0,
    kcal = kcal,
    p = 0.0,
    c = 0.0,
    f = 0.0,
)

private fun recipeWithCalc(calc: List<RecipeCalcItem>) = Recipe(
    id = "x",
    cat = "obiady",
    name = "Test",
    time = "10 min",
    kcal = calc.sumOf { it.kcal },
    ingredients = emptyList(),
    method = "",
    protein = null,
    carbs = null,
    fat = null,
    fiber = null,
    gi = null,
    gl = null,
    calc = calc,
)

class IngredientCanonTest {
    @Test
    fun `coreName strips leading quantities and known unit words`() {
        assertEquals("jajka", IngredientCanon.coreName("2 jajka"))
        assertEquals("szpinak", IngredientCanon.coreName("garść szpinaku"))
        // "ząbek" is a unit word (gets stripped), but the lemma fix for "czosnku"
        // itself lives in RAW_TO_CANON (applied by pantryCanon), not LEMMA_FIX.
        assertEquals("czosnku", IngredientCanon.coreName("1 ząbek czosnku"))
    }

    @Test
    fun `pantryCanon maps known raw names and passes through unknown ones`() {
        assertEquals("jajka", IngredientCanon.pantryCanon("jajko"))
        assertEquals("czosnek", IngredientCanon.pantryCanon(IngredientCanon.coreName("1 ząbek czosnku")))
        assertEquals("nieznany-skladnik-xyz", IngredientCanon.pantryCanon("nieznany-skladnik-xyz"))
    }

    @Test
    fun `thumbCanon resolves the Szakszuka ingredient labels to their pantry canon`() {
        assertEquals("jajka", IngredientCanon.thumbCanon("Jajko"))
        assertEquals("pomidor", IngredientCanon.thumbCanon("Pomidory krojone z puszki"))
        assertEquals("chleb żytni", IngredientCanon.thumbCanon("Chleb żytni na zakwasie"))
    }

    @Test
    fun `CANON_INFO has emoji for common canonical names`() {
        assertEquals("🥚", IngredientCanon.CANON_INFO["jajka"]?.emoji)
        assertEquals("🍅", IngredientCanon.CANON_INFO["pomidor"]?.emoji)
        assertEquals("🍞", IngredientCanon.CANON_INFO["chleb żytni"]?.emoji)
    }

    @Test
    fun `mainIngredientInfo picks the highest-kcal non-seasoning ingredient`() {
        // Mirrors recipe S1 "Szakszuka ze szpinakiem i pomidorami" from recipes.json:
        // Jajko (156 kcal) should win over Chleb żytni na zakwasie (80 kcal) and the rest.
        val recipe = recipeWithCalc(
            listOf(
                calcItem("Jajko", 156),
                calcItem("Pomidory krojone z puszki", 40),
                calcItem("Szpinak", 7),
                calcItem("Oliwa z oliwek", 40),
                calcItem("Chleb żytni na zakwasie", 80),
                calcItem("Czosnek", 4),
            ),
        )
        assertEquals(CanonInfo("Mięso, ryby, jajka", "🥚"), IngredientCanon.mainIngredientInfo(recipe))
    }

    @Test
    fun `mainIngredientInfo skips Przyprawy category unless nothing else qualifies`() {
        val onlySeasoning = recipeWithCalc(listOf(calcItem("Sól", 0), calcItem("Pieprz", 0)))
        assertEquals(CanonInfo("Przyprawy", "🧂"), IngredientCanon.mainIngredientInfo(onlySeasoning))

        val seasoningPlusFood = recipeWithCalc(
            listOf(calcItem("Sól", 500), calcItem("Jajko", 10)),
        )
        // Sól has more kcal but is a seasoning, so the much smaller Jajko still wins.
        assertEquals(CanonInfo("Mięso, ryby, jajka", "🥚"), IngredientCanon.mainIngredientInfo(seasoningPlusFood))
    }

    @Test
    fun `mainIngredientInfo returns null for a recipe with no calc breakdown`() {
        assertNull(IngredientCanon.mainIngredientInfo(recipeWithCalc(emptyList())))
    }
}
