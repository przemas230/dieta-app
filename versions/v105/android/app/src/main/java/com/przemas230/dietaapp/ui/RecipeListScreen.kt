package com.przemas230.dietaapp.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.runtime.withFrameNanos
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.przemas230.dietaapp.ui.theme.LocalDietaThemeId
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.przemas230.dietaapp.R
import com.przemas230.dietaapp.data.CookEntry
import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.ShoppingItem
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.data.RecipeReview
import com.przemas230.dietaapp.logic.AppThemes
import com.przemas230.dietaapp.logic.CATEGORIES
import com.przemas230.dietaapp.logic.CustomRecipeOperations
import com.przemas230.dietaapp.logic.DailyCalorieTargets
import com.przemas230.dietaapp.logic.IngredientCanon
import com.przemas230.dietaapp.logic.IngredientMacroEstimation
import com.przemas230.dietaapp.logic.Micronutrients
import com.przemas230.dietaapp.logic.PantryOperations
import com.przemas230.dietaapp.logic.PlannerOperations
import com.przemas230.dietaapp.logic.ProfileCalculations
import com.przemas230.dietaapp.logic.RecipeMatching
import com.przemas230.dietaapp.logic.RecipePantryMatching
import com.przemas230.dietaapp.logic.RecipeReviewOperations
import com.przemas230.dietaapp.logic.ShoppingOperations
import com.przemas230.dietaapp.logic.WeekPlan
import com.przemas230.dietaapp.logic.forCategory
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

/**
 * Requested 2026-08-26 ("czcionka podoba się to dla mnie i nie podoba się
 * w Android jest inna niż na Web"): matches index.html's
 * `.swipe-label-global{font-family:"Baloo 2","Quicksand",sans-serif;
 * font-weight:800}` (FR-56's balloon feedback label) -- a chunky, rounded
 * display font, ExtraBold (800) being its heaviest static weight, same as
 * web's. Same variable-font pattern as ClinicTheme.kt's SpaceGrotesk/DMSans
 * (one .ttf file, one FontVariation.Settings per weight) -- but this one
 * applies regardless of theme (the swipe gesture/label itself isn't
 * Klinika-specific), so it's defined here rather than in ClinicTheme.kt.
 */
