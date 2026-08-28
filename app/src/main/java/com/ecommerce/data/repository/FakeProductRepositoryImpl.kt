package com.ecommerce.data.repository

import com.ecommerce.data.fake.FakeProductDataSource
import com.ecommerce.domain.entity.Product
import com.ecommerce.domain.entity.ProductCategory
import com.ecommerce.domain.repository.ProductRepository
import kotlinx.coroutines.delay

class FakeProductRepositoryImpl : ProductRepository {
    private val products = FakeProductDataSource.dummyProduct.toMutableList()

    override suspend fun getProduct(): List<Product> {
        delay(400)
        return products
    }

    override suspend fun getProductById(id: Long): Product? {
        delay(200)
        return products.find { it.id == id }
    }

    override suspend fun searchProducts(query: String): List<Product> {
        delay(300)
        return  products.filter {
            it.name.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true)
        }
    }

    override suspend fun getProductsByCategory(category: ProductCategory): List<Product> {
        delay(300)
        return products.filter { it.category == category }
    }
}