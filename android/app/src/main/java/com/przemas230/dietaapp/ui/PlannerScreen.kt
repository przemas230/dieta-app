package com.przemas230.dietaapp.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.przemas230.dietaapp.data.EatenEntry
import com.przemas230.dietaapp.data.PlannedMeal
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.Snack
import com.przemas230.dietaapp.logic.AppThemes
import com.przemas230.dietaapp.logic.EatenOperations
import com.przemas230.dietaapp.logic.MacroGrams
import com.przemas230.dietaapp.logic.PlannerCategory
import com.przemas230.dietaapp.logic.PlannerOperations
import com.przemas230.dietaapp.logic.ProfileCalculations
import com.przemas230.dietaapp.logic.RecipeMatching
import com.przemas230.dietaapp.logic.WaterOperations
import com.przemas230.dietaapp.logic.WeekPlan
import com.przemas230.dietaapp.logic.forCategory
import com.przemas230.dietaapp.ui.theme.LocalDietaThemeId
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * FR-18/20/21/22/23/24: 7 day cards, each with the 5 meal-slot rows from
 * PlannerOperations.PLANNER_CATEGORIES, a portion-scale chip per filled slot
 * (FR-20), per-slot/per-day/whole-week random generation (FR-21), per-day
 * clearing (FR-22), "ugotuj na 2 dni" leftovers (FR-23), the proactive
 * next-day carry-over suggestion (FR-24), and a per-day "add this day's
 * ingredients to the shopping list" button (the Zakupy tab's own whole-week
 * version of this is FR-27, see ShoppingScreen.kt).
 */
