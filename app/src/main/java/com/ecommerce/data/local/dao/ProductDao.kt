package com.ecommerce.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ecommerce.data.local.entity.ProductEntity
import com.ecommerce.data.local.entity.ProductVariantEntity
import com.ecommerce.data.local.entity.ProductWithVariants

@Dao
interface ProductDao {
    @Transaction
    @Query("SELECT * FROM products ORDER BY id ASC")
    suspend fun getAllProductsWithVariants(): List<ProductWithVariants>

    @Transaction
    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Long): ProductWithVariants?

    @Transaction
    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    suspend fun searchProducts(query: String): List<ProductWithVariants>

    @Transaction
    @Query("SELECT * FROM products WHERE category = :category")
    suspend fun getProductsByCategory(category: String): List<ProductWithVariants>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVariants(variants: List<ProductVariantEntity>)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int
}