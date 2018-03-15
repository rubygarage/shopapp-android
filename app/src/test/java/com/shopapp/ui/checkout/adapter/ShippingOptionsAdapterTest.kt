package com.shopapp.ui.checkout.adapter

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.gateway.entity.ShippingRate
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.item.ShippingOptionItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
class ShippingOptionsAdapterTest {

    companion object {
        private const val SIZE = 4
    }

    @Mock
    private lateinit var dataList: MutableList<ShippingRate>

    @Mock
    private lateinit var formatter: NumberFormatter

    private lateinit var adapter: ShippingOptionsAdapter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        given(dataList.size).willReturn(SIZE)
        adapter = ShippingOptionsAdapter(dataList, formatter)
    }

    @Test
    fun shouldReturnCorrectCount() {
        assertEquals(SIZE, adapter.itemCount)
    }

    @Test
    fun shouldCallSetData() {
        val itemView: ShippingOptionItem = mock()
        val checkout = MockInstantiator.newCheckout()
        val shippingRate: ShippingRate = checkout.shippingRate!!
        adapter.setData(checkout, listOf(shippingRate))
        adapter.bindData(itemView, shippingRate, 0)
        verify(itemView).setData(shippingRate, shippingRate, checkout.currency, formatter)
    }

    @Test
    fun shouldNotCallSetDataOnNullCurrency() {
        val itemView: ShippingOptionItem = mock()
        val checkout = MockInstantiator.newCheckout()
        val shippingRate: ShippingRate = checkout.shippingRate!!
        adapter.bindData(itemView, shippingRate, 0)
        verify(itemView, never()).setData(shippingRate, shippingRate, checkout.currency, formatter)
    }

    @Test
    fun shouldReturnCorrectItemView() {
        val view = adapter.getItemView(RuntimeEnvironment.application.baseContext, 0)
        assertTrue(view is ShippingOptionItem)
    }
}