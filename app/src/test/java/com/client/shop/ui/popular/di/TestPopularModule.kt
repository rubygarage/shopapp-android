package com.client.shop.ui.popular.di

import com.client.shop.ui.product.contract.ProductListPresenter
import com.nhaarman.mockito_kotlin.mock
import dagger.Module
import dagger.Provides

@Module
class TestPopularModule {

    @Provides
    fun provideProductListPresenter(): ProductListPresenter = mock()
}