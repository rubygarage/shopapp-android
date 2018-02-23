package com.shopapp.ui.base.pagination

import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.TestShopApplication
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import com.shopapp.ui.base.recycler.adapter.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.activity_pagination.*
import org.junit.After
import org.junit.Assert.assertFalse
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
class PaginationActivityTest {

    private lateinit var activity: TestPaginationActivity
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        activity = Robolectric.setupActivity(TestPaginationActivity::class.java)
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldLoadData() {
        activity.loadData(true)
        verify(activity.presenter).loadTestData()
        assertTrue(activity.swipeRefreshLayout.isRefreshing)
    }

    @Test
    fun shouldLoadNextPage() {
        activity.loadNewPage()
        verify(activity.presenter).loadTestData()
        assertTrue(activity.swipeRefreshLayout.isRefreshing)
    }

    @Test
    fun shouldShowContent() {
        activity.swipeRefreshLayout.isRefreshing = true
        activity.showContent(listOf())
        assertFalse(activity.swipeRefreshLayout.isRefreshing)
    }

    @Test
    fun shouldHideLoadingWhenShowError() {
        activity.swipeRefreshLayout.isRefreshing = true
        activity.showError(false)
        assertFalse(activity.swipeRefreshLayout.isRefreshing)
    }

    @Test
    fun shouldRefreshContent() {
        activity.onRefresh()
        assertTrue(activity.swipeRefreshLayout.isRefreshing)
        verify(activity.presenter).loadTestData()
        verify(activity.recyclerView.adapter).notifyDataSetChanged()
    }

    @After
    fun tearDown() {
        activity.finish()
    }

    private class TestPaginationActivity : PaginationActivity<Any, BaseLceView<Any>, TestBaseLcePresenter>() {

        override fun setupAdapter(): BaseRecyclerAdapter<Any> = mock()

        override fun onItemClicked(data: Any, position: Int) {
        }

        override fun inject() {
        }

        override fun createPresenter(): TestBaseLcePresenter = mock()

        override fun loadData(pullToRefresh: Boolean) {
            super.loadData(pullToRefresh)
            presenter.loadTestData()
        }
    }

    private class TestBaseLcePresenter : BaseLcePresenter<Any, BaseLceView<Any>>() {

        fun loadTestData() {
        }
    }
}