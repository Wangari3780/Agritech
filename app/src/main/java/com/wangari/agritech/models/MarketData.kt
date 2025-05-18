package com.wangari.agritech.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Product(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val quantity: Double = 0.0,
    val unit: String = "",
    val location: String = "",
    val images: List<String> = emptyList(),
    val interestedBuyers: List<String> = emptyList(),
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
) : Parcelable

@Parcelize
data class Transaction(
    val id: String = "",
    val buyerId: String = "",
    val sellerId: String = "",
    val productId: String = "",
    val productName: String = "",
    val quantity: Double = 0.0,
    val unit: String = "",
    val amount: Double = 0.0,
    val status: TransactionStatus = TransactionStatus.PENDING,
    val paymentMethod: PaymentMethod = PaymentMethod.MPESA,
    val mpesaReference: String? = null,
    val timestamp: Date = Date()
) : Parcelable

enum class TransactionStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    CANCELLED,
    FAILED
}

enum class PaymentMethod {
    MPESA,
    CASH,
    BANK_TRANSFER
}

@Parcelize
data class MarketStat(
    val id: String = "",
    val cropName: String = "",
    val category: String = "",
    val currentPrice: Double = 0.0,
    val previousPrice: Double = 0.0,
    val changePercentage: Double = 0.0,
    val updatedAt: Date = Date()
) : Parcelable

@Parcelize
data class PriceTrend(
    val cropName: String = "",
    val changePercentage: Double = 0.0,
    val prices: List<PricePoint> = emptyList()
) : Parcelable

@Parcelize
data class PricePoint(
    val date: String = "",
    val price: Double = 0.0
) : Parcelable

enum class PriceTrendPeriod {
    WEEKLY,
    MONTHLY,
    YEARLY
}

@Parcelize
data class MarketTip(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val category: String = "",
    val imageUrl: String? = null,
    val publishedAt: Date = Date()
) : Parcelable

@Parcelize
data class MarketLocation(
    val id: String = "",
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = "",
    val marketType: String = "",
    val operatingHours: String? = null
) : Parcelable

@Parcelize
data class WeatherForecast(
    val location: String = "",
    val current: CurrentWeather = CurrentWeather(),
    val forecast: List<ForecastDay> = emptyList(),
    val farmingTip: String = ""
) : Parcelable

@Parcelize
data class CurrentWeather(
    val temperature: Double = 0.0,
    val condition: String = ""
) : Parcelable

@Parcelize
data class ForecastDay(
    val day: String = "",
    val temperature: Double = 0.0,
    val condition: String = ""
) : Parcelable