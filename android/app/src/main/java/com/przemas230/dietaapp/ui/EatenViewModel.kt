package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import com.przemas230.dietaapp.data.EatenDay
import com.przemas230.dietaapp.data.EatenEntry
import com.przemas230.dietaapp.data.Snack
import com.przemas230.dietaapp.logic.EatenOperations
import java.time.LocalDate
import java.time.ZoneOffset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * FR-36/FR-33/FR-34/FR-83: "did I eat this" state, keyed by date (mirrors
 * index.html's state.eaten[date] shape) so both TODAY's tracking (the
 * always-visible header panel) and editing an EARLIER day (the Postęp tab's
 * date-navigable tracker, FR-83) read/write the same underlying history.
 *
 * Before FR-83 this only ever held "today" -- [toggle]/[addSnack]/
 * [removeSnack] (unchanged signatures, still operate on today) and
 * [entries]/[snacks] (today's slice, unchanged shape) are kept exactly as
 * they were so every pre-FR-83 call site (HeaderKcalPanel, MainActivity's
 * onToggleEaten/onRemoveSnack wiring) needed no changes. [kcalHistory] is
 * now genuinely derived from the full per-date map on every mutation
 * instead of being separately accumulated, so editing a PAST day
 * immediately recomputes that day's history entry too -- previously
 * impossible since past days simply didn't exist here.
 */
class EatenViewModel : ViewModel() {
    private val _days = MutableStateFlow<Map<String, EatenDay>>(emptyMap())
    val days: StateFlow<Map<String, EatenDay>> = _days.asStateFlow()

    /** FR-83: which date the Postęp "co zjadłam" tracker is currently showing/editing -- never allowed past today, see [setSelectedDate]. */
    private val _selectedDate = MutableStateFlow(todayUtc())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _entries = MutableStateFlow<Map<String, EatenEntry>>(emptyMap())
    val entries: StateFlow<Map<String, EatenEntry>> = _entries.asStateFlow()

    private val _snacks = MutableStateFlow<List<Snack>>(emptyList())
    val snacks: StateFlow<List<Snack>> = _snacks.asStateFlow()

    // FR-41/42: date-string -> that day's total eaten kcal -- derived from
    // _days below on every change, so it always reflects the full history,
    // including any past day edited via FR-83.
    private val _kcalHistory = MutableStateFlow<Map<String, Int>>(emptyMap())
    val kcalHistory: StateFlow<Map<String, Int>> = _kcalHistory.asStateFlow()

    /** Swipe-to-mark-eaten toggles on today's header panel, same as index.html's `setEaten(today, cat, !wasEaten)`. */
    fun toggle(cat: String, plannedKcal: Int?, plannedName: String?) = toggleForDate(todayUtc(), cat, plannedKcal, plannedName)

    /** FR-83: same as [toggle] but for an arbitrary (non-future) date -- port of index.html's `setEaten(viewDate, cat, checked)`. */
    fun toggleForDate(date: LocalDate, cat: String, plannedKcal: Int?, plannedName: String?) {
        val key = date.toString()
        val day = _days.value[key] ?: EatenDay()
        val wasEaten = EatenOperations.isEaten(day.entries, cat)
        val newEntries = EatenOperations.setEaten(day.entries, cat, !wasEaten, plannedKcal, plannedName)
        applyDays(_days.value + (key to day.copy(entries = newEntries)))
    }

    /**
     * FR-87/v14: the Planer dashboard's directional swipe (right = eaten,
     * left = not eaten) -- unlike [toggle], always sets to a SPECIFIC
     * state regardless of what it currently is, matching index.html's
     * `setEaten(today, cat, dx > 0)` (a plain toggle read as "broken" when
     * swiping the "wrong" way on a card already in that state did the
     * opposite of what it looked like it should).
     */
    fun setEaten(cat: String, eaten: Boolean, plannedKcal: Int?, plannedName: String?) {
        val key = todayUtc().toString()
        val day = _days.value[key] ?: EatenDay()
        val newEntries = EatenOperations.setEaten(day.entries, cat, eaten, plannedKcal, plannedName)
        applyDays(_days.value + (key to day.copy(entries = newEntries)))
    }

    /** FR-33/34: the global "➕" quick-add dialog's "+ Dodaj" button -- always today. */
    fun addSnack(name: String, kcal: Int) = addSnackForDate(todayUtc(), name, kcal)

    /** FR-83: FR-33/34's snack add, but for an arbitrary (non-future) date. */
    fun addSnackForDate(date: LocalDate, name: String, kcal: Int) {
        val key = date.toString()
        val day = _days.value[key] ?: EatenDay()
        applyDays(_days.value + (key to day.copy(snacks = day.snacks + Snack(UUID.randomUUID().toString(), name, kcal))))
    }

    fun removeSnack(id: String) = removeSnackForDate(todayUtc(), id)

    /** FR-83: FR-33/34's snack removal, but for an arbitrary date. */
    fun removeSnackForDate(date: LocalDate, id: String) {
        val key = date.toString()
        val day = _days.value[key] ?: return
        applyDays(_days.value + (key to day.copy(snacks = day.snacks.filterNot { it.id == id })))
    }

    /** FR-83: never allows navigating into the future, matching index.html's `if(trackerViewDate > today) trackerViewDate = today`. */
    fun setSelectedDate(date: LocalDate) {
        val today = todayUtc()
        _selectedDate.value = if (date.isAfter(today)) today else date
    }

    /** FR-73/local persistence: applies an incoming snapshot wholesale (last-write-wins), replacing the entire local history. */
    fun replaceAll(days: Map<String, EatenDay>) = applyDays(days)

    private fun applyDays(newDays: Map<String, EatenDay>) {
        _days.value = newDays
        val today = newDays[todayUtc().toString()] ?: EatenDay()
        _entries.value = today.entries
        _snacks.value = today.snacks
        _kcalHistory.value = newDays.mapValues { (_, day) ->
            EatenOperations.dailyEatenKcal(day.entries) + EatenOperations.snacksKcal(day.snacks)
        }
    }

    private fun todayUtc(): LocalDate = LocalDate.now(ZoneOffset.UTC)
}
