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
import com.ecommerce.data.local.dao.UserDao
import com.ecommerce.data.local.entity.CartItemEntity
import com.ecommerce.data.local.entity.OrderEntity
import com.ecommerce.data.local.entity.OrderItemEntity
import com.ecommerce.data.local.entity.ProductEntity
import com.ecommerce.data.local.entity.ProductVariantEntity
import com.ecommerce.data.local.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProductEntity::class,
        ProductVariantEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        UserEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun userDao(): UserDao

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
                        prePopulateDatabase(database.productDao(), database.userDao())
                    }
                }
            }

            private suspend fun prePopulateDatabase(productDao: ProductDao, userDao: UserDao) {
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

                if (userDao.getUserCount() == 0) {
                    userDao.insertUser(
                        UserEntity(
                            id = 1L,
                            name = "Admin Toko Atsiri",
                            email = "admin@katalogminyak.com",
                            passwordHash = "admin123",
                            phone = "081299998888",
                            address = "Kantor Pusat Gudang Minyak Atsiri, Ambon",
                            role = "ADMIN",
                            isCurrentSession = false
                        )
                    )

                    userDao.insertUser(
                        UserEntity(
                            id = 2L,
                            name = "Ahmad Fulan",
                            email = "customer@email.com",
                            passwordHash = "customer123",
                            phone = "081234567890",
                            address = "Jl. Rempah Herbal No. 12, Jakarta Utara",
                            role = "CUSTOMER",
                            isCurrentSession = false
                        )
                    )
                }
            }
        }
    }
}