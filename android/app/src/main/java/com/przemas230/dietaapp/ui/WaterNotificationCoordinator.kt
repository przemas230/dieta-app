package com.przemas230.dietaapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.przemas230.dietaapp.data.WaterNotificationStore
import com.przemas230.dietaapp.notifications.WaterNotificationBridge

/**
 * FR-38/39: wires the tracker/reminder notifications to the rest of the app --
 * keeps the persistent tracker notification's count in sync with
 * [WaterViewModel] whenever water is logged from the header/Postęp tab
 * (mirrors index.html's `refreshWaterNotification()` calls after every
 * `renderHeaderWater`/`renderWater`), and applies changes made FROM the
 * notification (the +1/-1 buttons) back into [WaterViewModel] the moment
 * they happen via [WaterNotificationBridge] -- no polling needed, since a
 * BroadcastReceiver and this Composable share the same process. Renders
 * nothing; mounted once from DietaAppRoot alongside LocalPersistenceCoordinator.
 */
@Composable
fun WaterNotificationCoordinator(
    waterViewModel: WaterViewModel,
    notificationViewModel: WaterNotificationViewModel,
) {
    val context = LocalContext.current
    val waterCount by waterViewModel.count.collectAsState()
    val trackerEnabled by notificationViewModel.trackerEnabled.collectAsState()

    // One-time reconciliation on cold start: if the notification buttons
    // changed the count while this process wasn't alive to receive the live
    // callback below, WaterNotificationStore's copy is the freshest one.
    LaunchedEffect(Unit) {
        if (trackerEnabled) {
            val pending = WaterNotificationStore.readPendingWaterCount(context)
            if (pending != waterViewModel.count.value) waterViewModel.setCount(pending)
        }
    }

    DisposableEffect(Unit) {
        WaterNotificationBridge.onWaterCountChanged = { count -> waterViewModel.setCount(count) }
        WaterNotificationBridge.onReminderNextAtChanged = { nextAt -> notificationViewModel.onExternalNextAtChanged(nextAt) }
        onDispose {
            WaterNotificationBridge.onWaterCountChanged = null
            WaterNotificationBridge.onReminderNextAtChanged = null
        }
    }

    LaunchedEffect(waterCount, trackerEnabled) {
        notificationViewModel.syncTrackerNotification(waterCount)
    }
}
