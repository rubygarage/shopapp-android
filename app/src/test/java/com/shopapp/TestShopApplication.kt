package com.shopapp

import com.shopapp.di.component.AppComponent
import com.shopapp.di.component.DaggerTestAppComponent
import com.shopapp.di.component.TestAppComponent
import com.shopapp.data.dao.Dao
import com.shopapp.gateway.Api

class TestShopApplication : ShopApplication() {

    companion object {
        lateinit var testAppComponent: TestAppComponent
    }

    override fun buildAppComponent(api: Api?, dao: Dao?): AppComponent {
        testAppComponent = DaggerTestAppComponent.builder().build()
        return testAppComponent
    }
}