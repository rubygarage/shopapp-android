package com.shopapp.ui.product.di

import com.shopapp.domain.interactor.cart.CartAddItemUseCase
import com.shopapp.domain.interactor.product.ProductDetailsUseCase
import com.shopapp.domain.interactor.product.ProductListUseCase
import com.shopapp.ui.product.contract.ProductDetailsPresenter
import com.shopapp.ui.product.contract.ProductListPresenter
import com.shopapp.ui.product.router.ProductRouter
import dagger.Module
import dagger.Provides

@Module
class ProductModule {

    @Provides
    fun provideProductDetailsPresenter(productDetailsUseCase: ProductDetailsUseCase, cartAddItemUseCase: CartAddItemUseCase): ProductDetailsPresenter {
        return ProductDetailsPresenter(productDetailsUseCase, cartAddItemUseCase)
    }

    @Provides
    fun provideProductListPresenter(productListUseCase: ProductListUseCase): ProductListPresenter {
        return ProductListPresenter(productListUseCase)
    }

    @Provides
    fun provideProductRouter(): ProductRouter = ProductRouter()
}