package com.shopify

import android.content.Context
import com.domain.ShopWrapper
import com.domain.database.Dao
import com.shopify.api.ShopifyApi
import com.shopify.di.DaggerShopComponent
import com.shopify.di.ShopComponent
import com.shopify.di.ShopifyModule
import com.shopify.router.ShopifyRouter

class ShopifyWrapper(context: Context, dao: Dao) : ShopWrapper {

    companion object {
        const val BASE_URL = "vatosozu.myshopify.com"
        const val ANDROID_PAY_PUBLIC_KEY = "BIDbJPP1q4qo8QAgTucnHcsPLewIz2uv5mYjUWM3sHewR4mbh8zq16iOT2j0tURRfY5b32YK7sSGpC2Ncfy7Jkg="
        private const val ACCESS_TOKEN = "1634a4d3079dba0d6f73625e3c1ca0c2"

        lateinit var component: ShopComponent
    }

    override val api = ShopifyApi(context, BASE_URL, ACCESS_TOKEN)
    override val router = ShopifyRouter()

    init {
        component = DaggerShopComponent.builder().shopifyModule(ShopifyModule(api, dao)).build()
    }
}