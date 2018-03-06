package com.shopapp.ui.search

import android.content.Context
import android.support.v4.app.Fragment
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.ui.category.CategoryListFragment
import com.shopapp.ui.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_search_with_categories.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil


@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class SearchWithCategoriesFragmentTest {

    private lateinit var fragment: SearchWithCategoriesFragment
    private lateinit var context: Context

    @Before
    fun setUp() {
        fragment = SearchWithCategoriesFragment()
        SupportFragmentTestUtil.startFragment(fragment, HomeActivity::class.java)
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
    fun shouldExpandToolbar(){
        fragment.searchToolbar.changeToolbarState()
        assertTrue(fragment.searchToolbar.isToolbarExpanded())
    }
}