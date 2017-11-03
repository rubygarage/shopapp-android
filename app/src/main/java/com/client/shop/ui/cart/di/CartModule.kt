package com.client.shop.ui.cart.di

import com.client.shop.ui.cart.contract.CartPresenter
import com.repository.Repository
import dagger.Module
import dagger.Provides

@Module
class CartModule {

    @Provides
    fun provideCartPresenter(repository: Repository): CartPresenter {
        return CartPresenter(repository)
    }
}