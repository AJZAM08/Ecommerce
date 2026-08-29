package com.ecommerce.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.domain.entity.ProductVariant
import com.ecommerce.domain.repository.CartRepository
import com.ecommerce.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailProductViewModel(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    private val productId: Long = checkNotNull(savedStateHandle["productId"])
    private val _uiState = MutableStateFlow<DetailProdukUiState>(DetailProdukUiState.Loading)
    val uiState: StateFlow<DetailProdukUiState> = _uiState.asStateFlow()

    init {
        loadProductDetail()
    }

    fun loadProductDetail() {
        viewModelScope.launch {
            _uiState.value = DetailProdukUiState.Loading
            try {
                val product = productRepository.getProductById(productId)
                if (product != null && product.variants.isNotEmpty()) {
                    _uiState.value = DetailProdukUiState.Success(
                        product = product,
                        selectedVariant = product.variants.first(),
                        quantity = 1
                    )
                } else {
                    _uiState.value = DetailProdukUiState.Error("Produk tidak ditemukan")
                }
            } catch (e: Exception) {
                _uiState.value = DetailProdukUiState.Error(e.localizedMessage ?: "Gagal memuat detail produk")
            }
        }
    }

    fun onVariantSelected(variant: ProductVariant) {
        _uiState.update { currentState ->
            if (currentState is DetailProdukUiState.Success) {
                currentState.copy(
                    selectedVariant = variant,
                    quantity = 1
                )
            } else currentState
        }
    }

    fun onQuantityIncrease() {
        _uiState.update { currentState ->
            if (currentState is DetailProdukUiState.Success) {
                val maxStock = currentState.selectedVariant.stock
                if (currentState.quantity < maxStock) {
                    currentState.copy(quantity = currentState.quantity + 1)
                } else currentState
            } else currentState
        }
    }

    fun onQuantityDecrease() {
        _uiState.update { currentState ->
            if (currentState is DetailProdukUiState.Success) {
                if (currentState.quantity > 1) {
                    currentState.copy(quantity = currentState.quantity - 1)
                } else currentState
            } else currentState
        }
    }
    fun addToCart(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is DetailProdukUiState.Success) {
                cartRepository.addToCart(
                    product = currentState.product,
                    variant = currentState.selectedVariant,
                    quantity = currentState.quantity
                )
                onSuccess()
            }
        }
    }
}