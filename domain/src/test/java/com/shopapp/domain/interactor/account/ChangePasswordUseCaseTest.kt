package com.shopapp.domain.interactor.account

import com.shopapp.domain.repository.AuthRepository
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ChangePasswordUseCaseTest {

    private lateinit var useCase: ChangePasswordUseCase

    @Mock
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUpTest() {
        useCase = ChangePasswordUseCase(authRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val newPassword = "123456789"
        useCase.buildUseCaseCompletable(newPassword)
        verify(authRepository).changePassword(newPassword)
    }

}