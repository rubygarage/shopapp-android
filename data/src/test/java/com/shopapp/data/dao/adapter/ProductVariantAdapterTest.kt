package com.shopapp.data.dao.adapter

import com.shopapp.data.util.ConstantHolder
import com.shopapp.data.util.MockInstantiator
import com.shopapp.data.util.RequeryMockInstantiator
import org.junit.Assert.*
import org.junit.Test

class ProductVariantAdapterTest {

    @Test
    fun shouldAdaptFromProductVariantDataToVariantOption() {
        val result = ProductVariantAdapter.adaptFromStore(RequeryMockInstantiator.newProductVariant())
        assertEquals(ConstantHolder.DEFAULT_ID, result.id)
        assertEquals(ConstantHolder.DEFAULT_ID, result.productId)
        assertEquals(ConstantHolder.DEFAULT_TITLE, result.title)
        assertEquals(ConstantHolder.DEFAULT_FLOAT_PRICE, result.price.toFloat())
        assertTrue(result.isAvailable)
        assertNotNull(result.selectedOptions)
        assertNotNull(result.image)
        assertNotNull(result.productImage)
    }

    @Test
    fun shouldAdaptFromProductVariantToProductVariantData() {
        val result = ProductVariantAdapter.adaptToStore(MockInstantiator.newProductVariant())
        assertEquals(ConstantHolder.DEFAULT_ID, result.id)
        assertEquals(ConstantHolder.DEFAULT_ID, result.productItemId)
        assertEquals(ConstantHolder.DEFAULT_TITLE, result.title)
        assertEquals(ConstantHolder.DEFAULT_FLOAT_PRICE, result.price)
        assertTrue(result.isAvailable)
        assertNotNull(result.selectedOptions)
        assertNotNull(result.image)
        assertNotNull(result.productImage)
    }
}