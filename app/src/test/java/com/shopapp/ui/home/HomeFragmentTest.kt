package com.shopapp.ui.home

import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.ui.blog.BlogFragment
import com.shopapp.ui.product.ProductHorizontalFragment
import com.shopapp.ui.product.ProductPopularFragment
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentController

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class HomeFragmentTest {

    private lateinit var fragment: HomeFragment

    @Before
    fun setUp() {
        fragment = SupportFragmentController.of(HomeFragment())
            .create()
            .resume()
            .visible()
            .get()
    }

    @Test
    fun shouldShowAccountScreen() {
        assertTrue(fragment.childFragmentManager.findFragmentById(R.id.recentContainer) is ProductHorizontalFragment)
    }

    @Test
    fun shouldShowSearchScreen() {
        assertTrue(fragment.childFragmentManager.findFragmentById(R.id.popularContainer) is ProductPopularFragment)
    }

    @Test
    fun shouldShowHomeScreen() {
        assertTrue(fragment.childFragmentManager.findFragmentById(R.id.blogContainer) is BlogFragment)
    }
}