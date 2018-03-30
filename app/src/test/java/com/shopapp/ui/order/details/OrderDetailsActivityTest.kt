package com.shopapp.ui.order.details

import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.*
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.gateway.entity.Error
import com.shopapp.test.MockInstantiator
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import kotlinx.android.synthetic.main.view_lce_error.view.*
import kotlinx.android.synthetic.main.view_price.view.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
    private lateinit var formatterMock: NumberFormatter

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        val intent = OrderDetailsActivity.getStartIntent(context, ORDER_ID)
        activity = Robolectric.buildActivity(OrderDetailsActivity::class.java, intent).create().start().get()
        formatterMock = mock {
            on { formatPrice(any(), any()) } doAnswer { "${it.arguments[0]} ${it.arguments[1]} " }
        }
        activity.priceView.formatter = formatterMock

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
    fun shouldShowProduct() {
        val order = MockInstantiator.newOrder()
        val orderProductItem = MockInstantiator.newOrderProduct()
        val productVariant = MockInstantiator.newProductVariant()
        given(orderProductItem.productVariant).willReturn(productVariant)
        given(order.orderProducts).willReturn(listOf(orderProductItem))
        activity.showContent(order)
        activity.onItemClicked(0)
        verify(activity.router).showProduct(activity, productVariant)
    }

    @Test
    fun shouldNotShowProductWhenOrderIsNull() {
        activity.onItemClicked(0)
        assertNull(Shadows.shadowOf(activity.application).nextStartedActivity)
    }

    @Test
    fun shouldSetDefaultPrices() {
        val order = MockInstantiator.newOrder()
        given(order.subtotalPrice).willReturn(null)
        given(order.totalShippingPrice).willReturn(null)
        activity.showContent(order)
        assertEquals("0 USD ", activity.priceView.shippingValue.text.toString())
        assertEquals("0 USD ", activity.priceView.subtotalValue.text.toString())
    }

    @Test
    fun shouldShowAddressSectionWhenAddressIsNotNull() {
        val order = MockInstantiator.newOrder()
        activity.showContent(order)
        assertEquals(View.VISIBLE, activity.shippingAddressTitle.visibility)
        assertEquals(View.VISIBLE, activity.addressContentView.visibility)
    }

    @Test
    fun shouldHideAddressSectionWhenAddressIsNull() {
        val order = MockInstantiator.newOrder()
        given(order.address).willReturn(null)
        activity.showContent(order)
        assertEquals(View.GONE, activity.shippingAddressTitle.visibility)
        assertEquals(View.GONE, activity.addressContentView.visibility)
    }

    @Test
    fun shouldShowNotFoundErrorWithTargetWhenCriticalErrorReceived() {
        val target = context.getString(R.string.order)
        activity.showError(Error.Critical())
        assertEquals(View.VISIBLE, activity.lceLayout.errorView.visibility)
        assertEquals(context.getString(R.string.сould_not_find_with_placeholder, target),
            activity.lceLayout.errorView.errorMessage.text)
    }

    @After
    fun tearDown() {
        activity.finish()
    }
}