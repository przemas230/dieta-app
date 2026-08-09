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
