package com.shopapp.data.util

import com.shopapp.gateway.entity.*
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import java.math.BigDecimal
import java.util.*

object MockInstantiator {

    fun newCartProduct(): CartProduct = mock {
        val productVariantMock = newProductVariant()

        on { title } doReturn ConstantHolder.DEFAULT_TITLE
        on { productVariant } doReturn productVariantMock
        on { quantity } doReturn ConstantHolder.DEFAULT_QUANTITY
        on { currency } doReturn ConstantHolder.DEFAULT_CURRENCY

    }

    fun newProductVariant(): ProductVariant = mock {
        val imageMock = newImage()

        on { id } doReturn ConstantHolder.DEFAULT_ID
        on { title } doReturn ConstantHolder.DEFAULT_TITLE
        on { price } doReturn ConstantHolder.DEFAULT_PRICE
        on { isAvailable } doReturn true
        on { selectedOptions } doReturn listOf<VariantOption>()
        on { image } doReturn imageMock
        on { productImage } doReturn imageMock
        on { productId } doReturn ConstantHolder.DEFAULT_ID
    }

    fun newVariantOption(): VariantOption = mock {
        on { name } doReturn ConstantHolder.DEFAULT_NAME
        on { value } doReturn ConstantHolder.DEFAULT_VALUE
    }

    fun newImage(): Image = mock {
        on { id } doReturn ConstantHolder.DEFAULT_ID
        on { src } doReturn ConstantHolder.DEFAULT_SRC
        on { alt } doReturn ConstantHolder.DEFAULT_DESCRIPTION
    }
}