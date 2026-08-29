package com.przemas230.dietaapp.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.przemas230.dietaapp.WaterCupIcon
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.przemas230.dietaapp.data.EatenEntry
import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.PlannedMeal
import com.przemas230.dietaapp.data.Profile
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.Snack
import com.przemas230.dietaapp.logic.AppThemes
import com.przemas230.dietaapp.logic.CookHistoryOperations
import com.przemas230.dietaapp.logic.EatenOperations
import com.przemas230.dietaapp.logic.FastingOperations
import com.przemas230.dietaapp.logic.IngredientCanon
import com.przemas230.dietaapp.logic.MacroGrams
import com.przemas230.dietaapp.logic.PlannerCategory
import com.przemas230.dietaapp.logic.PlannerOperations
import com.przemas230.dietaapp.logic.PlannerSwipe
import com.przemas230.dietaapp.logic.ProfileCalculations
import com.przemas230.dietaapp.logic.RecipeMatching
import com.przemas230.dietaapp.logic.RecipePantryMatching
import com.przemas230.dietaapp.logic.ShoppingOperations
import com.przemas230.dietaapp.logic.WaterOperations
import com.przemas230.dietaapp.logic.WeekPlan
import com.przemas230.dietaapp.logic.forCategory
import com.przemas230.dietaapp.ui.theme.LocalDietaThemeId
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

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
    // Requested 2026-08-26 ("jest inna niż na Web a co za tym idzie
    // brzydsza"): RecipePreviewDialog now reuses the REAL RecipeCardBody
    // (favorite star, pantry-check, cook history, reviews, comments) --
    // needs the same shared ViewModels RecipeListScreen already gets from
    // MainActivity, hoisted here for the same reason (see RecipePreviewDialog's
    // own doc comment).
    pantryViewModel: PantryViewModel,
    recipeViewModel: RecipeViewModel,
    favoriteIngredientsViewModel: FavoriteIngredientsViewModel,
    activityLogViewModel: ActivityLogViewModel,
    recipeCommentsViewModel: RecipeCommentsViewModel,
    // Requested 2026-08-26 ("dodaj też przynajmniej 5 nowych funkcji" --
    // research flagged lost/broken data as a common complaint): shows a
    // Snackbar with an undo action -- MainActivity owns the actual
    // SnackbarHostState/Scaffold slot (same "hoisted at the top" pattern
    // as every other cross-screen ViewModel here), this just requests one.
    onShowUndoSnackbar: (message: String, actionLabel: String, onUndo: () -> Unit) -> Unit = { _, _, _ -> },
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
    onSetEaten: (cat: String, eaten: Boolean, portion: Double, plannedKcal: Int?, plannedName: String?) -> Unit = { _, _, _, _, _ -> },
    // Requested 2026-08-26: opt-in fill-with-color on the "POZOSTAŁO" tile,
    // see RemainingKcalFillViewModel/SettingsScreen's matching card.
    remainingKcalFillEnabled: Boolean = false,
    // Bug fixed 2026-08-26: FastingViewModel's status text used to only be
    // wired into MainActivity's HeaderKcalPanel, which never renders for
    // Klinika -- same fix pattern as remainingKcalFillEnabled just above.
    fastingEnabled: Boolean = false,
    fastingWindowStart: Int = 12,
    fastingWindowEnd: Int = 20,
    // Requested 2026-08-25 ("zrównaj dzień tygodnia/datę i Cześć, nazwę
    // użytkownika z plusikiem i kołem zębatym... żeby było w jednej
    // linii"): MainActivity's global quick-add/settings icons, rendered
    // here (Klinika only) on the same Row as the date/greeting text
    // instead of their own separate row above -- see MainActivity.kt's
    // isClinicHeader gate, which skips its own copy for this tab.
    headerActions: @Composable RowScope.() -> Unit = {},
) {
    val allRecipes by plannerViewModel.allRecipes.collectAsState()
    val weekPlan by plannerViewModel.weekPlan.collectAsState()
    // FR-99 (2026-08-29): carrying out one of the four swipe actions needs
    // the cook history AND the pantry, both of which live here (this screen
    // already owns recipeViewModel/pantryViewModel for RecipePreviewDialog)
    // rather than in PlannerDashboard, which deliberately takes no
    // ViewModels. The dashboard just reports WHICH action was committed.
    val handlePlannerSwipe: (String, PlannerSwipe.Action, Recipe, Int) -> Unit = { cat, action, recipe, scaledKcal ->
        when (action) {
            PlannerSwipe.Action.COOKED -> {
                // Idempotent: a second short right-swipe on the same dish on
                // the same day must NOT subtract the pantry twice.
                if (recipeViewModel.isCookedToday(recipe.id)) {
                    onShowUndoSnackbar("To danie jest już oznaczone jako zrobione dzisiaj", "OK") {}
                } else {
                    recipeViewModel.markCookedToday(recipe.id)
                    pantryViewModel.subtractForRecipe(recipe)
                    activityLogViewModel.log("cook_subtract", "Ugotowano „${recipe.name}” — odjęto składniki ze spiżarni")
                    onShowUndoSnackbar("🍳 Zrobione — odjęto składniki ze spiżarni", "Cofnij") {
                        if (recipeViewModel.undoCookedToday(recipe.id)) {
                            pantryViewModel.restoreForRecipe(recipe)
                            activityLogViewModel.log("pantry_add", "Cofnięto wpis „${recipe.name}” — przywrócono w spiżarni")
                        }
                    }
                }
            }
            PlannerSwipe.Action.EATEN -> onSetEaten(cat, true, 1.0, scaledKcal, recipe.name)
            PlannerSwipe.Action.HALF -> onSetEaten(cat, true, 0.5, scaledKcal, recipe.name)
            PlannerSwipe.Action.RESET -> {
                onSetEaten(cat, false, 0.0, scaledKcal, recipe.name)
                // "pasuje też móc cofnąć zrobienie bo mogło się przez
                // przypadek kliknąć" -- the long left swipe resets the card
                // completely, cook log included, restoring the ingredients
                // the cook entry took out of the pantry.
                if (recipeViewModel.undoCookedToday(recipe.id)) {
                    pantryViewModel.restoreForRecipe(recipe)
                    activityLogViewModel.log("pantry_add", "Cofnięto wpis „${recipe.name}” — przywrócono w spiżarni")
                }
            }
        }
    }
    val profile by profileViewModel.profile.collectAsState()
    // Requested 2026-08-26 (RecipePreviewDialog's full RecipeCardBody reuse).
    val pantryItems by pantryViewModel.items.collectAsState()
    val cookedMap by recipeViewModel.cooked.collectAsState()
    // FR-99: which dishes already carry a "zrobione" entry for today --
    // derived from the same StateFlow the cook-history modal reads, so the
    // card's chip updates the moment the swipe writes one.
    val cookedTodayIds = remember(cookedMap) {
        val now = System.currentTimeMillis()
        cookedMap.keys.filter { CookHistoryOperations.cookedTodayIndex(cookedMap, it, now) >= 0 }.toSet()
    }
    val reviews by recipeViewModel.reviews.collectAsState()
    val favoriteRecipeIds by recipeViewModel.favoriteRecipes.collectAsState()
    val favIngredients by favoriteIngredientsViewModel.favorites.collectAsState()
    // Requested 2026-08-25 (recipe preview dialog enrichment, see
    // RecipePreviewDialog's own comment): only new dependency needed to
    // add the "dodaj do listy zakupów" toggle to the preview.
    val shoppingItems by shoppingViewModel.items.collectAsState()

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
    // Requested 2026-08-26 ("📋 Kopiuj plan z innego dnia"): the day INDEX
    // being copied INTO -- non-null shows CopyDayPickerDialog below.
    var copyDayTarget by remember { mutableStateOf<Int?>(null) }
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
    // Requested 2026-08-25 (Web FR-87/v14 day-strip follow-up, ported here):
    // tapping a day-strip chip on the dashboard scrolls the matching day's
    // now-full-screen card (see DayCardClinic's fillParentMaxHeight() call
    // below) into view -- item index 1+day because the dashboard itself is
    // always item index 0 for Klinika (the AutoPlanWeekButton that occupies
    // index 0 for the other 11 themes moves to the END of the list for
    // Klinika instead, see below).
    val listState = rememberLazyListState()
    val scrollScope = rememberCoroutineScope()

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (isClinic) {
            // Requested 2026-08-25 (screenshot follow-up to FR-87/v15):
            // "Dzisiejszy Planer" only filled about half the screen, with
            // Monday's day-card already visible below it. fillParentMaxHeight()
            // gives PlannerDashboard a bounded height to actually stretch
            // into (see its own weight(1f) meal-list + DailyTargetBento
            // call inside it, moved in from its own separate item{} here)
            // -- matches web's #plannerTodayWrap{min-height:100dvh-...}.
            item {
                PlannerDashboard(
                    displayName = displayName,
                    kcalTarget = kcalTargets.daily,
                    dailyMacros = macroTargets.daily,
                    todayMeals = weekPlan[todayIndex].orEmpty(),
                    recipesById = recipesById,
                    eatenEntries = eatenEntries,
                    snacks = snacks,
                    waterCount = waterCount,
                    todayIndex = todayIndex,
                    onToggleEaten = onToggleEaten,
                    onClearSlot = { cat ->
                        val removed = weekPlan[todayIndex]?.get(cat)
                        plannerViewModel.clearSlot(todayIndex, cat)
                        if (removed != null) {
                            onShowUndoSnackbar("Usunięto z planu", "Cofnij") {
                                plannerViewModel.restoreMeal(todayIndex, cat, removed)
                            }
                        }
                    },
                    onSlotClick = { cat -> slotPicker = todayIndex to cat },
                    onSignOut = onSignOut,
                    onPreviewRecipe = { recipe, scale -> previewRecipe = recipe to scale },
                    pantryItems = pantryItems,
                    onDayJump = { di -> scrollScope.launch { listState.animateScrollToItem(1 + di) } },
                    onWaterTap = onWaterTap,
                    onWaterSetCount = onWaterSetCount,
                    onSwipeAction = { cat, action, recipe, scaledKcal ->
                        handlePlannerSwipe(cat, action, recipe, scaledKcal)
                    },
                    cookedTodayIds = cookedTodayIds,
                    remainingKcalFillEnabled = remainingKcalFillEnabled,
                    fastingEnabled = fastingEnabled,
                    fastingWindowStart = fastingWindowStart,
                    fastingWindowEnd = fastingWindowEnd,
                    headerActions = headerActions,
                    modifier = Modifier.fillParentMaxHeight(),
                )
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
            item { SharePlanButtons(weekPlan = weekPlan, recipesById = recipesById) }
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
            val copyDayClick: () -> Unit = { copyDayTarget = day }

            if (isClinic) {
                // Requested 2026-08-25, REVISED 2026-08-26, REVISED AGAIN
                // 2026-08-29: originally ALL 7 day cards got
                // fillParentMaxHeight() (one day per scroll); then only
                // today's did; now none of them do. The "Dzisiejszy Planer"
                // block at the top of this same screen (PlannerDashboard,
                // itself fillParentMaxHeight) is ALREADY a full-screen view
                // of today, so stretching today's card down here as well
                // just meant scrolling past the same day twice ("niżej w
                // dniach tygodnia nie ma potrzeby żeby dzisiejszy dzień też
                // był rozciągnięty na całą stronę, zrób go takiego jak
                // pozostałe dni"). isToday is still passed through, so the
                // card keeps its "this is today" accent -- only the height
                // override is gone. Day-strip chips' onDayJump still
                // scrolls to any day, it just lands on a compact card.
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
                    onCopyDayClick = copyDayClick,
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
                    onCopyDayClick = copyDayClick,
                )
            }
        }
        if (isClinic) {
            item { AutoPlanWeekButton(onClick = {
                pendingConfirm = PendingConfirm(
                    "To nadpisze wszystkie dania zaplanowane w całym tygodniu. Na pewno chcesz wygenerować nowy plan?",
                ) { plannerViewModel.randomizeWeek(profile) }
            }) }
            item { SharePlanButtons(weekPlan = weekPlan, recipesById = recipesById) }
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
        RecipePreviewDialog(
            recipe = recipe,
            scale = scale,
            profile = profile,
            targetGrams = macroTargets.forCategory(recipe.cat),
            isAddedToShopping = ShoppingOperations.isRecipeAdded(shoppingItems, recipe.id),
            onToggleAddToShopping = {
                if (ShoppingOperations.isRecipeAdded(shoppingItems, recipe.id)) {
                    shoppingViewModel.removeRecipe(recipe)
                } else {
                    shoppingViewModel.addRecipe(recipe)
                }
            },
            isFavorite = recipe.id in favoriteRecipeIds,
            onToggleFavorite = { recipeViewModel.toggleFavoriteRecipe(recipe.id) },
            pantryItems = pantryItems,
            onToggleHaveIngredient = { canonName, category, unitCat ->
                val hadBefore = pantryItems.containsKey(canonName)
                pantryViewModel.toggleHaveIngredient(canonName, category, unitCat)
                if (hadBefore) {
                    activityLogViewModel.log("pantry_delete", "Usunięto ze spiżarni: $canonName")
                } else {
                    activityLogViewModel.log("pantry_add", "Dodano do spiżarni: $canonName")
                }
            },
            onAddIngredientToShopping = { ingredientText ->
                val parsed = RecipePantryMatching.parseIngredient(ingredientText)
                val sourceKey = "single:${recipe.id}:${parsed.canonName}"
                shoppingViewModel.addSingleIngredient(ingredientText, sourceKey)
                activityLogViewModel.log("shopping_add", "Dodano pojedynczy składnik do listy: ${parsed.canonName}")
            },
            favIngredients = favIngredients,
            onToggleFavIngredient = { canonName -> favoriteIngredientsViewModel.toggle(canonName) },
            review = reviews[recipe.id],
            onSaveReview = { stars, comment -> recipeViewModel.setReview(recipe.id, stars, comment) },
            onClearReview = { recipeViewModel.clearReview(recipe.id) },
            onDeleteCustomRecipe = { recipeViewModel.removeCustomRecipe(recipe.id) },
            cookEntries = cookedMap[recipe.id].orEmpty(),
            onMarkDoneToday = {
                recipeViewModel.markCookedToday(recipe.id)
                pantryViewModel.subtractForRecipe(recipe)
                activityLogViewModel.log("cook_subtract", "Ugotowano „${recipe.name}” — odjęto składniki ze spiżarni")
            },
            onRemoveCookEntry = { index ->
                pantryViewModel.restoreForRecipe(recipe)
                recipeViewModel.removeCookEntry(recipe.id, index)
                activityLogViewModel.log("pantry_add", "Cofnięto wpis „${recipe.name}” — przywrócono w spiżarni")
            },
            weekPlan = weekPlan,
            onPlanRecipe = { day, cat ->
                val planScale = PlannerOperations.idealScaleFor(recipe, kcalTargets.forCategory(cat))
                plannerViewModel.setMeal(day, cat, recipe.id, planScale)
            },
            commentsViewModel = recipeCommentsViewModel,
            onDismiss = { previewRecipe = null },
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

    val copyTarget = copyDayTarget
    if (copyTarget != null) {
        CopyDayPickerDialog(
            targetDay = copyTarget,
            weekPlan = weekPlan,
            onPick = { fromDay ->
                plannerViewModel.copyDay(fromDay, copyTarget)
                copyDayTarget = null
            },
            onDismiss = { copyDayTarget = null },
        )
    }
}

private class PendingConfirm(val message: String, val action: () -> Unit)

/**
 * Requested 2026-08-26 ("dodaj też przynajmniej 5 nowych funkcji...
 * czego najbardziej potrzebują użytkownicy" -- meal-planner review research
 * consistently flags re-entering the same day's meals over and over, e.g. a
 * repeated weekly routine, as friction): pick a day to copy INTO [targetDay],
 * overwriting whatever's currently planned there. Empty days are shown but
 * disabled -- copying nothing would just silently clear the target, which
 * reads as a bug rather than a no-op.
 */
@Composable
private fun CopyDayPickerDialog(targetDay: Int, weekPlan: WeekPlan, onPick: (fromDay: Int) -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.widthIn(max = 480.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "📋 Kopiuj plan z innego dnia",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = onDismiss) { Text("✕") }
                }
                Text(
                    "Wybierz dzień, z którego skopiować plan na „${PlannerOperations.DAYS_PL[targetDay]}”. Nadpisze to, co jest już zaplanowane na „${PlannerOperations.DAYS_PL[targetDay]}”.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 6.dp, bottom = 10.dp),
                )
                PlannerOperations.DAYS_PL.forEachIndexed { di, dayName ->
                    if (di == targetDay) return@forEachIndexed
                    val hasPlan = weekPlan[di]?.values?.any { true } == true
                    TextButton(
                        onClick = { onPick(di) },
                        enabled = hasPlan,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(if (hasPlan) dayName else "$dayName (pusty)", modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

// FR-21: whole-week random generation, always requires confirmation since it
// overwrites every day. Extracted (was inline) since PlannerScreen now calls
// this from two different LazyColumn positions depending on isClinic.
@Composable
private fun AutoPlanWeekButton(onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text("🎲 Wygeneruj losowo cały tydzień")
    }
}

/**
 * Requested 2026-08-26 (5 new features -- users want their plan portable to
 * send to whoever does the shopping/cooking): native equivalent of
 * index.html's #plannerShareRow (WhatsApp link + clipboard copy), using the
 * same ACTION_SEND chooser pattern as ShoppingScreen's "📤 Udostępnij" button
 * plus a plain clipboard copy for parity with web's second button.
 */
@Composable
private fun SharePlanButtons(weekPlan: WeekPlan, recipesById: Map<String, Recipe>) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Button(
            onClick = {
                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, PlannerOperations.buildWeekPlanText(weekPlan, recipesById))
                }
                context.startActivity(Intent.createChooser(sendIntent, null))
            },
            modifier = Modifier.weight(1f),
        ) {
            Text("📤 Udostępnij plan")
        }
        OutlinedButton(
            onClick = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("Plan tygodnia", PlannerOperations.buildWeekPlanText(weekPlan, recipesById)))
                Toast.makeText(context, "Plan tygodnia skopiowany do schowka", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.weight(1f),
        ) {
            Text("📋 Kopiuj")
        }
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
    onCopyDayClick: () -> Unit = {},
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
            // Requested 2026-08-26 ("dodaj też przynajmniej 5 nowych
            // funkcji" -- meal-planner review research: re-entering the
            // same day's meals repeatedly is common friction).
            TextButton(onClick = onCopyDayClick, modifier = Modifier.fillMaxWidth()) {
                Text("📋 Kopiuj plan z innego dnia")
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
 * MainActivity.kt's isClinicHeader gate on HeaderKcalPanel), a "today first,
 * today highlighted" day strip (still does NOT switch which day's meals
 * show in THIS dashboard -- confirmed with the user before building this,
 * see FR-87.md v7; tapping a chip now scrolls the matching full-screen day
 * card further down into view instead, via onDayJump, requested 2026-08-25),
 * and "Dzisiejszy Planer" cards for today's 5 meal slots (tap
 * toggles eaten same as the old header's swipe-to-eat rows, × clears the
 * slot same as the picker dialog's "— brak / wyczyść —" row, empty slots
 * are a dashed "+ [category]" placeholder opening that same picker).
 */
@Composable
private fun PlannerDashboard(
    displayName: String,
    kcalTarget: Int,
    dailyMacros: MacroGrams,
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
    // Requested 2026-08-26: reports a meal-card tap up to PlannerScreen,
    // which owns the single shared RecipePreviewDialog instance (and the
    // ViewModels it needs) -- this composable no longer keeps its own
    // separate preview state/dialog, matching its existing style of never
    // taking a ViewModel directly.
    onPreviewRecipe: (Recipe, Double) -> Unit = { _, _ -> },
    // Requested 2026-08-26: pantry-coverage badge on each meal card.
    pantryItems: Map<String, PantryItem> = emptyMap(),
    onDayJump: (dayIndex: Int) -> Unit = {},
    onWaterTap: (Int) -> Unit = {},
    onWaterSetCount: (Int) -> Unit = {},
    // FR-99 (2026-08-29, Web parity): the meal-card swipe is no longer
    // "right = eaten / left = not eaten" -- the DISTANCE now picks one of
    // four actions (see PlannerSwipe). The card reports which one the user
    // committed to; PlannerScreen owns the ViewModels needed to carry it
    // out (cook history + pantry live there, not here).
    onSwipeAction: (cat: String, action: PlannerSwipe.Action, recipe: Recipe, scaledKcal: Int) -> Unit = { _, _, _, _ -> },
    /** FR-99: recipe ids already logged as cooked TODAY -- drives the "🍳 Zrobione" chip on the cards. */
    cookedTodayIds: Set<String> = emptySet(),
    remainingKcalFillEnabled: Boolean = false,
    // Bug fixed 2026-08-26: see PlannerScreen's matching param doc comment --
    // this is the only place fasting status can actually reach the screen
    // for Klinika, since HeaderKcalPanel (its original and only call site)
    // never renders under this theme.
    fastingEnabled: Boolean = false,
    fastingWindowStart: Int = 12,
    fastingWindowEnd: Int = 20,
    headerActions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier,
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
    // Requested 2026-08-25 (Web FR-87/v12, ported here): tapping a meal
    // card used to toggle eaten directly -- now opens a preview of the
    // recipe instead (RecipePreviewDialog, same one FR-86's day-card "👁️"
    // button already opens), while toggling eaten moved to swipe (see
    // onSetEaten above). Requested 2026-08-26 (RecipePreviewDialog's full
    // RecipeCardBody reuse): the dialog itself (and the ViewModels it
    // needs) now lives ONE level up, in PlannerScreen -- this composable
    // just reports the tap via onPreviewRecipe instead of keeping its own
    // separate previewRecipe state + second RecipePreviewDialog instance.

    // Requested 2026-08-25 (Web FR-87/v14+screenshot follow-up, ported
    // here): "Dzisiejszy Planer" only filled about half the screen, with
    // Monday's day-card already peeking in below it -- caller now passes
    // Modifier.fillParentMaxHeight() (see LazyColumn item below) so this
    // Column has a bounded height to actually stretch into; the meal-slot
    // list further down gets weight(1f) (each row ALSO weight(1f), same as
    // web's #plannerDashboard{flex:1}+.pd-today-list{flex:1}+per-card
    // flex:1) so the 5 rows grow to fill whatever's left, and
    // DailyTargetBento (moved inside from the caller) lands pinned right
    // after them at the bottom of the full-height column instead of
    // leaving a gap above it.
    Column(modifier = modifier.fillMaxWidth()) {
        // Requested 2026-08-25 (Web FR-87/v9, ported here): the sign-out
        // shortcut duplicated the one already in Ustawienia → Konto and was
        // reported as unwanted on the main screen -- dropped from this Row
        // (onSignOut is kept as a param, unused for now, rather than
        // threading a removal through every call site, in case this is
        // ever reversed the way the web header text was).
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(dateLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    if (displayName.isNotBlank()) "Cześć, $displayName!" else "Cześć!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                if (fastingEnabled) {
                    val now = LocalTime.now()
                    val minutesOfDay = now.hour * 60 + now.minute
                    val inWindow = FastingOperations.isInEatingWindow(fastingWindowStart, fastingWindowEnd, minutesOfDay)
                    val fastingText = if (inWindow) {
                        "🍽️ Okno jedzenia — post zacznie się o %02d:00".format(fastingWindowEnd)
                    } else {
                        "⏳ Okno postu — jedzenie od %02d:00".format(fastingWindowStart)
                    }
                    Text(
                        fastingText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (inWindow) FontWeight.Normal else FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            headerActions()
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Requested 2026-08-25 (Web FR-87/v9, ported here): the "Cel"
            // card was dropped -- the remaining-kcal card below stretches
            // left to fill that space instead of leaving a gap (weight 2.3
            // vs the old Cel(1)+this(1.5)=2.5 combined, matching the web
            // CSS's 2.3fr/1fr split closely enough).
            // Requested 2026-08-25 ("powiększ prostokątek z kołem liczącym
            // kalorie... oraz prostokąt ze znacznikiem ile wypitych
            // szklanek wody, powiększ 150%"): ring box, strokes, paddings
            // and font sizes below all scaled ×1.5 from their previous
            // values (48dp→72dp, 3dp→4.5dp, 9sp→13.5sp, etc.) -- fits fine
            // since the flex:1 meal list below absorbs the size change,
            // same as web's equivalent .pd-card scale-up.
            Card(
                modifier = Modifier.weight(2.3f),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                Row(modifier = Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(72.dp)) {
                        CircularProgressIndicator(
                            progress = { 1f },
                            modifier = Modifier.fillMaxSize(),
                            strokeWidth = 4.5.dp,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                        )
                        CircularProgressIndicator(
                            progress = { kcalPct },
                            modifier = Modifier.fillMaxSize(),
                            strokeWidth = 4.5.dp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text("$eatenKcal/$kcalTarget", fontSize = 13.5.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    // Requested 2026-08-26 ("prostokąt pozostałe kcal mógłby
                    // się zapełniać kolorem... jak coś zjedzone i kółeczko
                    // się zapełnia to i tu kolor"): opt-in overlay Box, same
                    // kcalPct that drives the ring above, filling this
                    // tile's width left-to-right -- drawn between the flat
                    // primary background and the text, same idea as web's
                    // .pd-remaining-fill.
                    // Bug reported 2026-08-29 with a screenshot ("jak się
                    // włączy opcje żeby kafelek pozostało się kolorował to
                    // rozciąga go niepotrzebnie na całą stronę"): the fill
                    // used to be a real child Box with .fillMaxHeight().
                    // This tile sits inside PlannerDashboard's
                    // fillParentMaxHeight Column, so the height constraint
                    // reaching it is the WHOLE remaining screen -- a child
                    // asking for maxHeight therefore grew the tile (and the
                    // Card around it) to fill the screen, pushing the meal
                    // list off it entirely. Drawing the fill with
                    // drawBehind() instead keeps it purely visual: it paints
                    // over the flat primary background (earlier in the
                    // modifier chain) and under the text, while taking no
                    // part in measurement at all, so the tile is sized by
                    // its text exactly like it is with the option off.
                    val fillFraction = kcalPct.coerceIn(0f, 1f)
                    val fillColor = Color.White.copy(alpha = 0.28f)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.primary)
                            .then(
                                if (remainingKcalFillEnabled) {
                                    Modifier.drawBehind {
                                        drawRect(color = fillColor, size = Size(size.width * fillFraction, size.height))
                                    }
                                } else {
                                    Modifier
                                },
                            ),
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 12.dp)) {
                        Text(
                            "POZOSTAŁO",
                            fontSize = 13.5.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            "$remaining kcal",
                            fontSize = 27.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                        )
                    }
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
                        // Requested 2026-08-25 (Web FR-87/v14 day-strip
                        // follow-up, ported here): the strip was purely
                        // decorative -- now doubles as a shortcut, scrolling
                        // the matching day's now-full-screen card into view
                        // (see onDayJump/DayCardClinic's fillParentMaxHeight()
                        // above/below).
                        .clickable { onDayJump(di) }
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
        // FR-99: a one-line cheat sheet for the four swipe outcomes -- the
        // gesture is only discoverable if something says it exists.
        Text(
            "← ½ połowa · ←← cofnij  |  → 🍳 zrobione · →→ 🍴 zjedzone",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 9.5.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(6.dp))
        // weight(1f) here (bounded by the outer Column's own
        // fillParentMaxHeight, see caller) is what actually makes the 5
        // meal rows below grow to fill the screen -- each row ALSO gets
        // weight(1f) so the extra space is distributed across all of them
        // instead of leaving one gap after the last one.
        Column(modifier = Modifier.weight(1f)) {
        PlannerOperations.PLANNER_CATEGORIES.forEachIndexed { index, category ->
            if (index > 0) Spacer(modifier = Modifier.height(6.dp))
            val meal = todayMeals[category.id]
            val recipe = meal?.let { recipesById[it.recipeId] }
            if (recipe == null || meal == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
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
                // FR-99 (2026-08-29): the portion actually eaten, so a half
                // portion reads as half (chip + half the kcal) instead of
                // being indistinguishable from a whole one.
                val portion = EatenOperations.portionOf(eatenEntries, category.id)
                val isHalf = eaten && portion > 0.0 && portion < 1.0
                val cookedToday = recipe.id in cookedTodayIds
                // Same drag-gesture shape as RecipeListScreen's existing
                // swipe-to-rate (Animatable offset,
                // detectHorizontalDragGestures, live colour feedback), but
                // the DISTANCE now picks one of four actions instead of
                // just its direction picking one of two -- see
                // PlannerSwipe, which owns that mapping so the live label,
                // the live tint and the release handler cannot disagree.
                // A plain tap still opens the recipe preview.
                val offsetX = remember(category.id) { Animatable(0f) }
                val swipeScope = rememberCoroutineScope()
                val density = LocalDensity.current
                val swipeMaxPx = with(density) { PlannerSwipe.MAX_DP.dp.toPx() }
                val swipeShortPx = with(density) { PlannerSwipe.SHORT_DP.dp.toPx() }
                val swipeLongPx = with(density) { PlannerSwipe.LONG_DP.dp.toPx() }
                val liveAction = PlannerSwipe.actionFor(offsetX.value, swipeShortPx, swipeLongPx)
                val dragTint = if (liveAction == null) {
                    Color.Transparent
                } else {
                    val intensity = PlannerSwipe.intensityFor(offsetX.value, liveAction, swipeShortPx, swipeLongPx, swipeMaxPx)
                    val base = when (liveAction) {
                        PlannerSwipe.Action.EATEN -> Color(0xFF2E8C5A)
                        PlannerSwipe.Action.COOKED -> Color(0xFF60B478)
                        PlannerSwipe.Action.HALF -> Color(0xFFD2963C)
                        PlannerSwipe.Action.RESET -> Color(0xFFBE463C)
                    }
                    base.copy(alpha = 0.14f + 0.26f * intensity)
                }
                // Bug reported 2026-08-25 ("przy przesuwaniu nie ma kolorów"):
                // Card/Surface always paints its OWN containerColor on top of
                // whatever the incoming modifier already drew, so an outer
                // `.background(dragTint)` on Card's modifier chain (the first
                // attempt) was invisible -- silently painted over. Blending
                // the tint into `colors.containerColor` instead (the color
                // Card itself paints) is the only place a Card's background
                // can actually be influenced from outside.
                val cardContainerColor = dragTint.compositeOver(MaterialTheme.colorScheme.surface)
                Box(modifier = Modifier.weight(1f)) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                            .pointerInput(category.id) {
                                detectHorizontalDragGestures(
                                    onDragEnd = {
                                        val committed = PlannerSwipe.actionFor(offsetX.value, swipeShortPx, swipeLongPx)
                                        swipeScope.launch {
                                            if (committed != null) onSwipeAction(category.id, committed, recipe, kcal)
                                            offsetX.animateTo(0f)
                                        }
                                    },
                                    onDragCancel = { swipeScope.launch { offsetX.animateTo(0f) } },
                                ) { change, dragAmount ->
                                    change.consume()
                                    swipeScope.launch {
                                        offsetX.snapTo((offsetX.value + dragAmount).coerceIn(-swipeMaxPx, swipeMaxPx))
                                    }
                                }
                            }
                            .clickable { onPreviewRecipe(recipe, meal.scale) },
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(containerColor = cardContainerColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    ) {
                        Row(modifier = Modifier.fillMaxHeight().padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
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
                                // Requested 2026-08-26 ("na karcie dzisiejszy
                                // planer na każdym daniu z racji że jest
                                // dużo miejsca dodaj również... znacznik ile
                                // produktów potrzebnych do dania jest
                                // aktualnie w spiżarni") -- same pantryMatch
                                // logic RecipeCardBody's own "🏺 Stan
                                // spiżarni" widget uses.
                                val pantryTotal = recipe.ingredients.size
                                val pantryHave = remember(recipe.id, pantryItems) {
                                    recipe.ingredients.count { ing -> pantryItems.containsKey(RecipePantryMatching.parseIngredient(ing).canonName) }
                                }
                                Text(
                                    "🏺 $pantryHave/$pantryTotal w spiżarni",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                // FR-99: three independent bits of state now
                                // (cooked / eaten / how much), so the card
                                // has to show all three -- struck-through
                                // name = eaten whole (above), these chips =
                                // the other two.
                                if (cookedToday || isHalf) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                        if (cookedToday) {
                                            MealStateChip(
                                                "🍳 Zrobione",
                                                MaterialTheme.colorScheme.primaryContainer,
                                                MaterialTheme.colorScheme.onPrimaryContainer,
                                            )
                                        }
                                        if (isHalf) {
                                            MealStateChip(
                                                "½ Zjedzone w połowie",
                                                MaterialTheme.colorScheme.tertiaryContainer,
                                                MaterialTheme.colorScheme.onTertiaryContainer,
                                            )
                                        }
                                    }
                                }
                            }
                            Text(
                                if (isHalf) "${Math.round(kcal * portion).toInt()} / $kcal kcal" else "$kcal kcal",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            IconButton(onClick = { onClearSlot(category.id) }, modifier = Modifier.size(32.dp)) {
                                Text("✕", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                    // FR-99: a static arrow can no longer say what will
                    // happen, because that depends on how far the drag has
                    // gone -- while dragging, the card names the action it
                    // would commit to right now; at rest it falls back to
                    // the old discrete hint.
                    if (liveAction != null) {
                        val toTheRight = liveAction == PlannerSwipe.Action.COOKED || liveAction == PlannerSwipe.Action.EATEN
                        Text(
                            liveAction.label,
                            modifier = Modifier
                                .align(if (toTheRight) Alignment.CenterEnd else Alignment.CenterStart)
                                .padding(horizontal = 12.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    } else if (offsetX.value == 0f) {
                        Text(
                            "↔",
                            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 44.dp).alpha(0.3f),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
        }
        Spacer(modifier = Modifier.height(10.dp))
        DailyTargetBento(kcalTarget = kcalTarget, macros = dailyMacros)
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
                            // Requested 2026-08-26 ("dla motywów klinika i
                            // klinika noc też zmień kółeczka od wody na
                            // kropelki wszędzie"): was a plain filled/empty
                            // circle -- now the same droplet WaterCupIcon
                            // the other 11 themes' header strip uses.
                            for (i in 0 until WaterOperations.MAX_LEVEL) {
                                WaterCupIcon(
                                    filled = i < waterCount,
                                    size = 22.dp,
                                    modifier = Modifier.clickable { onWaterTap(i) },
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
        // Requested 2026-08-25: enlarged 150% along with the kcal-ring
        // card next to it (see PlannerDashboard) -- this composable has
        // only that one call site, so scaled directly rather than adding
        // a flag.
        Column(modifier = Modifier.padding(15.dp)) {
            Text(
                label.uppercase(),
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
            )
            Text(value, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text(unit, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
 * FR-99: a small rounded status chip under a meal card's dish name --
 * "what this card currently IS" (cooked today / eaten only half), as
 * opposed to the drag label, which says what a swipe WOULD do.
 */
@Composable
private fun MealStateChip(text: String, background: Color, contentColor: Color) {
    Text(
        text,
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(background)
            .padding(horizontal = 6.dp, vertical = 1.dp),
        fontSize = 9.5.sp,
        fontWeight = FontWeight.Bold,
        color = contentColor,
    )
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
    onCopyDayClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
            // Requested 2026-08-26 ("dodaj też przynajmniej 5 nowych
            // funkcji" -- meal-planner review research: re-entering the
            // same day's meals repeatedly is common friction).
            TextButton(onClick = onCopyDayClick, modifier = Modifier.fillMaxWidth()) {
                Text("📋 Kopiuj plan z innego dnia")
            }
        }
    }
}

/** "1×"/"1.5×" -- matches index.html's String(scale).replace(".", ","), but with a dot since this is Polish-locale-agnostic UI text either way. */
private fun formatScale(scale: Double): String =
    (if (scale == scale.toLong().toDouble()) scale.toLong().toString() else scale.toString()) + "×"

private fun formatMacroNum(value: Double): String =
    if (value == value.toLong().toDouble()) value.toLong().toString() else value.toString()

/**
 * 2026-08-11 (user request, "dodaj możliwość podglądnięcia przepisu z
 * poziomu planera... żeby wyświetliło kartę jak na karcie z przepisami"):
 * originally a lighter, hand-rolled read-only subset.
 *
 * Rebuilt 2026-08-26 ("w Androidzie dalej nie podoba mi się karta z
 * przepisami otwierana po kliknięciu, jest inna... a co za tym idzie
 * brzydsza"): now a REAL reuse of `RecipeCardBody` (made `internal` in
 * RecipeListScreen.kt for this), the exact same composable Przepisy uses --
 * favorite star, pantry-check widget + dialog, cook history, reviews,
 * comments, "Zaplanuj" all included, wired to the same shared ViewModels
 * MainActivity already hoists (pantryViewModel/recipeViewModel/
 * favoriteIngredientsViewModel/activityLogViewModel/recipeCommentsViewModel,
 * now also threaded into PlannerScreen). This closes the gap the previous
 * round's revision note (superseded, see git history) explicitly flagged
 * as deferred.
 *
 * Portion scale (the ACTUAL planned amount, e.g. 1.5x, not the recipe's
 * base 1x) is preserved by feeding RecipeCardBody a `scaledRecipe` --
 * `recipe.copy()` with kcal/ingredients/protein/carbs/fat/fiber/gl scaled
 * by `PlannerOperations`, same id (so pantry/cook/review/favorite lookups
 * by recipe.id still resolve correctly) -- rather than losing the scaling
 * feature just to reuse the unscaled Przepisy-tab composable as-is.
 */
@Composable
private fun RecipePreviewDialog(
    recipe: Recipe,
    scale: Double,
    profile: Profile,
    targetGrams: MacroGrams?,
    isAddedToShopping: Boolean,
    onToggleAddToShopping: () -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    pantryItems: Map<String, PantryItem>,
    onToggleHaveIngredient: (canonName: String, category: PantryCategory, unitCat: String) -> Unit,
    onAddIngredientToShopping: (ingredientText: String) -> Unit,
    favIngredients: Set<String>,
    onToggleFavIngredient: (canonName: String) -> Unit,
    review: com.przemas230.dietaapp.data.RecipeReview?,
    onSaveReview: (stars: Int, comment: String?) -> Boolean,
    onClearReview: () -> Unit,
    onDeleteCustomRecipe: () -> Unit,
    cookEntries: List<com.przemas230.dietaapp.data.CookEntry>,
    onMarkDoneToday: () -> Unit,
    onRemoveCookEntry: (index: Int) -> Unit,
    weekPlan: WeekPlan,
    onPlanRecipe: (day: Int, cat: String) -> Unit,
    commentsViewModel: RecipeCommentsViewModel,
    onDismiss: () -> Unit,
) {
    val scaledRecipe = remember(recipe, scale) {
        if (scale == 1.0) {
            recipe
        } else {
            recipe.copy(
                kcal = PlannerOperations.scaledKcal(recipe, scale),
                ingredients = PlannerOperations.scaleIngredients(recipe.ingredients, scale),
                protein = recipe.protein?.let { it * scale },
                carbs = recipe.carbs?.let { it * scale },
                fat = recipe.fat?.let { it * scale },
                fiber = recipe.fiber?.let { it * scale },
                gl = recipe.gl?.let { it * scale },
            )
        }
    }
    val matchScore = remember(scaledRecipe, targetGrams, profile) { RecipeMatching.matchScore(scaledRecipe, targetGrams, profile) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var showPantryCheck by remember { mutableStateOf(false) }
    var showReviewDialog by remember { mutableStateOf(false) }
    var showCookHistory by remember { mutableStateOf(false) }
    var showPlanPicker by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.fillMaxWidth().heightIn(max = 640.dp)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                // Requested 2026-08-25/26 (Web FR-87/v17, ported here): a
                // slim row with just the close button, mirroring web's
                // trimmed `.modal-head` (the redundant "🍽️ Podgląd
                // przepisu" title text was removed there too) -- the
                // recipe's own name, rendered by RecipeCardBody right
                // below, already serves as the title.
                if (scale != 1.0) {
                    Text(
                        "Porcja ${formatScale(scale)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("✕") }
                }
                Row {
                    Column(modifier = Modifier.weight(1f)) {
                        RecipeCardBody(
                            recipe = scaledRecipe,
                            matchScore = matchScore,
                            expanded = true,
                            onInfoClick = { showInfoDialog = true },
                            onPantryCheckClick = { showPantryCheck = true },
                            pantryItems = pantryItems,
                            favIngredients = favIngredients,
                            onToggleFavIngredient = onToggleFavIngredient,
                            review = review,
                            onOpenReview = { showReviewDialog = true },
                            onDeleteCustomRecipe = { showDeleteConfirm = true },
                            isFavorite = isFavorite,
                            onToggleFavorite = onToggleFavorite,
                            commentsViewModel = commentsViewModel,
                            isAddedToShopping = isAddedToShopping,
                            onToggleAddToShopping = onToggleAddToShopping,
                            cookCount = cookEntries.size,
                            onMarkDoneClick = { showCookHistory = true },
                            onPlanClick = { showPlanPicker = true },
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        val thumbEmoji = remember(recipe.id) { IngredientCanon.mainIngredientInfo(recipe)?.emoji ?: "🍽️" }
                        Text(thumbEmoji, fontSize = 24.sp)
                    }
                }
            }
        }
    }

    if (showInfoDialog) {
        MacroInfoDialog(recipe = scaledRecipe, onDismiss = { showInfoDialog = false })
    }
    if (showPantryCheck) {
        PantryCheckDialog(
            recipe = scaledRecipe,
            pantryItems = pantryItems,
            onToggleHave = onToggleHaveIngredient,
            onAddToShopping = onAddIngredientToShopping,
            onDismiss = { showPantryCheck = false },
        )
    }
    if (showReviewDialog) {
        RecipeReviewDialog(
            recipeName = recipe.name,
            existing = review,
            onSave = onSaveReview,
            onDelete = { onClearReview(); showReviewDialog = false },
            onDismiss = { showReviewDialog = false },
        )
    }
    if (showCookHistory) {
        CookHistoryDialog(
            recipe = recipe,
            entries = cookEntries,
            review = review,
            onMarkDoneToday = onMarkDoneToday,
            onRateRecipe = { showCookHistory = false; showReviewDialog = true },
            onRemoveEntry = onRemoveCookEntry,
            onDismiss = { showCookHistory = false },
        )
    }
    if (showPlanPicker) {
        PlanPickerDialog(
            recipe = recipe,
            weekPlan = weekPlan,
            onPick = { day, cat -> onPlanRecipe(day, cat); showPlanPicker = false },
            onDismiss = { showPlanPicker = false },
        )
    }
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Usunąć ten przepis?") },
            text = { Text("„${recipe.name}” zniknie z Twoich przepisów. Tego nie da się cofnąć.") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteCustomRecipe()
                    showDeleteConfirm = false
                    onDismiss()
                }) { Text("Usuń") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Anuluj") }
            },
        )
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
