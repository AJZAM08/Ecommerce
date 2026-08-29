package com.ecommerce.ui.detail

import com.ecommerce.domain.entity.Product
import com.ecommerce.domain.entity.ProductVariant

sealed interface DetailProdukUiState {
    object Loading : DetailProdukUiState
    data class Success(
        val product: Product,
        val selectedVariant: ProductVariant,
        val quantity: Int = 1,
        val isAddedToCart: Boolean = false
    ) : DetailProdukUiState
    data class Error(val message: String) : DetailProdukUiState
}