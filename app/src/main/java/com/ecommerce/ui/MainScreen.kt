package com.ecommerce.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ecommerce.EcommerceApplication
import com.ecommerce.ui.navigation.BottomNavItem
import com.ecommerce.ui.navigation.EcommerceNavHost

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController()
) {
    // Ambil cartRepository dari Application Container
    val context = LocalContext.current
    val appContainer = (context.applicationContext as EcommerceApplication).container
    val cartItems by appContainer.cartRepository.getCartItems().collectAsState(initial = emptyList())
    val totalCartCount = cartItems.sumOf { it.quantity }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    // Tampilkan bottom bar hanya pada 4 tab utama
    val showBottomBar = currentRoute in BottomNavItem.values().map { it.route }
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    BottomNavItem.values().forEach { item ->
                        val selected = currentRoute == item.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            label = { Text(item.title) },
                            icon = {
                                if (item == BottomNavItem.CART && totalCartCount > 0) {
                                    BadgedBox(
                                        badge = {
                                            Badge { Text("$totalCartCount") }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                            contentDescription = item.title
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        EcommerceNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}