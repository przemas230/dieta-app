package com.przemas230.dietaapp.logic

import com.przemas230.dietaapp.data.ActivityLogEntry

/** FR-42: pure port of index.html's addLog() -- newest first, capped at 200 entries. */
object ActivityLogOperations {
    const val MAX_ENTRIES = 200

    fun addEntry(entries: List<ActivityLogEntry>, action: String, detail: String, nowEpochMillis: Long): List<ActivityLogEntry> =
        (listOf(ActivityLogEntry(nowEpochMillis, action, detail)) + entries).take(MAX_ENTRIES)

    /** FR-42: date-range filter for the "Historia aktywności" card ("Od"/"Do" inputs) -- both bounds inclusive, either/both may be null (no filter). */
    fun filterByDateRange(entries: List<ActivityLogEntry>, fromDateStr: String?, toDateStr: String?): List<ActivityLogEntry> {
        if (fromDateStr.isNullOrEmpty() && toDateStr.isNullOrEmpty()) return entries
        return entries.filter { entry ->
            val entryDate = AppDates.dateKey(entry.tsEpochMillis)
            val afterFrom = fromDateStr.isNullOrEmpty() || entryDate >= fromDateStr
            val beforeTo = toDateStr.isNullOrEmpty() || entryDate <= toDateStr
            afterFrom && beforeTo
        }
    }
}
