package com.przemas230.dietaapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.przemas230.dietaapp.data.CookEntry
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.RecipeRepository
import com.przemas230.dietaapp.data.RecipeReview
import com.przemas230.dietaapp.logic.CATEGORIES
import com.przemas230.dietaapp.logic.CommunityRecipeOperations
import com.przemas230.dietaapp.logic.CookHistoryOperations
import com.przemas230.dietaapp.logic.CustomRecipeOperations
import com.przemas230.dietaapp.logic.RecipeBrowsing
import com.przemas230.dietaapp.logic.RecipePantryMatching
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

    // FR-2: recipeId -> starred as a favorite RECIPE (index.html's
    // state.favorites) -- a distinct concept from FavoriteIngredientsViewModel's
    // favIngredients (favorite INGREDIENTS). Drives the ⭐ star button on each
    // card's head and the "⭐ Ulubione" filter toggle.
    private val _favoriteRecipes = MutableStateFlow<Set<String>>(emptySet())
    val favoriteRecipes: StateFlow<Set<String>> = _favoriteRecipes.asStateFlow()

    fun toggleFavoriteRecipe(recipeId: String) {
        _favoriteRecipes.value = if (recipeId in _favoriteRecipes.value) {
            _favoriteRecipes.value - recipeId
        } else {
            _favoriteRecipes.value + recipeId
        }
    }

    /** Used by LocalPersistenceCoordinator on app startup to restore favorites saved on a previous run. */
    fun replaceFavoriteRecipes(favorites: Set<String>) {
        _favoriteRecipes.value = favorites
    }

    // FR-68/76: "🌍 Pokazuj przepisy dodane przez innych użytkowników" --
    // a plain persisted/synced preference (CommunityCoordinator only
    // subscribes to Firestore while this is true AND the user is signed
    // into a real account), independent of the recipes it gates.
    private val _communityRecipesEnabled = MutableStateFlow(false)
    val communityRecipesEnabled: StateFlow<Boolean> = _communityRecipesEnabled.asStateFlow()

    fun setCommunityRecipesEnabled(enabled: Boolean) {
        _communityRecipesEnabled.value = enabled
    }

    // FR-76: mirrors index.html's communityRecipesCache -- the current
    // "status == approved" set from Firestore's recipes collection, kept
    // empty whenever CommunityCoordinator isn't actively subscribed.
    private val _communityRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val communityRecipes: StateFlow<List<Recipe>> = _communityRecipes.asStateFlow()

    /** Called by CommunityCoordinator's Firestore listener; emptyList() when signed out, anonymous, or the toggle is off. */
    fun replaceCommunityRecipes(recipes: List<Recipe>) {
        _communityRecipes.value = recipes
        recompute()
    }

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

    fun removeCookEntry(recipeId: String, index: Int) {
        _cooked.value = CookHistoryOperations.removeEntry(_cooked.value, recipeId, index)
    }

    /** FR-103: has this dish already been logged as cooked today? Keeps the Planer's short right-swipe idempotent. */
    fun isCookedToday(recipeId: String): Boolean =
        CookHistoryOperations.cookedTodayIndex(_cooked.value, recipeId, System.currentTimeMillis()) >= 0

    /**
     * FR-103: exact inverse of [markCookedToday] for today's entry -- the
     * Planer's long left-swipe ("cofnij wszystko", the user's "pasuje też
     * móc cofnąć zrobienie bo mogło się przez przypadek kliknąć").
     * Returns false when there was nothing logged today; the pantry
     * restore is the caller's job, same split as [markCookedToday].
     */
    fun undoCookedToday(recipeId: String): Boolean {
        val index = CookHistoryOperations.cookedTodayIndex(_cooked.value, recipeId, System.currentTimeMillis())
        if (index < 0) return false
        _cooked.value = CookHistoryOperations.removeEntry(_cooked.value, recipeId, index)
        return true
    }

    /**
     * 2026-08-11, on explicit user request ("scal w jedno system gwiazdek,
     * oceny po zrobieniu dania oraz ocene i komentarz ktory mozna dodać pod
     * przepisem, to jedno i to samo"): swiping a card is now just a fast
     * shortcut into the SAME review store [setReview]/the review dialog/the
     * card badge all read and write -- right = 5★, left = 1★, keeping any
     * existing comment untouched. Replaces the old separate like/dislike
     * flag ([RecipeRatingOperations], no longer written to -- see
     * [replaceRatings] for the one-time migration of old data) and the old
     * per-cook-occurrence star ([CookHistoryOperations.setRating], removed).
     */
    fun setRatingQuick(recipeId: String, stars: Int) {
        setReview(recipeId, stars, _reviews.value[recipeId]?.comment)
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

    /**
     * FR-73/local persistence: applies an incoming snapshot of the old
     * like/dislike map. 2026-08-11 one-time migration: any entry here
     * without an existing review becomes one (5★ for a like, 1★ for a
     * dislike) so the old signal isn't just discarded now that rating is
     * unified into [_reviews] -- never overwrites an existing real review.
     * Runs on every call (both the initial local-disk load and every cloud
     * pull), which is fine -- it only ever fills gaps, never re-migrates
     * something already reviewed.
     */
    fun replaceRatings(ratings: Map<String, RecipeRating>) {
        _ratings.value = ratings
        val missing = ratings.filterKeys { it !in _reviews.value }
        if (missing.isNotEmpty()) {
            _reviews.value = _reviews.value + missing.mapValues { (_, rating) ->
                RecipeReview(if (rating == RecipeRating.LIKE) 5 else 1, null, System.currentTimeMillis())
            }
        }
    }

    fun replaceReviews(reviews: Map<String, RecipeReview>) {
        _reviews.value = reviews
    }

    /** Used by LocalStateStore on app startup to restore custom recipes saved on a previous run. */
    fun replaceMyRecipes(recipes: List<Recipe>) {
        _myRecipes.value = recipes
        recompute()
    }

    /**
     * 2026-08-11 (compact "🔍" search dropdown, user request): full
     * ingredient vocabulary across every recipe this device currently
     * knows about (builtin + own + community) -- deliberately NOT
     * `visibleRecipes` (already narrowed by category/search/dietary
     * filters), so the dropdown always offers the complete list regardless
     * of whatever filter happens to be active when it's opened. Not a
     * StateFlow -- computed once when the dropdown opens, not kept
     * continuously in sync, since the recipe set rarely changes mid-session.
     */
    fun uniqueIngredientNames(): List<String> =
        RecipePantryMatching.uniqueIngredientNames(builtInRecipes + _myRecipes.value + _communityRecipes.value)

    private fun recompute() {
        val community = CommunityRecipeOperations.dedupeCommunityRecipes(_myRecipes.value, _communityRecipes.value)
        _visibleRecipes.value = RecipeBrowsing.visibleRecipes(
            builtInRecipes + _myRecipes.value + community,
            _selectedCategory.value,
            _searchTerm.value,
            glutenFree,
            lactoseFree,
        )
    }
}
