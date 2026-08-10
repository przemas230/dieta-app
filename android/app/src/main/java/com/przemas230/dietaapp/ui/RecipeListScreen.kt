package com.przemas230.dietaapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.przemas230.dietaapp.data.CookEntry
import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.ShoppingItem
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.logic.CATEGORIES
import com.przemas230.dietaapp.logic.DailyCalorieTargets
import com.przemas230.dietaapp.logic.IngredientCanon
import com.przemas230.dietaapp.logic.Micronutrients
import com.przemas230.dietaapp.logic.PantryOperations
import com.przemas230.dietaapp.logic.PlannerOperations
import com.przemas230.dietaapp.logic.ProfileCalculations
import com.przemas230.dietaapp.logic.RecipeMatching
import com.przemas230.dietaapp.logic.RecipePantryMatching
import com.przemas230.dietaapp.logic.RecipeRating
import com.przemas230.dietaapp.logic.RecipeRatingOperations
import com.przemas230.dietaapp.logic.ShoppingOperations
import com.przemas230.dietaapp.logic.WeekPlan
import com.przemas230.dietaapp.logic.forCategory
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

/**
 * Pure screen content — no own Scaffold/TopAppBar, since the app-level
 * Scaffold in MainActivity now owns the top bar and bottom navigation
 * shared across all tabs.
 */
@Composable
fun RecipeListScreen(
    profileViewModel: ProfileViewModel,
    pantryViewModel: PantryViewModel,
    shoppingViewModel: ShoppingViewModel,
    plannerViewModel: PlannerViewModel,
    swipeRatingStyleViewModel: SwipeRatingStyleViewModel,
    viewModel: RecipeViewModel = viewModel(),
) {
    val swipeRatingStyle by swipeRatingStyleViewModel.style.collectAsState()
    val recipes by viewModel.visibleRecipes.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchTerm by viewModel.searchTerm.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val cookedMap by viewModel.cooked.collectAsState()
    val ratings by viewModel.ratings.collectAsState()
    val pantryItems by pantryViewModel.items.collectAsState()
    val weekPlan by plannerViewModel.weekPlan.collectAsState()
    val shoppingItems by shoppingViewModel.items.collectAsState()
    val profile by profileViewModel.profile.collectAsState()
    LaunchedEffect(profile.glutenFree, profile.lactoseFree) {
        viewModel.setDietaryFilters(profile.glutenFree, profile.lactoseFree)
    }

    var sortByMatch by remember { mutableStateOf(false) }
    var sortByRating by remember { mutableStateOf(false) }
    val macroTargets = remember(profile) { ProfileCalculations.calcMacroTargets(profile) }
    val kcalTargets = remember(profile) { ProfileCalculations.calcTargets(profile) }
    // FR-72: the 🎯 badge means nothing before the user has entered real
    // profile data, so it's withheld entirely (not computed off the
    // internal placeholder Profile()) until profile.configured -- port of
    // index.html's `state.profile.configured ? recipeMatchScore(...) : null`.
    val matchScores = remember(recipes, macroTargets, profile) {
        if (!profile.configured) {
            emptyMap()
        } else {
            recipes.associate { it.id to RecipeMatching.matchScore(it, macroTargets.forCategory(it.cat), profile) }
        }
    }
    val displayedRecipes = remember(recipes, sortByMatch, sortByRating, matchScores, ratings) {
        // FR-2/FR-57: independent toggles applied in sequence (matches
        // index.html's own if(sortByMatch){...} if(sortByRating){...}), not
        // combined into one composite key.
        var result = recipes
        if (sortByMatch) result = result.sortedByDescending { matchScores[it.id] ?: -1 }
        if (sortByRating) result = RecipeRatingOperations.sortByRating(result, ratings)
        result
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchTerm,
            onValueChange = { viewModel.setSearchTerm(it) },
            label = { Text("Szukaj przepisu lub składnika…") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp),
        ) {
            items(CATEGORIES) { category ->
                FilterChip(
                    selected = category.id == selectedCategory,
                    onClick = { viewModel.selectCategory(category.id) },
                    label = { Text("${category.emoji} ${category.label}") },
                )
            }
            item {
                // FR-11/FR-2: sorts the visible list by 🎯 match-to-profile score, descending.
                FilterChip(
                    selected = sortByMatch,
                    onClick = { sortByMatch = !sortByMatch },
                    label = { Text("🎯 Dopasowanie") },
                )
            }
            item {
                // FR-57: liked first, then unrated, then disliked.
                FilterChip(
                    selected = sortByRating,
                    onClick = { sortByRating = !sortByRating },
                    label = { Text("❤️ Ranking") },
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        when {
            isLoading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Wczytywanie przepisów…")
            }
            displayedRecipes.isEmpty() -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Brak przepisów spełniających kryteria.")
            }
            else -> RecipeListWithScrollToTop(
                displayedRecipes,
                matchScores,
                cookedMap,
                ratings,
                pantryItems,
                weekPlan,
                shoppingItems,
                kcalTargets,
                viewModel,
                pantryViewModel,
                shoppingViewModel,
                plannerViewModel,
                swipeRatingStyle,
            )
        }
    }
}

