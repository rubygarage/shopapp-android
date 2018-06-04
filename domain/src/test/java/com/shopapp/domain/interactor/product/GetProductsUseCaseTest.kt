package com.shopapp.domain.interactor.product

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.ProductRepository
import com.shopapp.gateway.entity.SortType
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetProductsUseCaseTest {

    private lateinit var useCase: GetProductsUseCase

    @Mock
    private lateinit var productRepository: ProductRepository

    @Before
    fun setUpTest() {
        useCase = GetProductsUseCase(productRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val sortType = SortType.RELEVANT
        val perPage = 10
        val paginationValue = "paginationValue"
        val keyword = "keyword"
        val excludeKeyword = "excludeKeyword"
        val params = GetProductsUseCase.Params(sortType, perPage, paginationValue, keyword, excludeKeyword)
        useCase.buildUseCaseSingle(params)
        verify(productRepository).getProducts(perPage, paginationValue, sortType, keyword, excludeKeyword)
    }
}