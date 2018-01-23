package com.client.shop.ui.profile.di

import com.client.shop.ui.profile.contract.PersonalInfoPresenter
import com.domain.interactor.account.EditCustomerUseCase
import com.domain.interactor.account.GetCustomerUseCase
import com.domain.validator.FieldValidator
import dagger.Module
import dagger.Provides

@Module
class ProfileModule {

    @Provides
    fun providePersonalInfoPresenter(formValidator: FieldValidator,
                                     customerUseCase: GetCustomerUseCase,
                                     editCustomerUseCase: EditCustomerUseCase): PersonalInfoPresenter {
        return PersonalInfoPresenter(formValidator, customerUseCase, editCustomerUseCase)
    }

}