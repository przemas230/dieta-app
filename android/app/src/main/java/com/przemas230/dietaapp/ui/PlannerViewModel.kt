package com.przemas230.dietaapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.przemas230.dietaapp.data.PlannedMeal
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.RecipeRepository
import com.przemas230.dietaapp.logic.PlannerOperations
import com.przemas230.dietaapp.logic.WeekPlan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * FR-18/19/20: thin StateFlow/Android wrapper around PlannerOperations (in
 * the :logic module — unit-tested there in PlannerOperationsTest). Loads its
 * own copy of recipes.json (like RecipeViewModel does) rather than sharing
 * RecipeViewModel's filtered `visibleRecipes`, since the Planer's recipe
 * pickers need the full, category-scoped list regardless of the Przepisy
 * tab's search/filter state. Local-only for now, not yet persisted — same
 * as Pantry/Shopping/cook history (README.md step 6).
 */
class PlannerViewModel(application: Application) : AndroidViewModel(application) {
    private val _allRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val allRecipes: StateFlow<List<Recipe>> = _allRecipes.asStateFlow()

    private val _weekPlan = MutableStateFlow<WeekPlan>(emptyMap())
    val weekPlan: StateFlow<WeekPlan> = _weekPlan.asStateFlow()

    init {
        viewModelScope.launch {
            _allRecipes.value = withContext(Dispatchers.IO) { RecipeRepository.loadRecipes(application) }
        }
    }

    fun setMeal(day: Int, cat: String, recipeId: String, scale: Double) {
        _weekPlan.value = PlannerOperations.setMeal(_weekPlan.value, day, cat, PlannedMeal(recipeId, scale))
    }

    fun clearSlot(day: Int, cat: String) {
        _weekPlan.value = PlannerOperations.clearSlot(_weekPlan.value, day, cat)
    }

    fun setScale(day: Int, cat: String, scale: Double) {
        _weekPlan.value = PlannerOperations.setScale(_weekPlan.value, day, cat, scale)
    }
}
