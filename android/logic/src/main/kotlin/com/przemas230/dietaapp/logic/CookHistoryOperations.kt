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
     * FR-103: index of the LAST "zrobione" entry logged for [recipeId] on
     * the UTC day containing [nowEpochMillis], or -1. The Planer's short
     * right-swipe uses it to stay idempotent (never subtract the pantry
     * twice for the same dish on the same day) and its long left-swipe
     * uses it to know what to undo. UTC on purpose: every date key in this
     * app (EatenViewModel.todayUtc, index.html's todayStr) is a UTC
     * yyyy-mm-dd slice, so anything else would disagree with the eaten
     * record it sits next to.
     */
    fun cookedTodayIndex(
        entries: Map<String, List<CookEntry>>,
        recipeId: String,
        nowEpochMillis: Long,
    ): Int {
        val list = entries[recipeId] ?: return -1
        val today = utcDay(nowEpochMillis)
        return list.indexOfLast { utcDay(it.dateEpochMillis) == today }
    }

    private fun utcDay(epochMillis: Long): Long = Math.floorDiv(epochMillis, 86_400_000L)

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
