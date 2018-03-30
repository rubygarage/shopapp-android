package com.shopapp.ui.cart

import android.app.Activity
import android.view.View
import com.shopapp.TestShopApplication
import com.shopapp.test.RxImmediateSchedulerRule
import kotlinx.android.synthetic.main.widget_cart.view.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class CartWidgetTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var activity: Activity

    private lateinit var widget: CartWidget

    @Before
    fun setUp() {
        activity = Robolectric.setupActivity(Activity::class.java)
        widget = CartWidget(activity)
    }

    @Test
    fun shouldShowBadge() {
        widget.changeBadgeCount(5)
        assertEquals(View.VISIBLE, widget.badge.visibility)
        assertEquals("5", widget.badge.text.toString())
    }

    @Test
    fun shouldHideBadge() {
        widget.changeBadgeCount(0)
        assertEquals(View.INVISIBLE, widget.badge.visibility)
    }

    @Test
    fun shouldShowCart() {
        widget.image.performClick()
        verify(widget.router).showCart(widget.context)
    }
}