package com.shopapp.domain.interactor.cart

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.CartRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CartQuantityUseCaseTest {

    @Mock
    private lateinit var cartRepository: CartRepository

    private lateinit var useCase: CartQuantityUseCase

    @Before
    fun setUpTest() {
        useCase = CartQuantityUseCase(cartRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val id = "productVariantId"
        val qty = 20
        useCase.buildUseCaseSingle(CartQuantityUseCase.Params(id, qty))

        verify(cartRepository).changeCartProductQuantity(id, qty)
    }
}