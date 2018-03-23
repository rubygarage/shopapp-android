package com.shopapp.di.module

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.domain.validator.CardValidator
import com.shopapp.domain.validator.FieldValidator
import dagger.Module
import dagger.Provides

@Module
class TestValidatorModule {

    @Provides
    fun provideFieldValidator(): FieldValidator = mock()

    @Provides
    fun provideCardValidator(): CardValidator = mock()
}