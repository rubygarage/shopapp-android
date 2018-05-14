package com.shopapp.magento.api.response

import org.junit.Assert.assertEquals
import org.junit.Test

class StoreConfigResponseTest : ResponseTest<StoreConfigResponse>(StoreConfigResponse::class.java) {

    override fun getFilename() = "StoreConfigResponse.json"

    @Test
    fun shouldCorrectlyParse() {
        val result = response.getCurrency()
        assertEquals("USD", result)
    }
}