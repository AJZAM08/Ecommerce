package com.ecommerce.ui.cart

import com.ecommerce.domain.entity.CartItem

sealed interface CartUiState {
    object Loading : CartUiState
    data class Success(
        val items: List<CartItem>,
        val subtotal: Long = items.sumOf { it.price * it.quantity }
    ) : CartUiState
}