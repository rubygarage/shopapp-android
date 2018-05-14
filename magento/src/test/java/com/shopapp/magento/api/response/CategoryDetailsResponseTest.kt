package com.shopapp.magento.api.response

import com.shopapp.gateway.entity.Product
import com.shopapp.magento.test.TestConstant.DEFAULT_HOST
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat

class CategoryDetailsResponseTest : ResponseTest<CategoryDetailsResponse>(CategoryDetailsResponse::class.java) {

    override fun getFilename() = "CategoryDetailsResponse.json"

    @Test
    fun shouldCorrectlyParse() {
        val productList: List<Product> = listOf()
        val category = response.mapToEntity(DEFAULT_HOST, productList)

        assertEquals(23.toString(), category.id)
        assertEquals("Jackets", category.title)
        assertEquals(productList, category.productList)
        assertEquals("test description", category.categoryDescription)
        assertEquals("2018-04-30 06:41:05", SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(category.updatedAt))
    }
}