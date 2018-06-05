package com.shopapp.domain.interactor.account

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.CustomerRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetCustomerUseCaseTest {

    private lateinit var useCase: GetCustomerUseCase

    @Mock
    private lateinit var repository: CustomerRepository

    @Before
    fun setUpTest() {
        useCase = GetCustomerUseCase(repository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        useCase.buildUseCaseSingle(Unit)
        verify(repository).getCustomer()
    }

}