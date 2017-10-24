package com.client.shop.ui.details.di

import com.client.shop.ui.details.contract.DetailsPresenter
import com.repository.Repository
import dagger.Module
import dagger.Provides

@Module
class DetailsModule {

    @Provides
    fun provideBlogPresenter(repository: Repository): DetailsPresenter {
        return DetailsPresenter(repository)
    }
}