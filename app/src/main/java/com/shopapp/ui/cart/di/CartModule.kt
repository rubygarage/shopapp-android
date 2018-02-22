package com.shopapp.ui.cart.di

import com.shopapp.ui.cart.contract.CartWidgetPresenter
import com.shopapp.domain.interactor.cart.CartItemsUseCase
import com.shopapp.domain.interactor.cart.CartQuantityUseCase
import com.shopapp.domain.interactor.cart.CartRemoveUseCase
import com.shopapp.ui.cart.contract.CartPresenter
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