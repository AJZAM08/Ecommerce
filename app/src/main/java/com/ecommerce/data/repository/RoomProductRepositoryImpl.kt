package com.ecommerce.data.repository

import com.ecommerce.data.local.dao.ProductDao
import com.ecommerce.domain.entity.Product
import com.ecommerce.domain.entity.ProductCategory
import com.ecommerce.domain.repository.ProductRepository

class RoomProductRepositoryImpl(
    private val productDao: ProductDao
) : ProductRepository {
    override suspend fun getProduct(): List<Product> {
        return productDao.getAllProductsWithVariants().map { it.toDomain() }
    }
    override suspend fun getProductById(id: Long): Product? {
        return productDao.getProductById(id)?.toDomain()
    }
    override suspend fun searchProducts(query: String): List<Product> {
        return productDao.searchProducts(query).map { it.toDomain() }
    }
    override suspend fun getProductsByCategory(category: ProductCategory): List<Product> {
        return productDao.getProductsByCategory(category.name).map { it.toDomain() }
    }
}