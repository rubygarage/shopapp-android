package com.shopapp.domain.interactor.cart

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.CartRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.annotation.Config

@RunWith(MockitoJUnitRunner::class)
@Config(manifest = Config.NONE)
class CartQuantityUseCaseTest {

    private lateinit var useCase: CartQuantityUseCase

    @Mock
    private lateinit var cartRepository: CartRepository

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