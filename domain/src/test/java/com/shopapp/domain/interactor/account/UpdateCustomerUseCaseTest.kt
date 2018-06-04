package com.shopapp.domain.interactor.account

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.CustomerRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UpdateCustomerUseCaseTest {

    private lateinit var useCase: UpdateCustomerUseCase

    @Mock
    private lateinit var repository: CustomerRepository

    @Before
    fun setUpTest() {
        useCase = UpdateCustomerUseCase(repository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val name = "name"
        val lastName = "lastName"
        val phone = "0633291677"

        useCase.buildUseCaseSingle(UpdateCustomerUseCase.Params(name, lastName, phone))
        verify(repository).updateCustomer(name, lastName, phone)
    }
}