@OptIn(ExperimentalTextApi::class)
private val SwipeLabelFont = FontFamily(
    Font(R.font.baloo2_variable, FontWeight.ExtraBold, variationSettings = FontVariation.Settings(FontVariation.weight(800))),
)

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
    favoriteIngredientsViewModel: FavoriteIngredientsViewModel,
    activityLogViewModel: ActivityLogViewModel,
    viewModel: RecipeViewModel = viewModel(),
    // FR-44: hoisted (not the default rememberLazyListState()) so
    // MainActivity's header can observe scroll direction to auto-hide/show
    // itself -- see DietaAppRoot's headerExpanded auto-scroll LaunchedEffect.
    listState: LazyListState = rememberLazyListState(),
    // FR-77: hoisted (not a default viewModel() param) so CommunityCoordinator
    // can invalidate() an expanded comment thread from outside this screen.
    commentsViewModel: RecipeCommentsViewModel = viewModel(),
    // 2026-08-11: mirrors MainActivity's own headerExpanded -- the search/
    // filter bar now hides together with the header (same near-top-only +
    // manual-override state), since it took up too much of the screen
    // pinned visible the whole time scrolled into a long recipe list.
    headerExpanded: Boolean = true,
    // FR-66/v2 (2026-08-11): hoisted (was an internal `remember`) -- the
    // trigger moved from an inline "➕ Dodaj swój przepis" button to a
    // floating "📖" FAB that lives in MainActivity's Scaffold (Przepisy-only,
    // same reasoning as the FR-32/v2 floating "💡"), so MainActivity now owns
    // when this dialog opens; this screen still owns rendering it and
    // resetting the flag on dismiss/submit.
    showAddRecipeDialog: Boolean = false,
    onAddRecipeDialogDismiss: () -> Unit = {},
) {
    val swipeRatingStyle by swipeRatingStyleViewModel.style.collectAsState()
    val recipes by viewModel.visibleRecipes.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchTerm by viewModel.searchTerm.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val cookedMap by viewModel.cooked.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val pantryItems by pantryViewModel.items.collectAsState()
    val weekPlan by plannerViewModel.weekPlan.collectAsState()
    val shoppingItems by shoppingViewModel.items.collectAsState()
    val profile by profileViewModel.profile.collectAsState()
    val favIngredients by favoriteIngredientsViewModel.favorites.collectAsState()
    val favoriteRecipeIds by viewModel.favoriteRecipes.collectAsState()
    LaunchedEffect(profile.glutenFree, profile.lactoseFree) {
        viewModel.setDietaryFilters(profile.glutenFree, profile.lactoseFree)
    }

    // 2026-08-11 (user request): categories used to show/hide together with
    // the search field and the rest of the filter chips below, all keyed
    // off the SAME `headerExpanded` MainActivity passes in -- so scrolling
    // (which auto-collapses `headerExpanded`, FR-44) hid category switching
    // along with everything else. Now its own panel with its own toggle,
    // collapsed by default (`false`, not tied to `headerExpanded`'s
    // initial-expanded-on-Przepisy default) and NOT affected by scroll --
    // switching categories stays reachable even once the header has
    // auto-collapsed. Mirrors the header's own tap-to-toggle/chevron pattern
    // (MainActivity.kt's TopAppBar title).
    var categoryPanelExpanded by remember { mutableStateOf(false) }
    // 2026-08-11: compact "🔍" search dropdown, see IngredientSearchDialog.
    var showSearchDropdown by remember { mutableStateOf(false) }
    var sortByMatch by remember { mutableStateOf(false) }
    var sortByReview by remember { mutableStateOf(false) }
    // FR-2: independent filter toggles, applied before the three sorts above
    // -- same order as index.html's renderRecipes() (favorites -> ingredient
    // favorites -> pantry-ready -> user recipes -> rating threshold -> sorts).
    var onlyFavorites by remember { mutableStateOf(false) }
    var onlyIngFav by remember { mutableStateOf(false) }
    var onlyPantryReady by remember { mutableStateOf(false) }
    var onlyUserRecipes by remember { mutableStateOf(false) }
    var minRatingFilter by remember { mutableStateOf(0) }
    // 2026-08-11: "❤️ Podoba się" chip, see its own doc comment below.
    var onlyLiked by remember { mutableStateOf(false) }
    val macroTargets = remember(profile) { ProfileCalculations.calcMacroTargets(profile) }
    val kcalTargets = remember(profile) { ProfileCalculations.calcTargets(profile) }
    // FR-87: motyw "Klinika" pokazuje kategorie jako od razu widoczny rząd
    // chipów zamiast panelu rozwijanego stukiem -- ten sam selectedCategory/
    // viewModel.selectCategory co reszta motywów, tylko inny układ.
    val isClinic = AppThemes.isClinicFamily(LocalDietaThemeId.current)
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
    val displayedRecipes = remember(
        recipes, onlyFavorites, onlyIngFav, onlyPantryReady, onlyUserRecipes, minRatingFilter, onlyLiked,
        sortByMatch, sortByReview, matchScores, reviews,
        favoriteRecipeIds, favIngredients, pantryItems,
    ) {
        // FR-2/FR-67: independent toggles applied in sequence (matches
        // index.html's own if(onlyFav){...} if(onlyIngFav){...} ...
        // if(sortByMatch){...} if(sortByReview){...}), not combined into one
        // composite key.
        var result = recipes
        if (onlyFavorites) result = result.filter { it.id in favoriteRecipeIds }
        if (onlyIngFav) result = result.filter { r ->
            r.ingredients.any { RecipePantryMatching.parseIngredient(it).canonName in favIngredients }
        }
        if (onlyPantryReady) result = result.filter { RecipePantryMatching.pantryCoverageRatio(it, pantryItems) >= 0.6 }
        // FR-76: "user recipes" now covers own recipes AND approved
        // community recipes from other users, same as index.html's criterion.
        if (onlyUserRecipes) result = result.filter { it.source == "custom" || it.source == "community" }
        if (minRatingFilter > 0) result = result.filter { (reviews[it.id]?.stars ?: 0) >= minRatingFilter }
        // 2026-08-11: same >=4 "liked" cutoff RecipeCard's border tint/swipe
        // feedback already use (see class doc there) -- independent of
        // minRatingFilter above, which is a single-select 0/3/4/5 radio,
        // not a togglable shortcut for exactly "dania, które lubię".
        if (onlyLiked) result = result.filter { (reviews[it.id]?.stars ?: 0) >= 4 }
        if (sortByMatch) result = result.sortedByDescending { matchScores[it.id] ?: -1 }
        if (sortByReview) result = RecipeReviewOperations.sortByReview(result, reviews)
        result
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 2026-08-11: standalone category panel, see categoryPanelExpanded's
        // doc comment above -- always rendered (not gated on headerExpanded),
        // collapsed by default, shows the currently selected category in its
        // own collapsed header row so switching categories is still visible
        // at a glance even collapsed.
        if (isClinic) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            ) {
                items(CATEGORIES) { category ->
                    FilterChip(
                        selected = category.id == selectedCategory,
                        onClick = { viewModel.selectCategory(category.id) },
                        label = { Text("${category.emoji} ${category.label}") },
                        shape = MaterialTheme.shapes.extraLarge,
                    )
                }
            }
        } else {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
            val currentCategory = CATEGORIES.find { it.id == selectedCategory }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { categoryPanelExpanded = !categoryPanelExpanded },
            ) {
                Text(
                    "Kategoria: ${currentCategory?.emoji ?: "🍽️"} ${currentCategory?.label ?: ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                )
                Text(if (categoryPanelExpanded) "⌃" else "⌄", style = MaterialTheme.typography.labelSmall)
            }
            AnimatedVisibility(visible = categoryPanelExpanded) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                ) {
                    items(CATEGORIES) { category ->
                        FilterChip(
                            selected = category.id == selectedCategory,
                            onClick = { viewModel.selectCategory(category.id) },
                            label = { Text("${category.emoji} ${category.label}") },
                        )
                    }
                }
            }
        }
        }
        AnimatedVisibility(visible = headerExpanded) {
        Column {
        // 2026-08-11 (user request): compact search -- was a full-width
        // always-open OutlinedTextField, now just the "🔍" icon (matching
        // "zostaw samą lupkę") plus, only while a search is actually active,
        // the current term with a "✕" to clear it (otherwise an active
        // filter would be invisible/impossible to undo without reopening
        // the dropdown). Tapping either opens IngredientSearchDialog.
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
        ) {
            IconButton(onClick = { showSearchDropdown = true }) {
                Text("🔍", style = MaterialTheme.typography.titleMedium)
            }
            if (searchTerm.isNotBlank()) {
                Text(
                    searchTerm,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showSearchDropdown = true },
                )
                TextButton(onClick = { viewModel.setSearchTerm("") }) { Text("✕") }
            }
        }
        if (showSearchDropdown) {
            IngredientSearchDialog(
                currentTerm = searchTerm,
                ingredientNames = remember { viewModel.uniqueIngredientNames() },
                onSelect = { term ->
                    viewModel.setSearchTerm(term)
                    showSearchDropdown = false
                },
                onDismiss = { showSearchDropdown = false },
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp),
        ) {
            item {
                // FR-11/FR-2: sorts the visible list by 🎯 match-to-profile score, descending.
                FilterChip(
                    selected = sortByMatch,
                    onClick = { sortByMatch = !sortByMatch },
                    label = { Text("🎯 Dopasowanie") },
                )
            }
            item {
                // FR-67: sorts by the 1-5 star review, descending, unrated
                // last. 2026-08-11: this now also covers what the separate
                // "❤️ Ranking" chip used to do (swipe like/dislike sort) --
                // both read the same unified rating now, so having two
                // chips for the same thing was redundant. See
                // RecipeViewModel.setRatingQuick.
                FilterChip(
                    selected = sortByReview,
                    onClick = { sortByReview = !sortByReview },
                    label = { Text("🏆 Ocena") },
                )
            }
            item {
                // FR-2: only recipes starred ⭐ (index.html's onlyFav/state.favorites).
                FilterChip(
                    selected = onlyFavorites,
                    onClick = { onlyFavorites = !onlyFavorites },
                    label = { Text("⭐ Ulubione") },
                )
            }
            item {
                // 2026-08-11 (user request, "filtrowanie po daniach które
                // są zaznaczone jako podoba się to dla mnie"): dedicated
                // shortcut for the exact swipe-right label ("❤️ Podoba się
                // to dla mnie!", RecipeCard's swipe feedback) -- the
                // underlying data already existed (stars>=4 IS "liked" per
                // the unified rating's own >=4/<=2 convention, reachable
                // before this only via the generic "★4+"/"★5" threshold
                // chips below), this just makes it directly discoverable
                // under the name the user actually used, independent of
                // minRatingFilter's single-select radio-like behavior.
                FilterChip(
                    selected = onlyLiked,
                    onClick = { onlyLiked = !onlyLiked },
                    label = { Text("❤️ Podoba się") },
                )
            }
            item {
                // FR-2: only recipes containing at least one ☆-starred favorite ingredient.
                FilterChip(
                    selected = onlyIngFav,
                    onClick = { onlyIngFav = !onlyIngFav },
                    label = { Text("🌟 Ulub. składniki") },
                )
            }
            item {
                // FR-2: only recipes ≥60% coverable from the current pantry.
                FilterChip(
                    selected = onlyPantryReady,
                    onClick = { onlyPantryReady = !onlyPantryReady },
                    label = { Text("🏺 Ze spiżarni") },
                )
            }
            item {
                // FR-2/FR-66/FR-76: own recipes + approved community recipes.
                FilterChip(
                    selected = onlyUserRecipes,
                    onClick = { onlyUserRecipes = !onlyUserRecipes },
                    label = { Text("🧑‍🍳 Moje przepisy") },
                )
            }
            items(listOf(0, 3, 4, 5)) { threshold ->
                // FR-2: rating-threshold filter -- a chip row (not a native
                // dropdown) to stay consistent with every other toggle on
                // this bar; "Dowolna ocena" (0) turns the filter off entirely.
                FilterChip(
                    selected = minRatingFilter == threshold,
                    onClick = { minRatingFilter = threshold },
                    label = {
                        Text(
                            when (threshold) {
                                0 -> "Dowolna ocena"
                                5 -> "★ 5"
                                else -> "★ $threshold+"
                            },
                        )
                    },
                )
            }
        }
        }
        }

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
                pantryItems,
                weekPlan,
                shoppingItems,
                kcalTargets,
                viewModel,
                pantryViewModel,
                shoppingViewModel,
                plannerViewModel,
                swipeRatingStyle,
                favIngredients,
                favoriteIngredientsViewModel::toggle,
                reviews,
                activityLogViewModel,
                listState,
                favoriteRecipeIds,
                viewModel::toggleFavoriteRecipe,
                commentsViewModel,
            )
        }
    }

    if (showAddRecipeDialog) {
        AddCustomRecipeDialog(
            initialCat = selectedCategory,
            onAdd = { input -> viewModel.addCustomRecipe(input) },
            onDismiss = onAddRecipeDialogDismiss,
        )
    }
}

