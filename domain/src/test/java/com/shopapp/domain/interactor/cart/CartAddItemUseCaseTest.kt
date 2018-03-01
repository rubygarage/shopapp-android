package com.shopapp.domain.interactor.cart

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.CartRepository
import com.shopapp.gateway.entity.CartProduct
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CartAddItemUseCaseTest {

    @Mock
    private lateinit var cartRepository: CartRepository

    private lateinit var useCase: CartAddItemUseCase

    @Before
    fun setUpTest() {
        useCase = CartAddItemUseCase(cartRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val product: CartProduct = mock()
        useCase.buildUseCaseSingle(product)

        verify(cartRepository).addCartProduct(product)
    }
}