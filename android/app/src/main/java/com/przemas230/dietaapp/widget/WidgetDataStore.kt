package com.przemas230.dietaapp.widget

import android.content.Context
import com.przemas230.dietaapp.data.EatenDay
import com.przemas230.dietaapp.data.LocalStateStore
import com.przemas230.dietaapp.data.RecipeRepository
import com.przemas230.dietaapp.logic.AppDates
import com.przemas230.dietaapp.logic.CloudSyncCodec
import com.przemas230.dietaapp.logic.EatenOperations
import com.przemas230.dietaapp.logic.PlannerOperations
import com.przemas230.dietaapp.logic.ProfileCalculations
import com.przemas230.dietaapp.logic.WaterOperations

/**
 * Requested 2026-08-30 ("dodaj mi jeszcze widgety... chce kilka różnych do
 * wyboru żeby można było na nich zaznaczać posiłki i wodę oraz podglądać
 * dania"): the data layer shared by all three home-screen widgets
 * (TodayMealsWidget, WaterWidget, DashboardWidget).
 *
 * Widgets run outside the Activity/ViewModel tree entirely -- a
 * GlanceAppWidget's `provideGlance` has no access to PlannerViewModel,
 * EatenViewModel, etc. Rather than inventing a second persistence
 * mechanism, this reads/writes the EXACT SAME on-disk file
 * [LocalStateStore] already uses (`local_state.json`), through the exact
 * same [CloudSyncCodec] encode/decode functions the app's own
 * LocalPersistenceCoordinator uses -- one file, one codec, two readers.
 *
 * **The one real risk this creates** (documented here since it's the kind
 * of thing that's easy to miss): if the app process is ALSO alive and its
 * own debounced local-save fires around the same moment a widget writes,
 * "last write wins" could silently drop the widget's change -- the app's
 * save always writes its FULL in-memory snapshot, which wouldn't yet know
 * about a change the widget just made straight to disk. Mitigated (not
 * eliminated) by [com.przemas230.dietaapp.ui.LocalPersistenceCoordinator]
 * re-reading `eaten`/`water` from disk on `ON_RESUME` (see its own
 * comment), so returning to the app after using a widget picks up the
 * widget's change instead of racing it. The narrow window that remains
 * (app actively saving in the background AT THE EXACT INSTANT the widget
 * writes) is an acceptable, documented tradeoff for a single-user local
 * app, not something worth a full app-side write-lock/IPC mechanism for.
 */
object WidgetDataStore {
    data class MealRow(
        val catId: String,
        val label: String,
        val emoji: String,
        val recipeName: String?,
        val kcal: Int,
        val eaten: Boolean,
    )

    data class Snapshot(
        val meals: List<MealRow>,
        val waterCount: Int,
        val remainingKcal: Int,
        val kcalTarget: Int,
    )

    /** Everything all three widgets need, read in one pass so each only opens the file once per update. */
    fun readSnapshot(context: Context): Snapshot {
        val data = LocalStateStore.load(context)
        val weekPlan = CloudSyncCodec.decodeWeekPlan(
            data?.get("planner") as? Map<*, *>,
            data?.get("plannerScale") as? Map<*, *>,
            data?.get("plannerLeftover") as? Map<*, *>,
        ) ?: emptyMap()
        val eatenDays = CloudSyncCodec.decodeEaten(data?.get("eaten") as? Map<*, *>) ?: emptyMap()
        val waterCount = CloudSyncCodec.decodeWater(data?.get("water") as? Map<*, *>) ?: 0
        val profile = CloudSyncCodec.decodeProfile(data?.get("profile") as? Map<*, *>)
        val recipesById = RecipeRepository.loadRecipes(context).associateBy { it.id }

        val today = AppDates.today()
        val todayIndex = (today.dayOfWeek.value - 1).coerceIn(0, 6)
        val todayEntries = eatenDays[today.toString()]?.entries ?: emptyMap()
        val todayMeals = weekPlan[todayIndex].orEmpty()

        val rows = PlannerOperations.PLANNER_CATEGORIES.map { cat ->
            val meal = todayMeals[cat.id]
            val recipe = meal?.let { recipesById[it.recipeId] }
            // meal is guaranteed non-null whenever recipe is (recipe comes
            // from meal?.let{} above), so this is a single real check, not
            // a redundant pair -- the !! just tells the compiler what's
            // already true.
            val kcal = if (recipe != null) PlannerOperations.scaledKcal(recipe, meal!!.scale) else 0
            MealRow(
                catId = cat.id,
                label = cat.label,
                emoji = cat.emoji,
                recipeName = recipe?.name,
                kcal = kcal,
                eaten = EatenOperations.isEaten(todayEntries, cat.id),
            )
        }
        val eatenKcal = EatenOperations.dailyEatenKcal(todayEntries)
        val kcalTarget = profile?.let { ProfileCalculations.calcTargets(it).daily } ?: 0
        return Snapshot(
            meals = rows,
            waterCount = waterCount,
            remainingKcal = (kcalTarget - eatenKcal).coerceAtLeast(0),
            kcalTarget = kcalTarget,
        )
    }

    /** Toggles today's [catId] slot eaten/not-eaten -- the widget checkbox tap. No-op if that slot has no planned dish. */
    fun toggleMealEaten(context: Context, catId: String) {
        val data = LocalStateStore.load(context)?.toMutableMap() ?: mutableMapOf()
        val weekPlan = CloudSyncCodec.decodeWeekPlan(
            data["planner"] as? Map<*, *>,
            data["plannerScale"] as? Map<*, *>,
            data["plannerLeftover"] as? Map<*, *>,
        ) ?: emptyMap()
        val recipesById = RecipeRepository.loadRecipes(context).associateBy { it.id }

        val today = AppDates.today()
        val todayIndex = (today.dayOfWeek.value - 1).coerceIn(0, 6)
        val meal = weekPlan[todayIndex]?.get(catId) ?: return
        val recipe = recipesById[meal.recipeId] ?: return
        val kcal = PlannerOperations.scaledKcal(recipe, meal.scale)

        val eatenDays = (CloudSyncCodec.decodeEaten(data["eaten"] as? Map<*, *>) ?: emptyMap()).toMutableMap()
        val todayKey = today.toString()
        val todayDay = eatenDays[todayKey] ?: EatenDay()
        val wasEaten = EatenOperations.isEaten(todayDay.entries, catId)
        val newEntries = EatenOperations.setEaten(
            todayDay.entries,
            catId,
            done = !wasEaten,
            plannedKcal = kcal,
            plannedName = recipe.name,
        )
        eatenDays[todayKey] = todayDay.copy(entries = newEntries)

        data["eaten"] = CloudSyncCodec.encodeEaten(eatenDays)
        LocalStateStore.save(context, data)
    }

    /** Sets today's water level directly (droplet tap, same "tap current level to step back one" semantics as the header strip -- see [WaterOperations.tapDroplet]). */
    fun setWater(context: Context, count: Int) {
        val data = LocalStateStore.load(context)?.toMutableMap() ?: mutableMapOf()
        data["water"] = CloudSyncCodec.encodeWater(count.coerceIn(0, WaterOperations.MAX_LEVEL))
        LocalStateStore.save(context, data)
    }
}
