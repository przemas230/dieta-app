package com.przemas230.dietaapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.przemas230.dietaapp.data.LocalStateStore
import com.przemas230.dietaapp.logic.CloudSyncCodec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * On-device persistence for every local ViewModel -- runs regardless of
 * sign-in state (unlike CloudSyncCoordinator, which only pushes/pulls a
 * SUBSET of this once actually signed in). Loads once on app start and
 * applies whatever was saved last run, then debounce-saves (500ms, lighter
 * than the cloud's 1.5s since a local disk write has no network cost) on
 * every subsequent change to any of it.
 *
 * Reuses CloudSyncCodec's encode/decode functions for every field it also
 * knows how to sync to the cloud, plus three more this device tracks but
 * doesn't sync yet (myRecipes, recipeReviews, weights -- see PARITY.md's
 * FR-66/67/40 notes) -- one codec serving both destinations.
 *
 * Renders nothing; called once from DietaAppRoot alongside CloudSyncCoordinator.
 */
@Composable
fun LocalPersistenceCoordinator(
    profileViewModel: ProfileViewModel,
    pantryViewModel: PantryViewModel,
    themeViewModel: ThemeViewModel,
    uiScaleViewModel: UiScaleViewModel,
    swipeRatingStyleViewModel: SwipeRatingStyleViewModel,
    favoriteIngredientsViewModel: FavoriteIngredientsViewModel,
    recipeViewModel: RecipeViewModel,
    shoppingViewModel: ShoppingViewModel,
    plannerViewModel: PlannerViewModel,
    eatenViewModel: EatenViewModel,
    waterViewModel: WaterViewModel,
    weightViewModel: WeightViewModel,
) {
    val context = LocalContext.current

    val profile by profileViewModel.profile.collectAsState()
    val displayName by profileViewModel.displayName.collectAsState()
    val pantryItems by pantryViewModel.items.collectAsState()
    val themeId by themeViewModel.themeId.collectAsState()
    val uiScale by uiScaleViewModel.uiScale.collectAsState()
    val swipeStyle by swipeRatingStyleViewModel.style.collectAsState()
    val favIngredients by favoriteIngredientsViewModel.favorites.collectAsState()
    val cooked by recipeViewModel.cooked.collectAsState()
    val ratings by recipeViewModel.ratings.collectAsState()
    val reviews by recipeViewModel.reviews.collectAsState()
    val myRecipes by recipeViewModel.myRecipes.collectAsState()
    val shoppingItems by shoppingViewModel.items.collectAsState()
    val weekPlan by plannerViewModel.weekPlan.collectAsState()
    val eatenEntries by eatenViewModel.entries.collectAsState()
    val snacks by eatenViewModel.snacks.collectAsState()
    val waterCount by waterViewModel.count.collectAsState()
    val weightEntries by weightViewModel.entries.collectAsState()
    val kcalHistory by eatenViewModel.kcalHistory.collectAsState()
    val waterHistory by waterViewModel.history.collectAsState()

    // Guards the save effect below from firing (and clobbering the just-saved
    // file with the ViewModels' empty startup defaults) before the one-time
    // load has actually applied whatever was saved last run.
    var initialLoadDone by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val data = withContext(Dispatchers.IO) { LocalStateStore.load(context) }
        if (data != null) {
            (data["displayName"] as? String)?.let { profileViewModel.setDisplayName(it) }
            CloudSyncCodec.decodeProfile(data["profile"] as? Map<*, *>)?.let { profileViewModel.save(it) }
            CloudSyncCodec.decodePantry(data["pantry"] as? Map<*, *>)?.let { pantryViewModel.replaceAll(it) }
            (data["theme"] as? String)?.let { themeViewModel.setTheme(it) }
            (data["uiScale"] as? Number)?.toDouble()?.let { uiScaleViewModel.setScale(it) }
            (data["swipeRatingStyle"] as? String)?.let { raw ->
                SwipeRatingStyle.entries.find { it.name == raw }?.let { swipeRatingStyleViewModel.setStyle(it) }
            }
            CloudSyncCodec.decodeFavIngredients(data["favIngredients"] as? Map<*, *>)?.let { favoriteIngredientsViewModel.replaceAll(it) }
            CloudSyncCodec.decodeRecipeRating(data["recipeRating"] as? Map<*, *>)?.let { recipeViewModel.replaceRatings(it) }
            CloudSyncCodec.decodeCooked(data["cooked"] as? Map<*, *>)?.let { recipeViewModel.replaceCooked(it) }
            CloudSyncCodec.decodeReviews(data["recipeReviews"] as? Map<*, *>)?.let { recipeViewModel.replaceReviews(it) }
            CloudSyncCodec.decodeMyRecipes(data["myRecipes"] as? List<*>)?.let { recipeViewModel.replaceMyRecipes(it) }
            CloudSyncCodec.decodeShopping(data["shopping"] as? Map<*, *>)?.let { shoppingViewModel.replaceAll(it) }
            CloudSyncCodec.decodeWeekPlan(
                data["planner"] as? Map<*, *>,
                data["plannerScale"] as? Map<*, *>,
                data["plannerLeftover"] as? Map<*, *>,
            )?.let { plannerViewModel.replaceAll(it) }
            // History restored BEFORE replaceAll/setCount below, since those
            // internally re-derive TODAY's entry on top of whatever history
            // is already there -- restoring it first means past days survive.
            CloudSyncCodec.decodeDateIntMap(data["kcalHistory"] as? Map<*, *>)?.let { eatenViewModel.replaceHistory(it) }
            CloudSyncCodec.decodeDateIntMap(data["waterHistory"] as? Map<*, *>)?.let { waterViewModel.replaceHistory(it) }
            CloudSyncCodec.decodeEaten(data["eaten"] as? Map<*, *>)?.let { eatenViewModel.replaceAll(it.entries, it.snacks) }
            CloudSyncCodec.decodeWater(data["water"] as? Map<*, *>)?.let { waterViewModel.setCount(it) }
            CloudSyncCodec.decodeWeights(data["weights"] as? List<*>)?.let { weightViewModel.replaceAll(it) }
        }
        initialLoadDone = true
    }

    LaunchedEffect(
        initialLoadDone, profile, displayName, pantryItems, themeId, uiScale, swipeStyle,
        favIngredients, cooked, ratings, reviews, myRecipes, shoppingItems, weekPlan,
        eatenEntries, snacks, waterCount, weightEntries, kcalHistory, waterHistory,
    ) {
        if (!initialLoadDone) return@LaunchedEffect
        delay(500)
        val data = CloudSyncCodec.encodeAll(
            displayName = displayName,
            profile = profile,
            pantry = pantryItems,
            themeId = themeId,
            uiScale = uiScale,
            swipeRatingStyle = swipeStyle.name,
            favIngredients = favIngredients,
            recipeRating = ratings,
            cooked = cooked,
            shopping = shoppingItems,
            weekPlan = weekPlan,
            eatenEntries = eatenEntries,
            snacks = snacks,
            waterCount = waterCount,
        ) + mapOf(
            "recipeReviews" to CloudSyncCodec.encodeReviews(reviews),
            "myRecipes" to CloudSyncCodec.encodeMyRecipes(myRecipes),
            "weights" to CloudSyncCodec.encodeWeights(weightEntries),
            "kcalHistory" to CloudSyncCodec.encodeDateIntMap(kcalHistory),
            "waterHistory" to CloudSyncCodec.encodeDateIntMap(waterHistory),
        )
        withContext(Dispatchers.IO) { LocalStateStore.save(context, data) }
    }
}
