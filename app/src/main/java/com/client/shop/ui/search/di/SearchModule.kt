package com.client.shop.ui.search.di

import com.client.shop.ui.search.contract.SearchPresenter
import com.repository.Repository
import dagger.Module
import dagger.Provides

@Module
class SearchModule {

    @Provides
    fun provideSearchPresenter(repository: Repository): SearchPresenter {
        return SearchPresenter(repository)
    }
}