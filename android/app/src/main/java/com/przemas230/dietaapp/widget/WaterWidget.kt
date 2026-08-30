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
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.GlanceTheme
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

/**
 * Requested 2026-08-30 ("chce kilka różnych do wyboru żeby można było na
 * nich zaznaczać posiłki i wodę"): widget 2 of 3 -- a compact droplet strip,
 * same "tap a droplet to set that level, tap the CURRENT level to step back
 * one" interaction as the app header's own water row (WaterOperations.
 * tapDroplet), just reachable without opening the app at all. Deliberately
 * its own small widget (not folded only into DashboardWidget) since water
 * is logged far more often per day than any other single tracked thing --
 * it earns a dedicated, always-on-the-home-screen spot.
 */
class WaterWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val snapshot = WidgetDataStore.readSnapshot(context)
        provideContent {
            GlanceTheme {
                Content(snapshot.waterCount)
            }
        }
    }

    @Composable
    private fun Content(count: Int) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.widgetBackground)
                .padding(10.dp),
        ) {
            Text(
                "💧 Nawodnienie",
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GlanceTheme.colors.onSurface),
            )
            Spacer(modifier = GlanceModifier.height(4.dp))
            Row(verticalAlignment = Alignment.Vertical.CenterVertically) {
                for (i in 0 until 8) {
                    val filled = i < count
                    Text(
                        if (filled) "💧" else "⚪",
                        style = TextStyle(fontSize = 18.sp),
                        modifier = GlanceModifier
                            .padding(horizontal = 1.dp)
                            .clickable(actionRunCallback<SetWaterLevelAction>(actionParametersOf(SetWaterLevelAction.DROPLET_INDEX_KEY to i))),
                    )
                }
            }
            Spacer(modifier = GlanceModifier.height(2.dp))
            Text(
                "$count / 8 szklanek",
                style = TextStyle(fontSize = 11.sp, color = GlanceTheme.colors.onSurfaceVariant),
            )
        }
    }
}

class WaterWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WaterWidget()
}
