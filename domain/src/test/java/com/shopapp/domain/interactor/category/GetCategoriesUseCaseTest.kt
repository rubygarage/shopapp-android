package com.shopapp.domain.interactor.category

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.CategoryRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetCategoriesUseCaseTest {

    private lateinit var useCase: GetCategoriesUseCase

    @Mock
    private lateinit var repository: CategoryRepository

    @Before
    fun setUpTest() {
        useCase = GetCategoriesUseCase(repository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val perPage = 5
        val pagination = "pagination"
        val parentCategoryId = "parentCategoryId"
        useCase.buildUseCaseSingle(GetCategoriesUseCase.Params(perPage, pagination, parentCategoryId))
        verify(repository).getCategories(perPage, pagination, parentCategoryId)
    }
}