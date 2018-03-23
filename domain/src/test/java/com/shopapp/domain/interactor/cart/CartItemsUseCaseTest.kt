package com.shopapp.domain.interactor.cart

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.CartRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CartItemsUseCaseTest {

    @Mock
    private lateinit var cartRepository: CartRepository

    private lateinit var useCase: CartItemsUseCase

    @Before
    fun setUpTest() {
        useCase = CartItemsUseCase(cartRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        useCase.buildUseCaseObservable(Unit)

        verify(cartRepository).getCartProductList()
    }
}