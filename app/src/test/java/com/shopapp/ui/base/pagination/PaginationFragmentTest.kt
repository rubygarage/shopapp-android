package com.shopapp.ui.base.pagination

import android.content.Context
import android.os.Bundle
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Error
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.base.recycler.adapter.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.activity_pagination.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.fakes.RoboMenuItem
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class PaginationFragmentTest {

    private lateinit var fragment: TestPaginationFragment
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        fragment = TestPaginationFragment()
        SupportFragmentTestUtil.startFragment(fragment)
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldLoadData() {
        fragment.loadData(true)
        verify(fragment.presenter).loadTestData()
        assertTrue(fragment.swipeRefreshLayout.isRefreshing)
    }

    @Test
    fun shouldLoadNextPage() {
        fragment.loadNewPage()
        verify(fragment.presenter).loadTestData()
        assertTrue(fragment.swipeRefreshLayout.isRefreshing)
    }

    @Test
    fun shouldShowContent() {
        fragment.swipeRefreshLayout.isRefreshing = true
        fragment.showContent(listOf())
        assertFalse(fragment.swipeRefreshLayout.isRefreshing)
    }

    @Test
    fun shouldHideLoadingWhenShowError() {
        fragment.swipeRefreshLayout.isRefreshing = true
        fragment.showError(Error.Content())
        assertFalse(fragment.swipeRefreshLayout.isRefreshing)
    }

    @Test
    fun shouldRefreshContent() {
        fragment.onRefresh()
        assertTrue(fragment.swipeRefreshLayout.isRefreshing)
        verify(fragment.presenter).loadTestData()
        verify(fragment.recyclerView.adapter).notifyDataSetChanged()
    }

    @Test
    fun shouldNotCallOnClickWithNegativePosition() {
        fragment.onItemClicked(-1)
        verify(fragment.mockListener, never()).onItemClicked(-1)
    }

    @Test
    fun shouldNotCallOnClickWithPositionMoreThanDataListSize() {
        fragment.onItemClicked(9999)
        verify(fragment.mockListener, never()).onItemClicked(9999)
    }

    @Test
    fun shouldCallOnClickWithCorrectPosition() {
        fragment.onItemClicked(1)
        verify(fragment.mockListener).onItemClicked(1)
    }

    @Test
    fun shouldFinishParentActivity() {
        val item = RoboMenuItem(android.R.id.home)
        fragment.onOptionsItemSelected(item)
        assertTrue(fragment.activity!!.isFinishing)
    }

    class TestPaginationFragment : PaginationFragment<Any, BaseLceView<Any>, TestBaseLcePresenter>() {

        val mockListener: OnItemClickListener = mock()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            dataList.addAll(MockInstantiator.newList(Any()))
        }

        override fun setupAdapter(): BaseRecyclerAdapter<Any> = mock()

        override fun onItemClicked(data: Any, position: Int) {
            mockListener.onItemClicked(position)
        }

        override fun inject() {
        }

        override fun createPresenter(): TestBaseLcePresenter = mock()

        override fun loadData(pullToRefresh: Boolean) {
            super.loadData(pullToRefresh)
            presenter.loadTestData()
        }
    }

    class TestBaseLcePresenter : BaseLcePresenter<Any, BaseLceView<Any>>() {

        fun loadTestData() {
        }
    }
}