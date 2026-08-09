package com.przemas230.dietaapp.data

/**
 * One filled Planer slot — mirrors index.html's state.planner[day][cat] (the
 * recipe id) plus state.plannerScale[day][cat] and state.plannerLeftover[day][cat],
 * which the web app tracks as three parallel maps. Bundled into one value
 * here since Android's Planer never needs to update just one of the three
 * independently of the recipe it belongs to.
 */
data class PlannedMeal(
    val recipeId: String,
    val scale: Double = 1.0,
    val isLeftover: Boolean = false,
)
