package com.shopapp.domain.interactor.category

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.CategoryRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CategoryListUseCaseTest {

    private lateinit var useCase: CategoryListUseCase

    @Mock
    private lateinit var repository: CategoryRepository

    @Before
    fun setUpTest() {
        useCase = CategoryListUseCase(repository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val perPage = 5
        val pagination = "pagination"
        useCase.buildUseCaseSingle(CategoryListUseCase.Params(perPage, pagination))
        verify(repository).getCategoryList(perPage, pagination)
    }
}