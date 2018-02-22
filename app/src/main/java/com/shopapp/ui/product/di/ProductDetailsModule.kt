package com.shopapp.ui.product.di

import com.shopapp.domain.interactor.details.DetailsCartUseCase
import com.shopapp.domain.interactor.details.DetailsProductUseCase
import com.shopapp.ui.product.contract.DetailsPresenter
import dagger.Module
import dagger.Provides

@Module
class ProductDetailsModule {

    @Provides
    fun provideBlogPresenter(detailsProductUseCase: DetailsProductUseCase, detailsCartUseCase: DetailsCartUseCase): DetailsPresenter {
        return DetailsPresenter(detailsProductUseCase, detailsCartUseCase)
    }
}