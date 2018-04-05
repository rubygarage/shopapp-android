package com.shopapp.ui.cart.router

import com.shopapp.TestShopApplication
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.cart.CartActivity
import com.shopapp.ui.checkout.CheckoutActivity
import com.shopapp.ui.home.HomeActivity
import com.shopapp.ui.product.ProductDetailsActivity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class CartRouterTest {

    private lateinit var activity: CartActivity
    private lateinit var router: CartRouter

    @Before
    fun setUpTest() {
        activity = Robolectric.setupActivity(CartActivity::class.java)
        router = CartRouter()
    }

    @Test
    fun shouldStartCheckoutActivity() {
        router.showCheckout(activity)
        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(CheckoutActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartHomeActivity() {
        router.showHome(activity, true)
        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(HomeActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartProductDetailsActivity() {
        router.showProduct(activity, MockInstantiator.newProductVariant())
        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(MockInstantiator.DEFAULT_ID, startedIntent.extras.getString(ProductDetailsActivity.EXTRA_PRODUCT_ID))
        assertNotNull(startedIntent.extras.getParcelable(ProductDetailsActivity.EXTRA_PRODUCT_VARIANT))
        assertEquals(ProductDetailsActivity::class.java, shadowIntent.intentClass)
    }
}