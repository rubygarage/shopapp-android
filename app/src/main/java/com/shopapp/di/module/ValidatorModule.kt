package com.shopapp.di.module

import com.shopapp.domain.validator.FieldValidator
import dagger.Module
import dagger.Provides

@Module
class ValidatorModule {

    @Provides
    fun provideFieldValidator() = FieldValidator()

}