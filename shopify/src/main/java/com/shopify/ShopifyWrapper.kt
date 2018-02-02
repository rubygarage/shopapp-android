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
        const val BASE_URL = "celawojev.myshopify.com"
        const val ANDROID_PAY_PUBLIC_KEY = "BGrDRuoxm5Pbo1o0fyVHhz3mtxF0fHzprf3umF+gdQ9OarBYwiM0+BmJgaPg5gUbRGgQ5PMkBS/vWdYNnlKBqfc="
        private const val ACCESS_TOKEN = "fd719b8c3c31ea4ea5f4078e8b9a759f"

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