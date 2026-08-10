package com.przemas230.dietaapp.ui

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.przemas230.dietaapp.data.ShoppingItem
import com.przemas230.dietaapp.logic.DayCardState
import com.przemas230.dietaapp.logic.ShoppingDayCard
import com.przemas230.dietaapp.logic.ShoppingDayStrip
import com.przemas230.dietaapp.logic.ShoppingDisplay
import com.przemas230.dietaapp.logic.ShoppingOperations
import java.util.Calendar

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
fun ShoppingScreen(viewModel: ShoppingViewModel, plannerViewModel: PlannerViewModel) {
    val items by viewModel.items.collectAsState()
    val weekPlan by plannerViewModel.weekPlan.collectAsState()
    val allRecipes by plannerViewModel.allRecipes.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Lista zakupów (${items.size})", style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = { viewModel.clearChecked() }) { Text("Usuń kupione") }
        }

        // FR-58/FR-62: one strip of 7 day cards, each stating its own
        // add-to-shopping-list status and doubling as the add button itself.
        val todayIdx = remember { ShoppingDayStrip.todayIndex(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1) }
        val dayCards = remember(weekPlan, items) {
            ShoppingDayStrip.buildCards(weekPlan, { rid -> ShoppingOperations.isRecipeAdded(items, rid) }, todayIdx)
        }
        val recipesById = remember(allRecipes) { allRecipes.associateBy { it.id } }
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
            Text(
                "Lista zakupów jest pusta — dodaj składniki zaznaczając przepisy w zakładce Przepisy 🍽 lub przyciskiem w Planerze.",
                modifier = Modifier.padding(24.dp),
                style = MaterialTheme.typography.bodyMedium,
            )
        } else {
            val sorted = remember(items) { items.entries.sortedBy { it.value.name } }
            LazyColumn(contentPadding = PaddingValues(12.dp)) {
                items(sorted, key = { it.key }) { (key, item) ->
                    ShoppingRow(
                        item = item,
                        onToggle = { viewModel.toggleChecked(key) },
                        onRemove = { viewModel.removeItem(key) },
                    )
                }
            }
        }
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

@Composable
private fun ShoppingRow(item: ShoppingItem, onToggle: () -> Unit, onRemove: () -> Unit) {
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
                    "$qtyLabel $displayName",
                    textDecoration = if (item.checked) TextDecoration.LineThrough else TextDecoration.None,
                )
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Filled.Delete, contentDescription = "Usuń")
            }
        }
    }
}
