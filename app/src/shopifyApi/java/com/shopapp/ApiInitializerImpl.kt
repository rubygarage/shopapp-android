package com.shopapp

import android.content.Context
import android.util.Log
import com.shopapp.gateway.Api
import com.shopapp.shopify.api.ShopifyApi

object ApiInitializerImpl : ApiInitializer {

    override fun initialize(context: Context): Api? {
        Log.d("TEST", BuildConfig.BASE_DOMAIN)
        Log.d("TEST", BuildConfig.STOREFRONT_ACCESS_TOKEN)
        Log.d("TEST", BuildConfig.API_KEY)
        Log.d("TEST", BuildConfig.API_PASSWORD)
        Log.d("TEST", BuildConfig.APPLICATION_ID)
        return ShopifyApi(context, BuildConfig.BASE_DOMAIN, BuildConfig.STOREFRONT_ACCESS_TOKEN,
            BuildConfig.API_KEY, BuildConfig.API_PASSWORD)
    }

}