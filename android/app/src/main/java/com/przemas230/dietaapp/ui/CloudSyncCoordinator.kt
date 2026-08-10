package com.przemas230.dietaapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.przemas230.dietaapp.data.EatenEntry
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.ShoppingItem
import com.przemas230.dietaapp.data.Snack
import com.przemas230.dietaapp.logic.CloudSyncCodec
import com.przemas230.dietaapp.logic.RecipeRating
import com.przemas230.dietaapp.logic.WeekPlan
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

/**
 * Everything CloudSyncCoordinator last told Firestore, captured at the
 * moment a push is actually sent -- lets the snapshot listener recognize
 * "this incoming event is just confirming a write I already know about"
 * even when it arrives arbitrarily late (see the bug this fixes, below).
 */
private data class PushedSnapshot(
    val displayName: String,
    val profile: Profile,
    val pantry: Map<String, PantryItem>,
    val themeId: String,
    val uiScale: Double?,
    val swipeRatingStyle: SwipeRatingStyle,
    val favIngredients: Set<String>,
    val recipeRating: Map<String, RecipeRating>,
    val cooked: Map<String, List<com.przemas230.dietaapp.data.CookEntry>>,
    val shopping: Map<String, ShoppingItem>,
    val weekPlan: WeekPlan,
    val eatenEntries: Map<String, EatenEntry>,
    val snacks: List<Snack>,
    val waterCount: Int,
)

/**
 * FR-73: pushes the currently-syncable Android state to `users/{uid}` in
 * Firestore whenever it changes, debounced 1.5s like index.html's
 * scheduleCloudPush(), and applies incoming remote changes back onto the
 * same ViewModels the rest of the app already reads from. Only active while
 * signed in to a real (non-anonymous) account -- AuthViewModel's
 * Anonymous/Unavailable states leave this a no-op, matching FR-73's
 * "anonymous never syncs" criterion.
 *
 * Field shapes MUST match index.html's Firestore writes exactly (see
 * CloudSyncCodec's doc comment) -- pantry silently failed to round-trip
 * with the web app before 2026-08-10 despite this coordinator itself
 * working correctly, because the codec used Android-internal field names.
 *
 * **Bug fixed 2026-08-10** ("tylko pierwszy artykuł dodaje się, każdy
 * następny się zeruje" -- only the first pantry item added on Android
 * stuck, every next one immediately reverted): the 1.5s debounce is
 * invisible to the Firestore SDK's own `hasPendingWrites()` tracking --
 * during that debounce window (and the network round-trip after it), the
 * SDK genuinely has zero pending writes, so an ack for an OLDER push (e.g.
 * "just item 1") can arrive with `hasPendingWrites()==false` AFTER local
 * state has already moved on locally (e.g. "item 1 + item 2", item 2 not
 * pushed yet) -- the old code then saw remote({item1}) != local({item1,
 * item2}) and blindly replaced local with the stale remote value, wiping
 * item 2. `addSnapshotListener`'s callback closure ALSO captured
 * `pantryItems`/etc. only once (whenever DisposableEffect(uid) last ran,
 * i.e. sign-in time) instead of the live current value, compounding the
 * problem. Fixed two ways: (1) every collected value is wrapped in
 * `rememberUpdatedState` so the listener always compares against the
 * CURRENT value, not a value frozen at sign-in; (2) [PushedSnapshot] tracks
 * exactly what this device last told Firestore (captured right when a push
 * is sent) -- an incoming snapshot that matches the last-pushed value for a
 * field is recognized as a stale echo of our OWN write and skipped
 * entirely, regardless of `hasPendingWrites()` or arrival timing. This is
 * still FR-73's ORIGINAL "last cloud write wins" model, not FR-78's 3-way
 * merge (which sidesteps this whole class of bug structurally, at the cost
 * of considerably more complexity) -- ⬜ in Android, see PARITY.md.
 *
 * Renders nothing; called once from DietaAppRoot alongside the other
 * shared-ViewModel wiring, after every ViewModel it reads already exists.
 */
