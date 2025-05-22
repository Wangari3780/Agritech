package com.wangari.agritech.ui.screens.farmrecords

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.wangari.agritech.data.FarmRecordTab
import com.wangari.agritech.data.FarmRecordsViewModel
import com.wangari.agritech.models.Expense
import com.wangari.agritech.models.ExpenseCategory
import com.wangari.agritech.models.Harvest
import com.wangari.agritech.models.Inventory
import java.util.Date

@Composable
fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onAddExpense: (farmId: String, category: String, amount: Double, description: String?, date: String, photo: String?) -> Unit
) {
    var farmId by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var photo by remember { mutableStateOf("") } // Assuming photo as URL/path string

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Expense") },
        text = {
            Column {
                OutlinedTextField(
                    value = farmId,
                    onValueChange = { farmId = it },
                    label = { Text("Farm ID") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = photo,
                    onValueChange = { photo = it },
                    label = { Text("Photo URL/Path (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onAddExpense(
                    farmId,
                    category,
                    amount.toDoubleOrNull() ?: 0.0,
                    description.ifEmpty { null },
                    date,
                    photo.ifEmpty { null }
                )
            }) {
                Text("Add Expense")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// ---
// ExpenseItem
// ---

@Composable
fun ExpenseItem(expense: Expense, onDeleteClick: (Expense) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Category: ${expense.Category}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Amount: KES ${expense.amount}",
                    style = MaterialTheme.typography.bodyMedium
                )
                expense.description.let {
                    Text(
                        text = "Description: $it",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(
                    text = "Date: ${expense.Dates}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = { onDeleteClick(expense) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Expense",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// ---
// ExpensesTab
// ---

@Composable
fun ExpensesTab(expenses: List<Expense>, onDeleteExpense: (Expense) -> Unit) {
    if (expenses.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Text(
                text = "No expenses recorded yet. Click the '+' button to add one!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(expenses) { expense ->
                ExpenseItem(expense = expense, onDeleteClick = onDeleteExpense)
            }
        }
    }
}
@Composable
fun AddHarvestDialog(
    onDismiss: () -> Unit,
    onAddHarvest: (farmId: String, cropName: String, quantity: Double, unit: String, date: String, grade: String?, notes: String?, photos: List<String>) -> Unit
) {
    var farmId by remember { mutableStateOf("") }
    var cropName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var grade by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var photos by remember { mutableStateOf(emptyList<String>()) } // Simplified for now, could be a list of file paths/URLs

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Add Harvest", style = MaterialTheme.typography.titleLarge)
                OutlinedTextField(
                    value = farmId,
                    onValueChange = { farmId = it },
                    label = { Text("Farm ID") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = cropName,
                    onValueChange = { cropName = it },
                    label = { Text("Crop Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("Unit") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = grade,
                    onValueChange = { grade = it },
                    label = { Text("Grade (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                // Simplified photo input.  In a real app, you'd use a file picker.
                OutlinedTextField(
                    value = if (photos.isNotEmpty()) photos.joinToString(", ") else "",
                    onValueChange = { /* Handle photo selection here */ },
                    label = { Text("Photos (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true, //  Make it read-only, and open file picker on click
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Button(onClick = {
                        onAddHarvest(
                            farmId,
                            cropName,
                            quantity.toDoubleOrNull() ?: 0.0,
                            unit,
                            date,
                            grade,
                            notes,
                            photos
                        )
                        onDismiss()
                    }) {
                        Text("Add Harvest")
                    }
                }
            }
        }
    }
}

// ---
// Harvest Item
// ---
@Composable
fun HarvestItem(harvest: Harvest, onDeleteClick: (Harvest) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Crop: ${harvest.cropName}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Quantity: ${harvest.quantity} ${harvest.unit}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Date: ${harvest.harvestDate}", style = MaterialTheme.typography.bodySmall)
                if (harvest.qualityGrade != null) {
                    Text(text = "Grade: ${harvest.qualityGrade}", style = MaterialTheme.typography.bodySmall)
                }
                if (harvest.notes != null) {
                    Text(text = "Notes: ${harvest.notes}", style = MaterialTheme.typography.bodySmall)
                }
            }
            IconButton(onClick = { onDeleteClick(harvest) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Harvest",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// ---
// Harvests Tab
// ---

@Composable
fun HarvestsTab(harvests: List<Harvest>, onDeleteHarvest: (Harvest) -> Unit) {
    if (harvests.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No harvests recorded yet. Click the '+' button to add one!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(harvests) { harvest ->
                HarvestItem(harvest = harvest, onDeleteClick = onDeleteHarvest)
            }
        }
    }
}

// ---
// Add Inventory Dialog
// ---

@Composable
fun AddInventoryDialog(
    onDismiss: () -> Unit,
    onAddInventoryItem: (name: String, category: String, quantity: Double, unit: String, purchaseDate: String, expiryDate: String?, location: String, photo: String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var purchaseDate by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var photo by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Add Inventory Item", style = MaterialTheme.typography.titleLarge)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("Unit") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = purchaseDate,
                    onValueChange = { purchaseDate = it },
                    label = { Text("Purchase Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = { expiryDate = it },
                    label = { Text("Expiry Date (YYYY-MM-DD) (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = photo,
                    onValueChange = { photo = it },
                    label = { Text("Photo URL/Path (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Button(onClick = {
                        onAddInventoryItem(
                            name,
                            category,
                            quantity.toDoubleOrNull() ?: 0.0,
                            unit,
                            purchaseDate,
                            expiryDate,
                            location,
                            photo
                        )
                        onDismiss()
                    }) {
                        Text("Add Item")
                    }
                }
            }
        }
    }
}

// ---
// Inventory Item
// ---
@Composable
fun InventoryItem(item: Inventory, onDeleteClick: (Inventory) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Name: ${item.itemName}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Category: ${item.category}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Quantity: ${item.quantity} ${item.unit}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Purchase Date: ${item.purchaseDate}", style = MaterialTheme.typography.bodySmall)
                if (item.expiryDate != null) {
                    Text(text = "Expiry Date: ${item.expiryDate}", style = MaterialTheme.typography.bodySmall)
                }
                Text(text = "Location: ${item.storageLocation}", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { onDeleteClick(item) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Item",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// ---
// Inventory Tab
// ---

@Composable
fun InventoryTab(inventory: List<Inventory>, onDeleteInventoryItem: (Inventory) -> Unit) {
    if (inventory.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "No inventory items recorded yet. Click the '+' button to add one!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(inventory) { item ->
                InventoryItem(item = item, onDeleteClick = onDeleteInventoryItem)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmRecordsScreen(
    onNavigateBack: () -> Unit,
    viewModel: FarmRecordsViewModel = viewModel()
) {
    val activeTab by viewModel.activeTab.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Farm Records") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = activeTab.ordinal
            ) {
                FarmRecordTab.values().forEach { tab ->
                    Tab(
                        selected = activeTab == tab,
                        onClick = { viewModel.setActiveTab(tab) },
                        text = {
                            Text(
                                text = tab.name.capitalize(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    )
                }
            }

            if (error != null) {
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                when (activeTab) {
                    FarmRecordTab.EXPENSES -> ExpensesTab(
                        expenses = viewModel.expenses.collectAsState().value,
                        onDeleteExpense = { viewModel.deleteExpense(expenseId = it.id) }
                    )
                    FarmRecordTab.HARVESTS -> HarvestsTab(
                        harvests = viewModel.harvests.collectAsState().value,
                        onDeleteHarvest = { viewModel.deleteHarvest(harvestId = it.id) }
                    )
                    FarmRecordTab.INVENTORY -> InventoryTab(
                        inventory = viewModel.inventory.collectAsState().value,
                        onDeleteInventoryItem = { viewModel.deleteInventoryItem(itemId = it.id) }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (showAddDialog) {
            when (activeTab) {
                FarmRecordTab.EXPENSES -> {
                    AddExpenseDialog(
                        onDismiss = { showAddDialog = false },
                        onAddExpense = { farmId, category, amount, description, date, photo ->
                            viewModel.addExpense(farmId, Category = ExpenseCategory.valueOf(category), amount, "", Dates = Date(), photo)
                            showAddDialog = false
                        }
                    )
                }
                FarmRecordTab.HARVESTS -> {
                    AddHarvestDialog(
                        onDismiss = { showAddDialog = false },
                        onAddHarvest = { farmId, cropName, quantity, unit, date, grade, notes, photos ->
                            viewModel.addHarvest(farmId, cropName, quantity, unit, harvestDate = Date(), grade, notes, photos)
                            showAddDialog = false
                        }
                    )
                }
                FarmRecordTab.INVENTORY -> {
                    AddInventoryDialog(
                        onDismiss = { showAddDialog = false },
                        onAddInventoryItem = { name, category, quantity, unit, purchaseDate, expiryDate, location, photo ->
                            viewModel.addInventoryItem(name, category, quantity, unit, purchaseDate = Date(), expiryDate = Date(), location, photo)
                            showAddDialog = false
                        }
                    )
                }
            }
        }
    }
}

// Helper extension to capitalize string
private fun String.capitalizeFirstLetter(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}