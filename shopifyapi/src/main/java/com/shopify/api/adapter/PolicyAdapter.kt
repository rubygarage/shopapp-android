package com.shopify.api.adapter

import com.domain.entity.Policy
import com.shopify.buy3.Storefront

class PolicyAdapter {

    companion object {

        fun adapt(adaptee: Storefront.ShopPolicy?): Policy? {
            return if (adaptee != null) {
                Policy(
                        adaptee.title,
                        adaptee.body,
                        adaptee.url)
            } else {
                null
            }
        }
    }
}