package com.przemas230.dietaapp.logic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class IngredientMacroEstimationTest {
    @Test
    fun `estimateIngredientMacros is null for blank text`() {
        assertNull(IngredientMacroEstimation.estimateIngredientMacros(""))
        assertNull(IngredientMacroEstimation.estimateIngredientMacros("   "))
    }

    @Test
    fun `estimateIngredientMacros is null for an unrecognized ingredient`() {
        assertNull(IngredientMacroEstimation.estimateIngredientMacros("coś zupełnie nieznanego"))
    }

    @Test
    fun `a 1szt entry scales linearly with parsed quantity`() {
        val one = IngredientMacroEstimation.estimateIngredientMacros("jajka")
        assertEquals(78.0, one?.kcal)
        val three = IngredientMacroEstimation.estimateIngredientMacros("3 jajka")
        assertEquals(234.0, three?.kcal)
        assertEquals(18.9, three?.protein)
    }

    @Test
    fun `a 100g entry with an explicit weight uses that weight directly`() {
        // "kurczak (pierś)" is the canon key, but coreName strips the
        // parenthetical -- same quirk already documented for this DB's
        // sibling table, see SnackNutritionDbTest's `naturalAliasFor` map.
        val est = IngredientMacroEstimation.estimateIngredientMacros("150g kurczak")
        assertEquals(247.5, est?.kcal) // 165 kcal/100g * 150g / 100
        assertEquals(46.5, est?.protein) // 31 * 1.5
    }

    @Test
    fun `a 100g entry with no explicit weight falls back to typicalG`() {
        val est = IngredientMacroEstimation.estimateIngredientMacros("kurczak")
        assertEquals(247.5, est?.kcal) // 165 kcal/100g * 150g typical / 100
    }

    @Test
    fun `estimateRecipeMacrosFromText sums recognized lines and reports match count`() {
        val result = IngredientMacroEstimation.estimateRecipeMacrosFromText(
            "2 jajka\n150g kurczak\ncoś nieznanego\n",
        )
        assertEquals(3, result.total)
        assertEquals(2, result.matched)
        // 78*2 (jajka) + 165*1.5 (kurczak) = 156 + 247.5 = 403.5, rounded once
        // AFTER summing (not per-line) -- Math.round rounds .5 up to 404.
        assertEquals(404, result.kcal)
    }

    @Test
    fun `estimateRecipeMacrosFromText on empty text reports zero total`() {
        val result = IngredientMacroEstimation.estimateRecipeMacrosFromText("")
        assertEquals(0, result.total)
        assertEquals(0, result.matched)
        assertEquals(0, result.kcal)
    }

    @Test
    fun `the database has 105 entries, matching the extraction script's count`() {
        assertEquals(105, IngredientMacroDb.TABLE.size)
    }
}
