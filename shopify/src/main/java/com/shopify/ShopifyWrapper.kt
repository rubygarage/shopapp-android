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
        const val BASE_DOMAIN = "palkomin.myshopify.com"
        const val BASE_URL = "https://$BASE_DOMAIN"
        const val ANDROID_PAY_PUBLIC_KEY = "BIDbJPP1q4qo8QAgTucnHcsPLewIz2uv5mYjUWM3sHewR4mbh8zq16iOT2j0tURRfY5b32YK7sSGpC2Ncfy7Jkg="
        const val ACCESS_TOKEN = "2098ab2fb06659df83ccf0f6df678dc6"
        const val SHOPIFY_API_KEY = "d64eae31336ae451296daf24f52b0327"
        const val PASSWORD = "b54086c46fe6825198e4542a96499d51"

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