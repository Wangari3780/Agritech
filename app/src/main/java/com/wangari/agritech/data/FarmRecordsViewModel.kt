package com.wangari.agritech.data

import com.wangari.agritech.models.Expense
import com.wangari.agritech.models.ExpenseCategory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wangari.agritech.models.Harvest
import com.wangari.agritech.models.Inventory
import com.wangari.agritech.models.InventoryCategory
import com.wangari.agritech.repositories.FarmRecordsRepository
import com.wangari.agritech.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class FarmRecordsViewModel : ViewModel() {
    private val farmRecordsRepository = FarmRecordsRepository()
    private val userRepository = UserRepository()

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    private val _harvests = MutableStateFlow<List<Harvest>>(emptyList())
    val harvests: StateFlow<List<Harvest>> = _harvests.asStateFlow()

    private val _inventory = MutableStateFlow<List<Inventory>>(emptyList())
    val inventory: StateFlow<List<Inventory>> = _inventory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _activeTab = MutableStateFlow(FarmRecordTab.EXPENSES)
    val activeTab: StateFlow<FarmRecordTab> = _activeTab.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val currentUser = userRepository.getCurrentUser() ?: return

        viewModelScope.launch {
            _isLoading.value = true

            when (_activeTab.value) {
                FarmRecordTab.EXPENSES -> loadExpenses(currentUser.uid)
                FarmRecordTab.HARVESTS -> loadHarvests(currentUser.uid)
                FarmRecordTab.INVENTORY -> loadInventory(currentUser.uid)
            }

            _isLoading.value = false
        }
    }

    fun setActiveTab(tab: FarmRecordTab) {
        _activeTab.value = tab
        loadUserData()
    }

    private suspend fun loadExpenses(userId: String) {
        farmRecordsRepository.getUserExpenses(userId)
            .onSuccess { expensesList ->
                _expenses.value = expensesList
            }
            .onFailure { exception ->
                _error.value = "Failed to load expenses: ${exception.message}"
            }
    }

    private suspend fun loadHarvests(userId: String) {
        farmRecordsRepository.getUserHarvests(userId)
            .onSuccess { harvestsList ->
                _harvests.value = harvestsList
            }
            .onFailure { exception ->
                _error.value = "Failed to load harvests: ${exception.message}"
            }
    }

    private suspend fun loadInventory(userId: String) {
        farmRecordsRepository.getUserInventory(userId)
            .onSuccess { inventoryList ->
                _inventory.value = inventoryList
            }
            .onFailure { exception ->
                _error.value = "Failed to load inventory: ${exception.message}"
            }
    }

    fun addExpense(
        farmId: String,
        category: ExpenseCategory,
        amount: Double,
        description: String,
        date: Date,
        receiptPhoto: String? = null
    ) {
        val currentUser = userRepository.getCurrentUser() ?: return

        val expense = Expense(
            userId = currentUser.uid,
            farmId = farmId,
            category = category,
            amount = amount,
            description = description,
            date = date,
            receiptPhoto = receiptPhoto,
            createdAt = Date()
        )

        viewModelScope.launch {
            _isLoading.value = true

            farmRecordsRepository.createExpense(expense)
                .onSuccess {
                    loadExpenses(currentUser.uid)
                }
                .onFailure { exception ->
                    _error.value = "Failed to add expense: ${exception.message}"
                }

            _isLoading.value = false
        }
    }

    fun addHarvest(
        farmId: String,
        cropName: String,
        quantity: Double,
        unit: String,
        harvestDate: Date,
        qualityGrade: String? = null,
        notes: String? = null,
        harvestPhotos: List<String> = emptyList()
    ) {
        val currentUser = userRepository.getCurrentUser() ?: return

        val harvest = Harvest(
            userId = currentUser.uid,
            farmId = farmId,
            cropName = cropName,
            quantity = quantity,
            unit = unit,
            harvestDate = harvestDate,
            qualityGrade = qualityGrade,
            notes = notes,
            harvestPhotos = harvestPhotos,
            createdAt = Date()
        )

        viewModelScope.launch {
            _isLoading.value = true

            farmRecordsRepository.createHarvest(harvest)
                .onSuccess {
                    // Reload harvests to update the list
                    loadHarvests(currentUser.uid)
                }
                .onFailure { exception ->
                    _error.value = "Failed to add harvest: ${exception.message}"
                }

            _isLoading.value = false
        }
    }

    fun addInventoryItem(
        itemName: String,
        category: InventoryCategory,
        quantity: Double,
        unit: String,
        purchaseDate: Date? = null,
        expiryDate: Date? = null,
        storageLocation: String? = null,
        itemPhoto: String? = null
    ) {
        val currentUser = userRepository.getCurrentUser() ?: return

        val inventoryItem = Inventory(
            userId = currentUser.uid,
            itemName = itemName,
            category = category,
            quantity = quantity,
            unit = unit,
            purchaseDate = purchaseDate,
            expiryDate = expiryDate,
            storageLocation = storageLocation,
            itemPhoto = itemPhoto,
            createdAt = Date()
        )

        viewModelScope.launch {
            _isLoading.value = true

            farmRecordsRepository.createInventoryItem(inventoryItem)
                .onSuccess {
                    // Reload inventory to update the list
                    loadInventory(currentUser.uid)
                }
                .onFailure { exception ->
                    _error.value = "Failed to add inventory item: ${exception.message}"
                }

            _isLoading.value = false
        }
    }

    fun deleteExpense(expenseId: String) {
        val currentUser = userRepository.getCurrentUser() ?: return

        viewModelScope.launch {
            _isLoading.value = true

            farmRecordsRepository.deleteExpense(expenseId)
                .onSuccess {
                    // Reload expenses to update the list
                    loadExpenses(currentUser.uid)
                }
                .onFailure { exception ->
                    _error.value = "Failed to delete expense: ${exception.message}"
                }

            _isLoading.value = false
        }
    }

    fun deleteHarvest(harvestId: String) {
        val currentUser = userRepository.getCurrentUser() ?: return

        viewModelScope.launch {
            _isLoading.value = true

            farmRecordsRepository.deleteHarvest(harvestId)
                .onSuccess {
                    // Reload harvests to update the list
                    loadHarvests(currentUser.uid)
                }
                .onFailure { exception ->
                    _error.value = "Failed to delete harvest: ${exception.message}"
                }

            _isLoading.value = false
        }
    }

    fun deleteInventoryItem(itemId: String) {
        val currentUser = userRepository.getCurrentUser() ?: return

        viewModelScope.launch {
            _isLoading.value = true

            farmRecordsRepository.deleteInventoryItem(itemId)
                .onSuccess {
                    // Reload inventory to update the list
                    loadInventory(currentUser.uid)
                }
                .onFailure { exception ->
                    _error.value = "Failed to delete inventory item: ${exception.message}"
                }

            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}

enum class FarmRecordTab {
    EXPENSES,
    HARVESTS,
    INVENTORY
}