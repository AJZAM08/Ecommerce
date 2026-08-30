package com.ecommerce.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.ecommerce.domain.entity.Order
import com.ecommerce.domain.entity.OrderItem
import com.ecommerce.domain.entity.OrderStatus

@Entity(
    tableName = "orders"
)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val customerName: String,
    val address: String,
    val phone: String,
    val shippingCost: Long,
    val totalPrice: Long,
    val status: String,
    val createdAt: Long
)

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("orderId")]
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val orderId: Long,
    val productId: Long,
    val productName: String,
    val variantSize: String,
    val price: Long,
    val quantity: Int
)

data class OrderWithItems(
    @Embedded val order: OrderEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "orderId"
    )
    val items: List<OrderItemEntity>
) {
    fun toDomain() : Order = Order(
        id = order.id,
        customerName = order.customerName,
        address = order.address,
        phone = order.phone,
        shippingCost = order.shippingCost,
        totalPrice = order.totalPrice,
        status = try {
            OrderStatus.valueOf(order.status)
        } catch (e: Exception) {
            OrderStatus.PENDING
        },
        createdAt = order.createdAt,
        items = items.map {
            OrderItem(
                id = it.id,
                orderId = it.orderId,
                productId = it.productId,
                productName = it.productName,
                variantSize = it.variantSize,
                price = it.price,
                quantity = it.quantity
            )
        }
    )
}