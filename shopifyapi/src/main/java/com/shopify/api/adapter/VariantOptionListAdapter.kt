package com.shopify.api.adapter

import com.shopapicore.entity.VariantOption
import com.shopify.buy3.Storefront

class VariantOptionListAdapter {

    companion object {

        fun adapt(adaptee: List<Storefront.SelectedOption>?): List<VariantOption> {
            return adaptee?.map { VariantOption(it.name, it.value) } ?: listOf()
        }
    }
}