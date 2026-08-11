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
import com.przemas230.dietaapp.data.ActivityLogEntry
import com.przemas230.dietaapp.data.EatenEntry
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.ShoppingItem
import com.przemas230.dietaapp.data.Snack
import com.przemas230.dietaapp.data.WeightEntry
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
    val weights: List<WeightEntry>,
    val activityLog: List<ActivityLogEntry>,
    val communityRecipesEnabled: Boolean,
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
 * **Second bug fixed 2026-08-10** ("wypita woda tu swoje a tu swoje",
 * "historia też ma być wspólna"): `eaten` and `waterHistory` are per-DATE
 * maps in web (`state.eaten[date]`/`state.waterHistory[date]`) that
 * accumulate for MONTHS, but Android only ever tracks "today" locally (no
 * UI for past days). Pushing Android's narrow `{today: ...}` object as a
 * plain top-level field via `SetOptions.merge()` doesn't deep-merge nested
 * map VALUES -- it REPLACES the entire top-level field, so every other
 * date web had stored for that key was silently destroyed on the very
 * first Android push. Fixed by pushing those two fields through
 * `SetOptions.mergeFields("eaten.$today", "waterHistory.$today")` instead
 * of a blanket `SetOptions.merge()` -- Firestore's dotted mergeFields path
 * targets exactly that one nested key, leaving every other date's entry in
 * the document completely untouched. All other synced fields (profile,
 * pantry, shopping, weights, the activity log/"history"...) are simple
 * "current state" values, not accumulating per-date maps, so a plain
 * top-level replace for THOSE is correct and matches how web itself treats
 * them (none of them are in web's MAP_MERGE_KEYS either). On the PULL
 * side, `waterHistory` is merged (union) into Android's local map instead
 * of replacing it outright, so historical dates from web survive even
 * though Android only ever pushes today's.
 *
 * **Third change 2026-08-10**: pushing is now gated on having processed at
 * least one snapshot from Firestore first (`hasReceivedFirstSnapshot`),
 * mirroring `LocalPersistenceCoordinator`'s `initialLoadDone` guard. Before
 * this, a fresh sign-in could push this device's still-default/stale local
 * state (e.g. an unconfigured profile) to Firestore before the real
 * snapshot for that account had even arrived over the network, briefly (or
 * permanently, if nothing triggered another push afterwards) clobbering
 * the account's real data with Android's own defaults -- a plausible cause
 * of "parametry diety nie synchronizują się" reported by the user. The
 * gate clears on the FIRST listener callback regardless of outcome
 * (including "document doesn't exist yet", which correctly means "nothing
 * to lose, safe to push now").
 *
 * **Fourth change 2026-08-11** ("czy da się zrobić że ostatni zapis
 * wygrywa, na podstawie logu zmian" -- user explicitly asked for a
 * last-write-wins model informed by what actually changed, instead of the
 * full field-level 3-way merge + conflict dialog that FR-78 implements on
 * web): added [lastKnownFields], a per-field "last value we know Firestore
 * and this device agreed on" map. Every push now only lists the Firestore
 * fields that actually changed locally since [lastKnownFields] was last
 * updated -- NOT the full static field list -- in `SetOptions.mergeFields`.
 * This closes a real gap in the "last cloud write wins" model above: without
 * it, EVERY push re-writes ALL 19 fields using this device's current local
 * copy of each, including fields the user never touched here. If this
 * device was offline (or its listener simply hadn't caught up yet) while
 * ANOTHER device pushed a change to, say, `pantry`, and this device then
 * pushes an unrelated `profile` edit, the old code would silently include
 * this device's now-stale `pantry` in that same write, overwriting the
 * other device's real change in Firestore -- a genuine, silent data-loss
 * bug, not merely a display glitch. Restricting `mergeFields` to only the
 * fields this device actually changed makes that impossible: an unrelated
 * push simply never touches a field it didn't dirty. [lastKnownFields] is
 * updated optimistically right when a push is sent (same "before the
 * network round-trip" timing as [lastPushed], for the same reason) AND
 * whenever the pull side below successfully decodes a field from an
 * incoming snapshot, whether or not that value ends up applied locally --
 * either way, it is what this device now knows Firestore holds for that
 * field. Deliberately NOT a replacement for [PushedSnapshot]/[lastPushed]
 * or the stale-echo guards below -- those already correctly solve the
 * out-of-order-ack race documented above, and are left untouched here to
 * avoid risking a regression of that specific, previously-diagnosed bug.
 * There is still no conflict dialog: if two devices genuinely edit the
 * SAME field before either sees the other's change, whichever push reaches
 * the Firestore server last simply wins for that field, silently -- an
 * intentional, much simpler trade-off than FR-78's web behavior, since two
 * devices editing the exact same field within the same few seconds is rare
 * for a single-user app. Documented as such in PARITY.md rather than
 * chasing full parity with web's conflict UI.
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
    weightViewModel: WeightViewModel,
    activityLogViewModel: ActivityLogViewModel,
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
    val waterHistory by waterViewModel.history.collectAsState()
    val weightEntries by weightViewModel.entries.collectAsState()
    val activityLogEntries by activityLogViewModel.entries.collectAsState()
    val communityRecipesEnabled by recipeViewModel.communityRecipesEnabled.collectAsState()

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
    val currentWaterHistory = rememberUpdatedState(waterHistory)
    val currentWeightEntries = rememberUpdatedState(weightEntries)
    val currentActivityLog = rememberUpdatedState(activityLogEntries)
    val currentCommunityRecipesEnabled = rememberUpdatedState(communityRecipesEnabled)

    // Set right after CloudSyncCoordinator itself applies an incoming
    // remote snapshot, so that recomposition's own PUSH effect (below)
    // doesn't immediately echo the very data it just received back to
    // Firestore. Cleared again once consumed.
    var suppressNextPush by remember { mutableStateOf(false) }
    var lastPushed by remember { mutableStateOf<PushedSnapshot?>(null) }
    // See class doc, "Fourth change" -- per-field last-known-synced value,
    // used only to decide which Firestore fields a push actually needs to
    // touch. Keyed on uid so a different account starts with a clean slate
    // (everything looks "dirty" until the first real push for that account).
    var lastKnownFields by remember(uid) { mutableStateOf<Map<String, Any?>>(emptyMap()) }
    // See class doc, "Third change" -- blocks pushing until this device has
    // processed at least one snapshot for the signed-in account, so it never
    // races ahead and clobbers real cloud data with its own stale/default
    // local state. Keyed on uid so signing into a DIFFERENT account resets it.
    var hasReceivedFirstSnapshot by remember(uid) { mutableStateOf(false) }

    LaunchedEffect(
        uid, hasReceivedFirstSnapshot, profile, displayName, pantryItems, themeId, uiScale, swipeStyle,
        favIngredients, cooked, ratings, shoppingItems, weekPlan, eatenEntries, snacks, waterCount,
        waterHistory, weightEntries, activityLogEntries, communityRecipesEnabled,
    ) {
        if (uid == null || !hasReceivedFirstSnapshot) return@LaunchedEffect
        if (suppressNextPush) {
            suppressNextPush = false
            return@LaunchedEffect
        }
        delay(1500)
        val today = CloudSyncCodec.todayUtcDateString()

        // See class doc, "Fourth change" -- only fields that actually
        // changed since lastKnownFields was last updated get pushed, so a
        // push triggered by (say) a profile edit never re-writes this
        // device's possibly-stale local copy of unrelated fields like
        // pantry over a fresher value another device already wrote.
        val currentFieldValues: Map<String, Any?> = mapOf(
            "displayName" to displayName,
            "profile" to profile,
            "pantry" to pantryItems,
            "themeId" to themeId,
            "uiScale" to uiScale,
            "swipeStyle" to swipeStyle,
            "favIngredients" to favIngredients,
            "ratings" to ratings,
            "cooked" to cooked,
            "shoppingItems" to shoppingItems,
            "weekPlan" to weekPlan,
            "eaten" to (eatenEntries to snacks),
            "waterCount" to waterCount,
            "weights" to weightEntries,
            "activityLog" to activityLogEntries,
            "communityRecipesEnabled" to communityRecipesEnabled,
        )
        val dirtyKeys = currentFieldValues.filter { (key, value) -> value != lastKnownFields[key] }.keys
        if (dirtyKeys.isEmpty()) return@LaunchedEffect
        val fieldGroups: Map<String, List<String>> = mapOf(
            "displayName" to listOf("displayName"),
            "profile" to listOf("profile"),
            "pantry" to listOf("pantry"),
            "themeId" to listOf("theme"),
            "uiScale" to listOf("uiScale"),
            "swipeStyle" to listOf("swipeRatingStyle"),
            "favIngredients" to listOf("favIngredients"),
            "ratings" to listOf("recipeRating"),
            "cooked" to listOf("cooked"),
            "shoppingItems" to listOf("shopping"),
            "weekPlan" to listOf("planner", "plannerScale", "plannerLeftover"),
            "eaten" to listOf("eaten.$today"),
            "waterCount" to listOf("water", "waterHistory.$today"),
            "weights" to listOf("weights"),
            "activityLog" to listOf("history"),
            "communityRecipesEnabled" to listOf("communityRecipesEnabled"),
        )

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
            // Nested on purpose -- only "eaten.$today"/"waterHistory.$today"
            // are listed in mergeFields below, so this never touches any
            // OTHER date web has stored for either field (see class doc).
            "waterHistory" to mapOf(today to (waterHistory[today] ?: waterCount)),
            "weights" to CloudSyncCodec.encodeWeights(weightEntries),
            "history" to CloudSyncCodec.encodeActivityLog(activityLogEntries),
            "communityRecipesEnabled" to communityRecipesEnabled,
        )
        // Restricted to exactly the dirty keys -- Firestore's mergeFields
        // option only ever writes the paths listed here, so any OTHER
        // top-level key still present in `data` above is simply ignored.
        val mergeFields = dirtyKeys.flatMap { fieldGroups.getValue(it) }
        // Recorded BEFORE the network round-trip (not in a .then()-style
        // callback on ack) -- what matters is "what did we tell Firestore",
        // which is already fixed at this point, not when the server happens
        // to confirm it. Both lastPushed (whole-snapshot self-echo guard,
        // untouched by this change) and lastKnownFields (per-field dirty
        // baseline) are updated here for the same reason.
        lastPushed = PushedSnapshot(
            displayName, profile, pantryItems, themeId, uiScale, swipeStyle,
            favIngredients, ratings, cooked, shoppingItems, weekPlan, eatenEntries, snacks, waterCount,
            weightEntries, activityLogEntries, communityRecipesEnabled,
        )
        lastKnownFields = lastKnownFields + currentFieldValues.filterKeys { it in dirtyKeys }
        try {
            FirebaseFirestore.getInstance().collection("users").document(uid)
                .set(data, SetOptions.mergeFields(mergeFields))
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
                // Unblocks the push effect above regardless of what this
                // callback finds -- even "doc doesn't exist" is a definite
                // answer ("nothing to lose"), so it counts too.
                hasReceivedFirstSnapshot = true
                if (snapshot == null || !snapshot.exists()) return@addSnapshotListener
                // Fast path for the SDK's own optimistic local-cache echo of
                // a write we just made -- doesn't catch every self-echo (see
                // class doc), but cheap and correct as far as it goes.
                if (snapshot.metadata.hasPendingWrites()) return@addSnapshotListener
                val data = snapshot.data ?: return@addSnapshotListener
                val pushed = lastPushed

                var appliedAnything = false
                // See class doc, "Fourth change" -- every field successfully
                // decoded below (whether applied locally or recognized as a
                // stale/self echo) is Firestore's current known truth for
                // that key, so it updates the push side's dirty-tracking
                // baseline too. Collected here and applied once at the end
                // to avoid 16 separate recompositions per snapshot.
                val known = mutableMapOf<String, Any?>()
                (data["displayName"] as? String)?.let {
                    known["displayName"] = it
                    if (it != pushed?.displayName && it != currentDisplayName.value) {
                        profileViewModel.setDisplayName(it); appliedAnything = true
                    }
                }
                CloudSyncCodec.decodeProfile(data["profile"] as? Map<*, *>)?.let {
                    known["profile"] = it
                    if (it != pushed?.profile && it != currentProfile.value) {
                        profileViewModel.save(it); appliedAnything = true
                    }
                }
                CloudSyncCodec.decodePantry(data["pantry"] as? Map<*, *>)?.let {
                    known["pantry"] = it
                    if (it != pushed?.pantry && it != currentPantryItems.value) {
                        pantryViewModel.replaceAll(it); appliedAnything = true
                    }
                }
                (data["theme"] as? String)?.let {
                    known["themeId"] = it
                    if (it != pushed?.themeId && it != currentThemeId.value) {
                        themeViewModel.setTheme(it); appliedAnything = true
                    }
                }
                (data["uiScale"] as? Number)?.toDouble()?.let {
                    known["uiScale"] = it
                    if (it != pushed?.uiScale && it != currentUiScale.value) {
                        uiScaleViewModel.setScale(it); appliedAnything = true
                    }
                }
                (data["swipeRatingStyle"] as? String)?.let { raw ->
                    val style = SwipeRatingStyle.entries.find { it.name == raw }
                    if (style != null) {
                        known["swipeStyle"] = style
                        if (style != pushed?.swipeRatingStyle && style != currentSwipeStyle.value) {
                            swipeRatingStyleViewModel.setStyle(style); appliedAnything = true
                        }
                    }
                }
                CloudSyncCodec.decodeFavIngredients(data["favIngredients"] as? Map<*, *>)?.let {
                    known["favIngredients"] = it
                    if (it != pushed?.favIngredients && it != currentFavIngredients.value) {
                        favoriteIngredientsViewModel.replaceAll(it); appliedAnything = true
                    }
                }
                CloudSyncCodec.decodeRecipeRating(data["recipeRating"] as? Map<*, *>)?.let {
                    known["ratings"] = it
                    if (it != pushed?.recipeRating && it != currentRatings.value) {
                        recipeViewModel.replaceRatings(it); appliedAnything = true
                    }
                }
                CloudSyncCodec.decodeCooked(data["cooked"] as? Map<*, *>)?.let {
                    known["cooked"] = it
                    if (it != pushed?.cooked && it != currentCooked.value) {
                        recipeViewModel.replaceCooked(it); appliedAnything = true
                    }
                }
                CloudSyncCodec.decodeShopping(data["shopping"] as? Map<*, *>)?.let {
                    known["shoppingItems"] = it
                    if (it != pushed?.shopping && it != currentShoppingItems.value) {
                        shoppingViewModel.replaceAll(it); appliedAnything = true
                    }
                }
                CloudSyncCodec.decodeWeekPlan(
                    data["planner"] as? Map<*, *>,
                    data["plannerScale"] as? Map<*, *>,
                    data["plannerLeftover"] as? Map<*, *>,
                )?.let {
                    known["weekPlan"] = it
                    if (it != pushed?.weekPlan && it != currentWeekPlan.value) {
                        plannerViewModel.replaceAll(it); appliedAnything = true
                    }
                }
                CloudSyncCodec.decodeEaten(data["eaten"] as? Map<*, *>)?.let {
                    known["eaten"] = it.entries to it.snacks
                    val matchesPushed = pushed != null && it.entries == pushed.eatenEntries && it.snacks == pushed.snacks
                    val matchesCurrent = it.entries == currentEatenEntries.value && it.snacks == currentSnacks.value
                    if (!matchesPushed && !matchesCurrent) {
                        eatenViewModel.replaceAll(it.entries, it.snacks)
                        appliedAnything = true
                    }
                }
                CloudSyncCodec.decodeWater(data["water"] as? Map<*, *>)?.let {
                    known["waterCount"] = it
                    if (it != pushed?.waterCount && it != currentWaterCount.value) {
                        waterViewModel.setCount(it); appliedAnything = true
                    }
                }
                // waterHistory is a per-date map that accumulates for months
                // on web, but Android only ever PUSHES today's entry (see
                // class doc) -- so pulling must MERGE the remote map in
                // (keeping every date Android doesn't know about) rather
                // than replace, and must let the LOCAL entry win for any
                // overlapping date (in practice just "today") since that's
                // this device's own live, authoritative count, synced
                // separately via the plain "water" field above.
                CloudSyncCodec.decodeDateIntMap(data["waterHistory"] as? Map<*, *>)?.let { remote ->
                    val merged = remote + currentWaterHistory.value
                    if (merged != currentWaterHistory.value) {
                        waterViewModel.replaceHistory(merged); appliedAnything = true
                    }
                }
                CloudSyncCodec.decodeWeights(data["weights"] as? List<*>)?.let {
                    known["weights"] = it
                    if (it != pushed?.weights && it != currentWeightEntries.value) {
                        weightViewModel.replaceAll(it); appliedAnything = true
                    }
                }
                CloudSyncCodec.decodeActivityLog(data["history"] as? List<*>)?.let {
                    known["activityLog"] = it
                    if (it != pushed?.activityLog && it != currentActivityLog.value) {
                        activityLogViewModel.replaceAll(it); appliedAnything = true
                    }
                }
                (data["communityRecipesEnabled"] as? Boolean)?.let {
                    known["communityRecipesEnabled"] = it
                    if (it != pushed?.communityRecipesEnabled && it != currentCommunityRecipesEnabled.value) {
                        recipeViewModel.setCommunityRecipesEnabled(it); appliedAnything = true
                    }
                }
                if (known.isNotEmpty()) lastKnownFields = lastKnownFields + known
                if (appliedAnything) suppressNextPush = true
            }
        onDispose { registration.remove() }
    }
}
