package com.daocore.adapter

import com.daocore.entity.ImageData
import com.daocore.entity.ImageDataEntity
import com.domain.entity.Image

class ImageAdapter {

    companion object {

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
}