package com.client.shop.ui.product.di

import com.client.shop.ui.product.contract.DetailsPresenter
import com.domain.interactor.details.DetailsCartUseCase
import com.domain.interactor.details.DetailsProductUseCase
import dagger.Module
import dagger.Provides

@Module
class ProductDetailsModule {

    @Provides
    fun provideBlogPresenter(detailsProductUseCase: DetailsProductUseCase, detailsCartUseCase: DetailsCartUseCase): DetailsPresenter {
        return DetailsPresenter(detailsProductUseCase, detailsCartUseCase)
    }
}