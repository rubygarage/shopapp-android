package com.shopapp.ui.search.di

import com.shopapp.domain.interactor.category.CategoryListUseCase
import com.shopapp.domain.interactor.search.SearchUseCase
import com.shopapp.ui.category.contract.CategoryListPresenter
import com.shopapp.ui.search.contract.SearchPresenter
import dagger.Module
import dagger.Provides

@Module
class SearchModule {

    @Provides
    fun provideSearchPresenter(searchUseCase: SearchUseCase): SearchPresenter {
        return SearchPresenter(searchUseCase)
    }
}