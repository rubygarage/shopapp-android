package com.shopapp.ui.item.cart

import android.content.Context
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.mock
import com.shopapp.R
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.test.MockInstantiator
import kotlinx.android.synthetic.main.item_footer_cart.view.*
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
class CartTotalPriceTest {

    private lateinit var context: Context
    private lateinit var view: CartTotalPriceView

    private val formatter: NumberFormatter = mock {
        on { formatPrice(any(), any()) } doAnswer {
            it.getArgument<BigDecimal>(0).toString() + it.getArgument<String>(1)
        }
    }

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application.baseContext
        view = CartTotalPriceView(context)
        shadowOf(view).callOnAttachedToWindow()
    }

    @Test
    fun shouldSetTitle() {
        val size = 2
        val list = MockInstantiator.newList(MockInstantiator.newCartProduct(), size)
        view.setData(list, formatter)
        val expected = context.resources.getQuantityString(R.plurals.order_total_plurals, size, size)
        assertEquals(expected, view.orderTotalLabel.text.toString())
    }

    @Test
    fun shouldSetPrice() {
        val size = 2
        val list = MockInstantiator.newList(MockInstantiator.newCartProduct(), size)
        view.setData(list, formatter)

        val expectedPrice = size * MockInstantiator.DEFAULT_PRICE.toInt() * MockInstantiator.DEFAULT_QUANTITY
        val expected = expectedPrice.toString() + MockInstantiator.DEFAULT_CURRENCY
        assertEquals(expected, view.orderTotalValue.text.toString())
    }

    @Test
    fun shouldSetEmptyCurrencyWhenDataListIsEmpty() {
        view.setData(listOf(), formatter)
        assertEquals(0.toString(), view.orderTotalValue.text)
    }

    @After
    fun tearDown() {
        shadowOf(view).callOnDetachedFromWindow()
    }
}