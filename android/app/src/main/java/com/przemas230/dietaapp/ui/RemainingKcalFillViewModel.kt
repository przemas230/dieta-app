package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Requested 2026-08-26 ("prostokąt pozostałe kcal mógłby się zapełniać
 * kolorem... zrób to jako opcje do włączenia w opcjach"): whether
 * PlannerDashboard's "POZOSTAŁO" tile (Klinika theme) fills with color
 * proportionally to eaten kcal, same idea as the kcal ring next to it --
 * off by default, matching index.html's `state.remainingKcalFillEnabled`.
 *
 * Local-only (LocalPersistenceCoordinator), not wired into CloudSyncCoordinator
 * -- a pure display preference, not data worth 3-way-merging across devices,
 * unlike shopping lists/plans/pantry. Deliberate scope decision, not an
 * oversight (see android/PARITY.md).
 */
class RemainingKcalFillViewModel : ViewModel() {
    private val _enabled = MutableStateFlow(false)
    val enabled: StateFlow<Boolean> = _enabled.asStateFlow()

    fun setEnabled(value: Boolean) {
        _enabled.value = value
    }
}