/**
 * 2026-08-11 (compact search, user request): opened by the "🔍" icon --
 * free-text field at top (still searches recipe name OR ingredient text,
 * same as before via [onSelect]/`viewModel.setSearchTerm`, RecipeBrowsing's
 * existing substring match) plus a scrollable list of every distinct
 * ingredient name across all known recipes ([RecipeViewModel.uniqueIngredientNames]),
 * filtered live by whatever's typed, tap-to-select. Confirming via typed
 * text and tapping a list entry both go through the same [onSelect].
 */
@Composable
private fun IngredientSearchDialog(
    currentTerm: String,
    ingredientNames: List<String>,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var draft by remember { mutableStateOf(currentTerm) }
    val filtered = remember(draft, ingredientNames) {
        val query = draft.trim().lowercase()
        if (query.isBlank()) ingredientNames else ingredientNames.filter { it.lowercase().contains(query) }
    }
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "🔍 Szukaj przepisu lub składnika",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = onDismiss) { Text("✕") }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = draft,
                    onValueChange = { draft = it },
                    label = { Text("Nazwa przepisu lub składnika…") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { onSelect(draft.trim()) },
                    enabled = draft.isNotBlank(),
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("Szukaj „${draft.trim()}”") }
                Spacer(modifier = Modifier.height(12.dp))
                Text("…albo wybierz składnik z listy:", style = MaterialTheme.typography.labelMedium)
                LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                    if (filtered.isEmpty()) {
                        item {
                            Text(
                                "Brak pasujących składników.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 10.dp),
                            )
                        }
                    }
                    items(filtered) { name ->
                        Text(
                            name,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(name) }
                                .padding(vertical = 10.dp),
                        )
                    }
                }
            }
        }
    }
}

