package com.client.shop

import android.app.Application
import com.client.shop.di.component.AppComponent
import com.client.shop.di.component.DaggerAppComponent
import com.client.shop.di.module.NetworkModule
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.shopify.api.ShopifyApi

class MainApplication : Application() {

    companion object {
        private const val BASE_URL = "storeshmor.myshopify.com"
        private const val ACCESS_TOKEN = "12a5873d85ff01cddea6261913ecf3e9"

        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .networkModule(NetworkModule(ShopifyApi(this, BASE_URL, ACCESS_TOKEN)))
                .build()

        val config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build()
        Fresco.initialize(this, config)
    }
}