package com.shopapp

import android.content.Context
import com.shopapp.gateway.Api
import com.shopapp.shopify.api.ShopifyApi

object ApiInitializerImpl : ApiInitializer {

    override fun initialize(context: Context): Api? =
        ShopifyApi(context, BuildConfig.BASE_DOMAIN, BuildConfig.STOREFRONT_ACCESS_TOKEN,
            BuildConfig.API_KEY, BuildConfig.API_PASSWORD)
}