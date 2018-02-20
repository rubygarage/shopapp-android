package com.domain.interactor.account

import com.domain.repository.AuthRepository
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.annotation.Config

@Suppress("FunctionName")
@RunWith(MockitoJUnitRunner::class)
@Config(manifest = Config.NONE)
class ForgotPasswordUseCaseTest {

    private lateinit var useCase: ForgotPasswordUseCase

    @Mock
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUpTest() {
        useCase = ForgotPasswordUseCase(authRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val email = "test@test.com"
        useCase.buildUseCaseCompletable(email)
        verify(authRepository).forgotPassword(email)
    }

}