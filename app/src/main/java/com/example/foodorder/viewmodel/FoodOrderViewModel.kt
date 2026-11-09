package com.example.foodorder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.foodorder.FoodOrderApplication
import com.example.foodorder.data.local.entity.CartItemDetail
import com.example.foodorder.data.local.entity.FoodEntity
import com.example.foodorder.data.local.entity.OrderWithItems
import com.example.foodorder.data.repository.FoodRepository
import com.example.foodorder.data.repository.SettingsRepository
import com.example.foodorder.data.repository.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class FoodCategory(val label: String) {
    ALL("Semua"),
    MAKANAN("Makanan"),
    MINUMAN("Minuman"),
    CAMILAN("Camilan"),
    PROMO("Promo")
}

data class CartUiState(
    val items: List<CartItemDetail> = emptyList(),
    val subtotal: Int = 0,
    val shipping: Int = 0,
    val total: Int = 0
) {
    val itemCount: Int get() = items.sumOf { it.quantity }
}

class FoodOrderViewModel(
    private val repository: FoodRepository,
    private val settings: SettingsRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _category = MutableStateFlow(FoodCategory.ALL)
    val category: StateFlow<FoodCategory> = _category.asStateFlow()

    val foods: StateFlow<List<FoodEntity>> = repository.foods
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val filteredFoods: StateFlow<List<FoodEntity>> =
        combine(foods, _query, _category) { list, query, category ->
            val keyword = query.trim()
            list.filter { food ->
                val matchCategory = when (category) {
                    FoodCategory.ALL -> true
                    FoodCategory.PROMO -> food.isPromo
                    else -> food.category == category.label
                }
                val matchQuery = keyword.isBlank() || food.name.contains(keyword, ignoreCase = true)
                matchCategory && matchQuery
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val favoriteFoods: StateFlow<List<FoodEntity>> = foods
        .map { list -> list.filter { it.isFavorite } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val cartState: StateFlow<CartUiState> = repository.cartItems
        .map { items ->
            val subtotal = items.sumOf { it.price * it.quantity }
            val shipping = if (items.isEmpty()) 0 else FoodRepository.SHIPPING_FEE
            CartUiState(items, subtotal, shipping, subtotal + shipping)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CartUiState())

    private val _addedFoodName = MutableStateFlow<String?>(null)
    val addedFoodName: StateFlow<String?> = _addedFoodName.asStateFlow()

    val orders: StateFlow<List<OrderWithItems>> = repository.orders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val themeMode: StateFlow<ThemeMode> = settings.themeMode
    val dynamicColor: StateFlow<Boolean> = settings.dynamicColor

    init {
        viewModelScope.launch { repository.seedIfEmpty() }
    }

    fun onQueryChange(value: String) {
        _query.value = value
    }

    fun onCategorySelected(category: FoodCategory) {
        _category.value = category
    }

    fun toggleFavorite(food: FoodEntity) {
        viewModelScope.launch { repository.toggleFavorite(food) }
    }

    fun addToCart(food: FoodEntity, quantity: Int, showConfirmation: Boolean) {
        viewModelScope.launch {
            repository.addToCart(food.id, quantity)
            if (showConfirmation) _addedFoodName.value = food.name
        }
    }

    fun dismissAddedDialog() {
        _addedFoodName.value = null
    }

    fun updateQuantity(foodId: Int, quantity: Int) {
        viewModelScope.launch { repository.updateQuantity(foodId, quantity) }
    }

    fun removeFromCart(foodId: Int) {
        viewModelScope.launch { repository.removeFromCart(foodId) }
    }

    fun restoreToCart(foodId: Int, quantity: Int) {
        viewModelScope.launch { repository.addToCart(foodId, quantity) }
    }

    fun placeOrder(address: String, paymentMethod: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (repository.placeOrder(address, paymentMethod)) onSuccess()
        }
    }

    fun setThemeMode(mode: ThemeMode) = settings.setThemeMode(mode)

    fun setDynamicColor(enabled: Boolean) = settings.setDynamicColor(enabled)

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as FoodOrderApplication
                FoodOrderViewModel(app.repository, app.settingsRepository)
            }
        }
    }
}
