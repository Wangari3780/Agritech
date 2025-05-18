package com.wangari.agritech.repositories

import com.wangari.agritech.models.Expense
import com.wangari.agritech.models.Harvest
import com.wangari.agritech.models.Inventory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FarmRecordsRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val expensesCollection = firestore.collection("expenses")
    private val harvestsCollection = firestore.collection("harvests")
    private val inventoryCollection = firestore.collection("inventory")

    suspend fun createExpense(expense: Expense): Result<Expense> {
        return try {
            val expenseWithId = if (expense.id.isEmpty()) {
                val docRef = expensesCollection.document()
                expense.copy(id = docRef.id)
            } else {
                expense
            }

            expensesCollection.document(expenseWithId.id).set(expenseWithId).await()
            Result.success(expenseWithId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getExpense(expenseId: String): Result<Expense> {
        return try {
            val expenseDoc = expensesCollection.document(expenseId).get().await()
            val expense = expenseDoc.toObject(Expense::class.java) ?: throw Exception("Expense not found")
            Result.success(expense)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getUserExpenses(userId: String, limit: Int? = null): Result<List<Expense>> {
        return try {
            var query = expensesCollection.whereEqualTo("userId", userId)
                .orderBy("date", Query.Direction.DESCENDING)

            limit?.let { query = query.limit(it.toLong()) }

            val querySnapshot = query.get().await()
            val expenses = querySnapshot.documents.mapNotNull { it.toObject(Expense::class.java) }
            Result.success(expenses)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getFarmExpenses(farmId: String, limit: Int? = null): Result<List<Expense>> {
        return try {
            var query = expensesCollection.whereEqualTo("farmId", farmId)
                .orderBy("date", Query.Direction.DESCENDING)

            limit?.let { query = query.limit(it.toLong()) }

            val querySnapshot = query.get().await()
            val expenses = querySnapshot.documents.mapNotNull { it.toObject(Expense::class.java) }
            Result.success(expenses)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateExpense(expense: Expense): Result<Expense> {
        return try {
            expensesCollection.document(expense.id).set(expense).await()
            Result.success(expense)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteExpense(expenseId: String): Result<Boolean> {
        return try {
            expensesCollection.document(expenseId).delete().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createHarvest(harvest: Harvest): Result<Harvest> {
        return try {
            val harvestWithId = if (harvest.id.isEmpty()) {
                val docRef = harvestsCollection.document()
                harvest.copy(id = docRef.id)
            } else {
                harvest
            }

            harvestsCollection.document(harvestWithId.id).set(harvestWithId).await()
            Result.success(harvestWithId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getHarvest(harvestId: String): Result<Harvest> {
        return try {
            val harvestDoc = harvestsCollection.document(harvestId).get().await()
            val harvest = harvestDoc.toObject(Harvest::class.java) ?: throw Exception("Harvest record not found")
            Result.success(harvest)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserHarvests(userId: String, limit: Int? = null): Result<List<Harvest>> {
        return try {
            var query = harvestsCollection.whereEqualTo("userId", userId)
                .orderBy("harvestDate", Query.Direction.DESCENDING)

            limit?.let { query = query.limit(it.toLong()) }

            val querySnapshot = query.get().await()
            val harvests = querySnapshot.documents.mapNotNull { it.toObject(Harvest::class.java) }
            Result.success(harvests)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFarmHarvests(farmId: String, limit: Int? = null): Result<List<Harvest>> {
        return try {
            var query = harvestsCollection.whereEqualTo("farmId", farmId)
                .orderBy("harvestDate", Query.Direction.DESCENDING)

            limit?.let { query = query.limit(it.toLong()) }

            val querySnapshot = query.get().await()
            val harvests = querySnapshot.documents.mapNotNull { it.toObject(Harvest::class.java) }
            Result.success(harvests)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateHarvest(harvest: Harvest): Result<Harvest> {
        return try {
            harvestsCollection.document(harvest.id).set(harvest).await()
            Result.success(harvest)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteHarvest(harvestId: String): Result<Boolean> {
        return try {
            harvestsCollection.document(harvestId).delete().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createInventoryItem(inventoryItem: Inventory): Result<Inventory> {
        return try {
            val itemWithId = if (inventoryItem.id.isEmpty()) {
                val docRef = inventoryCollection.document()
                inventoryItem.copy(id = docRef.id)
            } else {
                inventoryItem
            }

            inventoryCollection.document(itemWithId.id).set(itemWithId).await()
            Result.success(itemWithId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getInventoryItem(itemId: String): Result<Inventory> {
        return try {
            val itemDoc = inventoryCollection.document(itemId).get().await()
            val item = itemDoc.toObject(Inventory::class.java) ?: throw Exception("Inventory item not found")
            Result.success(item)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserInventory(userId: String, limit: Int? = null): Result<List<Inventory>> {
        return try {
            var query = inventoryCollection.whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)

            limit?.let { query = query.limit(it.toLong()) }

            val querySnapshot = query.get().await()
            val items = querySnapshot.documents.mapNotNull { it.toObject(Inventory::class.java) }
            Result.success(items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateInventoryItem(inventoryItem: Inventory): Result<Inventory> {
        return try {
            inventoryCollection.document(inventoryItem.id).set(inventoryItem).await()
            Result.success(inventoryItem)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteInventoryItem(itemId: String): Result<Boolean> {
        return try {
            inventoryCollection.document(itemId).delete().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}