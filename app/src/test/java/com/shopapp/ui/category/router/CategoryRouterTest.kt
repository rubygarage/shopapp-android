package com.shopapp.ui.category.router

import com.shopapp.TestShopApplication
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.category.CategoryActivity
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
class CategoryRouterTest {

    private lateinit var router: CategoryRouter
    private lateinit var activity: CategoryActivity

    @Before
    fun setUpTest() {
        router = CategoryRouter()
        val context = RuntimeEnvironment.application.applicationContext
        val intent = CategoryActivity.getStartIntent(context, MockInstantiator.newCategory())
        activity = Robolectric.buildActivity(CategoryActivity::class.java, intent)
            .create()
            .resume()
            .visible()
            .get()
    }

    @Test
    fun shouldStartProductDetailsActivity() {
        val productMock = MockInstantiator.newProduct()
        router.showProduct(activity, productMock.id)

        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(productMock.id, startedIntent.extras.getString(ProductDetailsActivity.EXTRA_PRODUCT_ID))
        assertEquals(ProductDetailsActivity::class.java, shadowIntent.intentClass)
    }
}