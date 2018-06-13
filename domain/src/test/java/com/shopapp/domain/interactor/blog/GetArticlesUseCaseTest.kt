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
class GetArticlesUseCaseTest {

    private lateinit var useCase: GetArticlesUseCase

    @Mock
    private lateinit var blogRepository: BlogRepository

    @Before
    fun setUpTest() {
        useCase = GetArticlesUseCase(blogRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        useCase.buildUseCaseSingle(GetArticlesUseCase.Params(1, null))
        verify(blogRepository).getArticles(1, null, SortType.RECENT)
    }
}