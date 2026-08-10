package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import com.przemas230.dietaapp.logic.WaterOperations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** FR-70: today's hydration count (0-8 glasses). Local-only for now, same as the other local-only ViewModels -- see android/PARITY.md. */
class WaterViewModel : ViewModel() {
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    fun tapDroplet(index: Int) {
        _count.value = WaterOperations.tapDroplet(_count.value, index)
    }

    /** FR-73: applies an incoming cloud snapshot (only ever for today -- see CloudSyncCodec.decodeWater). */
    fun setCount(count: Int) {
        _count.value = count
    }
}
