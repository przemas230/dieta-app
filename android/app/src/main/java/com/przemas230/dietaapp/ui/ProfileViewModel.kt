package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.logic.DailyCalorieTargets
import com.przemas230.dietaapp.logic.ProfileCalculations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Shared across the whole NavHost (created once in DietaAppRoot, not per
 * screen) so the header subtitle and the Ustawienia form always agree on
 * the same profile — see MainActivity.kt. Local-only for now, same as
 * PantryViewModel/ShoppingViewModel: resets on process death until step 6
 * (persistence + Firestore sync) lands for all of these together.
 */
class ProfileViewModel : ViewModel() {
    private val _profile = MutableStateFlow(Profile())
    val profile: StateFlow<Profile> = _profile.asStateFlow()

    // FR-65: independent of Profile on purpose -- resetToDefault() below must
    // NOT clear it (index.html keeps displayName and profile as separate
    // top-level state keys for exactly this reason).
    private val _displayName = MutableStateFlow("")
    val displayName: StateFlow<String> = _displayName.asStateFlow()

    fun targets(): DailyCalorieTargets = ProfileCalculations.calcTargets(_profile.value)

    fun save(profile: Profile) {
        _profile.value = profile.copy(configured = true)
    }

    fun resetToDefault() {
        _profile.value = Profile(configured = true)
    }

    /** Saves on every keystroke, same as index.html's setDisplayName input listener -- no separate "Zapisz" button. */
    fun setDisplayName(name: String) {
        _displayName.value = name.take(30)
    }
}
