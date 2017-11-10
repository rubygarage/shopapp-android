package com.client.shop.ui.details.di

import com.client.shop.ui.details.contract.DetailsCartUseCase
import com.client.shop.ui.details.contract.DetailsPresenter
import com.client.shop.ui.details.contract.DetailsProductUseCase
import dagger.Module
import dagger.Provides

@Module
class DetailsModule {

    @Provides
    fun provideBlogPresenter(detailsProductUseCase: DetailsProductUseCase, detailsCartUseCase: DetailsCartUseCase): DetailsPresenter {
        return DetailsPresenter(detailsProductUseCase, detailsCartUseCase)
    }
}