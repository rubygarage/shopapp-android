package com.shopify.api.adapter

import com.client.shop.gateway.entity.VariantOption
import com.shopify.buy3.Storefront

object VariantOptionListAdapter {

    fun adapt(adaptee: List<Storefront.SelectedOption>?): List<VariantOption> =
        adaptee?.map { VariantOption(it.name, it.value) } ?: listOf()
}