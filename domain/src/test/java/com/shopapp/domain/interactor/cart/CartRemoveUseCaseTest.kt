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
class CartRemoveUseCaseTest {

    private lateinit var useCase: CartRemoveUseCase

    @Mock
    private lateinit var cartRepository: CartRepository

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