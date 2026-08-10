package com.przemas230.dietaapp.data

/** FR-42: one row of the "Historia aktywności" diagnostic log -- mirrors index.html's addLog()'s `{ts, action, detail}`. */
data class ActivityLogEntry(val tsEpochMillis: Long, val action: String, val detail: String)
