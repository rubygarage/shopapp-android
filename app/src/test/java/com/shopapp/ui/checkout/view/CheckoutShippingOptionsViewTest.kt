package com.shopapp.ui.checkout.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import com.nhaarman.mockito_kotlin.mock
import com.shopapp.R
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.checkout.adapter.ShippingOptionsAdapter
import kotlinx.android.synthetic.main.view_checkout_shipping_options.view.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CheckoutShippingOptionsViewTest {

    private lateinit var context: Context
    private lateinit var view: CheckoutShippingOptionsView

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        view = CheckoutShippingOptionsView(context)
    }

    @Test
    fun shouldShowDefaultMessageWhenDataDoesNotSet() {
        assertEquals(ContextCompat.getColor(context, R.color.textColorSecondary), view.title.currentTextColor)
        assertEquals(context.getString(R.string.please_add_shipping_address_first), view.message.text)
        assertEquals(View.VISIBLE, view.message.visibility)
        assertEquals(View.GONE, view.recyclerView.visibility)
    }

    @Test
    fun shouldShowMessageWhenShippingRateListIsEmpty() {
        val checkout = MockInstantiator.newCheckout()

        view.setData(checkout, emptyList())
        assertEquals(ContextCompat.getColor(context, R.color.textColorSecondary), view.title.currentTextColor)
        assertEquals(context.getString(R.string.order_can_not_be_processed), view.message.text)
        assertEquals(View.VISIBLE, view.message.visibility)
        assertEquals(View.GONE, view.recyclerView.visibility)
    }

    @Test
    fun shouldShowListWhenShippingRateListIsNotEmpty() {
        val size = 5
        val checkout = MockInstantiator.newCheckout()
        val shippingRates = MockInstantiator.newList(MockInstantiator.newShippingRate(), size)

        view.setData(checkout, shippingRates)
        assertEquals(ContextCompat.getColor(context, R.color.textColorPrimary), view.title.currentTextColor)
        assertEquals(View.GONE, view.message.visibility)
        assertEquals(View.VISIBLE, view.recyclerView.visibility)
        assertEquals(size, view.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldShowDefaultMessageWhenUnSelectAddress() {
        val size = 5
        val checkout = MockInstantiator.newCheckout()
        val shippingRates = MockInstantiator.newList(MockInstantiator.newShippingRate(), size)

        view.setData(checkout, shippingRates)
        assertEquals(ContextCompat.getColor(context, R.color.textColorPrimary), view.title.currentTextColor)
        assertEquals(View.GONE, view.message.visibility)
        assertEquals(View.VISIBLE, view.recyclerView.visibility)
        assertEquals(size, view.recyclerView.adapter.itemCount)

        view.unSelectAddress()
        assertEquals(ContextCompat.getColor(context, R.color.textColorSecondary), view.title.currentTextColor)
        assertEquals(context.getString(R.string.please_add_shipping_address_first), view.message.text)
        assertEquals(View.VISIBLE, view.message.visibility)
        assertEquals(View.GONE, view.recyclerView.visibility)
    }

    @Test
    fun shouldSelectShippingRate() {
        val shippingRate = MockInstantiator.newShippingRate()
        val adapter = view.recyclerView.adapter as ShippingOptionsAdapter

        view.setData(mock(), listOf())
        assertNull(adapter.shippingRate)
        view.selectShippingRate(shippingRate)
        assertEquals(shippingRate, adapter.shippingRate)
    }

    @Test
    fun shouldSetListenerToRecyclerViewAdapter() {
        val onOptionSelectedListener: CheckoutShippingOptionsView.OnOptionSelectedListener? = mock()
        view.onOptionSelectedListener = onOptionSelectedListener
        assertNotNull(view.recyclerView.adapter)
        assertTrue(view.recyclerView.adapter is ShippingOptionsAdapter)
        val adapter = view.recyclerView.adapter as ShippingOptionsAdapter
        assertEquals(onOptionSelectedListener, adapter.onOptionSelectedListener)
    }
}