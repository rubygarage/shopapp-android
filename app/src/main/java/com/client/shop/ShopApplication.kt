package com.client.shop

import android.app.Application
import com.client.shop.di.component.AppComponent
import com.client.shop.di.component.DaggerAppComponent
import com.client.shop.di.module.RepositoryModule
import com.client.shop.di.module.RouterModule
import com.daocore.DaoImpl
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.shopify.ShopifyModule
import io.reactivex.plugins.RxJavaPlugins


class ShopApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        val shopModule = ShopifyModule(this)
        val dao = DaoImpl(this)

        appComponent = DaggerAppComponent.builder()
                .routerModule(RouterModule(shopModule))
                .repositoryModule(RepositoryModule(shopModule.api, dao))
                .build()

        val config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build()
        Fresco.initialize(this, config)

        RxJavaPlugins.setErrorHandler { e -> e.printStackTrace() }
    }
}