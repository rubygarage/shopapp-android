package com.shopify

import android.content.Context
import com.domain.ShopWrapper
import com.domain.database.Dao
import com.shopify.api.ShopifyApi
import com.shopify.di.CheckoutModule
import com.shopify.di.DaggerShopComponent
import com.shopify.di.ShopComponent
import com.shopify.router.ShopifyRouter

class ShopifyWrapper(context: Context, dao: Dao) : ShopWrapper {

    companion object {
        private const val BASE_URL = "lalkastore.myshopify.com"
        private const val ACCESS_TOKEN = "677af790376ae84213f7ea1ed56f11ca"

        lateinit var component: ShopComponent
    }

    override val api = ShopifyApi(context, BASE_URL, ACCESS_TOKEN)
    override val router = ShopifyRouter()

    init {
        component = DaggerShopComponent.builder().checkoutModule(CheckoutModule(api, dao)).build()
    }
}