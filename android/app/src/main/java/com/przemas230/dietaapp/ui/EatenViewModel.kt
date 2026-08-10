package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import com.przemas230.dietaapp.data.EatenEntry
import com.przemas230.dietaapp.logic.EatenOperations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** FR-36: today's "did I eat this" state per Planer category. Local-only, same as the other local-only ViewModels -- see android/PARITY.md. */
class EatenViewModel : ViewModel() {
    private val _entries = MutableStateFlow<Map<String, EatenEntry>>(emptyMap())
    val entries: StateFlow<Map<String, EatenEntry>> = _entries.asStateFlow()

    /** Swipe-to-mark-eaten toggles, same as index.html's `setEaten(today, cat, !wasEaten)`. */
    fun toggle(cat: String, plannedKcal: Int?, plannedName: String?) {
        val wasEaten = EatenOperations.isEaten(_entries.value, cat)
        _entries.value = EatenOperations.setEaten(_entries.value, cat, !wasEaten, plannedKcal, plannedName)
    }
}
