package com.shopify.api.adapter

import com.shopapicore.entity.Policy
import com.shopify.buy3.Storefront

class PolicyAdapter(adaptee: Storefront.ShopPolicy) : Policy(
        adaptee.title,
        adaptee.body,
        adaptee.url
)