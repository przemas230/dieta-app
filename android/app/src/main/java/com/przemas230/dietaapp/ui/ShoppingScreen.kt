package com.przemas230.dietaapp.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PantryItem
import com.przemas230.dietaapp.data.ShoppingItem
import com.przemas230.dietaapp.logic.AppThemes
import com.przemas230.dietaapp.logic.DayCardState
import com.przemas230.dietaapp.logic.IngredientCanon
import com.przemas230.dietaapp.logic.PantryTiles
import com.przemas230.dietaapp.logic.RecipePantryMatching
import com.przemas230.dietaapp.logic.ShoppingDayCard
import com.przemas230.dietaapp.logic.ShoppingDayStrip
import com.przemas230.dietaapp.logic.ShoppingDisplay
import com.przemas230.dietaapp.logic.ShoppingOperations
import com.przemas230.dietaapp.ui.theme.LocalDietaThemeId
import java.util.Calendar
import com.przemas230.dietaapp.logic.PolishText
import androidx.compose.material3.OutlinedTextField

/**
 * FR-25/FR-27: list built exclusively from recipes (per-ingredient "🛒" on a
 * recipe's pantry-check window, whole-recipe "Dodaj do listy zakupów" on the
 * card, or this screen's own "🛒 Dodaj składniki z całego tygodnia" button),
 * same as index.html: there's no manual "add an arbitrary item" form there
 * either (its empty-state message literally only mentions those entry
 * points). Quantities of the same canonical ingredient/unit accumulate
 * across recipes; each row shows the FR-29/FR-25 grammatically-agreeing name
 * via ShoppingDisplay.
 */
