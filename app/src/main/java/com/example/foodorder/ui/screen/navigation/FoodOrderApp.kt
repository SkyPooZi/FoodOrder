package com.example.foodorder.ui.screen.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.foodorder.ui.screen.CartScreen
import com.example.foodorder.ui.screen.CheckoutScreen
import com.example.foodorder.ui.screen.DetailScreen
import com.example.foodorder.ui.screen.FavoritesScreen
import com.example.foodorder.ui.screen.HomeScreen
import com.example.foodorder.ui.screen.OrdersScreen
import com.example.foodorder.ui.screen.ProfileScreen
import com.example.foodorder.ui.screen.ThemeScreen
import com.example.foodorder.viewmodel.FoodOrderViewModel
import kotlinx.coroutines.launch

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun FoodOrderApp(viewModel: FoodOrderViewModel) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val isTopLevel = topLevelDestinations.any { it.screen.route == currentRoute }

    val useRail = LocalConfiguration.current.screenWidthDp >= 600

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val allFoods by viewModel.foods.collectAsStateWithLifecycle()
    val filteredFoods by viewModel.filteredFoods.collectAsStateWithLifecycle()
    val favorites by viewModel.favoriteFoods.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    val category by viewModel.category.collectAsStateWithLifecycle()
    val cart by viewModel.cartState.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val addedFoodName by viewModel.addedFoodName.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val dynamicColor by viewModel.dynamicColor.collectAsStateWithLifecycle()

    fun navigateTopLevel(route: String) {
        if (route == currentRoute) return
        navController.navigate(route) {
            popUpTo(Screen.Home.route) { inclusive = false }
            launchSingleTop = true
        }
    }

    fun navigateTopLevelAndClear(route: String) {
        navController.navigate(route) {
            popUpTo(Screen.Home.route) { inclusive = false }
            launchSingleTop = true
        }
    }

    fun openDrawer() {
        scope.launch { drawerState.open() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = isTopLevel,
        drawerContent = {
            ModalDrawerSheet {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 28.dp, end = 12.dp, top = 20.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Restaurant,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "FoodOrder",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { scope.launch { drawerState.close() } }) {
                        Icon(Icons.Filled.Close, contentDescription = "Tutup menu")
                    }
                }
                topLevelDestinations.forEach { destination ->
                    val selected = currentRoute == destination.screen.route
                    NavigationDrawerItem(
                        label = { Text(destination.label) },
                        icon = {
                            Icon(
                                if (selected) destination.selectedIcon else destination.unselectedIcon,
                                contentDescription = null
                            )
                        },
                        selected = selected,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navigateTopLevel(destination.screen.route)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Row(Modifier.fillMaxSize()) {
            if (useRail && isTopLevel) {
                NavigationRail {
                    Spacer(Modifier.height(8.dp))
                    topLevelDestinations.forEach { destination ->
                        val selected = currentRoute == destination.screen.route
                        NavigationRailItem(
                            selected = selected,
                            onClick = { navigateTopLevel(destination.screen.route) },
                            icon = {
                                Icon(
                                    if (selected) destination.selectedIcon else destination.unselectedIcon,
                                    contentDescription = destination.label
                                )
                            },
                            label = { Text(destination.label) }
                        )
                    }
                }
            }

            Scaffold(
                modifier = Modifier.weight(1f),
                bottomBar = {
                    if (!useRail && isTopLevel) {
                        NavigationBar {
                            topLevelDestinations.forEach { destination ->
                                val selected = currentRoute == destination.screen.route
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { navigateTopLevel(destination.screen.route) },
                                    icon = {
                                        Icon(
                                            if (selected) destination.selectedIcon else destination.unselectedIcon,
                                            contentDescription = destination.label
                                        )
                                    },
                                    label = { Text(destination.label) }
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route,
                    modifier = Modifier.padding(innerPadding).consumeWindowInsets(innerPadding)
                ) {
                    composable(Screen.Home.route) {
                        HomeScreen(
                            foods = filteredFoods,
                            query = query,
                            selectedCategory = category,
                            cartCount = cart.itemCount,
                            onQueryChange = viewModel::onQueryChange,
                            onCategorySelected = viewModel::onCategorySelected,
                            onFoodClick = { navController.navigate(Screen.Detail.createRoute(it)) },
                            onMenuClick = ::openDrawer,
                            onCartClick = { navController.navigate(Screen.Cart.route) }
                        )
                    }

                    composable(
                        route = Screen.Detail.route,
                        arguments = listOf(navArgument(Screen.Detail.ARG_FOOD_ID) { type = NavType.IntType })
                    ) { entry ->
                        val foodId = entry.arguments?.getInt(Screen.Detail.ARG_FOOD_ID) ?: 0
                        val food = allFoods.find { it.id == foodId }
                        DetailScreen(
                            food = food,
                            addedFoodName = addedFoodName,
                            onBack = { navController.popBackStack() },
                            onToggleFavorite = { food?.let(viewModel::toggleFavorite) },
                            onAddToCart = { qty ->
                                food?.let { viewModel.addToCart(it, qty, showConfirmation = true) }
                            },
                            onDismissDialog = viewModel::dismissAddedDialog,
                            onViewCart = {
                                viewModel.dismissAddedDialog()
                                navController.navigate(Screen.Cart.route)
                            },
                            onContinueShopping = {
                                viewModel.dismissAddedDialog()
                                navController.popBackStack()
                            }
                        )
                    }

                    composable(Screen.Cart.route) {
                        CartScreen(
                            cart = cart,
                            onBack = { navController.popBackStack() },
                            onQuantityChange = viewModel::updateQuantity,
                            onRemove = viewModel::removeFromCart,
                            onUndoRemove = viewModel::restoreToCart,
                            onCheckout = { navController.navigate(Screen.Checkout.route) },
                            onStartShopping = { navigateTopLevelAndClear(Screen.Home.route) }
                        )
                    }

                    composable(Screen.Checkout.route) {
                        CheckoutScreen(
                            cart = cart,
                            onBack = { navController.popBackStack() },
                            onPlaceOrder = viewModel::placeOrder,
                            onOrderSuccess = { navigateTopLevelAndClear(Screen.Orders.route) }
                        )
                    }

                    composable(Screen.Orders.route) {
                        OrdersScreen(
                            orders = orders,
                            cartCount = cart.itemCount,
                            onMenuClick = ::openDrawer,
                            onCartClick = { navController.navigate(Screen.Cart.route) },
                            onStartShopping = { navigateTopLevel(Screen.Home.route) }
                        )
                    }

                    composable(Screen.Favorites.route) {
                        FavoritesScreen(
                            favorites = favorites,
                            cartCount = cart.itemCount,
                            onMenuClick = ::openDrawer,
                            onCartClick = { navController.navigate(Screen.Cart.route) },
                            onFoodClick = { navController.navigate(Screen.Detail.createRoute(it)) },
                            onToggleFavorite = viewModel::toggleFavorite,
                            onStartShopping = { navigateTopLevel(Screen.Home.route) }
                        )
                    }

                    composable(Screen.Profile.route) {
                        ProfileScreen(
                            cartCount = cart.itemCount,
                            onMenuClick = ::openDrawer,
                            onCartClick = { navController.navigate(Screen.Cart.route) },
                            onThemeClick = { navController.navigate(Screen.Theme.route) },
                            onOrdersClick = { navigateTopLevel(Screen.Orders.route) }
                        )
                    }

                    composable(Screen.Theme.route) {
                        ThemeScreen(
                            themeMode = themeMode,
                            dynamicColor = dynamicColor,
                            onThemeModeChange = viewModel::setThemeMode,
                            onDynamicColorChange = viewModel::setDynamicColor,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}