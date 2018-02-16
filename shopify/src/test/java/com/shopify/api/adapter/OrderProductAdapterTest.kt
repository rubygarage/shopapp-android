package com.shopify.api.adapter

import com.shopify.JodaTimeAndroidRule
import com.shopify.StorefrontMockInstantiator
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class OrderProductAdapterTest {

    @Rule
    @JvmField
    var jodaTimeAndroidRule = JodaTimeAndroidRule()

    @Test
    fun shouldAdaptFromOrderLineItemStorefrontToOrderProduct() {
        val result = OrderProductAdapter.adapt(StorefrontMockInstantiator.newOrderLineItem())
        assertEquals(StorefrontMockInstantiator.DEFAULT_QUANTITY, result.quantity)
        assertEquals(StorefrontMockInstantiator.DEFAULT_TITLE, result.title)
        assertNotNull(result.productVariant)
    }
}