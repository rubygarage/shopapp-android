package com.shopapp.ui.order.list.router

import com.shopapp.TestShopApplication
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.home.HomeActivity
import com.shopapp.ui.order.details.OrderDetailsActivity
import com.shopapp.ui.order.list.OrderListActivity
import com.shopapp.ui.product.ProductDetailsActivity
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
class OrderListRouterTest {

    private lateinit var activity: OrderListActivity
    private lateinit var router: OrderListRouter

    @Before
    fun setUpTest() {
        activity = Robolectric.setupActivity(OrderListActivity::class.java)
        router = OrderListRouter()
    }

    @Test
    fun shouldStartProductDetailsActivity() {
        val productVariant = MockInstantiator.newProductVariant()
        router.showProduct(activity, productVariant)

        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(productVariant.productId, startedIntent.extras.getString(ProductDetailsActivity.EXTRA_PRODUCT_ID))
        assertEquals(productVariant, startedIntent.extras.getParcelable(ProductDetailsActivity.EXTRA_PRODUCT_VARIANT))
        assertEquals(ProductDetailsActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartHomeActivity() {
        router.showHome(activity, true)
        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(HomeActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartOrderDetailsActivity() {
        router.showOrder(activity, MockInstantiator.DEFAULT_ID)

        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(MockInstantiator.DEFAULT_ID, startedIntent.extras.getString(OrderDetailsActivity.EXTRA_ORDER_ID))
        assertEquals(OrderDetailsActivity::class.java, shadowIntent.intentClass)
    }
}