@Composable
fun ShoppingScreen(
    viewModel: ShoppingViewModel,
    plannerViewModel: PlannerViewModel,
    pantryViewModel: PantryViewModel,
    // FR-21/22/26/28/42 v2 (ported to Android 2026-08-29): destructive
    // "wyczyść/losuj" actions offer a Cofnij, the way the web version has
    // since 2026-08-28. MainActivity owns the SnackbarHostState (same
    // hoisting pattern PlannerScreen already uses), this just asks for one.
    onShowUndoSnackbar: (message: String, actionLabel: String, onUndo: () -> Unit) -> Unit = { _, _, _ -> },
) {
    val items by viewModel.items.collectAsState()
    val weekPlan by plannerViewModel.weekPlan.collectAsState()
    val allRecipes by plannerViewModel.allRecipes.collectAsState()
    val pantryItems by pantryViewModel.items.collectAsState()
    val context = LocalContext.current
    // FR-75: list view (default) vs. tile grid view -- same data either way,
    // just presentation, per the FR's own "przełącznik widoku nie zmienia
    // zawartości" criterion.
    var tileView by remember { mutableStateOf(false) }
    // FR-99 (ported to Android 2026-08-29). Deliberately NOT persisted and
    // NOT synced: a search term is a transient way of LOOKING at the list,
    // not part of it -- syncing it would mean one device could leave another
    // device's list mysteriously filtered.
    var searchTerm by remember { mutableStateOf("") }
    var showClearAllConfirm by remember { mutableStateOf(false) }
    val todayIdx = remember { ShoppingDayStrip.todayIndex(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1) }
    val recipesById = remember(allRecipes) { allRecipes.associateBy { it.id } }
    // 2026-08-11: which planner day(s) need each item, so the user can shop
    // for just "today + jutro" and stop -- see ShoppingOperations.computeIngredientDays.
    val ingredientDays = remember(items, weekPlan, recipesById) { ShoppingOperations.computeIngredientDays(items, weekPlan, recipesById) }
    // FR-87: motyw "Klinika" pokazuje wiersz z kolorowym badge kategorii
    // (IngredientCanon.CANON_INFO.cat -- dane juz istnieja, zero nowej logiki).
    val isClinic = AppThemes.isClinicFamily(LocalDietaThemeId.current)
    // FR-99: `items` stays the FULL list and `visibleItems` is what gets
    // rendered. Keeping them apart is what lets "lista jest pusta" and "nic
    // nie pasuje" stay two different messages -- collapsing them would tell
    // someone with an empty list that their search found nothing, instead of
    // how to add the first ingredient. Diacritics-insensitive via
    // PolishText, same as every other search in the app.
    // FR-106: ticking an item off means BOUGHT. When that tick was the last
    // one a recipe was waiting on, the app now knows the user has everything
    // for that dish -- and that is the moment to offer moving it into the
    // pantry, which is what makes "🏺 N/M w spiżarni" and the Planer's
    // "zrobione" swipe work afterwards.
    //
    // It OFFERS rather than acts: buying is not cooking, and silently filling
    // the pantry on every tick would be both noisy and occasionally wrong
    // (things do get put back on the shelf).
    val onTicked: (String) -> Unit = { key ->
        val boughtBefore = ShoppingOperations.fullyBoughtRecipes(items)
        viewModel.toggleChecked(key)
        val justBought = ShoppingOperations.fullyBoughtRecipes(viewModel.items.value) - boughtBefore
        justBought.forEach { recipeId ->
            val recipe = recipesById[recipeId]
            if (recipe != null) {
                onShowUndoSnackbar("Masz już wszystko na „${recipe.name}”", "Do spiżarni") {
                    pantryViewModel.stockFromRecipe(recipe)
                }
            }
        }
    }
    val visibleItems = remember(items, searchTerm) {
        if (searchTerm.isBlank()) items
        else items.filterValues { PolishText.contains(it.name, searchTerm.trim()) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                // FR-99: "N z M pozycji" while filtering, so the count never
                // contradicts what is on screen but still shows the rest of
                // the list exists.
                if (searchTerm.isBlank()) {
                    "Lista zakupów (${items.size})"
                } else {
                    "Lista zakupów (${visibleItems.size} z ${items.size})"
                },
                style = MaterialTheme.typography.titleMedium,
            )
            TextButton(onClick = {
                // FR-26/v2 (ported to Android 2026-08-30): this was the
                // worst offender in the app on web too before that fix --
                // irreversible delete with neither a confirm nor an undo, so
                // one mistap on a long list (an 87-item one was a real
                // reported case) silently destroyed work. Stays instant
                // (asking would be wrong for an in-flow tidy-up action) but
                // now snapshots first and offers "Cofnij"; a no-op tap with
                // nothing checked says so instead of pretending to have done
                // something.
                val checkedKeys = items.filterValues { it.checked }.keys
                if (checkedKeys.isEmpty()) {
                    Toast.makeText(context, "Nie ma odhaczonych pozycji do usunięcia", Toast.LENGTH_SHORT).show()
                } else {
                    val before = items
                    viewModel.clearChecked()
                    onShowUndoSnackbar("Usunięto ${checkedKeys.size} odhaczonych pozycji", "Cofnij") {
                        viewModel.replaceAll(before)
                    }
                }
            }) { Text("Usuń kupione") }
        }

        // FR-99: real shopping lists here get long (an 87-item one was
        // recorded while debugging FR-87/v9) and are ordered by category --
        // useful in the shop, useless for answering "did I already add
        // milk?" without scrolling the whole thing.
        if (items.isNotEmpty()) {
            OutlinedTextField(
                value = searchTerm,
                onValueChange = { searchTerm = it },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                singleLine = true,
                placeholder = { Text("Szukaj na liście zakupów…") },
                trailingIcon = {
                    if (searchTerm.isNotEmpty()) {
                        TextButton(onClick = { searchTerm = "" }) { Text("✕") }
                    }
                },
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FilterChip(selected = !tileView, onClick = { tileView = false }, label = { Text("📃 Lista") })
            FilterChip(selected = tileView, onClick = { tileView = true }, label = { Text("🏺 Kafelki (jak w spiżarni)") })
        }

        // FR-26: share the (unchecked) list as plain text through Android's
        // own share sheet -- the native equivalent of index.html's
        // navigator.share()/SMS/WhatsApp/copy buttons, all of which just
        // hand the same buildListText()-equivalent string to whatever the
        // user picks; "Wyczyść całą listę" was the other still-missing
        // piece of FR-26 (only "Usuń kupione"/clearChecked existed before).
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                onClick = {
                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, ShoppingOperations.buildShareText(items, ingredientDays, todayIdx))
                    }
                    context.startActivity(Intent.createChooser(sendIntent, null))
                },
                modifier = Modifier.weight(1f),
            ) {
                Text("📤 Udostępnij")
            }
            TextButton(onClick = { showClearAllConfirm = true }) { Text("Wyczyść całą listę") }
        }

        // FR-58/FR-62: one strip of 7 day cards, each stating its own
        // add-to-shopping-list status and doubling as the add button itself.
        val dayCards = remember(weekPlan, items) {
            ShoppingDayStrip.buildCards(weekPlan, { rid -> ShoppingOperations.isRecipeAdded(items, rid) }, todayIdx)
        }
        ShoppingDayStripRow(
            cards = dayCards,
            onDayClick = { card ->
                val message = viewModel.addDayPlanWithMessage(
                    weekPlan[card.day].orEmpty(),
                    recipesById,
                    ShoppingDayStrip.clickDayLabel(card),
                )
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            },
            onDayLongPress = { card ->
                val detail = if (card.planned > 0) {
                    "${card.dayName}: ${card.onList}/${card.planned} dań na liście zakupów"
                } else {
                    "${card.dayName}: brak zaplanowanych dań"
                }
                Toast.makeText(context, detail, Toast.LENGTH_SHORT).show()
            },
        )
        Spacer(modifier = Modifier.height(4.dp))

        if (weekPlan.values.any { it.isNotEmpty() }) {
            Button(
                onClick = { viewModel.addWeekPlan(weekPlan, recipesById) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            ) {
                Text("🛒 Dodaj składniki z całego tygodnia (Planer)")
            }
        }

        if (items.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (isClinic) {
                    Text("🛒", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Brak pozycji na liście", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(
                    "Lista zakupów jest pusta — dodaj składniki zaznaczając przepisy w zakładce Przepisy 🍽 lub przyciskiem w Planerze.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
            }
        } else if (tileView) {
            // Requested 2026-08-25: items were sorted flat by name only --
            // category badges existed per-tile but items from different
            // categories were interleaved instead of grouped into visible
            // sections, unlike the web version and unlike this app's OWN
            // Spiżarnia grid just below. Reuses the exact grouping
            // ShoppingOperations.buildShareText() already uses for the
            // share-sheet text export (PantryTiles.categoryAndEmoji +
            // CATEGORY_ORDER) rather than inventing a second scheme.
            val grouped = remember(visibleItems) {
                visibleItems.entries.groupBy { PantryTiles.categoryAndEmoji(it.value.name).first }
            }
            LazyVerticalGrid(columns = GridCells.Fixed(3), contentPadding = PaddingValues(12.dp)) {
                PantryTiles.CATEGORY_ORDER.forEach { category ->
                    val group = grouped[category]?.sortedBy { it.value.name } ?: return@forEach
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            category.label,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp, bottom = 2.dp),
                        )
                    }
                    gridItems(group, key = { it.key }) { (key, item) ->
                        ShoppingTile(
                            item = item,
                            pantryEntry = pantryItems[item.name] as? PantryItem.Product,
                            dayLabel = ShoppingOperations.formatIngredientDays(ingredientDays[key], todayIdx),
                            onToggle = { onTicked(key) },
                        )
                    }
                }
            }
        } else {
            // Requested 2026-08-25: same fix as the tile view above, grouped
            // into visible category sections instead of one flat sorted list.
            val grouped = remember(visibleItems) {
                visibleItems.entries.groupBy { PantryTiles.categoryAndEmoji(it.value.name).first }
            }
            LazyColumn(contentPadding = PaddingValues(12.dp)) {
                // FR-99: a filter that matches nothing says so, with the
                // phrase that was typed -- distinct from the "list is empty"
                // message above, which is about having nothing to shop for.
                if (visibleItems.isEmpty()) {
                    item {
                        Text(
                            "Nic nie pasuje do „${searchTerm.trim()}” na liście zakupów.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth().padding(24.dp),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                PantryTiles.CATEGORY_ORDER.forEach { category ->
                    val group = grouped[category]?.sortedBy { it.value.name } ?: return@forEach
                    item {
                        Text(
                            category.label,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp, bottom = 2.dp),
                        )
                    }
                    items(group, key = { it.key }) { (key, item) ->
                        if (isClinic) {
                            ShoppingRowClinic(
                                item = item,
                                dayLabel = ShoppingOperations.formatIngredientDays(ingredientDays[key], todayIdx),
                                onToggle = { onTicked(key) },
                                onRemove = { viewModel.removeItem(key) },
                            )
                        } else {
                            ShoppingRow(
                                item = item,
                                dayLabel = ShoppingOperations.formatIngredientDays(ingredientDays[key], todayIdx),
                                onToggle = { onTicked(key) },
                                onRemove = { viewModel.removeItem(key) },
                            )
                        }
                    }
                }
            }
        }
    }

    if (showClearAllConfirm) {
        AlertDialog(
            onDismissRequest = { showClearAllConfirm = false },
            title = { Text("Wyczyścić całą listę zakupów?") },
            text = { Text("Usunie wszystkie pozycje, także te jeszcze nieodhaczone.") },
            confirmButton = {
                TextButton(onClick = {
                    // FR-26/v2: snapshot BEFORE clearing so "Cofnij" restores
                    // the real list -- quantities, checked flags and the
                    // recipe links that drive "czy ten przepis jest na
                    // liście" -- not just the item names.
                    val before = items
                    viewModel.clearAll()
                    showClearAllConfirm = false
                    onShowUndoSnackbar("Wyczyszczono listę zakupów (${before.size})", "Cofnij") {
                        viewModel.replaceAll(before)
                    }
                }) { Text("Wyczyść") }
            },
            dismissButton = {
                TextButton(onClick = { showClearAllConfirm = false }) { Text("Anuluj") }
            },
        )
    }
}

