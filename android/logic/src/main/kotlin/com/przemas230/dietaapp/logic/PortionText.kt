package com.przemas230.dietaapp.logic

/**
 * FR-105 (2026-08-29): how a partial portion is worded, and which
 * ready-made sizes the picker offers — the exact port of index.html's
 * `PORTION_PRESETS`/`portionLabel()`.
 *
 * Split out of the UI so both platforms say the same thing for the same
 * number, and so the "is this one of the round fractions or an arbitrary
 * percentage" decision lives in one testable place rather than inside a
 * composable.
 */
object PortionText {
    /** The one-tap sizes offered next to the slider, as (fraction, label). */
    val PRESETS: List<Pair<Double, String>> = listOf(
        0.25 to "¼ porcji",
        0.5 to "½ porcji",
        0.75 to "¾ porcji",
        1.0 to "Cała porcja",
    )

    /**
     * Reads as "½ porcji zjedzone" for the round fractions people actually
     * think in, and falls back to a plain percentage for anything the
     * slider can land on in between.
     */
    fun label(portion: Double): String {
        val preset = PRESETS.firstOrNull { kotlin.math.abs(it.first - portion) < 0.001 }
        if (preset != null) return "${preset.second} zjedzone"
        return "${Math.round(portion * 100).toInt()}% porcji zjedzone"
    }

    /** kcal actually counted for a portion of a dish — the same rounding EatenOperations.dailyEatenKcal applies. */
    fun kcalFor(fullKcal: Int, portion: Double): Int =
        Math.round(fullKcal * portion.coerceIn(0.0, 1.0)).toInt()
}
