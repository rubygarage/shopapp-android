package com.shopapp.test

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.shopapp.gateway.entity.*
import com.shopapp.ui.const.Constant
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
    const val DEFAULT_COUNTRY_NAME = "default_country"
    const val DEFAULT_STATE = "default_state"
    const val DEFAULT_FIRST_NAME = "default_first_name"
    const val DEFAULT_LAST_NAME = "default_last_name"
    const val DEFAULT_ZIP = "default_zip"
    const val DEFAULT_PHONE = "default_phone"
    const val DEFAULT_BIO = "default_bio"
    const val DEFAULT_CONTENT_HTML = "<html><head></head><body>default_content_html</body></html>"
    const val DEFAULT_URL = "default_url"
    const val DEFAULT_VENDOR = "default_vendor"
    const val DEFAULT_TYPE = "default_type"
    const val DEFAULT_CARD_NUMBER = "4242424242424242"
    const val DEFAULT_MONTH = "07"
    const val DEFAULT_YEAR = "2020"
    const val DEFAULT_CVV = "123"
    const val DEFAULT_CODE = "default_code"
    const val DEFAULT_NUMBER_ID = 123L
    val DEFAULT_PRICE = BigDecimal.TEN
    val DEFAULT_DATE = Date()

    fun <T> newList(item: T, size: Int = Constant.DEFAULT_PER_PAGE_COUNT): List<T> {
        val list: MutableList<T> = mutableListOf()
        repeat(size) {
            list.add(item)
        }
        return list
    }

    fun newOrder(): Order = mock {

        val orderProductListMock = newList(newOrderProduct())
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

    fun newProductOption(): ProductOption = mock {
        on { id } doReturn DEFAULT_ID
        on { name } doReturn DEFAULT_NAME
        on { values } doReturn listOf(DEFAULT_VALUE)
    }

    fun newCartProduct(): CartProduct = mock {
        val productVariantMock = newProductVariant()

        on { title } doReturn DEFAULT_TITLE
        on { productVariant } doReturn productVariantMock
        on { quantity } doReturn DEFAULT_QUANTITY
        on { currency } doReturn DEFAULT_CURRENCY
    }

    fun newProduct(): Product = mock {

        val imagesMock = newList(newImage())
        val optionsMock = newList(newProductOption())
        val variantsMock = newList(newProductVariant())

        on { id } doReturn DEFAULT_ID
        on { title } doReturn DEFAULT_TITLE
        on { productDescription } doReturn DEFAULT_DESCRIPTION
        on { additionalDescription } doReturn DEFAULT_DESCRIPTION
        on { currency } doReturn DEFAULT_CURRENCY
        on { price } doReturn DEFAULT_PRICE
        on { hasAlternativePrice } doReturn true
        on { discount } doReturn ""
        on { vendor } doReturn DEFAULT_VENDOR
        on { type } doReturn DEFAULT_TYPE
        on { createdAt } doReturn DEFAULT_DATE
        on { updatedAt } doReturn DEFAULT_DATE
        on { tags } doReturn listOf<String>()
        on { images } doReturn imagesMock
        on { options } doReturn optionsMock
        on { variants } doReturn variantsMock
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
        on { country } doReturn DEFAULT_COUNTRY_NAME
        on { state } doReturn DEFAULT_STATE
        on { firstName } doReturn DEFAULT_FIRST_NAME
        on { lastName } doReturn DEFAULT_LAST_NAME
        on { zip } doReturn DEFAULT_ZIP
        on { phone } doReturn DEFAULT_PHONE
    }

    fun newArticle(): Article = mock {

        val imageMock = newImage()
        val authorMock = newAuthor()
        val tagsMock: List<String> = mock()

        on { id } doReturn DEFAULT_ID
        on { title } doReturn DEFAULT_TITLE
        on { content } doReturn DEFAULT_DESCRIPTION
        on { contentHTML } doReturn DEFAULT_CONTENT_HTML
        on { image } doReturn imageMock
        on { author } doReturn authorMock
        on { tags } doReturn tagsMock
        on { blogId } doReturn DEFAULT_ID
        on { blogTitle } doReturn DEFAULT_TITLE
        on { publishedAt } doReturn DEFAULT_DATE
        on { url } doReturn DEFAULT_URL
        on { paginationValue } doReturn DEFAULT_PAGINATION_VALUE
    }

    fun newAuthor(): Author = mock {
        on { firstName } doReturn DEFAULT_NAME
        on { lastName } doReturn DEFAULT_NAME
        on { fullName } doReturn DEFAULT_NAME
        on { email } doReturn DEFAULT_EMAIL
        on { bio } doReturn DEFAULT_BIO
    }

    fun newCustomer(): Customer = mock {

        val addressesMock = newList(newAddress(), 5)
        val defaultAddressMock = newAddress()

        on { id } doReturn DEFAULT_ID
        on { email } doReturn DEFAULT_EMAIL
        on { firstName } doReturn DEFAULT_FIRST_NAME
        on { lastName } doReturn DEFAULT_LAST_NAME
        on { phone } doReturn DEFAULT_PHONE
        on { isAcceptsMarketing } doReturn true
        on { addressList } doReturn addressesMock
        on { defaultAddress } doReturn defaultAddressMock
    }

    fun newShippingRate(): ShippingRate = mock {
        on { title } doReturn DEFAULT_TITLE
        on { price } doReturn DEFAULT_PRICE
        on { handle } doReturn ""
    }

    fun newCheckout(): Checkout = mock {

        val addressMock = newAddress()
        val shippingRateMock = newShippingRate()

        on { checkoutId } doReturn DEFAULT_ID
        on { webUrl } doReturn ""
        on { requiresShipping } doReturn true
        on { subtotalPrice } doReturn DEFAULT_PRICE
        on { totalPrice } doReturn DEFAULT_PRICE
        on { taxPrice } doReturn DEFAULT_PRICE
        on { currency } doReturn DEFAULT_CURRENCY
        on { address } doReturn addressMock
        on { shippingRate } doReturn shippingRateMock
    }

    fun newCategory(): Category = mock {
        val imageMock = newImage()
        val productListMock = newList(newProduct(), Constant.DEFAULT_PER_PAGE_COUNT)

        on { id } doReturn DEFAULT_ID
        on { title } doReturn DEFAULT_TITLE
        on { categoryDescription } doReturn DEFAULT_DESCRIPTION
        on { additionalDescription } doReturn DEFAULT_DESCRIPTION
        on { image } doReturn imageMock
        on { updatedAt } doReturn DEFAULT_DATE
        on { productList } doReturn productListMock
        on { paginationValue } doReturn DEFAULT_PAGINATION_VALUE
    }

    fun newCard(): Card = mock {
        on { firstName } doReturn DEFAULT_FIRST_NAME
        on { lastName } doReturn DEFAULT_LAST_NAME
        on { cardNumber } doReturn DEFAULT_CARD_NUMBER
        on { expireMonth } doReturn DEFAULT_MONTH
        on { expireYear } doReturn DEFAULT_YEAR
        on { verificationCode } doReturn DEFAULT_CVV
    }

    fun newPolicy(): Policy = mock {
        on { title } doReturn DEFAULT_TITLE
        on { body } doReturn DEFAULT_DESCRIPTION
        on { url } doReturn DEFAULT_URL
    }

    fun newCountry(): Country = mock {

        val statesMock = newList(newState(), 5)

        on { id } doReturn DEFAULT_NUMBER_ID
        on { code } doReturn DEFAULT_CODE
        on { name } doReturn DEFAULT_COUNTRY_NAME
        on { states } doReturn statesMock
    }

    fun newState(): State = mock {
        on { id } doReturn DEFAULT_NUMBER_ID
        on { countryId } doReturn DEFAULT_NUMBER_ID
        on { code } doReturn DEFAULT_CODE
        on { name } doReturn DEFAULT_NAME
    }

    fun newShop(): Shop = mock {

        val policy = newPolicy()

        on { name } doReturn DEFAULT_NAME
        on { description } doReturn DEFAULT_DESCRIPTION
        on { privacyPolicy } doReturn policy
        on { refundPolicy } doReturn policy
        on { termsOfService } doReturn policy
    }

}