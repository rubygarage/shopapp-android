package com.shopapp

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.di.component.AppComponent
import com.shopapp.di.component.DaggerTestAppComponent
import com.shopapp.di.module.TestRepositoryModule
import com.shopapp.domain.database.Dao
import com.shopapp.gateway.Api

class TestShopApplication : ShopApplication() {

    companion object {
        lateinit var module: TestRepositoryModule
    }

    override fun buildAppComponent(api: Api?, dao: Dao?): AppComponent {
        module = TestRepositoryModule(mock(), mock())
        return DaggerTestAppComponent.builder()
                .testRepositoryModule(module)
                .build()
    }
}