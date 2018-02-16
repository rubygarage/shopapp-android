package com.client.shop.ui.order.list.adapter

import com.client.shop.getaway.entity.Order
import com.client.shop.ui.item.OrderItem
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
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
class OrderListAdapterTest {

    companion object {
        private const val SIZE = 4
    }

    @Mock
    private lateinit var dataList: List<Order>

    @Mock
    private lateinit var clickListener: OrderListAdapter.OnOrderItemClickListener

    private lateinit var adapter: OrderListAdapter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        given(dataList.size).willReturn(SIZE)
        adapter = OrderListAdapter(dataList, clickListener)
    }

    @Test
    fun shouldReturnCorrectCount() {
        assertEquals(SIZE, adapter.itemCount)
    }

    @Test
    fun shouldReturnCorrectType() {
        val itemView = adapter.getItemView(RuntimeEnvironment.application.applicationContext, 0)
        assertTrue(itemView is OrderItem)
    }

    @Test
    fun shouldCallSetOrder() {
        val itemView: OrderItem = mock()
        val order: Order = mock()
        adapter.bindData(itemView, order, 0)
        verify(itemView).setOrder(order)
    }
}