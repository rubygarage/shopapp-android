package com.shopapp.ui.search

import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.const.Constant
import com.shopapp.ui.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_search_with_categories_list.*
import kotlinx.android.synthetic.main.layout_lce.*
import kotlinx.android.synthetic.main.view_lce_empty.view.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class SearchFragmentTest {

    companion object {
        private const val SEARCH_QUERY = "search query"
        private const val SEARCH_QUERY_CHANGED = "changed query"
    }

    private lateinit var fragment: SearchFragment
    private lateinit var context: Context

    @Before
    fun setUp() {
        fragment = SearchFragment()
        SupportFragmentTestUtil.startFragment(fragment, HomeActivity::class.java)
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldStartProductActivity() {
        val product = MockInstantiator.newProduct()
        fragment.onItemClicked(product, 0)
        verify(fragment.router).showProduct(fragment.context, product.id)
    }

    @Test
    fun shouldShowCategoriesList() {
        val products = MockInstantiator.newList(MockInstantiator.newProduct(), Constant.DEFAULT_PER_PAGE_COUNT)
        fragment.showContent(products)
        assertEquals(View.VISIBLE, fragment.contentView.visibility)
        assertEquals(Constant.DEFAULT_PER_PAGE_COUNT, fragment.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldAddItemDecoration() {
        assertEquals(2, fragment.recyclerView.itemDecorationCount)
    }

    @Test
    fun shouldShowEmptyState() {
        fragment.queryChanged(SEARCH_QUERY)
        fragment.showContent(emptyList())
        assertEquals(View.VISIBLE, fragment.emptyView.visibility)
    }

    @Test
    fun shouldShowCorrectEmptyView() {
        fragment.showEmptyState()
        val resId = shadowOf(fragment.emptyView.emptyImage.drawable).createdFromResId
        assertEquals(R.drawable.ic_search_not_found, resId)
        assertEquals(context.getString(R.string.no_results_found), fragment.emptyView.emptyMessage.text.toString())
    }

    @Test
    fun shouldSearchProductsOnQueryChange() {
        fragment.queryChanged(SEARCH_QUERY)
        verify(fragment.presenter).search(Constant.DEFAULT_PER_PAGE_COUNT, null, SEARCH_QUERY)
    }

    @Test
    fun shouldClearDataListOnQueryChange() {
        val products = MockInstantiator.newList(MockInstantiator.newProduct(), Constant.DEFAULT_PER_PAGE_COUNT)
        fragment.queryChanged(SEARCH_QUERY)
        fragment.showContent(products)

        fragment.queryChanged(SEARCH_QUERY_CHANGED)
        fragment.showContent(products)

        assertEquals(Constant.DEFAULT_PER_PAGE_COUNT, fragment.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldStopRefreshing() {
        fragment.hideProgress()
        assertFalse(fragment.swipeRefreshLayout.isRefreshing)
    }
}