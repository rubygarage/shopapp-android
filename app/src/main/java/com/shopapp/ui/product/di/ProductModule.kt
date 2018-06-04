package com.shopapp.ui.product.di

import com.shopapp.domain.interactor.cart.CartAddItemUseCase
import com.shopapp.domain.interactor.product.ProductDetailsUseCase
import com.shopapp.domain.interactor.product.GetProductsUseCase
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
    fun provideProductListPresenter(getProductsUseCase: GetProductsUseCase): ProductListPresenter {
        return ProductListPresenter(getProductsUseCase)
    }

    @Provides
    fun provideProductRouter(): ProductRouter = ProductRouter()
}