package com.przemas230.dietaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.przemas230.dietaapp.data.EatenEntry
import com.przemas230.dietaapp.data.PlannedMeal
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.Snack
import com.przemas230.dietaapp.logic.DailyCalorieTargets
import com.przemas230.dietaapp.logic.EatenOperations
import com.przemas230.dietaapp.logic.IngredientCanon
import com.przemas230.dietaapp.logic.PlannerCategory
import com.przemas230.dietaapp.logic.PlannerOperations
import com.przemas230.dietaapp.logic.ProfileCalculations
import com.przemas230.dietaapp.logic.ShoppingDayStrip
import com.przemas230.dietaapp.logic.SnackNutritionDb
import com.przemas230.dietaapp.logic.UiScale
import com.przemas230.dietaapp.logic.WaterOperations
import com.przemas230.dietaapp.logic.forCategory
import com.przemas230.dietaapp.ui.EatenViewModel
import com.przemas230.dietaapp.ui.PantryScreen
import com.przemas230.dietaapp.ui.PantryViewModel
import com.przemas230.dietaapp.ui.PlaceholderScreen
import com.przemas230.dietaapp.ui.PlannerScreen
import com.przemas230.dietaapp.ui.PlannerViewModel
import com.przemas230.dietaapp.ui.ProfileViewModel
import com.przemas230.dietaapp.ui.RecipeListScreen
import com.przemas230.dietaapp.ui.SettingsScreen
import com.przemas230.dietaapp.ui.ShoppingScreen
import com.przemas230.dietaapp.ui.ShoppingViewModel
import com.przemas230.dietaapp.ui.SwipeRatingStyleViewModel
import com.przemas230.dietaapp.ui.UiScaleViewModel
import com.przemas230.dietaapp.ui.WaterViewModel
import com.przemas230.dietaapp.ui.navigation.BOTTOM_NAV_SCREENS
import com.przemas230.dietaapp.ui.navigation.Screen
import com.przemas230.dietaapp.ui.theme.DietaAppTheme
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.max
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // FR-14: Compose has no CSS-zoom equivalent, so the whole app's
            // "zoom" is done by scaling LocalDensity around everything below
            // this point — including the top bar/bottom nav (this wraps them
            // too, matching zoom's effect on position:fixed elements in the
            // web version) — rather than scaling individual screens.
            val uiScaleViewModel: UiScaleViewModel = viewModel()
            val customScale by uiScaleViewModel.uiScale.collectAsState()
            val screenWidthDp = LocalConfiguration.current.screenWidthDp
            val effectiveScale = customScale ?: UiScale.detectDefault(screenWidthDp)
            val baseDensity = LocalDensity.current
            CompositionLocalProvider(
                LocalDensity provides Density(
                    density = baseDensity.density * effectiveScale.toFloat(),
                    fontScale = baseDensity.fontScale,
                ),
            ) {
                DietaAppTheme {
                    DietaAppRoot(uiScaleViewModel = uiScaleViewModel, effectiveScale = effectiveScale)
                }
            }
        }
    }
}

