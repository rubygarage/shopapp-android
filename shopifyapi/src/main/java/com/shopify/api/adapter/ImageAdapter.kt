package com.shopify.api.adapter

import com.shopapicore.entity.Image
import com.shopify.buy3.Storefront

class ImageAdapter private constructor(image: Storefront.Image) : Image(
        image.id.toString(),
        image.src,
        image.altText) {

    companion object {

        fun newInstance(image: Storefront.Image?): ImageAdapter? {
            return if (image != null) ImageAdapter(image) else null
        }
    }
}