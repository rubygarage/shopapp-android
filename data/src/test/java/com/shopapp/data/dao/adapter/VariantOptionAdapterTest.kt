package com.shopapp.data.dao.adapter

import com.shopapp.data.util.ConstantHolder
import com.shopapp.data.util.MockInstantiator
import com.shopapp.data.util.RequeryMockInstantiator
import org.junit.Assert.assertEquals
import org.junit.Test

class VariantOptionAdapterTest {

    @Test
    fun shouldAdaptFromVariantOptionDataToVariantOption() {
        val result = VariantOptionAdapter.adaptFromStore(RequeryMockInstantiator.newVariantOption())
        assertEquals(ConstantHolder.DEFAULT_NAME, result.name)
        assertEquals(ConstantHolder.DEFAULT_VALUE, result.value)
    }

    @Test
    fun shouldAdaptFromVariantOptionToVariantOptionData() {
        val result = VariantOptionAdapter.adaptToStore(MockInstantiator.newVariantOption())
        assertEquals(ConstantHolder.DEFAULT_NAME, result.optionName)
        assertEquals(ConstantHolder.DEFAULT_VALUE, result.optionValue)
    }
}