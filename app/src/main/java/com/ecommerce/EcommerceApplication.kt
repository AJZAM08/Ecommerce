package com.ecommerce

import android.app.Application
import com.ecommerce.data.AppContainer
import com.ecommerce.data.DefaultAppContainer
class EcommerceApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}