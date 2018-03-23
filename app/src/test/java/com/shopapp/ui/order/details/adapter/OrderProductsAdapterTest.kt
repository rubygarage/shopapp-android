package com.shopapp.ui.order.details.adapter

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.gateway.entity.OrderProduct
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.item.OrderProductItem
import com.shopapp.test.MockInstantiator
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
class OrderProductsAdapterTest {

    companion object {
        private const val SIZE = 4
    }

    @Mock
    private lateinit var dataList: List<OrderProduct>

    @Mock
    private lateinit var clickListener: OnItemClickListener

    private lateinit var adapter: OrderProductsAdapter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        given(dataList.size).willReturn(SIZE)
        adapter = OrderProductsAdapter(dataList, clickListener, MockInstantiator.DEFAULT_CURRENCY)
    }

    @Test
    fun shouldReturnCorrectCount() {
        assertEquals(SIZE, adapter.itemCount)
    }

    @Test
    fun shouldReturnCorrectType() {
        val itemView = adapter.getItemView(RuntimeEnvironment.application.applicationContext, 0)
        assertTrue(itemView is OrderProductItem)
    }

    @Test
    fun shouldCallSetOrderProduct() {
        val itemView: OrderProductItem = mock()
        val orderProduct: OrderProduct = mock()
        adapter.bindData(itemView, orderProduct, 0)
        verify(itemView).setOrderProduct(orderProduct, MockInstantiator.DEFAULT_CURRENCY)
    }
}