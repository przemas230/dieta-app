package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.ActivityLevel
import com.przemas230.dietaapp.data.ActivityLogEntry
import com.przemas230.dietaapp.data.EatenDay
import com.przemas230.dietaapp.data.EatenEntry
import com.przemas230.dietaapp.data.Goal
import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.PlannedMeal
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.RecipeReview
import com.przemas230.dietaapp.data.Sex
import com.przemas230.dietaapp.data.ShoppingItem
import com.przemas230.dietaapp.data.Snack
import com.przemas230.dietaapp.data.SpiceLevel
import com.przemas230.dietaapp.data.WeightEntry
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * FR-73: converts the Android app's currently-syncable state to/from plain
 * `Map<String, Any?>` shapes -- the native representation the Firestore SDK
 * reads/writes directly (`DocumentSnapshot.data`/`DocumentReference.set`),
 * so the app module doesn't need a JSON round-trip on top. Deliberately a
 * pure Kotlin file (no Firestore/Android import) so the encode/decode pairs
 * are unit-testable here -- see CloudSyncCodecTest.
 *
 * CRITICAL: field names/shapes here must match index.html's Firestore
 * writes EXACTLY (same `users/{uid}` document, read by both platforms) --
 * see SYNCED_STATE_KEYS in index.html. Before 2026-08-10 this codec used
 * Android-internal field names for pantry (`category`/`quantity`/`unit`
 * instead of web's `cat`/`qty`/`unitCat`, enum .name() instead of Polish
 * labels) which meant pantry silently never actually round-tripped between
 * platforms despite the sync mechanism itself working -- fixed together
 * with widening coverage to the other fields Android now has state for.
 *
 * Scope is FR-73's field list intersected with what Android actually has
 * built: displayName, profile, pantry, favIngredients, favorites (the
 * per-recipe ⭐/❤️ toggle, RecipeViewModel.favoriteRecipes -- reuses this
 * same encodeFavIngredients/decodeFavIngredients pair since the shape,
 * Set<String> -> {name: true}, is identical to favIngredients, just a
 * different top-level key/meaning; wired into CloudSyncCoordinator
 * 2026-08-24 after it was found to be ported and locally-persisted but
 * silently never actually synced to Firestore, see that file's "Seventh
 * change"), recipeRating, cooked, shopping,
 * planner/plannerScale/plannerLeftover, eaten (full per-date history as of
 * FR-83, see below), water (today only), waterHistory (today's entry only
 * -- see CloudSyncCoordinator's nested-merge push for why), weights,
 * history (the activity log, FR-42 -- index.html's field name, NOT
 * "activityLog"), theme, uiScale, swipeRatingStyle.
 * `communityRecipesEnabled` (FR-68/76) is synced too, but as a plain scalar
 * directly in CloudSyncCoordinator (not through this codec -- it's a bare
 * Boolean, no encode/decode pair needed). Fields FR-73/FR-78 also list that
 * don't exist as Android state yet (myRecipes, recipeReviews, customTiles,
 * pantryUnitOverride, pantryCategoryOverride, household, waterNotifEnabled,
 * waterReminder) are simply not encoded -- nothing to sync for a feature
 * that isn't ported. Semantics are FR-73's original "last cloud write wins the
 * whole document" (decode replaces local state wholesale) -- NOT FR-78's
 * later per-item 3-way merge, which is a separate, bigger port (Android
 * just does whole-field replace on every touched top-level key, same as
 * web's OWN semantics for the fields web itself doesn't put in
 * MAP_MERGE_KEYS, e.g. weights/history/profile). `eaten` used to be a
 * second exception here too (Android only ever knew "today" locally, so a
 * plain top-level replace would have wiped every other date web had
 * stored) -- fixed 2026-08-10 with a narrow "eaten.$today" nested
 * mergeFields push, and superseded entirely by FR-83 (2026-08-23): Android
 * now tracks full per-date history the same as web, so `eaten` rejoined the
 * ordinary whole-field-replace group above and no longer needs the nested
 * push (see CloudSyncCoordinator). `waterHistory` still needs it, since
 * water tracking itself remains today-only (FR-83 only covers weight +
 * calorie history editing, not water).
 */
object CloudSyncCodec {
    fun encodeProfile(profile: Profile): Map<String, Any?> = mapOf(
        "sex" to profile.sex.name,
        "age" to profile.age,
        "heightCm" to profile.heightCm,
        "weightKg" to profile.weightKg,
        "targetWeightKg" to profile.targetWeightKg,
        "activity" to profile.activity.name,
        "goal" to profile.goal.name,
        "glutenFree" to profile.glutenFree,
        "lactoseFree" to profile.lactoseFree,
        "strictLowGI" to profile.strictLowGI,
        "configured" to profile.configured,
    )

    /** Returns null on missing/corrupt data so callers can skip applying it, instead of crashing on a bad remote doc. */
    fun decodeProfile(map: Map<*, *>?): Profile? {
        if (map == null) return null
        return try {
            Profile(
                sex = enumFrom<Sex>(map["sex"]) ?: Sex.KOBIETA,
                age = numberFrom(map["age"])?.toInt() ?: Profile().age,
                heightCm = numberFrom(map["heightCm"])?.toInt() ?: Profile().heightCm,
                weightKg = numberFrom(map["weightKg"]) ?: Profile().weightKg,
                targetWeightKg = numberFrom(map["targetWeightKg"]) ?: Profile().targetWeightKg,
                activity = enumFrom<ActivityLevel>(map["activity"]) ?: ActivityLevel.LEKKO_AKTYWNY,
                goal = enumFrom<Goal>(map["goal"]) ?: Goal.REDUKCJA,
                glutenFree = map["glutenFree"] as? Boolean ?: false,
                lactoseFree = map["lactoseFree"] as? Boolean ?: false,
                strictLowGI = map["strictLowGI"] as? Boolean ?: true,
                configured = map["configured"] as? Boolean ?: false,
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * index.html's state.pantry[canon] shape is `{label, cat, type, qty,
     * unitCat}` (product) or `{label, cat, type, level}` (spice) -- Polish
     * category label (not Kotlin enum name), `qty` (not `quantity`),
     * `unitCat` (the coarse weight/volume/count bucket, not Android's
     * literal display unit like "g"/"ml"). `PantryTiles.unitCatToUnit` is
     * the exact inverse Android already uses when creating a fresh entry
     * (tileTapDelta/toggleHaveIngredient), so decoding through unitCat loses
     * nothing Android itself would ever have stored.
     */
    fun encodePantry(items: Map<String, PantryItem>): Map<String, Any?> =
        items.mapValues { (name, item) ->
            when (item) {
                is PantryItem.Product -> mapOf(
                    "label" to name,
                    "cat" to item.category.label,
                    "type" to "product",
                    "qty" to item.quantity,
                    "unitCat" to RecipePantryMatching.pantryUnitCat(item.unit),
                )
                is PantryItem.Spice -> mapOf(
                    "label" to name,
                    "cat" to item.category.label,
                    "type" to "spice",
                    "level" to item.level.label,
                )
            }
        }

    fun decodePantry(map: Map<*, *>?): Map<String, PantryItem>? {
        if (map == null) return null
        val result = LinkedHashMap<String, PantryItem>()
        map.forEach { (rawKey, rawValue) ->
            val name = rawKey as? String ?: return@forEach
            val entry = rawValue as? Map<*, *> ?: return@forEach
            val category = (entry["cat"] as? String)?.let { PantryCategory.byLabel(it) } ?: PantryCategory.INNE
            val item: PantryItem? = when (entry["type"] as? String) {
                "spice" -> {
                    val levelLabel = entry["level"] as? String
                    SpiceLevel.entries.find { it.label == levelLabel }?.let { PantryItem.Spice(name, category, it) }
                }
                "product" -> {
                    val qty = numberFrom(entry["qty"])
                    val unitCat = entry["unitCat"] as? String
                    if (qty != null && unitCat != null) {
                        PantryItem.Product(name, category, qty, PantryTiles.unitCatToUnit(unitCat))
                    } else {
                        null
                    }
                }
                else -> null
            }
            if (item != null) result[name] = item
        }
        return result
    }

    /** index.html's state.favIngredients[canon] = true (toggled off values may linger as false rather than being deleted). Android's Set only ever holds favorited names, so every encoded value is true. */
    fun encodeFavIngredients(favorites: Set<String>): Map<String, Any?> = favorites.associateWith { true }

    fun decodeFavIngredients(map: Map<*, *>?): Set<String>? {
        if (map == null) return null
        return map.entries.mapNotNull { (k, v) -> (k as? String)?.takeIf { v == true } }.toSet()
    }

    /** index.html's state.recipeRating[id] = "like"/"dislike" (lowercase strings, not Kotlin enum names). */
    fun encodeRecipeRating(ratings: Map<String, RecipeRating>): Map<String, Any?> =
        ratings.mapValues { (_, r) -> r.name.lowercase() }

    fun decodeRecipeRating(map: Map<*, *>?): Map<String, RecipeRating>? {
        if (map == null) return null
        val result = LinkedHashMap<String, RecipeRating>()
        map.forEach { (k, v) ->
            val id = k as? String ?: return@forEach
            val rating = when (v as? String) {
                "like" -> RecipeRating.LIKE
                "dislike" -> RecipeRating.DISLIKE
                else -> null
            }
            if (rating != null) result[id] = rating
        }
        return result
    }

    /** index.html's state.cooked[recipeId] = [{date: ISO8601 string, rating: 1..5|null}, ...]. */
    fun encodeCooked(cooked: Map<String, List<com.przemas230.dietaapp.data.CookEntry>>): Map<String, Any?> =
        cooked.mapValues { (_, entries) ->
            entries.map { entry ->
                mapOf(
                    "date" to Instant.ofEpochMilli(entry.dateEpochMillis).toString(),
                    "rating" to entry.rating,
                )
            }
        }

    fun decodeCooked(map: Map<*, *>?): Map<String, List<com.przemas230.dietaapp.data.CookEntry>>? {
        if (map == null) return null
        val result = LinkedHashMap<String, List<com.przemas230.dietaapp.data.CookEntry>>()
        map.forEach { (k, v) ->
            val recipeId = k as? String ?: return@forEach
            val list = v as? List<*> ?: return@forEach
            val entries = list.mapNotNull { raw ->
                val entryMap = raw as? Map<*, *> ?: return@mapNotNull null
                val dateStr = entryMap["date"] as? String ?: return@mapNotNull null
                val epochMillis = try { Instant.parse(dateStr).toEpochMilli() } catch (e: Exception) { return@mapNotNull null }
                com.przemas230.dietaapp.data.CookEntry(epochMillis, numberFrom(entryMap["rating"])?.toInt())
            }
            result[recipeId] = entries
        }
        return result
    }

    /** index.html's state.shopping[canon+"|"+unitCat] = {name, unitCat, qty, checked, contributions}. */
    fun encodeShopping(items: Map<String, ShoppingItem>): Map<String, Any?> =
        items.mapValues { (_, item) ->
            mapOf(
                "name" to item.name,
                "unitCat" to item.unitCat,
                "qty" to item.quantity,
                "checked" to item.checked,
                "contributions" to item.contributions,
            )
        }

    fun decodeShopping(map: Map<*, *>?): Map<String, ShoppingItem>? {
        if (map == null) return null
        val result = LinkedHashMap<String, ShoppingItem>()
        map.forEach { (k, v) ->
            val key = k as? String ?: return@forEach
            val entry = v as? Map<*, *> ?: return@forEach
            val name = entry["name"] as? String ?: return@forEach
            val unitCat = entry["unitCat"] as? String ?: return@forEach
            val qty = numberFrom(entry["qty"]) ?: return@forEach
            val checked = entry["checked"] as? Boolean ?: false
            val contributions = (entry["contributions"] as? Map<*, *>)?.entries
                ?.mapNotNull { (ck, cv) -> (ck as? String)?.let { it to (numberFrom(cv) ?: 0.0) } }
                ?.toMap() ?: emptyMap()
            result[key] = ShoppingItem(name, unitCat, qty, checked, contributions)
        }
        return result
    }

    /**
     * index.html tracks state.planner/state.plannerScale/state.plannerLeftover
     * as three parallel `[day][cat]` maps (day = 0..6 numeric index, but
     * Firestore/JSON map keys are always strings). Android bundles the same
     * three values into one PlannedMeal, so encoding fans them out into the
     * three separate top-level fields web expects, and decoding re-merges them.
     */
    fun encodePlanner(plan: WeekPlan): Map<String, Any?> =
        plan.entries.associate { (day, slots) -> day.toString() to slots.mapValues { (_, meal) -> meal.recipeId } }

    fun encodePlannerScale(plan: WeekPlan): Map<String, Any?> =
        plan.entries.associate { (day, slots) -> day.toString() to slots.mapValues { (_, meal) -> meal.scale } }

    fun encodePlannerLeftover(plan: WeekPlan): Map<String, Any?> =
        plan.entries.associate { (day, slots) -> day.toString() to slots.mapValues { (_, meal) -> meal.isLeftover } }

    fun decodeWeekPlan(planner: Map<*, *>?, plannerScale: Map<*, *>?, plannerLeftover: Map<*, *>?): WeekPlan? {
        if (planner == null) return null
        val result = LinkedHashMap<Int, Map<String, PlannedMeal>>()
        planner.forEach { (dayRaw, slotsRaw) ->
            val day = (dayRaw as? String)?.toIntOrNull() ?: return@forEach
            val slots = slotsRaw as? Map<*, *> ?: return@forEach
            val scaleDay = (plannerScale?.get(dayRaw) as? Map<*, *>)
            val leftoverDay = (plannerLeftover?.get(dayRaw) as? Map<*, *>)
            val slotResult = LinkedHashMap<String, PlannedMeal>()
            slots.forEach { (catRaw, recipeIdRaw) ->
                val cat = catRaw as? String ?: return@forEach
                val recipeId = recipeIdRaw as? String ?: return@forEach
                val scale = numberFrom(scaleDay?.get(cat)) ?: 1.0
                val leftover = leftoverDay?.get(cat) as? Boolean ?: false
                slotResult[cat] = PlannedMeal(recipeId, scale, leftover)
            }
            result[day] = slotResult
        }
        return result
    }

    /**
     * FR-83: index.html's state.eaten shape -- {date: {catId:
     * {done,kcal,name}, snacks:[...]}} for every date the user has ever
     * touched, mixing per-category entries with a "snacks" array key in the
     * SAME flat per-date object. Android used to only ever track "today"
     * here (see the class doc for that history); it now mirrors the full
     * per-date map the same way `pantry`/`profile`/every other synced field
     * already does, so no date-specific narrowing happens on either side.
     */
    fun encodeEaten(days: Map<String, EatenDay>): Map<String, Any?> =
        days.mapValues { (_, day) ->
            val dayMap = LinkedHashMap<String, Any?>()
            day.entries.forEach { (cat, entry) ->
                dayMap[cat] = mapOf("done" to entry.done, "kcal" to entry.kcal, "name" to entry.name)
            }
            dayMap["snacks"] = day.snacks.map { mapOf("id" to it.id, "name" to it.name, "kcal" to it.kcal) }
            dayMap
        }

    fun decodeEaten(map: Map<*, *>?): Map<String, EatenDay>? {
        if (map == null) return null
        val result = LinkedHashMap<String, EatenDay>()
        map.forEach { (dateRaw, dayRaw) ->
            val date = dateRaw as? String ?: return@forEach
            val dayMap = dayRaw as? Map<*, *> ?: return@forEach
            val entries = LinkedHashMap<String, EatenEntry>()
            val snacks = mutableListOf<Snack>()
            dayMap.forEach { (k, v) ->
                val key = k as? String ?: return@forEach
                if (key == "snacks") {
                    (v as? List<*>)?.forEach { raw ->
                        val s = raw as? Map<*, *> ?: return@forEach
                        val id = s["id"]?.toString() ?: return@forEach
                        val name = s["name"] as? String ?: return@forEach
                        val kcal = numberFrom(s["kcal"])?.toInt() ?: return@forEach
                        snacks.add(Snack(id, name, kcal))
                    }
                } else {
                    val entryMap = v as? Map<*, *> ?: return@forEach
                    val done = entryMap["done"] as? Boolean ?: false
                    entries[key] = EatenEntry(done, numberFrom(entryMap["kcal"])?.toInt(), entryMap["name"] as? String)
                }
            }
            result[date] = EatenDay(entries, snacks)
        }
        return result
    }

    /** index.html's state.water = {date: "YYYY-MM-DD" (UTC), count}. Decoding ignores a remote count from a different (UTC) day, same "today only" scope as eaten. */
    fun encodeWater(count: Int): Map<String, Any?> = mapOf("date" to todayUtcDateString(), "count" to count)

    fun decodeWater(map: Map<*, *>?): Int? {
        if (map == null) return null
        if (map["date"] as? String != todayUtcDateString()) return null
        return numberFrom(map["count"])?.toInt()
    }

    /**
     * FR-66: index.html's state.myRecipes entries (`{id, cat, name, time,
     * kcal, ingredients, method, source, protein?, carbs?, fat?, fiber?,
     * gi?, gl?}`) -- not currently pushed to Firestore (see class doc), but
     * used for LOCAL persistence (LocalStateStore) so custom recipes survive
     * an app restart. Only ever encodes recipes with source=="custom" --
     * the 229 built-in ones are loaded from recipes.json every launch, never
     * round-tripped through this store.
     */
    fun encodeMyRecipes(recipes: List<Recipe>): List<Map<String, Any?>> =
        recipes.filter { it.source == "custom" }.map { r ->
            mapOf(
                "id" to r.id,
                "cat" to r.cat,
                "name" to r.name,
                "time" to r.time,
                "kcal" to r.kcal,
                "ingredients" to r.ingredients,
                "method" to r.method,
                "source" to "custom",
                "protein" to r.protein,
                "carbs" to r.carbs,
                "fat" to r.fat,
                "fiber" to r.fiber,
                "gi" to r.gi,
                "gl" to r.gl,
            )
        }

    fun decodeMyRecipes(list: List<*>?): List<Recipe>? {
        if (list == null) return null
        return list.mapNotNull { raw ->
            val m = raw as? Map<*, *> ?: return@mapNotNull null
            val id = m["id"] as? String ?: return@mapNotNull null
            val name = m["name"] as? String ?: return@mapNotNull null
            val ingredients = (m["ingredients"] as? List<*>)?.mapNotNull { it as? String } ?: return@mapNotNull null
            Recipe(
                id = id,
                cat = m["cat"] as? String ?: "obiady",
                name = name,
                time = m["time"] as? String ?: "15 min",
                kcal = numberFrom(m["kcal"])?.toInt() ?: return@mapNotNull null,
                ingredients = ingredients,
                method = m["method"] as? String ?: "",
                protein = numberFrom(m["protein"]),
                carbs = numberFrom(m["carbs"]),
                fat = numberFrom(m["fat"]),
                fiber = numberFrom(m["fiber"]),
                gi = numberFrom(m["gi"]),
                gl = numberFrom(m["gl"]),
                source = "custom",
            )
        }
    }

    /** FR-67: index.html's state.recipeReviews[id] = {stars, comment, at}. Not currently pushed to Firestore (see class doc) -- used for LOCAL persistence only. */
    fun encodeReviews(reviews: Map<String, RecipeReview>): Map<String, Any?> =
        reviews.mapValues { (_, r) ->
            mapOf("stars" to r.stars, "comment" to r.comment, "at" to Instant.ofEpochMilli(r.atEpochMillis).toString())
        }

    fun decodeReviews(map: Map<*, *>?): Map<String, RecipeReview>? {
        if (map == null) return null
        val result = LinkedHashMap<String, RecipeReview>()
        map.forEach { (k, v) ->
            val id = k as? String ?: return@forEach
            val entry = v as? Map<*, *> ?: return@forEach
            val stars = numberFrom(entry["stars"])?.toInt() ?: return@forEach
            val atStr = entry["at"] as? String
            val at = atStr?.let { try { Instant.parse(it).toEpochMilli() } catch (e: Exception) { null } } ?: 0L
            result[id] = RecipeReview(stars, entry["comment"] as? String, at)
        }
        return result
    }

    /** FR-40: index.html's state.weights entries (`{date, kg}`). Not currently pushed to Firestore (see class doc) -- used for LOCAL persistence only. */
    fun encodeWeights(entries: List<WeightEntry>): List<Map<String, Any?>> =
        entries.map { mapOf("date" to it.dateStr, "kg" to it.kg) }

    fun decodeWeights(list: List<*>?): List<WeightEntry>? {
        if (list == null) return null
        return list.mapNotNull { raw ->
            val m = raw as? Map<*, *> ?: return@mapNotNull null
            val date = m["date"] as? String ?: return@mapNotNull null
            val kg = numberFrom(m["kg"]) ?: return@mapNotNull null
            WeightEntry(date, kg)
        }
    }

    /** FR-41/42: date-string -> Int maps (EatenViewModel.kcalHistory / WaterViewModel.history) -- local-persistence only, not cloud-synced yet. */
    fun encodeDateIntMap(map: Map<String, Int>): Map<String, Any?> = map

    fun decodeDateIntMap(map: Map<*, *>?): Map<String, Int>? {
        if (map == null) return null
        val result = LinkedHashMap<String, Int>()
        map.forEach { (k, v) ->
            val date = k as? String ?: return@forEach
            val value = numberFrom(v)?.toInt() ?: return@forEach
            result[date] = value
        }
        return result
    }

    /** index.html's `addLog()` (state.history) writes `ts` via `new Date().toISOString()` -- an ISO-8601 STRING, not a number. */
    private val activityLogTsFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC)

    /**
     * FR-73/FR-42: ActivityLogViewModel.entries ("Historia aktywności" / web's
     * `state.history`), synced to/from Firestore's `history` field by
     * CloudSyncCoordinator. `ts` is encoded as an ISO-8601 string (matching
     * [activityLogTsFormatter], the exact shape of JS's `toISOString()`) --
     * NOT epoch millis -- to match index.html's `addLog()` byte-for-byte.
     *
     * **Bug fixed 2026-08-24** (real data loss on a real account: 200
     * web-accumulated `history` entries collapsed to a handful of Android-only
     * ones after a single sign-in + a few pantry edits): this used to encode
     * `ts` as a raw epoch-millis Long, while index.html has always written it
     * as an ISO string. [decodeActivityLog] required a `Number` and silently
     * DROPPED every entry whose `ts` didn't cast, via `mapNotNull` -- so
     * decoding a web-authored `history` array (all string `ts`) returned an
     * EMPTY list rather than null or an error. CloudSyncCoordinator treats an
     * empty-but-non-null decode as a legitimate "this is Firestore's current
     * truth", applies it (`activityLogViewModel.replaceAll(emptyList())`),
     * and records that empty list as the new sync baseline. The next local
     * pantry/shopping action then makes `activityLog` look "dirty" against
     * that empty baseline, and since `history` is pushed as a whole-field
     * `SetOptions.mergeFields` replace (it's an appendable log, not a
     * per-date map like `eaten`/`waterHistory`), that push overwrites
     * Firestore's entire real history with Android's own short local list --
     * permanently, with no warning. Fixed by encoding `ts` the same way web
     * does, and by having [decodeActivityLog] accept EITHER shape (string OR
     * number) so it keeps reading any entries already written the old,
     * Android-only-numeric way.
     */
    fun encodeActivityLog(entries: List<ActivityLogEntry>): List<Map<String, Any?>> =
        entries.map {
            mapOf(
                "ts" to activityLogTsFormatter.format(Instant.ofEpochMilli(it.tsEpochMillis)),
                "action" to it.action,
                "detail" to it.detail,
            )
        }

    fun decodeActivityLog(list: List<*>?): List<ActivityLogEntry>? {
        if (list == null) return null
        return list.mapNotNull { raw ->
            val m = raw as? Map<*, *> ?: return@mapNotNull null
            val tsRaw = m["ts"]
            val ts = numberFrom(tsRaw)?.toLong()
                ?: (tsRaw as? String)?.let { runCatching { Instant.parse(it).toEpochMilli() }.getOrNull() }
                ?: return@mapNotNull null
            val action = m["action"] as? String ?: return@mapNotNull null
            val detail = m["detail"] as? String ?: return@mapNotNull null
            ActivityLogEntry(ts, action, detail)
        }
    }

    /** Public so CloudSyncCoordinator can target today's nested Firestore field path (e.g. "eaten.$today") without duplicating this calculation. */
    fun todayUtcDateString(): String = LocalDate.now(ZoneOffset.UTC).toString()

    /** The full syncable-state document -- what gets pushed to `users/{uid}` with SetOptions.merge(). */
    fun encodeAll(
        displayName: String,
        profile: Profile,
        pantry: Map<String, PantryItem>,
        themeId: String,
        uiScale: Double?,
        swipeRatingStyle: String,
        favIngredients: Set<String>,
        recipeRating: Map<String, RecipeRating>,
        cooked: Map<String, List<com.przemas230.dietaapp.data.CookEntry>>,
        shopping: Map<String, ShoppingItem>,
        weekPlan: WeekPlan,
        eatenDays: Map<String, EatenDay>,
        waterCount: Int,
    ): Map<String, Any?> = mapOf(
        "displayName" to displayName,
        "profile" to encodeProfile(profile),
        "pantry" to encodePantry(pantry),
        "theme" to themeId,
        "uiScale" to uiScale,
        "swipeRatingStyle" to swipeRatingStyle,
        "favIngredients" to encodeFavIngredients(favIngredients),
        "recipeRating" to encodeRecipeRating(recipeRating),
        "cooked" to encodeCooked(cooked),
        "shopping" to encodeShopping(shopping),
        "planner" to encodePlanner(weekPlan),
        "plannerScale" to encodePlannerScale(weekPlan),
        "plannerLeftover" to encodePlannerLeftover(weekPlan),
        "eaten" to encodeEaten(eatenDays),
        "water" to encodeWater(waterCount),
    )

    private inline fun <reified T : Enum<T>> enumFrom(raw: Any?): T? {
        val name = raw as? String ?: return null
        return try {
            enumValueOf<T>(name)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun numberFrom(raw: Any?): Double? = (raw as? Number)?.toDouble()
}
