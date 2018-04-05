package com.shopapp.ui.account.di

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ui.account.contract.*
import com.shopapp.ui.account.router.AccountRouter
import com.shopapp.ui.account.router.PersonalInfoRouter
import com.shopapp.ui.account.router.SignInRouter
import com.shopapp.ui.account.router.SignUpRouter
import dagger.Module
import dagger.Provides

@Module
class TestAuthModule {

    @Provides
    fun provideSignUpPresenter(): SignUpPresenter = mock()

    @Provides
    fun provideSignInPresenter(): SignInPresenter = mock()

    @Provides
    fun provideAuthPresenter(): AccountPresenter = mock()

    @Provides
    fun provideForgotPasswordPresenter(): ForgotPasswordPresenter = mock()

    @Provides
    fun providePersonalInfoPresenter(): PersonalInfoPresenter = mock()

    @Provides
    fun provideChangePasswordPresenter(): ChangePasswordPresenter = mock()

    @Provides
    fun provideAccountSettingsPresenter(): AccountSettingsPresenter = mock()

    @Provides
    fun provideAccountRouter(): AccountRouter = mock()

    @Provides
    fun provideSignInRouter(): SignInRouter = mock()

    @Provides
    fun provideSignUpRouter(): SignUpRouter = mock()

    @Provides
    fun providePersonalInfoRouter(): PersonalInfoRouter = mock()

}