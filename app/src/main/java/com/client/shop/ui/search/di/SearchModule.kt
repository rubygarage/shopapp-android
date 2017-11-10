package com.client.shop.ui.search.di

import com.client.shop.ui.search.contract.SearchPresenter
import com.client.shop.ui.search.contract.SearchUseCase
import dagger.Module
import dagger.Provides

@Module
class SearchModule {

    @Provides
    fun provideSearchPresenter(searchUseCase: SearchUseCase): SearchPresenter {
        return SearchPresenter(searchUseCase)
    }
}