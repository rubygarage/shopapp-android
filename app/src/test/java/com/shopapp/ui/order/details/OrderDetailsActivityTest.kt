package com.shopapp.ui.order.details

import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.ui.product.ProductDetailsActivity
import com.shopapp.util.MockInstantiator
import junit.framework.Assert.assertEquals
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class OrderDetailsActivityTest {

    companion object {
        private const val ORDER_ID = "order_id"
    }

    private lateinit var activity: OrderDetailsActivity
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        val intent = OrderDetailsActivity.getStartIntent(context, ORDER_ID)
        activity = Robolectric.buildActivity(OrderDetailsActivity::class.java, intent).create().start().get()
    }

    @Test
    fun shouldSetCorrectTitle() {
        assertEquals(context.getString(R.string.order_details), activity.toolbar.toolbarTitle.text)
    }

    @Test
    fun shouldLoadOrderWhenOnCreate() {
        verify(activity.presenter).loadOrderDetails(ORDER_ID)
        assertEquals(View.VISIBLE, activity.lceLayout.loadingView.visibility)
    }

    @Test
    fun shouldShowContentView() {
        val size = 5
        val order = MockInstantiator.newOrder()
        val list = MockInstantiator.newList(MockInstantiator.newOrderProduct(), size)
        given(order.orderProducts).willReturn(list)
        activity.showContent(order)
        assertEquals(size, activity.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldStartProductDetailsActivity() {
        val productId = "product_id"
        val order = MockInstantiator.newOrder()
        val orderProductItem = MockInstantiator.newOrderProduct()
        val productVariant = MockInstantiator.newProductVariant()
        given(productVariant.productId).willReturn(productId)
        given(orderProductItem.productVariant).willReturn(productVariant)
        given(order.orderProducts).willReturn(listOf(orderProductItem))
        activity.showContent(order)
        activity.onItemClicked(0)
        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(productId,
            startedIntent.extras.getString(ProductDetailsActivity.EXTRA_PRODUCT_ID))
        assertEquals(productVariant,
            startedIntent.extras.getParcelable(ProductDetailsActivity.EXTRA_PRODUCT_VARIANT))
        assertEquals(ProductDetailsActivity::class.java, shadowIntent.intentClass)
    }

    @After
    fun tearDown() {
        activity.finish()
    }
}