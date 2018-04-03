package com.shopapp.ui.order.list

import android.content.Context
import android.os.Looper
import android.view.View
import com.nhaarman.mockito_kotlin.*
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.const.Constant
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.activity_order_list.*
import kotlinx.android.synthetic.main.layout_lce.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import kotlinx.android.synthetic.main.view_lce_empty.view.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class OrderListActivityTest {

    private lateinit var activity: OrderListActivity
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        activity = Robolectric.setupActivity(OrderListActivity::class.java)
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldSetCorrectTitle() {
        assertEquals(context.getString(R.string.my_orders), activity.toolbar.toolbarTitle.text)
    }

    @Test
    fun shouldLoadOrdersWhenOnCreate() {
        verify(activity.presenter).getOrders(Constant.DEFAULT_PER_PAGE_COUNT, null)
        assertEquals(View.VISIBLE, activity.lceLayout.loadingView.visibility)
    }

    @Test
    fun shouldShowEmptyView() {
        activity.showContent(emptyList())
        val emptyView = activity.emptyView
        assertEquals(View.VISIBLE, emptyView.visibility)
        assertEquals(context.getString(R.string.you_have_no_orders_yet), emptyView.emptyMessage.text)
        assertEquals(context.getString(R.string.start_shopping), emptyView.emptyButton.text)
        assertEquals(View.VISIBLE, emptyView.emptyButton.visibility)
        val shadowDrawable = shadowOf(emptyView.emptyImage.drawable)
        assertEquals(R.drawable.ic_orders_empty, shadowDrawable.createdFromResId)
    }

    @Test
    fun shouldShowContentView() {
        val size = 5
        val orderList = MockInstantiator.newList(MockInstantiator.newOrder(), size)
        activity.showContent(orderList)
        assertEquals(View.GONE, activity.emptyView.visibility)
        assertEquals(false, activity.swipeRefreshLayout.isRefreshing)
        assertEquals(size, activity.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldShowOrder() {
        val orderMock = MockInstantiator.newOrder()
        activity.onItemClicked(orderMock, 0)
        verify(activity.router).showOrder(activity, orderMock.id)
    }

    @Test
    fun shouldShowProduct() {
        val orderMock = MockInstantiator.newOrder()
        activity.showContent(listOf(orderMock))
        activity.onProductVariantClicked(0, 0)
        val variant = orderMock.orderProducts[0].productVariant
        assertNotNull(variant)
        verify(activity.router).showProduct(activity, variant!!)
    }

    @Test
    fun shouldNotShowProductOnEmptyOrderList() {
        activity.showContent(listOf())
        activity.onProductVariantClicked(0, 0)
        verify(activity.router, never()).showProduct(any(), any())
    }

    @Test
    fun shouldNotStartProductOnEmptyProductList() {
        val orderMock = MockInstantiator.newOrder()
        given { orderMock.orderProducts } willReturn { listOf() }
        activity.showContent(listOf(orderMock))
        activity.onProductVariantClicked(0, 0)
        verify(activity.router, never()).showProduct(any(), any())
    }

    @Test
    fun shouldShowHome() {
        activity.onEmptyButtonClicked()
        verify(activity.router).showHome(activity, true)
    }

    @After
    fun tearDown() {
        activity.finish()
    }
}