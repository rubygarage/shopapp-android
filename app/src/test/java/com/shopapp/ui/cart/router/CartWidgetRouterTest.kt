package com.shopapp.ui.cart.router

import android.app.Activity
import com.shopapp.TestShopApplication
import com.shopapp.ui.cart.CartActivity
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
class CartWidgetRouterTest {

    private lateinit var activity: Activity
    private lateinit var router: CartWidgetRouter

    @Before
    fun setUpTest() {
        activity = Robolectric.setupActivity(Activity::class.java)
        router = CartWidgetRouter()
    }

    @Test
    fun shouldStartCartActivity() {
        router.showCart(activity)
        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(CartActivity::class.java, shadowIntent.intentClass)
    }
}