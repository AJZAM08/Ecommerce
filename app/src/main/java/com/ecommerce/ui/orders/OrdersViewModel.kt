package com.ecommerce.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.domain.repository.OrderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class OrdersViewModel(
    orderRepository: OrderRepository
) : ViewModel() {

    val uiState: StateFlow<OrdersUiState> = orderRepository.getOrders()
        .map { orders -> OrdersUiState.Success(orders = orders) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = OrdersUiState.Loading
        )
}