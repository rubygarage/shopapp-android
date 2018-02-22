package com.shopapp

import android.content.Context
import com.shopapp.gateway.Api
import com.shopapp.domain.database.Dao
import com.nhaarman.mockito_kotlin.mock
import com.shopapp.di.component.AppComponent
import com.shopapp.di.component.DaggerTestAppComponent
import com.shopapp.di.module.TestRepositoryModule

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