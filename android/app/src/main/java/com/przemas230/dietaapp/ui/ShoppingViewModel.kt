package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import com.przemas230.dietaapp.data.PlannedMeal
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.ShoppingItem
import com.przemas230.dietaapp.logic.ShoppingOperations
import com.przemas230.dietaapp.logic.WeekPlan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * FR-25: thin StateFlow/Android wrapper around ShoppingOperations (in the
 * :logic module — unit-tested there in ShoppingOperationsTest). Local-only
 * for now, not yet persisted or synced (README.md step 6).
 */
class ShoppingViewModel : ViewModel() {
    private val _items = MutableStateFlow<Map<String, ShoppingItem>>(emptyMap())
    val items: StateFlow<Map<String, ShoppingItem>> = _items.asStateFlow()

    fun addRecipe(recipe: Recipe) {
        _items.value = ShoppingOperations.addRecipe(_items.value, recipe)
    }

    fun removeRecipe(recipe: Recipe) {
        _items.value = ShoppingOperations.removeRecipe(_items.value, recipe)
    }

    fun addSingleIngredient(ingredientText: String, sourceKey: String) {
        _items.value = ShoppingOperations.addSingleIngredient(_items.value, ingredientText, sourceKey)
    }

    /** The Planer's own per-day "🛒 Dodaj składniki z tego dnia" button. */
    fun addDayPlan(dayMeals: Map<String, PlannedMeal>, recipesById: Map<String, Recipe>) {
        _items.value = ShoppingOperations.addDayPlan(_items.value, dayMeals, recipesById)
    }

    /** FR-27: "add the whole week's ingredients" button on the Zakupy tab. */
    fun addWeekPlan(weekPlan: WeekPlan, recipesById: Map<String, Recipe>) {
        _items.value = ShoppingOperations.addWeekPlan(_items.value, weekPlan, recipesById)
    }

    fun toggleChecked(key: String) {
        _items.value = ShoppingOperations.toggleChecked(_items.value, key)
    }

    fun removeItem(key: String) {
        _items.value = ShoppingOperations.removeItem(_items.value, key)
    }

    fun clearChecked() {
        _items.value = ShoppingOperations.clearChecked(_items.value)
    }
}
