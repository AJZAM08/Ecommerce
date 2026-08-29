package com.ecommerce.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.domain.repository.CartRepository
import com.ecommerce.domain.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState.Content())
    val uiState: StateFlow<CheckoutUiState.Content> = _uiState.asStateFlow()

    init {
        loadCartItems()
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            val items = cartRepository.getCartItems().first()
            _uiState.update { it.copy(items = items) }
        }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(customerName = name, errorMessage = null) }
    }

    fun onPhoneChange(phone: String) {
        _uiState.update { it.copy(phone = phone, errorMessage = null) }
    }

    fun onAddressChange(address: String) {
        _uiState.update { it.copy(address = address, errorMessage = null) }
    }

    fun onShippingSelected(shipping: ShippingOption) {
        _uiState.update { it.copy(selectedShipping = shipping) }
    }

    fun submitOrder() {
        val state = _uiState.value
        if (!state.isFormValid) {
            _uiState.update { it.copy(errorMessage = "Mohon lengkapi seluruh formulir pengiriman.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }

            val result = orderRepository.createOrder(
                customerName = state.customerName,
                address = state.address,
                phone = state.phone,
                shippingCost = state.selectedShipping.cost,
                items = state.items
            )

            result.onSuccess { newOrder ->
                // Bersihkan keranjang belanja setelah checkout sukses
                cartRepository.clearCart()
                _uiState.update { it.copy(isSubmitting = false, createdOrder = newOrder) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = error.localizedMessage ?: "Gagal membuat pesanan."
                    )
                }
            }
        }
    }
}