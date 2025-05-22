package com.wangari.agritech.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.wangari.agritech.data.DashboardViewModel
import com.wangari.agritech.data.UserViewModel
import com.wangari.agritech.models.MarketStat
import com.wangari.agritech.models.PriceTrend
import com.wangari.agritech.models.PriceTrendPeriod
import com.wangari.agritech.models.Transaction
import com.wangari.agritech.models.WeatherForecast
import com.wangari.agritech.navigate.AppDestinations
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigate: (String) -> Unit,
    navController: NavHostController,
    dashboardViewModel: DashboardViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val user by userViewModel.user.collectAsState()
    val weatherForecast by dashboardViewModel.weatherForecast.collectAsState()
    val recentTransactions by dashboardViewModel.recentTransactions.collectAsState()
    val marketStats by dashboardViewModel.marketStats.collectAsState()
    val priceTrends by dashboardViewModel.priceTrends.collectAsState()

    val isLoading by dashboardViewModel.isLoading.collectAsState()
    val error by dashboardViewModel.error.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "AgriTech",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Divider()

                user?.let { currentUser ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Profile",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = currentUser.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = currentUser.location,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    Divider()
                }

                val navigationItems = listOf(
                    NavigationItem(
                        title = "Dashboard",
                        icon = Icons.Default.Assessment,
                        destination = AppDestinations.ROUTE_HOME
                    ),
                    NavigationItem(
                        title = "Farm Records",
                        icon = Icons.Default.Agriculture,
                        destination = AppDestinations.FARM_RECORDS_ROUTE
                    ),
                    NavigationItem(
                        title = "Marketplace",
                        icon = Icons.Default.Storefront,
                        destination = AppDestinations.MARKETPLACE_ROUTE
                    ),
                    NavigationItem(
                        title = "Market Prices",
                        icon = Icons.Default.ShoppingCart,
                        destination = AppDestinations.MARKET_PRICES_ROUTE
                    ),
                    NavigationItem(
                        title = "My Products",
                        icon = Icons.Default.Inventory,
                        destination = AppDestinations.MY_PRODUCTS_ROUTE
                    ),
                    NavigationItem(
                        title = "Transactions",
                        icon = Icons.Default.Receipt,
                        destination = AppDestinations.TRANSACTIONS_ROUTE
                    )
                )

                navigationItems.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                        label = { Text(text = item.title) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                            onNavigate(item.destination)
                        },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                NavigationDrawerItem(
                    icon = { Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text(text = "Settings") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(AppDestinations.SETTINGS_ROUTE)
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Dashboard") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            },
//            floatingActionButton = {
//                androidx.compose.material3.FloatingActionButton(
//                    onClick = { onNavigate(AppDestinations.MY_PRODUCTS_ROUTE) },
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    contentColor = MaterialTheme.colorScheme.onPrimary
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Add,
//                        contentDescription = "Add Product"
//                    )
//                }
//            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                WeatherCard(weather = weatherForecast)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Market Overview",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (marketStats.isEmpty()) {
                            Text(
                                text = "Loading market stats...",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            marketStats.take(4).forEach { stat ->
                                MarketStatItem(stat = stat)
                                if (stat != marketStats.take(4).last()) {
                                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                                }
                            }
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Price Trends",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        var selectedPeriod by remember { mutableStateOf(PriceTrendPeriod.WEEKLY) }
                        PeriodSelector(
                            selectedPeriod = selectedPeriod,
                            onPeriodSelected = { period ->
                                selectedPeriod = period
                                dashboardViewModel.loadPriceTrends(period)
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        if (priceTrends.isEmpty()) {
                            Text(
                                text = "Loading price trends...",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            priceTrends.take(3).forEach { trend ->
                                PriceTrendItem(trend = trend)
                                if (trend != priceTrends.take(3).last()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Recent Transactions",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (recentTransactions.isEmpty()) {
                            Text(
                                text = "No recent transactions",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            recentTransactions.forEach { transaction ->
                                TransactionItem(transaction = transaction)
                                if (transaction != recentTransactions.last()) {
                                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun PeriodSelector(
    selectedPeriod: PriceTrendPeriod,
    onPeriodSelected: (PriceTrendPeriod) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        PeriodTab(
            title = "Weekly",
            selected = selectedPeriod == PriceTrendPeriod.WEEKLY,
            onClick = { onPeriodSelected(PriceTrendPeriod.WEEKLY) },
            modifier = Modifier.weight(1f)
        )

        PeriodTab(
            title = "Monthly",
            selected = selectedPeriod == PriceTrendPeriod.MONTHLY,
            onClick = { onPeriodSelected(PriceTrendPeriod.MONTHLY) },
            modifier = Modifier.weight(1f)
        )

        PeriodTab(
            title = "Yearly",
            selected = selectedPeriod == PriceTrendPeriod.YEARLY,
            onClick = { onPeriodSelected(PriceTrendPeriod.YEARLY) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun PeriodTab(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}
@Composable
fun WeatherCard(weather: WeatherForecast?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (weather == null) {
                Text(
                    text = "Loading weather data...",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Text(
                    text = "Weather in ${weather.location}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${weather.current.temperature}°C - ${weather.current.condition}",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = weather.farmingTip,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun MarketStatItem(stat: MarketStat) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stat.cropName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = stat.category,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = "KSh ${stat.currentPrice}/kg",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "${if (stat.changePercentage >= 0) "+" else ""}${stat.changePercentage}%",
            style = MaterialTheme.typography.bodyMedium,
            color = if (stat.changePercentage >= 0) Color(0xFF4CAF50) else Color(0xFFF44336),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun PriceTrendItem(trend: PriceTrend) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = trend.cropName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "${if (trend.changePercentage >= 0) "+" else ""}${trend.changePercentage}%",
                style = MaterialTheme.typography.bodyMedium,
                color = if (trend.changePercentage >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
            )
        }
        if (trend.prices.isNotEmpty()) {
            val lastPrice = trend.prices.last()
            Text(
                text = "Latest: KSh ${lastPrice.price}/kg (${lastPrice.date})",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = transaction.productName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "${transaction.quantity} ${transaction.unit} • ${transaction.status.name.toLowerCase().capitalize()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = "KSh ${transaction.amount}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val destination: String
)
fun String.toLowerCase(): String = this.lowercase()
fun String.capitalize(): String = this.replaceFirstChar { it.uppercase() }

