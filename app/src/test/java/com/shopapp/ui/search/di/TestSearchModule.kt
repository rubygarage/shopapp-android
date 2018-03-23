package com.shopapp.ui.search.di

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ui.search.contract.SearchPresenter
import dagger.Module
import dagger.Provides

@Module
class TestSearchModule {

    @Provides
    fun provideSearchPresenter(): SearchPresenter = mock()

}