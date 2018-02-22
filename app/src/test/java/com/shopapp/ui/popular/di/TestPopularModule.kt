package com.shopapp.ui.popular.di

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ui.product.contract.ProductListPresenter
import dagger.Module
import dagger.Provides

@Module
class TestPopularModule {

    @Provides
    fun provideProductListPresenter(): ProductListPresenter = mock()
}