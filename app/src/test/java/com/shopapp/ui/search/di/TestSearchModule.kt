package com.shopapp.ui.search.di

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ui.search.contract.SearchPresenter
import com.shopapp.ui.search.router.SearchRouter
import dagger.Module
import dagger.Provides

@Module
class TestSearchModule {

    @Provides
    fun provideSearchPresenter(): SearchPresenter = mock()

    @Provides
    fun provideSearchRouter(): SearchRouter = mock()
}