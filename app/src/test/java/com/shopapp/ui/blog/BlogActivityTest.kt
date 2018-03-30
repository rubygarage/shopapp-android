package com.shopapp.ui.blog

import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.const.Constant
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.activity_order_list.*
import kotlinx.android.synthetic.main.layout_lce.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class BlogActivityTest {

    private lateinit var activity: BlogActivity
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        activity = Robolectric.buildActivity(BlogActivity::class.java, BlogActivity.getStartIntent(context))
            .create()
            .resume()
            .get()
        activity.loadingView.minShowTime = 0
    }

    @Test
    fun shouldSetCorrectTitle() {
        assertEquals(context.getString(R.string.blog_posts), activity.toolbar.toolbarTitle.text)
    }

    @Test
    fun shouldLoadArticlesOnCreate() {
        verify(activity.presenter).loadArticles(Constant.DEFAULT_PER_PAGE_COUNT, null)
        assertEquals(View.VISIBLE, activity.lceLayout.loadingView.visibility)
    }

    @Test
    fun shouldShowContentView() {
        val articleCount = 5
        val articleList = MockInstantiator.newList(MockInstantiator.newArticle(), articleCount)
        assertEquals(View.VISIBLE, activity.lceLayout.loadingView.visibility)
        activity.showContent(articleList)
        assertEquals(View.GONE, activity.lceLayout.loadingView.visibility)
        assertEquals(View.GONE, activity.emptyView.visibility)
        assertEquals(false, activity.swipeRefreshLayout.isRefreshing)
        assertEquals(articleCount, activity.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldDoNothingIfDataIsEmpty() {
        val articleCount = 0
        activity.showContent(emptyList())
        assertEquals(View.GONE, activity.emptyView.visibility)
        assertEquals(false, activity.swipeRefreshLayout.isRefreshing)
        assertEquals(View.VISIBLE, activity.contentView.visibility)
        assertEquals(articleCount, activity.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldShowOrderDetails() {
        val articleMock = MockInstantiator.newArticle()
        activity.onItemClicked(articleMock, 0)
        verify(activity.router).showArticle(activity, articleMock.id)
    }

    @After
    fun tearDown() {
        activity.finish()
    }
}