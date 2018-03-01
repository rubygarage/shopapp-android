package com.shopapp.domain.interactor.product

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.ProductRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ProductDetailsUseCaseTest {

    private lateinit var useCase: ProductDetailsUseCase

    @Mock
    private lateinit var productRepository: ProductRepository

    @Before
    fun setUpTest() {
        useCase = ProductDetailsUseCase(productRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val productId = "productId"
        useCase.buildUseCaseSingle(productId)
        verify(productRepository).getProduct(productId)
    }
}