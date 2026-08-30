package com.przemas230.dietaapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.przemas230.dietaapp.data.PlannedMeal
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.RecipeRepository
import com.przemas230.dietaapp.logic.PlannerOperations
import com.przemas230.dietaapp.logic.ProfileCalculations
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

    // FR-115 (2026-08-30): one saved snapshot of a whole week -- null if
    // nothing saved yet. Single-slot by design (not a named list), same
    // simplification as the web version. PlannedMeal already bundles
    // scale+isLeftover, so unlike web's three parallel maps this is just
    // one WeekPlan, not three.
    private val _weekTemplate = MutableStateFlow<WeekPlan?>(null)
    val weekTemplate: StateFlow<WeekPlan?> = _weekTemplate.asStateFlow()

    init {
        viewModelScope.launch {
            _allRecipes.value = withContext(Dispatchers.IO) { RecipeRepository.loadRecipes(application) }
        }
    }

    fun setMeal(day: Int, cat: String, recipeId: String, scale: Double) {
        _weekPlan.value = PlannerOperations.setMeal(_weekPlan.value, day, cat, PlannedMeal(recipeId, scale))
    }

    /**
     * Requested 2026-08-26 ("Cofnij" undo after removing a planned meal):
     * restores a FULL captured PlannedMeal (recipeId+scale+isLeftover) in
     * one write -- setMeal(day,cat,recipeId,scale) above always defaults
     * isLeftover=false, so calling it followed by planLeftover() would
     * silently reset scale back to 1.0 (planLeftover always writes
     * scale=1.0), losing whatever real portion size the meal had.
     */
    fun restoreMeal(day: Int, cat: String, meal: PlannedMeal) {
        _weekPlan.value = PlannerOperations.setMeal(_weekPlan.value, day, cat, meal)
    }

    /**
     * FR-109: moves one planned dish to the same slot on another day,
     * swapping if that day is taken -- see [PlannerOperations.moveMeal] for
     * why a swap and not an overwrite. Being its own inverse when swapping,
     * and a plain move back otherwise, this is also what "Cofnij" calls.
     */
    fun moveMeal(fromDay: Int, toDay: Int, cat: String) {
        _weekPlan.value = PlannerOperations.moveMeal(_weekPlan.value, fromDay, toDay, cat)
    }

    /** FR-111: "🍱 ugotuj na dwa dni" -- adds a base-scale leftover copy on toDay, never touches fromDay. */
    fun cookForTwoDays(fromDay: Int, toDay: Int, cat: String) {
        _weekPlan.value = PlannerOperations.cookForTwoDays(_weekPlan.value, fromDay, toDay, cat)
    }

    fun clearSlot(day: Int, cat: String) {
        _weekPlan.value = PlannerOperations.clearSlot(_weekPlan.value, day, cat)
    }

    fun setScale(day: Int, cat: String, scale: Double) {
        _weekPlan.value = PlannerOperations.setScale(_weekPlan.value, day, cat, scale)
    }

    /** FR-21: "🎲 Losuj ten dzień" -- overwrites only this one day. */
    fun randomizeDay(day: Int, profile: Profile) {
        val macroTargets = ProfileCalculations.calcMacroTargets(profile)
        val kcalTargets = ProfileCalculations.calcTargets(profile)
        _weekPlan.value = PlannerOperations.randomizeDay(_weekPlan.value, day, _allRecipes.value, profile, macroTargets, kcalTargets)
    }

    /** FR-21: "🎲 Wygeneruj losowo cały tydzień" -- overwrites the whole week. */
    fun randomizeWeek(profile: Profile) {
        val macroTargets = ProfileCalculations.calcMacroTargets(profile)
        val kcalTargets = ProfileCalculations.calcTargets(profile)
        _weekPlan.value = PlannerOperations.randomizeWeek(_allRecipes.value, profile, macroTargets, kcalTargets)
    }

    /** FR-22: "🗑️ Wyczyść ten dzień". */
    fun clearDay(day: Int) {
        _weekPlan.value = PlannerOperations.clearDay(_weekPlan.value, day)
    }

    /**
     * FR-21/v2 + FR-22/v2: puts one day back exactly as it was, for the
     * "Cofnij" after randomising or clearing it. A whole-day replace rather
     * than replaying per-slot writes, so scale and leftover flags come back
     * too instead of only the recipe ids.
     */
    fun replaceDay(day: Int, meals: Map<String, com.przemas230.dietaapp.data.PlannedMeal>) {
        _weekPlan.value = _weekPlan.value + (day to meals)
    }

    /** "🔁 Losuj inne danie" for one slot. */
    fun regenerateSlot(day: Int, cat: String, profile: Profile) {
        val macroTargets = ProfileCalculations.calcMacroTargets(profile)
        val kcalTargets = ProfileCalculations.calcTargets(profile)
        _weekPlan.value = PlannerOperations.regenerateSlot(_weekPlan.value, day, cat, _allRecipes.value, profile, macroTargets, kcalTargets)
    }

    /** FR-23/24: carry a recipe over as a leftovers entry. */
    fun planLeftover(day: Int, cat: String, recipeId: String) {
        _weekPlan.value = PlannerOperations.planLeftover(_weekPlan.value, day, cat, recipeId)
    }

    /** Requested 2026-08-26 ("📋 Kopiuj plan z innego dnia"): overwrites toDay with fromDay's plan. */
    fun copyDay(fromDay: Int, toDay: Int) {
        _weekPlan.value = PlannerOperations.copyDay(_weekPlan.value, fromDay, toDay)
    }

    /** FR-73: applies an incoming cloud snapshot wholesale (last-cloud-write-wins), replacing local state. */
    fun replaceAll(plan: WeekPlan) {
        _weekPlan.value = plan
    }

    /** FR-115: "💾 Zapisz ten tydzień jako szablon" -- overwrites whatever was saved before. */
    fun saveWeekAsTemplate() {
        _weekTemplate.value = _weekPlan.value
    }

    /** FR-115: "📂 Wczytaj szablon" -- overwrites the whole current week; caller owns confirm/undo. */
    fun loadWeekTemplate() {
        _weekTemplate.value?.let { _weekPlan.value = it }
    }

    /** FR-115: local-persistence restore on app start (LocalPersistenceCoordinator). */
    fun replaceWeekTemplate(plan: WeekPlan?) {
        _weekTemplate.value = plan
    }
}
