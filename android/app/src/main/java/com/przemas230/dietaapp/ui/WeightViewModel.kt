package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import com.przemas230.dietaapp.data.WeightEntry
import com.przemas230.dietaapp.logic.WeightOperations
import java.time.LocalDate
import java.time.ZoneOffset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** FR-40: weight log, local-only for now, same as the other local-only ViewModels -- see android/PARITY.md. */
class WeightViewModel : ViewModel() {
    private val _entries = MutableStateFlow<List<WeightEntry>>(emptyList())
    val entries: StateFlow<List<WeightEntry>> = _entries.asStateFlow()

    /** Returns false (caller shows "Podaj prawidłową wagę") if kg is outside the valid range. */
    fun addWeight(kg: Double): Boolean {
        val today = LocalDate.now(ZoneOffset.UTC).toString()
        val result = WeightOperations.addWeight(_entries.value, today, kg) ?: return false
        _entries.value = result
        return true
    }

    /** Used by LocalStateStore on app startup to restore the weight log saved on a previous run. */
    fun replaceAll(entries: List<WeightEntry>) {
        _entries.value = entries
    }
}
