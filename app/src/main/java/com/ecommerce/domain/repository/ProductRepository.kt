package com.ecommerce.domain.repository

import com.ecommerce.domain.entity.Product
import com.ecommerce.domain.entity.ProductCategory

interface ProductRepository {
    suspend fun getProduct(): List<Product>
    suspend fun getProductById(id: Long): Product?
    suspend fun searchProducts(query: String): List<Product>
    suspend fun getProductsByCategory(category: ProductCategory): List<Product>
}