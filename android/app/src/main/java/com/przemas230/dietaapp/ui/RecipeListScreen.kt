package com.przemas230.dietaapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.przemas230.dietaapp.data.CookEntry
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.logic.CATEGORIES
import com.przemas230.dietaapp.logic.IngredientCanon
import com.przemas230.dietaapp.logic.ProfileCalculations
import com.przemas230.dietaapp.logic.RecipeMatching
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
    viewModel: RecipeViewModel = viewModel(),
) {
    val recipes by viewModel.visibleRecipes.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchTerm by viewModel.searchTerm.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val cookedMap by viewModel.cooked.collectAsState()
    val profile by profileViewModel.profile.collectAsState()
    LaunchedEffect(profile.glutenFree, profile.lactoseFree) {
        viewModel.setDietaryFilters(profile.glutenFree, profile.lactoseFree)
    }

    var sortByMatch by remember { mutableStateOf(false) }
    val macroTargets = remember(profile) { ProfileCalculations.calcMacroTargets(profile) }
    val matchScores = remember(recipes, macroTargets, profile) {
        recipes.associate { it.id to RecipeMatching.matchScore(it, macroTargets.forCategory(it.cat), profile) }
    }
    val displayedRecipes = remember(recipes, sortByMatch, matchScores) {
        if (sortByMatch) recipes.sortedByDescending { matchScores[it.id] ?: -1 } else recipes
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
        }

        Spacer(modifier = Modifier.height(4.dp))

        when {
            isLoading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Wczytywanie przepisów…")
            }
            displayedRecipes.isEmpty() -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Brak przepisów spełniających kryteria.")
            }
            else -> RecipeListWithScrollToTop(displayedRecipes, matchScores, cookedMap, viewModel, pantryViewModel)
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
    viewModel: RecipeViewModel,
    pantryViewModel: PantryViewModel,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val thresholdPx = with(density) { 400.dp.toPx() }
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
                    onMarkDoneToday = {
                        viewModel.markCookedToday(recipe.id)
                        pantryViewModel.subtractForRecipe(recipe)
                    },
                    onSetRating = { index, rating -> viewModel.setCookRating(recipe.id, index, rating) },
                    onRemoveEntry = { index ->
                        pantryViewModel.restoreForRecipe(recipe)
                        viewModel.removeCookEntry(recipe.id, index)
                    },
                )
            }
        }
        AnimatedVisibility(
            visible = showButton,
            enter = fadeIn(),
            exit = fadeOut(),
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
    onMarkDoneToday: () -> Unit,
    onSetRating: (index: Int, rating: Int) -> Unit,
    onRemoveEntry: (index: Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var showCookHistory by remember { mutableStateOf(false) }
    // FR-4: deterministic emoji thumbnail from the recipe's own biggest
    // ingredient — no network round-trip, same icon set as pantry tiles.
    val thumbEmoji = remember(recipe.id) { IngredientCanon.mainIngredientInfo(recipe)?.emoji ?: "🍽️" }
    Card(
        modifier = Modifier
            .fillMaxWidth()
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
                    RecipeCardBody(recipe, matchScore, expanded, onInfoClick = { showInfoDialog = true })
                }
            }
            // FR-15: always visible (not gated by `expanded`), same as
            // index.html's always-shown card-actions bar. Tapping this never
            // marks the dish done directly — it always opens the history
            // dialog first (revised from the original press-and-hold design,
            // see FR-15.md's "Uwagi").
            TextButton(
                onClick = { showCookHistory = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 38.dp)
                    .padding(horizontal = 10.dp),
            ) {
                Text("✅ Zrobione" + if (cookEntries.isNotEmpty()) " (${cookEntries.size}×)" else "")
            }
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
}

@Composable
private fun RecipeCardBody(recipe: Recipe, matchScore: Int?, expanded: Boolean, onInfoClick: () -> Unit) {
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
            Text("Składniki", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
            recipe.ingredients.forEach { ingredient ->
                Text("• $ingredient", style = MaterialTheme.typography.bodySmall)
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
 * openMacroInfoModal + the static #macroInfoOverlay legend text. Micronutrient
 * chips (computeMicronutrients in index.html) aren't ported yet — that's
 * FR-64, separate from this one.
 */
@Composable
private fun MacroInfoDialog(recipe: Recipe, onDismiss: () -> Unit) {
    var legendExpanded by remember { mutableStateOf(false) }
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.widthIn(max = 480.dp)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
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
                    }
                }
            }
        }
    }
}
