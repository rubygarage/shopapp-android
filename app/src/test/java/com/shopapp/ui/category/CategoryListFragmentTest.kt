package com.shopapp.ui.category

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Category
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.const.Constant.DEFAULT_PER_PAGE_COUNT
import kotlinx.android.synthetic.main.fragment_search_with_categories_list.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class CategoryListFragmentTest {

    private lateinit var fragment: CategoryListFragment
    private lateinit var context: Context

    @Before
    fun setUp() {
        fragment = CategoryListFragment()
        SupportFragmentTestUtil.startFragment(fragment)
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldGetCategoriesOnCreate() {
        verify(fragment.presenter).getCategoryList(DEFAULT_PER_PAGE_COUNT, null)
    }

    @Test
    fun shouldGetCategoriesOnLoadData() {
        fragment.loadData()
        verify(fragment.presenter, times(2)).getCategoryList(DEFAULT_PER_PAGE_COUNT, null)
    }

    @Test
    fun shouldShowData() {
        val categories = MockInstantiator.newList(MockInstantiator.newCategory(), DEFAULT_PER_PAGE_COUNT)
        fragment.showContent(categories)
        assertEquals(DEFAULT_PER_PAGE_COUNT, fragment.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldStartCategoryActivity() {
        val category: Category = mock()
        fragment.onItemClicked(category, 0)
        verify(fragment.router).showCategory(fragment.context, category)
    }

    @Test
    fun shouldShowItemsAsGrid() {
        assertTrue(fragment.recyclerView.layoutManager is GridLayoutManager)
    }
}