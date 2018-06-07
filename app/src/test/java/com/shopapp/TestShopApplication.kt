package com.shopapp

import com.shopapp.data.dao.Dao
import com.shopapp.di.component.AppComponent
import com.shopapp.di.component.DaggerTestAppComponent
import com.shopapp.di.component.TestAppComponent
import com.shopapp.gateway.Api
import com.shopapp.gateway.entity.Config

class TestShopApplication : ShopApplication() {

    companion object {
        lateinit var testAppComponent: TestAppComponent
    }

    override fun buildAppComponent(api: Api?, dao: Dao?, config: Config?): AppComponent {
        testAppComponent = DaggerTestAppComponent.builder().build()
        return testAppComponent
    }
}