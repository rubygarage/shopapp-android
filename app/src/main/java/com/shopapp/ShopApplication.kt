package com.shopapp

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.facebook.drawee.backends.pipeline.Fresco
import com.shopapp.data.dao.Dao
import com.shopapp.data.dao.impl.DaoImpl
import com.shopapp.di.component.AppComponent
import com.shopapp.di.component.DaggerAppComponent
import com.shopapp.di.module.ConfigModule
import com.shopapp.di.module.RepositoryModule
import com.shopapp.gateway.Api
import com.shopapp.gateway.entity.Config
import com.shopapp.magento.api.MagentoApi
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

        val api = MagentoApi(this, "http://10.14.14.29/")
        val dao = DaoImpl(this)

        appComponent = buildAppComponent(api, dao, api.getConfig())

        setupFresco()
        setupFabric()

        RxJavaPlugins.setErrorHandler { e ->
            e.printStackTrace()
            Crashlytics.logException(e)
        }
    }

    protected open fun buildAppComponent(api: Api?, dao: Dao?, config: Config?): AppComponent {
        val builder = DaggerAppComponent.builder()
        if (api != null && dao != null && config != null) {
            builder.repositoryModule(RepositoryModule(api, dao))
                .configModule(ConfigModule(config))
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