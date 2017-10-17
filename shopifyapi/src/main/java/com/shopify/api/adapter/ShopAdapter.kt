package com.shopify.api.adapter

import com.shopapicore.entity.Policy
import com.shopapicore.entity.Shop
import com.shopify.buy3.Storefront

class ShopAdapter(data: Storefront.QueryRoot) : Shop(
        data.shop.name,
        data.shop.description,
        convertPolicy(data.shop.privacyPolicy),
        convertPolicy(data.shop.refundPolicy),
        convertPolicy(data.shop.termsOfService)
) {
    companion object {

        private fun convertPolicy(shopPolicy: Storefront.ShopPolicy?): Policy? {
            return if (shopPolicy != null) PolicyAdapter(shopPolicy) else null
        }
    }
}
