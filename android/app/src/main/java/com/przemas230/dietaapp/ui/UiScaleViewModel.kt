package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * FR-14: null means "not customized yet" — MainActivity falls back to
 * UiScale.detectDefault(screenWidthDp) until the user actually drags the
 * slider, same as index.html's uiScale defaulting via detectDefaultUiScale().
 */
class UiScaleViewModel : ViewModel() {
    private val _uiScale = MutableStateFlow<Double?>(null)
    val uiScale: StateFlow<Double?> = _uiScale.asStateFlow()

    fun setScale(scale: Double) {
        _uiScale.value = scale
    }

    /** FR-79: "wyczyść dane lokalne" restores auto-detection, same as a fresh install. */
    fun resetToAuto() {
        _uiScale.value = null
    }
}
