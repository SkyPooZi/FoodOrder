package com.example.foodorder.ui.screen.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Orders : Screen("orders")
    data object Favorites : Screen("favorites")
    data object Profile : Screen("profile")
    data object Cart : Screen("cart")
    data object Checkout : Screen("checkout")
    data object Theme : Screen("theme")
    data object Detail : Screen("detail/{foodId}") {
        const val ARG_FOOD_ID = "foodId"
        fun createRoute(foodId: Int) = "detail/$foodId"
    }
}

data class TopLevelDestination(
    val screen: Screen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val topLevelDestinations = listOf(
    TopLevelDestination(Screen.Home, "Beranda", Icons.Filled.Home, Icons.Outlined.Home),
    TopLevelDestination(Screen.Orders, "Pesanan", Icons.Filled.ShoppingBag, Icons.Outlined.ShoppingBag),
    TopLevelDestination(Screen.Favorites, "Favorit", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
    TopLevelDestination(Screen.Profile, "Profil", Icons.Filled.Person, Icons.Outlined.Person)
)
