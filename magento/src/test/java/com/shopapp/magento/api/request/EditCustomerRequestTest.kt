package com.shopapp.magento.api.request

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class EditCustomerRequestTest {

    @Test
    fun shouldCorrectlySerialize() {

        val email = "testEmail"
        val firstName = "testFirstName"
        val lastName = "testLastName"
        val websiteId = 0
        val customerData = EditCustomerRequest.CustomerData(email, firstName, lastName, websiteId, listOf())
        val request = EditCustomerRequest(customerData)

        val gson = Gson()
        val result = gson.toJson(request)

        assertEquals("{\"customer\":{\"email\":\"$email\",\"firstname\":\"$firstName\",\"lastname\":\"$lastName\",\"website_id\":$websiteId,\"addresses\":[]}}", result)
    }
}