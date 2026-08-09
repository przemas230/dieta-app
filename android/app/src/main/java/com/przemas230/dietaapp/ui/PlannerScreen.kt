package com.przemas230.dietaapp.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.przemas230.dietaapp.data.PlannedMeal
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.logic.MacroGrams
import com.przemas230.dietaapp.logic.PlannerCategory
import com.przemas230.dietaapp.logic.PlannerOperations
import com.przemas230.dietaapp.logic.ProfileCalculations
import com.przemas230.dietaapp.logic.RecipeMatching
import com.przemas230.dietaapp.logic.WeekPlan
import com.przemas230.dietaapp.logic.forCategory

/**
 * FR-18/20/21/22/23/24: 7 day cards, each with the 5 meal-slot rows from
 * PlannerOperations.PLANNER_CATEGORIES, a portion-scale chip per filled slot
 * (FR-20), per-slot/per-day/whole-week random generation (FR-21), per-day
 * clearing (FR-22), "ugotuj na 2 dni" leftovers (FR-23) and the proactive
 * next-day carry-over suggestion (FR-24). FR-27 (add the whole week's
 * ingredients to the shopping list) isn't ported yet — see android/PARITY.md.
 */
@Composable
fun PlannerScreen(plannerViewModel: PlannerViewModel, profileViewModel: ProfileViewModel) {
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

    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            // FR-21: whole-week random generation, always requires confirmation since it overwrites every day.
            Button(
                onClick = {
                    pendingConfirm = PendingConfirm(
                        "To nadpisze wszystkie dania zaplanowane w całym tygodniu. Na pewno chcesz wygenerować nowy plan?",
                    ) { plannerViewModel.randomizeWeek(profile) }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("🎲 Wygeneruj losowo cały tydzień")
            }
        }
        itemsIndexed(PlannerOperations.DAYS_PL) { day, dayName ->
            DayCard(
                day = day,
                dayName = dayName,
                dayMeals = weekPlan[day].orEmpty(),
                recipesById = recipesById,
                totalKcal = PlannerOperations.dayTotalKcal(weekPlan, day, recipesById),
                onSlotClick = { cat -> slotPicker = day to cat },
                onScaleClick = { cat, currentScale ->
                    plannerViewModel.setScale(day, cat, PlannerOperations.nextScaleStep(currentScale))
                },
                onRegenerateSlot = { cat -> plannerViewModel.regenerateSlot(day, cat, profile) },
                prepAheadFor = { cat -> PlannerOperations.prepAheadSuggestion(weekPlan, day, cat, recipesById) },
                onApplyPrepAhead = { cat, recipeId -> plannerViewModel.planLeftover(day, cat, recipeId) },
                onRandomizeDay = {
                    pendingConfirm = PendingConfirm(
                        "Wygenerować losowo cały dzień „$dayName”? Nadpisze wybrane tam dania.",
                    ) { plannerViewModel.randomizeDay(day, profile) }
                },
                onClearDay = {
                    pendingConfirm = PendingConfirm(
                        "Wyczyścić wszystkie dania zaplanowane na „$dayName”?",
                    ) { plannerViewModel.clearDay(day) }
                },
            )
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
    prepAheadFor: (cat: String) -> Recipe?,
    onApplyPrepAhead: (cat: String, recipeId: String) -> Unit,
    onRandomizeDay: () -> Unit,
    onClearDay: () -> Unit,
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
