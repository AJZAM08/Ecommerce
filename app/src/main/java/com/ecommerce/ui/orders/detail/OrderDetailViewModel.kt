package com.ecommerce.ui.orders.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.domain.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val orderId: Long = checkNotNull(savedStateHandle["orderId"])

    private val _uiState = MutableStateFlow<OrderDetailUiState>(OrderDetailUiState.Loading)
    val uiState: StateFlow<OrderDetailUiState> = _uiState.asStateFlow()

    init {
        loadOrderDetail()
    }

    fun loadOrderDetail() {
        viewModelScope.launch {
            _uiState.value = OrderDetailUiState.Loading
            try {
                val order = orderRepository.getOrderById(orderId)
                if (order != null) {
                    _uiState.value = OrderDetailUiState.Success(order)
                } else {
                    _uiState.value = OrderDetailUiState.Error("Pesanan tidak ditemukan.")
                }
            } catch (e: Exception) {
                _uiState.value = OrderDetailUiState.Error(e.localizedMessage ?: "Gagal memuat pesanan.")
            }
        }
    }
}