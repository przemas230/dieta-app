package com.przemas230.dietaapp.logic

import kotlin.math.roundToInt

/**
 * FR-14: port of index.html's detectDefaultUiScale — 1.0 at ≥420dp width,
 * 0.75 at ≤360dp, linearly interpolated between and rounded to the nearest
 * 0.05 step (same as the web version's Math.round(x*20)/20). Compose has no
 * direct equivalent of CSS `zoom`, so the app applies this by scaling
 * LocalDensity around the whole content tree (see MainActivity.kt) — same
 * effect as zoom affecting position:fixed elements too.
 */
object UiScale {
    const val MIN = 0.7
    const val MAX = 1.3
    const val STEP = 0.05

    fun detectDefault(screenWidthDp: Int): Double {
        val raw = when {
            screenWidthDp >= 420 -> 1.0
            screenWidthDp <= 360 -> 0.75
            else -> 0.75 + (screenWidthDp - 360).toDouble() / (420 - 360) * 0.25
        }
        return (raw * 20).roundToInt() / 20.0
    }
}
