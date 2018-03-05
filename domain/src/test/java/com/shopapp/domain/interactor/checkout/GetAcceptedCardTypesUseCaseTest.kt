package com.shopapp.domain.interactor.checkout

import com.shopapp.domain.repository.CheckoutRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetAcceptedCardTypesUseCaseTest {

    private lateinit var useCase: GetAcceptedCardTypesUseCase

    @Mock
    private lateinit var checkoutRepository: CheckoutRepository

    @Before
    fun setUpTest() {
        useCase = GetAcceptedCardTypesUseCase(checkoutRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        useCase.buildUseCaseSingle(Unit)
        verify(checkoutRepository).getAcceptedCardTypes()
    }
}