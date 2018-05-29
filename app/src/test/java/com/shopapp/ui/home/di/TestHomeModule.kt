package com.shopapp.ui.home.di

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ui.home.contract.HomePresenter
import dagger.Module
import dagger.Provides

@Module
class TestHomeModule {

    @Provides
    fun provideHomePresenter(): HomePresenter = mock()
}