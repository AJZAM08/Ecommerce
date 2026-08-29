package com.ecommerce.ui.katalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.domain.entity.ProductCategory
import com.ecommerce.domain.repository.ProductRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class KatalogViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<KatalogUiState>(KatalogUiState.Loading)
    val uiState: StateFlow<KatalogUiState> = _uiState.asStateFlow()

    private val _filterState = MutableStateFlow(KatalogFilterState())
    val filterState: StateFlow<KatalogFilterState> = _filterState.asStateFlow()

    private var searchJob: Job? = null
    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = KatalogUiState.Loading
            try {
                fetchFilteredProducts()
            } catch (e: Exception) {
                _uiState.value = KatalogUiState.Error(e.localizedMessage ?: "Gagal memuat produk")
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _filterState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            fetchFilteredProducts()
        }
    }

    fun onCategorySelect(category: ProductCategory?) {
        _filterState.update { it.copy(selectedCategory = category) }
        viewModelScope.launch {
            fetchFilteredProducts()
        }
    }

    private suspend fun fetchFilteredProducts() {
        val currentFilter = _filterState.value
        try {
            val result = if (currentFilter.searchQuery.isNotBlank()) {
                val searched = productRepository.searchProducts(currentFilter.searchQuery)
                if (currentFilter.selectedCategory != null) {
                    searched.filter { it.category == currentFilter.selectedCategory }
                } else searched
            } else if (currentFilter.selectedCategory != null) {
                productRepository.getProductsByCategory(currentFilter.selectedCategory)
            } else {
                productRepository.getProduct()
            }
            _uiState.value = KatalogUiState.Success(result)
        } catch (e: Exception) {
            _uiState.value = KatalogUiState.Error(e.localizedMessage ?: "Terjadi kesalahan")
        }
    }
}