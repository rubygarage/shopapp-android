package com.shopapp.domain.interactor.checkout

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.CheckoutRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetCheckoutUseCaseTest {

    private lateinit var useCase: GetCheckoutUseCase

    @Mock
    private lateinit var checkoutRepository: CheckoutRepository

    @Before
    fun setUpTest() {
        useCase = GetCheckoutUseCase(checkoutRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val checkoutId = "checkoutId"
        useCase.buildUseCaseSingle(checkoutId)
        verify(checkoutRepository).getCheckout(checkoutId)
    }
}