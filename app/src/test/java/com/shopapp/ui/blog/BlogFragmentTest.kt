package com.shopapp.ui.blog

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.View
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.TestShopApplication
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.base.ui.FragmentVisibilityListener
import com.shopapp.ui.blog.adapter.BlogAdapter
import com.shopapp.ui.const.Constant
import com.shopapp.ui.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_blog.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class BlogFragmentTest {

    companion object {
        private const val DECORATOR_COUNT = 1
    }

    private lateinit var fragment: BlogFragment
    private lateinit var context: Context

    @Before
    fun setUp() {
        fragment = BlogFragment()
        startFragment(fragment, HomeActivity::class.java)
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldAddDecoratorOnCreate() {
        assertEquals(DECORATOR_COUNT, fragment.recyclerView.itemDecorationCount)
    }

    @Test
    fun shouldAddLinearLayoutManagerOnCreate() {
        assertNotNull(fragment.recyclerView.layoutManager)
        assertTrue(fragment.recyclerView.layoutManager is LinearLayoutManager)
        val manager = fragment.recyclerView.layoutManager as LinearLayoutManager
        assertEquals(OrientationHelper.VERTICAL, manager.orientation)
    }

    @Test
    fun shouldAddAdapterOnCreate() {
        assertNotNull(fragment.recyclerView.adapter)
        assertTrue(fragment.recyclerView.adapter is BlogAdapter)
        val adapter = fragment.recyclerView.adapter as BlogAdapter
        assertEquals(0, adapter.itemCount)
    }

    @Test
    fun shouldHideSeeAllOnCreate() {
        assertEquals(View.GONE, fragment.seeAll.visibility)
    }

    @Test
    fun shouldGetArticlesOnCreate() {
        verify(fragment.presenter).loadArticles(Constant.DEFAULT_PER_PAGE_COUNT, null)
    }

    @Test
    fun shouldLoadArticlesOnLoadDate() {
        fragment.loadData()
        verify(fragment.presenter, times(2)).loadArticles(Constant.DEFAULT_PER_PAGE_COUNT)
    }

    @Test
    fun shouldNotifyVisibilityListener() {
        val listener: FragmentVisibilityListener = mock()
        fragment.visibilityListener = listener

        val articlesCount = 5
        val articleList = MockInstantiator.newList(MockInstantiator.newArticle(), articlesCount)
        fragment.showContent(articleList)
        verify(listener).changeVisibility(true)

        fragment.showContent(emptyList())
        verify(listener).changeVisibility(false)
    }

    @Test
    fun shouldShowContentView() {
        val articlesCount = 5
        val articles = MockInstantiator.newList(MockInstantiator.newArticle(), articlesCount)
        fragment.showContent(articles)
        assertEquals(articlesCount, fragment.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldNotShowContentView() {
        val articlesCount = 0
        fragment.showContent(emptyList())
        assertEquals(articlesCount, fragment.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldShowSeeAllView() {
        val articlesCount = Constant.DEFAULT_PER_PAGE_COUNT
        val articles = MockInstantiator.newList(MockInstantiator.newArticle(), articlesCount)
        fragment.showContent(articles)
        assertEquals(View.VISIBLE, fragment.seeAll.visibility)
    }

    @Test
    fun shouldHideSeeAllView() {
        fragment.showContent(emptyList())
        assertEquals(View.GONE, fragment.seeAll.visibility)
    }

    @Test
    fun shouldStartBlogActivityWhenSeeAllClicked() {
        fragment.seeAll.performClick()
        verify(fragment.router).showFullBlogList(fragment.context)
    }

    @Test
    fun shouldStartArticleActivityWhenItemClicked() {
        val article = MockInstantiator.newArticle()
        fragment.showContent(listOf(article))
        fragment.onItemClicked(0)
        verify(fragment.router).showArticle(fragment.context, article.id)
    }

    @Test
    fun shouldNotStartArticleActivityWhenItemClickedWithWrongPosition() {
        fragment.showContent(listOf())
        fragment.onItemClicked(0)

        val startedIntent = shadowOf(fragment.activity).nextStartedActivity
        assertNull(startedIntent)
    }
}