package com.shopapp.ui.blog.adapter

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.gateway.entity.Article
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.item.ArticleItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class BlogAdapterTest {

    companion object {
        private const val SIZE = 4
    }

    @Mock
    private lateinit var dataList: List<Article>

    @Mock
    private lateinit var clickListener: OnItemClickListener

    private lateinit var adapter: BlogAdapter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        given(dataList.size).willReturn(BlogAdapterTest.SIZE)
        adapter = BlogAdapter(dataList, clickListener)
    }

    @Test
    fun shouldReturnCorrectCount() {
        assertEquals(BlogAdapterTest.SIZE, adapter.itemCount)
    }

    @Test
    fun shouldReturnCorrectType() {
        val itemView = adapter.getItemView(RuntimeEnvironment.application.applicationContext, 0)
        assertTrue(itemView is ArticleItem)
    }

    @Test
    fun shouldCallSetOrderProduct() {
        val itemView: ArticleItem = mock()
        val article: Article = mock()
        adapter.bindData(itemView, article, 0)
        verify(itemView).setArticle(article)
    }
}