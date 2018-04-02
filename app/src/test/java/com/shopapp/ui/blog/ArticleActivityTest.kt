package com.shopapp.ui.blog

import android.content.Context
import android.view.View
import android.webkit.WebSettings
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Error
import com.shopapp.test.MockInstantiator
import com.shopapp.test.ext.replaceCommandSymbols
import kotlinx.android.synthetic.main.activity_article.*
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.layout_lce.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import kotlinx.android.synthetic.main.view_lce_error.view.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class ArticleActivityTest {

    private lateinit var activity: ArticleActivity
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        val intent = ArticleActivity.getStartIntent(context, MockInstantiator.DEFAULT_ID)
        activity = Robolectric.buildActivity(ArticleActivity::class.java, intent)
            .create()
            .resume()
            .visible()
            .get()
        activity.loadingView.minShowTime = 0
    }

    @Test
    fun shouldSetupWebViewOnCreate() {
        with(activity.content.settings) {
            assertTrue(javaScriptEnabled)
            assertTrue(domStorageEnabled)
            assertEquals(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING, layoutAlgorithm)
        }
    }

    @Test
    fun shouldGetArticleOnCreate() {
        verify(activity.presenter).loadArticles(MockInstantiator.DEFAULT_ID)
    }

    @Test
    fun shouldShowArticleTitleOnShowContent() {
        activity.showContent(Pair(MockInstantiator.newArticle(), MockInstantiator.DEFAULT_URL))
        assertEquals(MockInstantiator.DEFAULT_TITLE, activity.articleTitle.text.toString())
    }

    @Test
    fun shouldShowArticleAuthorOnShowContent() {
        activity.showContent(Pair(MockInstantiator.newArticle(), MockInstantiator.DEFAULT_URL))
        assertEquals(MockInstantiator.DEFAULT_NAME, activity.author.text.toString())
    }

    @Test
    fun shouldShowArticleImage() {
        val article = MockInstantiator.newArticle()
        activity.showContent(Pair(article, MockInstantiator.DEFAULT_URL))
        assertEquals(View.VISIBLE, activity.image.visibility)
    }

    @Test
    fun shouldNotShowArticleImage() {
        val article = MockInstantiator.newArticle()
        given(article.image?.src).willReturn(null)
        activity.showContent(Pair(article, MockInstantiator.DEFAULT_URL))
        assertEquals(View.GONE, activity.image.visibility)
    }

    @Test
    fun shouldShowContentInWebView() {
        assertEquals(View.VISIBLE, activity.lceLayout.loadingView.visibility)
        activity.showContent(Pair(MockInstantiator.newArticle(), MockInstantiator.DEFAULT_URL))
        assertEquals(View.GONE, activity.lceLayout.loadingView.visibility)
        val webView = shadowOf(activity.content)
        val tmp = shadowOf(activity.mainLooper)
        tmp.idle()
        assertEquals(MockInstantiator.DEFAULT_URL, webView.lastLoadDataWithBaseURL.baseUrl)
        assertEquals(
            MockInstantiator.DEFAULT_CONTENT_HTML.replaceCommandSymbols(),
            webView.lastLoadDataWithBaseURL.data.replaceCommandSymbols()
        )
        assertEquals("text/html", webView.lastLoadDataWithBaseURL.mimeType)
        assertEquals("UTF-8", webView.lastLoadDataWithBaseURL.encoding)
    }

    @Test
    fun shouldShowNotFoundErrorWithTargetWhenCriticalErrorReceived() {
        val target = context.getString(R.string.article)
        activity.showError(Error.Critical())
        assertEquals(View.VISIBLE, activity.lceLayout.errorView.visibility)
        assertEquals(context.getString(R.string.сould_not_find_with_placeholder, target),
            activity.lceLayout.errorView.errorMessage.text)
    }

    @After
    fun tearDown() {
        activity.finish()
    }
}