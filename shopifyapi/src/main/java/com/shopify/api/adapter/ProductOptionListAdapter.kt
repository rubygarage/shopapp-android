package com.shopify.api.adapter

import com.shopapicore.entity.ProductOption
import com.shopify.buy3.Storefront

class ProductOptionListAdapter {

    companion object {

        fun adapt(adaptee: List<Storefront.ProductOption>?): List<ProductOption> {
            return adaptee?.map { ProductOption(it.id.toString(), it.name, it.values) } ?: listOf()
        }
    }
}