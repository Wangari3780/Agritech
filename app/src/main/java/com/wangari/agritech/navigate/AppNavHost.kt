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
import com.wangari.agritech.repositories.UserRepository
import com.wangari.agritech.ui.screens.dashboard.DashboardScreen
import com.wangari.agritech.ui.screens.login.login
import com.wangari.agritech.ui.screens.onboardingscreen.OnboardingScreen
import com.wangari.agritech.ui.screens.register.RegisterScreen


@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppDestinations.ROUTE_LOGIN,
    authViewModel: AuthViewModel = viewModel()
) {
    val userRepository = remember { UserRepository() }
    val currentUser by authViewModel.currentUser.collectAsState()

    // If user is already logged in, navigate to dashboard
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
                    navController.navigate(AppDestinations.ROUTE_LOGIN)
                }
            )
        }

        composable(AppDestinations.ROUTE_LOGIN) {
            login(
                onLoginSuccess = {
                    navController.navigate(AppDestinations.ROUTE_HOME) {
                        popUpTo(AppDestinations.ROUTE_LOGIN) { inclusive = true }
                    }
                },
                onNavigateToSignup = {
                    navController.navigate(AppDestinations.ROUTE_REGISTER)
                },
                viewModel = authViewModel
            )
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
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }
    }
    }