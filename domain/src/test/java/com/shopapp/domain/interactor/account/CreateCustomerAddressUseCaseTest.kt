package com.shopapp.domain.interactor.account

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.domain.repository.AuthRepository
import com.shopapp.gateway.entity.Address
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CreateCustomerAddressUseCaseTest {

    private lateinit var useCase: CreateCustomerAddressUseCase

    @Mock
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUpTest() {
        useCase = CreateCustomerAddressUseCase(authRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val address: Address = mock()
        useCase.buildUseCaseSingle(address)
        verify(authRepository).createCustomerAddress(address)
    }
}