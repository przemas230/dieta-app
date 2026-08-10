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
import com.przemas230.dietaapp.data.WeightEntry
import com.przemas230.dietaapp.logic.WaterOperations
import com.przemas230.dietaapp.logic.WeightOperations

/**
 * FR-37/FR-40/FR-60: the Postęp tab -- previously a bare PlaceholderScreen.
 * Covers the golden-rules card, a full-size water view (mirrors the header
 * strip, FR-70), and weight tracking with a simple line chart.
 *
 * Deliberately NOT covered yet (see android/PARITY.md for why each is a
 * separate, bigger piece of work): FR-38/39 (water reminder notifications --
 * needs Android notification channels/permissions), FR-41 (calorie history
 * chart + weekly balance -- needs a per-date eaten history Android doesn't
 * persist yet, only "today"), FR-42 (streaks + activity log -- same
 * per-date history gap, plus the web version's `addLog()` call is sprinkled
 * across dozens of mutation sites that would all need instrumenting).
 */
@Composable
fun PostepScreen(
    profileViewModel: ProfileViewModel,
    waterViewModel: WaterViewModel,
    weightViewModel: WeightViewModel,
) {
    val profile by profileViewModel.profile.collectAsState()
    val waterCount by waterViewModel.count.collectAsState()
    val weightEntries by weightViewModel.entries.collectAsState()

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

        WeightCard(entries = weightEntries, targetKg = profile.targetWeightKg, onAddWeight = weightViewModel::addWeight)
    }
}

/** "67.0" -> "67", "67.5" -> "67.5" — matches how JS template literals print numbers. */
private fun formatKg(value: Double): String =
    if (value == value.toLong().toDouble()) value.toLong().toString() else value.toString()

@Composable
private fun WeightCard(entries: List<WeightEntry>, targetKg: Double, onAddWeight: (Double) -> Boolean) {
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
