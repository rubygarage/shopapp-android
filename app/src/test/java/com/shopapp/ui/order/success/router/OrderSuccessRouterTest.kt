package com.shopapp.ui.order.success.router

import android.app.Activity
import com.shopapp.TestShopApplication
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.order.details.OrderDetailsActivity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class OrderSuccessRouterTest {

    private lateinit var activity: Activity
    private lateinit var router: OrderSuccessRouter

    @Before
    fun setUpTest() {
        activity = Robolectric.setupActivity(Activity::class.java)
        router = OrderSuccessRouter()
    }

    @Test
    fun shouldStartOrderDetailsActivityWhenViewOrderButtonClicked() {
        router.showOrder(activity, MockInstantiator.DEFAULT_ID)

        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(MockInstantiator.DEFAULT_ID, startedIntent.extras.getString(OrderDetailsActivity.EXTRA_ORDER_ID))
        assertEquals(OrderDetailsActivity::class.java, shadowIntent.intentClass)
    }
}