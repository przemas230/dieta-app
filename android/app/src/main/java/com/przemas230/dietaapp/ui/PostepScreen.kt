package com.przemas230.dietaapp.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import com.przemas230.dietaapp.data.ActivityLogEntry
import com.przemas230.dietaapp.data.WeightEntry
import com.przemas230.dietaapp.logic.ActivityLogOperations
import com.przemas230.dietaapp.logic.HistoryOperations
import com.przemas230.dietaapp.logic.ProfileCalculations
import com.przemas230.dietaapp.logic.WaterOperations
import com.przemas230.dietaapp.logic.WeightOperations
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * FR-37/FR-40/FR-41/FR-42/FR-60: the Postęp tab -- previously a bare
 * PlaceholderScreen. Covers the golden-rules card, streaks, a full-size
 * water view (mirrors the header strip, FR-70), weight tracking with a
 * simple line chart, and calorie history with a weekly balance.
 *
 * FR-41/42's history (kcal per day, water per day) only starts
 * accumulating from whenever this shipped -- EatenViewModel/WaterViewModel
 * record TODAY's totals into a date-keyed map on every change, but Android
 * never tracked earlier dates, so there's no retroactive data to show for
 * days before this feature existed. Same honest limitation as everything
 * else in this local-only version (see PARITY.md).
 *
 * Deliberately NOT covered yet: FR-38/39 (water reminder notifications --
 * needs Android notification channels/permissions).
 */
