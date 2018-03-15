package com.shopapp.ui.search

import android.content.Context
import android.support.v4.app.Fragment
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.anyOrNull
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.ui.category.CategoryListFragment
import kotlinx.android.synthetic.main.fragment_search_with_categories.*
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
    fun shouldShowCategoriesScreenOnCreate() {
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
        var showedFragment: Fragment = fragment.childFragmentManager.findFragmentById(R.id.container)
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
        val fragment = fragment.childFragmentManager.fragments.firstOrNull()
        assertNotNull(fragment)
        assertTrue(fragment is SearchFragment)
        verify((fragment as SearchFragment).presenter).search(any(), anyOrNull(), eq(testQuery))
    }

}