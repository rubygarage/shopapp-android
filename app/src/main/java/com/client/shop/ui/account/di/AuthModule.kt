package com.client.shop.ui.account.di

import com.client.shop.ui.account.contract.*
import com.domain.interactor.account.*
import com.domain.validator.FieldValidator
import dagger.Module
import dagger.Provides

@Module
class AuthModule {

    @Provides
    fun provideSignUpPresenter(formValidator: FieldValidator, signUpUseCase: SignUpUseCase): SignUpPresenter {
        return SignUpPresenter(formValidator, signUpUseCase)
    }

    @Provides
    fun provideSignInPresenter(
        formValidator: FieldValidator,
        signInUseCase: SignInUseCase,
        forgotPasswordUseCase: ForgotPasswordUseCase
    ): SignInPresenter {
        return SignInPresenter(formValidator, signInUseCase, forgotPasswordUseCase)
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

    @Provides
    fun providePersonalInfoPresenter(validator: FieldValidator,
                                     customerUseCase: GetCustomerUseCase,
                                     editCustomerUseCase: EditCustomerUseCase): PersonalInfoPresenter {
        return PersonalInfoPresenter(validator, customerUseCase, editCustomerUseCase)
    }

    @Provides
    fun provideChangePasswordPresenter(validator: FieldValidator, changePasswordUseCase: ChangePasswordUseCase): ChangePasswordPresenter {
        return ChangePasswordPresenter(validator, changePasswordUseCase)
    }

    @Provides
    fun provideAccountSettingsPresenter(customerUseCase: GetCustomerUseCase,
                                        updateAccountSettingsUseCase: UpdateAccountSettingsUseCase): AccountSettingsPresenter {
        return AccountSettingsPresenter(customerUseCase, updateAccountSettingsUseCase)
    }
}