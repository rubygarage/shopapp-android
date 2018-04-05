package com.shopapp.ui.blog.router

import com.shopapp.TestShopApplication
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.blog.ArticleActivity
import com.shopapp.ui.blog.BlogActivity
import com.shopapp.ui.blog.BlogFragment
import com.shopapp.ui.home.HomeActivity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class BlogRouterTest {

    private lateinit var fragment: BlogFragment
    private lateinit var router: BlogRouter

    @Before
    fun setUp() {
        fragment = BlogFragment()
        SupportFragmentTestUtil.startFragment(fragment, HomeActivity::class.java)
        router = BlogRouter()
    }

    @Test
    fun shouldShowArticleActivity() {
        val articleMock = MockInstantiator.newArticle()
        router.showArticle(fragment.context, articleMock.id)

        val startedIntent = Shadows.shadowOf(fragment.activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(MockInstantiator.DEFAULT_ID, startedIntent.extras.getString(ArticleActivity.EXTRA_ARTICLE_ID))
        assertEquals(ArticleActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldBlogActivity() {
        router.showFullBlogList(fragment.context)
        val startedIntent = Shadows.shadowOf(fragment.activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(BlogActivity::class.java, shadowIntent.intentClass)
    }

}