/**
 * App-level shell: one top bar (title + Ustawienia action, matching the web
 * app's header gear icon — see FR list in android/PARITY.md), one bottom
 * nav row mirroring index.html's `nav.bottom`, and a NavHost swapping the
 * screen content underneath both. Every tab besides Przepisy is currently
 * a PlaceholderScreen — see android/README.md "Co dalej" for the build order.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DietaAppRoot(uiScaleViewModel: UiScaleViewModel, effectiveScale: Double) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    // Created here (not inside SettingsScreen's default viewModel() param) so
    // it's scoped to the whole NavHost/Activity — the header subtitle below
    // and the profile form on the Ustawienia screen must share one instance,
    // otherwise Compose Navigation would hand each destination its own.
    val profileViewModel: ProfileViewModel = viewModel()
    val profile by profileViewModel.profile.collectAsState()
    val displayName by profileViewModel.displayName.collectAsState()
    // FR-15: shared here (not PantryScreen's default viewModel() param) so
    // marking a recipe "✅ Zrobione" on the Przepisy tab and viewing the
    // resulting stock change on the Spiżarnia tab see the same instance —
    // same reasoning as profileViewModel above.
    val pantryViewModel: PantryViewModel = viewModel()
    // FR-16: shared for the same reason -- the "🛒" per-ingredient add button
    // in the recipe pantry-check window must show up on the Zakupy tab too.
    val shoppingViewModel: ShoppingViewModel = viewModel()
    // FR-19: shared so "📅 Zaplanuj" on a recipe card (Przepisy tab) and the
    // Planer tab itself see the same week plan.
    val plannerViewModel: PlannerViewModel = viewModel()
    // FR-61: shared so a change on the Ustawienia screen is reflected
    // immediately by the swipe-drag card on the Przepisy tab.
    val swipeRatingStyleViewModel: SwipeRatingStyleViewModel = viewModel()
    // FR-70: shared at the Scaffold level (not per-screen) so the header
    // droplet strip below is the single source of truth, visible on every tab.
    val waterViewModel: WaterViewModel = viewModel()
    // FR-36: shared for the same reason as waterViewModel above.
    val eatenViewModel: EatenViewModel = viewModel()
    val eatenEntries by eatenViewModel.entries.collectAsState()
    val snacks by eatenViewModel.snacks.collectAsState()
    var showQuickAddDialog by remember { mutableStateOf(false) }
    val weekPlan by plannerViewModel.weekPlan.collectAsState()
    val allRecipes by plannerViewModel.allRecipes.collectAsState()
    val recipesById = remember(allRecipes) { allRecipes.associateBy { it.id } }
    val todayIdx = remember { ShoppingDayStrip.todayIndex(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1) }
    val todayMeals = weekPlan[todayIdx].orEmpty()
    val kcalTargets = remember(profile) { ProfileCalculations.calcTargets(profile) }
    val plannedTodayKcal = remember(weekPlan, recipesById, todayIdx) {
        PlannerOperations.dayTotalKcal(weekPlan, todayIdx, recipesById)
    }
    // FR-44/45 (auto-hide-on-scroll + manual-override precedence) aren't
    // ported -- this is just the simpler "starts expanded on Przepisy,
    // collapsed elsewhere, manually toggleable" base behavior index.html's
    // switchView already had before those two were layered on top of it.
    // Keyed on currentRoute so switching tabs resets to that tab's default,
    // same as index.html's `if(name!=="recipes") headerEl.classList.add("collapsed")`.
    var headerExpanded by remember(currentRoute) { mutableStateOf(currentRoute == Screen.Recipes.route) }

    Scaffold(
        topBar = {
            // FR-36's ring/meal-list/summary panel is tall enough that it
            // does NOT fit inside TopAppBar's own `title` slot -- that slot
            // silently clips content past a fixed max height (confirmed by
            // testing: even a single always-composed debug Text placed after
            // HeaderWaterRow there never appeared). Scaffold's topBar slot
            // itself has no such cap, so the water row and kcal panel live
            // here instead, as siblings after TopAppBar inside one
            // teal-background Column, not nested in its title.
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.primary)) {
                TopAppBar(
                    title = {
                        Column(modifier = Modifier.clickable { headerExpanded = !headerExpanded }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Dieta App")
                                Text(
                                    if (headerExpanded) "⌃" else "⌄",
                                    modifier = Modifier.padding(start = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            }
                            val namePrefix = if (displayName.isNotBlank()) "$displayName · " else ""
                            val subtitle = if (!profile.configured) {
                                "${namePrefix}👋 Ustaw swój profil w Ustawieniach, aby dopasować dietę do siebie"
                            } else {
                                "$namePrefix${profile.sex.label}, ${profile.age} lat · ${profile.heightCm} cm · " +
                                    "${formatWeight(profile.weightKg)} kg → cel ${formatWeight(profile.targetWeightKg)} kg · " +
                                    "${profile.goal.headerLabel} · ~${kcalTargets.daily} kcal/dzień"
                            }
                            Text(
                                subtitle,
                                style = MaterialTheme.typography.labelSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    actions = {
                        // FR-33: global quick-add, visible on every tab (matches
                        // index.html's header "➕" button) -- the Postęp tab's
                        // OWN floating-button entry point to this same dialog
                        // isn't ported, since that tab is still a placeholder.
                        IconButton(onClick = { showQuickAddDialog = true }) {
                            Icon(Icons.Filled.Add, contentDescription = "Dodaj przekąskę lub dodatkowe danie")
                        }
                        IconButton(onClick = {
                            navController.navigate(Screen.Settings.route) { launchSingleTop = true }
                        }) {
                            Icon(Screen.Settings.icon, contentDescription = Screen.Settings.label)
                        }
                    },
                )
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    HeaderWaterRow(waterViewModel)
                    if (headerExpanded) {
                        HeaderKcalPanel(
                            plannedKcal = plannedTodayKcal,
                            targetKcal = kcalTargets.daily,
                            kcalTargets = kcalTargets,
                            todayMeals = todayMeals,
                            recipesById = recipesById,
                            eatenEntries = eatenEntries,
                            snacks = snacks,
                            onToggleEaten = { cat, kcal, name -> eatenViewModel.toggle(cat, kcal, name) },
                            onRemoveSnack = { id -> eatenViewModel.removeSnack(id) },
                        )
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar {
                BOTTOM_NAV_SCREENS.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                    )
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Recipes.route,
            modifier = Modifier.padding(padding),
        ) {
            composable(Screen.Recipes.route) {
                RecipeListScreen(
                    profileViewModel = profileViewModel,
                    pantryViewModel = pantryViewModel,
                    shoppingViewModel = shoppingViewModel,
                    plannerViewModel = plannerViewModel,
                    swipeRatingStyleViewModel = swipeRatingStyleViewModel,
                )
            }
            composable(Screen.Shopping.route) {
                ShoppingScreen(viewModel = shoppingViewModel, plannerViewModel = plannerViewModel)
            }
            composable(Screen.Planner.route) {
                PlannerScreen(
                    plannerViewModel = plannerViewModel,
                    profileViewModel = profileViewModel,
                    shoppingViewModel = shoppingViewModel,
                )
            }
            composable(Screen.Progress.route) { PlaceholderScreen(Screen.Progress.label) }
            composable(Screen.Pantry.route) { PantryScreen(viewModel = pantryViewModel) }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    profileViewModel = profileViewModel,
                    uiScaleViewModel = uiScaleViewModel,
                    effectiveUiScale = effectiveScale,
                    swipeRatingStyleViewModel = swipeRatingStyleViewModel,
                )
            }
        }
    }

    if (showQuickAddDialog) {
        QuickAddSnackDialog(
            onDismiss = { showQuickAddDialog = false },
            onAdd = { name, kcal ->
                eatenViewModel.addSnack(name, kcal)
                showQuickAddDialog = false
            },
        )
    }
}

/** "67.0" -> "67", "67.5" -> "67.5" — matches how JS template literals print numbers. */
private fun formatWeight(value: Double): String =
    if (value == value.toLong().toDouble()) value.toLong().toString() else value.toString()

