package com.shopapp.ui.category

import android.content.Context
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Category
import com.shopapp.test.MockInstantiator
import kotlinx.android.synthetic.main.layout_lce.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
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
class CategoryListActivityTest {

    private lateinit var activity: CategoryListActivity
    private lateinit var context: Context
    private lateinit var category: Category

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application.baseContext
        category = MockInstantiator.newCategory()
        val intent = CategoryListActivity.getStartIntent(context, category, false)
        activity = Robolectric.buildActivity(CategoryListActivity::class.java, intent)
            .create()
            .resume()
            .visible()
            .get()
    }

    @Test
    fun shouldSetCorrectTitle() {
        assertEquals(MockInstantiator.DEFAULT_TITLE, activity.toolbar.toolbarTitle.text)
    }

    @Test
    fun shouldAddCategoryListFragment() {
        assertTrue(activity.supportFragmentManager.findFragmentById(R.id.content) is CategoryListFragment)
    }
}