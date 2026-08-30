package com.przemas230.dietaapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
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
import com.przemas230.dietaapp.logic.PantryOperations
import com.przemas230.dietaapp.logic.PantryShortage
import com.przemas230.dietaapp.logic.PantryDisplay
import com.przemas230.dietaapp.logic.PantryTiles
import com.przemas230.dietaapp.logic.RecipePantryMatching
import com.przemas230.dietaapp.logic.ShoppingDisplay
import com.przemas230.dietaapp.logic.WeekPlan
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
fun PantryScreen(
    viewModel: PantryViewModel,
    allRecipes: List<Recipe>,
    activityLogViewModel: ActivityLogViewModel,
    // FR-21/22/26/28/42 v2 (ported to Android 2026-08-29): destructive
    // "wyczyść/losuj" actions offer a Cofnij, the way the web version has
    // since 2026-08-28. MainActivity owns the SnackbarHostState (same
    // hoisting pattern PlannerScreen already uses), this just asks for one.
    onShowUndoSnackbar: (message: String, actionLabel: String, onUndo: () -> Unit) -> Unit = { _, _, _ -> },
    // FR-108: everything needed to answer "will this run out before the week
    // does". Passed in rather than collected here so this screen keeps
    // depending on exactly one ViewModel of its own -- MainActivity already
    // holds the Planer's week and the cook history for its other screens.
    weekPlan: WeekPlan = emptyMap(),
    recipesById: Map<String, Recipe> = emptyMap(),
    todayDayIndex: Int = 0,
    isCookedOnDay: (recipeId: String, dayIndex: Int) -> Boolean = { _, _ -> false },
) {
    val items by viewModel.items.collectAsState()
    // FR-102: canonical names the user deleted for good -- see
    // PantryOperations.visibleTileNames for why a separate set is needed
    // at all (the tiles themselves are derived, not stored).
    val hidden by viewModel.hidden.collectAsState()
    var addTileCategory by remember { mutableStateOf<PantryCategory?>(null) }
    var actionTarget by remember { mutableStateOf<Pair<String, PantryCategory>?>(null) }
    var showClearAllConfirm by remember { mutableStateOf(false) }
    var deleteForeverTarget by remember { mutableStateOf<String?>(null) }
    var showRestoreHiddenConfirm by remember { mutableStateOf(false) }

    val recipeTileNames = remember(allRecipes) { PantryTiles.buildTileNames(allRecipes) }
    val unitCats = remember(allRecipes) { PantryTiles.computeTileUnitCats(allRecipes) }
    // Union with currently-tracked names too, so a custom tile (or an item
    // tracked via FR-16's "Mam to", which can use a name outside any recipe)
    // keeps showing even though it's not recipe-derived.
    val tileNames = remember(recipeTileNames, items.keys, hidden) {
        PantryOperations.visibleTileNames(recipeTileNames, items.keys, hidden)
    }
    val grouped = remember(tileNames, items) {
        tileNames.groupBy { name -> items[name]?.category ?: PantryTiles.categoryAndEmoji(name).first }
    }
    // FR-108. Recomputed whenever the pantry itself changes, so subtracting a
    // tile down past what the week needs makes the warning appear on the very
    // next tap rather than on the next visit to this screen.
    val shortages = remember(weekPlan, recipesById, items, todayDayIndex) {
        PantryShortage.compute(weekPlan, recipesById, items, todayDayIndex, isCookedOnDay)
    }
    val shortageByName = remember(shortages) { shortages.associateBy { it.canonName } }
    var shortagesExpanded by remember { mutableStateOf(false) }
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
            "Górna połowa kafelka = dodaj, dolna połowa = odejmij. Przytrzymaj kafelek, by zmienić " +
                "kategorię, usunąć śledzenie albo usunąć produkt ze spiżarni na stałe. Przyprawy: Mało → Wystarczy → Dużo.",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodySmall,
        )
        if (shortages.isNotEmpty()) {
            PantryShortageCard(
                shortages = shortages,
                expanded = shortagesExpanded,
                onToggle = { shortagesExpanded = !shortagesExpanded },
            )
        }
        TextButton(
            onClick = { showClearAllConfirm = true },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
        ) {
            Text("🗑️ Wyczyść całą spiżarnię")
        }
        // Requested 2026-08-30 ("dodaj przycisk który dodaje po 1 kg każdego
        // składnika, żeby przetestować czy się odejmuje"): a debug/testing
        // tool, not a real feature -- gives every tracked product enough
        // headroom (+1 kg / +1 L / +20 szt.) to observe subtraction (cooking
        // a recipe, "Do spiżarni" from the shopping list) without manually
        // topping up items one at a time first. Labelled "Testowo" so it
        // doesn't read as a real inventory action.
        TextButton(
            onClick = {
                val before = items
                viewModel.addTestQuantityToAll()
                onShowUndoSnackbar("Testowo: dodano zapas do wszystkich produktów", "Cofnij") {
                    viewModel.replaceAll(before)
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
        ) {
            Text("🧪 Testowo: +1 kg / +1 L do każdego produktu")
        }
        // FR-102: only shown when there is actually something to bring back,
        // so "usuń na stałe" never becomes an irreversible mistake the user
        // can neither see nor undo.
        if (hidden.isNotEmpty()) {
            TextButton(
                onClick = { showRestoreHiddenConfirm = true },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            ) {
                Text("↩️ Przywróć usunięte produkty (${hidden.size})")
            }
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
                                // FR-108: the tile itself says it, not just
                                // the card at the top -- the card is scrolled
                                // away by the time the user is looking at the
                                // product it is talking about.
                                shortage = shortageByName[name],
                                onTap = { dir ->
                                    viewModel.tileTapDelta(name, category, unitCat, dir)
                                    activityLogViewModel.log("pantry_add", "Spiżarnia: $name (${if (dir > 0) "+" else "-"}1)")
                                },
                                // FR-102: the menu used to open only for
                                // TRACKED tiles, which made "usuń produkt na
                                // stałe" unreachable for exactly the tiles
                                // most worth removing -- recipe-derived ones
                                // the user never tracks and doesn't want to
                                // scroll past. Every tile opens it now; the
                                // stock-specific rows inside simply do
                                // nothing when there is no stock.
                                onLongPress = { actionTarget = name to category },
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
                // FR-102: adding a product back by hand un-deletes it.
                viewModel.unhide(name)
                viewModel.tileTapDelta(name, category, unitCats[name] ?: "count", dir = 1)
                activityLogViewModel.log("pantry_add", "Dodano własny produkt: $name (${category.label})")
            },
            onDismiss = { addTileCategory = null },
        )
    }
    actionTarget?.let { (name, category) ->
        val product = items[name] as? PantryItem.Product
        val unitCat = unitCats[name] ?: "count"
        TileActionDialog(
            name = name,
            currentCategory = category,
            stepUnitCat = unitCat.takeIf { it == "weight" || it == "volume" },
            currentStep = product?.stepOverride ?: PantryTiles.tileStep(unitCat),
            onChangeCategory = { newCategory ->
                viewModel.changeCategory(name, newCategory)
                activityLogViewModel.log("pantry_recat", "Zmieniono kategorię „$name” na: ${newCategory.label}")
            },
            onChangeStep = { newStep ->
                viewModel.changeStep(name, newStep)
                activityLogViewModel.log("pantry_add", "Zmieniono skok +/-: „$name” na $newStep")
            },
            onRemoveTracking = {
                viewModel.removeItem(name)
                activityLogViewModel.log("pantry_delete", "Usunięto ze spiżarni: $name")
            },
            onDeleteForever = { deleteForeverTarget = name },
            onDismiss = { actionTarget = null },
        )
    }
    deleteForeverTarget?.let { name ->
        AlertDialog(
            onDismissRequest = { deleteForeverTarget = null },
            title = { Text("Usunąć „$name” na stałe?") },
            text = { Text("Kafelek zniknie ze Spiżarni razem ze stanem. Możesz go przywrócić przyciskiem „↩️ Przywróć usunięte produkty” na górze tego ekranu.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteForever(name)
                    activityLogViewModel.log("pantry_delete", "Usunięto produkt ze spiżarni na stałe: $name")
                    deleteForeverTarget = null
                }) { Text("Usuń na stałe") }
            },
            dismissButton = {
                TextButton(onClick = { deleteForeverTarget = null }) { Text("Anuluj") }
            },
        )
    }
    if (showRestoreHiddenConfirm) {
        AlertDialog(
            onDismissRequest = { showRestoreHiddenConfirm = false },
            title = { Text("Przywrócić usunięte produkty?") },
            text = { Text("${hidden.size} produktów wróci do Spiżarni jako nieśledzone kafelki, bez stanu.") },
            confirmButton = {
                TextButton(onClick = {
                    val count = hidden.size
                    viewModel.restoreHidden()
                    activityLogViewModel.log("pantry_add", "Przywrócono usunięte produkty spiżarni ($count)")
                    showRestoreHiddenConfirm = false
                }) { Text("Przywróć") }
            },
            dismissButton = {
                TextButton(onClick = { showRestoreHiddenConfirm = false }) { Text("Anuluj") }
            },
        )
    }
    if (showClearAllConfirm) {
        AlertDialog(
            onDismissRequest = { showClearAllConfirm = false },
            title = { Text("Wyczyścić całą spiżarnię?") },
            text = { Text("Usunie śledzenie wszystkich produktów i przypraw. Własne kafelki i zmienione kategorie/jednostki zostają.") },
            confirmButton = {
                TextButton(onClick = {
                    // FR-28/v2: snapshot BEFORE clearing, so "Cofnij" puts
                    // back the real quantities rather than re-adding empty
                    // tiles. Cheap -- the pantry is a small in-memory map.
                    val before = items
                    viewModel.replaceAll(emptyMap())
                    activityLogViewModel.log("pantry_delete", "Wyczyszczono całą spiżarnię")
                    showClearAllConfirm = false
                    onShowUndoSnackbar("Wyczyszczono spiżarnię (${before.size})", "Cofnij") {
                        viewModel.replaceAll(before)
                        activityLogViewModel.log("pantry_add", "Cofnięto wyczyszczenie spiżarni")
                    }
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
    shortage: PantryShortage.Shortage? = null,
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
                    when {
                        // FR-108: drawn even in "metro", which otherwise has
                        // no tile border at all -- a warning that only some
                        // themes show is a warning that gets missed.
                        shortage != null -> Modifier.border(2.dp, MaterialTheme.colorScheme.error, shape)
                        !metro -> Modifier.border(1.5.dp, if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline, shape)
                        else -> Modifier
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
                    .background(
                        if (shortage != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary,
                        RoundedCornerShape(50),
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            ) {
                Text(
                    if (shortage != null) "⚠ $badgeText" else badgeText,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (shortage != null) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSecondary,
                )
            }
        }
    }
}

/**
 * FR-108: the one place that says outright "this will not be enough", with
 * the numbers behind it. Collapsed to the three worst by default -- the
 * point is a nudge before shopping, not an inventory report; the rest is one
 * tap away.
 */
@Composable
private fun PantryShortageCard(
    shortages: List<PantryShortage.Shortage>,
    expanded: Boolean,
    onToggle: () -> Unit,
) {
    val shown = if (expanded) shortages else shortages.take(3)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable(enabled = shortages.size > 3) { onToggle() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                "⚠️ Nie starczy na zaplanowane dania (${shortages.size})",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onErrorContainer,
            )
            shown.forEach { shortage ->
                val have = ShoppingDisplay.formatQty(shortage.unitCat, shortage.haveBase)
                val needed = ShoppingDisplay.formatQty(shortage.unitCat, shortage.neededBase)
                Column {
                    Text(
                        "${PantryDisplay.displayName(shortage.canonName, null)} — masz $have, trzeba $needed",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                    )
                    Text(
                        "${PantryShortage.dishCountLabel(shortage.dishes.size)}: ${shortage.dishes.joinToString(", ")}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                    )
                }
            }
            if (shortages.size > 3) {
                Text(
                    if (expanded) "Zwiń" else "…i jeszcze ${shortages.size - 3} — stuknij, by pokazać",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                )
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
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun TileActionDialog(
    name: String,
    currentCategory: PantryCategory,
    stepUnitCat: String?,
    currentStep: Double,
    onChangeCategory: (PantryCategory) -> Unit,
    onChangeStep: (Double) -> Unit,
    onRemoveTracking: () -> Unit,
    /** FR-102: "❌ Usuń produkt ze spiżarni na stałe" -- the caller confirms before actually doing it. */
    onDeleteForever: () -> Unit,
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
                // On explicit user request ("dodaj opcje zmieniania skoku po
                // przytrzymaniu kafelka") -- only weight/volume have a
                // meaningful numeric +/- increment to override (count steps
                // by 1 whole item, spices cycle Mało/Wystarczy/Dużo).
                if (stepUnitCat != null) {
                    val unitLabel = if (stepUnitCat == "weight") "g" else "ml"
                    val stepOptions = if (stepUnitCat == "weight") {
                        listOf(10.0, 25.0, 50.0, 100.0, 250.0, 500.0)
                    } else {
                        listOf(10.0, 25.0, 50.0, 100.0, 200.0, 250.0)
                    }
                    Text("🔢 Skok +/-", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        stepOptions.forEach { step ->
                            FilterChip(
                                selected = step == currentStep,
                                onClick = { onChangeStep(step) },
                                label = { Text("${step.toInt()} $unitLabel") },
                            )
                        }
                    }
                }
                TextButton(onClick = {
                    onRemoveTracking()
                    onDismiss()
                }) { Text("🗑️ Usuń śledzenie (wyzeruj stan)") }
                // FR-102: the row above only zeroes the stock and leaves the
                // tile in the grid, which read as "nie da się usunąć
                // produktu ze spiżarni całkowicie". This one removes the
                // tile itself.
                TextButton(onClick = {
                    onDeleteForever()
                    onDismiss()
                }) {
                    Text("❌ Usuń produkt ze spiżarni na stałe", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
