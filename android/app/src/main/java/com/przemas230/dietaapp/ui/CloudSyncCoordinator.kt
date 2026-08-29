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
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.przemas230.dietaapp.data.ActivityLogEntry
import com.przemas230.dietaapp.data.CloudSyncBaselineStore
import com.przemas230.dietaapp.data.EatenDay
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.ShoppingItem
import com.przemas230.dietaapp.data.WeightEntry
import com.przemas230.dietaapp.logic.CloudSyncCodec
import com.przemas230.dietaapp.logic.RecipeRating
import com.przemas230.dietaapp.logic.WeekPlan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Turns [lastKnownFields] (semantic key -> decoded domain value) into a
 * JSON-safe `Map<String, Any?>` for [CloudSyncBaselineStore], reusing the
 * exact same per-field encoders [CloudSyncCodec] already uses for pushing
 * to Firestore -- one encoding, two destinations, so there's no separate
 * format to keep in sync. Unchecked casts are safe here: every value ever
 * stored in `lastKnownFields` originates either from this same file's own
 * strongly-typed ViewModel state or from [decodeBaselineField]'s own
 * (correctly-typed) output.
 */
@Suppress("UNCHECKED_CAST")
private fun encodeBaselineField(key: String, value: Any?): Any? = when (key) {
    "displayName" -> value as? String
    "profile" -> (value as? Profile)?.let(CloudSyncCodec::encodeProfile)
    "pantry" -> (value as? Map<String, PantryItem>)?.let(CloudSyncCodec::encodePantry)
    "pantryHidden" -> (value as? Set<String>)?.let(CloudSyncCodec::encodePantryHidden)
    "themeId" -> value as? String
    "uiScale" -> value as? Double
    "swipeStyle" -> (value as? SwipeRatingStyle)?.name
    "favIngredients" -> (value as? Set<String>)?.let(CloudSyncCodec::encodeFavIngredients)
    "favoriteRecipes" -> (value as? Set<String>)?.let(CloudSyncCodec::encodeFavIngredients)
    "ratings" -> (value as? Map<String, RecipeRating>)?.let(CloudSyncCodec::encodeRecipeRating)
    "cooked" -> (value as? Map<String, List<com.przemas230.dietaapp.data.CookEntry>>)?.let(CloudSyncCodec::encodeCooked)
    "shoppingItems" -> (value as? Map<String, ShoppingItem>)?.let(CloudSyncCodec::encodeShopping)
    "weekPlan" -> (value as? WeekPlan)?.let {
        mapOf(
            "planner" to CloudSyncCodec.encodePlanner(it),
            "plannerScale" to CloudSyncCodec.encodePlannerScale(it),
            "plannerLeftover" to CloudSyncCodec.encodePlannerLeftover(it),
        )
    }
    "eaten" -> (value as? Map<String, EatenDay>)?.let(CloudSyncCodec::encodeEaten)
    "waterCount" -> (value as? Int)?.let(CloudSyncCodec::encodeWater)
    "weights" -> (value as? List<WeightEntry>)?.let(CloudSyncCodec::encodeWeights)
    "activityLog" -> (value as? List<ActivityLogEntry>)?.let(CloudSyncCodec::encodeActivityLog)
    "communityRecipesEnabled" -> value as? Boolean
    else -> null
}

