package com.shopify.api.adapter

import com.shopapicore.entity.VariantOption
import com.shopify.buy3.Storefront

class VariantOptionListAdapter(options: List<Storefront.SelectedOption>) : ArrayList<VariantOption>() {

    init {
        options.map { VariantOption(it.name, it.value) }.forEach { add(it) }
    }
}