/** FR-58/FR-62: the horizontally-scrolling strip of 7 day cards above the shopping list. */
@Composable
private fun ShoppingDayStripRow(
    cards: List<ShoppingDayCard>,
    onDayClick: (ShoppingDayCard) -> Unit,
    onDayLongPress: (ShoppingDayCard) -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
    ) {
        items(cards, key = { it.day }) { card ->
            ShoppingDayCardView(card = card, onClick = { onDayClick(card) }, onLongPress = { onDayLongPress(card) })
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ShoppingDayCardView(card: ShoppingDayCard, onClick: () -> Unit, onLongPress: () -> Unit) {
    val isToday = card.label == "Dziś"
    val borderColor = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    val containerColor = if (card.state == DayCardState.DONE) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val statusText = when (card.state) {
        DayCardState.EMPTY -> "—"
        DayCardState.TODO -> "Dodaj"
        DayCardState.STARTED -> "${card.onList}/${card.planned}"
        DayCardState.DONE -> "Gotowe"
    }
    val statusColor = when (card.state) {
        DayCardState.TODO -> MaterialTheme.colorScheme.tertiary
        DayCardState.STARTED, DayCardState.DONE -> MaterialTheme.colorScheme.primary
        DayCardState.EMPTY -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier
            .width(68.dp)
            .border(if (isToday) 2.dp else 1.dp, borderColor, MaterialTheme.shapes.medium)
            .combinedClickable(onClick = onClick, onLongClick = onLongPress),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp)
                .alpha(if (card.state == DayCardState.EMPTY) 0.55f else 1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                card.label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
            Text(statusText, style = MaterialTheme.typography.labelSmall, color = statusColor, maxLines = 1)
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { card.progressPct / 100f },
                modifier = Modifier.fillMaxWidth().height(4.dp),
            )
        }
    }
}

