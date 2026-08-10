package com.przemas230.dietaapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.przemas230.dietaapp.data.CookEntry
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.RecipeRepository
import com.przemas230.dietaapp.data.RecipeReview
import com.przemas230.dietaapp.logic.CATEGORIES
import com.przemas230.dietaapp.logic.CookHistoryOperations
import com.przemas230.dietaapp.logic.CustomRecipeOperations
import com.przemas230.dietaapp.logic.RecipeBrowsing
import com.przemas230.dietaapp.logic.RecipeRating
import com.przemas230.dietaapp.logic.RecipeRatingOperations
import com.przemas230.dietaapp.logic.RecipeReviewOperations
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
    private var builtInRecipes: List<Recipe> = emptyList()

    // FR-66: user-added recipes (index.html's state.myRecipes) -- folded
    // into the same visible/filtered list as the 229 built-in recipes via
    // recombineAllRecipes(), same as index.html's allRecipes().
    private val _myRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val myRecipes: StateFlow<List<Recipe>> = _myRecipes.asStateFlow()

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

    // FR-67: recipeId -> deliberate 1-5 star review + optional comment.
    private val _reviews = MutableStateFlow<Map<String, RecipeReview>>(emptyMap())
    val reviews: StateFlow<Map<String, RecipeReview>> = _reviews.asStateFlow()

    init {
        viewModelScope.launch {
            val loaded = withContext(Dispatchers.IO) { RecipeRepository.loadRecipes(application) }
            builtInRecipes = loaded
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

    /** FR-67: returns false (caller shows "Wybierz od 1 do 5 gwiazdek") if stars is out of range. */
    fun setReview(recipeId: String, stars: Int, comment: String?): Boolean {
        val result = RecipeReviewOperations.setReview(_reviews.value, recipeId, stars, comment, System.currentTimeMillis())
        if (result == null) return false
        _reviews.value = result
        return true
    }

    fun clearReview(recipeId: String) {
        _reviews.value = RecipeReviewOperations.clearReview(_reviews.value, recipeId)
    }

    /** FR-66: returns null (caller shows a validation message) if [input] doesn't pass CustomRecipeOperations.validate. */
    fun addCustomRecipe(input: CustomRecipeOperations.Input): CustomRecipeOperations.ValidationError? {
        val error = CustomRecipeOperations.validate(input)
        if (error != null) return error
        val recipe = CustomRecipeOperations.build(input, "custom-" + System.currentTimeMillis())!!
        _myRecipes.value = _myRecipes.value + recipe
        recompute()
        return null
    }

    /** FR-66: "🗑️ Usuń" on a custom recipe's card -- doesn't touch cook history/shopping entries already derived from it, same as index.html. */
    fun removeCustomRecipe(recipeId: String) {
        _myRecipes.value = _myRecipes.value.filterNot { it.id == recipeId }
        recompute()
    }

    /** FR-73: applies an incoming cloud snapshot wholesale (last-cloud-write-wins), replacing local state. */
    fun replaceCooked(cooked: Map<String, List<CookEntry>>) {
        _cooked.value = cooked
    }

    fun replaceRatings(ratings: Map<String, RecipeRating>) {
        _ratings.value = ratings
    }

    private fun recompute() {
        _visibleRecipes.value = RecipeBrowsing.visibleRecipes(
            builtInRecipes + _myRecipes.value,
            _selectedCategory.value,
            _searchTerm.value,
            glutenFree,
            lactoseFree,
        )
    }
}
