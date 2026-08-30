package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.WeightEntry
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

/**
 * Self-initiated 2026-08-30 (user: "propozycje jak w najnowszych
 * aplikacjach tego typu na świecie" -- ported from web the same turn, see
 * index.html's computeAdaptiveTdee for the original comment this mirrors).
 *
 * The single most-copied differentiator of the current best-in-class app
 * in this category, MacroFactor, over MyFitnessPal-style static-formula
 * apps: an ADAPTIVE calorie estimate derived from what actually happened
 * to the user's weight given what they actually ate, instead of trusting
 * a Mifflin-St Jeor formula that can be off by hundreds of kcal for a
 * real person.
 *
 * Advisory-only -- does NOT touch [ProfileCalculations.calcTargets] or any
 * of its call sites, just reports what the data suggests alongside it.
 *
 * Math: a least-squares slope over the weight points (not just
 * first-vs-last, so one noisy day doesn't swing the whole estimate) gives
 * kg/day. (avg daily kcal actually logged) minus (kg/day × 7700, the
 * standard kcal-per-kg-bodymass constant) is what daily intake would have
 * to be to hold weight exactly steady -- i.e. the real, current TDEE
 * implied by the data.
 *
 * Takes a plain `date -> total kcal that day` map rather than the raw
 * eaten records: [EatenViewModel] already builds exactly that shape
 * ([EatenViewModel.kcalHistory], the same one the Postęp history list
 * uses), so the UI layer needs zero new plumbing to call this.
 */
object AdaptiveTdee {
    const val WINDOW_DAYS = 28
    const val MIN_SPAN_DAYS = 10
    const val MIN_LOGGED_DAYS = 6
    const val KCAL_PER_KG = 7700.0

    data class Result(
        val estimatedTdee: Int,
        val weeklyKgChange: Double,
        val avgKcal: Int,
        val loggedDays: Int,
        val spanDays: Int,
    )

    fun compute(weightEntries: List<WeightEntry>, kcalHistory: Map<String, Int>, today: LocalDate): Result? {
        val cutoff = today.minusDays(WINDOW_DAYS.toLong())
        val weights = weightEntries
            .filter { LocalDate.parse(it.dateStr) >= cutoff }
            .sortedBy { it.dateStr }
        if (weights.size < 2) return null
        val firstDate = LocalDate.parse(weights.first().dateStr)
        val lastDate = LocalDate.parse(weights.last().dateStr)
        val spanDays = ChronoUnit.DAYS.between(firstDate, lastDate).toInt()
        if (spanDays < MIN_SPAN_DAYS) return null

        val xs = weights.map { ChronoUnit.DAYS.between(firstDate, LocalDate.parse(it.dateStr)).toDouble() }
        val ys = weights.map { it.kg }
        val n = xs.size
        val sumX = xs.sum()
        val sumY = ys.sum()
        val sumXY = xs.indices.sumOf { xs[it] * ys[it] }
        val sumXX = xs.sumOf { it * it }
        val denom = n * sumXX - sumX * sumX
        if (denom == 0.0) return null // every point on the same day -- no slope to fit
        val kgPerDay = (n * sumXY - sumX * sumY) / denom

        // Average logged kcal over the SAME date range -- only days present
        // in kcalHistory count. A day missing from the map has no data at
        // all, and averaging it in as zero would crater the estimate for
        // anyone who doesn't log every single day.
        val loggedKcals = mutableListOf<Int>()
        var d = firstDate
        while (!d.isAfter(lastDate)) {
            kcalHistory[d.toString()]?.let { loggedKcals.add(it) }
            d = d.plusDays(1)
        }
        if (loggedKcals.size < MIN_LOGGED_DAYS) return null
        val avgKcal = loggedKcals.average()

        return Result(
            estimatedTdee = (avgKcal - kgPerDay * KCAL_PER_KG).roundToInt(),
            weeklyKgChange = kgPerDay * 7,
            avgKcal = avgKcal.roundToInt(),
            loggedDays = loggedKcals.size,
            spanDays = spanDays,
        )
    }
}
