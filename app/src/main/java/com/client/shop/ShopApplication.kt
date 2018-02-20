package com.client.shop

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.client.shop.di.component.AppComponent
import com.client.shop.di.component.DaggerAppComponent
import com.client.shop.di.module.RepositoryModule
import com.client.shop.gateway.Api
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.data.dao.DaoImpl
import com.domain.database.Dao
import com.facebook.drawee.backends.pipeline.Fresco
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

        val api = null //Initialize your api here.
        val dao = DaoImpl(this)

        appComponent = buildAppComponent(api, dao)

        setupFresco()
        setupFabric()

        RxJavaPlugins.setErrorHandler { e ->
            e.printStackTrace()
            Crashlytics.logException(e)
        }
    }

    open protected fun buildAppComponent(api: Api?, dao: Dao?): AppComponent {
        val builder = DaggerAppComponent.builder()
        if (api != null && dao != null) {
            builder.repositoryModule(RepositoryModule(api, dao))
        }
        return builder.build()
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