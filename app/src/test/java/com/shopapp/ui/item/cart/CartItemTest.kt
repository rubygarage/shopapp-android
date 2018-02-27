package com.shopapp.ui.item.cart

import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.shopapp.R
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.util.MockInstantiator
import kotlinx.android.synthetic.main.item_cart.view.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.math.BigDecimal

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CartItemTest {

    private lateinit var context: Context
    private lateinit var view: CartItem

    private val formatter: NumberFormatter = mock {
        on { formatPrice(any(), any()) } doAnswer {
            it.getArgument<BigDecimal>(0).toString() + it.getArgument<String>(1)
        }
    }

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application.baseContext
        view = CartItem(context, formatter)
        shadowOf(view).callOnAttachedToWindow()
    }

    @After
    fun tearDown() {
        shadowOf(view).callOnDetachedFromWindow()
    }

    @Test
    fun shouldSetTitle() {
        val product = MockInstantiator.newCartProduct()
        view.setCartProduct(product)
        val expected = context.resources.getString(R.string.cart_product_title, MockInstantiator.DEFAULT_TITLE, MockInstantiator.DEFAULT_TITLE)
        assertEquals(expected, view.titleText.text.toString())
    }

    @Test
    fun shouldSetQuantity() {
        val product = MockInstantiator.newCartProduct()
        view.setCartProduct(product)
        assertEquals(MockInstantiator.DEFAULT_QUANTITY.toString(), view.quantityEditText.text.toString())
        assertEquals(MockInstantiator.DEFAULT_QUANTITY.toString().length, view.quantityEditText.selectionEnd)
    }

    @Test
    fun shouldSetPrice() {
        val product = MockInstantiator.newCartProduct()
        view.setCartProduct(product)
        val expectedPrice = MockInstantiator.DEFAULT_PRICE.toInt() * MockInstantiator.DEFAULT_QUANTITY
        val expected = expectedPrice.toString() + MockInstantiator.DEFAULT_CURRENCY
        assertEquals(expected, view.totalPrice.text.toString())
    }

    @Test
    fun shouldChangePriceOnQuantityChanged() {
        val product = MockInstantiator.newCartProduct()
        view.setCartProduct(product)
        val expectedPrice = MockInstantiator.DEFAULT_PRICE.toInt() * MockInstantiator.DEFAULT_QUANTITY
        val expected = expectedPrice.toString() + MockInstantiator.DEFAULT_CURRENCY
        assertEquals(expected, view.totalPrice.text.toString())

        val newQuantity = 1
        view.quantityEditText.setText(newQuantity.toString())
        val expectedChangedPrice = MockInstantiator.DEFAULT_PRICE.toInt() * newQuantity
        val expectedChanged = expectedChangedPrice.toString() + MockInstantiator.DEFAULT_CURRENCY
        assertEquals(expectedChanged, view.totalPrice.text.toString())
    }

    @Test
    fun shouldShowEachPriceTitle() {
        val product = MockInstantiator.newCartProduct()
        view.setCartProduct(product)
        val str = MockInstantiator.DEFAULT_PRICE.toString() + MockInstantiator.DEFAULT_CURRENCY
        val expected = context.getString(R.string.each_price_pattern, str)
        assertEquals(expected, view.eachPrice.text.toString())
        assertEquals(View.VISIBLE, view.eachPrice.visibility)
    }

    @Test
    fun shouldHideEachPriceTitle() {
        val product = MockInstantiator.newCartProduct()
        given(product.quantity).willReturn(1)
        view.setCartProduct(product)
        assertEquals(View.GONE, view.eachPrice.visibility)
    }

}