/**
 * FR-5: a "⬆️" FAB that appears once the list has scrolled roughly 400px
 * past the top (index.html's threshold) and animates back to the first
 * item on tap — same behavior as the web app's floating back-to-top button.
 */
@Composable
private fun RecipeListWithScrollToTop(
    recipes: List<Recipe>,
    matchScores: Map<String, Int?>,
    cookedMap: Map<String, List<CookEntry>>,
    ratings: Map<String, RecipeRating>,
    pantryItems: Map<String, PantryItem>,
    weekPlan: WeekPlan,
    shoppingItems: Map<String, ShoppingItem>,
    kcalTargets: DailyCalorieTargets,
    viewModel: RecipeViewModel,
    pantryViewModel: PantryViewModel,
    shoppingViewModel: ShoppingViewModel,
    plannerViewModel: PlannerViewModel,
    swipeRatingStyle: SwipeRatingStyle,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val thresholdPx = with(density) { 400.dp.toPx() }
    // FR-50: honors the system-wide "remove animations" setting, same as
    // the web app's prefers-reduced-motion handling.
    val context = LocalContext.current
    val reducedMotion = remember { isReducedMotionEnabled(context) }
    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > thresholdPx
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(recipes, key = { it.id }) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    matchScore = matchScores[recipe.id],
                    cookEntries = cookedMap[recipe.id].orEmpty(),
                    pantryItems = pantryItems,
                    onMarkDoneToday = {
                        viewModel.markCookedToday(recipe.id)
                        pantryViewModel.subtractForRecipe(recipe)
                    },
                    onSetRating = { index, rating -> viewModel.setCookRating(recipe.id, index, rating) },
                    onRemoveEntry = { index ->
                        pantryViewModel.restoreForRecipe(recipe)
                        viewModel.removeCookEntry(recipe.id, index)
                    },
                    onToggleHaveIngredient = { canonName, category, unitCat ->
                        pantryViewModel.toggleHaveIngredient(canonName, category, unitCat)
                    },
                    onAddIngredientToShopping = { ingredientText ->
                        val parsed = RecipePantryMatching.parseIngredient(ingredientText)
                        val sourceKey = "single:${recipe.id}:${parsed.canonName}"
                        shoppingViewModel.addSingleIngredient(ingredientText, sourceKey)
                    },
                    weekPlan = weekPlan,
                    onPlanRecipe = { day, cat ->
                        val scale = PlannerOperations.idealScaleFor(recipe, kcalTargets.forCategory(cat))
                        plannerViewModel.setMeal(day, cat, recipe.id, scale)
                    },
                    isAddedToShopping = ShoppingOperations.isRecipeAdded(shoppingItems, recipe.id),
                    onToggleAddToShopping = {
                        if (ShoppingOperations.isRecipeAdded(shoppingItems, recipe.id)) {
                            shoppingViewModel.removeRecipe(recipe)
                        } else {
                            shoppingViewModel.addRecipe(recipe)
                        }
                    },
                    rating = ratings[recipe.id],
                    onSwipeRate = { rating -> viewModel.setRating(recipe.id, rating) },
                    onClearSwipeRating = { viewModel.clearRating(recipe.id) },
                    swipeRatingStyle = swipeRatingStyle,
                )
            }
        }
        AnimatedVisibility(
            visible = showButton,
            enter = if (reducedMotion) EnterTransition.None else fadeIn(),
            exit = if (reducedMotion) ExitTransition.None else fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        ) {
            FloatingActionButton(onClick = {
                coroutineScope.launch { listState.animateScrollToItem(0) }
            }) {
                Text("⬆️")
            }
        }
    }
}

