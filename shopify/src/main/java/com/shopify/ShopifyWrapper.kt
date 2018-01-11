package com.shopify

import android.content.Context
import com.domain.ShopWrapper
import com.domain.database.Dao
import com.domain.router.AppRouter
import com.shopify.api.ShopifyApi
import com.shopify.di.component.DaggerShopComponent
import com.shopify.di.component.ShopComponent
import com.shopify.di.module.RouterModule
import com.shopify.di.module.ShopifyModule
import com.shopify.router.ShopifyRouter

class ShopifyWrapper(context: Context, dao: Dao, appRouter: AppRouter) : ShopWrapper {

    companion object {
        const val BASE_URL = "blablablstore.myshopify.com"
        const val ANDROID_PAY_PUBLIC_KEY = "BIDbJPP1q4qo8QAgTucnHcsPLewIz2uv5mYjUWM3sHewR4mbh8zq16iOT2j0tURRfY5b32YK7sSGpC2Ncfy7Jkg="
        private const val ACCESS_TOKEN = "317392940ba3519bcfa070943900b1de"

        lateinit var component: ShopComponent
    }

    override val api = ShopifyApi(context, BASE_URL, ACCESS_TOKEN)
    override val router = ShopifyRouter()

    init {
        component = DaggerShopComponent.builder()
                .shopifyModule(ShopifyModule(api, dao))
                .routerModule(RouterModule(appRouter))
                .build()
    }
}