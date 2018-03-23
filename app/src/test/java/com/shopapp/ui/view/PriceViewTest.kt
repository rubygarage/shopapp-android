package com.shopapp.ui.view

import android.content.Context
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.test.MockInstantiator
import kotlinx.android.synthetic.main.view_price.view.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class PriceViewTest {

    private lateinit var context: Context
    private lateinit var itemView: PriceView

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        itemView = PriceView(context)
    }

    @Test
    fun shouldDisplayPrices() {
        val formatter = NumberFormatter()
        val subtotalPrice = (10).toBigDecimal()
        val discountPrice = (20).toBigDecimal()
        val shippingPrice = (30).toBigDecimal()
        val totalPrice = (40).toBigDecimal()
        itemView.setData(subtotalPrice, discountPrice, shippingPrice, totalPrice,
            MockInstantiator.DEFAULT_CURRENCY)

        assertEquals(formatter.formatPrice(subtotalPrice, MockInstantiator.DEFAULT_CURRENCY),
            itemView.subtotalValue.text)
        assertEquals(formatter.formatPrice(discountPrice, MockInstantiator.DEFAULT_CURRENCY),
            itemView.discountValue.text)
        assertEquals(formatter.formatPrice(shippingPrice, MockInstantiator.DEFAULT_CURRENCY),
            itemView.shippingValue.text)
        assertEquals(formatter.formatPrice(totalPrice, MockInstantiator.DEFAULT_CURRENCY),
            itemView.totalValue.text)
    }
}