package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.Goal
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.Recipe
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

private fun recipe(protein: Double?, carbs: Double?, fat: Double?, gl: Double?) = Recipe(
    id = "x",
    cat = "obiady",
    name = "Test",
    time = "10 min",
    kcal = 400,
    ingredients = emptyList(),
    method = "Wymieszaj.",
    protein = protein,
    carbs = carbs,
    fat = fat,
    fiber = null,
    gi = null,
    gl = gl,
)

private fun profile(goal: Goal, strictLowGI: Boolean) = Profile(goal = goal, strictLowGI = strictLowGI, configured = true)

class RecipeMatchingTest {
    private val target = MacroGrams(protein = 30, carbs = 40, fat = 15)

    @Test
    fun `perfect macro match with low GL scores 100`() {
        val score = RecipeMatching.matchScore(
            recipe(protein = 30.0, carbs = 40.0, fat = 15.0, gl = 5.0),
            target,
            profile(Goal.REDUKCJA, strictLowGI = true),
        )
        assertEquals(100, score)
    }

    @Test
    fun `macro deviation lowers score, high GL applies redukcja penalty`() {
        val score = RecipeMatching.matchScore(
            recipe(protein = 20.0, carbs = 40.0, fat = 15.0, gl = 25.0),
            target,
            profile(Goal.REDUKCJA, strictLowGI = true),
        )
        // base: dp=10/30=0.3333, dc=df=0, score=100-11.11=88.89; GL penalty (25-20)*1.5=7.5
        assertEquals(81, score)
    }

    @Test
    fun `budowanie masy relaxes the GL threshold and weight`() {
        val score = RecipeMatching.matchScore(
            recipe(protein = 20.0, carbs = 40.0, fat = 15.0, gl = 25.0),
            target,
            profile(Goal.BUDOWANIE, strictLowGI = true),
        )
        // same base 88.89, but gl=25 is under the gain threshold (35) -> no penalty
        assertEquals(89, score)
    }

    @Test
    fun `strictLowGI off skips the GL penalty regardless of gl value`() {
        val score = RecipeMatching.matchScore(
            recipe(protein = 20.0, carbs = 40.0, fat = 15.0, gl = 999.0),
            target,
            profile(Goal.REDUKCJA, strictLowGI = false),
        )
        assertEquals(89, score)
    }

    @Test
    fun `missing protein data returns null instead of a score`() {
        val score = RecipeMatching.matchScore(
            recipe(protein = null, carbs = 40.0, fat = 15.0, gl = 5.0),
            target,
            profile(Goal.REDUKCJA, strictLowGI = true),
        )
        assertNull(score)
    }

    @Test
    fun `missing target returns null`() {
        val score = RecipeMatching.matchScore(
            recipe(protein = 30.0, carbs = 40.0, fat = 15.0, gl = 5.0),
            null,
            profile(Goal.REDUKCJA, strictLowGI = true),
        )
        assertNull(score)
    }

    @Test
    fun `score is clamped to 0, never negative`() {
        val score = RecipeMatching.matchScore(
            recipe(protein = 1000.0, carbs = 1000.0, fat = 1000.0, gl = 0.0),
            target,
            profile(Goal.REDUKCJA, strictLowGI = true),
        )
        assertEquals(0, score)
    }

    @Test
    fun `forCategory picks the matching MacroTargets field`() {
        val targets = MacroTargets(
            daily = MacroGrams(1, 1, 1),
            sniadania = MacroGrams(2, 2, 2),
            drugie = MacroGrams(3, 3, 3),
            obiady = MacroGrams(4, 4, 4),
            kolacje = MacroGrams(5, 5, 5),
            deser = MacroGrams(6, 6, 6),
        )
        assertEquals(targets.sniadania, targets.forCategory("sniadania"))
        assertEquals(targets.obiady, targets.forCategory("obiady"))
        assertNull(targets.forCategory("nieznana"))
    }
}
