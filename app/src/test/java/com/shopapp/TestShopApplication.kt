package com.client.shop

import android.content.Context
import com.client.shop.di.component.DaggerTestAppComponent
import com.client.shop.di.module.TestRepositoryModule
import com.client.shop.gateway.Api
import com.domain.database.Dao
import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ShopApplication
import com.shopapp.di.component.AppComponent

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

    override fun buildAppComponent(api: Api?, dao: Dao?): AppComponent {
        module = TestRepositoryModule(mock(), mock())
        return DaggerTestAppComponent.builder()
            .testRepositoryModule(module)
            .build()
    }
}