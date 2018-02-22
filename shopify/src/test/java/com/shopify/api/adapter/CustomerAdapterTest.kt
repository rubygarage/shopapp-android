package com.shopify.api.adapter

import com.shopify.JodaTimeAndroidRule
import com.shopify.StorefrontMockInstantiator
import junit.framework.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CustomerAdapterTest {

    @Rule
    @JvmField
    var jodaTimeAndroidRule = JodaTimeAndroidRule()

    @Test
    fun shouldAdaptCustomerStorefrontToCustomer() {
        val result = CustomerAdapter.adapt(StorefrontMockInstantiator.newCustomer())
        assertEquals(StorefrontMockInstantiator.DEFAULT_ID, result.id)
        assertEquals(StorefrontMockInstantiator.DEFAULT_FIRST_NAME, result.firstName)
        assertEquals(StorefrontMockInstantiator.DEFAULT_LAST_NAME, result.lastName)
        assertEquals(StorefrontMockInstantiator.DEFAULT_PHONE, result.phone)
        assertEquals(StorefrontMockInstantiator.DEFAULT_EMAIL, result.email)
        assertFalse(result.isAcceptsMarketing)
        assertNotNull(result.defaultAddress)
        assertNotNull(result.addressList.first())
    }
}