package com.data.dao.adapter

import com.data.dao.entity.ImageData
import com.data.dao.entity.ImageDataEntity
import com.client.shop.gateway.entity.Image

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