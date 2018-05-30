package com.shopapp.magento.api.response

import org.junit.Assert.assertEquals
import org.junit.Test

class ErrorResponseTest : BaseResponseTest<ErrorResponse>(ErrorResponse::class.java) {

    override fun getFilename() = "ErrorResponse.json"

    @Test
    fun shouldCorrectlyParse() {
        val result = response.message
        assertEquals("Error message", result)
    }
}