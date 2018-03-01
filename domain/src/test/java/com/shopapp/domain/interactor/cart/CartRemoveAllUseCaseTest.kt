package com.shopapp.domain.interactor.cart

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.CartRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CartRemoveAllUseCaseTest {

    @Mock
    private lateinit var cartRepository: CartRepository

    private lateinit var useCase: CartRemoveAllUseCase

    @Before
    fun setUpTest() {
        useCase = CartRemoveAllUseCase(cartRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        useCase.buildUseCaseCompletable(Unit)

        verify(cartRepository).deleteAllProductsFromCart()
    }
}