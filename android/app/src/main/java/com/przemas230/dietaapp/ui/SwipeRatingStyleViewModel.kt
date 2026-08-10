package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** FR-61: which visual feedback a FR-55 card swipe shows while dragging -- independent of the color theme (FR-48, not done yet). */
enum class SwipeRatingStyle { BALLOON, GLOW }

/** Default BALLOON, matching index.html's `state.swipeRatingStyle||"balloon"` fallback. */
class SwipeRatingStyleViewModel : ViewModel() {
    private val _style = MutableStateFlow(SwipeRatingStyle.BALLOON)
    val style: StateFlow<SwipeRatingStyle> = _style.asStateFlow()

    fun setStyle(style: SwipeRatingStyle) {
        _style.value = style
    }
}
