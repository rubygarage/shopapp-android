package com.shopify.api.adapter

import com.domain.entity.Image
import com.shopify.buy3.Storefront

object ImageAdapter {

    fun adapt(image: Storefront.Image?): Image? {
        return if (image != null) {
            Image(
                    image.id.toString(),
                    image.src,
                    image.altText
            )
        } else null
    }
}