@Composable
fun PlannerScreen(
    plannerViewModel: PlannerViewModel,
    profileViewModel: ProfileViewModel,
    shoppingViewModel: ShoppingViewModel,
    // FR-87/v7: the rest of these params feed ONLY the Klinika-only
    // PlannerDashboard below -- they're the exact same data/callbacks
    // MainActivity's global-header HeaderKcalPanel used to receive
    // directly (that panel no longer renders at all for Klinika, see
    // MainActivity.kt's isClinicHeader gate), just routed here instead.
    // The other 11 themes' DayCard/DayCardClinic loop below is completely
    // unaffected either way.
    eatenEntries: Map<String, EatenEntry> = emptyMap(),
    snacks: List<Snack> = emptyList(),
    displayName: String = "",
    onToggleEaten: (cat: String, plannedKcal: Int?, plannedName: String?) -> Unit = { _, _, _ -> },
    waterCount: Int = 0,
    onSignOut: () -> Unit = {},
    onWaterTap: (Int) -> Unit = {},
    onWaterSetCount: (Int) -> Unit = {},
) {
    val allRecipes by plannerViewModel.allRecipes.collectAsState()
    val weekPlan by plannerViewModel.weekPlan.collectAsState()
    val profile by profileViewModel.profile.collectAsState()

    if (allRecipes.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Wczytywanie przepisów…")
        }
        return
    }

    val recipesById = remember(allRecipes) { allRecipes.associateBy { it.id } }
    val kcalTargets = remember(profile) { ProfileCalculations.calcTargets(profile) }
    val macroTargets = remember(profile) { ProfileCalculations.calcMacroTargets(profile) }

    var slotPicker by remember { mutableStateOf<Pair<Int, String>?>(null) }
    var pendingConfirm by remember { mutableStateOf<PendingConfirm?>(null) }
    // 2026-08-11 (user request, "dodaj możliwość podglądnięcia przepisu z
    // poziomu planera"): (recipe, portion scale) for the currently open
    // preview, see RecipePreviewDialog. Scale is carried alongside the
    // recipe so ingredients/kcal shown match the ACTUAL planned portion,
    // not the recipe's base 1x amounts.
    var previewRecipe by remember { mutableStateOf<Pair<Recipe, Double>?>(null) }

    // FR-87: motyw "Klinika" dostaje bento-uklad -- nagrodkowy pasek celu
    // kcal/makro (z danych juz wyliczonych wyzej, zero nowych wywolan
    // ViewModel/logiki) i przeprojektowane karty dni. Reszta motywow ma
    // dokladnie ten sam DayCard co dotychczas.
    val isClinic = AppThemes.isClinicFamily(LocalDietaThemeId.current)
    val todayIndex = remember { (LocalDate.now().dayOfWeek.value - 1).coerceIn(0, 6) }

    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (isClinic) {
            item {
                PlannerDashboard(
                    displayName = displayName,
                    kcalTarget = kcalTargets.daily,
                    todayMeals = weekPlan[todayIndex].orEmpty(),
                    recipesById = recipesById,
                    eatenEntries = eatenEntries,
                    snacks = snacks,
                    waterCount = waterCount,
                    todayIndex = todayIndex,
                    onToggleEaten = onToggleEaten,
                    onClearSlot = { cat -> plannerViewModel.clearSlot(todayIndex, cat) },
                    onSlotClick = { cat -> slotPicker = todayIndex to cat },
                    onSignOut = onSignOut,
                    onWaterTap = onWaterTap,
                    onWaterSetCount = onWaterSetCount,
                )
            }
            item {
                DailyTargetBento(kcalTarget = kcalTargets.daily, macros = macroTargets.daily)
            }
        }
        // Requested 2026-08-25 (Web FR-87/v9, ported here): moved below the
        // day-card list instead of above it -- Klinika/Klinika (noc) only,
        // the other 11 themes keep it exactly where it always was.
        if (!isClinic) {
            item { AutoPlanWeekButton(onClick = {
                pendingConfirm = PendingConfirm(
                    "To nadpisze wszystkie dania zaplanowane w całym tygodniu. Na pewno chcesz wygenerować nowy plan?",
                ) { plannerViewModel.randomizeWeek(profile) }
            }) }
        }
        itemsIndexed(PlannerOperations.DAYS_PL) { day, dayName ->
            val slotClick: (String) -> Unit = { cat -> slotPicker = day to cat }
            val scaleClick: (String, Double) -> Unit = { cat, currentScale ->
                plannerViewModel.setScale(day, cat, PlannerOperations.nextScaleStep(currentScale))
            }
            val regenerateSlot: (String) -> Unit = { cat -> plannerViewModel.regenerateSlot(day, cat, profile) }
            val previewClick: (Recipe, Double) -> Unit = { recipe, scale -> previewRecipe = recipe to scale }
            val prepAheadFor: (String) -> Recipe? = { cat -> PlannerOperations.prepAheadSuggestion(weekPlan, day, cat, recipesById) }
            val applyPrepAhead: (String, String) -> Unit = { cat, recipeId -> plannerViewModel.planLeftover(day, cat, recipeId) }
            val randomizeDay: () -> Unit = {
                pendingConfirm = PendingConfirm(
                    "Wygenerować losowo cały dzień „$dayName”? Nadpisze wybrane tam dania.",
                ) { plannerViewModel.randomizeDay(day, profile) }
            }
            val clearDay: () -> Unit = {
                pendingConfirm = PendingConfirm(
                    "Wyczyścić wszystkie dania zaplanowane na „$dayName”?",
                ) { plannerViewModel.clearDay(day) }
            }
            val addDayToShopping: () -> Unit = { shoppingViewModel.addDayPlan(weekPlan[day].orEmpty(), recipesById) }

            if (isClinic) {
                DayCardClinic(
                    day = day,
                    dayName = dayName,
                    isToday = day == todayIndex,
                    dayMeals = weekPlan[day].orEmpty(),
                    recipesById = recipesById,
                    totalKcal = PlannerOperations.dayTotalKcal(weekPlan, day, recipesById),
                    kcalTarget = kcalTargets.daily,
                    onSlotClick = slotClick,
                    onScaleClick = scaleClick,
                    onRegenerateSlot = regenerateSlot,
                    onPreviewClick = previewClick,
                    prepAheadFor = prepAheadFor,
                    onApplyPrepAhead = applyPrepAhead,
                    onRandomizeDay = randomizeDay,
                    onClearDay = clearDay,
                    onAddDayToShopping = addDayToShopping,
                )
            } else {
                DayCard(
                    day = day,
                    dayName = dayName,
                    dayMeals = weekPlan[day].orEmpty(),
                    recipesById = recipesById,
                    totalKcal = PlannerOperations.dayTotalKcal(weekPlan, day, recipesById),
                    onSlotClick = slotClick,
                    onScaleClick = scaleClick,
                    onRegenerateSlot = regenerateSlot,
                    onPreviewClick = previewClick,
                    prepAheadFor = prepAheadFor,
                    onApplyPrepAhead = applyPrepAhead,
                    onRandomizeDay = randomizeDay,
                    onClearDay = clearDay,
                    onAddDayToShopping = addDayToShopping,
                )
            }
        }
        if (isClinic) {
            item { AutoPlanWeekButton(onClick = {
                pendingConfirm = PendingConfirm(
                    "To nadpisze wszystkie dania zaplanowane w całym tygodniu. Na pewno chcesz wygenerować nowy plan?",
                ) { plannerViewModel.randomizeWeek(profile) }
            }) }
        }
    }

    val picker = slotPicker
    if (picker != null) {
        val (day, cat) = picker
        PlannerSlotDialog(
            day = day,
            category = PlannerOperations.PLANNER_CATEGORIES.first { it.id == cat },
            currentMeal = weekPlan[day]?.get(cat),
            weekPlan = weekPlan,
            recipes = allRecipes.filter { it.cat == cat },
            profile = profile,
            targetGrams = macroTargets.forCategory(cat),
            onPick = { recipe ->
                val scale = PlannerOperations.idealScaleFor(recipe, kcalTargets.forCategory(cat))
                plannerViewModel.setMeal(day, cat, recipe.id, scale)
                slotPicker = null
            },
            onClear = {
                plannerViewModel.clearSlot(day, cat)
                slotPicker = null
            },
            onPlanLeftover = { targetDay, recipeId ->
                plannerViewModel.planLeftover(targetDay, cat, recipeId)
            },
            onDismiss = { slotPicker = null },
        )
    }

    previewRecipe?.let { (recipe, scale) ->
        RecipePreviewDialog(recipe = recipe, scale = scale, onDismiss = { previewRecipe = null })
    }

    val confirm = pendingConfirm
    if (confirm != null) {
        AlertDialog(
            onDismissRequest = { pendingConfirm = null },
            title = { Text("Potwierdź") },
            text = { Text(confirm.message) },
            confirmButton = {
                TextButton(onClick = {
                    confirm.action()
                    pendingConfirm = null
                }) { Text("Tak") }
            },
            dismissButton = {
                TextButton(onClick = { pendingConfirm = null }) { Text("Anuluj") }
            },
        )
    }
}

