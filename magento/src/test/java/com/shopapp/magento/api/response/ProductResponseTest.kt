package com.shopapp.magento.api.response

import com.shopapp.magento.test.TestConstant.DEFAULT_CURRENCY
import com.shopapp.magento.test.TestConstant.DEFAULT_HOST
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat

class ProductResponseTest : ResponseTest<ProductResponse>(ProductResponse::class.java) {

    override fun getFilename() = "ProductResponse.json"

    @Test
    fun shouldCorrectlyParse() {
        val product = response.mapToEntity(DEFAULT_HOST, DEFAULT_CURRENCY)

        assertEquals("WSH10-28-Orange", product.id)
        assertEquals("Ana Running Short-28-Orange", product.title)
        assertEquals("Time to lace up your kicks and beat that personal best in the Ana Running Short.", product.productDescription)
        assertEquals(40f.toBigDecimal(), product.price)
        assertEquals("10", product.type)
        assertEquals("2018-04-30 06:41:41", SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(product.createdAt))
        assertEquals("2018-04-30 06:41:41", SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(product.updatedAt))

        assertEquals(1, product.images.size)
        assertEquals("http://10.14.14.227/pub/media/catalog/product/w/s/wsh10-orange_main.jpg", product.images[0].src)
    }
}