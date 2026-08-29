package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.CookEntry

/**
 * FR-15/FR-17: pure port of index.html's markRecipeCookedToday (history part)
 * and the star-tap/delete handlers in renderCookHistoryBody. Pantry
 * subtraction/restoration is a separate concern (RecipePantryMatching) so
 * this stays testable without any PantryItem/Recipe knowledge.
 */
object CookHistoryOperations {
    fun addToday(
        entries: Map<String, List<CookEntry>>,
        recipeId: String,
        nowEpochMillis: Long,
    ): Map<String, List<CookEntry>> =
        entries + (recipeId to (entries[recipeId].orEmpty() + CookEntry(nowEpochMillis)))

    /**
     * FR-104: same as [addToday] but for an arbitrary calendar day -- the
     * day cards can mark a dish cooked on Tuesday while it is Saturday.
     * Stored at noon of that day (see [AppDates.noonEpochMillis]) so reading
     * it back never tips into the neighbouring day.
     */
    fun addOnDate(
        entries: Map<String, List<CookEntry>>,
        recipeId: String,
        date: java.time.LocalDate,
    ): Map<String, List<CookEntry>> =
        entries + (recipeId to (entries[recipeId].orEmpty() + CookEntry(AppDates.noonEpochMillis(date))))

    /** Tapping the same star again clears the rating, same as index.html's `entry.rating === n ? null : n`. */
    fun setRating(
        entries: Map<String, List<CookEntry>>,
        recipeId: String,
        index: Int,
        rating: Int,
    ): Map<String, List<CookEntry>> {
        val list = entries[recipeId] ?: return entries
        if (index !in list.indices) return entries
        val current = list[index]
        val newRating = if (current.rating == rating) null else rating
        val newList = list.toMutableList().apply { set(index, current.copy(rating = newRating)) }
        return entries + (recipeId to newList)
    }

    /**
     * FR-103/FR-104: index of the LAST "zrobione" entry logged for
     * [recipeId] on the calendar day [dateKey] (`yyyy-MM-dd`), or -1.
     *
     * The Planer's forward swipe uses it to stay idempotent (never subtract
     * the pantry twice for the same dish on the same day) and the backward
     * swipe uses it to know what to undo. Date-parameterised rather than
     * today-only so the dashboard card and the week's day cards (FR-104)
     * share one definition of "is this cooked" instead of drifting apart.
     *
     * The day is resolved through [AppDates], i.e. the user's LOCAL
     * calendar -- same as every other date key in the app since FR-101 was
     * ported here (2026-08-29). It used to floor-divide the raw epoch,
     * which is a UTC day and would disagree with the eaten record sitting
     * right next to it for the first hour or two of every Polish day.
     */
    fun cookedOnDateIndex(
        entries: Map<String, List<CookEntry>>,
        recipeId: String,
        dateKey: String,
    ): Int {
        val list = entries[recipeId] ?: return -1
        return list.indexOfLast { AppDates.dateKey(it.dateEpochMillis) == dateKey }
    }

    fun removeEntry(
        entries: Map<String, List<CookEntry>>,
        recipeId: String,
        index: Int,
    ): Map<String, List<CookEntry>> {
        val list = entries[recipeId] ?: return entries
        if (index !in list.indices) return entries
        val newList = list.toMutableList().apply { removeAt(index) }
        return if (newList.isEmpty()) entries - recipeId else entries + (recipeId to newList)
    }
}
