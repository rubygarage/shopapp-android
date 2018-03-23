package com.shopapp.domain.interactor.checkout

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.domain.repository.CheckoutRepository
import com.shopapp.gateway.entity.Card
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CheckCreditCardUseCaseTest {

    private lateinit var useCase: CheckCreditCardUseCase

    @Mock
    private lateinit var checkoutRepository: CheckoutRepository

    @Before
    fun setUpTest() {
        useCase = CheckCreditCardUseCase(checkoutRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val card: Card = mock()
        useCase.buildUseCaseSingle(card)
        verify(checkoutRepository).getCardToken(card)
    }
}