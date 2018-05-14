package com.shopapp.magento.api.response

import com.shopapp.gateway.entity.Category
import java.util.*

class CategoryListResponse(
    val id: String,
    val name: String,
    val childrenData: List<CategoryListResponse>
) {

    private fun mapToEntity(responseData: CategoryListResponse): Category {
        return Category(
            id = responseData.id,
            title = responseData.name,
            categoryDescription = "",
            additionalDescription = "",
            image = null,
            updatedAt = Date(),
            childrenCategoryList = responseData.childrenData.map { mapToEntity(it) },
            productList = listOf()
        )
    }

    fun mapToEntityList(): List<Category> {
        return childrenData.map { mapToEntity(it) }
    }
}