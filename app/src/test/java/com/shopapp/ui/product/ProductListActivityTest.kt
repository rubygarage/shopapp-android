package com.shopapp.ui.product

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.SortType
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.const.Constant
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.activity_pagination.*
import kotlinx.android.synthetic.main.layout_lce.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
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
class ProductListActivityTest {

    companion object {
        private const val TITLE = "title"
        private const val KEYWORD = "keyword"
        private const val EXCLUDE_KEYWORD = "keyword"
        private val sortType = SortType.NAME
    }

    private lateinit var context: Context
    private lateinit var activity: ProductListActivity

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        val intent = ProductListActivity.getStartIntent(context, TITLE, sortType, KEYWORD, EXCLUDE_KEYWORD)
        activity = Robolectric.buildActivity(ProductListActivity::class.java, intent).create().start().resume().visible().get()
    }

    @Test
    fun shouldSetCorrectTitle() {
        assertEquals(TITLE, activity.toolbar.toolbarTitle.text)
    }

    @Test
    fun shouldLoadDataWhenOnCreate() {
        verify(activity.presenter).loadProductList(sortType, Constant.DEFAULT_PER_PAGE_COUNT,
            null, KEYWORD, EXCLUDE_KEYWORD)
        assertEquals(View.VISIBLE, activity.lceLayout.loadingView.visibility)
    }

    @Test
    fun shouldSetCorrectRecyclerView() {
        assertTrue(activity.recyclerView.layoutManager is GridLayoutManager)
        assertEquals(ContextCompat.getDrawable(context, R.color.white), activity.recyclerView.background)
    }

    @Test
    fun shouldShowContentView() {
        val size = 5
        val dataList = MockInstantiator.newList(MockInstantiator.newProduct(), size)
        activity.showContent(dataList)
        assertEquals(View.GONE, activity.emptyView.visibility)
        assertEquals(false, activity.swipeRefreshLayout.isRefreshing)
        assertEquals(size, activity.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldShowProduct() {
        val productMock = MockInstantiator.newProduct()
        activity.showContent(listOf(productMock))
        activity.onItemClicked(productMock, 0)
        verify(activity.router).showProduct(activity, productMock.id)
    }

    @After
    fun tearDown() {
        activity.finish()
    }
}