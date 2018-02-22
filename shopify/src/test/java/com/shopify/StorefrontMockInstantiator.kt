package com.shopify

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.spy
import com.shopify.buy3.Storefront
import com.shopify.graphql.support.ID
import org.joda.time.DateTime
import java.math.BigDecimal

object StorefrontMockInstantiator {
    const val DEFAULT_ID = "default_id"
    const val DEFAULT_SRC = "default_src"
    const val DEFAULT_ALT_TEXT = "default_alt_text"
    const val DEFAULT_NAME = "default_name"
    const val DEFAULT_VALUE = "default_value"
    const val DEFAULT_TITLE = "default_title"
    const val DEFAULT_QUANTITY = 5
    const val DEFAULT_ADDRESS = "default_address"
    const val DEFAULT_CITY = "default_city"
    const val DEFAULT_COUNTRY = "default_country"
    const val DEFAULT_FIRST_NAME = "default_first_name"
    const val DEFAULT_LAST_NAME = "default_last_name"
    const val DEFAULT_PHONE = "default_phone"
    const val DEFAULT_STATE = "default_province"
    const val DEFAULT_ZIP = "default_zip"
    const val DEFAULT_EMAIL = "default@email.com"
    const val DEFAULT_ORDER_NUMBER = 15
    const val DEFAULT_ACCEPT_MARKETING = false
    val DEFAULT_PRICE: BigDecimal = BigDecimal.TEN
    val DEFAULT_DATE: DateTime = DateTime.now()
    val DEFAULT_CURRENCY_CODE: Storefront.CurrencyCode = Storefront.CurrencyCode.USD

    fun newID() = spy(ID(DEFAULT_ID))

    fun newImage(): Storefront.Image = mock {
        on { id } doReturn newID()
        on { src } doReturn DEFAULT_SRC
        on { altText } doReturn DEFAULT_ALT_TEXT
    }

    fun newSelectedOption(): Storefront.SelectedOption = mock {
        on { name } doReturn DEFAULT_NAME
        on { value } doReturn DEFAULT_VALUE
    }

    fun newProductVariant(): Storefront.ProductVariant = mock {

        val imageMock = newImage()
        val selectedOption = newSelectedOption()
        val partialProduct = newPartialProduct()

        on { id } doReturn newID()
        on { title } doReturn DEFAULT_TITLE
        on { price } doReturn DEFAULT_PRICE
        on { availableForSale } doReturn true
        on { image } doReturn imageMock
        on { selectedOptions } doReturn listOf(selectedOption)
        on { product } doReturn partialProduct
    }

    fun newPartialProduct(): Storefront.Product = mock {

        val imageConnection = newImageConnection()

        on { id } doReturn newID()
        on { images } doReturn imageConnection
    }

    fun newOrderLineItem(): Storefront.OrderLineItem = mock {

        val productVariantMock = newProductVariant()

        on { title } doReturn DEFAULT_TITLE
        on { quantity } doReturn DEFAULT_QUANTITY
        on { variant } doReturn productVariantMock
    }

    fun newAddress(): Storefront.MailingAddress = mock {
        on { id } doReturn newID()
        on { address1 } doReturn DEFAULT_ADDRESS
        on { address2 } doReturn DEFAULT_ADDRESS
        on { city } doReturn DEFAULT_CITY
        on { country } doReturn DEFAULT_COUNTRY
        on { firstName } doReturn DEFAULT_FIRST_NAME
        on { lastName } doReturn DEFAULT_LAST_NAME
        on { phone } doReturn DEFAULT_PHONE
        on { province } doReturn DEFAULT_STATE
        on { zip } doReturn DEFAULT_ZIP
    }

    fun newCustomer(): Storefront.Customer = mock {
        val addressConnection = newAddressConnection()
        val address = addressConnection.edges[0].node

        on { id } doReturn newID()
        on { email } doReturn DEFAULT_EMAIL
        on { defaultAddress } doReturn address
        on { firstName } doReturn DEFAULT_FIRST_NAME
        on { lastName } doReturn DEFAULT_LAST_NAME
        on { phone } doReturn DEFAULT_PHONE
        on { acceptsMarketing } doReturn DEFAULT_ACCEPT_MARKETING
        on { addresses } doReturn addressConnection
    }

    fun newAddressConnection(): Storefront.MailingAddressConnection = mock {
        val edgeMockList = listOf(newAddressEdge())
        on { edges } doReturn edgeMockList
    }

    private fun newAddressEdge(): Storefront.MailingAddressEdge = mock {
        val addressMock = newAddress()
        on { node } doReturn addressMock
    }

    fun newOrder(): Storefront.Order = mock {
        val addressMock = newAddress()
        val lineItemsMock = newOrderLineItemConnection()
        on { id } doReturn newID()
        on { currencyCode } doReturn newCurrencyCode()
        on { email } doReturn DEFAULT_EMAIL
        on { orderNumber } doReturn DEFAULT_ORDER_NUMBER
        on { totalPrice } doReturn DEFAULT_PRICE
        on { subtotalPrice } doReturn DEFAULT_PRICE
        on { totalShippingPrice } doReturn DEFAULT_PRICE
        on { shippingAddress } doReturn addressMock
        on { processedAt } doReturn DEFAULT_DATE
        on { lineItems } doReturn lineItemsMock
    }

    fun newOrderConnection(): Storefront.OrderConnection = mock {
        val edgeMockList = listOf(newOrderEdge())
        on { edges } doReturn edgeMockList
    }

    private fun newOrderEdge(): Storefront.OrderEdge = mock {
        val orderMock = newOrder()
        on { node } doReturn orderMock
    }

    private fun newOrderLineItemConnection(): Storefront.OrderLineItemConnection = mock {
        val edgeMock = newOrderLineItemEdge()
        on { edges } doReturn listOf(edgeMock)
    }

    private fun newOrderLineItemEdge(): Storefront.OrderLineItemEdge = mock {
        val orderLineItemMock = newOrderLineItem()
        on { node } doReturn orderLineItemMock
    }

    private fun newCurrencyCode() = DEFAULT_CURRENCY_CODE

    private fun newImageConnection(): Storefront.ImageConnection = mock {

        val imageEdge = newImageEdge()

        on { edges } doReturn listOf(imageEdge)
    }

    private fun newImageEdge(): Storefront.ImageEdge = mock {

        val image = newImage()

        on { node } doReturn image
    }
}
