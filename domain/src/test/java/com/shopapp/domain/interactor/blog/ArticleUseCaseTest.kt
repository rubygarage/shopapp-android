package com.shopapp.domain.interactor.blog

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.BlogRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ArticleUseCaseTest {

    private lateinit var useCase: ArticleUseCase

    @Mock
    private lateinit var blogRepository: BlogRepository

    @Before
    fun setUpTest() {
        useCase = ArticleUseCase(blogRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val articleId = "articleId"
        useCase.buildUseCaseSingle(articleId)
        verify(blogRepository).getArticle(articleId)
    }
}