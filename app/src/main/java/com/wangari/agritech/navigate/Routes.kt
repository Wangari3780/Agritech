package com.wangari.agritech.navigate

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.wangari.agritech.data.AuthViewModel
import com.wangari.agritech.ui.screens.dashboard.DashboardScreen
import com.wangari.agritech.ui.screens.login.LoginScreen
import com.wangari.agritech.ui.screens.register.RegisterScreen
import com.wangari.agritech.ui.screens.splashscreen.SplashScreen

object AppDestinations {
    const val ROUTE_HOME = "DashboardScreen"
    const val ROUTE_LOGIN = "LoginScreen"
    const val ROUTE_REGISTER = "RegisterScreen"
    const val ONBOARDING_ROUTE = "onboarding"
    const val ROUTE_SPLASH = "splashscreen"
    const val FARM_RECORDS_ROUTE = "farm_records"
    const val MARKETPLACE_ROUTE = "marketplace"
    const val MARKET_PRICES_ROUTE = "market_prices"
    const val MY_PRODUCTS_ROUTE = "my_products"
    const val TRANSACTIONS_ROUTE = "transactions"
    const val SETTINGS_ROUTE = "settings"
    const val RAFIKI ="RafikiScreen"
}
