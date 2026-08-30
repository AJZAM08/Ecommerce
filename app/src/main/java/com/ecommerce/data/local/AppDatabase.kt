package com.ecommerce.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ecommerce.data.fake.FakeProductDataSource
import com.ecommerce.data.local.dao.CartDao
import com.ecommerce.data.local.dao.OrderDao
import com.ecommerce.data.local.dao.ProductDao
import com.ecommerce.data.local.entity.CartItemEntity
import com.ecommerce.data.local.entity.OrderEntity
import com.ecommerce.data.local.entity.OrderItemEntity
import com.ecommerce.data.local.entity.ProductEntity
import com.ecommerce.data.local.entity.ProductVariantEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProductEntity::class,
        ProductVariantEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        OrderItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ) : AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "katalog_minyak.db"
                ).addCallback(DatabaseCallBack(scope)).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallBack(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        prePopulateDatabase(database.productDao())
                    }
                }
            }

            private suspend fun prePopulateDatabase(productDao: ProductDao) {
                if (productDao.getProductCount() == 0) {
                    val dummyProducts = FakeProductDataSource.dummyProduct
                    for (p in dummyProducts) {
                        val productEntity = ProductEntity(
                            p.id,
                            p.name,
                            p.description,
                            p.category.name,
                            p.benefits,
                            p.composition,
                            p.usage,
                            p.imageUrl
                        )
                        val insertedId = productDao.insertProduct(productEntity)
                        val variantEntities = p.variants.map { v ->
                            ProductVariantEntity(
                                v.id,
                                insertedId,
                                v.size,
                                v.price,
                                v.stock
                            )
                        }
                        productDao.insertVariants(variantEntities)
                    }
                }
            }
        }
    }
}