package com.wangari.agritech.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Expense(
    val id: String = "",
    val userId: String = "",
    val farmId: String = "",
    val Category: ExpenseCategory = ExpenseCategory.OTHER,
    val amount: Double = 0.0,
    val description: String = "",
    val Dates: Date = Date(),
    val receiptPhoto: String? = null,
    val createdAt: Date = Date()
) : Parcelable

enum class ExpenseCategory {
    SEEDS,
    FERTILIZER,
    PESTICIDES,
    LABOR,
    EQUIPMENT,
    FUEL,
    IRRIGATION,
    TRANSPORT,
    PACKAGING,
    OTHER
}

@Parcelize
data class Harvest(
    val id: String = "",
    val userId: String = "",
    val farmId: String = "",
    val cropName: String = "",
    val quantity: Double = 0.0,
    val unit: String = "",
    val harvestDate: Date = Date(),
    val qualityGrade: String? = null,
    val notes: String? = null,
    val harvestPhotos: List<String> = emptyList(),
    val createdAt: Date = Date()
) : Parcelable

@Parcelize
data class Inventory(
    val id: String = "",
    val userId: String = "",
    val itemName: String = "",
    val category: InventoryCategory = InventoryCategory.OTHER,
    val quantity: Double = 0.0,
    val unit: String = "",
    val purchaseDate: Date? = null,
    val expiryDate: Date? = null,
    val storageLocation: String? = null,
    val itemPhoto: String? = null,
    val createdAt: Date = Date()
) : Parcelable

enum class InventoryCategory {
    SEEDS,
    FERTILIZER,
    PESTICIDES,
    TOOLS,
    EQUIPMENT,
    HARVESTED_CROPS,
    LIVESTOCK_FEED,
    PACKAGING_MATERIALS,
    OTHER
}