package com.shopapp.ui.product.adapter

import android.widget.FrameLayout
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.gateway.entity.Product
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.item.ProductItem
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
class ProductListAdapterTest {

    companion object {
        private const val SIZE = 5
        private const val ITEM_WIDTH = 100
        private const val ITEM_HEIGHT = 200
    }

    @Mock
    private lateinit var dataList: List<Product>

    @Mock
    private lateinit var clickListener: OnItemClickListener

    private lateinit var adapter: ProductListAdapter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        given(dataList.size).willReturn(SIZE)
        adapter = ProductListAdapter(ITEM_WIDTH, ITEM_HEIGHT, dataList, clickListener)
    }

    @Test
    fun shouldReturnCorrectCount() {
        assertEquals(SIZE, adapter.itemCount)
    }

    @Test
    fun shouldCallSetProduct() {
        val itemView: ProductItem = mock()
        val product: Product = mock()
        adapter.bindData(itemView, product, 0)
        verify(itemView).setProduct(product)
    }

    @Test
    fun shouldSetCorrectSize() {
        val context = RuntimeEnvironment.application.baseContext
        val viewHolder = adapter.createViewHolder(FrameLayout(context), adapter.getItemViewType(0))
        assertEquals(ITEM_WIDTH, viewHolder.itemView.layoutParams.width)
        assertEquals(ITEM_HEIGHT, viewHolder.itemView.layoutParams.height)
    }
}