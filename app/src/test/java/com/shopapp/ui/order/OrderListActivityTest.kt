package com.client.shop.ui.order

import android.content.Context
import android.view.View
import com.client.MockInstantiator
import com.client.shop.R
import com.client.shop.TestShopApplication
import com.shopapp.ui.const.Constant
import com.shopapp.ui.home.HomeActivity
import com.shopapp.ui.order.details.OrderDetailsActivity
import com.shopapp.ui.order.list.OrderListActivity
import com.shopapp.ui.product.ProductDetailsActivity
import com.nhaarman.mockito_kotlin.verify
import junit.framework.Assert.assertEquals
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.activity_order_list.*
import kotlinx.android.synthetic.main.layout_lce.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import kotlinx.android.synthetic.main.view_lce_empty.view.*
import org.junit.After
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
    fun shouldStartOrderDetailsActivity() {
        val orderMock = MockInstantiator.newOrder()
        activity.onItemClicked(orderMock, 0)
        val startedIntent = shadowOf(activity).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertEquals(orderMock.id, startedIntent.extras.getString(OrderDetailsActivity.EXTRA_ORDER_ID))
        assertEquals(OrderDetailsActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartProductDetailsActivity() {
        val orderMock = MockInstantiator.newOrder()
        activity.showContent(listOf(orderMock))
        activity.onProductVariantClicked(0, 0)
        val startedIntent = shadowOf(activity).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertEquals(orderMock.orderProducts.first().productVariant.productId,
            startedIntent.extras.getString(ProductDetailsActivity.EXTRA_PRODUCT_ID))
        assertEquals(orderMock.orderProducts.first().productVariant,
            startedIntent.extras.getParcelable(ProductDetailsActivity.EXTRA_PRODUCT_VARIANT))
        assertEquals(ProductDetailsActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartHomeActivity() {
        activity.onEmptyButtonClicked()
        val startedIntent = shadowOf(activity).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertEquals(HomeActivity::class.java, shadowIntent.intentClass)
    }

    @After
    fun tearDown() {
        activity.finish()
    }
}