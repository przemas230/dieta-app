package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.WeightEntry

/** FR-40: pure port of index.html's addWeightBtn handler. */
object WeightOperations {
    /** Returns null (caller shows "Podaj prawidłową wagę") if kg is outside [30, 250], same range as the web form. */
    fun addWeight(entries: List<WeightEntry>, todayStr: String, kg: Double): List<WeightEntry>? {
        if (kg < 30.0 || kg > 250.0) return null
        // One entry per day -- a same-day re-add replaces the earlier value, doesn't duplicate.
        return entries.filterNot { it.dateStr == todayStr } + WeightEntry(todayStr, kg)
    }

    fun sortedByDate(entries: List<WeightEntry>): List<WeightEntry> = entries.sortedBy { it.dateStr }

    /** How many kg left to the target -- positive means still above it, negative/zero means goal reached. Null if there's no entry yet. */
    fun kgToGo(entries: List<WeightEntry>, targetKg: Double): Double? {
        val last = sortedByDate(entries).lastOrNull() ?: return null
        return Math.round((last.kg - targetKg) * 10) / 10.0
    }
}
