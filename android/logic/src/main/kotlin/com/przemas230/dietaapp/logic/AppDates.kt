package com.przemas230.dietaapp.logic

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * FR-101 (ported to Android 2026-08-29): the single place that answers
 * "which calendar day is it?" for the whole app.
 *
 * Every date key in this app (`yyyy-MM-dd`) means the user's **local**
 * calendar day. Before this, Android explicitly forced `ZoneOffset.UTC` in
 * eight separate places (WaterViewModel, WeightViewModel,
 * EatenViewModel.todayUtc, PostepScreen ×2, WaterNotificationStore ×2,
 * ActivityLogOperations, CloudSyncCodec.todayUtcDateString) while
 * PlannerScreen used a plain, local `LocalDate.now()`. Poland is UTC+1/+2,
 * so between local midnight and 01:00/02:00 the app disagreed **with
 * itself**: the Planer had already rolled over to the new day while the
 * water and kcal counters were still writing into the previous one. That is
 * strictly worse than the web bug FR-101 originally fixed, where at least
 * every reader was wrong in the same direction.
 *
 * Deliberately NOT a `todayUtc()`-shaped replacement scattered around: the
 * whole point is that there is now one function, so the next person adding a
 * date-keyed feature can't quietly pick a different zone. Timestamp
 * SERIALISATION (CloudSyncCodec's ISO `...Z` strings, shared with
 * index.html's `toISOString()`) is a different thing and stays UTC — an
 * instant is an instant regardless of where it is read.
 */
object AppDates {
    /** Today in the device's own timezone. */
    fun today(): LocalDate = LocalDate.now(zone())

    /** Today as the `yyyy-MM-dd` key used by `eaten`, `waterHistory`, weights and the activity log. */
    fun todayKey(): String = today().toString()

    /** The local calendar day an instant falls on -- for reading back stored timestamps (cook history, activity log). */
    fun dateKey(epochMillis: Long): String = localDate(epochMillis).toString()

    fun localDate(epochMillis: Long): LocalDate =
        Instant.ofEpochMilli(epochMillis).atZone(zone()).toLocalDate()

    /**
     * Noon of the given local day, as an instant. Used when logging
     * something against a day that is not today (FR-104's day-card gesture)
     * -- midnight would sit right on the boundary and could read back as the
     * neighbouring day after any timezone change, noon never does.
     */
    fun noonEpochMillis(date: LocalDate): Long =
        date.atTime(12, 0).atZone(zone()).toInstant().toEpochMilli()

    private fun zone(): ZoneId = ZoneId.systemDefault()
}
