package com.client.shop.ui.auth.di

import com.client.shop.ui.auth.contract.SignInPresenter
import com.client.shop.ui.auth.contract.SignInUseCase
import com.client.shop.ui.auth.contract.SignUpPresenter
import com.client.shop.ui.auth.contract.SignUpUseCase
import dagger.Module
import dagger.Provides

@Module
class AuthModule {

    @Provides
    fun provideSignUpPresenter(signUpUseCase: SignUpUseCase): SignUpPresenter {
        return SignUpPresenter(signUpUseCase)
    }

    @Provides
    fun provideSignInPresenter(signInUseCase: SignInUseCase): SignInPresenter {
        return SignInPresenter(signInUseCase)
    }
}