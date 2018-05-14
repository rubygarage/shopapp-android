package com.shopapp.magento.adapter

import com.shopapp.gateway.entity.Image
import com.shopapp.magento.api.response.ProductResponse

object ImageAdapter {

    const val NO_SELECTION = "no_selection"
    const val IMAGE_MEDIA_TYPE = "image"

    fun adapt(host: String, catalogPath: String, imagePath: String?): Image? {
        return if (imagePath != null && imagePath != NO_SELECTION) {
            val src = buildSrc(host, catalogPath, imagePath)
            Image(src, src, "")
        } else {
            null
        }
    }

    fun adapt(host: String, catalogPath: String, entry: ProductResponse.GalleryEntry): Image? {
        return if (entry.mediaType == IMAGE_MEDIA_TYPE) {
            Image(entry.id, buildSrc(host, catalogPath, entry.file), entry.label)
        } else {
            null
        }
    }

    private fun buildSrc(host: String, catalogPath: String, imagePath: String): String {
        return host + catalogPath + imagePath
    }
}