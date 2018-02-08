package com.shopify

import android.content.Context
import com.domain.ShopWrapper
import com.domain.database.Dao
import com.domain.router.AppRouter
import com.shopify.api.BuildConfig
import com.shopify.api.ShopifyApi
import com.shopify.di.component.DaggerShopComponent
import com.shopify.di.component.ShopComponent
import com.shopify.di.module.RouterModule
import com.shopify.di.module.ShopifyModule
import com.shopify.router.ShopifyRouter

class ShopifyWrapper(context: Context, dao: Dao, appRouter: AppRouter) : ShopWrapper {

    companion object {
        lateinit var component: ShopComponent
    }

    override val api = ShopifyApi(context, BuildConfig.BASE_DOMAIN, BuildConfig.ACCESS_TOKEN)
    override val router = ShopifyRouter()

    init {
        component = DaggerShopComponent.builder()
            .shopifyModule(ShopifyModule(api, dao))
            .routerModule(RouterModule(appRouter))
            .build()
    }
}