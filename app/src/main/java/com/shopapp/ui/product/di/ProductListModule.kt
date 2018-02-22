package com.shopapp.ui.product.di

import com.shopapp.domain.interactor.recent.ProductListUseCase
import com.shopapp.ui.product.contract.ProductListPresenter
import dagger.Module
import dagger.Provides

@Module
class ProductListModule {

    @Provides
    fun provideProductListPresenter(productListUseCase: ProductListUseCase): ProductListPresenter {
        return ProductListPresenter(productListUseCase)
    }
}