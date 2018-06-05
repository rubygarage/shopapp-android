package com.shopapp.ui.home

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.nhaarman.mockito_kotlin.*
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.ui.blog.BlogFragment
import com.shopapp.ui.product.ProductPopularFragment
import com.shopapp.ui.product.ProductShortcutFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_product_shortcut.*
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

    private lateinit var config: com.shopapp.gateway.entity.Config
    private lateinit var fragment: HomeFragment

    @Before
    fun setUp() {
        config = mock()
        given(config.isPopularEnabled).willReturn(true)
        given(config.isBlogEnabled).willReturn(true)
        given(config.isCategoryGridEnabled).willReturn(true)
        fragment = SupportFragmentController.of(HomeFragment())
            .create()
            .resume()
            .visible()
            .get()
    }

    @Test
    fun shouldShowProductShortcutFragment() {
        fragment.onConfigReceived(config)
        assertTrue(fragment.childFragmentManager.findFragmentById(R.id.recentContainer) is ProductShortcutFragment)
    }

    @Test
    fun shouldShowHorizontalProductShortcutFragment() {
        fragment.onConfigReceived(config)
        val childFragment =
            fragment.childFragmentManager.findFragmentById(R.id.recentContainer) as ProductShortcutFragment
        val layoutManager = childFragment.recyclerView.layoutManager
        assertTrue(layoutManager is LinearLayoutManager)
        assertEquals(
            LinearLayoutManager.HORIZONTAL,
            (layoutManager as LinearLayoutManager).orientation
        )
    }

    @Test
    fun shouldShowVerticalProductShortcutFragment() {
        given(config.isPopularEnabled).willReturn(false)
        given(config.isBlogEnabled).willReturn(false)
        fragment.onConfigReceived(config)
        val childFragment =
            fragment.childFragmentManager.findFragmentById(R.id.recentContainer) as ProductShortcutFragment
        val layoutManager = childFragment.recyclerView.layoutManager
        assertTrue(layoutManager is GridLayoutManager)
        assertEquals(GridLayoutManager.VERTICAL, (layoutManager as GridLayoutManager).orientation)
    }

    @Test
    fun shouldShowProductPopularFragment() {
        fragment.onConfigReceived(config)
        assertTrue(fragment.childFragmentManager.findFragmentById(R.id.popularContainer) is ProductPopularFragment)
    }

    @Test
    fun shouldNotShowProductPopularFragment() {
        given(config.isPopularEnabled).willReturn(false)
        fragment.onConfigReceived(config)
        assertNull(fragment.childFragmentManager.findFragmentById(R.id.popularContainer))
    }

    @Test
    fun shouldShowBlogFragment() {
        fragment.onConfigReceived(config)
        assertTrue(fragment.childFragmentManager.findFragmentById(R.id.blogContainer) is BlogFragment)
    }

    @Test
    fun shouldNotShowBlogFragment() {
        given(config.isBlogEnabled).willReturn(false)
        fragment.onConfigReceived(config)
        assertNull(fragment.childFragmentManager.findFragmentById(R.id.blogContainer))
    }

    @Test
    fun shouldShowRecentContainerWhenCallChangeVisibilityWithVisibleTrue() {
        fragment.onConfigReceived(config)
        val childFragment =
            fragment.childFragmentManager.findFragmentById(R.id.recentContainer) as ProductShortcutFragment
        childFragment.fragmentVisibilityListener?.changeVisibility(true)
        assertEquals(View.VISIBLE, fragment.recentContainer.visibility)
    }

    @Test
    fun shouldHideRecentContainerWhenCallChangeVisibilityWithVisibleFalse() {
        fragment.onConfigReceived(config)
        val childFragment =
            fragment.childFragmentManager.findFragmentById(R.id.recentContainer) as ProductShortcutFragment
        childFragment.fragmentVisibilityListener?.changeVisibility(false)
        assertEquals(View.GONE, fragment.recentContainer.visibility)
    }

    @Test
    fun shouldShowPopularContainerWhenCallChangeVisibilityWithVisibleTrue() {
        fragment.onConfigReceived(config)
        val childFragment =
            fragment.childFragmentManager.findFragmentById(R.id.popularContainer) as ProductPopularFragment
        childFragment.fragmentVisibilityListener?.changeVisibility(true)
        assertEquals(View.VISIBLE, fragment.popularContainer.visibility)
    }

    @Test
    fun shouldHidePopularContainerWhenCallChangeVisibilityWithVisibleFalse() {
        fragment.onConfigReceived(config)
        val childFragment =
            fragment.childFragmentManager.findFragmentById(R.id.popularContainer) as ProductPopularFragment
        childFragment.fragmentVisibilityListener?.changeVisibility(false)
        assertEquals(View.GONE, fragment.popularContainer.visibility)
    }

    @Test
    fun shouldShowBlogContainerWhenCallChangeVisibilityWithVisibleTrue() {
        fragment.onConfigReceived(config)
        val childFragment =
            fragment.childFragmentManager.findFragmentById(R.id.blogContainer) as BlogFragment
        childFragment.fragmentVisibilityListener?.changeVisibility(true)
        assertEquals(View.VISIBLE, fragment.blogContainer.visibility)
    }

    @Test
    fun shouldHideBlogContainerWhenCallChangeVisibilityWithVisibleFalse() {
        fragment.onConfigReceived(config)
        val childFragment =
            fragment.childFragmentManager.findFragmentById(R.id.blogContainer) as BlogFragment
        childFragment.fragmentVisibilityListener?.changeVisibility(false)
        assertEquals(View.GONE, fragment.blogContainer.visibility)
    }

    @Test
    fun shouldStopRefreshAndStartLoadDataWhenOnRefresh() {
        fragment.onConfigReceived(config)
        val productHorizontalFragment =
            fragment.childFragmentManager.findFragmentById(R.id.recentContainer) as ProductShortcutFragment
        val productPopularFragment =
            fragment.childFragmentManager.findFragmentById(R.id.popularContainer) as ProductPopularFragment
        val blogFragment =
            fragment.childFragmentManager.findFragmentById(R.id.blogContainer) as BlogFragment

        fragment.refreshLayout.isRefreshing = true
        assertTrue(fragment.refreshLayout.isRefreshing)

        fragment.onRefresh()
        assertTrue(fragment.refreshLayout.isRefreshing)
        verify(productHorizontalFragment.presenter, times(2)).loadProductList(
            any(),
            any(),
            anyOrNull(),
            anyOrNull(),
            anyOrNull()
        )
        verify(productPopularFragment.presenter, times(2)).loadProductList(
            any(),
            any(),
            anyOrNull(),
            anyOrNull(),
            anyOrNull()
        )
        verify(blogFragment.presenter, times(2)).loadArticles(any(), anyOrNull())
    }
}