package com.shopapp.magento.api.request

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class ForgotPasswordRequestTest {

    @Test
    fun shouldCorrectlySerialize() {

        val email = "testEmail"
        val request = ForgotPasswordRequest(email)

        val gson = Gson()
        val result = gson.toJson(request)

        assertEquals("{\"email\":\"$email\",\"template\":\"email_reset\"}", result)
    }
}