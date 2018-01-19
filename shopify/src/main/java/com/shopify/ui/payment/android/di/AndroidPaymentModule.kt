package com.shopify.ui.payment.android.di

import com.shopify.ui.payment.android.contract.AndroidPayCartUseCase
import com.shopify.ui.payment.android.contract.AndroidPaymentCompleteUseCase
import com.shopify.ui.payment.android.contract.AndroidPaymentPresenter
import dagger.Module
import dagger.Provides

@Module
class AndroidPaymentModule {

    @Provides
    fun provideAndroidPaymentPresenter(
        androidPayCartUseCase: AndroidPayCartUseCase,
        androidPaymentCompleteUseCase: AndroidPaymentCompleteUseCase
    ): AndroidPaymentPresenter =
        AndroidPaymentPresenter(androidPayCartUseCase, androidPaymentCompleteUseCase)
}