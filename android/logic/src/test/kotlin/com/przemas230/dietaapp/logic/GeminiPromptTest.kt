package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.ActivityLevel
import com.przemas230.dietaapp.data.Goal
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.Sex
import kotlin.test.Test
import kotlin.test.assertTrue

private fun recipe(name: String) = Recipe(
    id = "x",
    cat = "obiady",
    name = name,
    time = "10 min",
    kcal = 400,
    ingredients = emptyList(),
    method = "Wymieszaj.",
    protein = null,
    carbs = null,
    fat = null,
    fiber = null,
    gi = null,
    gl = null,
)

class GeminiPromptTest {
    @Test
    fun `includes dish name, profile numbers and diet goal`() {
        val profile = Profile(
            sex = Sex.MEZCZYZNA,
            age = 30,
            heightCm = 180,
            weightKg = 80.0,
            targetWeightKg = 75.0,
            activity = ActivityLevel.BARDZO_AKTYWNY,
            goal = Goal.BUDOWANIE,
            glutenFree = true,
            lactoseFree = false,
            strictLowGI = false,
        )
        val prompt = GeminiPrompt.build(recipe("Kurczak z ryżem"), profile, kcalTarget = 650, macroTarget = MacroGrams(protein = 50, carbs = 70, fat = 15))

        assertTrue(prompt.contains("Kurczak z ryżem"))
        assertTrue(prompt.contains("mężczyzna"))
        assertTrue(prompt.contains("180 cm"))
        assertTrue(prompt.contains("80.0 kg"))
        assertTrue(prompt.contains("bardzo aktywny"))
        assertTrue(prompt.contains("budowanie masy"))
        assertTrue(prompt.contains("bez glutenu"))
        assertTrue(prompt.contains("650 kcal"))
        assertTrue(prompt.contains("gramaturą"))
    }

    @Test
    fun `no restrictions reads as explicit 'brak'`() {
        val profile = Profile(glutenFree = false, lactoseFree = false, strictLowGI = false)
        val prompt = GeminiPrompt.build(recipe("Zupa"), profile, kcalTarget = 300, macroTarget = MacroGrams(protein = 10, carbs = 30, fat = 5))

        assertTrue(prompt.contains("brak specjalnych ograniczeń"))
    }

    @Test
    fun `missing macro target reads as explicit fallback instead of nulls`() {
        val profile = Profile()
        val prompt = GeminiPrompt.build(recipe("Zupa"), profile, kcalTarget = null, macroTarget = null)

        assertTrue(prompt.contains("brak wyliczonego celu"))
    }
}