/**
 * FR-66: name/category/time/ingredients(one per line)/method/kcal(required)
 * + optional protein/carbs/fat -- port of index.html's "➕ Dodaj swój
 * przepis" form, including live macro auto-calculation from the ingredients
 * text (IngredientMacroEstimation, port of estimateRecipeMacrosFromText) --
 * see the kcalDirty/proteinDirty/carbsDirty/fatDirty flags below for the
 * "auto-calc fills it until you type into it yourself" behavior.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddCustomRecipeDialog(
    initialCat: String,
    onAdd: (CustomRecipeOperations.Input) -> CustomRecipeOperations.ValidationError?,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var cat by remember { mutableStateOf(PlannerOperations.PLANNER_CATEGORIES.find { it.id == initialCat }?.id ?: PlannerOperations.PLANNER_CATEGORIES.first().id) }
    var time by remember { mutableStateOf("") }
    var ingredientsText by remember { mutableStateOf("") }
    var method by remember { mutableStateOf("") }
    var inspirationSourceText by remember { mutableStateOf("") }
    var kcalText by remember { mutableStateOf("") }
    var proteinText by remember { mutableStateOf("") }
    var carbsText by remember { mutableStateOf("") }
    var fatText by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<CustomRecipeOperations.ValidationError?>(null) }
    // FR-66: "dirty" flags -- port of index.html's arMacroDirty. Auto-calc
    // keeps filling these fields live as ingredientsText changes, but the
    // moment the user types into one of them directly, their number wins
    // from then on (auto-calc never overwrites a deliberately-entered value).
    var kcalDirty by remember { mutableStateOf(false) }
    var proteinDirty by remember { mutableStateOf(false) }
    var carbsDirty by remember { mutableStateOf(false) }
    var fatDirty by remember { mutableStateOf(false) }
    var autoCalcHint by remember {
        mutableStateOf("Wpisz składniki, a kalorie i makroskładniki obliczą się automatycznie.")
    }
    LaunchedEffect(ingredientsText) {
        val result = IngredientMacroEstimation.estimateRecipeMacrosFromText(ingredientsText)
        autoCalcHint = when {
            result.total == 0 -> "Wpisz składniki, a kalorie i makroskładniki obliczą się automatycznie."
            result.matched == 0 -> "Nie rozpoznano żadnego z ${result.total} składników — wpisz kalorie i makroskładniki ręcznie."
            else -> "Rozpoznano ${result.matched} z ${result.total} składników — obliczono na tej podstawie (możesz poprawić ręcznie)."
        }
        if (result.matched == 0) return@LaunchedEffect
        if (!kcalDirty) kcalText = result.kcal.toString()
        if (!proteinDirty) proteinText = formatNum(result.protein)
        if (!carbsDirty) carbsText = formatNum(result.carbs)
        if (!fatDirty) fatText = formatNum(result.fat)
    }

    fun errorText(e: CustomRecipeOperations.ValidationError) = when (e) {
        CustomRecipeOperations.ValidationError.MissingName -> "Podaj nazwę przepisu."
        CustomRecipeOperations.ValidationError.MissingIngredients -> "Dodaj przynajmniej jeden składnik (jeden na linię)."
        CustomRecipeOperations.ValidationError.InvalidKcal -> "Podaj dodatnią liczbę kalorii."
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
                        "➕ Dodaj swój przepis",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = onDismiss) { Text("✕") }
                }
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nazwa przepisu") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Kategoria", style = MaterialTheme.typography.labelMedium)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(top = 4.dp)) {
                    PlannerOperations.PLANNER_CATEGORIES.forEach { category ->
                        FilterChip(
                            selected = category.id == cat,
                            onClick = { cat = category.id },
                            label = { Text("${category.emoji} ${category.label}") },
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Czas przygotowania (np. 15 min)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = ingredientsText,
                    onValueChange = { ingredientsText = it },
                    label = { Text("Składniki (jeden na linię)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 6,
                )
                Text(
                    autoCalcHint,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = method,
                    onValueChange = { method = it },
                    label = { Text("Sposób przygotowania") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 5,
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = inspirationSourceText,
                    onValueChange = { inspirationSourceText = it },
                    label = { Text("Źródło inspiracji (opcjonalnie)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = kcalText,
                    onValueChange = { kcalText = it; kcalDirty = true },
                    label = { Text("Kalorie (kcal)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Makroskładniki (opcjonalnie, w gramach)", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                    OutlinedTextField(
                        value = proteinText,
                        onValueChange = { proteinText = it; proteinDirty = true },
                        label = { Text("Białko") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                    )
                    OutlinedTextField(
                        value = carbsText,
                        onValueChange = { carbsText = it; carbsDirty = true },
                        label = { Text("Węgle") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                    )
                    OutlinedTextField(
                        value = fatText,
                        onValueChange = { fatText = it; fatDirty = true },
                        label = { Text("Tłuszcz") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                    )
                }
                if (error != null) {
                    Text(
                        errorText(error!!),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) { Text("Anuluj") }
                    Button(onClick = {
                        val input = CustomRecipeOperations.Input(
                            name, cat, time, ingredientsText, method, kcalText, proteinText, carbsText, fatText,
                            inspirationSourceText,
                        )
                        val result = onAdd(input)
                        if (result == null) onDismiss() else error = result
                    }) { Text("Dodaj") }
                }
            }
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
    pantryItems: Map<String, PantryItem>,
    weekPlan: WeekPlan,
    shoppingItems: Map<String, ShoppingItem>,
    kcalTargets: DailyCalorieTargets,
    viewModel: RecipeViewModel,
    pantryViewModel: PantryViewModel,
    shoppingViewModel: ShoppingViewModel,
    plannerViewModel: PlannerViewModel,
    swipeRatingStyle: SwipeRatingStyle,
    favIngredients: Set<String>,
    onToggleFavIngredient: (canonName: String) -> Unit,
    reviews: Map<String, RecipeReview>,
    activityLogViewModel: ActivityLogViewModel,
    listState: LazyListState,
    favoriteRecipeIds: Set<String>,
    onToggleFavoriteRecipe: (recipeId: String) -> Unit,
    commentsViewModel: RecipeCommentsViewModel,
) {
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
    // FR-3: hoisted here (not a local `remember` inside each RecipeCard) so
    // only ONE card can be expanded at a time, per the FR's own acceptance
    // criterion -- expanding a second card auto-collapses whichever one was
    // open. Also lives outside the per-item composable so it survives that
    // item scrolling out of the LazyColumn's composition window and back.
    var expandedRecipeId by remember { mutableStateOf<String?>(null) }
    // FR-3/v3+v4: auto-centers (or top-aligns if now taller than the
    // viewport -- see centerOrTopAlignScrollDelta) the just-expanded card,
    // since the card's collapsible body isn't behind an AnimatedVisibility/
    // animateContentSize (it's a plain `if(expanded)`, so there's no
    // in-flight animation to wait out -- the layout pass after this
    // recomposition already reflects the card's new, taller measured size
    // by the time this LaunchedEffect's coroutine runs).
    LaunchedEffect(expandedRecipeId) {
        val id = expandedRecipeId ?: return@LaunchedEffect
        // Reading listState.layoutInfo immediately here would still see the
        // PRE-toggle (collapsed) item height -- this LaunchedEffect's
        // coroutine can resume before Compose's layout phase has re-measured
        // the now-expanded item. Waiting two vsync frames reliably gets past
        // both the recomposition and the following layout pass.
        withFrameNanos {}
        withFrameNanos {}
        val itemInfo = listState.layoutInfo.visibleItemsInfo.find { it.key == id } ?: return@LaunchedEffect
        val delta = centerOrTopAlignScrollDelta(itemInfo, listState.layoutInfo.viewportSize.height)
        if (reducedMotion) listState.scrollBy(delta) else listState.animateScrollBy(delta)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            itemsIndexed(recipes, key = { _, recipe -> recipe.id }) { index, recipe ->
                RecipeCard(
                    index = index,
                    recipe = recipe,
                    matchScore = matchScores[recipe.id],
                    cookEntries = cookedMap[recipe.id].orEmpty(),
                    pantryItems = pantryItems,
                    onMarkDoneToday = {
                        viewModel.markCookedToday(recipe.id)
                        pantryViewModel.subtractForRecipe(recipe)
                        activityLogViewModel.log("cook_subtract", "Ugotowano „${recipe.name}” — odjęto składniki ze spiżarni")
                    },
                    onRemoveEntry = { index ->
                        pantryViewModel.restoreForRecipe(recipe)
                        viewModel.removeCookEntry(recipe.id, index)
                        activityLogViewModel.log("pantry_add", "Cofnięto wpis „${recipe.name}” — przywrócono w spiżarni")
                    },
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
                    onToggleFavIngredient = onToggleFavIngredient,
                    weekPlan = weekPlan,
                    onPlanRecipe = { day, cat ->
                        val scale = PlannerOperations.idealScaleFor(recipe, kcalTargets.forCategory(cat))
                        plannerViewModel.setMeal(day, cat, recipe.id, scale)
                    },
                    isAddedToShopping = ShoppingOperations.isRecipeAdded(shoppingItems, recipe.id),
                    onToggleAddToShopping = {
                        if (ShoppingOperations.isRecipeAdded(shoppingItems, recipe.id)) {
                            shoppingViewModel.removeRecipe(recipe)
                            activityLogViewModel.log("shopping_remove", "Usunięto z listy zakupów: ${recipe.name}")
                        } else {
                            shoppingViewModel.addRecipe(recipe)
                            activityLogViewModel.log("shopping_add", "Dodano do listy zakupów: ${recipe.name}")
                        }
                    },
                    onSwipeRate = { stars -> viewModel.setRatingQuick(recipe.id, stars) },
                    swipeRatingStyle = swipeRatingStyle,
                    review = reviews[recipe.id],
                    onSaveReview = { stars, comment -> viewModel.setReview(recipe.id, stars, comment) },
                    onClearReview = { viewModel.clearReview(recipe.id) },
                    onDeleteCustomRecipe = { viewModel.removeCustomRecipe(recipe.id) },
                    isFavorite = recipe.id in favoriteRecipeIds,
                    onToggleFavorite = { onToggleFavoriteRecipe(recipe.id) },
                    isExpanded = recipe.id == expandedRecipeId,
                    // FR-3/v10: single tap expands immediately (v4-v9's
                    // two-step center-then-expand was dropped as unnecessary
                    // friction -- protection against a tap that's really the
                    // tail end of a list fling now lives in RecipeCard's own
                    // scroll-position-at-down-vs-up guard, see there).
                    onToggleExpanded = {
                        // On explicit user request, tapping an ALREADY-expanded
                        // card no longer collapses it (used to be a single
                        // immediate tap) -- "uciążliwe w używaniu" (a stray tap
                        // while reading the open card would close it). The only
                        // way to collapse a card now is to expand a different
                        // one (auto-collapses whichever was open, per FR-3's
                        // "only one expanded at a time").
                        if (recipe.id != expandedRecipeId) {
                            expandedRecipeId = recipe.id
                        }
                    },
                    listState = listState,
                    commentsViewModel = commentsViewModel,
                )
            }
        }
        // Bug found 2026-08-23 ("do wersji kotlin dodaj button przewijania do
        // góry listy przepisów" -- it turned out to already exist in code but
        // be completely invisible in practice): MainActivity's own Scaffold
        // floatingActionButton slot stacks "💡"/"📖" at BottomEnd for this
        // exact same route, and this button lived in a SEPARATE composition
        // scope targeting that identical corner -- both render at the same
        // physical position, with the Scaffold slot's FABs drawn on top,
        // completely hiding this one underneath them. Moved to BottomStart
        // (the opposite corner) so it can never collide with that stack,
        // however many buttons it grows to.
        AnimatedVisibility(
            visible = showButton,
            enter = if (reducedMotion) EnterTransition.None else fadeIn(),
            exit = if (reducedMotion) ExitTransition.None else fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomStart)
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

/**
 * FR-3/v4: port of index.html's scrollCardIntoView -- centers the item
 * unless it's taller than the viewport (only possible once expanded), in
 * which case centering would clip its top half (title/ingredients)
 * off-screen, so its TOP edge is aligned into view instead. Returns a delta
 * for `LazyListState.animateScrollBy`/`scrollBy` (positive = scroll
 * forward), not an absolute position.
 */