@Composable
private fun RecipeCard(
    recipe: Recipe,
    matchScore: Int?,
    cookEntries: List<CookEntry>,
    pantryItems: Map<String, PantryItem>,
    onMarkDoneToday: () -> Unit,
    onSetRating: (index: Int, rating: Int) -> Unit,
    onRemoveEntry: (index: Int) -> Unit,
    onToggleHaveIngredient: (canonName: String, category: PantryCategory, unitCat: String) -> Unit,
    onAddIngredientToShopping: (ingredientText: String) -> Unit,
    weekPlan: WeekPlan,
    onPlanRecipe: (day: Int, cat: String) -> Unit,
    isAddedToShopping: Boolean,
    onToggleAddToShopping: () -> Unit,
    rating: RecipeRating?,
    onSwipeRate: (RecipeRating) -> Unit,
    onClearSwipeRating: () -> Unit,
    swipeRatingStyle: SwipeRatingStyle,
) {
    var expanded by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var showCookHistory by remember { mutableStateOf(false) }
    var showPantryCheck by remember { mutableStateOf(false) }
    var showPlanPicker by remember { mutableStateOf(false) }
    // FR-4: deterministic emoji thumbnail from the recipe's own biggest
    // ingredient — no network round-trip, same icon set as pantry tiles.
    val thumbEmoji = remember(recipe.id) { IngredientCanon.mainIngredientInfo(recipe)?.emoji ?: "🍽️" }

    // FR-55: horizontal drag past the 90dp threshold commits like/dislike;
    // detectHorizontalDragGestures only engages once it sees a clearly
    // horizontal motion (its own touch-slop check), so plain vertical list
    // scrolling is left alone -- same axis-lock intent as index.html's
    // gesture, without hand-rolling the disambiguation.
    val offsetX = remember { Animatable(0f) }
    val swipeCoroutineScope = rememberCoroutineScope()
    val swipeThresholdPx = with(LocalDensity.current) { 90.dp.toPx() }
    val restBorderColor = when (rating) {
        RecipeRating.LIKE -> Color(0xFF43A047)
        RecipeRating.DISLIKE -> Color(0xFFE53935)
        null -> Color.Transparent
    }
    // FR-61: "glow" style tints the whole border while dragging (classic
    // pre-FR-56 look), scaling from a still-visible 0.3 alpha up to 0.9 right
    // at the commit threshold -- same 0.3+0.6*intensity formula as
    // index.html's useGlow branch. "balloon" (default) leaves the border
    // alone during the drag and lets the FR-56 label alone carry the
    // feedback; either way it reverts to restBorderColor once offsetX is 0.
    val dragBorderColor = if (offsetX.value != 0f && swipeRatingStyle == SwipeRatingStyle.GLOW) {
        val intensity = (abs(offsetX.value) / swipeThresholdPx).coerceIn(0f, 1f)
        val alpha = 0.3f + 0.6f * intensity
        if (offsetX.value > 0) Color(0xFF3CAA6E).copy(alpha = alpha) else Color(0xFFBE463C).copy(alpha = alpha)
    } else {
        restBorderColor
    }

    Box {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .border(2.dp, dragBorderColor, MaterialTheme.shapes.medium)
                .pointerInput(recipe.id) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            val committed = offsetX.value
                            swipeCoroutineScope.launch {
                                when {
                                    committed > swipeThresholdPx -> onSwipeRate(RecipeRating.LIKE)
                                    committed < -swipeThresholdPx -> onSwipeRate(RecipeRating.DISLIKE)
                                }
                                offsetX.animateTo(0f)
                            }
                        },
                        onDragCancel = { swipeCoroutineScope.launch { offsetX.animateTo(0f) } },
                    ) { change, dragAmount ->
                        change.consume()
                        swipeCoroutineScope.launch { offsetX.snapTo(offsetX.value + dragAmount) }
                    }
                }
                .clickable { expanded = !expanded },
        ) {
        Column {
            Row(modifier = Modifier.padding(14.dp)) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(thumbEmoji, fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    RecipeCardBody(
                        recipe,
                        matchScore,
                        expanded,
                        onInfoClick = { showInfoDialog = true },
                        onPantryCheckClick = { showPantryCheck = true },
                    )
                }
            }
            // FR-25: whole-recipe add/remove toggle, mirrors index.html's
            // data-add button ("🛒 Dodaj..." / "✓ Na liście zakupów").
            TextButton(
                onClick = onToggleAddToShopping,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 38.dp)
                    .padding(horizontal = 10.dp),
            ) {
                Text(if (isAddedToShopping) "✓ Na liście zakupów" else "🛒 Dodaj do listy zakupów")
            }
            // FR-15: always visible (not gated by `expanded`), same as
            // index.html's always-shown card-actions bar. Tapping this never
            // marks the dish done directly — it always opens the history
            // dialog first (revised from the original press-and-hold design,
            // see FR-15.md's "Uwagi").
            Row(modifier = Modifier.padding(horizontal = 10.dp)) {
                TextButton(
                    onClick = { showCookHistory = true },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 38.dp),
                ) {
                    Text("✅ Zrobione" + if (cookEntries.isNotEmpty()) " (${cookEntries.size}×)" else "")
                }
                TextButton(
                    onClick = { showPlanPicker = true },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 38.dp),
                ) {
                    Text("📅 Zaplanuj")
                }
            }
        }
        }
        // FR-57: persistent 👍/👎 badge on a rated card -- tap to clear the rating.
        if (rating != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .size(30.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(15.dp))
                    .clickable { onClearSwipeRating() },
                contentAlignment = Alignment.Center,
            ) {
                Text(if (rating == RecipeRating.LIKE) "👍" else "👎", fontSize = 16.sp)
            }
        }
        // FR-56: balloon feedback that fades in as the drag approaches the
        // commit threshold, so the user sees what will happen before
        // releasing -- alpha 0 at rest, 1 right at the threshold.
        if (offsetX.value != 0f) {
            val swipeProgress = (abs(offsetX.value) / swipeThresholdPx).coerceIn(0f, 1f)
            Text(
                text = if (offsetX.value > 0) "❤️ LUBIĘ" else "👎 NIE LUBIĘ",
                modifier = Modifier
                    .align(Alignment.Center)
                    .alpha(swipeProgress),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = if (offsetX.value > 0) Color(0xFF43A047) else Color(0xFFE53935),
            )
        }
    }

    if (showInfoDialog) {
        MacroInfoDialog(recipe = recipe, onDismiss = { showInfoDialog = false })
    }
    if (showCookHistory) {
        CookHistoryDialog(
            recipe = recipe,
            entries = cookEntries,
            onMarkDoneToday = onMarkDoneToday,
            onSetRating = onSetRating,
            onRemoveEntry = onRemoveEntry,
            onDismiss = { showCookHistory = false },
        )
    }
    if (showPantryCheck) {
        PantryCheckDialog(
            recipe = recipe,
            pantryItems = pantryItems,
            onToggleHave = onToggleHaveIngredient,
            onAddToShopping = onAddIngredientToShopping,
            onDismiss = { showPantryCheck = false },
        )
    }
    if (showPlanPicker) {
        PlanPickerDialog(
            recipe = recipe,
            weekPlan = weekPlan,
            onPick = { day, cat ->
                onPlanRecipe(day, cat)
                showPlanPicker = false
            },
            onDismiss = { showPlanPicker = false },
        )
    }
}

