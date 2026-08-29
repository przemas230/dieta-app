package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.EatenEntry
import com.przemas230.dietaapp.data.Snack

/**
 * FR-36: pure port of index.html's setEaten/isEaten/dailyEatenKcal, scoped
 * to "today" only -- Android has no per-arbitrary-date history yet (that's
 * FR-41/42, not ported), so this only ever tracks the current day's 5
 * Planer slots, keyed by category id. Doesn't cover ad-hoc snacks
 * (state.eaten[date].snacks in index.html) -- that's FR-33/34, also not
 * ported yet.
 */
object EatenOperations {
    fun isEaten(entries: Map<String, EatenEntry>, cat: String): Boolean = entries[cat]?.done == true

    /**
     * Marking eaten captures the planned recipe's current kcal/name.
     * Unmarking keeps whatever kcal/name was last captured (matches
     * index.html: "done:false" still carries the previous kcal/name instead
     * of discarding it), so a summary that reads *only* done entries never
     * loses that context if something re-checks it later.
     */
    fun setEaten(
        entries: Map<String, EatenEntry>,
        cat: String,
        done: Boolean,
        plannedKcal: Int?,
        plannedName: String?,
        portion: Double = 1.0,
    ): Map<String, EatenEntry> {
        val entry = if (done) {
            EatenEntry(done = true, kcal = plannedKcal ?: 0, name = plannedName, portion = portion.coerceIn(0.0, 1.0))
        } else {
            val prev = entries[cat]
            EatenEntry(done = false, kcal = prev?.kcal, name = prev?.name, portion = 0.0)
        }
        return entries + (cat to entry)
    }

    /** FR-103: 0 when not eaten, otherwise the recorded fraction (1.0 for anything written before portions existed). */
    fun portionOf(entries: Map<String, EatenEntry>, cat: String): Double {
        val entry = entries[cat] ?: return 0.0
        if (!entry.done) return 0.0
        return entry.portion.coerceIn(0.0, 1.0)
    }

    /** FR-103: half a portion counts half the kcal -- matches index.html's `Math.round(e.kcal * portion)`. */
    fun dailyEatenKcal(entries: Map<String, EatenEntry>): Int =
        entries.values.filter { it.done }.sumOf { Math.round((it.kcal ?: 0) * it.portion.coerceIn(0.0, 1.0)).toInt() }

    /** FR-33/34: ad-hoc snacks add on top of the 5 Planer slots -- port of index.html's `(day.snacks||[]).forEach(s=> total += s.kcal)`. */
    fun snacksKcal(snacks: List<Snack>): Int = snacks.sumOf { it.kcal }
}
