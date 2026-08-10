package com.przemas230.dietaapp.notifications

/**
 * In-process callback hook so [WaterActionReceiver] can push a live update
 * straight into the running UI when the app is in the foreground, instead of
 * only ever syncing on resume. index.html needs `postMessage` for this
 * (service worker and page are separate JS contexts); a native
 * BroadcastReceiver runs in the same process as the rest of the app, so a
 * plain callback reference does the same job with no IPC. Set by
 * WaterNotificationViewModel while it's alive, cleared in onCleared() --
 * null (app backgrounded/killed) just means the receiver skips the callback
 * and relies on [com.przemas230.dietaapp.data.WaterNotificationStore] being
 * read on the next resume instead.
 */
object WaterNotificationBridge {
    var onWaterCountChanged: ((Int) -> Unit)? = null
    var onReminderNextAtChanged: ((Long?) -> Unit)? = null
}
