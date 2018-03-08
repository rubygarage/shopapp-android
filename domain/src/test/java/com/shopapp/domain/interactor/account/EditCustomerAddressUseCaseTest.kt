package com.shopapp.domain.interactor.account

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.AuthRepository
import com.shopapp.gateway.entity.Address
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EditCustomerAddressUseCaseTest {

    private lateinit var useCase: EditCustomerAddressUseCase

    @Mock
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUpTest() {
        useCase = EditCustomerAddressUseCase(authRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val addressId = "addressId"
        val address: Address = mock()
        useCase.buildUseCaseCompletable(EditCustomerAddressUseCase.Params(addressId, address))
        verify(authRepository).editCustomerAddress(addressId, address)
    }
}