package com.shopapp.ui.blog.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.interactor.blog.ArticleUseCase
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

class ArticlePresenterTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: ArticleView

    @Mock
    private lateinit var useCase: ArticleUseCase

    private lateinit var presenter: ArticlePresenter


    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = ArticlePresenter(useCase)
        presenter.attachView(view)
        useCase.mock()
    }

    @Test
    fun shouldExecuteUseCaseOnValidData() {
        val result: Pair<Article, String> = mock()
        given(useCase.buildUseCaseSingle(any())).willReturn(Single.just(result))
        presenter.loadArticles("1")
        verify(useCase).execute(any(), any(), eq("1"))
    }

    @Test
    fun shouldShowContentOnArticleLoaded() {
        val result: Pair<Article, String> = mock()
        given(useCase.buildUseCaseSingle(any())).willReturn(Single.just(result))
        presenter.loadArticles("1")
        val inOrder = inOrder(useCase, view)
        inOrder.verify(useCase).execute(any(), any(), eq("1"))
        inOrder.verify(view).showContent(result)
    }

    @Test
    fun shouldShowMessageOnUseCaseNonCriticalError() {
        given(useCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.NonCritical("ErrorMessage")))
        presenter.loadArticles("1")
        verify(view).showMessage("ErrorMessage")
    }

    @Test
    fun shouldShowErrorOnUseCaseContentError() {
        given(useCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.Content()))
        presenter.loadArticles("1")
        argumentCaptor<Error>().apply {
            verify(view).showError(capture())
            assertTrue(firstValue is Error.Content)
        }
    }

    @After
    fun tearDown() {
        presenter.detachView(false)
    }

}