package com.shopapp.ui.home

import android.view.View
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.anyOrNull
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.ui.blog.BlogFragment
import com.shopapp.ui.product.ProductHorizontalFragment
import com.shopapp.ui.product.ProductPopularFragment
import kotlinx.android.synthetic.main.fragment_home.*
import org.junit.Assert.*
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

    @Test
    fun shouldShowRecentContainerWhenCallChangeVisibilityWithVisibleTrue() {
        val childFragment = fragment.childFragmentManager.findFragmentById(R.id.recentContainer) as ProductHorizontalFragment
        childFragment.visibilityListener?.changeVisibility(true)
        assertEquals(View.VISIBLE, fragment.recentContainer.visibility)
    }

    @Test
    fun shouldHideRecentContainerWhenCallChangeVisibilityWithVisibleFalse() {
        val childFragment = fragment.childFragmentManager.findFragmentById(R.id.recentContainer) as ProductHorizontalFragment
        childFragment.visibilityListener?.changeVisibility(false)
        assertEquals(View.GONE, fragment.recentContainer.visibility)
    }

    @Test
    fun shouldShowPopularContainerWhenCallChangeVisibilityWithVisibleTrue() {
        val childFragment = fragment.childFragmentManager.findFragmentById(R.id.popularContainer) as ProductPopularFragment
        childFragment.visibilityListener?.changeVisibility(true)
        assertEquals(View.VISIBLE, fragment.popularContainer.visibility)
    }

    @Test
    fun shouldHidePopularContainerWhenCallChangeVisibilityWithVisibleFalse() {
        val childFragment = fragment.childFragmentManager.findFragmentById(R.id.popularContainer) as ProductPopularFragment
        childFragment.visibilityListener?.changeVisibility(false)
        assertEquals(View.GONE, fragment.popularContainer.visibility)
    }

    @Test
    fun shouldShowBlogContainerWhenCallChangeVisibilityWithVisibleTrue() {
        val childFragment = fragment.childFragmentManager.findFragmentById(R.id.blogContainer) as BlogFragment
        childFragment.visibilityListener?.changeVisibility(true)
        assertEquals(View.VISIBLE, fragment.blogContainer.visibility)
    }

    @Test
    fun shouldHideBlogContainerWhenCallChangeVisibilityWithVisibleFalse() {
        val childFragment = fragment.childFragmentManager.findFragmentById(R.id.blogContainer) as BlogFragment
        childFragment.visibilityListener?.changeVisibility(false)
        assertEquals(View.GONE, fragment.blogContainer.visibility)
    }

    @Test
    fun shouldStopRefreshAndStartLoadDataWhenOnRefresh() {
        val productHorizontalFragment = fragment.childFragmentManager.findFragmentById(R.id.recentContainer) as ProductHorizontalFragment
        val productPopularFragment = fragment.childFragmentManager.findFragmentById(R.id.popularContainer) as ProductPopularFragment
        val blogFragment = fragment.childFragmentManager.findFragmentById(R.id.blogContainer) as BlogFragment

        fragment.refreshLayout.isRefreshing = true
        assertTrue(fragment.refreshLayout.isRefreshing)

        fragment.onRefresh()
        assertFalse(fragment.refreshLayout.isRefreshing)
        verify(productHorizontalFragment.presenter, times(2)).loadProductList(any(), any(), anyOrNull(), anyOrNull(), anyOrNull())
        verify(productPopularFragment.presenter, times(2)).loadProductList(any(), any(), anyOrNull(), anyOrNull(), anyOrNull())
        verify(blogFragment.presenter, times(2)).loadArticles(any(), anyOrNull())
    }
}