package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import com.przemas230.dietaapp.data.EatenEntry
import com.przemas230.dietaapp.data.Snack
import com.przemas230.dietaapp.logic.EatenOperations
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

    /** Swipe-to-mark-eaten toggles, same as index.html's `setEaten(today, cat, !wasEaten)`. */
    fun toggle(cat: String, plannedKcal: Int?, plannedName: String?) {
        val wasEaten = EatenOperations.isEaten(_entries.value, cat)
        _entries.value = EatenOperations.setEaten(_entries.value, cat, !wasEaten, plannedKcal, plannedName)
    }

    /** FR-33/34: the global "➕" quick-add dialog's "+ Dodaj" button. */
    fun addSnack(name: String, kcal: Int) {
        _snacks.value = _snacks.value + Snack(UUID.randomUUID().toString(), name, kcal)
    }

    fun removeSnack(id: String) {
        _snacks.value = _snacks.value.filterNot { it.id == id }
    }
}
