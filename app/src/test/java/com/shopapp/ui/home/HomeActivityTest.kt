package com.shopapp.ui.home

import android.content.Context
import android.os.Bundle
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.ui.account.AccountFragment
import com.shopapp.ui.search.SearchWithCategoriesFragment
import kotlinx.android.synthetic.main.activity_home.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class HomeActivityTest {

    private lateinit var activity: HomeActivity
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        val intent = HomeActivity.getStartIntent(context)
        activity = Robolectric.buildActivity(HomeActivity::class.java, intent).create().start().get()
    }

    @Test
    fun shouldSelectHomeTabOnCreate() {
        assertEquals(HomeActivity.HOME, activity.bottomTabNavigation.selectedTabPosition)
        assertTrue(activity.supportFragmentManager.findFragmentById(R.id.content) is HomeFragment)
    }

    @Test
    fun shouldShowAccountScreen() {
        activity.bottomTabNavigation.getTabAt(HomeActivity.ACCOUNT)?.select()
        assertEquals(HomeActivity.ACCOUNT, activity.bottomTabNavigation.selectedTabPosition)
        assertTrue(activity.supportFragmentManager.findFragmentById(R.id.content) is AccountFragment)
    }

    @Test
    fun shouldShowSearchScreen() {
        activity.bottomTabNavigation.getTabAt(HomeActivity.SEARCH)?.select()
        assertEquals(HomeActivity.SEARCH, activity.bottomTabNavigation.selectedTabPosition)
        assertTrue(activity.supportFragmentManager.findFragmentById(R.id.content) is SearchWithCategoriesFragment)
    }

    @Test
    fun shouldShowHomeScreen() {
        activity.bottomTabNavigation.getTabAt(HomeActivity.SEARCH)?.select()
        assertEquals(HomeActivity.SEARCH, activity.bottomTabNavigation.selectedTabPosition)
        assertTrue(activity.supportFragmentManager.findFragmentById(R.id.content) is SearchWithCategoriesFragment)

        activity.bottomTabNavigation.getTabAt(HomeActivity.HOME)?.select()
        assertEquals(HomeActivity.HOME, activity.bottomTabNavigation.selectedTabPosition)
        assertTrue(activity.supportFragmentManager.findFragmentById(R.id.content) is HomeFragment)
    }

    @Test
    fun shouldFinishActivityOnBackPress() {
        activity.onBackPressed()
        assertTrue(activity.isFinishing)
    }

    @Test
    fun shouldOpenHomeFromSearchOnBackPress() {
        activity.bottomTabNavigation.getTabAt(HomeActivity.SEARCH)?.select()
        assertTrue(activity.supportFragmentManager.findFragmentById(R.id.content) is SearchWithCategoriesFragment)
        activity.onBackPressed()
        assertEquals(HomeActivity.HOME, activity.bottomTabNavigation.selectedTabPosition)
        assertTrue(activity.supportFragmentManager.findFragmentById(R.id.content) is HomeFragment)
    }

    @Test
    fun shouldOpenHomeFromAccountOnBackPress() {
        activity.bottomTabNavigation.getTabAt(HomeActivity.ACCOUNT)?.select()
        assertTrue(activity.supportFragmentManager.findFragmentById(R.id.content) is AccountFragment)
        activity.onBackPressed()
        assertEquals(HomeActivity.HOME, activity.bottomTabNavigation.selectedTabPosition)
        assertTrue(activity.supportFragmentManager.findFragmentById(R.id.content) is HomeFragment)
    }

    @After
    fun tearDown() {
        activity.finish()
    }
}