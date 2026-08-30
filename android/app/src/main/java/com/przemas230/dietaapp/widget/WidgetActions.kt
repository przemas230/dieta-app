package com.przemas230.dietaapp.widget

import android.content.Context
import android.content.Intent
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.updateAll
import com.przemas230.dietaapp.MainActivity

/**
 * Requested 2026-08-30 (home-screen widgets). Every mutating tap across all
 * three widgets funnels through [WidgetDataStore] and then refreshes ALL
 * THREE widget types -- eaten/water changes can show up in more than one
 * of them at once (e.g. TodayMealsWidget's checkmarks and DashboardWidget's
 * remaining-kcal figure both move when one meal is toggled), and calling
 * `updateAll` on a widget type with zero placed instances is a harmless
 * no-op, so this stays simple rather than tracking which widgets are
 * actually on screen.
 */
private suspend fun refreshAllWidgets(context: Context) {
    TodayMealsWidget().updateAll(context)
    WaterWidget().updateAll(context)
    DashboardWidget().updateAll(context)
}

class ToggleMealAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val catId = parameters[CAT_ID_KEY] ?: return
        WidgetDataStore.toggleMealEaten(context, catId)
        refreshAllWidgets(context)
    }

    companion object {
        val CAT_ID_KEY = ActionParameters.Key<String>("cat_id")
    }
}

class SetWaterLevelAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val tappedIndex = parameters[DROPLET_INDEX_KEY] ?: return
        // Same "tap the droplet AT the current level to step back one glass"
        // semantics as the header strip (WaterOperations.tapDroplet) --
        // reimplemented here rather than imported since it needs the
        // CURRENT count read fresh from disk, not a value baked into the
        // widget's last render.
        val current = WidgetDataStore.readSnapshot(context).waterCount
        val next = if (current == tappedIndex + 1) tappedIndex else tappedIndex + 1
        WidgetDataStore.setWater(context, next)
        refreshAllWidgets(context)
    }

    companion object {
        val DROPLET_INDEX_KEY = ActionParameters.Key<Int>("droplet_index")
    }
}

/**
 * Opens the app on a specific tab, reusing FR-118's shortcut mechanism
 * (MainActivity.EXTRA_SHORTCUT_ROUTE) -- widgets and the long-press app
 * icon shortcuts land on exactly the same code path, so there is only one
 * place that knows how to jump to a tab on launch.
 */
class OpenAppRouteAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val route = parameters[ROUTE_KEY]
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (route != null) putExtra(MainActivity.EXTRA_SHORTCUT_ROUTE, route)
        }
        context.startActivity(intent)
    }

    companion object {
        val ROUTE_KEY = ActionParameters.Key<String>("route")
    }
}
