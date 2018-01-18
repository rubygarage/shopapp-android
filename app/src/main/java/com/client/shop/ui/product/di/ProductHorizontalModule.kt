package com.client.shop.ui.product.di

import com.client.shop.ui.product.contract.ProductListPresenter
import com.domain.interactor.recent.ProductListUseCase
import dagger.Module
import dagger.Provides

@Module
class ProductHorizontalModule {

    @Provides
    fun provideProductListPresenter(productListUseCase: ProductListUseCase): ProductListPresenter {
        return ProductListPresenter(productListUseCase)
    }
}