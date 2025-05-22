package com.wangari.agritech.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wangari.agritech.models.MarketStat
import com.wangari.agritech.models.PriceTrend
import com.wangari.agritech.models.PriceTrendPeriod
import com.wangari.agritech.models.Transaction
import com.wangari.agritech.models.WeatherForecast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val _weatherForecast = MutableStateFlow<WeatherForecast?>(null)
    val weatherForecast: StateFlow<WeatherForecast?> = _weatherForecast

    private val _recentTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val recentTransactions: StateFlow<List<Transaction>> = _recentTransactions

    private val _marketStats = MutableStateFlow<List<MarketStat>>(emptyList())
    val marketStats: StateFlow<List<MarketStat>> = _marketStats

    private val _priceTrends = MutableStateFlow<List<PriceTrend>>(emptyList())
    val priceTrends: StateFlow<List<PriceTrend>> = _priceTrends

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        // Initialize data loading when the ViewModel is created
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null // Clear previous errors
            try {

                _weatherForecast.value = WeatherForecast(
                    location = "Nairobi",
                    current = com.wangari.agritech.models.CurrentWeather(temperature = 25.0, condition = "Sunny"),
                    farmingTip = "Ensure adequate irrigation for your crops during sunny periods."
                )
                _recentTransactions.value = listOf(
                    Transaction(productName = "Tomatoes", quantity = 50.0, unit = "kg", amount = 2500.0),
                    Transaction(productName = "Maize", quantity = 100.0, unit = "kg", amount = 3000.0, status = com.wangari.agritech.models.TransactionStatus.COMPLETED)
                )
                _marketStats.value = listOf(
                    MarketStat(cropName = "Maize", category = "Grains", currentPrice = 35.0, changePercentage = 2.5),
                    MarketStat(cropName = "Cabbage", category = "Vegetables", currentPrice = 40.0, changePercentage = -1.0)
                )
                _priceTrends.value = listOf(
                    PriceTrend(cropName = "Maize", changePercentage = 5.0, prices = listOf(com.wangari.agritech.models.PricePoint("Mon", 30.0), com.wangari.agritech.models.PricePoint("Fri", 35.0))),
                    PriceTrend(cropName = "Beans", changePercentage = -2.0, prices = listOf(com.wangari.agritech.models.PricePoint("Mon", 100.0), com.wangari.agritech.models.PricePoint("Fri", 98.0)))
                )

            } catch (e: Exception) {
                _error.value = "Failed to load dashboard data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadPriceTrends(period: PriceTrendPeriod) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val newTrends = when (period) {
                    PriceTrendPeriod.WEEKLY -> listOf(
                        PriceTrend(cropName = "Weekly Crop A", changePercentage = 1.2, prices = listOf(com.wangari.agritech.models.PricePoint("Day 1", 10.0), com.wangari.agritech.models.PricePoint("Day 7", 11.2))),
                        PriceTrend(cropName = "Weekly Crop B", changePercentage = -0.5, prices = listOf(com.wangari.agritech.models.PricePoint("Day 1", 20.0), com.wangari.agritech.models.PricePoint("Day 7", 19.5)))
                    )
                    PriceTrendPeriod.MONTHLY -> listOf(
                        PriceTrend(cropName = "Monthly Crop X", changePercentage = 8.0, prices = listOf(com.wangari.agritech.models.PricePoint("Week 1", 50.0), com.wangari.agritech.models.PricePoint("Week 4", 58.0)))
                    )
                    PriceTrendPeriod.YEARLY -> listOf(
                        PriceTrend(cropName = "Yearly Crop Y", changePercentage = 15.0, prices = listOf(com.wangari.agritech.models.PricePoint("Jan", 100.0), com.wangari.agritech.models.PricePoint("Dec", 115.0)))
                    )
                }
                _priceTrends.value = newTrends
            } catch (e: Exception) {
                _error.value = "Failed to load price trends: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}