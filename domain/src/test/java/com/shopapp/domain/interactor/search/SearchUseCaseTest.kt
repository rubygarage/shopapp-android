package com.shopapp.domain.interactor.search

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.interactor.product.ProductListUseCase
import com.shopapp.domain.repository.ProductRepository
import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.SortType
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchUseCaseTest {

    private lateinit var useCase: SearchUseCase

    @Mock
    private lateinit var productRepository: ProductRepository

    @Before
    fun setUpTest() {
        useCase = SearchUseCase(productRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val perPage = 10
        val paginationValue = "paginationValue"
        val query = "query"
        val params = SearchUseCase.Params(perPage, paginationValue, query)
        useCase.buildUseCaseSingle(params)
        verify(productRepository).searchProductListByQuery(query, perPage, paginationValue)
    }

}