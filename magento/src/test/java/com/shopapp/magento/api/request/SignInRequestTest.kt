package com.shopapp.magento.api.request

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class SignInRequestTest {

    @Test
    fun shouldCorrectlySerialize() {

        val email = "testEmail"
        val password = "testPassword"
        val request = SignInRequest(email, password)

        val gson = Gson()
        val result = gson.toJson(request)

        assertEquals("{\"username\":\"$email\",\"password\":\"$password\"}", result)
    }
}