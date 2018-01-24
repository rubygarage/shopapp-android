package com.shopify.util

import com.shopify.buy3.Storefront

object OptionsUtils {

    fun isSingleOption(options: MutableList<Storefront.ProductOption>): Boolean {
        return options.size == 1 && options.first().values.size == 1
    }

}