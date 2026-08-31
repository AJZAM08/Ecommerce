package com.ecommerce.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.domain.entity.Order
import com.ecommerce.domain.entity.OrderStatus
import com.ecommerce.domain.entity.Product
import com.ecommerce.domain.entity.ProductCategory
import com.ecommerce.domain.entity.ProductVariant
import com.ecommerce.domain.repository.OrderRepository
import com.ecommerce.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminUiState(
    val products: List<Product> = emptyList(),
    val isAddProductDialogOpen: Boolean = false,
    val isLoading: Boolean = false,
    val message: String? = null
)

class AdminViewModel(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    val orders: StateFlow<List<Order>> = orderRepository.getOrders().stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000), emptyList())
    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val list = productRepository.getProduct()
            _uiState.update { it.copy(products = list, isLoading = false) }
        }
    }

    fun setAddProductDialog(show: Boolean) {
        _uiState.update { it.copy(isAddProductDialogOpen = show) }
    }

    fun addProduct(
        name: String,
        desc: String,
        category: ProductCategory,
        benefits: String,
        composition: String,
        usage: String,
        imageUrl: String,
        size: String,
        price: Long,
        stock: Int
    ) {
        viewModelScope.launch {
            val newProduct = Product(
                id = 0L,
                name = name,
                description = desc,
                category = category,
                benefits = benefits,
                composition = composition,
                usage = usage,
                imageUrl = imageUrl.ifBlank { "https://images.unsplash.com/photo-1608571423902-eed4a5ad8108?q=80&w=800&auto=format&fit=crop" },
                variants = listOf(
                    ProductVariant(0L, 0L, size, price, stock)
                )
            )
            productRepository.addProduct(newProduct)
            loadProducts()
            _uiState.update { it.copy(isAddProductDialogOpen = false, message = "Produk berhasil ditambahkan!") }
        }
    }

    fun updateStock(variantId: Long, newStock: Int) {
        viewModelScope.launch {
            productRepository.updateProductStock(variantId, newStock)
            loadProducts()
        }
    }

    fun deleteProduct(productId: Long) {
        viewModelScope.launch {
            productRepository.deleteProduct(productId)
            loadProducts()
        }
    }

    fun updateOrderStatus(orderId: Long, newStatus: OrderStatus) {
        viewModelScope.launch {
            orderRepository.updateOrderStatus(orderId, newStatus)
        }
    }
}