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
        val result = ShopAdapter.adapt(StorefrontMockInstantiator.newShop())
        Assert.assertEquals(StorefrontMockInstantiator.DEFAULT_SHOP_NAME, result.name)
        Assert.assertEquals(StorefrontMockInstantiator.DEFAULT_DESCRIPTION, result.description)
        Assert.assertNotNull(result.privacyPolicy)
        Assert.assertNotNull(result.refundPolicy)
        Assert.assertNotNull(result.termsOfService)
    }

}