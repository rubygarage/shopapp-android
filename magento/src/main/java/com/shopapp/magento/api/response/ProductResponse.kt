package com.shopapp.magento.api.response

import com.shopapp.gateway.entity.Image
import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.ProductVariant
import com.shopapp.magento.adapter.ImageAdapter
import com.shopapp.magento.api.Constant
import com.shopapp.magento.api.Constant.DESCRIPTION_ATTRIBUTE
import com.shopapp.magento.api.Constant.PRODUCT_IMAGE_PATH
import com.shopapp.magento.api.ext.getValue
import com.shopapp.magento.api.response.util.CustomAttribute
import org.jsoup.Jsoup
import java.util.*

open class ProductResponse(
    val id: Int,
    val sku: String,
    val name: String,
    val attributeSetId: String,
    val price: Double,
    val createdAt: Date,
    val updatedAt: Date,
    val customAttributes: List<CustomAttribute>,
    val mediaGalleryEntries: List<GalleryEntry>
) {

    class GalleryEntry(
        val id: String,
        val mediaType: String,
        val label: String?,
        val file: String
    )

    fun mapToEntity(host: String, currency: String): Product {

        val htmlDescription = customAttributes.getValue(DESCRIPTION_ATTRIBUTE) ?: ""
        val description = Jsoup.parse(htmlDescription).text()
        val images = mediaGalleryEntries.mapNotNull { ImageAdapter.adapt(host, PRODUCT_IMAGE_PATH, it) }
        val productVariant = createProductVariant(host, images.firstOrNull())

        return Product(
            id = sku,
            title = name,
            productDescription = description,
            additionalDescription = description,
            currency = currency,
            price = price.toBigDecimal(),
            hasAlternativePrice = false,
            discount = null,
            vendor = "stub vendor",
            type = attributeSetId,
            createdAt = createdAt,
            updatedAt = updatedAt,
            tags = listOf(),
            images = images,
            options = listOf(),
            variants = listOf(productVariant)
        )
    }

    private fun createProductVariant(host: String, productImage: Image?): ProductVariant {

        val thumbnail = ImageAdapter.adapt(host, PRODUCT_IMAGE_PATH, customAttributes.getValue(Constant.THUMBNAIL_ATTRIBUTE))
        val image = ImageAdapter.adapt(host, PRODUCT_IMAGE_PATH, customAttributes.getValue(Constant.IMAGE_ATTRIBUTE))
        val images = listOfNotNull(thumbnail, image)
        val mainImage = images.firstOrNull() ?: productImage

        return ProductVariant(
            id = sku,
            title = name,
            price = price.toBigDecimal(),
            isAvailable = true,
            selectedOptions = listOf(),
            image = null,
            productImage = mainImage,
            productId = sku
        )
    }
}