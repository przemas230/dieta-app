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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.przemas230.dietaapp.data.ShoppingItem

/**
 * Local shopping-list screen, structurally closest to state.shopping in the
 * web app (README.md "Co dalej" krok 3) — a name-keyed map of items with
 * quantity/unit/checked. Not yet auto-filled from the planner and not yet
 * synced — those come with README.md steps 4/6.
 */
@Composable
fun ShoppingScreen(viewModel: ShoppingViewModel = viewModel()) {
    val items by viewModel.items.collectAsState()
    var showAddForm by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Lista zakupów", style = MaterialTheme.typography.titleMedium)
            Row {
                TextButton(onClick = { viewModel.clearChecked() }) { Text("Usuń kupione") }
                TextButton(onClick = { showAddForm = !showAddForm }) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Text(if (showAddForm) "Zamknij" else "Dodaj")
                }
            }
        }

        if (showAddForm) {
            AddShoppingItemForm(onAdd = { name, qty, unit -> viewModel.addItem(name, qty, unit) })
        }

        if (items.isEmpty()) {
            Text(
                "Lista zakupów jest pusta.",
                modifier = Modifier.padding(24.dp),
                style = MaterialTheme.typography.bodyMedium,
            )
        } else {
            LazyColumn(contentPadding = PaddingValues(12.dp)) {
                items(items.values.toList(), key = { it.name }) { item ->
                    ShoppingRow(
                        item = item,
                        onToggle = { viewModel.toggleChecked(item.name) },
                        onRemove = { viewModel.removeItem(item.name) },
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
                Text(
                    "${item.name} — ${item.quantity} ${item.unit}",
                    textDecoration = if (item.checked) TextDecoration.LineThrough else TextDecoration.None,
                )
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Filled.Delete, contentDescription = "Usuń")
            }
        }
    }
}

@Composable
private fun AddShoppingItemForm(onAdd: (String, Double, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var unit by remember { mutableStateOf("szt.") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nazwa produktu") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Ilość") },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            OutlinedTextField(
                value = unit,
                onValueChange = { unit = it },
                label = { Text("Jednostka") },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
        }
        TextButton(onClick = {
            onAdd(name, quantity.toDoubleOrNull() ?: 1.0, unit)
            name = ""
            quantity = "1"
        }) {
            Text("Dodaj do listy")
        }
    }
}
