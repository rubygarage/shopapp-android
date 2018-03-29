package com.shopapp.ui.search.di

import com.shopapp.domain.interactor.search.SearchUseCase
import com.shopapp.ui.search.contract.SearchPresenter
import com.shopapp.ui.search.router.SearchRouter
import dagger.Module
import dagger.Provides

@Module
class SearchModule {

    @Provides
    fun provideSearchPresenter(searchUseCase: SearchUseCase): SearchPresenter {
        return SearchPresenter(searchUseCase)
    }

    @Provides
    fun provideSearchRouter(): SearchRouter = SearchRouter()
}