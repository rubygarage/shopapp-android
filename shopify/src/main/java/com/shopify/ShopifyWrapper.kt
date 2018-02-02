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
        const val BASE_DOMAIN = "celawojev.myshopify.com"
        const val BASE_URL = "https://celawojev.myshopify.com"
        const val ANDROID_PAY_PUBLIC_KEY = "BIDbJPP1q4qo8QAgTucnHcsPLewIz2uv5mYjUWM3sHewR4mbh8zq16iOT2j0tURRfY5b32YK7sSGpC2Ncfy7Jkg="
        const val ACCESS_TOKEN = "fd719b8c3c31ea4ea5f4078e8b9a759f"
        const val SHOPIFY_API_KEY = "3a46b61f31cb8b9028a507b29a1c15fb"
        const val PASSWORD = "5c0122b460eb82233253ed581530ceb6"

        lateinit var component: ShopComponent
    }

    override val api = ShopifyApi(context, BASE_DOMAIN, ACCESS_TOKEN)
    override val router = ShopifyRouter()

    init {
        component = DaggerShopComponent.builder()
            .shopifyModule(ShopifyModule(api, dao))
            .routerModule(RouterModule(appRouter))
            .build()
    }
}