private fun centerOrTopAlignScrollDelta(itemInfo: LazyListItemInfo, viewportHeight: Int): Float =
    if (itemInfo.size > viewportHeight) {
        itemInfo.offset.toFloat()
    } else {
        val itemCenter = itemInfo.offset + itemInfo.size / 2
        (itemCenter - viewportHeight / 2).toFloat()
    }

@Composable
private fun RecipeCard(
    index: Int,
    recipe: Recipe,
    matchScore: Int?,
    cookEntries: List<CookEntry>,
    pantryItems: Map<String, PantryItem>,
    onMarkDoneToday: () -> Unit,
    onRemoveEntry: (index: Int) -> Unit,
    onToggleHaveIngredient: (canonName: String, category: PantryCategory, unitCat: String) -> Unit,
    onAddIngredientToShopping: (ingredientText: String) -> Unit,
    favIngredients: Set<String>,
    onToggleFavIngredient: (canonName: String) -> Unit,
    weekPlan: WeekPlan,
    onPlanRecipe: (day: Int, cat: String) -> Unit,
    isAddedToShopping: Boolean,
    onToggleAddToShopping: () -> Unit,
    onSwipeRate: (Int) -> Unit,
    swipeRatingStyle: SwipeRatingStyle,
    review: RecipeReview?,
    onSaveReview: (stars: Int, comment: String?) -> Boolean,
    onClearReview: () -> Unit,
    onDeleteCustomRecipe: () -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    isExpanded: Boolean,
    onToggleExpanded: () -> Unit,
    listState: LazyListState,
    commentsViewModel: RecipeCommentsViewModel,
) {
    val expanded = isExpanded
    var showInfoDialog by remember { mutableStateOf(false) }
    var showCookHistory by remember { mutableStateOf(false) }
    var showPantryCheck by remember { mutableStateOf(false) }
    var showPlanPicker by remember { mutableStateOf(false) }
    var showReviewDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
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
    // FR-3/v10: port of index.html's startScrollY/scrollMoved (attachSwipeRating's
    // finish()) -- a touch used to stop a still-flinging list can register as a
    // legitimate tap by Compose's own touch-slop rules (the finger barely moved),
    // which used to expand the card and make the screen visibly "jump". Recording
    // the list's scroll position on down and comparing it to the position at click
    // time (below) catches that case even when the touch itself looks stationary.
    // Passive observer only (never consumes), so it doesn't interfere with the
    // existing horizontal drag detector or .clickable's own gesture handling.
    var scrollBaselineIndex by remember { mutableStateOf(0) }
    var scrollBaselineOffset by remember { mutableStateOf(0) }
    val scrollMovedThresholdPx = with(LocalDensity.current) { 3.dp.toPx() }
    // 2026-08-11: the persistent border tint now reflects the unified
    // review's stars (>=4 "liked", <=2 "disliked", 3 or unrated neutral)
    // instead of the old separate like/dislike flag -- see setRatingQuick.
    val restBorderColor = when {
        (review?.stars ?: 0) >= 4 -> Color(0xFF43A047)
        (review?.stars ?: 0) in 1..2 -> Color(0xFFE53935)
        else -> Color.Transparent
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

    // FR-49/FR-63: Polaroid/Kafelki structurally reshape the recipe card
    // (not just its palette) -- one-to-one port of index.html's
    // [data-theme="polaroid"]/[data-theme="metro"] .card.recipe-card rules.
    // Every other theme keeps the plain rounded Material3 default.
    val themeId = LocalDietaThemeId.current
    val cardShape = when (themeId) {
        "polaroid" -> RoundedCornerShape(3.dp)
        "metro" -> RoundedCornerShape(2.dp)
        else -> MaterialTheme.shapes.medium
    }
    // Polaroid: a slight scattered tilt (nth-child(even) alternation, 0-indexed
    // odd index here since CSS nth-child is 1-indexed), straightened while expanded.
    val restRotation = if (themeId == "polaroid") (if (index % 2 == 0) -0.6f else 0.6f) else 0f
    val rotation by animateFloatAsState(if (expanded) 0f else restRotation, label = "polaroidTilt")
    // Metro: colored left accent bar standing in for the whole-card color
    // wash other themes use, teal at rest / honey once expanded.
    val metroAccent = if (expanded) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    val metroElevation = if (expanded) 4.dp else 0.dp

    Box {
        Card(
            shape = cardShape,
            elevation = if (themeId == "metro") CardDefaults.cardElevation(defaultElevation = metroElevation) else CardDefaults.cardElevation(),
            modifier = Modifier
                .fillMaxWidth()
                .rotate(rotation)
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .border(2.dp, dragBorderColor, cardShape)
                .pointerInput(recipe.id, listState) {
                    awaitEachGesture {
                        awaitFirstDown(requireUnconsumed = false)
                        scrollBaselineIndex = listState.firstVisibleItemIndex
                        scrollBaselineOffset = listState.firstVisibleItemScrollOffset
                    }
                }
                .pointerInput(recipe.id) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            val committed = offsetX.value
                            swipeCoroutineScope.launch {
                                when {
                                    committed > swipeThresholdPx -> onSwipeRate(5)
                                    committed < -swipeThresholdPx -> onSwipeRate(1)
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
                .clickable {
                    val scrollMoved = listState.firstVisibleItemIndex != scrollBaselineIndex ||
                        abs(listState.firstVisibleItemScrollOffset - scrollBaselineOffset) > scrollMovedThresholdPx
                    if (!scrollMoved) onToggleExpanded()
                },
        ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            if (themeId == "metro") {
                Box(modifier = Modifier.fillMaxHeight().width(5.dp).background(metroAccent))
            }
        Column(modifier = Modifier.weight(1f).padding(bottom = if (themeId == "polaroid") 18.dp else 0.dp)) {
            Row(modifier = Modifier.padding(14.dp)) {
                // Thumbnail on the right (not left) on explicit user request,
                // so the title text starts flush against the card's left
                // edge like index.html's .card-head (title flex:1 on the
                // left, .card-head-side incl. the thumb on the right).
                Column(modifier = Modifier.weight(1f)) {
                    RecipeCardBody(
                        recipe,
                        matchScore,
                        expanded,
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
                    Text(thumbEmoji, fontSize = 24.sp)
                }
            }
        }
        }
        }
        // 2026-08-11: persistent ★N badge showing the unified rating (see
        // setRatingQuick) -- tap opens the exact same review dialog as the
        // "⭐ Oceń i skomentuj" button and the post-cook prompt, instead of
        // just clearing it, so rating behaves identically everywhere it's
        // reachable from.
        if (review != null && review.stars > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .size(30.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(15.dp))
                    .clickable { showReviewDialog = true },
                contentAlignment = Alignment.Center,
            ) {
                Text("★${review.stars}", fontSize = 14.sp)
            }
        }
        // FR-56: balloon feedback that fades in as the drag approaches the
        // commit threshold, so the user sees what will happen before
        // releasing -- alpha 0 at rest, 1 right at the threshold.
        if (offsetX.value != 0f) {
            val swipeProgress = (abs(offsetX.value) / swipeThresholdPx).coerceIn(0f, 1f)
            Text(
                text = if (offsetX.value > 0) "❤️ Podoba się to dla mnie!" else "👎 Nie podoba się to dla mnie!",
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp)
                    .alpha(swipeProgress),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                fontFamily = SwipeLabelFont,
                fontWeight = FontWeight.ExtraBold,
                color = if (offsetX.value > 0) Color(0xFF43A047) else Color(0xFFE53935),
            )
        }
        // Requested 2026-08-25 (Web FR-87/v14, ported here): discrete,
        // always-visible hint that this card can be swiped to rate -- the
        // gesture above only ever gave feedback DURING a swipe (the
        // balloon label), nothing suggested it was possible beforehand.
        // Hidden while actively dragging so it doesn't clash with that
        // label; purely visual (no pointerInput/clickable), same
        // red=dislike/green=like colors the drag feedback already uses.
        if (offsetX.value == 0f) {
            Text(
                "‹", modifier = Modifier.align(Alignment.CenterStart).padding(start = 4.dp).alpha(0.3f),
                fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFBE463C),
            )
            Text(
                "›", modifier = Modifier.align(Alignment.CenterEnd).padding(end = 4.dp).alpha(0.3f),
                fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3CAA6E),
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
            review = review,
            onMarkDoneToday = onMarkDoneToday,
            onRateRecipe = { showCookHistory = false; showReviewDialog = true },
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
    if (showReviewDialog) {
        RecipeReviewDialog(
            recipeName = recipe.name,
            existing = review,
            onSave = { stars, comment -> onSaveReview(stars, comment) },
            onDelete = { onClearReview(); showReviewDialog = false },
            onDismiss = { showReviewDialog = false },
        )
    }
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Usunąć przepis?") },
            text = { Text("„${recipe.name}” zostanie trwale usunięty. Wcześniejsze wpisy historii gotowania i pozycje na liście zakupów pochodzące z tego przepisu nie zostaną naruszone.") },
            confirmButton = {
                TextButton(onClick = { onDeleteCustomRecipe(); showDeleteConfirm = false }) { Text("Usuń") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Anuluj") }
            },
        )
    }
}

/**
 * FR-67: 5 large stars (StarRatingRow, same widget as FR-17's cook-history
 * rating) + an optional up-to-300-char comment. Re-opening for an already
 * reviewed recipe pre-fills both, ready to edit -- port of
 * index.html's openRecipeReviewModal/renderRecipeReviewStars.
 */
@Composable
internal fun RecipeReviewDialog(
    recipeName: String,
    existing: RecipeReview?,
    onSave: (stars: Int, comment: String?) -> Boolean,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    var stars by remember { mutableStateOf(existing?.stars ?: 0) }
    var comment by remember { mutableStateOf(existing?.comment ?: "") }
    var showStarsRequiredHint by remember { mutableStateOf(false) }
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.widthIn(max = 480.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "⭐ Oceń: $recipeName",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = onDismiss) { Text("✕") }
                }
                Spacer(modifier = Modifier.height(10.dp))
                StarRatingRow(rating = stars, onRate = { n -> stars = if (stars == n) 0 else n; showStarsRequiredHint = false })
                if (showStarsRequiredHint) {
                    Text(
                        "Wybierz od 1 do 5 gwiazdek",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = comment,
                    onValueChange = { if (it.length <= 300) comment = it },
                    label = { Text("Komentarz (opcjonalnie)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4,
                )
                Text(
                    "${comment.length}/300",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    if (existing != null) {
                        TextButton(onClick = onDelete) { Text("Usuń ocenę") }
                    }
                    Button(onClick = {
                        if (stars == 0) {
                            showStarsRequiredHint = true
                        } else if (onSave(stars, comment)) {
                            onDismiss()
                        }
                    }) { Text("Zapisz") }
                }
            }
        }
    }
}

