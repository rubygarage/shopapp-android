package com.client.shop.ui.product.di

import com.client.shop.ui.product.contract.ProductListPresenter
import com.domain.interactor.recent.ProductListUseCase
import com.nhaarman.mockito_kotlin.mock
import dagger.Module
import dagger.Provides

@Module
class TestProductHorizontalModule {

    @Provides
    fun provideProductListPresenter(): ProductListPresenter = mock()
}