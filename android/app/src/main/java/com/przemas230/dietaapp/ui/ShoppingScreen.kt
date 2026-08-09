package com.przemas230.dietaapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.przemas230.dietaapp.data.ShoppingItem
import com.przemas230.dietaapp.logic.ShoppingDisplay

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

        if (weekPlan.values.any { it.isNotEmpty() }) {
            Button(
                onClick = {
                    val recipesById = allRecipes.associateBy { it.id }
                    viewModel.addWeekPlan(weekPlan, recipesById)
                },
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
