package com.shopapp.ui.order.success

import android.content.Context
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import kotlinx.android.synthetic.main.activity_order_success.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class OrderSuccessActivityTest {

    companion object {
        private const val ORDER_ID = "order_id"
        private const val ORDER_NUMBER = 15
    }

    private lateinit var activity: OrderSuccessActivity
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        val intent = OrderSuccessActivity.getStartIntent(context, ORDER_ID, ORDER_NUMBER)
        activity = Robolectric.buildActivity(OrderSuccessActivity::class.java, intent).create().start().get()
    }

    @Test
    fun shouldSetCorrectData() {
        assertEquals(context.getString(R.string.shop), activity.toolbar.toolbarTitle.text)
        assertEquals(ORDER_NUMBER.toString(), activity.orderNumberValue.text)
    }

    @Test
    fun shouldShowOrder() {
        activity.viewOrderButton.performClick()
        verify(activity.router).showOrder(activity, ORDER_ID)
    }

    @Test
    fun shouldFinishWhenContinueShippingClicked() {
        activity.continueShipping.performClick()
        assertTrue(activity.isFinishing)
    }

    @After
    fun tearDown() {
        activity.finish()
    }
}