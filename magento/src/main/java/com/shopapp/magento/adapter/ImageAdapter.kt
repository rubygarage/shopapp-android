package com.shopapp.magento.adapter

import com.shopapp.gateway.entity.Image
import com.shopapp.magento.api.MagentoApi
import com.shopapp.magento.api.response.ProductResponse

object ImageAdapter {

    private const val NO_SELECTION = "no_selection"
    private const val IMAGE_MEDIA_TYPE = "image"

    fun adapt(catalogPath: String, imagePath: String?): Image? {
        return if (imagePath != null && imagePath != NO_SELECTION) {
            val src = buildSrc(catalogPath, imagePath)
            Image(src, src, "")
        } else {
            null
        }
    }

    fun adapt(catalogPath: String, entry: ProductResponse.GalleryEntry): Image? {
        return if (entry.mediaType == IMAGE_MEDIA_TYPE) {
            Image(entry.id, buildSrc(catalogPath, entry.file), entry.label)
        } else {
            null
        }
    }

    private fun buildSrc(catalogPath: String, imagePath: String): String {
        return MagentoApi.HOST + catalogPath + imagePath
    }
}