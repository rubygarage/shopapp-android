package com.domain.interactor.account

import com.domain.repository.AuthRepository
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.annotation.Config

@RunWith(MockitoJUnitRunner::class)
@Config(manifest = Config.NONE)
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