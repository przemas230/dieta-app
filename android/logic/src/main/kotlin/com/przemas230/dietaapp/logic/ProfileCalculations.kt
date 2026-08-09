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

    private fun roundTo10(value: Double): Int = (value / 10.0).roundToInt() * 10
}
