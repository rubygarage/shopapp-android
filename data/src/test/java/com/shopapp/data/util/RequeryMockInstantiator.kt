package com.shopapp.data.util

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.shopapp.data.dao.entity.CartProductData
import com.shopapp.data.dao.entity.ImageData
import com.shopapp.data.dao.entity.ProductVariantData
import com.shopapp.data.dao.entity.VariantOptionData

object RequeryMockInstantiator {

    fun newImage(): ImageData = mock {
        val productMock: ProductVariantData = mock()

        on { id } doReturn ConstantHolder.DEFAULT_ID
        on { src } doReturn ConstantHolder.DEFAULT_SRC
        on { alt } doReturn ConstantHolder.DEFAULT_DESCRIPTION
        on { productVariant } doReturn (productMock)
    }

    fun newProduct(): CartProductData = mock {
        val mock = newProductVariant()

        on { id } doReturn ConstantHolder.DEFAULT_ID
        on { title } doReturn ConstantHolder.DEFAULT_TITLE
        on { productVariant } doReturn mock
        on { quantity } doReturn ConstantHolder.DEFAULT_QUANTITY
        on { currency } doReturn ConstantHolder.DEFAULT_CURRENCY
    }

    fun newProductVariant(): ProductVariantData = mock {
        val imageData: ImageData = newImage()
        val variants = listOf(newVariantOption(it))

        on { id } doReturn ConstantHolder.DEFAULT_ID
        on { title } doReturn ConstantHolder.DEFAULT_TITLE
        on { price } doReturn ConstantHolder.DEFAULT_FLOAT_PRICE
        on { isAvailable } doReturn true
        on { selectedOptions } doReturn variants
        on { image } doReturn imageData
        on { productImage } doReturn imageData
        on { productItemId } doReturn ConstantHolder.DEFAULT_ID

    }

    fun newVariantOption(productMock: ProductVariantData = mock()): VariantOptionData = mock {
        on { id } doReturn ConstantHolder.DEFAULT_INT_ID
        on { optionName } doReturn ConstantHolder.DEFAULT_NAME
        on { optionValue } doReturn ConstantHolder.DEFAULT_VALUE
        on { productVariant } doReturn productMock
    }
}