package com.przemas230.dietaapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.przemas230.dietaapp.data.PantryCategory
import com.przemas230.dietaapp.data.PantryItem

/**
 * Local pantry screen, structurally closest to state.pantry in the web app
 * (README.md "Co dalej" krok 3) — a name-keyed map of products (qty+unit)
 * and spices (Brak/Mało/Wystarczy level). No sync yet.
 */
@Composable
fun PantryScreen(viewModel: PantryViewModel) {
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
            Text("Spiżarnia", style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = { showAddForm = !showAddForm }) {
                Icon(Icons.Filled.Add, contentDescription = null)
                Text(if (showAddForm) "Zamknij" else "Dodaj")
            }
        }

        if (showAddForm) {
            AddPantryItemForm(
                onAddProduct = { name, cat, qty, unit -> viewModel.addProduct(name, cat, qty, unit) },
                onAddSpice = { name, cat -> viewModel.addSpice(name, cat, com.przemas230.dietaapp.data.SpiceLevel.WYSTARCZY) },
            )
        }

        if (items.isEmpty()) {
            Text(
                "Spiżarnia jest pusta — dodaj pierwszy produkt.",
                modifier = Modifier.padding(24.dp),
                style = MaterialTheme.typography.bodyMedium,
            )
        } else {
            LazyColumn(contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp)) {
                items(items.values.toList(), key = { it.name }) { item ->
                    PantryRow(
                        item = item,
                        onAdjustQty = { delta -> viewModel.adjustProductQuantity(item.name, delta) },
                        onCycleLevel = { viewModel.cycleSpiceLevel(item.name) },
                        onRemove = { viewModel.removeItem(item.name) },
                    )
                }
            }
        }
    }
}

@Composable
private fun PantryRow(
    item: PantryItem,
    onAdjustQty: (Double) -> Unit,
    onCycleLevel: () -> Unit,
    onRemove: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(item.name, fontWeight = FontWeight.SemiBold)
                Text(item.category.label, style = MaterialTheme.typography.bodySmall)
            }
            when (item) {
                is PantryItem.Product -> Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onAdjustQty(-1.0) }) {
                        Icon(Icons.Filled.Remove, contentDescription = "Zmniejsz ilość")
                    }
                    Text("${item.quantity} ${item.unit}")
                    IconButton(onClick = { onAdjustQty(1.0) }) {
                        Icon(Icons.Filled.Add, contentDescription = "Zwiększ ilość")
                    }
                    IconButton(onClick = onRemove) {
                        Icon(Icons.Filled.Delete, contentDescription = "Usuń")
                    }
                }
                is PantryItem.Spice -> Row(verticalAlignment = Alignment.CenterVertically) {
                    FilledTonalButton(onClick = onCycleLevel) { Text(item.level.label) }
                    IconButton(onClick = onRemove) {
                        Icon(Icons.Filled.Delete, contentDescription = "Usuń")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddPantryItemForm(
    onAddProduct: (String, PantryCategory, Double, String) -> Unit,
    onAddSpice: (String, PantryCategory) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var unit by remember { mutableStateOf("szt.") }
    var isSpice by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf(PantryCategory.INNE) }
    var categoryMenuExpanded by remember { mutableStateOf(false) }

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

        ExposedDropdownMenuBox(expanded = categoryMenuExpanded, onExpandedChange = { categoryMenuExpanded = it }) {
            OutlinedTextField(
                value = category.label,
                onValueChange = {},
                readOnly = true,
                label = { Text("Kategoria") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryMenuExpanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
            )
            DropdownMenu(expanded = categoryMenuExpanded, onDismissRequest = { categoryMenuExpanded = false }) {
                PantryCategory.entries.forEach { cat ->
                    DropdownMenuItem(text = { Text(cat.label) }, onClick = {
                        category = cat
                        categoryMenuExpanded = false
                    })
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = { isSpice = false }) { Text(if (!isSpice) "● Produkt" else "○ Produkt") }
            TextButton(onClick = { isSpice = true }) { Text(if (isSpice) "● Przyprawa" else "○ Przyprawa") }
        }

        if (!isSpice) {
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
        }

        FilledTonalButton(onClick = {
            if (isSpice) {
                onAddSpice(name, category)
            } else {
                onAddProduct(name, category, quantity.toDoubleOrNull() ?: 1.0, unit)
            }
            name = ""
            quantity = "1"
        }) {
            Text("Dodaj do spiżarni")
        }
    }
}
