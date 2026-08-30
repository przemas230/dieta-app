package com.przemas230.dietaapp.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
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
import androidx.glance.text.TextStyle

/**
 * Requested 2026-08-30 ("chce kilka różnych do wyboru... ogólnie jak
 * najbardziej funkcjonalne"): widget 3 of 3 -- the smallest, most
 * glanceable of the three, for whoever wants one compact tile instead of
 * TodayMealsWidget's full checklist. Remaining kcal + a mini water strip
 * (same [SetWaterLevelAction] as WaterWidget -- one droplet-tap action,
 * shared) up top, three one-tap launchers to the tabs FR-118's app-icon
 * shortcuts already jump to (Zakupy/Spiżarnia/Postęp) along the bottom, so
 * this alone covers "glance at today's numbers" AND "jump straight to the
 * tab I actually need" without opening Planer first.
 */
class DashboardWidget : GlanceAppWidget() {
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
                .padding(10.dp),
        ) {
            Text(
                "${snapshot.remainingKcal} kcal",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = GlanceTheme.colors.primary),
            )
            Text(
                "pozostało dzisiaj",
                style = TextStyle(fontSize = 10.sp, color = GlanceTheme.colors.onSurfaceVariant),
            )
            Spacer(modifier = GlanceModifier.height(6.dp))
            Row(verticalAlignment = Alignment.Vertical.CenterVertically) {
                for (i in 0 until 8) {
                    val filled = i < snapshot.waterCount
                    Text(
                        if (filled) "💧" else "⚪",
                        style = TextStyle(fontSize = 13.sp),
                        modifier = GlanceModifier
                            .padding(horizontal = 1.dp)
                            .clickable(actionRunCallback<SetWaterLevelAction>(actionParametersOf(SetWaterLevelAction.DROPLET_INDEX_KEY to i))),
                    )
                }
            }
            Spacer(modifier = GlanceModifier.height(8.dp))
            Row(modifier = GlanceModifier.fillMaxWidth()) {
                QuickLaunchButton("🛒", "shopping")
                Spacer(modifier = GlanceModifier.width(10.dp))
                QuickLaunchButton("🏺", "pantry")
                Spacer(modifier = GlanceModifier.width(10.dp))
                QuickLaunchButton("📈", "progress")
            }
        }
    }

    @Composable
    private fun QuickLaunchButton(emoji: String, route: String) {
        Text(
            emoji,
            style = TextStyle(fontSize = 18.sp),
            modifier = GlanceModifier
                .background(GlanceTheme.colors.secondaryContainer)
                .padding(6.dp)
                .clickable(actionRunCallback<OpenAppRouteAction>(actionParametersOf(OpenAppRouteAction.ROUTE_KEY to route))),
        )
    }
}

class DashboardWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = DashboardWidget()
}
