package com.shopapp.domain.interactor.account

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.AuthRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SignUpUseCaseTest {

    private lateinit var useCase: SignUpUseCase

    @Mock
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUpTest() {
        useCase = SignUpUseCase(authRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val firstName = "firstName"
        val lastName = "lastName"
        val email = "email@test.com"
        val password = "123456789"
        val phone = "+380633291677"
        useCase.buildUseCaseCompletable(SignUpUseCase.Params(firstName, lastName, email, password, phone))
        verify(authRepository).signUp(firstName, lastName, email, password, phone)
    }

}