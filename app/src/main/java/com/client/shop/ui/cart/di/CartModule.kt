package com.client.shop.ui.cart.di

import com.client.shop.ui.cart.contract.*
import dagger.Module
import dagger.Provides

@Module
class CartModule {

    @Provides
    fun provideCartPresenter(cartItemsUseCase: CartItemsUseCase,
                             cartRemoveUseCase: CartRemoveUseCase,
                             cartQuantityUseCase: CartQuantityUseCase): CartPresenter {
        return CartPresenter(cartItemsUseCase, cartRemoveUseCase, cartQuantityUseCase)
    }

    @Provides
    fun provideCartWidgetPresenter(cartItemsUseCase: CartItemsUseCase): CartWidgetPresenter {
        return CartWidgetPresenter(cartItemsUseCase)
    }
}