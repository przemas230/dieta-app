package com.przemas230.dietaapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class RecipeCategory(val id: String, val label: String, val emoji: String)

/**
 * Same 4-tab grouping as the web app's browse view (FR-74): "Śniadania" and
 * "II Śniadanie" share one tab here too, even though they're still distinct
 * `cat` values in the data (the Planer/meal-slot distinction only matters
 * once there's a planner screen — this is just the recipe browser).
 */
val CATEGORIES = listOf(
    RecipeCategory("sniadania", "Śniadania", "🍳"),
    RecipeCategory("obiady", "Obiady", "🍲"),
    RecipeCategory("kolacje", "Kolacje", "🌙"),
    RecipeCategory("deser", "Deser / Przekąska", "🍰"),
)

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val allRecipes = MutableStateFlow<List<Recipe>>(emptyList())

    private val _selectedCategory = MutableStateFlow(CATEGORIES.first().id)
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchTerm = MutableStateFlow("")
    val searchTerm: StateFlow<String> = _searchTerm.asStateFlow()

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

    private fun recompute() {
        val category = _selectedCategory.value
        val matchesCategory: (Recipe) -> Boolean = { recipe ->
            if (category == "sniadania") recipe.cat == "sniadania" || recipe.cat == "drugie"
            else recipe.cat == category
        }
        val term = _searchTerm.value.trim().lowercase()
        _visibleRecipes.value = allRecipes.value.filter { recipe ->
            matchesCategory(recipe) &&
                (term.isEmpty() ||
                    recipe.name.lowercase().contains(term) ||
                    recipe.ingredients.any { it.lowercase().contains(term) })
        }
    }
}
