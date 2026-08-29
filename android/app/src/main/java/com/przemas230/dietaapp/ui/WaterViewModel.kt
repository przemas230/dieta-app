package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import com.przemas230.dietaapp.logic.WaterOperations
import java.time.LocalDate
import java.time.ZoneOffset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.przemas230.dietaapp.logic.AppDates

/** FR-70: today's hydration count (0-8 glasses). Local-only for now, same as the other local-only ViewModels -- see android/PARITY.md. */
class WaterViewModel : ViewModel() {
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    // FR-42: date-string -> that day's final water count, mirrors
    // index.html's state.waterHistory[date] -- fuels calcWaterStreak. Only
    // ever writes TODAY's key, same "starts accumulating from today"
    // limitation as EatenViewModel.kcalHistory.
    private val _history = MutableStateFlow<Map<String, Int>>(emptyMap())
    val history: StateFlow<Map<String, Int>> = _history.asStateFlow()

    fun tapDroplet(index: Int) {
        _count.value = WaterOperations.tapDroplet(_count.value, index)
        recordTodayInHistory()
    }

    /** FR-73/local persistence: applies an incoming snapshot (only ever for today -- see CloudSyncCodec.decodeWater). */
    fun setCount(count: Int) {
        _count.value = count
        recordTodayInHistory()
    }

    /** Used by LocalPersistenceCoordinator on app startup to restore history saved on a previous run. */
    fun replaceHistory(history: Map<String, Int>) {
        _history.value = history
    }

    private fun recordTodayInHistory() {
        val today = AppDates.todayKey()
        _history.value = _history.value + (today to _count.value)
    }
}
