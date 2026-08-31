package com.ecommerce.data.repository

import com.ecommerce.data.local.dao.ProductDao
import com.ecommerce.data.local.entity.ProductEntity
import com.ecommerce.data.local.entity.ProductVariantEntity
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
    override suspend fun addProduct(product: Product): Result<Long> {
        return try {
            val entity = ProductEntity(
                name = product.name,
                description = product.description,
                category = product.category.name,
                benefits = product.benefits,
                composition = product.composition,
                usage = product.usage,
                imageUrl = product.imageUrl
            )
            val productId = productDao.insertProduct(entity)
            val variantEntities = product.variants.map { v ->
                ProductVariantEntity(
                    productId = productId,
                    size = v.size,
                    price = v.price,
                    stock = v.stock
                )
            }
            productDao.insertVariants(variantEntities)
            Result.success(productId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun updateProductStock(variantId: Long, newStock: Int) {
        productDao.updateVariantStock(variantId, newStock)
    }
    override suspend fun deleteProduct(productId: Long) {
        productDao.deleteProduct(productId)
    }
}