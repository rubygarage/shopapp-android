package com.shopapp.domain.interactor.account

import com.shopapp.domain.repository.AuthRepository
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.annotation.Config

@RunWith(MockitoJUnitRunner::class)
@Config(manifest = Config.NONE)
class SingInUseCaseTest {

    private lateinit var useCase: SignInUseCase

    @Mock
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUpTest() {
        useCase = SignInUseCase(authRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val password = "123456789"
        val email = "somemail@gmail.com"
        useCase.buildUseCaseCompletable(SignInUseCase.Params(email, password))
        verify(authRepository).signIn(email, password)
    }

}