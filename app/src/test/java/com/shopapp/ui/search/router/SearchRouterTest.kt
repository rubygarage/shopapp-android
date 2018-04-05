package com.shopapp.ui.search.router

import android.app.Activity
import com.shopapp.TestShopApplication
import com.shopapp.test.MockInstantiator
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
class SearchRouterTest {

    private lateinit var router: SearchRouter
    private lateinit var activity: Activity

    @Before
    fun setUpTest() {
        router = SearchRouter()
        activity = Robolectric.setupActivity(Activity::class.java)
    }

    @Test
    fun shouldStartProductActivity() {
        router.showProduct(activity, MockInstantiator.DEFAULT_ID)

        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(MockInstantiator.DEFAULT_ID, startedIntent.extras.getString(ProductDetailsActivity.EXTRA_PRODUCT_ID))
        assertEquals(ProductDetailsActivity::class.java, shadowIntent.intentClass)
    }

}