/** The decode side of [encodeBaselineField] -- turns the JSON-safe form back into the same domain values `lastKnownFields` holds during a session, so comparisons after a restart use the same types as comparisons within one. */
private fun decodeBaselineField(key: String, raw: Any?): Any? = when (key) {
    "displayName" -> raw as? String
    "profile" -> CloudSyncCodec.decodeProfile(raw as? Map<*, *>)
    "pantry" -> CloudSyncCodec.decodePantry(raw as? Map<*, *>)
    "pantryHidden" -> CloudSyncCodec.decodePantryHidden(raw as? Map<*, *>)
    "themeId" -> raw as? String
    "uiScale" -> (raw as? Number)?.toDouble()
    "swipeStyle" -> (raw as? String)?.let { name -> SwipeRatingStyle.entries.find { it.name == name } }
    "favIngredients" -> CloudSyncCodec.decodeFavIngredients(raw as? Map<*, *>)
    "favoriteRecipes" -> CloudSyncCodec.decodeFavIngredients(raw as? Map<*, *>)
    "ratings" -> CloudSyncCodec.decodeRecipeRating(raw as? Map<*, *>)
    "cooked" -> CloudSyncCodec.decodeCooked(raw as? Map<*, *>)
    "shoppingItems" -> CloudSyncCodec.decodeShopping(raw as? Map<*, *>)
    "weekPlan" -> {
        val m = raw as? Map<*, *>
        CloudSyncCodec.decodeWeekPlan(m?.get("planner") as? Map<*, *>, m?.get("plannerScale") as? Map<*, *>, m?.get("plannerLeftover") as? Map<*, *>)
    }
    "eaten" -> CloudSyncCodec.decodeEaten(raw as? Map<*, *>)
    "waterCount" -> CloudSyncCodec.decodeWater(raw as? Map<*, *>)
    "weights" -> CloudSyncCodec.decodeWeights(raw as? List<*>)
    "activityLog" -> CloudSyncCodec.decodeActivityLog(raw as? List<*>)
    "communityRecipesEnabled" -> raw as? Boolean
    else -> null
}

private fun encodeBaselineFields(fields: Map<String, Any?>): Map<String, Any?> =
    fields.mapValues { (key, value) -> encodeBaselineField(key, value) }

