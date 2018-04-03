package com.shopapp.ui.product

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.SortType
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.base.ui.FragmentVisibilityListener
import com.shopapp.ui.const.Constant
import kotlinx.android.synthetic.main.fragment_recent.*
import kotlinx.android.synthetic.main.layout_lce.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class ProductHorizontalFragmentTest {

    companion object {
        private const val KEYWORD = "keyword"
        private const val EXCLUDE_KEYWORD = "keyword"
        private val sortType = SortType.RECENT
    }

    private lateinit var fragment: ProductHorizontalFragment
    private lateinit var context: Context

    @Before
    fun setUp() {
        fragment = ProductHorizontalFragment.newInstance(sortType, KEYWORD, EXCLUDE_KEYWORD)
        startFragment(fragment)
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldReceiveCorrectArguments() {
        assertEquals(sortType, fragment.arguments?.getSerializable(ProductHorizontalFragment.SORT_TYPE) as SortType)
        assertEquals(KEYWORD, fragment.arguments?.getString(ProductHorizontalFragment.KEYWORD))
        assertEquals(EXCLUDE_KEYWORD, fragment.arguments?.getString(ProductHorizontalFragment.EXCLUDE_KEYWORD))
    }

    @Test
    fun shouldShowRecentTitle() {
        fragment = ProductHorizontalFragment.newInstance(SortType.RECENT, KEYWORD, EXCLUDE_KEYWORD)
        startFragment(fragment)
        assertEquals(context.getString(R.string.latest_arrivals), fragment.titleText.text.toString())
    }

    @Test
    fun shouldShowRelatedTitle() {
        fragment = ProductHorizontalFragment.newInstance(SortType.TYPE, KEYWORD, EXCLUDE_KEYWORD)
        startFragment(fragment)
        assertEquals(context.getString(R.string.related_items), fragment.titleText.text.toString())
    }

    @Test
    fun shouldShowEmptyTitle() {
        fragment = ProductHorizontalFragment.newInstance(SortType.NAME, KEYWORD, EXCLUDE_KEYWORD)
        startFragment(fragment)
        assertEquals("", fragment.titleText.text.toString())
    }

    @Test
    fun shouldLoadDataWhenOnViewCreated() {
        verify(fragment.presenter).loadProductList(sortType, Constant.DEFAULT_PER_PAGE_COUNT,
            null, KEYWORD, EXCLUDE_KEYWORD)
    }

    @Test
    fun shouldSetCorrectRecyclerView() {
        val layoutManager = fragment.recyclerView.layoutManager
        assertTrue(layoutManager is LinearLayoutManager)
        assertEquals(LinearLayoutManager.HORIZONTAL, (layoutManager as LinearLayoutManager).orientation)
        assertEquals(1, fragment.recyclerView.itemDecorationCount)
    }

    @Test
    fun shouldShowContentView() {
        val size = 5
        val dataList = MockInstantiator.newList(MockInstantiator.newProduct(), size)
        fragment.showContent(dataList)
        assertEquals(View.GONE, fragment.emptyView.visibility)
        assertEquals(size, fragment.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldSeeAllButtonBeGone() {
        val size = 9
        val data = MockInstantiator.newList(MockInstantiator.newProduct(), size)
        assertEquals(View.GONE, fragment.seeAll.visibility)
        fragment.showContent(data)
        assertEquals(View.GONE, fragment.seeAll.visibility)
    }

    @Test
    fun shouldSeeAllButtonBeVisible() {
        val size = 10
        val data = MockInstantiator.newList(MockInstantiator.newProduct(), size)
        assertEquals(View.GONE, fragment.seeAll.visibility)
        fragment.showContent(data)
        assertEquals(View.VISIBLE, fragment.seeAll.visibility)
    }

    @Test
    fun shouldShowProductList() {
        fragment.seeAll.performClick()
        verify(fragment.router).showProductList(
            fragment.context,
            context.getString(R.string.latest_arrivals),
            sortType,
            KEYWORD,
            EXCLUDE_KEYWORD
        )
    }

    @Test
    fun shouldNotifyVisibilityListener() {
        val listener: FragmentVisibilityListener = mock()
        fragment.visibilityListener = listener

        val size = 5
        val dataList = MockInstantiator.newList(MockInstantiator.newProduct(), size)
        fragment.showContent(dataList)
        verify(listener).changeVisibility(true)

        fragment.showContent(emptyList())
        verify(listener).changeVisibility(false)
    }

    @Test
    fun shouldShowProduct() {
        val productMock = MockInstantiator.newProduct()
        fragment.showContent(listOf(productMock))
        fragment.onItemClicked(0)
        verify(fragment.router).showProduct(fragment.context, productMock.id)
    }
}