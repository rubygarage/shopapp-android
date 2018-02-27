package com.shopapp.ui.cart.adapter

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.gateway.entity.CartProduct
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.item.OrderItem
import com.shopapp.ui.item.cart.CartItem
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
class CartAdapterTest {

    companion object {
        private const val SIZE = 4
    }

    @Mock
    private lateinit var dataList: List<CartProduct>

    @Mock
    private lateinit var clickListener: OnItemClickListener

    private lateinit var adapter: CartAdapter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        given(dataList.size).willReturn(SIZE)
        adapter = CartAdapter(dataList, clickListener)
    }

    @Test
    fun shouldReturnCorrectCount() {
        assertEquals(SIZE, adapter.itemCount)
    }

    @Test
    fun shouldReturnCorrectType() {
        val itemView = adapter.getItemView(RuntimeEnvironment.application.applicationContext, 0)
        assertTrue(itemView is CartItem)
    }

    @Test
    fun shouldCallSetOrder() {
        val itemView: CartItem = mock()
        val product: CartProduct = mock()
        adapter.bindData(itemView, product, 0)
        verify(itemView).setCartProduct(product)
    }
}