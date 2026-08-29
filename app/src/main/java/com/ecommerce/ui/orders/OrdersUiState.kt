package com.ecommerce.ui.orders

import com.ecommerce.domain.entity.Order
sealed interface OrdersUiState {
    object Loading : OrdersUiState
    data class Success(val orders: List<Order>) : OrdersUiState
}