@Composable
internal fun RecipeCardBody(
    recipe: Recipe,
    matchScore: Int?,
    expanded: Boolean,
    onInfoClick: () -> Unit,
    onPantryCheckClick: () -> Unit,
    pantryItems: Map<String, PantryItem>,
    favIngredients: Set<String>,
    onToggleFavIngredient: (canonName: String) -> Unit,
    review: RecipeReview?,
    onOpenReview: () -> Unit,
    onDeleteCustomRecipe: () -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    commentsViewModel: RecipeCommentsViewModel,
    isAddedToShopping: Boolean,
    onToggleAddToShopping: () -> Unit,
    cookCount: Int,
    onMarkDoneClick: () -> Unit,
    onPlanClick: () -> Unit,
) {
    val context = LocalContext.current
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Port of index.html's ".recipe-title" click handler -- opens a
            // Google search for the dish instead of toggling the card (the
            // click is consumed here, same as the web's e.stopPropagation()).
            // Requested 2026-08-26 ("wyszukiwanie dania będzie dostępne
            // dopiero na otwartej rozwiniętej karcie a nie na zwiniętej"):
            // only wired up while `expanded` -- on a collapsed card, the
            // title has no clickable of its own, so the tap falls through
            // to RecipeCard's own `.clickable{ onToggleExpanded() }` one
            // level up instead, same as tapping anywhere else on the card.
            // Web ported alongside this (see index.html's own change), same
            // reasoning: an accidental tap on a long, multi-line collapsed
            // title used to silently open a browser tab instead of just
            // expanding the card, which read as broken/surprising.
            Text(
                recipe.name,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.5.sp, lineHeight = 19.sp),
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (expanded) {
                            Modifier.clickable {
                                val query = Uri.encode("${recipe.name} przepis")
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=$query")))
                            }
                        } else {
                            Modifier
                        },
                    ),
            )
            // FR-66/FR-76: distinguishes a user-added recipe (own, or another
            // user's approved community recipe) from the 229 built-in ones.
            if (recipe.source == "custom" || recipe.source == "community") {
                Text(
                    if (recipe.source == "custom") "✍️ Twój przepis" else "🌍 ${recipe.authorDisplayName ?: "Anonimowy użytkownik"}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                )
            }
            // FR-2: ⭐ favorite-RECIPE toggle -- port of index.html's
            // .star-btn (distinct from the ☆ per-ingredient toggles below).
            TextButton(
                onClick = onToggleFavorite,
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                modifier = Modifier.widthIn(min = 32.dp),
            ) {
                Text(if (isFavorite) "★" else "☆", style = MaterialTheme.typography.titleMedium)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        val matchSuffix = matchScore?.let { "   🎯 $it%" } ?: ""
        Text(
            "⏱ ${recipe.time}   🔥 ${recipe.kcal} kcal$matchSuffix",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        // Requested 2026-08-25 ("karty z przepisami... są dużo niższe niż
        // web wersja"): port of index.html's always-visible .expand-toggle
        // button ("Składniki i przygotowanie" + chevron rotating 180° when
        // .expanded) -- this row was simply missing from the collapsed
        // Android card entirely (RecipeCardBody used to render NOTHING
        // between the meta line and the `if(expanded)` block), which is
        // most of why the collapsed card measured noticeably shorter than
        // web's. No onClick of its own needed -- the whole Card already has
        // one `.clickable{ onToggleExpanded() }` (RecipeCard, one level up)
        // that this Row sits inside, same as index.html's card-wide click
        // handler that the button's own listener is redundant with.
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "Składniki i przygotowanie",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                if (expanded) "⌃" else "⌄",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

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
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                    )
                    if (recipe.calc.isNotEmpty()) {
                        TextButton(onClick = onInfoClick) { Text("ℹ️") }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // "Składniki" section label -- web doesn't have one (see FR-3's
            // Uwagi), but the user explicitly asked for it back on Android
            // specifically ("w kotlin brakuje na karcie napisu składniki i
            // przygotowanie"), so this is a deliberate, documented
            // divergence from the otherwise-1:1 port, not an oversight.
            Text("Składniki", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                recipe.ingredients.forEach { ingredient ->
                    // FR-35: emoji suffix when the ingredient resolves to a known canon -- port of index.html's withEmoji.
                    val canon = remember(ingredient) { RecipePantryMatching.parseIngredient(ingredient).canonName }
                    // FR-32: star toggles this ingredient as a favorite (fuels the
                    // dish-idea generator below); "have it" highlight (bold) fires
                    // when the ingredient is either favorited OR already tracked in
                    // the pantry -- port of index.html's `(have||f)?'have-it'` class.
                    val isFav = canon in favIngredients
                    val haveIt = isFav || pantryItems.containsKey(canon)
                    // Plain clickable Text instead of a TextButton -- a Material
                    // button enforces a ~40dp minimum touch height per row even
                    // with zero content padding, which was inflating every
                    // ingredient row far beyond web's tight `margin-bottom:4px`
                    // <li> rows and was the main reason the card read as "less
                    // compact" than the PWA one.
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            if (isFav) "★" else "☆",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isFav) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.clickable { onToggleFavIngredient(canon) },
                        )
                        Text(
                            IngredientCanon.withEmoji(ingredient, canon),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (haveIt) FontWeight.Bold else FontWeight.Normal,
                            color = if (haveIt) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // "Przygotowanie" section label -- same deliberate divergence as
            // "Składniki" above, on the same explicit user request.
            Text("Przygotowanie", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
            Text(
                recipe.method,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (recipe.inspirationSource != null) {
                Text(
                    "💡 Inspiracja: ${recipe.inspirationSource}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
            // Requested 2026-08-25 (Web FR-66/v5, ported here): explicit
            // search buttons for the full dish name -- previously the only
            // way to search it was tapping the recipe title, with no
            // visible button/affordance suggesting that was possible.
            Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val context = LocalContext.current
                OutlinedButton(
                    onClick = {
                        val uri = Uri.parse("https://www.google.com/search?q=" + Uri.encode("${recipe.name} przepis"))
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                ) { Text("🔎 Google", maxLines = 1, overflow = TextOverflow.Ellipsis) }
                OutlinedButton(
                    onClick = {
                        val uri = Uri.parse("https://www.youtube.com/results?search_query=" + Uri.encode("${recipe.name} przepis"))
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                ) { Text("▶️ YouTube", maxLines = 1, overflow = TextOverflow.Ellipsis) }
                // Requested 2026-08-26 ("dodaj też button szukają w Gemini
                // żeby sztuczna inteligencja dostała prompt na propozycje
                // przygotowania tego konkretnego dania rozpisane w
                // szczegółach"): opens Gemini's web app with the prompt
                // pre-filled via `?q=` (same unofficial-but-widely-observed
                // pattern as chatgpt.com/?q=... -- if Gemini ever stops
                // honoring it, the button still opens Gemini itself, just
                // without the pre-fill, so this degrades gracefully either
                // way).
                OutlinedButton(
                    onClick = {
                        val prompt = "Rozpisz szczegółowo, krok po kroku, jak przygotować danie: ${recipe.name}. " +
                            "Podaj dokładne czasy, temperatury, ilości składników i wskazówki przydatne dla początkujących."
                        val uri = Uri.parse("https://gemini.google.com/app?q=" + Uri.encode(prompt))
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                ) { Text("✨ Gemini", maxLines = 1, overflow = TextOverflow.Ellipsis) }
            }
            if (review?.comment != null) {
                Text(
                    "💬 Twój komentarz: „${review.comment}”",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
            // FR-16/v4: inline coverage summary (count + shaded progress bar)
            // instead of a plain trigger button, so the user sees what's
            // missing at a glance without tapping -- moved to the bottom of
            // the card on explicit user request. Still opens the same detail
            // dialog (PantryCheckDialog) on tap for the per-ingredient
            // "Mam to" actions -- port of index.html's .pantry-status-btn.
            val pantryTotal = recipe.ingredients.size
            val pantryHave = remember(recipe.id, pantryItems) {
                recipe.ingredients.count { ing -> pantryItems.containsKey(RecipePantryMatching.parseIngredient(ing).canonName) }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Card(
                onClick = onPantryCheckClick,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("🏺 Stan spiżarni", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                        Text(
                            "$pantryHave / $pantryTotal składników",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { if (pantryTotal > 0) pantryHave.toFloat() / pantryTotal else 0f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }
            // FR-67/FR-77: review + comments toggle side by side on explicit
            // user request ("oceń i skomentuj oraz komentarze użytkowników
            // też daj do jednej linii"), positioned below the pantry widget.
            // Comments state hoisted here (not inside a self-contained
            // composable) so the toggle button can live in this row while
            // the actual comment list still expands full-width beneath it.
            var commentsExpanded by remember { mutableStateOf(false) }
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onOpenReview,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 38.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Text(
                        if (review != null) "⭐ Ocena ${review.stars}/5" else "⭐ Oceń",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                OutlinedButton(
                    onClick = {
                        commentsExpanded = !commentsExpanded
                        if (commentsExpanded) commentsViewModel.loadFirstPage(recipe.id)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 38.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Text(
                        if (commentsExpanded) "💬 Komentarze ▲" else "💬 Komentarze ▼",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            RecipeCommentsBody(recipeId = recipe.id, viewModel = commentsViewModel, expanded = commentsExpanded)
            // FR-25/FR-15: shopping-list toggle, "Zrobione" and "Zaplanuj"
            // side by side on explicit user request ("przycisk dodaj do
            // listy zakupów zrób tak samo jak zrobione i zaplanuj w tej
            // samej linii"). All three now live inside the expanded body
            // (visible only once expanded), same reasoning as the shopping
            // button's own earlier move: sitting always-visible right under
            // the title made it easy to hit by accident while trying to
            // expand the card.
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(
                    onClick = onToggleAddToShopping,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 38.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp, vertical = 8.dp),
                ) {
                    Text(
                        if (isAddedToShopping) "✓ Na liście" else "🛒 Zakupy",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                TextButton(
                    onClick = onMarkDoneClick,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 38.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp, vertical = 8.dp),
                ) {
                    Text(
                        "✅ Zrobione" + if (cookCount > 0) " (${cookCount}×)" else "",
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                TextButton(
                    onClick = onPlanClick,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 38.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp, vertical = 8.dp),
                ) {
                    Text(
                        "📅 Zaplanuj",
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            // FR-66: only a custom (user-added) recipe can be deleted this way.
            if (recipe.source == "custom") {
                Spacer(modifier = Modifier.height(6.dp))
                TextButton(
                    onClick = onDeleteCustomRecipe,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                ) {
                    Text("🗑️ Usuń ten przepis")
                }
            }
        }
    }
}

/**
 * FR-77: "💬 Komentarze innych użytkowników" body -- fetches the first page
 * (3) on first expand, "Pokaż więcej" fetches 10 more at a time until
 * Firestore returns fewer than requested. Port of index.html's
 * comments-toggle-btn/comments-body (index.html:4202-4211, 4254-4264).
 * The toggle BUTTON itself lives in `RecipeCardBody` now (sharing a row
 * with "Oceń i skomentuj" on explicit user request), so `expanded` is
 * hoisted there instead of owned locally here.
 */
@Composable
private fun RecipeCommentsBody(recipeId: String, viewModel: RecipeCommentsViewModel, expanded: Boolean) {
    val pages by viewModel.pages.collectAsState()
    val page = pages[recipeId]

    if (expanded) {
        Column(modifier = Modifier.padding(top = 4.dp)) {
            when {
                page == null || (page.loading && page.comments.isEmpty()) -> {
                    Text("Wczytywanie komentarzy…", style = MaterialTheme.typography.bodySmall)
                }
                page.unavailable -> {
                    Text(
                        "Komentarze są teraz niedostępne (brak połączenia z chmurą).",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                page.comments.isEmpty() -> {
                    Text(
                        "Bądź pierwszą osobą, która oceni to danie.",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                else -> {
                    page.comments.forEach { comment ->
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text(
                                "${"★".repeat(comment.stars)}${"☆".repeat(5 - comment.stars)}  ${comment.displayName}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                            val commentText = comment.comment
                            if (commentText != null) {
                                Text(commentText, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                    if (!page.exhausted) {
                        TextButton(
                            onClick = { viewModel.loadMore(recipeId) },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(if (page.loading) "Wczytywanie…" else "Pokaż więcej")
                        }
                    }
                }
            }
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
internal fun CookHistoryDialog(
    recipe: Recipe,
    entries: List<CookEntry>,
    review: RecipeReview?,
    onMarkDoneToday: () -> Unit,
    onRateRecipe: () -> Unit,
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
                Spacer(modifier = Modifier.height(8.dp))
                // 2026-08-11: this is now a pure log of WHEN the dish was
                // made -- no per-occurrence rating of its own anymore (see
                // RecipeCard's setRatingQuick doc). Opens the exact same
                // review dialog as the card's badge and its own "Oceń i
                // skomentuj" button.
                OutlinedButton(onClick = onRateRecipe, modifier = Modifier.fillMaxWidth()) {
                    Text(if (review != null) "⭐ Twoja ocena: ${review.stars}/5 (zmień)" else "⭐ Oceń to danie")
                }
                Spacer(modifier = Modifier.height(12.dp))

                if (entries.isEmpty()) {
                    Text("Jeszcze nie oznaczone jako zrobione.", style = MaterialTheme.typography.bodyMedium)
                } else {
                    Text("Zrobione ${entries.size}×", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(10.dp))

                    entries.forEachIndexed { index, entry ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                "${formatCookDate(entry.dateEpochMillis)} ${formatCookTime(entry.dateEpochMillis)}",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            TextButton(onClick = { pendingDeleteIndex = index }) { Text("✕") }
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
internal fun PantryCheckDialog(
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
                    // Requested 2026-08-26 (5 new features -- users flagged
                    // not knowing what to swap in for a missing ingredient):
                    // when missing, suggest whatever's already in the pantry
                    // from the same category as a possible substitute, same
                    // no-fixed-table approach as index.html's pcr-sub-hint.
                    val siblings = remember(pantryItems, category, parsed.canonName) {
                        pantryItems.entries
                            .filter { it.value.category == category && it.key != parsed.canonName }
                            .map { it.value.name }
                    }
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(IngredientCanon.withEmoji(ingredient, parsed.canonName), style = MaterialTheme.typography.bodyMedium)
                        if (entry == null && siblings.isNotEmpty()) {
                            Text(
                                "🔁 Masz w spiżarni (ta sama kategoria): " + siblings.take(3).joinToString(", "),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 2.dp),
                            )
                        }
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
internal fun PlanPickerDialog(
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
internal fun MacroInfoDialog(recipe: Recipe, onDismiss: () -> Unit) {
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
