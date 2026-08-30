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
 * knows how to sync to the cloud, plus two more this device tracks but
 * doesn't sync yet (myRecipes, recipeReviews -- see PARITY.md's FR-66/67
 * notes) -- one codec serving both destinations.
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
    activityLogViewModel: ActivityLogViewModel,
    remainingKcalFillViewModel: RemainingKcalFillViewModel,
    fastingViewModel: FastingViewModel,
) {
    val context = LocalContext.current

    val profile by profileViewModel.profile.collectAsState()
    val displayName by profileViewModel.displayName.collectAsState()
    val pantryItems by pantryViewModel.items.collectAsState()
    // FR-102: products deleted from the Spiżarnia for good (PantryStore keeps
    // its own copy too -- this one keeps the single local-state snapshot
    // complete, same as every other field here).
    val pantryHidden by pantryViewModel.hidden.collectAsState()
    val themeId by themeViewModel.themeId.collectAsState()
    val uiScale by uiScaleViewModel.uiScale.collectAsState()
    val swipeStyle by swipeRatingStyleViewModel.style.collectAsState()
    val favIngredients by favoriteIngredientsViewModel.favorites.collectAsState()
    val cooked by recipeViewModel.cooked.collectAsState()
    val ratings by recipeViewModel.ratings.collectAsState()
    val reviews by recipeViewModel.reviews.collectAsState()
    val myRecipes by recipeViewModel.myRecipes.collectAsState()
    val favoriteRecipes by recipeViewModel.favoriteRecipes.collectAsState()
    val communityRecipesEnabled by recipeViewModel.communityRecipesEnabled.collectAsState()
    val shoppingItems by shoppingViewModel.items.collectAsState()
    val weekPlan by plannerViewModel.weekPlan.collectAsState()
    val weekTemplate by plannerViewModel.weekTemplate.collectAsState()
    val eatenDays by eatenViewModel.days.collectAsState()
    val waterCount by waterViewModel.count.collectAsState()
    val weightEntries by weightViewModel.entries.collectAsState()
    val waterHistory by waterViewModel.history.collectAsState()
    val activityLogEntries by activityLogViewModel.entries.collectAsState()
    val remainingKcalFillEnabled by remainingKcalFillViewModel.enabled.collectAsState()
    val fastingEnabled by fastingViewModel.enabled.collectAsState()
    val fastingWindowStart by fastingViewModel.windowStart.collectAsState()
    val fastingWindowEnd by fastingViewModel.windowEnd.collectAsState()

    // Guards the save effect below from firing (and clobbering the just-saved
    // file with the ViewModels' empty startup defaults) before the one-time
    // load has actually applied whatever was saved last run.
    var initialLoadDone by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val data = withContext(Dispatchers.IO) { LocalStateStore.load(context) }
        if (data != null) {
            applyLocalSnapshot(
                data = data,
                profileViewModel = profileViewModel,
                pantryViewModel = pantryViewModel,
                themeViewModel = themeViewModel,
                uiScaleViewModel = uiScaleViewModel,
                swipeRatingStyleViewModel = swipeRatingStyleViewModel,
                favoriteIngredientsViewModel = favoriteIngredientsViewModel,
                recipeViewModel = recipeViewModel,
                shoppingViewModel = shoppingViewModel,
                plannerViewModel = plannerViewModel,
                eatenViewModel = eatenViewModel,
                waterViewModel = waterViewModel,
                weightViewModel = weightViewModel,
                activityLogViewModel = activityLogViewModel,
                remainingKcalFillViewModel = remainingKcalFillViewModel,
                fastingViewModel = fastingViewModel,
            )
        }
        initialLoadDone = true
    }

    LaunchedEffect(
        initialLoadDone, profile, displayName, pantryItems, pantryHidden, themeId, uiScale, swipeStyle,
        favIngredients, cooked, ratings, reviews, myRecipes, favoriteRecipes, shoppingItems, weekPlan,
        weekTemplate, eatenDays, waterCount, weightEntries, waterHistory, activityLogEntries,
        communityRecipesEnabled, remainingKcalFillEnabled,
        fastingEnabled, fastingWindowStart, fastingWindowEnd,
    ) {
        if (!initialLoadDone) return@LaunchedEffect
        delay(500)
        val data = CloudSyncCodec.encodeAll(
            displayName = displayName,
            profile = profile,
            pantry = pantryItems,
            pantryHidden = pantryHidden,
            themeId = themeId,
            uiScale = uiScale,
            swipeRatingStyle = swipeStyle.name,
            favIngredients = favIngredients,
            recipeRating = ratings,
            cooked = cooked,
            shopping = shoppingItems,
            weekPlan = weekPlan,
            eatenDays = eatenDays,
            waterCount = waterCount,
        ) + mapOf(
            "recipeReviews" to CloudSyncCodec.encodeReviews(reviews),
            "myRecipes" to CloudSyncCodec.encodeMyRecipes(myRecipes),
            // FR-2: recipe-level favorites (state.favorites in index.html) --
            // reuses encodeFavIngredients/decodeFavIngredients above since
            // the shape (Set<String> -> {name: true}) is identical to
            // favIngredients, just a different top-level key/meaning.
            "favorites" to CloudSyncCodec.encodeFavIngredients(favoriteRecipes),
            "weights" to CloudSyncCodec.encodeWeights(weightEntries),
            "waterHistory" to CloudSyncCodec.encodeDateIntMap(waterHistory),
            "activityLog" to CloudSyncCodec.encodeActivityLog(activityLogEntries),
            "communityRecipesEnabled" to communityRecipesEnabled,
            "remainingKcalFillEnabled" to remainingKcalFillEnabled,
            "fastingEnabled" to fastingEnabled,
            "fastingWindowStart" to fastingWindowStart,
            "fastingWindowEnd" to fastingWindowEnd,
        ) + (weekTemplate?.let { template ->
            mapOf(
                "weekTemplatePlanner" to CloudSyncCodec.encodePlanner(template),
                "weekTemplatePlannerScale" to CloudSyncCodec.encodePlannerScale(template),
                "weekTemplatePlannerLeftover" to CloudSyncCodec.encodePlannerLeftover(template),
            )
        } ?: emptyMap())
        withContext(Dispatchers.IO) { LocalStateStore.save(context, data) }
    }
}

