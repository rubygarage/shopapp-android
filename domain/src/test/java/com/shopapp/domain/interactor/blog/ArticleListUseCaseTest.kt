package com.shopapp.domain.interactor.blog

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.BlogRepository
import com.shopapp.gateway.entity.SortType
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ArticleListUseCaseTest {

    private lateinit var useCase: ArticleListUseCase

    @Mock
    private lateinit var blogRepository: BlogRepository

    @Before
    fun setUpTest() {
        useCase = ArticleListUseCase(blogRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        useCase.buildUseCaseSingle(ArticleListUseCase.Params(1, null))
        verify(blogRepository).getArticleList(1, null, SortType.RECENT, true)
    }
}