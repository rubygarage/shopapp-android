package com.shopify.api.adapter

import com.shopify.JodaTimeAndroidRule
import com.shopify.StorefrontMockInstantiator
import junit.framework.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ShopAdapterTest {

    @Rule
    @JvmField
    var jodaTimeAndroidRule = JodaTimeAndroidRule()

    @Test
    fun shouldAdaptFromShopStorefrontToShop() {
        val paginationValue = "pagination_value"
        val result = ShopAdapter.adapt(StorefrontMockInstantiator.newOrder(), paginationValue)
        Assert.assertEquals(StorefrontMockInstantiator.DEFAULT_ID, result.id)
        Assert.assertEquals(StorefrontMockInstantiator.DEFAULT_CURRENCY_CODE.name, result.currency)
        Assert.assertEquals(StorefrontMockInstantiator.DEFAULT_EMAIL, result.email)
        Assert.assertEquals(StorefrontMockInstantiator.DEFAULT_ORDER_NUMBER, result.orderNumber)
        Assert.assertEquals(StorefrontMockInstantiator.DEFAULT_PRICE, result.totalPrice)
        Assert.assertEquals(StorefrontMockInstantiator.DEFAULT_PRICE, result.subtotalPrice)
        Assert.assertEquals(StorefrontMockInstantiator.DEFAULT_PRICE, result.totalShippingPrice)
        Assert.assertNotNull(result.address)
        Assert.assertEquals(StorefrontMockInstantiator.DEFAULT_DATE.toDate(), result.processedAt)
        Assert.assertNotNull(result.orderProducts.first())
        Assert.assertEquals(paginationValue, result.paginationValue)
    }
}