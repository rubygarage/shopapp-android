package com.shopapp.ui.product.di

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ui.product.contract.ProductDetailsPresenter
import com.shopapp.ui.product.contract.ProductListPresenter
import com.shopapp.ui.product.router.ProductRouter
import dagger.Module
import dagger.Provides

@Module
class TestProductModule {

    @Provides
    fun provideProductDetailsPresenter(): ProductDetailsPresenter = mock()

    @Provides
    fun provideProductListPresenter(): ProductListPresenter = mock()


    @Provides
    fun provideProductRouter(): ProductRouter = mock()
}