package com.shopapp.data.dao.adapter

import com.shopapp.data.util.ConstantHolder
import com.shopapp.data.util.MockInstantiator
import com.shopapp.data.util.RequeryMockInstantiator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ImageAdapterTest {

    @Test
    fun shouldAdaptFromImageDataStorefrontToImage() {
        val result = ImageAdapter.adaptFromStore(RequeryMockInstantiator.newImage())
        assertEquals(ConstantHolder.DEFAULT_ID, result?.id)
        assertEquals(ConstantHolder.DEFAULT_SRC, result?.src)
        assertEquals(ConstantHolder.DEFAULT_DESCRIPTION, result?.alt)
    }

    @Test
    fun shouldAdaptFromImageStorefrontToImageData() {
        val result = ImageAdapter.adaptToStore(MockInstantiator.newImage())
        assertEquals(ConstantHolder.DEFAULT_ID, result?.id)
        assertEquals(ConstantHolder.DEFAULT_SRC, result?.src)
        assertEquals(ConstantHolder.DEFAULT_DESCRIPTION, result?.alt)
    }

    @Test
    fun shouldReturnNullWhenAdaptFromImageStorefrontToImageData() {
        assertNull(ImageAdapter.adaptToStore(null))
    }

    @Test
    fun shouldReturnNullWhenAdaptFromImageDataStorefrontToImage() {
        assertNull(ImageAdapter.adaptFromStore(null))
    }
}