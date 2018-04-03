package com.shopapp.ui.product

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.SortType
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.base.ui.FragmentVisibilityListener
import kotlinx.android.synthetic.main.activity_pagination.*
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
class ProductPopularFragmentTest {

    companion object {
        private const val MAX_ITEMS = 4
    }

    private lateinit var fragment: ProductPopularFragment
    private lateinit var context: Context

    @Before
    fun setUp() {
        fragment = ProductPopularFragment()
        startFragment(fragment)
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldLoadDataWhenOnViewCreated() {
        verify(fragment.presenter).loadProductList(SortType.RELEVANT, MAX_ITEMS)
    }

    @Test
    fun shouldSetCorrectRecyclerView() {
        assertTrue(fragment.recyclerView.layoutManager is GridLayoutManager)
        assertEquals(1, fragment.recyclerView.itemDecorationCount)
    }

    @Test
    fun shouldShowFullDataList() {
        val size = 3
        val data = MockInstantiator.newList(MockInstantiator.newProduct(), size)
        fragment.showContent(data)
        assertEquals(size, fragment.recyclerView.adapter.itemCount)
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
    fun shouldShowCutDataList() {
        val size = 5
        val data = MockInstantiator.newList(MockInstantiator.newProduct(), size)
        fragment.showContent(data)
        assertEquals(MAX_ITEMS, fragment.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldShowProduct() {
        val productMock = MockInstantiator.newProduct()
        fragment.showContent(listOf(productMock))
        fragment.onItemClicked(0)
        verify(fragment.router).showProduct(fragment.context, productMock.id)
    }

    @Test
    fun shouldNotShowProductOnInvalidPositionClick() {
        val productMock = MockInstantiator.newProduct()
        val dataList = listOf(productMock)
        fragment.showContent(dataList)
        fragment.onItemClicked(dataList.size + 1)
        verify(fragment.router, never()).showProduct(any(), any())
    }
}