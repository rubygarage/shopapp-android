package com.shopapp.magento.api.request

import com.google.gson.Gson
import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Country
import com.shopapp.gateway.entity.Customer
import com.shopapp.gateway.entity.State
import org.junit.Assert.assertEquals
import org.junit.Test

class CustomerRequestTest {

    @Test
    fun shouldCorrectlySerialize() {

        val email = "testEmail"
        val firstName = "testFirstName"
        val lastName = "testLastName"
        val state = State("state_id", "country_id", "TS", "Test State")
        val country = Country("country_id", "TC", "Test Country", listOf(state))
        val address = Address("address_id", "addr1", "addr2", "cty", country,
            state, firstName, lastName, "49000", "344364343343")
        val customer = Customer(
            "customer_id", email, firstName, lastName, "435632344",
            true, listOf(address), address
        )

        val customerData = CustomerRequest.CustomerData(customer)
        val request = CustomerRequest(customerData)

        val gson = Gson()
        val result = gson.toJson(request)

        assertEquals("{\"customer\":{\"email\":\"testEmail\",\"website_id\":0,\"" +
                "addresses\":[{\"id\":\"address_id\",\"countryId\":\"country_id\"," +
                "\"street\":[\"addr1\",\"addr2\"],\"telephone\":\"344364343343\"," +
                "\"postcode\":\"49000\",\"city\":\"cty\",\"firstname\":\"testFirstName\"," +
                "\"lastname\":\"testLastName\",\"defaultShipping\":1,\"region_id\":\"state_id\"}]," +
                "\"firstname\":\"testFirstName\",\"lastname\":\"testLastName\"}}", result)
    }
}