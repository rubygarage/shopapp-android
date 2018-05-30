package com.shopapp.magento.api.response

import com.shopapp.magento.test.TestConstant.DEFAULT_CURRENCY
import com.shopapp.magento.test.TestConstant.DEFAULT_HOST
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat

class ProductListResponseTest : BaseResponseTest<ProductListResponse>(ProductListResponse::class.java) {

    override fun getFilename() = "ProductListResponse.json"

    @Test
    fun shouldCorrectlyParse() {
        val result = response.mapToEntityList(DEFAULT_HOST, DEFAULT_CURRENCY, 1, 10)
        assertEquals(2, result.size)

        val item = result[0]
        assertEquals("24-MG04", item.id)
        assertEquals("Aim Analog Watch", item.title)
        assertEquals("Stay light-years ahead of the competition with our Aim Analog Watch. Japanese quartz movement. Strap fits 7\" to 8.0\".", item.productDescription)
        assertEquals(45f.toBigDecimal(), item.price)
        assertEquals("11", item.type)
        assertEquals("2018-04-30 06:40:55", SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(item.createdAt))
        assertEquals("2018-04-30 06:40:57", SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(item.updatedAt))

        assertEquals(2, item.images.size)
        assertEquals("http://10.14.14.227/pub/media/catalog/product/m/g/mg04-bk-0.jpg", item.images[0].src)
    }
}