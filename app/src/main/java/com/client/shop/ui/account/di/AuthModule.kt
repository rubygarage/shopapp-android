package com.client.shop.ui.account.di

import com.client.shop.ui.account.contract.AccountPresenter
import com.client.shop.ui.account.contract.ForgotPasswordPresenter
import com.client.shop.ui.account.contract.SignInPresenter
import com.client.shop.ui.account.contract.SignUpPresenter
import com.domain.interactor.account.*
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
    fun provideAuthPresenter(sessionCheckUseCase: SessionCheckUseCase, signOutUseCase: SignOutUseCase,
                             shopInfoUseCase: ShopInfoUseCase, getCustomerUseCase: GetCustomerUseCase): AccountPresenter {
        return AccountPresenter(sessionCheckUseCase, signOutUseCase, shopInfoUseCase, getCustomerUseCase)
    }

    @Provides
    fun provideForgotPasswordPresenter(forgotPasswordUseCase: ForgotPasswordUseCase): ForgotPasswordPresenter {
        return ForgotPasswordPresenter(forgotPasswordUseCase)
    }
}