@Composable
fun CloudSyncCoordinator(
    authViewModel: AuthViewModel,
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
) {
    val authState by authViewModel.state.collectAsState()
    val profile by profileViewModel.profile.collectAsState()
    val displayName by profileViewModel.displayName.collectAsState()
    val pantryItems by pantryViewModel.items.collectAsState()
    val themeId by themeViewModel.themeId.collectAsState()
    val uiScale by uiScaleViewModel.uiScale.collectAsState()
    val swipeStyle by swipeRatingStyleViewModel.style.collectAsState()
    val favIngredients by favoriteIngredientsViewModel.favorites.collectAsState()
    val cooked by recipeViewModel.cooked.collectAsState()
    val ratings by recipeViewModel.ratings.collectAsState()
    val shoppingItems by shoppingViewModel.items.collectAsState()
    val weekPlan by plannerViewModel.weekPlan.collectAsState()
    val eatenEntries by eatenViewModel.entries.collectAsState()
    val snacks by eatenViewModel.snacks.collectAsState()
    val waterCount by waterViewModel.count.collectAsState()

    val uid = (authState as? AuthState.SignedIn)?.uid

    // The addSnapshotListener callback below is a long-lived closure that
    // outlives any single composition -- reading `profile`/`pantryItems`/etc.
    // directly would freeze them at whatever they were when DisposableEffect
    // last (re)ran (sign-in time), not their current value. rememberUpdatedState
    // gives a State whose `.value` is always current, safe to read from
    // inside a callback that isn't itself part of recomposition.
    val currentProfile = rememberUpdatedState(profile)
    val currentDisplayName = rememberUpdatedState(displayName)
    val currentPantryItems = rememberUpdatedState(pantryItems)
    val currentThemeId = rememberUpdatedState(themeId)
    val currentUiScale = rememberUpdatedState(uiScale)
    val currentSwipeStyle = rememberUpdatedState(swipeStyle)
    val currentFavIngredients = rememberUpdatedState(favIngredients)
    val currentCooked = rememberUpdatedState(cooked)
    val currentRatings = rememberUpdatedState(ratings)
    val currentShoppingItems = rememberUpdatedState(shoppingItems)
    val currentWeekPlan = rememberUpdatedState(weekPlan)
    val currentEatenEntries = rememberUpdatedState(eatenEntries)
    val currentSnacks = rememberUpdatedState(snacks)
    val currentWaterCount = rememberUpdatedState(waterCount)

    // Set right after CloudSyncCoordinator itself applies an incoming
    // remote snapshot, so that recomposition's own PUSH effect (below)
    // doesn't immediately echo the very data it just received back to
    // Firestore. Cleared again once consumed.
    var suppressNextPush by remember { mutableStateOf(false) }
    var lastPushed by remember { mutableStateOf<PushedSnapshot?>(null) }

    LaunchedEffect(
        uid, profile, displayName, pantryItems, themeId, uiScale, swipeStyle,
        favIngredients, cooked, ratings, shoppingItems, weekPlan, eatenEntries, snacks, waterCount,
    ) {
        if (uid == null) return@LaunchedEffect
        if (suppressNextPush) {
            suppressNextPush = false
            return@LaunchedEffect
        }
        delay(1500)
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
        )
        // Recorded BEFORE the network round-trip (not in a .then()-style
        // callback on ack) -- what matters is "what did we tell Firestore",
        // which is already fixed at this point, not when the server happens
        // to confirm it.
        lastPushed = PushedSnapshot(
            displayName, profile, pantryItems, themeId, uiScale, swipeStyle,
            favIngredients, ratings, cooked, shoppingItems, weekPlan, eatenEntries, snacks, waterCount,
        )
        try {
            FirebaseFirestore.getInstance().collection("users").document(uid)
                .set(data, SetOptions.merge())
                .await()
        } catch (e: Exception) {
            // Offline or transient failure -- Firestore's own offline cache
            // will retry the write once connectivity returns; the next
            // local change will also naturally re-attempt a push.
        }
    }

    DisposableEffect(uid) {
        if (uid == null) return@DisposableEffect onDispose {}
        val registration = FirebaseFirestore.getInstance().collection("users").document(uid)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null || !snapshot.exists()) return@addSnapshotListener
                // Fast path for the SDK's own optimistic local-cache echo of
                // a write we just made -- doesn't catch every self-echo (see
                // class doc), but cheap and correct as far as it goes.
                if (snapshot.metadata.hasPendingWrites()) return@addSnapshotListener
                val data = snapshot.data ?: return@addSnapshotListener
                val pushed = lastPushed

                var appliedAnything = false
                (data["displayName"] as? String)?.let {
                    if (it != pushed?.displayName && it != currentDisplayName.value) {
                        profileViewModel.setDisplayName(it); appliedAnything = true
                    }
                }
                CloudSyncCodec.decodeProfile(data["profile"] as? Map<*, *>)?.let {
                    if (it != pushed?.profile && it != currentProfile.value) {
                        profileViewModel.save(it); appliedAnything = true
                    }
                }
                CloudSyncCodec.decodePantry(data["pantry"] as? Map<*, *>)?.let {
                    if (it != pushed?.pantry && it != currentPantryItems.value) {
                        pantryViewModel.replaceAll(it); appliedAnything = true
                    }
                }
                (data["theme"] as? String)?.let {
                    if (it != pushed?.themeId && it != currentThemeId.value) {
                        themeViewModel.setTheme(it); appliedAnything = true
                    }
                }
                (data["uiScale"] as? Number)?.toDouble()?.let {
                    if (it != pushed?.uiScale && it != currentUiScale.value) {
                        uiScaleViewModel.setScale(it); appliedAnything = true
                    }
                }
                (data["swipeRatingStyle"] as? String)?.let { raw ->
                    val style = SwipeRatingStyle.entries.find { it.name == raw }
                    if (style != null && style != pushed?.swipeRatingStyle && style != currentSwipeStyle.value) {
                        swipeRatingStyleViewModel.setStyle(style); appliedAnything = true
                    }
                }
                CloudSyncCodec.decodeFavIngredients(data["favIngredients"] as? Map<*, *>)?.let {
                    if (it != pushed?.favIngredients && it != currentFavIngredients.value) {
                        favoriteIngredientsViewModel.replaceAll(it); appliedAnything = true
                    }
                }
                CloudSyncCodec.decodeRecipeRating(data["recipeRating"] as? Map<*, *>)?.let {
                    if (it != pushed?.recipeRating && it != currentRatings.value) {
                        recipeViewModel.replaceRatings(it); appliedAnything = true
                    }
                }
                CloudSyncCodec.decodeCooked(data["cooked"] as? Map<*, *>)?.let {
                    if (it != pushed?.cooked && it != currentCooked.value) {
                        recipeViewModel.replaceCooked(it); appliedAnything = true
                    }
                }
                CloudSyncCodec.decodeShopping(data["shopping"] as? Map<*, *>)?.let {
                    if (it != pushed?.shopping && it != currentShoppingItems.value) {
                        shoppingViewModel.replaceAll(it); appliedAnything = true
                    }
                }
                CloudSyncCodec.decodeWeekPlan(
                    data["planner"] as? Map<*, *>,
                    data["plannerScale"] as? Map<*, *>,
                    data["plannerLeftover"] as? Map<*, *>,
                )?.let {
                    if (it != pushed?.weekPlan && it != currentWeekPlan.value) {
                        plannerViewModel.replaceAll(it); appliedAnything = true
                    }
                }
                CloudSyncCodec.decodeEaten(data["eaten"] as? Map<*, *>)?.let {
                    val matchesPushed = pushed != null && it.entries == pushed.eatenEntries && it.snacks == pushed.snacks
                    val matchesCurrent = it.entries == currentEatenEntries.value && it.snacks == currentSnacks.value
                    if (!matchesPushed && !matchesCurrent) {
                        eatenViewModel.replaceAll(it.entries, it.snacks)
                        appliedAnything = true
                    }
                }
                CloudSyncCodec.decodeWater(data["water"] as? Map<*, *>)?.let {
                    if (it != pushed?.waterCount && it != currentWaterCount.value) {
                        waterViewModel.setCount(it); appliedAnything = true
                    }
                }
                if (appliedAnything) suppressNextPush = true
            }
        onDispose { registration.remove() }
    }
}
