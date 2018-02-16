package com.shopify.api.adapter

import com.shopify.StorefrontMockInstantiator
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ProductVariantAdapterTest {

    @Test
    fun shouldAdaptFromProductVariantStorefrontToProductVariant() {
        val result = ProductVariantAdapter.adapt(StorefrontMockInstantiator.newProductVariant())
        Assert.assertEquals(StorefrontMockInstantiator.DEFAULT_ID, result.id)
        Assert.assertEquals(StorefrontMockInstantiator.DEFAULT_TITLE, result.title)
        Assert.assertEquals(StorefrontMockInstantiator.DEFAULT_PRICE, result.price)
        Assert.assertEquals(true, result.isAvailable)
        Assert.assertEquals(StorefrontMockInstantiator.DEFAULT_ID, result.productId)
        Assert.assertNotNull(result.selectedOptions.first())
        Assert.assertNotNull(result.image)
        Assert.assertNotNull(result.productImage)
    }
}