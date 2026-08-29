package com.ecommerce.ui.orders.detail

import com.ecommerce.domain.entity.Order

sealed interface OrderDetailUiState {
    object Loading : OrderDetailUiState
    data class Success(val order: Order) : OrderDetailUiState
    data class Error(val message: String) : OrderDetailUiState
}