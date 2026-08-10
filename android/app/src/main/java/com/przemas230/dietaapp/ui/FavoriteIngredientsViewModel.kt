package com.przemas230.dietaapp.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * FR-32: canon ingredient names starred by the user (index.html's
 * state.favIngredients) -- drives both the "have it" highlight on recipe
 * ingredient lists and DishIdeaGenerator's dish-idea suggestions. Local,
 * in-memory state, same as the rest of the app's data before step 6
 * (persistence).
 */
class FavoriteIngredientsViewModel : ViewModel() {
    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    fun toggle(canonName: String) {
        _favorites.value = if (canonName in _favorites.value) {
            _favorites.value - canonName
        } else {
            _favorites.value + canonName
        }
    }

    /** FR-73: applies an incoming cloud snapshot wholesale (last-cloud-write-wins), replacing local state. */
    fun replaceAll(favorites: Set<String>) {
        _favorites.value = favorites
    }
}
