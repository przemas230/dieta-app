package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.ActivityLevel
import com.przemas230.dietaapp.data.Goal
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.Sex
import kotlin.test.Test
import kotlin.test.assertEquals

class ProfileCalculationsTest {
    @Test
    fun `default profile (kobieta, redukcja) matches index html DEFAULT_PROFILE numbers`() {
        val profile = Profile(
            sex = Sex.KOBIETA,
            age = 37,
            heightCm = 164,
            weightKg = 67.0,
            targetWeightKg = 62.0,
            activity = ActivityLevel.LEKKO_AKTYWNY,
            goal = Goal.REDUKCJA,
            configured = true,
        )

        val targets = ProfileCalculations.calcTargets(profile)

        assertEquals(1480, targets.daily)
        assertEquals(340, targets.sniadania)
        assertEquals(260, targets.drugie)
        assertEquals(410, targets.obiady)
        assertEquals(280, targets.kolacje)
        assertEquals(200, targets.deser)
    }

    @Test
    fun `mezczyzna, budowanie masy, umiarkowanie aktywny`() {
        val profile = Profile(
            sex = Sex.MEZCZYZNA,
            age = 30,
            heightCm = 180,
            weightKg = 80.0,
            targetWeightKg = 85.0,
            activity = ActivityLevel.UMIARKOWANIE_AKTYWNY,
            goal = Goal.BUDOWANIE,
            configured = true,
        )

        val targets = ProfileCalculations.calcTargets(profile)

        assertEquals(3170, targets.daily)
        assertEquals(720, targets.sniadania)
        assertEquals(550, targets.drugie)
        assertEquals(890, targets.obiady)
        assertEquals(590, targets.kolacje)
        assertEquals(420, targets.deser)
    }

    @Test
    fun `macro targets for default profile (kobieta, redukcja) match hand-calculated grams`() {
        val profile = Profile(
            sex = Sex.KOBIETA,
            age = 37,
            heightCm = 164,
            weightKg = 67.0,
            targetWeightKg = 62.0,
            activity = ActivityLevel.LEKKO_AKTYWNY,
            goal = Goal.REDUKCJA,
            configured = true,
        )

        val macros = ProfileCalculations.calcMacroTargets(profile)

        // daily=1480 kcal, redukcja ratios protein 0.30/carbs 0.35/fat 0.35
        assertEquals(MacroGrams(protein = 111, carbs = 130, fat = 58), macros.daily)
        // sniadania=340 kcal
        assertEquals(MacroGrams(protein = 26, carbs = 30, fat = 13), macros.sniadania)
    }

    @Test
    fun `macro targets for budowanie masy use the higher-carb ratio`() {
        val profile = Profile(
            sex = Sex.MEZCZYZNA,
            age = 30,
            heightCm = 180,
            weightKg = 80.0,
            targetWeightKg = 85.0,
            activity = ActivityLevel.UMIARKOWANIE_AKTYWNY,
            goal = Goal.BUDOWANIE,
            configured = true,
        )

        val macros = ProfileCalculations.calcMacroTargets(profile)

        // daily=3170 kcal, budowanie ratios protein 0.25/carbs 0.50/fat 0.25
        assertEquals(MacroGrams(protein = 198, carbs = 396, fat = 88), macros.daily)
    }

    @Test
    fun `utrzymanie wagi does not scale TDEE`() {
        val profile = Profile(
            sex = Sex.KOBIETA,
            age = 25,
            heightCm = 170,
            weightKg = 60.0,
            targetWeightKg = 60.0,
            activity = ActivityLevel.SIEDZACY,
            goal = Goal.UTRZYMANIE,
            configured = true,
        )

        val targets = ProfileCalculations.calcTargets(profile)

        val bmr = 10 * 60.0 + 6.25 * 170 - 5 * 25 - 161
        val expectedDaily = Math.round(bmr * 1.2 / 10) * 10
        assertEquals(expectedDaily.toInt(), targets.daily)
    }
}