@Composable
private fun RecipeCardBody(
    recipe: Recipe,
    matchScore: Int?,
    expanded: Boolean,
    onInfoClick: () -> Unit,
    onPantryCheckClick: () -> Unit,
) {
    Column {
        Text(recipe.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        val matchSuffix = matchScore?.let { "   🎯 $it%" } ?: ""
        Text("⏱ ${recipe.time}   🔥 ${recipe.kcal} kcal$matchSuffix", style = MaterialTheme.typography.bodySmall)

        if (expanded) {
            val protein = recipe.protein
            val carbs = recipe.carbs
            val fat = recipe.fat
            if (protein != null && carbs != null && fat != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val fiberPart = recipe.fiber?.let { " · Błonnik ${formatNum(it)}g" } ?: ""
                    val giPart = recipe.gi?.let { " · IG ~${formatNum(it)}" } ?: ""
                    val glPart = recipe.gl?.let { " (ŁG ${formatNum(it)})" } ?: ""
                    Text(
                        "B ${formatNum(protein)}g · W ${formatNum(carbs)}g · T ${formatNum(fat)}g$fiberPart$giPart$glPart",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f),
                    )
                    if (recipe.calc.isNotEmpty()) {
                        TextButton(onClick = onInfoClick) { Text("ℹ️") }
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            // FR-16: full button styling (border + filled background), not
            // bare text, per the revised acceptance criteria in FR-16.md.
            OutlinedButton(
                onClick = onPantryCheckClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 38.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Text("🏺 Sprawdź stan spiżarni dla tego dania")
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text("Składniki", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
            recipe.ingredients.forEach { ingredient ->
                // FR-35: emoji suffix when the ingredient resolves to a known canon -- port of index.html's withEmoji.
                val canon = remember(ingredient) { RecipePantryMatching.parseIngredient(ingredient).canonName }
                Text("• ${IngredientCanon.withEmoji(ingredient, canon)}", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Przygotowanie", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
            Text(recipe.method, style = MaterialTheme.typography.bodySmall)
        }
    }
}

/** "18.0" -> "18", "9.1" -> "9.1" — matches how JS template literals print numbers. */
private fun formatNum(value: Double): String =
    if (value == value.toLong().toDouble()) value.toLong().toString() else value.toString()

/**
 * FR-15/FR-17: "✅ Zrobione" always opens this — a history of past cook
 * dates with a star rating each (FR-17), a "Zrobione dzisiaj" button to add
 * today's entry, and a way to delete a wrong one. Port of index.html's
 * cookHistoryOverlay / renderCookHistoryBody. FR-16 (the separate
 * per-recipe pantry-check window) is a different button, not ported yet.
 */
@Composable
private fun CookHistoryDialog(
    recipe: Recipe,
    entries: List<CookEntry>,
    onMarkDoneToday: () -> Unit,
    onSetRating: (index: Int, rating: Int) -> Unit,
    onRemoveEntry: (index: Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var pendingDeleteIndex by remember { mutableStateOf<Int?>(null) }
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.widthIn(max = 480.dp)) {
            // FR-59: capped so long content (many cook-history entries,
            // ingredients, or recipe picks) scrolls internally instead of
            // being pushed off-screen unreachably.
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .heightIn(max = 560.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        recipe.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = onDismiss) { Text("✕") }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onMarkDoneToday, modifier = Modifier.fillMaxWidth()) {
                    Text("✅ Zrobione dzisiaj")
                }
                Spacer(modifier = Modifier.height(12.dp))

                if (entries.isEmpty()) {
                    Text("Jeszcze nie oznaczone jako zrobione.", style = MaterialTheme.typography.bodyMedium)
                } else {
                    val rated = entries.filter { it.rating != null }
                    val avg = if (rated.isNotEmpty()) rated.sumOf { it.rating!! }.toDouble() / rated.size else null
                    val summary = "Zrobione ${entries.size}×" + (avg?.let { " · średnia ocena ${formatNum(roundTo(it, 1))}★" } ?: "")
                    Text(summary, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(10.dp))

                    entries.forEachIndexed { index, entry ->
                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    "${formatCookDate(entry.dateEpochMillis)} ${formatCookTime(entry.dateEpochMillis)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                                TextButton(onClick = { pendingDeleteIndex = index }) { Text("✕") }
                            }
                            StarRatingRow(rating = entry.rating, onRate = { n -> onSetRating(index, n) })
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }

    val deleteIndex = pendingDeleteIndex
    if (deleteIndex != null) {
        AlertDialog(
            onDismissRequest = { pendingDeleteIndex = null },
            title = { Text("Usunąć wpis?") },
            text = { Text("Usunąć ten wpis o zrobieniu dania? Odjęte wcześniej składniki wrócą do spiżarni.") },
            confirmButton = {
                TextButton(onClick = {
                    onRemoveEntry(deleteIndex)
                    pendingDeleteIndex = null
                }) { Text("Usuń") }
            },
            dismissButton = {
                TextButton(onClick = { pendingDeleteIndex = null }) { Text("Anuluj") }
            },
        )
    }
}

/**
 * FR-17: 5 stars spread across the full row width (SpaceEvenly, each taking
 * an equal weight) rather than crammed to one side — the touch-target fix
 * described in FR-17.md's revision history.
 */
@Composable
private fun StarRatingRow(rating: Int?, onRate: (Int) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        for (n in 1..5) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 34.dp)
                    .clickable { onRate(n) },
                contentAlignment = Alignment.Center,
            ) {
                Text(if ((rating ?: 0) >= n) "★" else "☆", fontSize = 20.sp)
            }
        }
    }
}

private fun describePantryEntry(item: PantryItem): String = when (item) {
    is PantryItem.Product -> "${formatNum(item.quantity)} ${item.unit}"
    is PantryItem.Spice -> item.level.label
}

/**
 * FR-16: styled like a recipe card (not a generic drawer), one row per
 * ingredient with a big "Mam to" toggle (min. 34dp touch height) and a
 * separate "🛒" button to add just that ingredient to the shopping list.
 * Port of index.html's openPantryModal — the "📅 Zaplanuj to danie" button
 * it also has isn't ported yet since there's no Planner screen (FR-18/19).
 */
@Composable
private fun PantryCheckDialog(
    recipe: Recipe,
    pantryItems: Map<String, PantryItem>,
    onToggleHave: (canonName: String, category: PantryCategory, unitCat: String) -> Unit,
    onAddToShopping: (ingredientText: String) -> Unit,
    onDismiss: () -> Unit,
) {
    val addedToShopping = remember { mutableStateMapOf<Int, Boolean>() }
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.widthIn(max = 480.dp)) {
            // FR-59: capped so long content (many cook-history entries,
            // ingredients, or recipe picks) scrolls internally instead of
            // being pushed off-screen unreachably.
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .heightIn(max = 560.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        recipe.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = onDismiss) { Text("✕") }
                }
                Spacer(modifier = Modifier.height(8.dp))

                recipe.ingredients.forEachIndexed { index, ingredient ->
                    val parsed = remember(ingredient) { RecipePantryMatching.parseIngredient(ingredient) }
                    val category = remember(parsed.canonName) {
                        PantryOperations.categoryForCanon(IngredientCanon.CANON_INFO[parsed.canonName]?.cat ?: "Inne")
                    }
                    val entry = pantryItems[parsed.canonName]
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(IngredientCanon.withEmoji(ingredient, parsed.canonName), style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                entry?.let { "🏺 " + describePantryEntry(it) } ?: "Brak w spiżarni",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (entry != null) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (entry == null) {
                                    val added = addedToShopping[index] == true
                                    TextButton(
                                        onClick = {
                                            onAddToShopping(ingredient)
                                            addedToShopping[index] = true
                                        },
                                        enabled = !added,
                                        modifier = Modifier.heightIn(min = 34.dp),
                                    ) {
                                        Text(if (added) "✓" else "🛒")
                                    }
                                }
                                Button(
                                    onClick = { onToggleHave(parsed.canonName, category, parsed.unitCat) },
                                    modifier = Modifier.heightIn(min = 34.dp),
                                ) {
                                    Text(if (entry != null) "✓ Mam" else "+ Mam to")
                                }
                            }
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

/**
 * FR-19: "📅 Zaplanuj" — lets you push a recipe into any day×category slot,
 * not just its own category (default-selected but freely changeable). Both
 * grids render as multi-row wraps rather than a horizontally scrolling
 * strip, per the revised acceptance criteria in FR-19.md. Port of
 * index.html's openPlanPicker.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PlanPickerDialog(
    recipe: Recipe,
    weekPlan: WeekPlan,
    onPick: (day: Int, cat: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedCat by remember { mutableStateOf(recipe.cat) }
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.widthIn(max = 480.dp)) {
            // FR-59: capped so long content (many cook-history entries,
            // ingredients, or recipe picks) scrolls internally instead of
            // being pushed off-screen unreachably.
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .heightIn(max = 560.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "📅 Zaplanuj: ${recipe.name}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = onDismiss) { Text("✕") }
                }
                Spacer(modifier = Modifier.height(10.dp))
                // FlowRow (not a fixed-size chunked Row) so each chip keeps
                // its natural width and wraps onto a new line by itself --
                // a plain Row squeezes whatever doesn't fit into leftover
                // space instead of wrapping, which for a long Polish label
                // ("Poniedziałek") corrupts into a single-letter-per-line
                // column instead of moving to the next row.
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PlannerOperations.PLANNER_CATEGORIES.forEach { cat ->
                        FilterChip(
                            selected = cat.id == selectedCat,
                            onClick = { selectedCat = cat.id },
                            label = { Text("${cat.emoji} ${cat.label}") },
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PlannerOperations.DAYS_PL.forEachIndexed { day, dayName ->
                        val already = weekPlan[day]?.get(selectedCat)?.recipeId == recipe.id
                        FilterChip(
                            selected = already,
                            onClick = { onPick(day, selectedCat) },
                            label = { Text(dayName + if (already) " ✓" else "") },
                        )
                    }
                }
            }
        }
    }
}

private fun roundTo(value: Double, decimals: Int): Double {
    val factor = Math.pow(10.0, decimals.toDouble())
    return Math.round(value * factor) / factor
}

/** "09.08.2026" — matches index.html's toLocaleDateString('pl-PL', {day/month/year: '2-digit'/'numeric'}). */
private fun formatCookDate(epochMillis: Long): String =
    java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale("pl", "PL")).format(java.util.Date(epochMillis))

/** "13:45" — matches index.html's toLocaleTimeString('pl-PL', {hour/minute: '2-digit'}). */
private fun formatCookTime(epochMillis: Long): String =
    java.text.SimpleDateFormat("HH:mm", java.util.Locale("pl", "PL")).format(java.util.Date(epochMillis))

/**
 * FR-12: per-ingredient kcal/macro breakdown ("📊 Jak policzono: <nazwa>")
 * plus a methodology legend, collapsed by default — port of index.html's
 * openMacroInfoModal + the static #macroInfoOverlay legend text. Also shows
 * the FR-64 micronutrient chips (Micronutrients.estimate) when the recipe
 * has at least one recognized ingredient.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MacroInfoDialog(recipe: Recipe, onDismiss: () -> Unit) {
    var legendExpanded by remember { mutableStateOf(false) }
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.widthIn(max = 480.dp)) {
            // FR-59: capped so long content (many cook-history entries,
            // ingredients, or recipe picks) scrolls internally instead of
            // being pushed off-screen unreachably.
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .heightIn(max = 560.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "📊 Jak policzono: ${recipe.name}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = onDismiss) { Text("✕") }
                }
                Spacer(modifier = Modifier.height(8.dp))

                recipe.calc.forEach { item ->
                    val qtyNote = if (item.qty != 1.0) " × ${formatNum(item.qty)}" else ""
                    Row(modifier = Modifier.padding(vertical = 6.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("${item.label}$qtyNote", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                "baza (${item.unit}): ${item.baseKcal} kcal · " +
                                    "B${formatNum(item.baseP)} W${formatNum(item.baseC)} T${formatNum(item.baseF)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Text(
                            "${item.kcal} kcal\nB${formatNum(item.p)} W${formatNum(item.c)} T${formatNum(item.f)}",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                    HorizontalDivider()
                }

                val rawSum = recipe.calc.sumOf { it.kcal }
                val scaleNote = if (abs(rawSum - recipe.kcal) > max(15, (recipe.kcal * 0.08).roundToInt())) {
                    "Suma z powyższych składników (~$rawSum kcal) skalibrowano do ${recipe.kcal} kcal " +
                        "podanych przy przepisie (proporcjonalnie do B/W/T), by dopasować do znanej kaloryczności dania."
                } else {
                    "Suma powyższych składników (~$rawSum kcal) odpowiada kaloryczności ${recipe.kcal} kcal " +
                        "podanej przy przepisie — bez dodatkowej kalibracji."
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(scaleNote, style = MaterialTheme.typography.bodySmall)

                // FR-64: only when at least one ingredient is on the
                // recognized list -- never a row of zeros.
                val micro = remember(recipe.id) { Micronutrients.estimate(recipe) }
                if (micro != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        MicroChip("🦴 Wapń ~${micro.ca} mg")
                        MicroChip("☀️ Wit. D ~${formatNum(micro.vitD)} µg")
                        MicroChip("🥩 B12 ~${formatNum(micro.b12)} µg")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                TextButton(onClick = { legendExpanded = !legendExpanded }) {
                    Text("ℹ️ Legenda i metodologia (B/W/T, IG, ŁG) ${if (legendExpanded) "⌃" else "⌄"}")
                }
                if (legendExpanded) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                        Text(
                            "B — białko (g) na porcję. Buduje i chroni mięśnie, daje sytość.",
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            "W — węglowodany (g) na porcję, razem z błonnikiem. Główne źródło energii.",
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text("T — tłuszcz (g) na porcję.", style = MaterialTheme.typography.bodySmall)
                        Text(
                            "Błonnik (g) — spowalnia trawienie węglowodanów i wchłanianie cukru.",
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            "IG — indeks glikemiczny (0–100) tego posiłku: jak szybko jego węglowodany " +
                                "podnoszą poziom cukru we krwi. Niżej = wolniej i łagodniej (dobre przy " +
                                "insulinooporności). Liczony jako średnia IG poszczególnych składników " +
                                "ważona ilością węglowodanów, jakie każdy z nich wnosi.",
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            "ŁG — ładunek glikemiczny = (węglowodany [g] × IG) / 100. To realny wpływ całej " +
                                "porcji na cukier — ŁG ≤10 uznaje się za niski, 11–19 za średni, 20+ za wysoki. " +
                                "Liczy się bardziej niż samo IG, bo uwzględnia ilość węgli w porcji, nie tylko " +
                                "ich \"jakość\".",
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            "Jak to policzyliśmy: dla każdego składnika przyjęto standardowe wartości " +
                                "odżywcze (w stylu bazy USDA FoodData Central) oraz publikowane wartości " +
                                "indeksu glikemicznego (baza Uniwersytetu Sydney, glycemicindex.com), a " +
                                "następnie całość skalibrowano tak, by suma kalorii odpowiadała kaloryczności " +
                                "podanej przy przepisie. To rzetelne szacunki na podstawie uznanych źródeł, a " +
                                "nie wyniki laboratoryjnego badania konkretnego dania — realne wartości mogą " +
                                "się nieco różnić w zależności od dokładnych proporcji i sposobu przygotowania.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        // FR-64: the "these are approximate" caveat lives once
                        // here (general background info), not repeated inline
                        // every time a recipe's micro chips show above.
                        Text(
                            Micronutrients.APPROX_NOTE,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MicroChip(text: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 5.dp),
    ) {
        Text(text, style = MaterialTheme.typography.labelSmall)
    }
}
