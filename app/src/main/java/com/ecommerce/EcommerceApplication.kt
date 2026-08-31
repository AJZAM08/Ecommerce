package com.ecommerce

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.ecommerce.data.AppContainer
import com.ecommerce.data.DefaultAppContainer
class EcommerceApplication : Application(), ImageLoaderFactory {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this).memoryCache {
            MemoryCache.Builder(this).maxSizePercent(0.25).build()
        }.diskCache {
            DiskCache.Builder().directory(cacheDir.resolve("image_cache")).maxSizeBytes(50L * 1024 * 1024).build()
        }.crossfade(true).respectCacheHeaders(false).build()
    }
}