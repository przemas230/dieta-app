package com.przemas230.dietaapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.RecipeRepository
import com.przemas230.dietaapp.logic.CATEGORIES
import com.przemas230.dietaapp.logic.RecipeBrowsing
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
