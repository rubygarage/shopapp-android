package com.shopapp.domain.interactor.cart

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.CartRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CartRemoveUseCaseTest {

    @Mock
    private lateinit var cartRepository: CartRepository

    private lateinit var useCase: CartRemoveUseCase

    @Before
    fun setUpTest() {
        useCase = CartRemoveUseCase(cartRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val id = "productVariantId"
        useCase.buildUseCaseCompletable(id)

        verify(cartRepository).deleteProductFromCart(id)
    }
}