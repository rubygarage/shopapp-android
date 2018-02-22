package com.shopapp.ui.popular.di

import com.shopapp.ui.product.contract.ProductListPresenter
import com.shopapp.domain.interactor.recent.ProductListUseCase
import dagger.Module
import dagger.Provides

@Module
class PopularModule {

    @Provides
    fun provideProductListPresenter(productListUseCase: ProductListUseCase): ProductListPresenter {
        return ProductListPresenter(productListUseCase)
    }
}