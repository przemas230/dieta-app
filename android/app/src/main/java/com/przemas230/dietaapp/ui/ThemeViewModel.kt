package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import com.przemas230.dietaapp.logic.AppThemes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** FR-48: which of the 11 AppThemes.ALL palettes is active -- local state, like the rest of this app version. */
class ThemeViewModel : ViewModel() {
    private val _themeId = MutableStateFlow(AppThemes.DEFAULT_ID)
    val themeId: StateFlow<String> = _themeId.asStateFlow()

    fun setTheme(id: String) {
        _themeId.value = id
    }
}
