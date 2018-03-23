package com.shopapp.domain.interactor.checkout

import com.shopapp.domain.repository.CheckoutRepository
import com.shopapp.gateway.entity.ShippingRate
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SetShippingRateUseCaseTest {

    private lateinit var useCase: SetShippingRateUseCase

    @Mock
    private lateinit var shippingRate: ShippingRate

    @Mock
    private lateinit var checkoutRepository: CheckoutRepository

    @Before
    fun setUpTest() {
        useCase = SetShippingRateUseCase(checkoutRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val checkoutId = "checkoutId"
        useCase.buildUseCaseSingle(SetShippingRateUseCase.Params(checkoutId, shippingRate))
        Mockito.verify(checkoutRepository).selectShippingRate(checkoutId, shippingRate)
    }
}