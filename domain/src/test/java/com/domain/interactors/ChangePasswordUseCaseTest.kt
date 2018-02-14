package com.domain.interactors

import com.domain.interactor.account.ChangePasswordUseCase
import com.domain.repository.AuthRepository
import com.nhaarman.mockito_kotlin.eq
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
class ChangePasswordUseCaseTest {

    private lateinit var useCase: ChangePasswordUseCase

    @Mock
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUpTest() {
        useCase = ChangePasswordUseCase(authRepository)
    }

    @Test
    fun changePasswordUseCaseTest_buildUseCase() {
        useCase.buildUseCaseCompletable("123456789")
        verify(authRepository).changePassword(eq("123456789"))
    }

}