package com.shopapp.ui.product.di

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ui.product.contract.ProductListPresenter
import dagger.Module
import dagger.Provides

@Module
class TestProductHorizontalModule {

    @Provides
    fun provideProductListPresenter(): ProductListPresenter = mock()
}