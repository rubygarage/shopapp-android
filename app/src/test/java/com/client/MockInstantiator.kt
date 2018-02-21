package com.client

import com.client.shop.gateway.entity.*
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
    const val DEFAULT_NAME = "default_name"
    const val DEFAULT_VALUE = "default_value"
    const val DEFAULT_ADDRESS = "default_address"
    const val DEFAULT_CITY = "default_city"
    const val DEFAULT_COUNTRY = "default_country"
    const val DEFAULT_STATE = "default_state"
    const val DEFAULT_FIRST_NAME = "default_first_name"
    const val DEFAULT_LAST_NAME = "default_last_name"
    const val DEFAULT_ZIP = "default_zip"
    const val DEFAULT_PHONE = "default_phone"
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
        val addressMock = newAddress()

        on { id } doReturn DEFAULT_ID
        on { currency } doReturn DEFAULT_CURRENCY
        on { email } doReturn DEFAULT_EMAIL
        on { orderNumber } doReturn DEFAULT_ORDER_NUMBER
        on { subtotalPrice } doReturn DEFAULT_PRICE
        on { totalShippingPrice } doReturn DEFAULT_PRICE
        on { totalPrice } doReturn DEFAULT_PRICE
        on { processedAt } doReturn DEFAULT_DATE
        on { orderProducts } doReturn orderProductListMock
        on { address } doReturn addressMock
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

    fun newVariantOption(): VariantOption = mock {
        on { name } doReturn DEFAULT_NAME
        on { value } doReturn DEFAULT_VALUE
    }

    fun newImage(): Image = mock {
        on { id } doReturn DEFAULT_ID
        on { src } doReturn DEFAULT_SRC
        on { alt } doReturn DEFAULT_DESCRIPTION
    }

    fun newAddress(): Address = mock {
        on { id } doReturn DEFAULT_ID
        on { address } doReturn DEFAULT_ADDRESS
        on { secondAddress } doReturn DEFAULT_ADDRESS
        on { city } doReturn DEFAULT_CITY
        on { country } doReturn DEFAULT_COUNTRY
        on { state } doReturn DEFAULT_STATE
        on { firstName } doReturn DEFAULT_FIRST_NAME
        on { lastName } doReturn DEFAULT_LAST_NAME
        on { zip } doReturn DEFAULT_ZIP
        on { phone } doReturn DEFAULT_PHONE
    }
}