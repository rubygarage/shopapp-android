package com.shopapp.data.dao.adapter

import com.shopapp.gateway.entity.Image
import com.shopapp.data.dao.entity.ImageData
import com.shopapp.data.dao.entity.ImageDataEntity

object ImageAdapter {

    fun adaptToStore(adaptee: Image?): ImageData? {
        return if (adaptee != null) {
            val image = ImageDataEntity()
            image.id = adaptee.id
            image.src = adaptee.src
            image.alt = adaptee.alt
            image
        } else {
            null
        }
    }

    fun adaptFromStore(adaptee: ImageData?): Image? {
        return if (adaptee != null) {
            Image(
                adaptee.id,
                adaptee.src,
                adaptee.alt
            )
        } else {
            null
        }
    }
}