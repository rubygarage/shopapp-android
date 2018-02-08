package com.shopify.api.ext

import com.shopify.buy3.Storefront

fun Storefront.Product.isSingleOptions(): Boolean {
    return options.size == 1 && options.first().values.size == 1
}