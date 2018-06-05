package com.shopapp.domain.interactor.account

import com.shopapp.domain.repository.CustomerRepository
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
    private lateinit var repository: CustomerRepository

    @Before
    fun setUpTest() {
        useCase = DeleteCustomerAddressUseCase(repository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val addressId = "addressId"
        useCase.buildUseCaseCompletable(addressId)
        Mockito.verify(repository).deleteCustomerAddress(addressId)
    }
}