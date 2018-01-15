package com.client.shop

import android.app.Application
import android.support.multidex.MultiDexApplication
import com.client.shop.di.component.AppComponent
import com.client.shop.di.component.DaggerAppComponent
import com.client.shop.di.module.RepositoryModule
import com.client.shop.di.module.RouterModule
import com.client.shop.router.AppRouterImpl
import com.data.dao.DaoImpl
import com.domain.router.AppRouter
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.shopify.ShopifyWrapper
import io.reactivex.plugins.RxJavaPlugins


class ShopApplication : MultiDexApplication() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        val appRouter: AppRouter = AppRouterImpl()
        val dao = DaoImpl(this)
        val shopWrapper = ShopifyWrapper(this, dao, appRouter)

        appComponent = DaggerAppComponent.builder()
                .routerModule(RouterModule(shopWrapper))
                .repositoryModule(RepositoryModule(shopWrapper.api, dao))
                .build()

        val config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build()
        Fresco.initialize(this, config)

        RxJavaPlugins.setErrorHandler { e -> e.printStackTrace() }
    }
}