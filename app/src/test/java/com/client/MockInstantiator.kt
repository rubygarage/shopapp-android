package com.client

import com.client.shop.getaway.entity.*
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import java.math.BigDecimal
import java.util.*

object MockInstantiator {

    const val DEFAULT_ID = "DEFAULT_ID"
    const val DEFAULT_CURRENCY = "USD"
    const val DEFAULT_EMAIL = "default@mail.com"
    const val DEFAULT_ORDER_NUMBER = 5
    const val DEFAULT_PAGINATION_VALUE = "default_pagination_value"
    const val DEFAULT_TITLE = "default_title"
    const val DEFAULT_QUANTITY = 5
    const val DEFAULT_SRC = "default_src"
    const val DEFAULT_DESCRIPTION = "default_description"
    val DEFAULT_PRICE = BigDecimal.TEN
    val DEFAULT_DATE = Date()

    fun <T> newList(item: T, size: Int = 0): List<T> {
        val list: MutableList<T> = mutableListOf()
        repeat(size) {
            list.add(item)
        }
        return list
    }

    fun newOrder(): Order = mock {

        val orderProductListMock = newList(newOrderProduct(), 5)

        on { id } doReturn DEFAULT_ID
        on { currency } doReturn DEFAULT_CURRENCY
        on { email } doReturn DEFAULT_EMAIL
        on { orderNumber } doReturn DEFAULT_ORDER_NUMBER
        on { subtotalPrice } doReturn DEFAULT_PRICE
        on { totalShippingPrice } doReturn DEFAULT_PRICE
        on { totalPrice } doReturn DEFAULT_PRICE
        on { processedAt } doReturn DEFAULT_DATE
        on { orderProducts } doReturn orderProductListMock
        on { address } doReturn newAddress()
        on { paginationValue } doReturn DEFAULT_PAGINATION_VALUE
    }

    fun newOrderProduct(): OrderProduct = mock {

        val productVariantMock = newProductVariant()

        on { title } doReturn DEFAULT_TITLE
        on { productVariant } doReturn productVariantMock
        on { quantity } doReturn DEFAULT_QUANTITY
    }

    fun newProductVariant(): ProductVariant = mock {

        val imageMock = newImage()

        on { id } doReturn DEFAULT_ID
        on { title } doReturn DEFAULT_TITLE
        on { price } doReturn DEFAULT_PRICE
        on { isAvailable } doReturn true
        on { selectedOptions } doReturn listOf<VariantOption>()
        on { image } doReturn imageMock
        on { productImage } doReturn imageMock
        on { productId } doReturn DEFAULT_ID
    }

    fun newImage(): Image = mock {
        on { id } doReturn DEFAULT_ID
        on { src } doReturn DEFAULT_SRC
        on { alt } doReturn DEFAULT_DESCRIPTION
    }

    fun newAddress(): Address = mock {
        //TODO ADD MOCKED DATA
    }
}