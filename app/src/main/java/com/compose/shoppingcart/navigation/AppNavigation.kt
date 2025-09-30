package com.compose.shoppingcart.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.compose.shoppingcart.ui.cart.CartScreen
import com.compose.shoppingcart.ui.detail.ProductDetailScreen
import com.compose.shoppingcart.ui.splash.SplashScreen
import com.compose.shoppingcart.ui.login.LoginScreen
import com.compose.shoppingcart.ui.home.HomeScreen
import com.compose.shoppingcart.ui.profile.ProfileScreen
import com.compose.shoppingcart.ui.register.RegisterScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Login : Screen("login")
    object Register : Screen("register")
    object Detail : Screen("detail/{productId}") {
        fun createRoute(productId: Int) = "detail/$productId"
    }

    object Cart : Screen("cart")
    object Profile : Screen("profile")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onAuthSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0)
                    }
                },
                onRegister = {
                    navController.navigate(Screen.Register.route) {
                        popUpTo(Screen.Register.route) {
                            inclusive = false
                        }
                    }
                })
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegister = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                },
                onDetail = { productId ->
                    navController.navigate(Screen.Detail.createRoute(productId))
                },
                onShoppingCart = {
                    navController.navigate(Screen.Cart.route)
                },
                onLogin = {
                    navController.navigate(Screen.Login.route)
                },
                onProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }



        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            ProductDetailScreen(
                productId = productId,
                onBack = { navController.popBackStack() },
                onCart = { navController.navigate(Screen.Home.route) }
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(
                onCheckout = {
                    // Lógica de pago
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

    }
}