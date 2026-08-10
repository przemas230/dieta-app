package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import com.przemas230.dietaapp.data.ActivityLogEntry
import com.przemas230.dietaapp.logic.ActivityLogOperations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * FR-42: diagnostic log of pantry/shopping mutations -- index.html's
 * addLog()/state.history. Shared at the MainActivity level (like
 * pantryViewModel etc.) so every screen that mutates pantry/shopping can
 * log to the same list the Postęp tab displays.
 */
class ActivityLogViewModel : ViewModel() {
    private val _entries = MutableStateFlow<List<ActivityLogEntry>>(emptyList())
    val entries: StateFlow<List<ActivityLogEntry>> = _entries.asStateFlow()

    fun log(action: String, detail: String) {
        _entries.value = ActivityLogOperations.addEntry(_entries.value, action, detail, System.currentTimeMillis())
    }

    fun clear() {
        _entries.value = emptyList()
    }

    /** Used by LocalPersistenceCoordinator on app startup to restore the log saved on a previous run. */
    fun replaceAll(entries: List<ActivityLogEntry>) {
        _entries.value = entries
    }
}
