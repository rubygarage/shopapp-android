package com.shopapp.domain.interactor.checkout

import com.shopapp.domain.repository.CheckoutRepository
import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Checkout
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CompleteCheckoutByCardUseCaseTest {

    private lateinit var useCase: CompleteCheckoutByCardUseCase

    @Mock
    private lateinit var checkout: Checkout

    @Mock
    private lateinit var address: Address

    @Mock
    private lateinit var checkoutRepository: CheckoutRepository

    @Before
    fun setUpTest() {
        useCase = CompleteCheckoutByCardUseCase(checkoutRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val email = "email"
        val token = "token"
        useCase.buildUseCaseSingle(CompleteCheckoutByCardUseCase.Params(checkout, email, address, token))
        Mockito.verify(checkoutRepository).completeCheckoutByCard(checkout, email, address, token)
    }
}