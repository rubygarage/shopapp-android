package com.shopify.api.adapter

import com.client.shop.gateway.entity.Shop
import com.shopify.buy3.Storefront

object ShopAdapter {

    fun adapt(data: Storefront.Shop): Shop {
        return with(data) {
            Shop(
                name,
                description,
                PolicyAdapter.adapt(privacyPolicy),
                PolicyAdapter.adapt(refundPolicy),
                PolicyAdapter.adapt(termsOfService)
            )
        }
    }
}
