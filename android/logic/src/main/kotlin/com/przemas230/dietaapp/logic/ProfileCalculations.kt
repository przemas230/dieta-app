package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.Goal
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.Sex
import kotlin.math.roundToInt

/** Daily kcal target plus the same 5-way meal-slot split index.html uses (MEAL_RATIOS). */
data class DailyCalorieTargets(
    val daily: Int,
    val sniadania: Int,
    val drugie: Int,
    val obiady: Int,
    val kolacje: Int,
    val deser: Int,
)

/** Target grams of each macro for one meal slot (or the whole day). */
data class MacroGrams(val protein: Int, val carbs: Int, val fat: Int)

/** FR-10: per-slot macro targets, one MacroGrams per DailyCalorieTargets field. */
data class MacroTargets(
    val daily: MacroGrams,
    val sniadania: MacroGrams,
    val drugie: MacroGrams,
    val obiady: MacroGrams,
    val kolacje: MacroGrams,
    val deser: MacroGrams,
)

private data class MacroRatio(val protein: Double, val carbs: Double, val fat: Double)

/**
 * Port of index.html's calcTargets — same Mifflin-St Jeor BMR formula, same
 * activity multiplier and goal adjustment, same meal-slot ratios (the
 * Deser/Przekąska share is carved out of the other four, not added on top —
 * see index.html's MEAL_RATIOS comment for why 200/1500).
 */
object ProfileCalculations {
    private const val SNIADANIA_RATIO = 340.0 / 1500.0
    private const val DRUGIE_RATIO = 260.0 / 1500.0
    private const val OBIADY_RATIO = 420.0 / 1500.0
    private const val KOLACJE_RATIO = 280.0 / 1500.0
    private const val DESER_RATIO = 200.0 / 1500.0

    // Port of index.html's MACRO_RATIOS — redukcja leans on protein (satiety,
    // preserving muscle mass while at a deficit), budowanie masy leans on
    // carbs, utrzymanie stays balanced.
    private val MACRO_RATIOS = mapOf(
        Goal.REDUKCJA to MacroRatio(protein = 0.30, carbs = 0.35, fat = 0.35),
        Goal.UTRZYMANIE to MacroRatio(protein = 0.25, carbs = 0.45, fat = 0.30),
        Goal.BUDOWANIE to MacroRatio(protein = 0.25, carbs = 0.50, fat = 0.25),
    )

    fun calcTargets(profile: Profile): DailyCalorieTargets {
        val bmr = if (profile.sex == Sex.MEZCZYZNA) {
            10 * profile.weightKg + 6.25 * profile.heightCm - 5 * profile.age + 5
        } else {
            10 * profile.weightKg + 6.25 * profile.heightCm - 5 * profile.age - 161
        }
        val tdee = bmr * profile.activity.factor
        val dailyRaw = when (profile.goal) {
            Goal.REDUKCJA -> tdee * 0.8
            Goal.BUDOWANIE -> tdee * 1.15
            Goal.UTRZYMANIE -> tdee
        }
        val daily = roundTo10(dailyRaw)
        return DailyCalorieTargets(
            daily = daily,
            sniadania = roundTo10(daily * SNIADANIA_RATIO),
            drugie = roundTo10(daily * DRUGIE_RATIO),
            obiady = roundTo10(daily * OBIADY_RATIO),
            kolacje = roundTo10(daily * KOLACJE_RATIO),
            deser = roundTo10(daily * DESER_RATIO),
        )
    }

    /** FR-10: docelowe gramatury makroskładników, zależne od celu i kategorii posiłku. */
    fun calcMacroTargets(profile: Profile): MacroTargets {
        val t = calcTargets(profile)
        val ratio = MACRO_RATIOS.getValue(profile.goal)
        fun gramsFor(kcalForMeal: Int) = MacroGrams(
            protein = (kcalForMeal * ratio.protein / 4).roundToInt(),
            carbs = (kcalForMeal * ratio.carbs / 4).roundToInt(),
            fat = (kcalForMeal * ratio.fat / 9).roundToInt(),
        )
        return MacroTargets(
            daily = gramsFor(t.daily),
            sniadania = gramsFor(t.sniadania),
            drugie = gramsFor(t.drugie),
            obiady = gramsFor(t.obiady),
            kolacje = gramsFor(t.kolacje),
            deser = gramsFor(t.deser),
        )
    }

    private fun roundTo10(value: Double): Int = (value / 10.0).roundToInt() * 10
}
