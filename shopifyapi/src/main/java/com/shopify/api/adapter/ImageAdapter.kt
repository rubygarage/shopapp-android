package com.shopify.api.adapter

import com.shopapicore.entity.Image
import com.shopify.buy3.Storefront

class ImageAdapter(image: Storefront.Image) : Image(
        image.id.toString(),
        image.src,
        image.altText)