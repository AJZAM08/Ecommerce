package com.ecommerce.ui.checkout

import com.ecommerce.domain.entity.CartItem
import com.ecommerce.domain.entity.Order

data class ShippingOption(
    val id: String,
    val name: String,
    val cost: Long,
    val estimatedDays: String
)

sealed interface CheckoutUiState {
    data class Content(
        val items: List<CartItem> = emptyList(),
        val customerName: String = "",
        val phone: String = "",
        val address: String = "",
        val selectedShipping: ShippingOption = shippingOptions.first(),
        val isSubmitting: Boolean = false,
        val errorMessage: String? = null,
        val createdOrder: Order? = null // Terisi jika pesanan berhasil dibuat
    ) : CheckoutUiState {
        val subtotal: Long = items.sumOf { it.price * it.quantity }
        val totalPayment: Long = subtotal + selectedShipping.cost
        val isFormValid: Boolean = customerName.isNotBlank() && phone.isNotBlank() && address.isNotBlank() && items.isNotEmpty()
    }
}

val shippingOptions = listOf(
    ShippingOption("reg", "Reguler (J&T / SiCepat)", 15000L, "2 - 3 Hari"),
    ShippingOption("exp", "Express (Next Day)", 25000L, "1 Hari"),
    ShippingOption("cargo", "Kargo Hemat", 35000L, "4 - 6 Hari")
)