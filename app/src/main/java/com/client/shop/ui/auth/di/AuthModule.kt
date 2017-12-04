package com.client.shop.ui.auth.di

import com.client.shop.ui.auth.contract.*
import dagger.Module
import dagger.Provides

@Module
class AuthModule {

    @Provides
    fun provideSignUpPresenter(signUpUseCase: SignUpUseCase): SignUpPresenter {
        return SignUpPresenter(signUpUseCase)
    }

    @Provides
    fun provideSignInPresenter(signInUseCase: SignInUseCase, forgotPasswordUseCase: ForgotPasswordUseCase): SignInPresenter {
        return SignInPresenter(signInUseCase, forgotPasswordUseCase)
    }

    @Provides
    fun provideAuthPresenter(authUseCase: AuthUseCase, signOutUseCase: SignOutUseCase): AuthPresenter {
        return AuthPresenter(authUseCase, signOutUseCase)
    }
}