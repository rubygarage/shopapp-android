package com.shopapp.magento.api.response

import org.junit.Assert.assertEquals
import org.junit.Test

class CountryListResponseTest : BaseResponseTest<CountryListResponse>(CountryListResponse::class.java) {

    override fun getFilename() = "CountryListResponse.json"

    @Test
    fun shouldCorrectlyParse() {
        val result = response.mapToEntityList()
        assertEquals(13, result.size)

        val item = result.last()
        assertEquals("AT", item.id)
        assertEquals("Austria", item.name)
        assertEquals("AT", item.code)
        assertEquals(9, item.states?.size)
    }
}