private class PendingConfirm(val message: String, val action: () -> Unit)

// FR-21: whole-week random generation, always requires confirmation since it
// overwrites every day. Extracted (was inline) since PlannerScreen now calls
// this from two different LazyColumn positions depending on isClinic.
@Composable
private fun AutoPlanWeekButton(onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text("🎲 Wygeneruj losowo cały tydzień")
    }
}

@Composable
private fun DayCard(
    day: Int,
    dayName: String,
    dayMeals: Map<String, PlannedMeal>,
    recipesById: Map<String, Recipe>,
    totalKcal: Int,
    onSlotClick: (cat: String) -> Unit,
    onScaleClick: (cat: String, currentScale: Double) -> Unit,
    onRegenerateSlot: (cat: String) -> Unit,
    onPreviewClick: (recipe: Recipe, scale: Double) -> Unit,
    prepAheadFor: (cat: String) -> Recipe?,
    onApplyPrepAhead: (cat: String, recipeId: String) -> Unit,
    onRandomizeDay: () -> Unit,
    onClearDay: () -> Unit,
    onAddDayToShopping: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(dayName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            PlannerOperations.PLANNER_CATEGORIES.forEach { category ->
                val meal = dayMeals[category.id]
                val recipe = meal?.let { recipesById[it.recipeId] }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "${category.emoji} ${category.label}",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.widthIn(min = 100.dp),
                    )
                    val label = when {
                        recipe == null -> "— wybierz danie —"
                        meal!!.isLeftover -> "🍱 ${recipe.name}"
                        else -> recipe.name
                    }
                    OutlinedButton(
                        onClick = { onSlotClick(category.id) },
                        modifier = Modifier.weight(1f).heightIn(min = 38.dp),
                    ) {
                        Text(label, maxLines = 1)
                    }
                    if (recipe != null && meal != null) {
                        Spacer(modifier = Modifier.width(4.dp))
                        // 2026-08-11 (user request): preview ingredients/
                        // method without leaving the planner or changing
                        // the slot -- separate from onSlotClick (which
                        // opens the CHANGE-recipe picker) so tapping either
                        // does exactly one clearly distinct thing.
                        TextButton(onClick = { onPreviewClick(recipe, meal.scale) }) {
                            Text("👁️")
                        }
                        TextButton(onClick = { onScaleClick(category.id, meal.scale) }) {
                            Text(formatScale(meal.scale))
                        }
                        TextButton(onClick = { onRegenerateSlot(category.id) }) {
                            Text("🔁")
                        }
                    }
                }
                if (recipe == null) {
                    val suggestion = prepAheadFor(category.id)
                    if (suggestion != null) {
                        val prevDayName = PlannerOperations.DAYS_PL[(day + 6) % 7]
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(start = 4.dp, bottom = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                "🍱 Ugotowano na więcej dni w $prevDayName: ${suggestion.name} — powtórzyć jako resztki?",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f),
                            )
                            TextButton(onClick = { onApplyPrepAhead(category.id, suggestion.id) }) {
                                Text("Tak, powtórz")
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Razem: $totalKcal kcal",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedButton(onClick = onAddDayToShopping, modifier = Modifier.fillMaxWidth()) {
                Text("🛒 Dodaj składniki z tego dnia do listy zakupów")
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onRandomizeDay, modifier = Modifier.weight(1f)) {
                    Text("🎲 Losuj ten dzień")
                }
                TextButton(onClick = onClearDay, modifier = Modifier.weight(1f)) {
                    Text("🗑️ Wyczyść ten dzień")
                }
            }
        }
    }
}