/**
 * FR-75: one tile in the "🏺 Kafelki" grid view -- same visual idea as a
 * pantry tile (emoji + name + a badge), badge showing the NEGATIVE amount
 * still missing after subtracting pantry stock (RecipePantryMatching.
 * missingAfterPantry), or "✓" once pantry stock fully covers it. Tapping
 * toggles checked, same action as the list view's checkbox -- both views
 * read/write the same ShoppingViewModel state, so there's never a second
 * source of truth to keep in sync.
 */
@Composable
private fun ShoppingTile(item: ShoppingItem, pantryEntry: PantryItem.Product?, dayLabel: String = "", onToggle: () -> Unit) {
    val missing = remember(item.name, item.unitCat, item.quantity, pantryEntry) {
        RecipePantryMatching.missingAfterPantry(item.quantity, item.unitCat, pantryEntry)
    }
    val badge = if (missing == null) "✓" else "−${ShoppingDisplay.formatQty(item.unitCat, missing)}"
    val emoji = IngredientCanon.CANON_INFO[item.name]?.emoji ?: "🛒"
    val displayName = ShoppingDisplay.displayName(item.name, item.unitCat, item.quantity) + dayLabel
    val containerColor = if (item.checked) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable(onClick = onToggle),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(emoji, fontSize = 22.sp)
            Text(
                displayName,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                textDecoration = if (item.checked) TextDecoration.LineThrough else TextDecoration.None,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                badge,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (missing == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
private fun ShoppingRow(item: ShoppingItem, dayLabel: String = "", onToggle: () -> Unit, onRemove: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = item.checked, onCheckedChange = { onToggle() })
                val qtyLabel = ShoppingDisplay.formatQty(item.unitCat, item.quantity)
                val displayName = ShoppingDisplay.displayName(item.name, item.unitCat, item.quantity)
                Text(
                    "$qtyLabel $displayName$dayLabel",
                    textDecoration = if (item.checked) TextDecoration.LineThrough else TextDecoration.None,
                )
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Filled.Delete, contentDescription = "Usuń")
            }
        }
    }
}

