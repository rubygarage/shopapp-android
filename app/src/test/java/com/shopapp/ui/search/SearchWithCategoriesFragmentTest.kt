package com.shopapp.ui.search

import android.content.Context
import android.support.v4.app.Fragment
import com.nhaarman.mockito_kotlin.*
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.ui.category.CategoryListFragment
import kotlinx.android.synthetic.main.fragment_search_with_categories_lce.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentController


@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class SearchWithCategoriesFragmentTest {

    private lateinit var fragment: SearchWithCategoriesFragment
    private lateinit var context: Context

    @Before
    fun setUp() {
        fragment = SupportFragmentController.of(SearchWithCategoriesFragment())
            .create()
            .start()
            .resume()
            .visible()
            .get()
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldLoadConfigDataOnceWhenOnCreate() {
        fragment.showContent(mock())
        fragment.onViewCreated(fragment.view!!, null)
        verify(fragment.presenter).getConfig()
    }

    @Test
    fun shouldShowCategoriesScreenWhenConfigReceived() {
        val config = com.shopapp.gateway.entity.Config(false, false, false, false)
        fragment.showContent(config)
        val showedFragment = fragment.childFragmentManager.findFragmentById(R.id.container)
        assertTrue(showedFragment is CategoryListFragment)
    }

    @Test
    fun shouldShowCategoriesScreen() {
        fragment.onToolbarStateChanged(true)
        val showedFragment = fragment.childFragmentManager.findFragmentById(R.id.container)
        assertTrue(showedFragment is SearchFragment)
    }

    @Test
    fun shouldShowSearchScreen() {
        fragment.onToolbarStateChanged(true)
        var showedFragment: Fragment =
            fragment.childFragmentManager.findFragmentById(R.id.container)
        assertTrue(showedFragment is SearchFragment)

        fragment.onToolbarStateChanged(false)
        showedFragment = fragment.childFragmentManager.findFragmentById(R.id.container)
        assertTrue(showedFragment is CategoryListFragment)
    }

    @Test
    fun shouldCollapseToolbar() {
        fragment.searchToolbar.changeToolbarState()
        fragment.onBackPressed()
        assertFalse(fragment.searchToolbar.isToolbarExpanded())
    }

    @Test
    fun shouldExpandToolbar() {
        fragment.searchToolbar.changeToolbarState()
        assertTrue(fragment.searchToolbar.isToolbarExpanded())
    }

    @Test
    fun shouldSearchQueryWhenQueryChangedAndSearchFragmentIsVisible() {
        fragment.onToolbarStateChanged(true)
        val testQuery = "testQuery"
        fragment.onQueryChanged(testQuery)
        val searchFragment =
            fragment.childFragmentManager.fragments.firstOrNull() as? SearchFragment
        assertNotNull(searchFragment)
        verify(searchFragment!!.presenter).search(any(), anyOrNull(), eq(testQuery))
    }

    @Test
    fun shouldSearchQueryWhenQueryIsEmptyAndSearchFragmentIsVisible() {
        fragment.onToolbarStateChanged(true)
        val testQuery = ""
        fragment.onQueryChanged(testQuery)
        val searchFragment =
            fragment.childFragmentManager.fragments.firstOrNull() as? SearchFragment
        assertNotNull(searchFragment)
        verify(searchFragment!!.presenter).search(any(), anyOrNull(), eq(testQuery))
    }

    @Test
    fun shouldNotSearchQueryWhenQueryIsNotEmptyAndSearchFragmentIsInvisible() {
        fragment.onToolbarStateChanged(true)

        val searchFragment =
            fragment.childFragmentManager.fragments.firstOrNull() as? SearchFragment
        assertNotNull(searchFragment)
        assertTrue(searchFragment!!.isVisible)

        fragment.onToolbarStateChanged(false)
        val testQuery = "test query"
        fragment.onQueryChanged(testQuery)

        assertFalse(searchFragment.isVisible)
        verify(searchFragment.presenter, never()).search(any(), anyOrNull(), any())
    }

    @Test
    fun shouldSearchQueryWhenQueryChangedAndSearchFragmentIsInvisible() {
        fragment.onToolbarStateChanged(true)

        val searchFragment =
            fragment.childFragmentManager.fragments.firstOrNull() as? SearchFragment
        assertNotNull(searchFragment)
        assertTrue(searchFragment!!.isVisible)

        fragment.onToolbarStateChanged(false)

        assertFalse(searchFragment.isVisible)
        val testQuery = ""
        fragment.onQueryChanged(testQuery)
        val categoryFragment = fragment.childFragmentManager.fragments.firstOrNull()
        assertNotNull(categoryFragment)
        assertTrue(categoryFragment is CategoryListFragment)

        verify(searchFragment.presenter).search(any(), anyOrNull(), eq(testQuery))
    }
}