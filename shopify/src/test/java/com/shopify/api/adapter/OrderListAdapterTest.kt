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
class OrderListAdapterTest {

    @Rule
    @JvmField
    var jodaTimeAndroidRule = JodaTimeAndroidRule()

    @Test
    fun shouldAdaptFromOrderListStorefrontToOrderList() {
        val result = OrderListAdapter.adapt(StorefrontMockInstantiator.newOrderConnection())
        assertEquals(1, result.size)
        assertNotNull(result.first())
    }
}