package com.shopapp.domain.interactor.checkout

import com.shopapp.domain.repository.CheckoutRepository
import com.shopapp.gateway.entity.Address
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SetShippingAddressUseCaseTest {

    private lateinit var useCase: SetShippingAddressUseCase

    @Mock
    private lateinit var address: Address

    @Mock
    private lateinit var checkoutRepository: CheckoutRepository

    @Before
    fun setUpTest() {
        useCase = SetShippingAddressUseCase(checkoutRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val checkoutId = "checkoutId"
        useCase.buildUseCaseSingle(SetShippingAddressUseCase.Params(checkoutId, address))
        Mockito.verify(checkoutRepository).setShippingAddress(checkoutId, address)
    }
}