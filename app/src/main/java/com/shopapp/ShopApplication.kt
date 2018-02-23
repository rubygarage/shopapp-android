package com.shopapp

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.facebook.drawee.backends.pipeline.Fresco
import com.shopapp.data.dao.DaoImpl
import com.shopapp.di.component.AppComponent
import com.shopapp.di.component.DaggerAppComponent
import com.shopapp.di.module.RepositoryModule
import com.shopapp.domain.database.Dao
import com.shopapp.gateway.Api
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

//        val api = ShopifyApi(
//            this,
//            "guned.myshopify.com",
//            "52cba0bfcddb2ad3158ef5fdd02d6097",
//            "94d6c7429f787a5a9221f788d335c60c",
//            "50379a5c87be476210d402b3adcddd6b"
//        )
        val api = null
        val dao = DaoImpl(this)

        appComponent = buildAppComponent(api, dao)

        setupFresco()
        setupFabric()

        RxJavaPlugins.setErrorHandler { e ->
            e.printStackTrace()
            Crashlytics.logException(e)
        }
    }

    protected open fun buildAppComponent(api: Api?, dao: Dao?): AppComponent {
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