@Composable
fun PostepScreen(
    profileViewModel: ProfileViewModel,
    waterViewModel: WaterViewModel,
    weightViewModel: WeightViewModel,
    eatenViewModel: EatenViewModel,
    activityLogViewModel: ActivityLogViewModel,
) {
    val profile by profileViewModel.profile.collectAsState()
    val waterCount by waterViewModel.count.collectAsState()
    val weightEntries by weightViewModel.entries.collectAsState()
    val kcalHistory by eatenViewModel.kcalHistory.collectAsState()
    val waterHistory by waterViewModel.history.collectAsState()
    val activityLog by activityLogViewModel.entries.collectAsState()
    val today = remember { LocalDate.now(ZoneOffset.UTC) }
    val dailyTarget = remember(profile) { ProfileCalculations.calcTargets(profile).daily }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
    ) {
        // FR-60: only relevant when the user is actually following a strict
        // low-GI diet -- port of index.html's conditional rules-card.
        if (profile.strictLowGI) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        "⭐ Złote zasady przy Hashimoto i insulinooporności",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    listOf(
                        "Kolejność jedzenia: zawsze zaczynaj od warzyw i białka, węglowodany (kasze, chleb) na końcu.",
                        "Gotuj na 2 dni: zwiększaj porcje obiadowe x2 — oszczędzasz czas.",
                        "Bazuj na mrożonkach: warzywa na patelnię (bez sosów i ziemniaków) gotowe w 8 minut.",
                        "Nawodnienie: pij min. 2 litry wody dziennie, unikaj słodzonych napojów i soków.",
                        "Produkty bez laktozy: skyr, serki wiejskie bez laktozy lub napoje roślinne bez cukru.",
                    ).forEachIndexed { i, rule ->
                        Text("${i + 1}. $rule", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // FR-42: consecutive-day streaks -- port of index.html's calcKcalStreak/calcWaterStreak.
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("🔥 Serie", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                    val kcalStreak = remember(kcalHistory, dailyTarget, today) {
                        HistoryOperations.calcKcalStreak(kcalHistory, dailyTarget, today)
                    }
                    val waterStreak = remember(waterHistory, today) {
                        HistoryOperations.calcWaterStreak(waterHistory, today)
                    }
                    StreakTile(kcalStreak, "🔥 dni w celu kalorycznym", Modifier.weight(1f))
                    StreakTile(waterStreak, "💧 dni z pełnym nawodnieniem", Modifier.weight(1f))
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("💧 Nawodnienie dzisiaj", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    for (i in 0 until WaterOperations.MAX_LEVEL) {
                        Text(
                            if (i < waterCount) "💧" else "⚪",
                            fontSize = 26.sp,
                            modifier = Modifier.clickable { waterViewModel.tapDroplet(i) },
                        )
                    }
                }
                Text(
                    "$waterCount/${WaterOperations.MAX_LEVEL} szklanek",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        WeightCard(
            entries = weightEntries,
            targetKg = profile.targetWeightKg,
            onAddWeight = weightViewModel::addWeight,
            onEditWeight = weightViewModel::editWeight,
            onRemoveWeight = weightViewModel::removeWeight,
        )
        Spacer(modifier = Modifier.height(12.dp))

        KcalHistoryCard(kcalHistory = kcalHistory, dailyTarget = dailyTarget, today = today)
        Spacer(modifier = Modifier.height(12.dp))

        ActivityHistoryCard(entries = activityLog, onClear = activityLogViewModel::clear)
    }
}

/** FR-42: raw log of pantry/shopping mutations -- port of index.html's #historyList (Od/Do date filter, 20-entry default cap with "pokaż całą historię" toggle, "Wyczyść" with confirm). */
@Composable
private fun ActivityHistoryCard(entries: List<ActivityLogEntry>, onClear: () -> Unit) {
    var fromDate by remember { mutableStateOf("") }
    var toDate by remember { mutableStateOf("") }
    var showAll by remember { mutableStateOf(false) }
    var showClearConfirm by remember { mutableStateOf(false) }
    val filtering = fromDate.isNotBlank() || toDate.isNotBlank()
    val filtered = remember(entries, fromDate, toDate) {
        ActivityLogOperations.filterByDateRange(entries, fromDate.ifBlank { null }, toDate.ifBlank { null })
    }
    val visible = if (!filtering && !showAll) filtered.take(20) else filtered
    val formatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm") }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("📜 Historia aktywności", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                TextButton(onClick = { showClearConfirm = true }) { Text("Wyczyść") }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = fromDate,
                    onValueChange = { fromDate = it },
                    label = { Text("Od (RRRR-MM-DD)") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                OutlinedTextField(
                    value = toDate,
                    onValueChange = { toDate = it },
                    label = { Text("Do (RRRR-MM-DD)") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
            }
            if (filtering) {
                Text(
                    "Wyczyść filtr, aby zobaczyć 20 ostatnich wpisów.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .clickable { fromDate = ""; toDate = "" },
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            when {
                entries.isEmpty() -> Text(
                    "Historia jest pusta — pojawią się tu dodania/usunięcia z listy zakupów i spiżarni.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                filtered.isEmpty() -> Text(
                    "Brak wpisów w wybranym zakresie dat.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                else -> {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        visible.forEach { entry ->
                            val dt = remember(entry.tsEpochMillis) {
                                Instant.ofEpochMilli(entry.tsEpochMillis).atZone(ZoneOffset.UTC).format(formatter)
                            }
                            Column {
                                Text(dt, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(entry.detail, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                    if (!filtering && filtered.size > 20) {
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = { showAll = !showAll }, modifier = Modifier.fillMaxWidth()) {
                            Text(if (showAll) "Pokaż tylko 20 ostatnich" else "Pokaż całą historię (${filtered.size})")
                        }
                    }
                }
            }
        }
    }

    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            title = { Text("Wyczyścić całą historię aktywności?") },
            confirmButton = {
                TextButton(onClick = { onClear(); showClearConfirm = false }) { Text("Wyczyść") }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) { Text("Anuluj") }
            },
        )
    }
}

@Composable
private fun StreakTile(days: Int, label: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(days.toString(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/** FR-41: last 14 days of recorded kcal as a simple bar chart, plus the last-7-days-vs-target balance -- port of index.html's kcalHistoryChartCanvas/kcalWeeklyBalance. */
@Composable
private fun KcalHistoryCard(kcalHistory: Map<String, Int>, dailyTarget: Int, today: LocalDate) {
    val days = remember(kcalHistory, today) { HistoryOperations.lastNDays(kcalHistory, today, 14) }
    val balance = remember(kcalHistory, dailyTarget, today) { HistoryOperations.weeklyBalance(kcalHistory, dailyTarget, today) }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text("📈 Historia kalorii", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(10.dp))
            KcalHistoryChart(days, dailyTarget)
            Spacer(modifier = Modifier.height(8.dp))
            val sign = if (balance.diff > 0) "+" else ""
            Text(
                "Bilans ostatnich 7 dni: ${balance.totalKcal} / ${balance.targetKcal} kcal ($sign${balance.diff} kcal). " +
                    "Przerywana linia na wykresie to Twój dzienny cel.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** Simple bar chart (Canvas, same "no charting library" approach as WeightChart) plus a dashed line marking the daily target. */
@Composable
private fun KcalHistoryChart(days: List<Pair<String, Int>>, dailyTarget: Int) {
    val barColor = MaterialTheme.colorScheme.primary
    val targetLineColor = MaterialTheme.colorScheme.error
    Canvas(modifier = Modifier.fillMaxWidth().height(140.dp)) {
        if (days.isEmpty()) return@Canvas
        val paddingPx = 8.dp.toPx()
        val plotWidth = size.width - 2 * paddingPx
        val plotHeight = size.height - 2 * paddingPx
        val maxKcal = maxOf(days.maxOf { it.second }, dailyTarget, 1)
        val barWidth = plotWidth / days.size
        days.forEachIndexed { index, (_, kcal) ->
            val barHeight = (kcal.toFloat() / maxKcal) * plotHeight
            val x = paddingPx + index * barWidth
            drawRect(
                barColor,
                topLeft = Offset(x + barWidth * 0.15f, paddingPx + plotHeight - barHeight),
                size = androidx.compose.ui.geometry.Size(barWidth * 0.7f, barHeight),
            )
        }
        val targetY = paddingPx + plotHeight - (dailyTarget.toFloat() / maxKcal) * plotHeight
        drawLine(
            targetLineColor,
            Offset(paddingPx, targetY),
            Offset(size.width - paddingPx, targetY),
            strokeWidth = 2.dp.toPx(),
            pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 8f)),
        )
    }
}

/** "67.0" -> "67", "67.5" -> "67.5" — matches how JS template literals print numbers. */
private fun formatKg(value: Double): String =
    if (value == value.toLong().toDouble()) value.toLong().toString() else value.toString()

@Composable
private fun WeightCard(
    entries: List<WeightEntry>,
    targetKg: Double,
    onAddWeight: (Double) -> Boolean,
    onEditWeight: (String, Double) -> Boolean,
    onRemoveWeight: (String) -> Unit,
) {
    var input by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }
    val sorted = remember(entries) { WeightOperations.sortedByDate(entries) }
    val toGo = remember(entries, targetKg) { WeightOperations.kgToGo(entries, targetKg) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                "⚖️ Postęp wagi (cel: ${formatKg(targetKg)} kg)",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it; error = false },
                    label = { Text("Dzisiejsza waga, np. 66.4") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    val kg = input.trim().replace(',', '.').toDoubleOrNull()
                    if (kg != null && onAddWeight(kg)) {
                        input = ""
                        error = false
                    } else {
                        error = true
                    }
                }) { Text("Dodaj") }
            }
            if (error) {
                Text(
                    "Podaj prawidłową wagę (30-250 kg)",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            if (sorted.isEmpty()) {
                Text(
                    "Brak wpisów — dodaj pierwszą wagę powyżej.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 10.dp),
                )
            } else {
                Spacer(modifier = Modifier.height(10.dp))
                WeightChart(sorted.takeLast(30))
                Spacer(modifier = Modifier.height(8.dp))
                val info = if (toGo != null && toGo > 0) {
                    "Zostało ${formatKg(toGo)} kg do celu (${formatKg(targetKg)} kg)."
                } else {
                    "Cel osiągnięty! 🎉"
                }
                Text(info, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    sorted.asReversed().take(15).forEach { entry ->
                        WeightEntryRow(entry, onEditWeight, onRemoveWeight)
                    }
                }
            }
        }
    }
}

/** On explicit user request ("dodaj mozliwośc edytowania postępu wagi jak się wpisze zła wage to daj jakieś okno edycjy") -- lets a wrongly-typed past entry be corrected or removed, not just appended-over. Port of index.html's inline weight-edit-list rows. */
@Composable
private fun WeightEntryRow(entry: WeightEntry, onEditWeight: (String, Double) -> Boolean, onRemoveWeight: (String) -> Unit) {
    var editing by remember(entry.dateStr) { mutableStateOf(false) }
    var showDeleteConfirm by remember(entry.dateStr) { mutableStateOf(false) }
    val dateLabel = remember(entry.dateStr) {
        runCatching { LocalDate.parse(entry.dateStr).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) }.getOrDefault(entry.dateStr)
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Usunąć wpis wagi?") },
            text = { Text("Usunąć wpis wagi z $dateLabel?") },
            confirmButton = { TextButton(onClick = { onRemoveWeight(entry.dateStr); showDeleteConfirm = false }) { Text("Usuń") } },
            dismissButton = { TextButton(onClick = { showDeleteConfirm = false }) { Text("Anuluj") } },
        )
    }

    if (editing) {
        var editInput by remember(entry.dateStr) { mutableStateOf(formatKg(entry.kg)) }
        var editError by remember(entry.dateStr) { mutableStateOf(false) }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
            Text("$dateLabel:", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(end = 6.dp))
            OutlinedTextField(
                value = editInput,
                onValueChange = { editInput = it; editError = false },
                singleLine = true,
                modifier = Modifier.width(90.dp),
            )
            TextButton(onClick = {
                val kg = editInput.trim().replace(',', '.').toDoubleOrNull()
                if (kg != null && onEditWeight(entry.dateStr, kg)) editing = false else editError = true
            }) { Text("Zapisz") }
            TextButton(onClick = { editing = false }) { Text("Anuluj") }
        }
        if (editError) {
            Text(
                "Podaj prawidłową wagę (30-250 kg)",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        ) {
            Text("$dateLabel: ${formatKg(entry.kg)} kg", style = MaterialTheme.typography.bodySmall)
            Row {
                TextButton(onClick = { editing = true }) { Text("✏️") }
                TextButton(onClick = { showDeleteConfirm = true }) { Text("🗑") }
            }
        }
    }
}

/**
 * Deliberately simple hand-rolled line chart (Canvas, no charting library --
 * matches this codebase's existing preference for zero extra Gradle
 * dependencies where a small custom implementation suffices, e.g. FR-64's
 * micronutrient chips). Teal line through every point, the most recent
 * point highlighted in the secondary color, same visual intent as
 * index.html's Chart.js line (just without the gradient fill/axis labels).
 */
@Composable
private fun WeightChart(entries: List<WeightEntry>) {
    val lineColor = MaterialTheme.colorScheme.primary
    val lastPointColor = MaterialTheme.colorScheme.secondary
    Canvas(modifier = Modifier.fillMaxWidth().height(160.dp)) {
        if (entries.size < 2) {
            // A single point still deserves a visible dot, just no line to draw.
            if (entries.size == 1) {
                drawCircle(lastPointColor, radius = 6.dp.toPx(), center = Offset(size.width / 2f, size.height / 2f))
            }
            return@Canvas
        }
        val minKg = entries.minOf { it.kg }
        val maxKg = entries.maxOf { it.kg }
        val range = (maxKg - minKg).takeIf { it > 0.01 } ?: 1.0
        val paddingPx = 12.dp.toPx()
        val plotWidth = size.width - 2 * paddingPx
        val plotHeight = size.height - 2 * paddingPx
        val points = entries.mapIndexed { index, entry ->
            val x = paddingPx + (index.toFloat() / (entries.size - 1)) * plotWidth
            val y = paddingPx + (1f - ((entry.kg - minKg) / range).toFloat()) * plotHeight
            Offset(x, y)
        }
        for (i in 0 until points.size - 1) {
            drawLine(lineColor, points[i], points[i + 1], strokeWidth = 3.dp.toPx(), cap = StrokeCap.Round)
        }
        points.dropLast(1).forEach { drawCircle(lineColor, radius = 3.dp.toPx(), center = it) }
        drawCircle(lastPointColor, radius = 6.dp.toPx(), center = points.last())
    }
}
