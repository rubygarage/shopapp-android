package com.shopapp.magento.api.response

import com.shopapp.gateway.entity.Category
import com.shopapp.gateway.entity.Product
import com.shopapp.magento.adapter.ImageAdapter
import com.shopapp.magento.api.Constant
import com.shopapp.magento.api.Constant.CATEGORY_IMAGE_PATH
import com.shopapp.magento.api.Constant.IMAGE_ATTRIBUTE
import com.shopapp.magento.api.ext.getValue
import com.shopapp.magento.api.response.util.CustomAttribute
import org.jsoup.Jsoup
import java.util.*

class CategoryDetailsResponse(
    val id: String,
    val name: String,
    val customAttributes: List<CustomAttribute>,
    val updatedAt: Date
) {

    fun mapToEntity(host: String, productList: List<Product>): Category {

        val htmlDescription = customAttributes.getValue(Constant.DESCRIPTION_ATTRIBUTE) ?: ""
        val description = Jsoup.parse(htmlDescription).text()
        val image = ImageAdapter.adapt(host, CATEGORY_IMAGE_PATH, customAttributes.getValue(IMAGE_ATTRIBUTE))

        return Category(
            id = id,
            title = name,
            categoryDescription = description,
            additionalDescription = description,
            image = image,
            updatedAt = updatedAt,
            childrenCategoryList = listOf(),
            productList = productList
        )
    }
}
