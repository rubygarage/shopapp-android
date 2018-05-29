package com.shopapp.magento.api.response

import org.junit.Assert.assertEquals
import org.junit.Test

class CustomerResponseTest : BaseResponseTest<CustomerResponse>(CustomerResponse::class.java) {

    override fun getFilename() = "CustomerResponse.json"

    @Test
    fun shouldCorrectlyParse() {
        val result = response.mapToEntity()

        assertEquals("9", result.id)
        assertEquals("test@mail.com", result.email)
        assertEquals("f", result.firstName)
        assertEquals("l", result.lastName)
    }
}