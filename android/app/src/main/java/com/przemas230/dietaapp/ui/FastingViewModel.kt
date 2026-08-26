package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Requested 2026-08-26 ("dodaj też przynajmniej 5 nowych funkcji" --
 * intermittent fasting / time-restricted eating is one of the most-requested
 * diet-app features per user reviews): windowStart/windowEnd are hours of
 * day (0-23) marking the EATING window, e.g. 12-20 for a classic 16:8
 * schedule -- everything outside it is "post". Port of index.html's
 * state.fasting = {enabled, windowStart, windowEnd}.
 *
 * Local-only (LocalPersistenceCoordinator), not wired into CloudSyncCoordinator
 * -- same deliberate scope decision as RemainingKcalFillViewModel (a display/
 * schedule preference, not data worth 3-way-merging across devices). See
 * android/PARITY.md.
 */
class FastingViewModel : ViewModel() {
    private val _enabled = MutableStateFlow(false)
    val enabled: StateFlow<Boolean> = _enabled.asStateFlow()

    private val _windowStart = MutableStateFlow(12)
    val windowStart: StateFlow<Int> = _windowStart.asStateFlow()

    private val _windowEnd = MutableStateFlow(20)
    val windowEnd: StateFlow<Int> = _windowEnd.asStateFlow()

    fun setEnabled(value: Boolean) {
        _enabled.value = value
    }

    fun setWindowStart(hour: Int) {
        _windowStart.value = hour.coerceIn(0, 23)
    }

    fun setWindowEnd(hour: Int) {
        _windowEnd.value = hour.coerceIn(0, 23)
    }
}
