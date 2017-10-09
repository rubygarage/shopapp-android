package com.shopify.api.adapter

import com.shopapicore.entity.Policy
import com.shopapicore.entity.Shop
import com.shopify.buy3.Storefront

class ShopAdapter(data: Storefront.QueryRoot) : Shop(
        data.shop.name,
        data.shop.description,
        extractPolicy(data.shop.privacyPolicy),
        extractPolicy(data.shop.refundPolicy),
        extractPolicy(data.shop.termsOfService)
) {
    companion object {
        private fun extractPolicy(shopPolicy: Storefront.ShopPolicy?): Policy? {
            return if (shopPolicy != null) PolicyAdapter(shopPolicy) else null
        }
    }
}
