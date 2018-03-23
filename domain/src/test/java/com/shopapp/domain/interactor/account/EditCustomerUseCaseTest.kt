package com.shopapp.domain.interactor.account

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.AuthRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EditCustomerUseCaseTest {

    private lateinit var useCase: EditCustomerUseCase

    @Mock
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUpTest() {
        useCase = EditCustomerUseCase(authRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val name = "name"
        val lastName = "lastName"
        val phone = "0633291677"

        useCase.buildUseCaseSingle(EditCustomerUseCase.Params(name, lastName, phone))
        verify(authRepository).editCustomer(name, lastName, phone)
    }

}