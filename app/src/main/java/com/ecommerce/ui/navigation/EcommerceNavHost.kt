package com.ecommerce.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ecommerce.ui.account.AccountScreen
import com.ecommerce.ui.account.AccountViewModel
import com.ecommerce.ui.admin.AdminDashboardScreen
import com.ecommerce.ui.admin.AdminViewModel
import com.ecommerce.ui.auth.AuthViewModel
import com.ecommerce.ui.auth.LoginScreen
import com.ecommerce.ui.auth.RegisterScreen
import com.ecommerce.ui.cart.CartScreen
import com.ecommerce.ui.cart.CartViewModel
import com.ecommerce.ui.checkout.CheckoutScreen
import com.ecommerce.ui.checkout.CheckoutViewModel
import com.ecommerce.ui.detail.DetailProductViewModel
import com.ecommerce.ui.detail.DetailProdukScreen
import com.ecommerce.ui.katalog.KatalogScreen
import com.ecommerce.ui.katalog.KatalogViewModel
import com.ecommerce.ui.onboarding.OnboardingScreen
import com.ecommerce.ui.orders.OrdersScreen
import com.ecommerce.ui.orders.OrdersViewModel
import com.ecommerce.ui.orders.detail.OrderDetailScreen
import com.ecommerce.ui.orders.detail.OrderDetailViewModel
import com.ecommerce.ui.provider.AppViewModelProvider

@Composable
fun EcommerceNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Katalog.route,
        modifier = modifier
    ) {
        composable(Screen.Katalog.route) {
            val katalogViewModel: KatalogViewModel = viewModel(factory = AppViewModelProvider.Factory)
            KatalogScreen(
                viewModel = katalogViewModel,
                onProductClick = { productId ->
                    navController.navigate(Screen.DetailProduk.createRoute(productId))
                }
            )
        }
        composable(Screen.DetailProduk.route, listOf(navArgument("productId") { type = NavType.LongType })) {
            val detailViewModel: DetailProductViewModel = viewModel(factory = AppViewModelProvider.Factory)
            DetailProdukScreen(
                viewModel = detailViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCart = {
                    navController.navigate(Screen.Cart.route) {
                        popUpTo(Screen.Katalog.route)
                    }
                }
            )
        }
        composable(Screen.Cart.route) {
            val cartViewModel: CartViewModel = viewModel(factory = AppViewModelProvider.Factory)
            CartScreen(
                viewModel = cartViewModel,
                onNavigateToKatalog = {
                    navController.navigate(Screen.Katalog.route) {
                        popUpTo(Screen.Katalog.route) { inclusive = true }
                    }
                },
                onNavigateToCheckout = {
                    navController.navigate(Screen.Checkout.route)
                }
            )
        }
        composable(Screen.Checkout.route) {
            val checkoutViewModel: CheckoutViewModel = viewModel(factory = AppViewModelProvider.Factory)
            CheckoutScreen(
                viewModel = checkoutViewModel,
                onNavigateBack = { navController.popBackStack() },
                onOrderSuccess = { orderId ->
                    navController.navigate(Screen.Orders.route) {
                        popUpTo(Screen.Katalog.route)
                    }
                }
            )
        }
        composable(Screen.Orders.route) {
            val ordersViewModel: OrdersViewModel = viewModel(factory = AppViewModelProvider.Factory)
            OrdersScreen(
                viewModel = ordersViewModel,
                onOrderClick = { orderId ->
                    navController.navigate(Screen.OrderDetail.createRoute(orderId))
                },
                onNavigateToKatalog = {
                    navController.navigate(Screen.Katalog.route) {
                        popUpTo(Screen.Katalog.route) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Screen.OrderDetail.route,
            arguments = listOf(navArgument("orderId") { type = NavType.LongType })
        ) {
            val orderDetailViewModel: OrderDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
            OrderDetailScreen(
                viewModel = orderDetailViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Account.route) {
            val accountViewModel: AccountViewModel = viewModel(factory = AppViewModelProvider.Factory)
            AccountScreen(
                viewModel = accountViewModel,
                onNavigateToOrders = { navController.navigate(Screen.Orders.route) },
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onNavigateToAdmin = { navController.navigate(Screen.AdminDashboard.route) }
            )
        }

        composable(Screen.Login.route) {
            val authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.popBackStack()
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onContinueAsGuest = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Register.route) {
            val authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.popBackStack(Screen.Login.route, inclusive = true)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.AdminDashboard.route) {
            val adminViewModel: AdminViewModel = viewModel(factory = AppViewModelProvider.Factory)
            AdminDashboardScreen(
                viewModel = adminViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinished = {
                    navController.navigate(Screen.Katalog.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
    }
}