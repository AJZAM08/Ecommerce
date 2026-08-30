package com.ecommerce.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.ecommerce.domain.entity.Product
import com.ecommerce.domain.entity.ProductCategory
import com.ecommerce.domain.entity.ProductVariant

@Entity(
    tableName = "products"
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val description: String,
    val category: String,
    val benefits: String,
    val composition: String,
    val usage: String,
    val imageUrl: String
)

@Entity(
    tableName = "product_variants",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("productId")]
)
data class ProductVariantEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val productId: Long,
    val size: String,
    val price: Long,
    val stock: Int
)

data class ProductWithVariants(
    @Embedded val product: ProductEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "productId"
    )
    val variants: List<ProductVariantEntity>
) {
    fun toDomain(): Product {
        return Product(
            id = product.id,
            name = product.name,
            description = product.description,
            category = try {
                ProductCategory.valueOf(product.category)
            } catch (e: Exception) {
                ProductCategory.PURE_OIL
            },
            benefits = product.benefits,
            composition = product.composition,
            usage = product.usage,
            imageUrl = product.imageUrl,
            variants = variants.map {
                ProductVariant(
                    id = it.id,
                    productId = it.productId,
                    size = it.size,
                    price = it.price,
                    stock = it.stock
                )
            }
        )
    }
}