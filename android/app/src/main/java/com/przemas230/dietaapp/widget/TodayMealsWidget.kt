package com.przemas230.dietaapp.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.CheckboxDefaults
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.GlanceTheme
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextDecoration
import androidx.glance.text.TextStyle

/**
 * Requested 2026-08-30 ("chce kilka różnych do wyboru żeby można było na
 * nich zaznaczać posiłki i wodę oraz podglądać dania"): widget 1 of 3 --
 * today's 5 Planer slots as a checklist. A tap on the checkbox marks that
 * meal eaten/not-eaten right from the home screen (WidgetDataStore.
 * toggleMealEaten, no app launch needed); a tap on the dish name/row itself
 * opens the app straight to the Planer tab, matching how the dashboard
 * card's own tap-to-preview works there. Category label + dish name double
 * as the "podgląd dania" (glance/preview) the widget itself IS, by design
 * -- a home-screen widget's whole point is showing that at a glance,
 * without opening anything.
 */
class TodayMealsWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val snapshot = WidgetDataStore.readSnapshot(context)
        provideContent {
            GlanceTheme {
                Content(snapshot)
            }
        }
    }

    @Composable
    private fun Content(snapshot: WidgetDataStore.Snapshot) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.widgetBackground)
                .padding(12.dp),
        ) {
            Text(
                "🍽️ Dzisiejszy Planer",
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GlanceTheme.colors.onSurface),
            )
            Spacer(modifier = GlanceModifier.height(6.dp))
            snapshot.meals.forEach { meal ->
                MealRow(meal)
            }
        }
    }

    @Composable
    private fun MealRow(meal: WidgetDataStore.MealRow) {
        val hasDish = meal.recipeName != null
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(vertical = 3.dp)
                .clickable(actionRunCallback<OpenAppRouteAction>(actionParametersOf(OpenAppRouteAction.ROUTE_KEY to "planner"))),
            verticalAlignment = Alignment.Vertical.CenterVertically,
        ) {
            CheckBox(
                checked = meal.eaten,
                onCheckedChange = if (hasDish) {
                    actionRunCallback<ToggleMealAction>(actionParametersOf(ToggleMealAction.CAT_ID_KEY to meal.catId))
                } else {
                    null
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = GlanceTheme.colors.primary,
                    uncheckedColor = GlanceTheme.colors.onSurfaceVariant,
                ),
            )
            Spacer(modifier = GlanceModifier.width(4.dp))
            Column(modifier = GlanceModifier.defaultWeight()) {
                Text(
                    meal.emoji + " " + meal.label,
                    style = TextStyle(fontSize = 10.sp, color = GlanceTheme.colors.onSurfaceVariant),
                    maxLines = 1,
                )
                Text(
                    meal.recipeName ?: "— brak dania —",
                    style = TextStyle(
                        fontSize = 13.sp,
                        color = if (hasDish) GlanceTheme.colors.onSurface else GlanceTheme.colors.onSurfaceVariant,
                        textDecoration = if (meal.eaten) TextDecoration.LineThrough else TextDecoration.None,
                    ),
                    maxLines = 1,
                )
            }
            if (hasDish) {
                Text(
                    "${meal.kcal} kcal",
                    style = TextStyle(fontSize = 11.sp, color = GlanceTheme.colors.onSurfaceVariant),
                    maxLines = 1,
                )
            }
        }
    }
}

class TodayMealsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TodayMealsWidget()
}
