package com.shopapp.magento.api.request

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class SignUpRequestTest {

    @Test
    fun shouldCorrectlySerialize() {

        val email = "testEmail"
        val firstName = "testFirstName"
        val lastName = "testLastName"
        val customerData = SignUpRequest.CustomerData(email, firstName, lastName)
        val password = "testPassword"
        val request = SignUpRequest(customerData, password)

        val gson = Gson()
        val result = gson.toJson(request)

        assertEquals("{\"customer\":{\"email\":\"$email\",\"firstname\":\"$firstName\",\"lastname\":\"$lastName\"},\"password\":\"$password\"}", result)
    }
}