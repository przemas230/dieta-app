package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.EatenDay

/**
 * FR-107: how much of a given dish this person usually eats.
 *
 * The data was already there — every eaten entry has carried a `portion`
 * since FR-105, and its `name` records which dish it was. Nothing new is
 * stored; this only reads back what the portion picker has been writing all
 * along. That is the whole feature: someone who habitually eats half of a
 * particular dinner should not have to say so every single time.
 *
 * Two deliberate restraints, both about not being annoying:
 *
 *  * **At least [MIN_OCCURRENCES] recorded meals.** One half-portion is an
 *    occasion, not a habit, and presenting it as one would make the app feel
 *    like it is guessing.
 *  * **A whole portion is never reported as "usual".** It is the default
 *    anyway, so saying it out loud would be pure noise on every dish the
 *    person simply finishes.
 */
object PortionHistory {
    const val MIN_OCCURRENCES = 2

    /**
     * The portion this dish is most often eaten in, or null when there is not
     * enough history to say anything useful.
     *
     * Ties go to the most RECENT of the tied values: if someone has eaten a
     * dish half twice and whole twice, what they did last time is the better
     * guess than an arbitrary pick.
     */
    fun usualPortion(days: Map<String, EatenDay>, dishName: String, minOccurrences: Int = MIN_OCCURRENCES): Double? {
        if (dishName.isBlank()) return null
        // Oldest first, so "last seen" ends up as the highest index.
        val seen = mutableListOf<Double>()
        days.entries.sortedBy { it.key }.forEach { (_, day) ->
            day.entries.values.forEach { entry ->
                if (entry.done && entry.name == dishName) {
                    seen.add(entry.portion.coerceIn(0.0, 1.0))
                }
            }
        }
        if (seen.size < minOccurrences) return null

        val counts = seen.groupingBy { it }.eachCount()
        val topCount = counts.values.max()
        val tied = counts.filterValues { it == topCount }.keys
        val winner = seen.lastOrNull { it in tied } ?: return null
        return if (winner >= 1.0) null else winner
    }

    /** Wording for the hint, e.g. "zwykle zjadasz ½ tego dania". Null when there is nothing worth saying. */
    fun usualPortionHint(days: Map<String, EatenDay>, dishName: String): String? {
        val usual = usualPortion(days, dishName) ?: return null
        val label = PortionText.PRESETS.firstOrNull { kotlin.math.abs(it.first - usual) < 0.001 }?.second
            ?: "${Math.round(usual * 100).toInt()}%"
        return "Zwykle zjadasz $label tego dania"
    }
}
