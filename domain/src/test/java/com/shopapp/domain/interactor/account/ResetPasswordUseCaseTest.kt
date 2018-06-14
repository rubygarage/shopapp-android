package com.shopapp.domain.interactor.account

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.AuthRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ResetPasswordUseCaseTest {

    private lateinit var useCase: ResetPasswordUseCase

    @Mock
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUpTest() {
        useCase = ResetPasswordUseCase(authRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val email = "test@test.com"
        useCase.buildUseCaseCompletable(email)
        verify(authRepository).resetPassword(email)
    }

}