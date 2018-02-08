package com.client.shop.ui.cart.di

import com.client.shop.ui.cart.contract.CartPresenter
import com.client.shop.ui.cart.contract.CartWidgetPresenter
import com.domain.interactor.cart.CartItemsUseCase
import com.domain.interactor.cart.CartQuantityUseCase
import com.domain.interactor.cart.CartRemoveUseCase
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