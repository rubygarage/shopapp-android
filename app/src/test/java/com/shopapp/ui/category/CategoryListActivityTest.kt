package com.shopapp.ui.category

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Category
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.const.Constant.DEFAULT_PER_PAGE_COUNT
import kotlinx.android.synthetic.main.fragment_search_with_categories_list.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
        val intent = CategoryActivity.getStartIntent(context, category)
        activity = Robolectric.buildActivity(CategoryListActivity::class.java, intent)
            .create()
            .resume()
            .visible()
            .get()
    }

    @Test
    fun shouldGetCategoriesOnCreate() {
        verify(activity.presenter).getCategoryList(DEFAULT_PER_PAGE_COUNT, null,
            category.id)
    }

    @Test
    fun shouldGetCategoriesOnLoadData() {
        activity.loadData()
        verify(activity.presenter, times(2)).getCategoryList(DEFAULT_PER_PAGE_COUNT,
            null, category.id)
    }

    @Test
    fun shouldShowData() {
        val categories = MockInstantiator.newList(MockInstantiator.newCategory(), DEFAULT_PER_PAGE_COUNT)
        activity.showContent(categories)
        assertEquals(DEFAULT_PER_PAGE_COUNT, activity.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldStartCategoryActivity() {
        activity.onItemClicked(category, 0)
        verify(activity.router).showCategory(activity, category)
    }

    @Test
    fun shouldStartCategoryListActivity() {
        val childCategoryList = listOf<Category>(mock())
        given(category.childrenCategoryList).willReturn(childCategoryList)
        activity.onItemClicked(category, 0)
        verify(activity.router).showCategoryList(activity, category)
    }

    @Test
    fun shouldShowItemsAsGrid() {
        assertFalse(activity.recyclerView.layoutManager is GridLayoutManager)
    }
}