package com.shopify.api.adapter

import com.client.shop.getaway.entity.Shop
import com.shopify.buy3.Storefront

object ShopAdapter {

    fun adapt(data: Storefront.QueryRoot): Shop {
        return Shop(
            data.shop.name,
            data.shop.description,
            PolicyAdapter.adapt(data.shop.privacyPolicy),
            PolicyAdapter.adapt(data.shop.refundPolicy),
            PolicyAdapter.adapt(data.shop.termsOfService)
        )
    }
}