/**
 * FR-87/v7: Klinika-only dashboard at the top of the Planer tab -- port of
 * index.html's renderPlannerDashboard() (same data shape: todaysPlannerMeals
 * -equivalent map, EatenOperations, WaterOperations -- only the layout
 * differs). Greeting/date/sign-out, a CEL/POZOSTAŁO(ring)/WODA card row (the
 * ring replaces the kcal ring the global header used to show, see
 * MainActivity.kt's isClinicHeader gate on HeaderKcalPanel), a decorative
 * "today first, today highlighted" day strip (does NOT switch which day's
 * meals show below -- confirmed with the user before building this, see
 * FR-87.md v7), and "Dzisiejszy Planer" cards for today's 5 meal slots (tap
 * toggles eaten same as the old header's swipe-to-eat rows, × clears the
 * slot same as the picker dialog's "— brak / wyczyść —" row, empty slots
 * are a dashed "+ [category]" placeholder opening that same picker).
 */
@Composable
private fun PlannerDashboard(
    displayName: String,
    kcalTarget: Int,
    todayMeals: Map<String, PlannedMeal>,
    recipesById: Map<String, Recipe>,
    eatenEntries: Map<String, EatenEntry>,
    snacks: List<Snack>,
    waterCount: Int,
    todayIndex: Int,
    onToggleEaten: (cat: String, plannedKcal: Int?, plannedName: String?) -> Unit,
    onClearSlot: (cat: String) -> Unit,
    onSlotClick: (cat: String) -> Unit,
    onSignOut: () -> Unit,
    onWaterTap: (Int) -> Unit = {},
    onWaterSetCount: (Int) -> Unit = {},
) {
    val eatenKcal = EatenOperations.dailyEatenKcal(eatenEntries) + EatenOperations.snacksKcal(snacks)
    val remaining = (kcalTarget - eatenKcal).coerceAtLeast(0)
    val kcalPct = if (kcalTarget > 0) (eatenKcal.toFloat() / kcalTarget).coerceIn(0f, 1f) else 0f
    val dateLabel = remember {
        val raw = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale("pl")))
        raw.replaceFirstChar { it.uppercase() }
    }
    // Requested 2026-08-25 (Web FR-87/v9, ported here): tapping the Woda
    // card used to do nothing -- opens the same tap-a-circle picker
    // already used elsewhere (PostepScreen's Klinika water card), see the
    // dialog at the end of this composable.
    var showWaterPicker by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Requested 2026-08-25 (Web FR-87/v9, ported here): the sign-out
        // shortcut duplicated the one already in Ustawienia → Konto and was
        // reported as unwanted on the main screen -- dropped from this Row
        // (onSignOut is kept as a param, unused for now, rather than
        // threading a removal through every call site, in case this is
        // ever reversed the way the web header text was).
        Column {
            Text(dateLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                if (displayName.isNotBlank()) "Cześć, $displayName!" else "Cześć!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Requested 2026-08-25 (Web FR-87/v9, ported here): the "Cel"
            // card was dropped -- the remaining-kcal card below stretches
            // left to fill that space instead of leaving a gap (weight 2.3
            // vs the old Cel(1)+this(1.5)=2.5 combined, matching the web
            // CSS's 2.3fr/1fr split closely enough).
            Card(
                modifier = Modifier.weight(2.3f),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(48.dp)) {
                        CircularProgressIndicator(
                            progress = { 1f },
                            modifier = Modifier.fillMaxSize(),
                            strokeWidth = 3.dp,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                        )
                        CircularProgressIndicator(
                            progress = { kcalPct },
                            modifier = Modifier.fillMaxSize(),
                            strokeWidth = 3.dp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text("$eatenKcal/$kcalTarget", style = MaterialTheme.typography.labelSmall, fontSize = 9.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                    ) {
                        Text(
                            "POZOSTAŁO",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            "$remaining kcal",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
            DashboardStatCard(
                label = "Woda",
                value = "$waterCount/${WaterOperations.MAX_LEVEL}",
                unit = "szklanek",
                modifier = Modifier.weight(1f),
                onClick = { showWaterPicker = true },
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            for (i in 0 until 7) {
                val di = (todayIndex + i) % 7
                val isToday = i == 0
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surface)
                        .then(
                            if (isToday) {
                                Modifier.border(1.5.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.medium)
                            } else {
                                Modifier
                            },
                        )
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                ) {
                    // Requested 2026-08-25 (Web FR-87/v9, ported here):
                    // today's chip spells out the full day name instead of
                    // the same 2-letter abbreviation as every other day --
                    // the border above already marked it, this makes the
                    // label itself say so too.
                    Text(
                        if (isToday) PlannerOperations.DAYS_PL[di] else PlannerOperations.DAYS_PL[di].take(2),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium,
                        color = if (isToday) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text("Dzisiejszy Planer", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        PlannerOperations.PLANNER_CATEGORIES.forEachIndexed { index, category ->
            if (index > 0) Spacer(modifier = Modifier.height(6.dp))
            val meal = todayMeals[category.id]
            val recipe = meal?.let { recipesById[it.recipeId] }
            if (recipe == null || meal == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .dashedBorder(MaterialTheme.colorScheme.outlineVariant, 16.dp)
                        .clickable { onSlotClick(category.id) }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "+ ${category.label}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                val kcal = PlannerOperations.scaledKcal(recipe, meal.scale)
                val eaten = EatenOperations.isEaten(eatenEntries, category.id)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleEaten(category.id, kcal, recipe.name) },
                    shape = MaterialTheme.shapes.large,
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(category.emoji, fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                category.label.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                recipe.name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textDecoration = if (eaten) TextDecoration.LineThrough else TextDecoration.None,
                            )
                        }
                        Text(
                            "$kcal kcal",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        IconButton(onClick = { onClearSlot(category.id) }, modifier = Modifier.size(32.dp)) {
                            Text("✕", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
    if (showWaterPicker) {
        // Requested 2026-08-25 (Web FR-87/v9, ported here): same tap-a-
        // circle picker as PostepScreen's Klinika water card (156-173),
        // in a dialog instead of inline, so water can be logged from
        // Planer without switching tabs.
        Dialog(onDismissRequest = { showWaterPicker = false }) {
            Card(shape = MaterialTheme.shapes.large) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("💧 Ile szklanek wody?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextButton(onClick = { onWaterSetCount((waterCount - 1).coerceAtLeast(0)) }) { Text("–") }
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(horizontal = 4.dp)) {
                            for (i in 0 until WaterOperations.MAX_LEVEL) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (i < waterCount) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                        )
                                        .clickable { onWaterTap(i) },
                                )
                            }
                        }
                        TextButton(onClick = { onWaterSetCount((waterCount + 1).coerceAtMost(WaterOperations.MAX_LEVEL)) }) { Text("+") }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "$waterCount/${WaterOperations.MAX_LEVEL} szklanek",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardStatCard(label: String, value: String, unit: String, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    Card(
        modifier = if (onClick != null) modifier.clickable(onClick = onClick) else modifier,
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
            )
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(unit, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/** Dashed rounded-rect outline -- Modifier.border only draws solid lines, so the empty-slot placeholder (visually distinct "tap to add" affordance) needs its own draw pass. */
private fun Modifier.dashedBorder(color: androidx.compose.ui.graphics.Color, cornerRadiusDp: androidx.compose.ui.unit.Dp) = this.drawWithContent {
    drawContent()
    drawRoundRect(
        color = color,
        style = Stroke(width = 1.5.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f)),
        cornerRadius = CornerRadius(cornerRadiusDp.toPx()),
    )
}

/** FR-87: bento pasek celu dnia dla motywu "Klinika" -- poziome kafelki kcal/B/T/W. */
@Composable
private fun DailyTargetBento(kcalTarget: Int, macros: MacroGrams) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        BentoMetricTile(value = "$kcalTarget", label = "kcal / dzień", modifier = Modifier.weight(1.3f))
        BentoMetricTile(value = "${macros.protein}g", label = "białko", modifier = Modifier.weight(1f))
        BentoMetricTile(value = "${macros.fat}g", label = "tłuszcz", modifier = Modifier.weight(1f))
        BentoMetricTile(value = "${macros.carbs}g", label = "węgle", modifier = Modifier.weight(1f))
    }
}

@Composable
private fun BentoMetricTile(value: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/**
 * FR-87: bento-wariant DayCard dla motywu "Klinika" -- ten sam stan i te
 * same callbacki co DayCard (zero nowej logiki), tylko inny uklad: kafelek
 * dnia z odznaka "Dziś" i pigulka kcal-vs-cel zamiast plaskiego tekstu,
 * wiersze posilkow jako zaokraglone chipy z emoji-avatarem zamiast
 * OutlinedButton na cala szerokosc.
 */
@Composable
private fun DayCardClinic(
    day: Int,
    dayName: String,
    isToday: Boolean,
    dayMeals: Map<String, PlannedMeal>,
    recipesById: Map<String, Recipe>,
    totalKcal: Int,
    kcalTarget: Int,
    onSlotClick: (cat: String) -> Unit,
    onScaleClick: (cat: String, currentScale: Double) -> Unit,
    onRegenerateSlot: (cat: String) -> Unit,
    onPreviewClick: (recipe: Recipe, scale: Double) -> Unit,
    prepAheadFor: (cat: String) -> Recipe?,
    onApplyPrepAhead: (cat: String, recipeId: String) -> Unit,
    onRandomizeDay: () -> Unit,
    onClearDay: () -> Unit,
    onAddDayToShopping: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isToday) 3.dp else 1.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(dayName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                if (isToday) {
                    Box(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                    ) {
                        Text("Dziś", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                ) {
                    Text(
                        "$totalKcal / $kcalTarget kcal",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            PlannerOperations.PLANNER_CATEGORIES.forEach { category ->
                val meal = dayMeals[category.id]
                val recipe = meal?.let { recipesById[it.recipeId] }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                        .clickable { onSlotClick(category.id) }
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(category.emoji, style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(category.label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        val label = when {
                            recipe == null -> "Dodaj posiłek"
                            meal!!.isLeftover -> "🍱 ${recipe.name}"
                            else -> recipe.name
                        }
                        Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, maxLines = 1)
                    }
                    if (recipe != null && meal != null) {
                        Text(
                            formatScale(meal.scale),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .clickable { onScaleClick(category.id, meal.scale) }
                                .padding(horizontal = 4.dp),
                        )
                        TextButton(onClick = { onPreviewClick(recipe, meal.scale) }) { Text("👁️") }
                        TextButton(onClick = { onRegenerateSlot(category.id) }) { Text("🔁") }
                    }
                }
                if (recipe == null) {
                    val suggestion = prepAheadFor(category.id)
                    if (suggestion != null) {
                        val prevDayName = PlannerOperations.DAYS_PL[(day + 6) % 7]
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 2.dp, bottom = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                "🍱 Resztki z $prevDayName: ${suggestion.name}?",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f),
                            )
                            TextButton(onClick = { onApplyPrepAhead(category.id, suggestion.id) }) {
                                Text("Tak")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedButton(onClick = onAddDayToShopping, modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium) {
                Text("🛒 Dodaj składniki z tego dnia do listy zakupów")
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onRandomizeDay, modifier = Modifier.weight(1f)) {
                    Text("🎲 Losuj ten dzień")
                }
                TextButton(onClick = onClearDay, modifier = Modifier.weight(1f)) {
                    Text("🗑️ Wyczyść ten dzień")
                }
            }
        }
    }
}

/** "1×"/"1.5×" -- matches index.html's String(scale).replace(".", ","), but with a dot since this is Polish-locale-agnostic UI text either way. */
private fun formatScale(scale: Double): String =
    (if (scale == scale.toLong().toDouble()) scale.toLong().toString() else scale.toString()) + "×"

/**
 * 2026-08-11 (user request, "dodaj możliwość podglądnięcia przepisu z
 * poziomu planera... żeby wyświetliło kartę jak na karcie z przepisami"):
 * read-only preview -- ingredients (scaled to the ACTUAL planned portion,
 * `PlannerOperations.scaleIngredients`/`scaledKcal`, not the recipe's base
 * 1x amounts) and preparation method, plus the same "tap title -> Google
 * search" idiom `RecipeListScreen.kt`'s `RecipeCardBody` already uses
 * (`ACTION_VIEW` + `google.com/search?q=`). Deliberately NOT a reuse of
 * `RecipeCard`/`RecipeCardBody` -- those are tightly coupled to
 * RecipeListScreen's own ViewModels (swipe-rate, pantry-check, reviews,
 * comments, favorites) that make no sense to fake from here; this is a
 * lighter, planner-scoped subset of the same content.
 */
@Composable
private fun RecipePreviewDialog(recipe: Recipe, scale: Double, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val scaledIngredients = remember(recipe, scale) { PlannerOperations.scaleIngredients(recipe.ingredients, scale) }
    val kcal = remember(recipe, scale) { PlannerOperations.scaledKcal(recipe, scale) }
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.fillMaxWidth().heightIn(max = 560.dp)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        recipe.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                val query = Uri.encode("${recipe.name} przepis")
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=$query")),
                                )
                            },
                    )
                    TextButton(onClick = onDismiss) { Text("✕") }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "⏱ ${recipe.time}   🔥 $kcal kcal" + if (scale != 1.0) "  (porcja ${formatScale(scale)})" else "",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text("Składniki", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                scaledIngredients.forEach { line ->
                    Text("• $line", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 2.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Przygotowanie", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(recipe.method, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

/**
 * Recipe picker for one Planer slot -- port of index.html's
 * openPlannerPickerModal, merged with the "detail" step (index.html shows
 * a separate confirm screen) into one list since scale/leftover controls
 * live on the day-card row itself here instead of a second modal. FR-23's
 * "🍱 Ugotuj na 2 dni" button lives here since it needs the current slot's
 * recipe/scale.
 */
@Composable
private fun PlannerSlotDialog(
    day: Int,
    category: PlannerCategory,
    currentMeal: PlannedMeal?,
    weekPlan: WeekPlan,
    recipes: List<Recipe>,
    profile: Profile,
    targetGrams: MacroGrams?,
    onPick: (Recipe) -> Unit,
    onClear: () -> Unit,
    onPlanLeftover: (targetDay: Int, recipeId: String) -> Unit,
    onDismiss: () -> Unit,
) {
    val sorted = remember(recipes, targetGrams, profile) {
        recipes.sortedByDescending { RecipeMatching.matchScore(it, targetGrams, profile) ?: -1 }
    }
    val currentRecipe = currentMeal?.let { meal -> recipes.find { it.id == meal.recipeId } }
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.widthIn(max = 480.dp)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .heightIn(max = 560.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "${category.emoji} ${category.label}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = onDismiss) { Text("✕") }
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (currentMeal != null && currentRecipe != null) {
                    // FR-23: offered once the portion is scaled ≥2x, or the dish itself reheats well (FR-24's keyword heuristic) -- doesn't create a new shopping-list entry.
                    if (currentMeal.scale >= 2.0 || PlannerOperations.isPrepAheadFriendly(currentRecipe)) {
                        val targetDay = (day + 2) % 7
                        val alreadyLeftover = weekPlan[targetDay]?.get(category.id)?.recipeId == currentRecipe.id
                        OutlinedButton(
                            onClick = { onPlanLeftover(targetDay, currentRecipe.id) },
                            enabled = !alreadyLeftover,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                if (alreadyLeftover) {
                                    "🍱 Resztki już zaplanowane na ${PlannerOperations.DAYS_PL[targetDay]}"
                                } else {
                                    "🍱 Ugotuj na 2 dni — zaplanuj resztki na ${PlannerOperations.DAYS_PL[targetDay]}"
                                },
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    TextButton(onClick = onClear, modifier = Modifier.fillMaxWidth()) {
                        Text("— brak / wyczyść —")
                    }
                    HorizontalDivider()
                }
                sorted.forEach { recipe ->
                    val score = RecipeMatching.matchScore(recipe, targetGrams, profile)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPick(recipe) }
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(recipe.name, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                        val scoreSuffix = score?.let { " · 🎯 $it%" } ?: ""
                        Text(
                            "${recipe.kcal} kcal$scoreSuffix",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}
