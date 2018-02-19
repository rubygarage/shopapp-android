package com.shopify.api.adapter

import com.shopify.JodaTimeAndroidRule
import com.shopify.StorefrontMockInstantiator
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddressAdapterTest {

    @Rule
    @JvmField
    var jodaTimeAndroidRule = JodaTimeAndroidRule()

    @Test
    fun shouldAdaptAddressStorefrontToAddress() {
        val result = AddressAdapter.adapt(StorefrontMockInstantiator.newAddress())
        assertEquals(StorefrontMockInstantiator.DEFAULT_ID, result.id)
        assertEquals(StorefrontMockInstantiator.DEFAULT_ADDRESS, result.address)
        assertEquals(StorefrontMockInstantiator.DEFAULT_ADDRESS, result.secondAddress)
        assertEquals(StorefrontMockInstantiator.DEFAULT_CITY, result.city)
        assertEquals(StorefrontMockInstantiator.DEFAULT_COUNTRY, result.country)
        assertEquals(StorefrontMockInstantiator.DEFAULT_STATE, result.state)
        assertEquals(StorefrontMockInstantiator.DEFAULT_FIRST_NAME, result.firstName)
        assertEquals(StorefrontMockInstantiator.DEFAULT_LAST_NAME, result.lastName)
        assertEquals(StorefrontMockInstantiator.DEFAULT_ZIP, result.zip)
        assertEquals(StorefrontMockInstantiator.DEFAULT_PHONE, result.phone)
    }
}