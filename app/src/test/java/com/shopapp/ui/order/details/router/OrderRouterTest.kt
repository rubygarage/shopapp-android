package com.shopapp.ui.order.details.router

import com.shopapp.TestShopApplication
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.order.details.OrderDetailsActivity
import com.shopapp.ui.product.ProductDetailsActivity
import org.junit.Assert.assertEquals
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
class OrderRouterTest {

    private lateinit var activity: OrderDetailsActivity
    private lateinit var router: OrderRouter

    @Before
    fun setUpTest() {
        val context = RuntimeEnvironment.application.baseContext
        val intent = OrderDetailsActivity.getStartIntent(context, MockInstantiator.DEFAULT_ID)
        activity = Robolectric.buildActivity(OrderDetailsActivity::class.java, intent).create().start().get()
        router = OrderRouter()
    }

    @Test
    fun shouldStartProductDetailsActivity() {
        val productVariant = MockInstantiator.newProductVariant()
        router.showProduct(activity, productVariant)

        val startedIntent = Shadows.shadowOf(activity.application).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(MockInstantiator.DEFAULT_ID,
            startedIntent.extras.getString(ProductDetailsActivity.EXTRA_PRODUCT_ID))
        assertEquals(productVariant,
            startedIntent.extras.getParcelable(ProductDetailsActivity.EXTRA_PRODUCT_VARIANT))
        assertEquals(ProductDetailsActivity::class.java, shadowIntent.intentClass)
    }

}