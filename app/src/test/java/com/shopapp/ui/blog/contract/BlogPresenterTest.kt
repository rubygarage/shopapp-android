package com.shopapp.ui.blog.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.interactor.blog.ArticleListUseCase
import com.shopapp.gateway.entity.Article
import com.shopapp.gateway.entity.Error
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.test.ext.mock
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class BlogPresenterTest {

    companion object {
        private const val PAGE_SIZE = 1
        private const val PAGINATION_VALUE = "paginationValue"
    }

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: BlogView

    @Mock
    private lateinit var articleListUseCase: ArticleListUseCase

    @Mock
    private lateinit var articles: List<Article>

    private lateinit var presenter: BlogPresenter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = BlogPresenter(articleListUseCase)
        presenter.attachView(view)
        articleListUseCase.mock()
    }

    @Test
    fun shouldCallUseCaseOnAuthorizationCheck() {
        given(articleListUseCase.buildUseCaseSingle(any())).willReturn(Single.just(articles))
        presenter.loadArticles(PAGE_SIZE, PAGINATION_VALUE)
        verify(articleListUseCase).execute(any(), any(), eq(ArticleListUseCase.Params(PAGE_SIZE, PAGINATION_VALUE)))
    }

    @Test
    fun shouldShowContent() {
        given(articleListUseCase.buildUseCaseSingle(any())).willReturn(Single.just(articles))
        presenter.loadArticles(PAGE_SIZE, PAGINATION_VALUE)
        val inOrder = inOrder(view, articleListUseCase)
        inOrder.verify(articleListUseCase).execute(any(), any(), eq(ArticleListUseCase.Params(PAGE_SIZE, PAGINATION_VALUE)))
        inOrder.verify(view).showContent(articles)
    }

    @Test
    fun shouldShowMessageOnGetCustomerNonCriticalError() {
        given(articleListUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.NonCritical("ErrorMessage")))
        presenter.loadArticles(PAGE_SIZE, PAGINATION_VALUE)

        val inOrder = inOrder(view, articleListUseCase)
        inOrder.verify(articleListUseCase).execute(any(), any(), eq(ArticleListUseCase.Params(PAGE_SIZE, PAGINATION_VALUE)))
        inOrder.verify(view).showMessage(eq("ErrorMessage"))
    }

    @Test
    fun shouldShowErrorOnGetCustomerContentError() {
        given(articleListUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.Content()))
        presenter.loadArticles(PAGE_SIZE, PAGINATION_VALUE)

        val inOrder = inOrder(view, articleListUseCase)
        inOrder.verify(articleListUseCase).execute(any(), any(), eq(ArticleListUseCase.Params(PAGE_SIZE, PAGINATION_VALUE)))
        argumentCaptor<Error>().apply {
            inOrder.verify(view).showError(capture())
            assertTrue(firstValue is Error.Content)
        }
    }

    @After
    fun tearDown() {
        presenter.detachView(false)
    }

}