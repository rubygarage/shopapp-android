package com.shopapp.domain.interactor.category

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.CategoryRepository
import com.shopapp.gateway.entity.SortType
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CategoryUseCaseTest {

    private lateinit var useCase: CategoryUseCase

    @Mock
    private lateinit var repository: CategoryRepository

    @Before
    fun setUpTest() {
        useCase = CategoryUseCase(repository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val perPage = 5
        val pagination = "pagination"
        val categoryId = "categoryId"
        useCase.buildUseCaseSingle(CategoryUseCase.Params(perPage, pagination, categoryId, SortType.NAME))
        verify(repository).getCategory(categoryId, perPage, pagination, SortType.NAME)
    }
}