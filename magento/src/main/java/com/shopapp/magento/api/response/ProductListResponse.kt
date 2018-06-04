package com.shopapp.magento.api.response

import com.shopapp.gateway.entity.Product
import com.shopapp.magento.adapter.ImageAdapter
import com.shopapp.magento.api.Constant
import com.shopapp.magento.api.Constant.DESCRIPTION_ATTRIBUTE
import com.shopapp.magento.api.Constant.IMAGE_ATTRIBUTE
import com.shopapp.magento.api.Constant.PRODUCT_IMAGE_PATH
import com.shopapp.magento.api.Constant.THUMBNAIL_ATTRIBUTE
import com.shopapp.magento.api.ext.getValue
import com.shopapp.magento.api.response.util.CustomAttribute
import org.jsoup.Jsoup
import java.util.*

class ProductListResponse(
    private val items: List<ProductResponseItem>,
    private val totalCount: Int
) {

    fun mapToEntityList(host: String, currency: String, page: Int, perPage: Int): List<Product> {
        val paginationValue = calculatePaginationValue(page, perPage)
        return items.map { it.mapToEntity(host, currency, paginationValue) }
    }

    private fun calculatePaginationValue(page: Int, perPage: Int): Int {
        return if (totalCount >= page * perPage && page != Constant.PAGINATION_END_VALUE) {
            page + 1
        } else {
            Constant.PAGINATION_END_VALUE
        }
    }

    class ProductResponseItem(
        val id: Int,
        val sku: String,
        val name: String,
        val attributeSetId: String,
        val price: Double,
        val createdAt: Date,
        val updatedAt: Date,
        val customAttributes: List<CustomAttribute>
    ) {

        fun mapToEntity(host: String, currency: String, paginationValue: Int): Product {

            val htmlDescription = customAttributes.getValue(DESCRIPTION_ATTRIBUTE) ?: ""
            val description = Jsoup.parse(htmlDescription).text()
            val thumbnail = ImageAdapter.adapt(
                host,
                PRODUCT_IMAGE_PATH,
                customAttributes.getValue(THUMBNAIL_ATTRIBUTE)
            )
            val image = ImageAdapter.adapt(
                host,
                PRODUCT_IMAGE_PATH,
                customAttributes.getValue(IMAGE_ATTRIBUTE)
            )
            val images = listOfNotNull(thumbnail, image)

            return Product(
                id = sku,
                title = name,
                productDescription = description,
                additionalDescription = description,
                currency = currency,
                price = price.toBigDecimal(),
                hasAlternativePrice = false,
                discount = null,
                vendor = "",
                type = attributeSetId,
                createdAt = createdAt,
                updatedAt = updatedAt,
                tags = listOf(),
                images = images,
                options = listOf(),
                variants = listOf(),
                paginationValue = paginationValue.toString()
            )
        }
    }
}