package com.shopapp.ui.account.di

import com.shopapp.domain.interactor.account.*
import com.shopapp.domain.interactor.shop.ConfigUseCase
import com.shopapp.domain.interactor.shop.ShopInfoUseCase
import com.shopapp.domain.validator.FieldValidator
import com.shopapp.ui.account.contract.*
import com.shopapp.ui.account.router.AccountRouter
import com.shopapp.ui.account.router.PersonalInfoRouter
import com.shopapp.ui.account.router.SignInRouter
import com.shopapp.ui.account.router.SignUpRouter
import dagger.Module
import dagger.Provides

@Module
class AccountModule {

    @Provides
    fun provideSignUpPresenter(configUseCase: ConfigUseCase, formValidator: FieldValidator, signUpUseCase: SignUpUseCase): SignUpPresenter {
        return SignUpPresenter(configUseCase, formValidator, signUpUseCase)
    }

    @Provides
    fun provideSignInPresenter(
            formValidator: FieldValidator,
            signInUseCase: SignInUseCase
    ): SignInPresenter {
        return SignInPresenter(formValidator, signInUseCase)
    }

    @Provides
    fun provideAuthPresenter(sessionCheckUseCase: SessionCheckUseCase, signOutUseCase: SignOutUseCase,
                             shopInfoUseCase: ShopInfoUseCase, getCustomerUseCase: GetCustomerUseCase): AccountPresenter {
        return AccountPresenter(sessionCheckUseCase, signOutUseCase, shopInfoUseCase, getCustomerUseCase)
    }

    @Provides
    fun provideForgotPasswordPresenter(formValidator: FieldValidator, resetPasswordUseCase: ResetPasswordUseCase): ForgotPasswordPresenter {
        return ForgotPasswordPresenter(formValidator, resetPasswordUseCase)
    }

    @Provides
    fun providePersonalInfoPresenter(
            configUseCase: ConfigUseCase,
            customerUseCase: GetCustomerUseCase,
            updateCustomerUseCase: UpdateCustomerUseCase
    ): PersonalInfoPresenter {
        return PersonalInfoPresenter(configUseCase, customerUseCase, updateCustomerUseCase)
    }

    @Provides
    fun provideChangePasswordPresenter(validator: FieldValidator, updatePasswordUseCase: UpdatePasswordUseCase): ChangePasswordPresenter {
        return ChangePasswordPresenter(validator, updatePasswordUseCase)
    }

    @Provides
    fun provideAccountSettingsPresenter(customerUseCase: GetCustomerUseCase,
                                        updateCustomerSettingsUseCase: UpdateCustomerSettingsUseCase): AccountSettingsPresenter {
        return AccountSettingsPresenter(customerUseCase, updateCustomerSettingsUseCase)
    }

    @Provides
    fun provideAccountRouter(): AccountRouter = AccountRouter()

    @Provides
    fun provideSignInRouter(): SignInRouter = SignInRouter()

    @Provides
    fun provideSignUpRouter(): SignUpRouter = SignUpRouter()

    @Provides
    fun providePersonalInfoRouter(): PersonalInfoRouter = PersonalInfoRouter()

}