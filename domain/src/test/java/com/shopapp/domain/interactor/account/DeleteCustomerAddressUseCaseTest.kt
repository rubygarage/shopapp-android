package com.shopapp.domain.interactor.account

import com.shopapp.domain.repository.AuthRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DeleteCustomerAddressUseCaseTest {

    private lateinit var useCase: DeleteCustomerAddressUseCase

    @Mock
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUpTest() {
        useCase = DeleteCustomerAddressUseCase(authRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val addressId = "addressId"
        useCase.buildUseCaseCompletable(addressId)
        Mockito.verify(authRepository).deleteCustomerAddress(addressId)
    }
}