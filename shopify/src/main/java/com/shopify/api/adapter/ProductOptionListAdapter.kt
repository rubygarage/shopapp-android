package com.shopify.api.adapter

import com.client.shop.gateway.entity.ProductOption
import com.shopify.buy3.Storefront

object ProductOptionListAdapter {

    fun adapt(adaptee: List<Storefront.ProductOption>?): List<ProductOption> =
        adaptee?.map { ProductOption(it.id.toString(), it.name, it.values) } ?: listOf()
}