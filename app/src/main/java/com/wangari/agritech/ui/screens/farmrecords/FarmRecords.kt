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
import com.wangari.agritech.data.FarmRecordTab
import com.wangari.agritech.data.FarmRecordsViewModel

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
            // Tab Row
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

            // Error message
            if (error != null) {
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Content
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Tab content
                when (activeTab) {
                    FarmRecordTab.EXPENSES -> ExpensesTab(
                        expenses = viewModel.expenses.collectAsState().value,
                        onDeleteExpense = { viewModel.deleteExpense(it) }
                    )
                    FarmRecordTab.HARVESTS -> HarvestsTab(
                        harvests = viewModel.harvests.collectAsState().value,
                        onDeleteHarvest = { viewModel.deleteHarvest(it) }
                    )
                    FarmRecordTab.INVENTORY -> InventoryTab(
                        inventory = viewModel.inventory.collectAsState().value,
                        onDeleteInventoryItem = { viewModel.deleteInventoryItem(it) }
                    )
                }
            }
        }

        // Add Dialog
        if (showAddDialog) {
            when (activeTab) {
                FarmRecordTab.EXPENSES -> {
                    AddExpenseDialog(
                        onDismiss = { showAddDialog = false },
                        onAddExpense = { farmId, category, amount, description, date, photo ->
                            viewModel.addExpense(farmId, category, amount, description, date, photo)
                            showAddDialog = false
                        }
                    )
                }
                FarmRecordTab.HARVESTS -> {
                    AddHarvestDialog(
                        onDismiss = { showAddDialog = false },
                        onAddHarvest = { farmId, cropName, quantity, unit, date, grade, notes, photos ->
                            viewModel.addHarvest(farmId, cropName, quantity, unit, date, grade, notes, photos)
                            showAddDialog = false
                        }
                    )
                }
                FarmRecordTab.INVENTORY -> {
                    AddInventoryDialog(
                        onDismiss = { showAddDialog = false },
                        onAddInventoryItem = { name, category, quantity, unit, purchaseDate, expiryDate, location, photo ->
                            viewModel.addInventoryItem(name, category, quantity, unit, purchaseDate, expiryDate, location, photo)
                            showAddDialog = false
                        }
                    )
                }
            }
        }
    }
}

// Helper extension to capitalize string
private fun String.capitalize(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}