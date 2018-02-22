package com.shopapp.ui.product.di

import com.shopapp.ui.product.contract.ProductListPresenter
import com.shopapp.domain.interactor.recent.ProductListUseCase
import dagger.Module
import dagger.Provides

@Module
class ProductHorizontalModule {

    @Provides
    fun provideProductListPresenter(productListUseCase: ProductListUseCase): ProductListPresenter {
        return ProductListPresenter(productListUseCase)
    }
}