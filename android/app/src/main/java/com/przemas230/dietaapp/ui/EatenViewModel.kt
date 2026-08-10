package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import com.przemas230.dietaapp.data.EatenEntry
import com.przemas230.dietaapp.data.Snack
import com.przemas230.dietaapp.logic.EatenOperations
import java.time.LocalDate
import java.time.ZoneOffset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/** FR-36/FR-33/FR-34: today's "did I eat this" state per Planer category, plus ad-hoc snacks. Local-only, same as the other local-only ViewModels -- see android/PARITY.md. */
class EatenViewModel : ViewModel() {
    private val _entries = MutableStateFlow<Map<String, EatenEntry>>(emptyMap())
    val entries: StateFlow<Map<String, EatenEntry>> = _entries.asStateFlow()

    private val _snacks = MutableStateFlow<List<Snack>>(emptyList())
    val snacks: StateFlow<List<Snack>> = _snacks.asStateFlow()

    // FR-41/42: date-string -> that day's total eaten kcal, updated
    // automatically (below) whenever today's entries/snacks change --
    // mirrors index.html's state.eaten[date] naturally accumulating history
    // as different dates get touched. Android only ever tracks "today", so
    // this only ever writes TODAY's key -- history starts accumulating from
    // whenever this shipped, same "no retroactive data" limitation already
    // documented for the rest of this feature area.
    private val _kcalHistory = MutableStateFlow<Map<String, Int>>(emptyMap())
    val kcalHistory: StateFlow<Map<String, Int>> = _kcalHistory.asStateFlow()

    /** Swipe-to-mark-eaten toggles, same as index.html's `setEaten(today, cat, !wasEaten)`. */
    fun toggle(cat: String, plannedKcal: Int?, plannedName: String?) {
        val wasEaten = EatenOperations.isEaten(_entries.value, cat)
        _entries.value = EatenOperations.setEaten(_entries.value, cat, !wasEaten, plannedKcal, plannedName)
        recordTodayInHistory()
    }

    /** FR-33/34: the global "➕" quick-add dialog's "+ Dodaj" button. */
    fun addSnack(name: String, kcal: Int) {
        _snacks.value = _snacks.value + Snack(UUID.randomUUID().toString(), name, kcal)
        recordTodayInHistory()
    }

    fun removeSnack(id: String) {
        _snacks.value = _snacks.value.filterNot { it.id == id }
        recordTodayInHistory()
    }

    /** FR-73/local persistence: applies an incoming snapshot wholesale (last-write-wins), replacing local state. */
    fun replaceAll(entries: Map<String, EatenEntry>, snacks: List<Snack>) {
        _entries.value = entries
        _snacks.value = snacks
        recordTodayInHistory()
    }

    /** Used by LocalPersistenceCoordinator on app startup to restore history saved on a previous run. */
    fun replaceHistory(history: Map<String, Int>) {
        _kcalHistory.value = history
    }

    private fun recordTodayInHistory() {
        val today = LocalDate.now(ZoneOffset.UTC).toString()
        val total = EatenOperations.dailyEatenKcal(_entries.value) + EatenOperations.snacksKcal(_snacks.value)
        _kcalHistory.value = _kcalHistory.value + (today to total)
    }
}
