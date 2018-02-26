package com.shopapp.data.dao.adapter

import com.shopapp.data.util.ConstantHolder
import com.shopapp.data.util.MockInstantiator
import com.shopapp.data.util.RequeryMockInstantiator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class CartProductAdapterTest {

    @Test
    fun shouldAdaptFromCartProductDataToCartProduct() {
        val result = CartProductAdapter.adaptFromStore(RequeryMockInstantiator.newProduct())
        assertEquals(ConstantHolder.DEFAULT_TITLE, result.title)
        assertEquals(ConstantHolder.DEFAULT_QUANTITY, result.quantity)
        assertEquals(ConstantHolder.DEFAULT_CURRENCY, result.currency)
        assertNotNull(result.productVariant)
    }

    @Test
    fun shouldAdaptFromCartProductToCartProductData() {
        val result = CartProductAdapter.adaptToStore(MockInstantiator.newCartProduct())
        assertEquals(ConstantHolder.DEFAULT_TITLE, result.title)
        assertEquals(ConstantHolder.DEFAULT_QUANTITY, result.quantity)
        assertEquals(ConstantHolder.DEFAULT_CURRENCY, result.currency)
        assertNotNull(result.productVariant)
    }
}