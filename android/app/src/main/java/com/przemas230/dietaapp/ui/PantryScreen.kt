package com.przemas230.dietaapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.Recipe
import com.przemas230.dietaapp.logic.AppThemes
import com.przemas230.dietaapp.logic.PantryDisplay
import com.przemas230.dietaapp.logic.PantryTiles
import com.przemas230.dietaapp.logic.RecipePantryMatching
import com.przemas230.dietaapp.logic.ShoppingDisplay
import com.przemas230.dietaapp.ui.theme.LocalDietaThemeId
import kotlin.math.roundToInt

/**
 * FR-28: a tile per canonical ingredient across EVERY recipe (not just ones
 * the user has already tracked), grouped into 8 categories, plus a trailing
 * "➕ Dodaj własny" tile per category — a one-to-one port of index.html's
 * renderPantry()/buildPantryTileList(). Tapping the upper half of a tile
 * adds a unit, the lower half subtracts (PantryOperations.tileTapDelta);
 * long-pressing a TRACKED tile opens a small menu to change its category or
 * remove tracking entirely. `state.pantry`-equivalent storage (PantryViewModel/
 * PantryStore) only ever holds entries the user has actually tapped — the
 * full tile list itself is recomputed fresh from `allRecipes` every time
 * this screen composes, never persisted.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantryScreen(viewModel: PantryViewModel, allRecipes: List<Recipe>, activityLogViewModel: ActivityLogViewModel) {
    val items by viewModel.items.collectAsState()
    var addTileCategory by remember { mutableStateOf<PantryCategory?>(null) }
    var actionTarget by remember { mutableStateOf<Pair<String, PantryCategory>?>(null) }
    var showClearAllConfirm by remember { mutableStateOf(false) }

    val recipeTileNames = remember(allRecipes) { PantryTiles.buildTileNames(allRecipes) }
    val unitCats = remember(allRecipes) { PantryTiles.computeTileUnitCats(allRecipes) }
    // Union with currently-tracked names too, so a custom tile (or an item
    // tracked via FR-16's "Mam to", which can use a name outside any recipe)
    // keeps showing even though it's not recipe-derived.
    val tileNames = remember(recipeTileNames, items.keys) { (recipeTileNames + items.keys).sorted() }
    val grouped = remember(tileNames, items) {
        tileNames.groupBy { name -> items[name]?.category ?: PantryTiles.categoryAndEmoji(name).first }
    }
    // FR-87: motyw "Klinika" -- kategorie jako akordeon (stukniecie w
    // naglowek zwija/rozwija). Domyslnie wszystkie rozwiniete, wiec nic sie
    // nie zmienia wizualnie dopoki uzytkownik czegos nie zwinie. PantryTile/
    // AddOwnTile (gesty dodawania/odejmowania, long-press menu) sa
    // CALKOWICIE nietkniete -- tylko to, ktore kategorie w ogole trafiaja do
    // LazyVerticalGrid, sie zmienia.
    val isClinic = AppThemes.isClinicFamily(LocalDietaThemeId.current)
    var expandedCategories by remember { mutableStateOf(PantryTiles.CATEGORY_ORDER.toSet()) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Górna połowa kafelka = dodaj, dolna połowa = odejmij. Przytrzymaj śledzony kafelek, by zmienić " +
                "kategorię albo usunąć śledzenie. Przyprawy: Mało → Wystarczy → Dużo.",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodySmall,
        )
        TextButton(
            onClick = { showClearAllConfirm = true },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
        ) {
            Text("🗑️ Wyczyść całą spiżarnię")
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 78.dp),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PantryTiles.CATEGORY_ORDER.forEach { category ->
                val names = grouped[category].orEmpty()
                if (names.isEmpty() && category == PantryCategory.INNE) return@forEach
                val isExpanded = !isClinic || category in expandedCategories
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 2.dp)
                            .then(
                                if (isClinic) {
                                    Modifier.clickable {
                                        expandedCategories = if (category in expandedCategories) {
                                            expandedCategories - category
                                        } else {
                                            expandedCategories + category
                                        }
                                    }
                                } else {
                                    Modifier
                                },
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            category.label,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                        )
                        if (isClinic) {
                            Text(
                                "${names.size}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(end = 6.dp),
                            )
                            Text(if (isExpanded) "⌃" else "⌄", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
                if (isExpanded) {
                    if (names.isEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Text(
                                "Brak produktów",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 8.dp),
                            )
                        }
                    } else {
                        gridItems(names, key = { it }) { name ->
                            val entry = items[name]
                            val (_, emoji) = PantryTiles.categoryAndEmoji(name)
                            val unitCat = unitCats[name] ?: "count"
                            PantryTile(
                                name = name,
                                emoji = emoji,
                                entry = entry,
                                onTap = { dir ->
                                    viewModel.tileTapDelta(name, category, unitCat, dir)
                                    activityLogViewModel.log("pantry_add", "Spiżarnia: $name (${if (dir > 0) "+" else "-"}1)")
                                },
                                onLongPress = { if (entry != null) actionTarget = name to category },
                            )
                        }
                    }
                    item { AddOwnTile(onClick = { addTileCategory = category }) }
                }
            }
        }
    }

    addTileCategory?.let { category ->
        AddCustomTileDialog(
            category = category,
            onAdd = { name ->
                viewModel.tileTapDelta(name, category, unitCats[name] ?: "count", dir = 1)
                activityLogViewModel.log("pantry_add", "Dodano własny produkt: $name (${category.label})")
            },
            onDismiss = { addTileCategory = null },
        )
    }
    actionTarget?.let { (name, category) ->
        TileActionDialog(
            name = name,
            currentCategory = category,
            onChangeCategory = { newCategory ->
                viewModel.changeCategory(name, newCategory)
                activityLogViewModel.log("pantry_recat", "Zmieniono kategorię „$name” na: ${newCategory.label}")
            },
            onRemoveTracking = {
                viewModel.removeItem(name)
                activityLogViewModel.log("pantry_delete", "Usunięto ze spiżarni: $name")
            },
            onDismiss = { actionTarget = null },
        )
    }
    if (showClearAllConfirm) {
        AlertDialog(
            onDismissRequest = { showClearAllConfirm = false },
            title = { Text("Wyczyścić całą spiżarnię?") },
            text = { Text("Usunie śledzenie wszystkich produktów i przypraw. Własne kafelki i zmienione kategorie/jednostki zostają.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.replaceAll(emptyMap())
                    activityLogViewModel.log("pantry_delete", "Wyczyszczono całą spiżarnię")
                    showClearAllConfirm = false
                }) { Text("Wyczyść") }
            },
            dismissButton = {
                TextButton(onClick = { showClearAllConfirm = false }) { Text("Anuluj") }
            },
        )
    }
}

@Composable
private fun PantryTile(
    name: String,
    emoji: String,
    entry: PantryItem?,
    onTap: (dir: Int) -> Unit,
    onLongPress: () -> Unit,
) {
    val active = entry != null
    // Bug fixed 2026-08-11 ("zepsuło się w spiżarni menu po przytrzymaniu
    // produktu, nic się nie dzieje"): `pointerInput(name)` below is keyed
    // ONLY on `name`, so its gesture-detection coroutine does NOT restart
    // when `entry`/`category` change for that same tile -- it keeps running
    // the closures captured whenever this tile FIRST composed. Concretely:
    // a tile starts untracked (`entry == null`), its `onLongPress` closure
    // captures that (`if (entry != null) ...`, evaluates to a no-op) — the
    // moment the user taps to start tracking it, `entry` becomes non-null
    // and a NEW `onLongPress` closure is created by the caller, but since
    // `name` hasn't changed, the ALREADY-RUNNING coroutine never picks it
    // up and keeps calling the ORIGINAL (permanently no-op) one -- so
    // long-pressing a tile added earlier in the same session silently does
    // nothing, forever, until the grid is torn down and rebuilt (e.g.
    // leaving and re-entering Spiżarnia) coincidentally gives it a fresh
    // start. `rememberUpdatedState` gives the gesture coroutine a stable
    // indirection that always reads the CURRENT callback on each new
    // gesture, without needing `pointerInput` to restart at all.
    val currentOnTap = rememberUpdatedState(onTap)
    val currentOnLongPress = rememberUpdatedState(onLongPress)
    val themeId = LocalDietaThemeId.current
    val metro = themeId == "metro"
    val shape = if (metro) RoundedCornerShape(2.dp) else RoundedCornerShape(14.dp)
    val background = when {
        metro && active -> MaterialTheme.colorScheme.primary
        active -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surface
    }
    val nameColor = when {
        metro && active -> MaterialTheme.colorScheme.onPrimary
        active -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurface
    }
    val displayName = if (entry is PantryItem.Product && PantryDisplay.isCountUnit(entry.unit)) {
        PantryDisplay.displayName(name, entry.quantity.roundToInt())
    } else {
        PantryDisplay.displayName(name, null)
    }
    val badgeText = when (entry) {
        is PantryItem.Product -> ShoppingDisplay.formatQty(RecipePantryMatching.pantryUnitCat(entry.unit), entry.quantity)
        is PantryItem.Spice -> entry.level.label
        null -> ""
    }

    // The badge below is deliberately positioned half-outside this tile's
    // corner (mirroring the web version's `position:absolute; top:-7px;
    // right:-7px`), so the clip/background/border must live on an INNER
    // layer (sized via matchParentSize) rather than on this outer Box --
    // otherwise Compose's .clip() on the outer Box would also clip the
    // overflowing badge, cutting its text off.
    Box(
        modifier = Modifier
            .heightIn(min = 76.dp)
            .pointerInput(name) {
                detectTapGestures(
                    onLongPress = { currentOnLongPress.value() },
                    onTap = { offset -> currentOnTap.value(if (offset.y < size.height / 2f) 1 else -1) },
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .shadow(if (active) 3.dp else 1.dp, shape, clip = false)
                .clip(shape)
                .background(background, shape)
                .then(
                    if (!metro) {
                        Modifier.border(1.5.dp, if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline, shape)
                    } else {
                        Modifier
                    },
                ),
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier.padding(8.dp),
        ) {
            Text(emoji, fontSize = 21.sp)
            Text(
                displayName,
                fontSize = 9.5.sp,
                textAlign = TextAlign.Center,
                fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                color = nameColor,
            )
        }
        if (active) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = (-6).dp)
                    .shadow(2.dp, RoundedCornerShape(50), clip = false)
                    .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(50))
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            ) {
                Text(badgeText, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondary)
            }
        }
    }
}

@Composable
private fun AddOwnTile(onClick: () -> Unit) {
    val shape = RoundedCornerShape(14.dp)
    Box(
        modifier = Modifier
            .heightIn(min = 76.dp)
            .clip(shape)
            .border(1.5.dp, MaterialTheme.colorScheme.outline, shape)
            .pointerInput(Unit) { detectTapGestures(onTap = { onClick() }) },
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text("➕", fontSize = 21.sp)
            Text(
                "Dodaj własny",
                fontSize = 9.5.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun AddCustomTileDialog(category: PantryCategory, onAdd: (name: String) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.widthIn(max = 480.dp)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("➕ Dodaj własny produkt", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Kategoria: ${category.label}", style = MaterialTheme.typography.bodySmall)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nazwa produktu") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) { Text("Anuluj") }
                    FilledTonalButton(onClick = {
                        val trimmed = name.trim()
                        if (trimmed.isNotEmpty()) {
                            onAdd(trimmed)
                            onDismiss()
                        }
                    }) { Text("Dodaj") }
                }
            }
        }
    }
}

/** FR-30: long-press a TRACKED tile -> change category, or remove tracking (wyzeruj stan). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TileActionDialog(
    name: String,
    currentCategory: PantryCategory,
    onChangeCategory: (PantryCategory) -> Unit,
    onRemoveTracking: () -> Unit,
    onDismiss: () -> Unit,
) {
    var selected by remember(name) { mutableStateOf(currentCategory) }
    var menuExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.widthIn(max = 480.dp)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("🗂️ $name", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                ExposedDropdownMenuBox(expanded = menuExpanded, onExpandedChange = { menuExpanded = it }) {
                    OutlinedTextField(
                        value = selected.label,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Kategoria") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                    )
                    DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        PantryTiles.CATEGORY_ORDER.forEach { cat ->
                            DropdownMenuItem(text = { Text(cat.label) }, onClick = {
                                selected = cat
                                menuExpanded = false
                            })
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) { Text("Anuluj") }
                    FilledTonalButton(onClick = {
                        onChangeCategory(selected)
                        onDismiss()
                    }) { Text("Zapisz kategorię") }
                }
                TextButton(onClick = {
                    onRemoveTracking()
                    onDismiss()
                }) { Text("🗑️ Usuń śledzenie (wyzeruj stan)") }
            }
        }
    }
}
