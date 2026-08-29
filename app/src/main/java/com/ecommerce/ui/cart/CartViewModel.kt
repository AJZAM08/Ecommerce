package com.ecommerce.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.domain.repository.CartRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {

    // Mengamati cart repository secara real-time via Flow
    val uiState: StateFlow<CartUiState> = cartRepository.getCartItems()
        .map { items -> CartUiState.Success(items = items) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CartUiState.Loading
        )

    fun onQuantityChange(cartItemId: Long, newQuantity: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(cartItemId, newQuantity)
        }
    }

    fun onRemoveItem(cartItemId: Long) {
        viewModelScope.launch {
            cartRepository.removeFromCart(cartItemId)
        }
    }

    fun onClearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }
}