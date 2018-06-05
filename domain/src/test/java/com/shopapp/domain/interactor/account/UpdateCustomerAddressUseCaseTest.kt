package com.shopapp.domain.interactor.account

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.CustomerRepository
import com.shopapp.gateway.entity.Address
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UpdateCustomerAddressUseCaseTest {

    private lateinit var useCase: UpdateCustomerAddressUseCase

    @Mock
    private lateinit var repository: CustomerRepository

    @Before
    fun setUpTest() {
        useCase = UpdateCustomerAddressUseCase(repository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val address: Address = mock()
        useCase.buildUseCaseCompletable(address)
        verify(repository).updateCustomerAddress(address)
    }
}