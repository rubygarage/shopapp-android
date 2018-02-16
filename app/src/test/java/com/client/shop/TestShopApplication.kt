package com.client.shop

import android.content.Context
import com.client.shop.di.component.AppComponent
import com.client.shop.di.component.DaggerTestAppComponent
import com.client.shop.di.module.TestRepositoryModule
import com.data.dao.DaoImpl
import com.shopify.api.ShopifyApi

class TestShopApplication : ShopApplication() {

    companion object {
        lateinit var module: TestRepositoryModule
    }

    override fun attachBaseContext(base: Context) {
        try {
            super.attachBaseContext(base)
        } catch (ignored: RuntimeException) {
            // Multidex support doesn't play well with Robolectric yet
        }
    }

    override fun buildAppComponent(api: ShopifyApi, dao: DaoImpl): AppComponent {
        module = TestRepositoryModule(api, dao)
        return DaggerTestAppComponent.builder()
            .testRepositoryModule(module)
            .build()
    }
}