private fun decodeBaselineFields(raw: Map<String, Any?>): Map<String, Any?> =
    raw.mapNotNull { (key, value) -> decodeBaselineField(key, value)?.let { key to it } }.toMap()

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
 * następny się zeruje"): the 1.5s debounce is invisible to the Firestore
 * SDK's own `hasPendingWrites()` tracking, so a stale ack for an OLDER push
 * could arrive after local state had already moved on, wiping the newer
 * local change. Fixed by tracking, per field, exactly what this device last
 * told Firestore -- see [lastKnownFields] below, which now carries that
 * responsibility (the original one-shot `PushedSnapshot`/`lastPushed` this
 * comment used to describe was folded into it on 2026-08-11, see "Fifth
 * change").
 *
 * **Second bug fixed 2026-08-10** ("wypita woda tu swoje a tu swoje",
 * "historia też ma być wspólna"): `eaten` and `waterHistory` are per-DATE
 * maps in web (`state.eaten[date]`/`state.waterHistory[date]`) that
 * accumulate for MONTHS, but Android only ever tracked "today" locally (no
 * UI for past days) at the time this was fixed. Pushing Android's narrow
 * `{today: ...}` object as a plain top-level field via `SetOptions.merge()`
 * doesn't deep-merge nested map VALUES -- it REPLACES the entire top-level
 * field, so every other date web had stored for that key was silently
 * destroyed on the very first Android push. Fixed by pushing those two
 * fields through `SetOptions.mergeFields("eaten.$today",
 * "waterHistory.$today")` instead of a blanket `SetOptions.merge()` --
 * Firestore's dotted mergeFields path targets exactly that one nested key,
 * leaving every other date's entry in the document completely untouched.
 * **FR-83 (2026-08-23) superseded this for `eaten` specifically**: Android
 * now tracks full per-date history locally (see EatenViewModel), so `eaten`
 * pushes/pulls as an ordinary whole-field replace like `pantry`/`profile`
 * below -- the narrow "eaten.$today" nested path and the pull-side
 * union-merge described above no longer apply to it. `waterHistory` is
 * untouched by FR-83 (water tracking itself is still today-only) and still
 * needs both: pushed via `SetOptions.mergeFields("waterHistory.$today")`,
 * and on the PULL side merged (union) into Android's local map instead of
 * replacing it outright, so historical dates from web survive even though
 * Android only ever pushes today's.
 *
 * **Third change 2026-08-10**: pushing is gated on having processed at
 * least one snapshot from Firestore first (`hasReceivedFirstSnapshot`).
 * Before this, a fresh sign-in could push this device's still-default/stale
 * local state to Firestore before the real snapshot for that account had
 * even arrived, clobbering the account's real data with Android's own
 * defaults. The gate clears on the FIRST listener callback regardless of
 * outcome (including "document doesn't exist yet", which correctly means
 * "nothing to lose, safe to push now").
 *
 * **Fourth change 2026-08-11** ("czy da się zrobić że ostatni zapis
 * wygrywa, na podstawie logu zmian"): added [lastKnownFields], a per-field
 * "last value we know Firestore and this device agreed on" map. Every push
 * now only lists the Firestore fields that actually changed locally since
 * [lastKnownFields] was last updated -- NOT the full static field list --
 * in `SetOptions.mergeFields`. Without this, EVERY push re-wrote ALL 19
 * fields using this device's current local copy of each, including fields
 * the user never touched here; if this device was offline while ANOTHER
 * device pushed a change to, say, `pantry`, and this device then pushed an
 * unrelated `profile` edit, the old code silently included this device's
 * now-stale `pantry` in that same write, overwriting the other device's
 * real change.
 *
 * **Fifth change 2026-08-11** (real data-loss bug: "w kotlin nie
 * zapamiętuje mi w ustawieniach że jestem mężczyzną, przełącza mi na
 * kobietę... cel zmieniałem a teraz widzę znów z defaultu wstawił"):
 * [lastKnownFields] was pure in-memory Compose state, reset to empty on
 * every app restart. Sequence that lost data: user edits their profile,
 * the app is closed before the 1.5s debounced push fires (or before its
 * network round-trip completes). On next launch, the edit is correctly
 * restored locally from `LocalPersistenceCoordinator` -- but this
 * coordinator starts with an EMPTY `lastKnownFields`, so the FIRST incoming
 * Firestore snapshot (still holding the OLD, pre-edit value) looked
 * "different from what we last knew" and got silently applied over the
 * fresh local edit. This is what the old `PushedSnapshot`/`lastPushed`
 * self-echo guard was ALSO meant to catch, but it was equally in-memory-only
 * and so equally blind across a restart. Fixed by persisting
 * [lastKnownFields] to disk ([CloudSyncBaselineStore], scoped per uid) and
 * loading it BEFORE the Firestore listener is even allowed to attach
 * ([baselineLoaded] gates the `DisposableEffect` below) -- so even the
 * FIRST snapshot after a cold start can correctly tell "this matches what I
 * already knew" (stale echo, ignore) apart from "this is genuinely
 * different from what I last confirmed" (real remote change OR a brand new
 * device/sign-in with no baseline at all, both of which should apply).
 * `PushedSnapshot`/`lastPushed`/`suppressNextPush` are removed entirely --
 * [lastKnownFields] alone now does their job, more robustly (it survives
 * restarts, they didn't), so keeping them around was redundant risk, not
 * safety margin.
 *
 * **Sixth change 2026-08-11** ("za każdym razem wraca mi jakaś defaultowa
 * dieta, mimo że jestem zalogowany do konta Google"): the persisted
 * baseline from the Fifth change could itself go stale relative to the
 * ViewModels/local state it's meant to describe, in two ways, both now
 * fixed: (a) [CloudSyncBaselineStore] had no way to be invalidated, so
 * MainActivity's "Wyczyść dane lokalne" (FR-79) reset every ViewModel to
 * fresh-install defaults but left the baseline holding the account's real
 * (pre-clear) data -- the next sign-in to the SAME account then saw the
 * incoming Firestore snapshot as "already known" and never re-applied it,
 * permanently; fixed by clearing the baseline there too (see
 * [CloudSyncBaselineStore]'s own doc comment). (b) the baseline save below
 * had no debounce while [LocalPersistenceCoordinator]'s save of the exact
 * same pulled value is debounced 500ms, so a process death in that window
 * right after a pull could leave the baseline matching Firestore's real
 * data while the on-disk local state still held the pre-pull value --
 * fixed by delaying the baseline save to lag behind, instead of race
 * ahead of, the local-state write.
 *
 * There is still no conflict dialog: if two devices genuinely edit the SAME
 * field before either sees the other's change, whichever push reaches the
 * Firestore server last simply wins for that field, silently -- an
 * intentional, much simpler trade-off than FR-78's web behavior, since two
 * devices editing the exact same field within the same few seconds is rare
 * for a single-user app. Documented as such in PARITY.md rather than
 * chasing full parity with web's conflict UI.
 *
 * **Seventh change 2026-08-24** (bug: recipe favorites -- the ⭐/❤️ star
 * toggle on each recipe card, `RecipeViewModel.favoriteRecipes` -- silently
 * never synced between devices, even though FR-73's own field list has
 * always named it: "ulubione przepisy i ulubione składniki (`favorites`,
 * `favIngredients`)"). It WAS ported and correctly persisted to local disk
 * ([LocalPersistenceCoordinator] already round-trips it under the web field
 * name `favorites`, reusing [CloudSyncCodec.encodeFavIngredients]/
 * `decodeFavIngredients` since the shape is identical), but this coordinator
 * -- the only thing that talks to Firestore -- never read or wrote it, so a
 * recipe favorited on one device stayed invisible on every other device
 * forever, no matter how many times anything else synced. Fixed by adding
 * `favoriteRecipes` to [lastKnownFields]'s encode/decode, the push side's
 * dirty-field tracking, and the pull side's decode -- same pattern as every
 * other field here, reusing the same codec functions
 * [LocalPersistenceCoordinator] already uses for the local copy.
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
    val context = LocalContext.current
    val authState by authViewModel.state.collectAsState()
    val profile by profileViewModel.profile.collectAsState()
    val displayName by profileViewModel.displayName.collectAsState()
    val pantryItems by pantryViewModel.items.collectAsState()
    // FR-102: products deleted from the Spiżarnia for good -- synced like
    // any other field so a product removed on the phone stays removed in
    // the browser (and vice versa) instead of silently coming back.
    val pantryHidden by pantryViewModel.hidden.collectAsState()
    val themeId by themeViewModel.themeId.collectAsState()
    val uiScale by uiScaleViewModel.uiScale.collectAsState()
    val swipeStyle by swipeRatingStyleViewModel.style.collectAsState()
    val favIngredients by favoriteIngredientsViewModel.favorites.collectAsState()
    val favoriteRecipes by recipeViewModel.favoriteRecipes.collectAsState()
    val cooked by recipeViewModel.cooked.collectAsState()
    val ratings by recipeViewModel.ratings.collectAsState()
    val shoppingItems by shoppingViewModel.items.collectAsState()
    val weekPlan by plannerViewModel.weekPlan.collectAsState()
    val eatenDays by eatenViewModel.days.collectAsState()
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
    val currentPantryHidden = rememberUpdatedState(pantryHidden)
    val currentThemeId = rememberUpdatedState(themeId)
    val currentUiScale = rememberUpdatedState(uiScale)
    val currentSwipeStyle = rememberUpdatedState(swipeStyle)
    val currentFavIngredients = rememberUpdatedState(favIngredients)
    val currentFavoriteRecipes = rememberUpdatedState(favoriteRecipes)
    val currentCooked = rememberUpdatedState(cooked)
    val currentRatings = rememberUpdatedState(ratings)
    val currentShoppingItems = rememberUpdatedState(shoppingItems)
    val currentWeekPlan = rememberUpdatedState(weekPlan)
    val currentEatenDays = rememberUpdatedState(eatenDays)
    val currentWaterCount = rememberUpdatedState(waterCount)
    val currentWaterHistory = rememberUpdatedState(waterHistory)
    val currentWeightEntries = rememberUpdatedState(weightEntries)
    val currentActivityLog = rememberUpdatedState(activityLogEntries)
    val currentCommunityRecipesEnabled = rememberUpdatedState(communityRecipesEnabled)

    // See class doc, "Fourth"/"Fifth change" -- per-field "last value this
    // device and Firestore agreed on", used both to decide which Firestore
    // fields a push actually needs to touch AND to decide whether an
    // incoming snapshot is genuinely new or already-known. Keyed on uid so
    // a different account starts with a clean slate. Persisted to disk (see
    // the LaunchedEffect below) so it survives app restarts -- an in-memory
    // `remember` alone is what caused the "Fifth change" data-loss bug.
    var lastKnownFields by remember(uid) { mutableStateOf<Map<String, Any?>>(emptyMap()) }
    // Blocks the Firestore listener from attaching (and thus the push
    // effect, gated on hasReceivedFirstSnapshot below, from firing) until
    // the on-disk baseline for this uid has been loaded -- see class doc,
    // "Fifth change". Keyed on uid so switching accounts re-loads.
    var baselineLoaded by remember(uid) { mutableStateOf(false) }
    // See class doc, "Third change" -- blocks pushing until this device has
    // processed at least one snapshot for the signed-in account, so it never
    // races ahead and clobbers real cloud data with its own stale/default
    // local state. Keyed on uid so signing into a DIFFERENT account resets it.
    var hasReceivedFirstSnapshot by remember(uid) { mutableStateOf(false) }

    LaunchedEffect(uid) {
        if (uid == null) {
            baselineLoaded = true
            return@LaunchedEffect
        }
        val saved = withContext(Dispatchers.IO) { CloudSyncBaselineStore.load(context, uid) }
        if (saved != null) lastKnownFields = decodeBaselineFields(saved)
        baselineLoaded = true
    }

    // Persists lastKnownFields to disk on every change so a future cold
    // start doesn't start from empty again (see class doc, "Fifth change").
    // Also fires once right after the initial load above, harmlessly
    // re-saving what was just read.
    //
    // Delayed to lag behind LocalPersistenceCoordinator's 500ms debounced
    // save of the same pulled value (bug fixed 2026-08-11, part of "za
    // każdym razem wraca mi jakaś defaultowa dieta"): a pulled Firestore
    // value updates a ViewModel AND lastKnownFields in the same tick, but
    // without this delay the baseline write (no debounce) could reach disk
    // before the ViewModel's own 500ms-debounced local-state write did. If
    // the process died in that window, the next cold start restored the
    // ViewModel from the OLD (pre-pull) local-state file while the baseline
    // already matched the NEW value -- so the next snapshot looked
    // "already known" and was never re-applied, permanently. Matching (and
    // slightly exceeding) the 500ms debounce here means the local-state
    // write reliably wins the race instead.
    LaunchedEffect(uid, baselineLoaded, lastKnownFields) {
        if (uid == null || !baselineLoaded) return@LaunchedEffect
        delay(600)
        withContext(Dispatchers.IO) { CloudSyncBaselineStore.save(context, uid, encodeBaselineFields(lastKnownFields)) }
    }

    LaunchedEffect(
        uid, hasReceivedFirstSnapshot, profile, displayName, pantryItems, pantryHidden, themeId, uiScale, swipeStyle,
        favIngredients, favoriteRecipes, cooked, ratings, shoppingItems, weekPlan, eatenDays, waterCount,
        waterHistory, weightEntries, activityLogEntries, communityRecipesEnabled,
    ) {
        if (uid == null || !hasReceivedFirstSnapshot) return@LaunchedEffect
        delay(1500)
        val today = CloudSyncCodec.todayDateString()

        // See class doc, "Fourth change" -- only fields that actually
        // changed since lastKnownFields was last updated get pushed, so a
        // push triggered by (say) a profile edit never re-writes this
        // device's possibly-stale local copy of unrelated fields like
        // pantry over a fresher value another device already wrote.
        val currentFieldValues: Map<String, Any?> = mapOf(
            "displayName" to displayName,
            "profile" to profile,
            "pantry" to pantryItems,
            "pantryHidden" to pantryHidden,
            "themeId" to themeId,
            "uiScale" to uiScale,
            "swipeStyle" to swipeStyle,
            "favIngredients" to favIngredients,
            "favoriteRecipes" to favoriteRecipes,
            "ratings" to ratings,
            "cooked" to cooked,
            "shoppingItems" to shoppingItems,
            "weekPlan" to weekPlan,
            "eaten" to eatenDays,
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
            "pantryHidden" to listOf("pantryHidden"),
            "themeId" to listOf("theme"),
            "uiScale" to listOf("uiScale"),
            "swipeStyle" to listOf("swipeRatingStyle"),
            "favIngredients" to listOf("favIngredients"),
            "favoriteRecipes" to listOf("favorites"),
            "ratings" to listOf("recipeRating"),
            "cooked" to listOf("cooked"),
            "shoppingItems" to listOf("shopping"),
            "weekPlan" to listOf("planner", "plannerScale", "plannerLeftover"),
            "eaten" to listOf("eaten"),
            "waterCount" to listOf("water", "waterHistory.$today"),
            "weights" to listOf("weights"),
            "activityLog" to listOf("history"),
            "communityRecipesEnabled" to listOf("communityRecipesEnabled"),
        )

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
            // Nested on purpose -- only "eaten.$today"/"waterHistory.$today"
            // are listed in mergeFields below, so this never touches any
            // OTHER date web has stored for either field (see class doc).
            "waterHistory" to mapOf(today to (waterHistory[today] ?: waterCount)),
            "favorites" to CloudSyncCodec.encodeFavIngredients(favoriteRecipes),
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
        // to confirm it. Persisted to disk by the LaunchedEffect above.
        lastKnownFields = lastKnownFields + currentFieldValues.filterKeys { it in dirtyKeys }
        try {
            FirebaseFirestore.getInstance().collection("users").document(uid)
                .set(data, SetOptions.mergeFields(mergeFields))
                .await()
        } catch (e: Exception) {
            // Genuinely offline (not yet reconnected) doesn't throw here --
            // Firestore's Task stays pending until the write is actually
            // acknowledged or durably fails, so anything reaching this catch
            // (permission-denied, invalid data, etc.) is a real failure this
            // device would otherwise have zero visibility into. Logged (not
            // surfaced to the user -- FR-73 has no error UI yet, matching
            // web's own `console.warn`-only handling of the same failure)
            // so `adb logcat` can actually show WHY a sync silently didn't
            // happen, instead of leaving that a total mystery.
            Log.w("CloudSyncCoordinator", "Push to users/$uid failed (fields: $mergeFields)", e)
        }
    }

    DisposableEffect(uid, baselineLoaded) {
        if (uid == null || !baselineLoaded) return@DisposableEffect onDispose {}
        val registration = FirebaseFirestore.getInstance().collection("users").document(uid)
            .addSnapshotListener { snapshot, error ->
                // Unblocks the push effect above regardless of what this
                // callback finds -- even "doc doesn't exist" is a definite
                // answer ("nothing to lose"), so it counts too.
                hasReceivedFirstSnapshot = true
                if (error != null) {
                    // A genuine listener failure (e.g. permission-denied)
                    // looks identical to "document doesn't exist yet" below
                    // without this -- logged so it's at least visible in
                    // `adb logcat` instead of silently doing nothing.
                    Log.w("CloudSyncCoordinator", "Listener for users/$uid failed", error)
                }
                if (snapshot == null || !snapshot.exists()) return@addSnapshotListener
                // Fast path for the SDK's own optimistic local-cache echo of
                // a write we just made -- doesn't catch every self-echo (see
                // class doc), but cheap and correct as far as it goes.
                if (snapshot.metadata.hasPendingWrites()) return@addSnapshotListener
                val data = snapshot.data ?: return@addSnapshotListener

                // See class doc, "Fourth"/"Fifth change" -- every field
                // successfully decoded below (whether applied locally or
                // recognized as already-known) is Firestore's current known
                // truth for that key, so it updates the push side's dirty-
                // tracking baseline too. Collected here and applied once at
                // the end to avoid 16 separate recompositions per snapshot.
                val known = mutableMapOf<String, Any?>()
                (data["displayName"] as? String)?.let {
                    known["displayName"] = it
                    if (it != lastKnownFields["displayName"] && it != currentDisplayName.value) {
                        profileViewModel.setDisplayName(it)
                    }
                }
                CloudSyncCodec.decodeProfile(data["profile"] as? Map<*, *>)?.let {
                    known["profile"] = it
                    if (it != lastKnownFields["profile"] && it != currentProfile.value) {
                        profileViewModel.save(it)
                    }
                }
                CloudSyncCodec.decodePantry(data["pantry"] as? Map<*, *>)?.let {
                    known["pantry"] = it
                    if (it != lastKnownFields["pantry"] && it != currentPantryItems.value) {
                        pantryViewModel.replaceAll(it)
                    }
                }
                CloudSyncCodec.decodePantryHidden(data["pantryHidden"] as? Map<*, *>)?.let {
                    known["pantryHidden"] = it
                    if (it != lastKnownFields["pantryHidden"] && it != currentPantryHidden.value) {
                        pantryViewModel.replaceHidden(it)
                    }
                }
                (data["theme"] as? String)?.let {
                    known["themeId"] = it
                    if (it != lastKnownFields["themeId"] && it != currentThemeId.value) {
                        themeViewModel.setTheme(it)
                    }
                }
                (data["uiScale"] as? Number)?.toDouble()?.let {
                    known["uiScale"] = it
                    if (it != lastKnownFields["uiScale"] && it != currentUiScale.value) {
                        uiScaleViewModel.setScale(it)
                    }
                }
                (data["swipeRatingStyle"] as? String)?.let { raw ->
                    val style = SwipeRatingStyle.entries.find { it.name == raw }
                    if (style != null) {
                        known["swipeStyle"] = style
                        if (style != lastKnownFields["swipeStyle"] && style != currentSwipeStyle.value) {
                            swipeRatingStyleViewModel.setStyle(style)
                        }
                    }
                }
                CloudSyncCodec.decodeFavIngredients(data["favIngredients"] as? Map<*, *>)?.let {
                    known["favIngredients"] = it
                    if (it != lastKnownFields["favIngredients"] && it != currentFavIngredients.value) {
                        favoriteIngredientsViewModel.replaceAll(it)
                    }
                }
                CloudSyncCodec.decodeFavIngredients(data["favorites"] as? Map<*, *>)?.let {
                    known["favoriteRecipes"] = it
                    if (it != lastKnownFields["favoriteRecipes"] && it != currentFavoriteRecipes.value) {
                        recipeViewModel.replaceFavoriteRecipes(it)
                    }
                }
                CloudSyncCodec.decodeRecipeRating(data["recipeRating"] as? Map<*, *>)?.let {
                    known["ratings"] = it
                    if (it != lastKnownFields["ratings"] && it != currentRatings.value) {
                        recipeViewModel.replaceRatings(it)
                    }
                }
                CloudSyncCodec.decodeCooked(data["cooked"] as? Map<*, *>)?.let {
                    known["cooked"] = it
                    if (it != lastKnownFields["cooked"] && it != currentCooked.value) {
                        recipeViewModel.replaceCooked(it)
                    }
                }
                CloudSyncCodec.decodeShopping(data["shopping"] as? Map<*, *>)?.let {
                    known["shoppingItems"] = it
                    if (it != lastKnownFields["shoppingItems"] && it != currentShoppingItems.value) {
                        shoppingViewModel.replaceAll(it)
                    }
                }
                CloudSyncCodec.decodeWeekPlan(
                    data["planner"] as? Map<*, *>,
                    data["plannerScale"] as? Map<*, *>,
                    data["plannerLeftover"] as? Map<*, *>,
                )?.let {
                    known["weekPlan"] = it
                    if (it != lastKnownFields["weekPlan"] && it != currentWeekPlan.value) {
                        plannerViewModel.replaceAll(it)
                    }
                }
                CloudSyncCodec.decodeEaten(data["eaten"] as? Map<*, *>)?.let {
                    known["eaten"] = it
                    if (it != lastKnownFields["eaten"] && it != currentEatenDays.value) {
                        eatenViewModel.replaceAll(it)
                    }
                }
                CloudSyncCodec.decodeWater(data["water"] as? Map<*, *>)?.let {
                    known["waterCount"] = it
                    if (it != lastKnownFields["waterCount"] && it != currentWaterCount.value) {
                        waterViewModel.setCount(it)
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
                        waterViewModel.replaceHistory(merged)
                    }
                }
                CloudSyncCodec.decodeWeights(data["weights"] as? List<*>)?.let {
                    known["weights"] = it
                    if (it != lastKnownFields["weights"] && it != currentWeightEntries.value) {
                        weightViewModel.replaceAll(it)
                    }
                }
                CloudSyncCodec.decodeActivityLog(data["history"] as? List<*>)?.let {
                    known["activityLog"] = it
                    if (it != lastKnownFields["activityLog"] && it != currentActivityLog.value) {
                        activityLogViewModel.replaceAll(it)
                    }
                }
                (data["communityRecipesEnabled"] as? Boolean)?.let {
                    known["communityRecipesEnabled"] = it
                    if (it != lastKnownFields["communityRecipesEnabled"] && it != currentCommunityRecipesEnabled.value) {
                        recipeViewModel.setCommunityRecipesEnabled(it)
                    }
                }
                if (known.isNotEmpty()) lastKnownFields = lastKnownFields + known
            }
        onDispose { registration.remove() }
    }
}
