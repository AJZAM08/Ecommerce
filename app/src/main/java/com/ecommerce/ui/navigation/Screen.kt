package com.ecommerce.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object Katalog : Screen("katalog")
    object DetailProduk : Screen("detail/{productId}") {
        fun createRoute(productId: Long) = "detail/$productId"
    }
    object Cart : Screen("cart")
    object Checkout : Screen("checkout")
    object Orders : Screen("orders")
    object OrderDetail : Screen("order_detail/{orderId}") {
        fun createRoute(orderId: Long) = "order_detail/$orderId"
    }
    object Account : Screen("account")
    object Login : Screen("login")
    object Register : Screen("register")
    object AdminDashboard : Screen("admin_dashboard")
    object Onboarding : Screen("onboarding")
}

enum class BottomNavItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    KATALOG("Katalog", Screen.Katalog.route, Icons.Filled.Home, Icons.Outlined.Home),
    CART("Keranjang", Screen.Cart.route, Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart),
    ORDERS("Pesanan Saya", Screen.Orders.route, Icons.Filled.ReceiptLong, Icons.Outlined.ReceiptLong),
    ACCOUNT("Akun", Screen.Account.route, Icons.Filled.Person, Icons.Outlined.Person),
}