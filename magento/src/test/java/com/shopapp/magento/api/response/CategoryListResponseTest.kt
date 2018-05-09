package com.shopapp.magento.api.response

import org.junit.Assert.assertEquals
import org.junit.Test

class CategoryListResponseTest : ResponseTest<CategoryListResponse>(CategoryListResponse::class.java) {

    override fun getFilename() = "CategoryListResponse.json"

    @Test
    fun shouldCorrectlyParse() {
        val result = response.mapToEntityList()
        assertEquals(2, result.size)

        val item = result[0]
        assertEquals(21.toString(), item.id)
        assertEquals("Tops", item.title)
        assertEquals(4, item.childrenCategoryList.size)
    }
}