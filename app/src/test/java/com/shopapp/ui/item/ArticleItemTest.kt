package com.shopapp.ui.item

import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.given
import com.shopapp.test.MockInstantiator
import kotlinx.android.synthetic.main.item_article.view.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ArticleItemTest {

    private lateinit var context: Context
    private lateinit var itemView: ArticleItem

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application.baseContext
        itemView = ArticleItem(context)
    }

    @Test
    fun shouldSetTitle() {
        val article = MockInstantiator.newArticle()
        itemView.setArticle(article)
        assertEquals(MockInstantiator.DEFAULT_TITLE, itemView.titleTextView.text.toString())
    }

    @Test
    fun shouldSetContent() {
        val article = MockInstantiator.newArticle()
        itemView.setArticle(article)
        assertEquals(MockInstantiator.DEFAULT_DESCRIPTION, itemView.content.text.toString())
    }

    @Test
    fun shouldShowAuthorName() {
        val article = MockInstantiator.newArticle()
        itemView.setArticle(article)
        assertEquals(MockInstantiator.DEFAULT_NAME, itemView.author.text.toString())
    }

    @Test
    fun shouldShowImage() {
        val article = MockInstantiator.newArticle()
        itemView.setArticle(article)
        assertEquals(View.VISIBLE, itemView.image.visibility)
    }

    @Test
    fun shouldNotShowImage() {
        val article = MockInstantiator.newArticle()
        given(article.image?.src).willReturn(null)
        itemView.setArticle(article)
        assertEquals(View.GONE, itemView.image.visibility)
    }
}