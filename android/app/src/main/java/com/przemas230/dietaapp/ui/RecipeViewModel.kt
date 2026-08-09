package com.przemas230.dietaapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.przemas230.dietaapp.data.CookEntry
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.RecipeRepository
import com.przemas230.dietaapp.logic.CATEGORIES
import com.przemas230.dietaapp.logic.CookHistoryOperations
import com.przemas230.dietaapp.logic.RecipeBrowsing
import com.przemas230.dietaapp.logic.RecipeRating
import com.przemas230.dietaapp.logic.RecipeRatingOperations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Thin StateFlow/Android glue around RecipeBrowsing (in the :logic module,
 * see android/logic/ — unit-tested there in RecipeBrowsingTest). This class
 * itself can't be unit-tested here (needs AndroidViewModel/Application),
 * but the actual filtering rules it delegates to are genuinely covered.
 */
class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val allRecipes = MutableStateFlow<List<Recipe>>(emptyList())

    private val _selectedCategory = MutableStateFlow(CATEGORIES.first().id)
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchTerm = MutableStateFlow("")
    val searchTerm: StateFlow<String> = _searchTerm.asStateFlow()

    private var glutenFree = false
    private var lactoseFree = false

    private val _visibleRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val visibleRecipes: StateFlow<List<Recipe>> = _visibleRecipes.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // FR-15: recipeId -> history of "✅ Zrobione" entries (date + optional FR-17 star rating).
    private val _cooked = MutableStateFlow<Map<String, List<CookEntry>>>(emptyMap())
    val cooked: StateFlow<Map<String, List<CookEntry>>> = _cooked.asStateFlow()

    // FR-55/57: recipeId -> like/dislike, persistent (never removes the card from the list).
    private val _ratings = MutableStateFlow<Map<String, RecipeRating>>(emptyMap())
    val ratings: StateFlow<Map<String, RecipeRating>> = _ratings.asStateFlow()

    init {
        viewModelScope.launch {
            val loaded = withContext(Dispatchers.IO) { RecipeRepository.loadRecipes(application) }
            allRecipes.value = loaded
            _isLoading.value = false
            recompute()
        }
    }

    fun selectCategory(categoryId: String) {
        _selectedCategory.value = categoryId
        recompute()
    }

    fun setSearchTerm(term: String) {
        _searchTerm.value = term
        recompute()
    }

    /** FR-8: called from RecipeListScreen whenever the shared ProfileViewModel's profile changes. */
    fun setDietaryFilters(glutenFree: Boolean, lactoseFree: Boolean) {
        this.glutenFree = glutenFree
        this.lactoseFree = lactoseFree
        recompute()
    }

    /** FR-15: pantry subtraction is a separate call — see RecipeListScreen, which also owns the PantryViewModel. */
    fun markCookedToday(recipeId: String) {
        _cooked.value = CookHistoryOperations.addToday(_cooked.value, recipeId, System.currentTimeMillis())
    }

    fun setCookRating(recipeId: String, index: Int, rating: Int) {
        _cooked.value = CookHistoryOperations.setRating(_cooked.value, recipeId, index, rating)
    }

    fun removeCookEntry(recipeId: String, index: Int) {
        _cooked.value = CookHistoryOperations.removeEntry(_cooked.value, recipeId, index)
    }

    /** FR-55: swipe right (or tap the rating badge again to toggle) sets/clears like; swipe left sets dislike. */
    fun setRating(recipeId: String, rating: RecipeRating) {
        _ratings.value = RecipeRatingOperations.setRating(_ratings.value, recipeId, rating)
    }

    fun clearRating(recipeId: String) {
        _ratings.value = RecipeRatingOperations.clearRating(_ratings.value, recipeId)
    }

    private fun recompute() {
        _visibleRecipes.value = RecipeBrowsing.visibleRecipes(
            allRecipes.value,
            _selectedCategory.value,
            _searchTerm.value,
            glutenFree,
            lactoseFree,
        )
    }
}