/** FR-87: bento-wariant dla motywu "Klinika" -- ten sam stan/callbacki co ShoppingRow, plus kolorowy badge kategorii (IngredientCanon.CANON_INFO.cat). */
private val CATEGORY_BADGE_COLORS = mapOf(
    "Nabiał" to Color(0xFFE7EEF5),
    "Warzywa" to Color(0xFFE3F2EC),
    "Owoce" to Color(0xFFFBEFE1),
    "Mięso" to Color(0xFFF7E3E3),
    "Ryby" to Color(0xFFE1F0F5),
    "Produkty zbożowe" to Color(0xFFF3EDE1),
    "Orzechy i nasiona" to Color(0xFFEFE7D8),
    "Przyprawy" to Color(0xFFF1E7F5),
    "Napoje" to Color(0xFFE1F5F2),
    "Inne" to Color(0xFFEDEDED),
)

@Composable
private fun ShoppingRowClinic(item: ShoppingItem, dayLabel: String = "", onToggle: () -> Unit, onRemove: () -> Unit) {
    val category = IngredientCanon.CANON_INFO[item.name]?.cat
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = if (item.checked) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Checkbox(checked = item.checked, onCheckedChange = { onToggle() })
                Column(modifier = Modifier.weight(1f)) {
                    val qtyLabel = ShoppingDisplay.formatQty(item.unitCat, item.quantity)
                    val displayName = ShoppingDisplay.displayName(item.name, item.unitCat, item.quantity)
                    Text(
                        "$qtyLabel $displayName$dayLabel",
                        style = MaterialTheme.typography.bodyMedium,
                        textDecoration = if (item.checked) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (item.checked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                    )
                    if (category != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(CATEGORY_BADGE_COLORS[category] ?: MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = 8.dp, vertical = 2.dp),
                        ) {
                            Text(category, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Filled.Delete, contentDescription = "Usuń", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
