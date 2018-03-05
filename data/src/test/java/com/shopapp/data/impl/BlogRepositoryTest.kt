package com.shopapp.data.impl

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.data.RxImmediateSchedulerRule
import com.shopapp.domain.repository.BlogRepository
import com.shopapp.gateway.Api
import com.shopapp.gateway.ApiCallback
import com.shopapp.gateway.entity.Article
import com.shopapp.gateway.entity.Error
import com.shopapp.gateway.entity.SortType
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

class BlogRepositoryTest {

    companion object {
        private const val articleId = "articleId"
        private const val perPage = 5
        private const val paginationValue = "pagination"
    }

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var api: Api

    @Mock
    private lateinit var articleResult: Pair<Article, String>

    @Mock
    private lateinit var articleListResult: List<Article>

    private lateinit var articleObserver: TestObserver<Pair<Article, String>>
    private lateinit var articleListObserver: TestObserver<List<Article>>
    private lateinit var repository: BlogRepository

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        repository = BlogRepositoryImpl(api)
        articleObserver = TestObserver()
        articleListObserver = TestObserver()
    }

    @Test
    fun getArticleShouldDelegateCallToApi() {
        repository.getArticle(articleId).subscribe()
        verify(api).getArticle(eq(articleId), any())
    }

    @Test
    fun getArticleShouldReturnValueWhenOnResult() {
        given(api.getArticle(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Pair<Article, String>>>(1)
            callback.onResult(articleResult)
        })
        repository.getArticle(articleId).subscribe(articleObserver)
        articleObserver.assertValue(articleResult)
        articleObserver.assertComplete()
    }

    @Test
    fun getArticleShouldReturnErrorOnFailure() {
        val error = Error.Content()
        given(api.getArticle(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Pair<Article, String>>>(1)
            callback.onFailure(error)
        })
        repository.getArticle(articleId).subscribe(articleObserver)
        articleObserver.assertError(error)
    }

    @Test
    fun getArticleListShouldDelegateCallToApi() {
        repository.getArticleList(perPage, paginationValue, SortType.RECENT, true).subscribe()
        verify(api).getArticleList(eq(perPage), eq(paginationValue), eq(SortType.RECENT), eq(true), any())
    }

    @Test
    fun getArticleListShouldReturnValueWhenOnResult() {
        given(api.getArticleList(any(), any(), any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<Article>>>(4)
            callback.onResult(articleListResult)
        })
        repository.getArticleList(perPage, paginationValue, SortType.RECENT, true).subscribe(articleListObserver)
        articleListObserver.assertValue(articleListResult)
        articleListObserver.assertComplete()
    }

    @Test
    fun getArticleListShouldReturnErrorOnFailure() {
        val error = Error.Content()
        given(api.getArticleList(any(), any(), any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<Article>>>(4)
            callback.onFailure(error)
        })
        repository.getArticleList(perPage, paginationValue, SortType.RECENT, true).subscribe(articleListObserver)
        articleListObserver.assertError(error)
    }
}