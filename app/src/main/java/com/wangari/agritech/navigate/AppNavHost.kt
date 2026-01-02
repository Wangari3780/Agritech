package com.wangari.agritech.navigate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wangari.agritech.data.AuthViewModel
import com.wangari.agritech.navigate.AppDestinations.ROUTE_HOME
import com.wangari.agritech.repositories.UserRepository
import com.wangari.agritech.ui.screens.dashboard.DashboardScreen
import com.wangari.agritech.ui.screens.farmrecords.FarmRecordsScreen
import com.wangari.agritech.ui.screens.login.LoginScreen
import com.wangari.agritech.ui.screens.onboardingscreen.OnboardingScreen
import com.wangari.agritech.ui.screens.rafiki.GeminiNanoClient
import com.wangari.agritech.ui.screens.rafiki.RafikiScreen
import com.wangari.agritech.ui.screens.register.RegisterScreen
import com.wangari.agritech.ui.screens.splashscreen.SplashScreen


@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppDestinations.ROUTE_SPLASH,
    authViewModel: AuthViewModel = viewModel()
) {
    val userRepository = remember { UserRepository() }
    val currentUser by authViewModel.currentUser.collectAsState()

    LaunchedEffect(currentUser) {
        currentUser?.let {
            navController.navigate(AppDestinations.ROUTE_HOME) {
                popUpTo(AppDestinations.ROUTE_LOGIN) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(AppDestinations.ONBOARDING_ROUTE) {
            OnboardingScreen(
                onGetStarted = {
                    navController.navigate(AppDestinations.ROUTE_HOME)
                }
            )
        }

        composable(AppDestinations.ROUTE_LOGIN) {
            LoginScreen(navController)
//                onLoginSuccess = {
//                    navController.navigate(AppDestinations.ROUTE_HOME) {
//                        popUpTo(AppDestinations.ROUTE_LOGIN) { inclusive = true }
//                    }
//                },
//                onNavigateToSignup = {
//                    navController.navigate(AppDestinations.ROUTE_REGISTER)
//                }
//                viewModel = authViewModel
//            )
        }

        composable(AppDestinations.ROUTE_REGISTER) {
            RegisterScreen(
                onSignupSuccess = {
                    navController.navigate(AppDestinations.ROUTE_HOME) {
                        popUpTo(AppDestinations.ROUTE_REGISTER) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                viewModel = authViewModel
            )
        }

        composable(AppDestinations.ROUTE_HOME) {
            DashboardScreen(
                navController = navController,
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }
        composable(AppDestinations.FARM_RECORDS_ROUTE) {
            FarmRecordsScreen(navController::navigateUp)
        }
        composable(AppDestinations.RAFIKI) {
            RafikiScreen(onNavigateBack = { navController.navigateUp() })
        }
        composable(AppDestinations.ROUTE_SPLASH) {
            SplashScreen(navController=navController, context = navController.context)
        }
    }
    }