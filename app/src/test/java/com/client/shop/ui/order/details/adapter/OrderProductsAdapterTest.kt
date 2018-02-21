package com.client.shop.ui.order.details.adapter

import com.client.MockInstantiator
import com.client.shop.gateway.entity.OrderProduct
import com.client.shop.ui.base.recycler.OnItemClickListener
import com.client.shop.ui.item.OrderProductItem
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