/**
 * FR-70: compact droplet strip, always visible in the header on every tab
 * (not gated by a collapse state -- FR-44/45's header auto-hide isn't
 * ported yet). Each droplet is its own tap target -- port of index.html's
 * renderHeaderWater, minus the Postępy tab's full water view to mirror it
 * with (that tab is still a placeholder, see android/PARITY.md).
 */
@Composable
private fun HeaderWaterRow(viewModel: WaterViewModel) {
    val count by viewModel.count.collectAsState()
    Row(
        modifier = Modifier.padding(top = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        for (i in 0 until WaterOperations.MAX_LEVEL) {
            Text(
                if (i < count) "💧" else "⚪",
                fontSize = 12.sp,
                modifier = Modifier.clickable { viewModel.tapDroplet(i) },
            )
        }
        Text(
            "$count/${WaterOperations.MAX_LEVEL}",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}

/**
 * FR-36: today's kcal ring (planned kcal vs daily target -- NOT eaten kcal;
 * index.html's own renderHeader computes the ring from todaysPlannedKcal(),
 * independent of the eaten toggle below it, so this is a faithful port even
 * though it means marking something eaten doesn't move the ring itself,
 * only the "Zjedzone/Zostało" line), the 5 Planer-slot rows (swipe right to
 * toggle eaten), and the eaten-kcal summary line. Only shown while
 * headerExpanded, in DietaAppRoot.
 */
@Composable
private fun HeaderKcalPanel(
    plannedKcal: Int,
    targetKcal: Int,
    kcalTargets: DailyCalorieTargets,
    todayMeals: Map<String, PlannedMeal>,
    recipesById: Map<String, Recipe>,
    eatenEntries: Map<String, EatenEntry>,
    snacks: List<Snack>,
    onToggleEaten: (cat: String, plannedKcal: Int?, plannedName: String?) -> Unit,
    onRemoveSnack: (id: String) -> Unit,
) {
    val pct = if (targetKcal > 0) (plannedKcal.toFloat() / targetKcal).coerceIn(0f, 1f) else 0f
    Column(modifier = Modifier.padding(top = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(60.dp)) {
                CircularProgressIndicator(
                    progress = { pct },
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 7.dp,
                    color = Color(0xFFE8B93C),
                    trackColor = Color.White.copy(alpha = 0.22f),
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(plannedKcal.toString(), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                    Text("z $targetKcal kcal", fontSize = 8.sp, color = Color.White.copy(alpha = 0.85f))
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                PlannerOperations.PLANNER_CATEGORIES.forEach { category ->
                    val meal = todayMeals[category.id]
                    val recipe = meal?.let { recipesById[it.recipeId] }
                    KcalMealRow(
                        category = category,
                        targetKcal = kcalTargets.forCategory(category.id) ?: 0,
                        recipe = recipe,
                        eaten = EatenOperations.isEaten(eatenEntries, category.id),
                        onToggle = { onToggleEaten(category.id, recipe?.kcal, recipe?.name) },
                    )
                }
            }
        }
        // FR-33/34: ad-hoc snacks, each removable -- port of index.html's
        // per-snack "×" delete in the "co zjadłaś/eś" list.
        snacks.forEach { snack ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 1.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "🍿 ${snack.name} · ${snack.kcal} kcal",
                    color = Color.White,
                    fontSize = 10.sp,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    "✕",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 10.sp,
                    modifier = Modifier.clickable { onRemoveSnack(snack.id) }.padding(start = 4.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        val eatenKcal = EatenOperations.dailyEatenKcal(eatenEntries) + EatenOperations.snacksKcal(snacks)
        val remaining = max(0, targetKcal - eatenKcal)
        Text(
            "🍽️ Zjedzone: $eatenKcal kcal · Zostało: $remaining kcal",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
        )
    }
}

/**
 * One Planer-slot row -- swipe right past the commit threshold to toggle
 * eaten (only when a recipe is actually planned there, matching
 * index.html's `.eatable` class gate on attachSwipeToEat). Right-only drag,
 * clamped to [0, maxPx], same shape as index.html's SWIPE_MAX/SWIPE_COMMIT
 * but in dp instead of raw CSS px.
 */
@Composable
private fun KcalMealRow(
    category: PlannerCategory,
    targetKcal: Int,
    recipe: Recipe?,
    eaten: Boolean,
    onToggle: () -> Unit,
) {
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val maxPx = with(density) { 40.dp.toPx() }
    val commitPx = with(density) { 22.dp.toPx() }
    val dragModifier = if (recipe != null) {
        Modifier.pointerInput(category.id) {
            detectHorizontalDragGestures(
                onDragEnd = {
                    val committed = offsetX.value > commitPx
                    scope.launch {
                        if (committed) onToggle()
                        offsetX.animateTo(0f)
                    }
                },
                onDragCancel = { scope.launch { offsetX.animateTo(0f) } },
            ) { change, dragAmount ->
                change.consume()
                scope.launch { offsetX.snapTo((offsetX.value + dragAmount).coerceIn(0f, maxPx)) }
            }
        }
    } else {
        Modifier
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
            .then(dragModifier)
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (eaten) {
            Text("✓", color = Color(0xFF5AD28C), fontSize = 10.sp, modifier = Modifier.padding(end = 2.dp))
        }
        Column {
            Text(
                "${category.emoji} ${category.label} · $targetKcal kcal",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
            )
            if (recipe != null) {
                Text(
                    recipe.name,
                    color = Color.White.copy(alpha = if (eaten) 0.55f else 0.72f),
                    fontSize = 9.sp,
                    textDecoration = if (eaten) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

/**
 * FR-33/34: the "➕" quick-add dialog -- free-text name (auto-filled kcal via
 * SnackNutritionDb.estimate, editable, with a startsWith-then-contains
 * suggestion list from 2 typed characters, matching index.html's
 * buildSnackAddForm/renderSuggestions) plus a manual kcal field.
 */
@Composable
private fun QuickAddSnackDialog(onDismiss: () -> Unit, onAdd: (name: String, kcal: Int) -> Unit) {
    var name by remember { mutableStateOf("") }
    var kcalText by remember { mutableStateOf("") }
    var kcalWasAutoFilled by remember { mutableStateOf(false) }

    val estimate = remember(name) { SnackNutritionDb.estimate(name) }
    LaunchedEffect(estimate) {
        if (estimate != null) {
            kcalText = estimate.kcal.toString()
            kcalWasAutoFilled = true
        } else if (kcalWasAutoFilled) {
            kcalText = ""
            kcalWasAutoFilled = false
        }
    }
    val suggestions = remember(name) {
        val q = name.trim().lowercase()
        if (q.length < 2) {
            emptyList()
        } else {
            val names = SnackNutritionDb.TABLE.keys
            val starts = names.filter { it.startsWith(q) }
            val includes = names.filter { !it.startsWith(q) && it.contains(q) }
            (starts + includes).take(8)
        }
    }

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
                        "➕ Dodaj przekąskę lub dodatkowe danie",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = onDismiss) { Text("✕") }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Co zjadłaś/eś?") },
                    placeholder = { Text("np. 1 banan, 150g ryżu, jogurt 200g") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                if (suggestions.isNotEmpty()) {
                    Column {
                        suggestions.forEach { suggestion ->
                            // FR-35: emoji suffix in the suggestion, matching index.html's renderSuggestions.
                            Text(
                                IngredientCanon.withEmoji(suggestion, suggestion),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { name = suggestion }
                                    .padding(vertical = 6.dp),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                } else if (name.trim().length >= 2) {
                    Text(
                        if (estimate != null) {
                            "≈ ${estimate.kcal} kcal (${estimate.basis}) — możesz poprawić ręcznie"
                        } else {
                            "Nie rozpoznano produktu — podaj kcal ręcznie"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = kcalText,
                        onValueChange = { kcalText = it; kcalWasAutoFilled = false },
                        label = { Text("kcal") },
                        singleLine = true,
                        modifier = Modifier.width(110.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val kcal = kcalText.toIntOrNull()
                            if (name.isNotBlank() && kcal != null && kcal > 0) {
                                // FR-35: emoji suffix baked into the saved snack name, matching index.html's withEmoji(rawName, est.canon).
                                val savedName = estimate?.let { IngredientCanon.withEmoji(name.trim(), it.canonName) } ?: name.trim()
                                onAdd(savedName, kcal)
                            }
                        },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("+ Dodaj")
                    }
                }
            }
        }
    }
}
