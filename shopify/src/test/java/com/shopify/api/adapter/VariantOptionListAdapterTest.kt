package com.shopify.api.adapter

import com.shopify.StorefrontMockInstantiator
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class VariantOptionListAdapterTest {

    @Test
    fun shouldAdaptFromOptionListStorefrontToVariantOptionList() {
        val resultList = VariantOptionListAdapter.adapt(listOf(StorefrontMockInstantiator.newSelectedOption()))
        val result = resultList.first()
        assertEquals(StorefrontMockInstantiator.DEFAULT_NAME, result.name)
        assertEquals(StorefrontMockInstantiator.DEFAULT_VALUE, result.value)
    }
}