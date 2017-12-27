package com.client.shop.di.module

import com.domain.validator.FieldValidator
import dagger.Module
import dagger.Provides

@Module
class ValidatorModule {

    @Provides
    fun provideFieldValidator() = FieldValidator()
}