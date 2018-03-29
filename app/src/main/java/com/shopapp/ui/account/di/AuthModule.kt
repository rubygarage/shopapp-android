package com.shopapp.ui.account.di

import com.shopapp.domain.interactor.account.*
import com.shopapp.domain.validator.FieldValidator
import com.shopapp.ui.account.contract.*
import com.shopapp.ui.account.router.AccountRouter
import com.shopapp.ui.account.router.PersonalInfoRouter
import com.shopapp.ui.account.router.SignInRouter
import com.shopapp.ui.account.router.SignUpRouter
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
    fun provideForgotPasswordPresenter(formValidator: FieldValidator, forgotPasswordUseCase: ForgotPasswordUseCase): ForgotPasswordPresenter {
        return ForgotPasswordPresenter(formValidator, forgotPasswordUseCase)
    }

    @Provides
    fun providePersonalInfoPresenter(customerUseCase: GetCustomerUseCase,
                                     editCustomerUseCase: EditCustomerUseCase): PersonalInfoPresenter {
        return PersonalInfoPresenter(customerUseCase, editCustomerUseCase)
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

    @Provides
    fun provideAccountRouter(): AccountRouter = AccountRouter()

    @Provides
    fun provideSignInRouter(): SignInRouter = SignInRouter()

    @Provides
    fun provideSignUpRouter(): SignUpRouter = SignUpRouter()

    @Provides
    fun providePersonalInfoRouter(): PersonalInfoRouter = PersonalInfoRouter()

}