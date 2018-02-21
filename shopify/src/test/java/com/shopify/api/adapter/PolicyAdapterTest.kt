package com.shopify.api.adapter

import com.shopify.StorefrontMockInstantiator
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PolicyAdapterTest {

    @Test
    fun shouldAdaptFromPolicyStorefrontToShop() {
        val result = PolicyAdapter.adapt(StorefrontMockInstantiator.newPolicy())
        Assert.assertEquals(StorefrontMockInstantiator.DEFAULT_TITLE, result?.title)
        Assert.assertEquals(StorefrontMockInstantiator.DEFAULT_BODY, result?.body)
        Assert.assertEquals(StorefrontMockInstantiator.DEFAULT_URL, result?.url)
    }

}