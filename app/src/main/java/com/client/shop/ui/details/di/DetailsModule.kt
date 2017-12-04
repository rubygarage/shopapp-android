package com.client.shop.ui.details.di

import com.client.shop.ui.details.contract.DetailsPresenter
import com.domain.interactor.details.DetailsCartUseCase
import com.domain.interactor.details.DetailsProductUseCase
import dagger.Module
import dagger.Provides

@Module
class DetailsModule {

    @Provides
    fun provideBlogPresenter(detailsProductUseCase: DetailsProductUseCase, detailsCartUseCase: DetailsCartUseCase): DetailsPresenter {
        return DetailsPresenter(detailsProductUseCase, detailsCartUseCase)
    }
}