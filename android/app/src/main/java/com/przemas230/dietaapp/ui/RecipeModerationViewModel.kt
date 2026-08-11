package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import com.przemas230.dietaapp.data.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * FR-76/v2 (2026-08-11, user request): "Moje przepisy" (own submission
 * status) + moderator-only approval, both new -- see
 * RecipeModerationCoordinator for the Firestore listeners/writes that feed
 * this, and SettingsScreen's `MyRecipesCard`/`RecipeModerationCard` for the UI.
 */
class RecipeModerationViewModel : ViewModel() {
    /** recipeId -> "pending"/"approved"/"rejected", for docs authored by the current signed-in uid. */
    private val _myRecipeStatuses = MutableStateFlow<Map<String, String>>(emptyMap())
    val myRecipeStatuses: StateFlow<Map<String, String>> = _myRecipeStatuses.asStateFlow()

    /** All `status == "pending"` community recipes -- only ever populated while signed in as the moderator account. */
    private val _pendingRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val pendingRecipes: StateFlow<List<Recipe>> = _pendingRecipes.asStateFlow()

    fun replaceMyRecipeStatuses(statuses: Map<String, String>) {
        _myRecipeStatuses.value = statuses
    }

    fun replacePendingRecipes(recipes: List<Recipe>) {
        _pendingRecipes.value = recipes
    }
}
