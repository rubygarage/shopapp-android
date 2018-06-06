package com.shopapp.gateway.entity

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AddressEqualsTest {

    private lateinit var address: Address

    @Before
    fun setUp() {
        val state = State("state_id", "country_id", "state_code", "state_name")
        val country = Country("country_id", "country_code", "country_name", listOf(state))
        address = Address(
            "address_id",
            "address1",
            "address2",
            "city",
            country,
            state,
            "first_name",
            "last_name",
            "49000",
            "32323423423"
        )
    }

    @Test
    fun shouldBeEqualsWithSameObject() {
        assertTrue(address == address)
    }

    @Test
    fun shouldBeEqualsWithObjectWithSameData() {
        assertTrue(address == address.copy())
    }

    @Test
    fun shouldBeEqualsWithObjectWithDifferentId() {
        assertTrue(address == address.copy(id = "different_address_id"))
    }

    @Test
    fun shouldBeNotEqualsWithObjectWithDifferentData() {
        assertFalse(address == address.copy(address = "different_address 1"))
        assertFalse(address == address.copy(secondAddress = "different_address 2"))
        assertFalse(address == address.copy(city = "different_city"))
        assertFalse(address == address.copy(firstName = "different_first_name"))
        assertFalse(address == address.copy(lastName = "different_last_name"))
        assertFalse(address == address.copy(zip = "37000"))
        assertFalse(address == address.copy(phone = "235325235255"))

        val country = address.country.copy(id = "different_id")
        assertFalse(address == address.copy(country = country))

        val state = address.state?.copy(id = "different_id")
        assertFalse(address == address.copy(state = state))
    }
}