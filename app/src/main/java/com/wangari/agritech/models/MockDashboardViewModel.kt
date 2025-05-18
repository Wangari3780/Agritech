package com.wangari.agritech.models

import androidx.compose.material3.MaterialTheme
import com.wangari.agritech.ui.screens.dashboard.DashboardScreen

package com.wangari.agritech.ui.screens.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.wangari.agritech.data.DashboardViewModel
import com.wangari.agritech.data.UserViewModel
import com.wangari.agritech.models.MarketStat
import com.wangari.agritech.models.PriceTrend
import com.wangari.agritech.models.PriceTrendPeriod
import com.wangari.agritech.models.Transaction
import com.wangari.agritech.models.TransactionStatus
import com.wangari.agritech.models.User
import com.wangari.agritech.models.WeatherForecast
import com.wangari.agritech.models.CurrentWeather
import com.wangari.agritech.models.WeatherForecast

import androidx.lifecycle.ViewModel // Import ViewModel if not already

class MockDashboardViewModel : ViewModel() {
    private val _weatherForecast = MutableStateFlow(
        WeatherForecast(
            location = "Nairobi",
            current = CurrentWeather(
                temperature = 25.0,
                condition = "SUNNY",

            ),
            farmingTip = "It's sunny, good for drying crops. Ensure adequate irrigation for young plants."
        )
    )
    val weatherForecastt: StateFlow<WeatherForecast?> = _weatherForecast.asStateFlow()

    private val _recentTransactions = MutableStateFlow(
        listOf(
            Transaction(
                id = "1",
                buyerId = "buyer123",
                sellerId = "seller456",
                productId = "product789",
                productName = "Maize (20 bags)",
                amount = 45000.0,
                timestamp = "2023-05-07",
                status = TransactionStatus.COMPLETED,
                quantity = 20.0,
                unit = "bags",
                paymentMethod = PaymentMethod.CASH

            ),
            Transaction(
                id = "2",
                productName = "Fertilizer (50kg)",
                amount = 3500.0,
                timestamp = "2024-05-08",
                status = TransactionStatus.PENDING,
                quantity = 50.0,
                unit = "kg",

            )
        )
    )
    val recentTransactions: StateFlow<List<Transaction>> = _recentTransactions.asStateFlow()

    private val _marketStats = MutableStateFlow(
        listOf(
            MarketStat(
                cropName = "Maize",
                category = "Cereals",
                currentPrice = 2800.0,
                changePercentage = 2.5
            ),
            MarketStat(
                cropName = "Beans",
                category = "Legumes",
                currentPrice = 3500.0,
                changePercentage = -1.2
            ),
            MarketStat(
                cropName = "Potatoes",
                category = "Vegetables",
                currentPrice = 2200.0,
                changePercentage = 0.8
            ),
            MarketStat(
                cropName = "Tomatoes",
                category = "Vegetables",
                currentPrice = 50.0,
                changePercentage = 5.0
            )
        )
    )
    val marketStats: StateFlow<List<MarketStat>> = _marketStats.asStateFlow()

    private val _priceTrends = MutableStateFlow(
        listOf(
            PriceTrend(
                cropName = "Maize",
                changePercentage = 3.0,
                prices = listOf(
                    PricePoint(date = "Mon", price = 2700.0),
                    PricePoint(date = "Tue", price = 2750.0),
                    PricePoint(date = "Wed", price = 2800.0)
                )
            ),
            PriceTrend(
                cropName = "Cabbage",
                changePercentage = -0.5,
                prices = listOf(
                    PricePoint(date = "Mon", price = 15.0),
                    PricePoint(date = "Tue", price = 14.5),
                    PricePoint(date = "Wed", price = 14.8)
                )
            )
        )
    )
    val priceTrends: StateFlow<List<PriceTrend>> = _priceTrends.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadPriceTrends(period: PriceTrendPeriod) {
        // Mock loading logic for preview
        _isLoading.value = true
        _priceTrends.value = emptyList() // Clear previous trends for effect
        _error.value = null
        // Simulate network delay
        // In a real ViewModel, you'd fetch data here
        // For preview, we just update with some mock data
        when (period) {
            PriceTrendPeriod.WEEKLY -> {
                _priceTrends.value = listOf(
                    PriceTrend(
                        cropName = "Maize",
                        changePercentage = 3.0,
                        prices = listOf(
                            PricePoint(date = "Mon", price = 2700.0),
                            PricePoint(date = "Tue", price = 2750.0),
                            PricePoint(date = "Wed", price = 2800.0)
                        )
                    ),
                    PriceTrend(
                        cropName = "Cabbage",
                        changePercentage = -0.5,
                        prices = listOf(
                            PricePoint(date = "Mon", price = 15.0),
                            PricePoint(date = "Tue", price = 14.5),
                            PricePoint(date = "Wed", price = 14.8)
                        )
                    )
                )
            }
            PriceTrendPeriod.MONTHLY -> {
                _priceTrends.value = listOf(
                    PriceTrend(
                        cropName = "Wheat",
                        changePercentage = 7.0,
                        prices = listOf(
                            PricePoint(date = "Week 1", price = 4000.0),
                            PricePoint(date = "Week 2", price = 4100.0),
                            PricePoint(date = "Week 3", price = 4250.0)
                        )
                    )
                )
            }
            PriceTrendPeriod.YEARLY -> {
                _priceTrends.value = listOf(
                    PriceTrend(
                        cropName = "Avocado",
                        changePercentage = 15.0,
                        prices = listOf(
                            PricePoint(date = "Q1", price = 100.0),
                            PricePoint(date = "Q2", price = 110.0),
                            PricePoint(date = "Q3", price = 115.0)
                        )
                    )
                )
            }
        }
        _isLoading.value = false
    }
}

class MockUserViewModel : ViewModel() {
    private val _user = MutableStateFlow(
        User(
            uid = "mock_user_id",
            name = "Wangari Farmer",
            email = "wangari@agritech.com",
            location = "Nairobi",
            phone = "+254712345678"
        )
    )
    val user: StateFlow<User?> = _user.asStateFlow()
}

// --- Preview Composable ---

@Preview(showBackground = true, widthDp = 360, heightDp = 1200) // Adjust height as needed for scrolling content
@Composable
fun DashboardScreenPreview() {
    MaterialTheme { // Wrap in MaterialTheme to apply design system
        DashboardScreen(
            onNavigate = { destination ->
                println("Navigating to: $destination") // Log navigation attempts in preview
            },
            dashboardViewModel = MockDashboardViewModel(), // Provide mock ViewModel
            userViewModel = MockUserViewModel() // Provide mock ViewModel
        )
    }
}