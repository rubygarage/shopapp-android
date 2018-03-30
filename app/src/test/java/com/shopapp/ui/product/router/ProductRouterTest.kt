package com.shopapp.ui.product.router

import android.app.Activity
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.SortType
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.product.ProductDetailsActivity
import com.shopapp.ui.product.ProductListActivity
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
class ProductRouterTest {

    private lateinit var router: ProductRouter
    private lateinit var activity: Activity

    @Before
    fun setUpTest() {
        router = ProductRouter()
        activity = Robolectric.setupActivity(Activity::class.java)
    }

    @Test
    fun shouldOpenProductListWhenSeeAllButtonClicked() {
        val title = "title"
        val sortType = SortType.RECENT
        val keyword = "keyword"
        val excludeKeyword = "excludeKeyword"
        router.showProductList(activity, title, sortType, keyword, excludeKeyword)

        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(title, startedIntent.extras.getString(ProductListActivity.TITLE))
        assertEquals(sortType, startedIntent.extras.getSerializable(ProductListActivity.SORT_TYPE) as SortType)
        assertEquals(keyword, startedIntent.extras.getString(ProductListActivity.KEYWORD))
        assertEquals(excludeKeyword, startedIntent.extras.getString(ProductListActivity.EXCLUDE_KEYWORD))
        assertEquals(ProductListActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldOpenProductDetailsByClick() {
        router.showProduct(activity, MockInstantiator.DEFAULT_ID)

        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(MockInstantiator.DEFAULT_ID, startedIntent.extras.getString(ProductDetailsActivity.EXTRA_PRODUCT_ID))
        assertEquals(ProductDetailsActivity::class.java, shadowIntent.intentClass)
    }
}