package com.ecommerce.ui.katalog

import com.ecommerce.domain.entity.Product
import com.ecommerce.domain.entity.ProductCategory

sealed interface KatalogUiState {
    object Loading : KatalogUiState
    data class Success(val products: List<Product>) : KatalogUiState
    data class Error(val errorMessage: String) : KatalogUiState
}

data class KatalogFilterState(
    val searchQuery: String = "",
    val selectedCategory: ProductCategory? = null
)