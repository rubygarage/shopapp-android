package com.client.shop

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.client.shop.di.component.AppComponent
import com.client.shop.di.component.DaggerAppComponent
import com.client.shop.di.module.RepositoryModule
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.data.dao.DaoImpl
import com.facebook.drawee.backends.pipeline.Fresco
import com.shopify.api.ShopifyApi
import io.fabric.sdk.android.Fabric
import io.reactivex.plugins.RxJavaPlugins

open class ShopApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        val api = ShopifyApi(this, BuildConfig.BASE_DOMAIN, BuildConfig.ACCESS_TOKEN)
        val dao = DaoImpl(this)

        appComponent = buildAppComponent(api, dao)

        setupFresco()
        setupFabric()

        RxJavaPlugins.setErrorHandler { e ->
            e.printStackTrace()
            Crashlytics.logException(e)
        }
    }

    open protected fun buildAppComponent(api: ShopifyApi, dao: DaoImpl): AppComponent {
        return DaggerAppComponent.builder()
            .repositoryModule(RepositoryModule(api, dao))
            .build()
    }

    private fun setupFabric() {
        // Set up Crashlytics, disabled for debug builds
        val crashlyticsKit = Crashlytics.Builder()
            .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
            .build()
        Fabric.with(this, crashlyticsKit)
    }

    private fun setupFresco() {
        Fresco.initialize(this)
    }
}