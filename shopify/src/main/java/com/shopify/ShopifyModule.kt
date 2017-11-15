package com.shopify

import android.content.Context
import com.domain.ShopModule
import com.shopify.api.ShopifyApi
import com.shopify.di.ShopComponent
import com.shopify.router.ShopifyRouter

class ShopifyModule(context: Context) : ShopModule {

    companion object {
        private const val BASE_URL = "lalkastore.myshopify.com"
        private const val ACCESS_TOKEN = "677af790376ae84213f7ea1ed56f11ca"

        lateinit var shopComponent: ShopComponent
    }

    override val api = ShopifyApi(context, BASE_URL, ACCESS_TOKEN)
    override val router = ShopifyRouter()
}