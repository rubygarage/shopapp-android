package com.shopapp.ui.item

import android.content.Context
import com.shopapp.R
import com.shopapp.domain.formatter.DateFormatter
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.test.MockInstantiator
import kotlinx.android.synthetic.main.item_order.view.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class OrderItemTest {

    private val dateFormatter = DateFormatter()
    private val numberFormatter = NumberFormatter()
    private lateinit var context: Context
    private lateinit var itemView: OrderItem

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        itemView = OrderItem(context, dateFormatter, numberFormatter)
    }

    @Test
    fun shouldDisplayOrderData() {
        val order = MockInstantiator.newOrder()
        itemView.setOrder(order)
        assertEquals(order.orderProducts.size.toString(), itemView.itemsCountValueTextView.text)
        assertEquals(context.resources.getString(R.string.total_price_pattern,
            numberFormatter.formatPrice(order.totalPrice, order.currency)), itemView.totalPriceTextView.text)
        assertEquals(order.orderProducts.size, itemView.productVariantsRecyclerView.adapter.itemCount)
    }
}