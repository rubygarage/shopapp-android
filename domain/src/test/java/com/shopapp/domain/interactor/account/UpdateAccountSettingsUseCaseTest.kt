package com.shopapp.domain.interactor.account

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.AuthRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UpdateAccountSettingsUseCaseTest {

    private lateinit var useCase: UpdateAccountSettingsUseCase

    @Mock
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUpTest() {
        useCase = UpdateAccountSettingsUseCase(authRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        useCase.buildUseCaseCompletable(false)
        verify(authRepository).updateAccountSettings(false)
    }

}