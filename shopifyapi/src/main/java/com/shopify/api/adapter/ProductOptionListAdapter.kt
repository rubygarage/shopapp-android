package com.shopify.api.adapter

import com.shopapicore.entity.ProductOption
import com.shopify.buy3.Storefront

class ProductOptionListAdapter(options: List<Storefront.ProductOption>) : ArrayList<ProductOption>() {

    init {
        options.map { ProductOption(it.id.toString(), it.name, it.values) }.forEach { add(it) }
    }
}