/**
 * FR-98 (ported to Android 2026-08-29): applies one saved snapshot onto
 * every ViewModel.
 *
 * Extracted from LocalPersistenceCoordinator's own startup effect so the
 * backup IMPORT can reuse the exact same path. Two implementations of
 * "restore everything from a map" would be two places to forget a field the
 * next time one is added -- and a backup that silently skips a field is
 * worse than no backup, because it looks like it worked.
 */
internal fun applyLocalSnapshot(
    data: Map<String, Any?>,
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
    activityLogViewModel: ActivityLogViewModel,
    remainingKcalFillViewModel: RemainingKcalFillViewModel,
    fastingViewModel: FastingViewModel,
) {
            (data["displayName"] as? String)?.let { profileViewModel.setDisplayName(it) }
            CloudSyncCodec.decodeProfile(data["profile"] as? Map<*, *>)?.let { profileViewModel.save(it) }
            CloudSyncCodec.decodePantry(data["pantry"] as? Map<*, *>)?.let { pantryViewModel.replaceAll(it) }
            CloudSyncCodec.decodePantryHidden(data["pantryHidden"] as? Map<*, *>)?.let { pantryViewModel.replaceHidden(it) }
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
            CloudSyncCodec.decodeFavIngredients(data["favorites"] as? Map<*, *>)?.let { recipeViewModel.replaceFavoriteRecipes(it) }
            (data["communityRecipesEnabled"] as? Boolean)?.let { recipeViewModel.setCommunityRecipesEnabled(it) }
            (data["remainingKcalFillEnabled"] as? Boolean)?.let { remainingKcalFillViewModel.setEnabled(it) }
            (data["fastingEnabled"] as? Boolean)?.let { fastingViewModel.setEnabled(it) }
            (data["fastingWindowStart"] as? Number)?.toInt()?.let { fastingViewModel.setWindowStart(it) }
            (data["fastingWindowEnd"] as? Number)?.toInt()?.let { fastingViewModel.setWindowEnd(it) }
            CloudSyncCodec.decodeShopping(data["shopping"] as? Map<*, *>)?.let { shoppingViewModel.replaceAll(it) }
            CloudSyncCodec.decodeWeekPlan(
                data["planner"] as? Map<*, *>,
                data["plannerScale"] as? Map<*, *>,
                data["plannerLeftover"] as? Map<*, *>,
            )?.let { plannerViewModel.replaceAll(it) }
            // FR-115: single saved week-template slot, null if never saved --
            // reuses the same planner codec functions under different keys.
            plannerViewModel.replaceWeekTemplate(
                CloudSyncCodec.decodeWeekPlan(
                    data["weekTemplatePlanner"] as? Map<*, *>,
                    data["weekTemplatePlannerScale"] as? Map<*, *>,
                    data["weekTemplatePlannerLeftover"] as? Map<*, *>,
                )
            )
            CloudSyncCodec.decodeDateIntMap(data["waterHistory"] as? Map<*, *>)?.let { waterViewModel.replaceHistory(it) }
            // FR-83: eatenViewModel now derives kcalHistory straight from the
            // full per-date map it restores here, so there's no separate
            // "kcalHistory" field left to load first.
            CloudSyncCodec.decodeEaten(data["eaten"] as? Map<*, *>)?.let { eatenViewModel.replaceAll(it) }
            CloudSyncCodec.decodeWater(data["water"] as? Map<*, *>)?.let { waterViewModel.setCount(it) }
            CloudSyncCodec.decodeWeights(data["weights"] as? List<*>)?.let { weightViewModel.replaceAll(it) }
            CloudSyncCodec.decodeActivityLog(data["activityLog"] as? List<*>)?.let { activityLogViewModel.replaceAll(it) }
}
