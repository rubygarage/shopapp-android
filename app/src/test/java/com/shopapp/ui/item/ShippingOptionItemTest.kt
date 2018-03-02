package com.shopapp.ui.item

import android.content.Context
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.checkout.view.CheckoutShippingOptionsView
import kotlinx.android.synthetic.main.item_shipping_option.view.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ShippingOptionItemTest {

    companion object {
        private const val TEST_PRICE = "test_price"
    }

    @Mock
    private lateinit var onOptionSelectedListener: CheckoutShippingOptionsView.OnOptionSelectedListener

    @Mock
    private lateinit var formatter: NumberFormatter

    private lateinit var context: Context
    private lateinit var itemView: ShippingOptionItem

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        context = RuntimeEnvironment.application.baseContext
        itemView = ShippingOptionItem(context, onOptionSelectedListener)
        given(formatter.formatPrice(any(), any())).willReturn(TEST_PRICE)
    }

    @Test
    fun shouldShowUnCheckedData() {
        val shippingRate = MockInstantiator.newShippingRate()
        val selectedShippingRate = MockInstantiator.newShippingRate()
        itemView.setData(shippingRate, selectedShippingRate, MockInstantiator.DEFAULT_CURRENCY, formatter)
        assertEquals(false, itemView.radioButton.isChecked)
        assertEquals(TEST_PRICE, itemView.price.text)
        assertEquals(shippingRate.title, itemView.name.text)
    }

    @Test
    fun shouldShowCheckedData() {
        val shippingRate = MockInstantiator.newShippingRate()
        itemView.setData(shippingRate, shippingRate, MockInstantiator.DEFAULT_CURRENCY, formatter)
        assertEquals(true, itemView.radioButton.isChecked)
        assertEquals(TEST_PRICE, itemView.price.text)
        assertEquals(shippingRate.title, itemView.name.text)
    }

    @Test
    fun shouldCallListenerOnce() {
        val shippingRate = MockInstantiator.newShippingRate()
        itemView.setData(shippingRate, shippingRate, MockInstantiator.DEFAULT_CURRENCY, formatter)
        itemView.radioButton.isChecked = false
        itemView.radioButton.isChecked = true

        verify(onOptionSelectedListener).onOptionSelected(shippingRate)
    }
}