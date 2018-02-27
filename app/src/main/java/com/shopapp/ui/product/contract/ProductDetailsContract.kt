package com.shopapp.ui.product.contract

import com.shopapp.R
import com.shopapp.domain.interactor.cart.CartAddItemUseCase
import com.shopapp.domain.interactor.product.ProductDetailsUseCase
import com.shopapp.gateway.entity.CartProduct
import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.ProductVariant
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import javax.inject.Inject

interface ProductDetailsView : BaseLceView<Product> {

    fun productAddedToCart()
}

class ProductDetailsPresenter @Inject constructor(
    private val productDetailsUseCase: ProductDetailsUseCase,
    private val cartAddItemUseCase: CartAddItemUseCase
) : BaseLcePresenter<Product, ProductDetailsView>(productDetailsUseCase, cartAddItemUseCase) {

    fun loadProductDetails(productId: String) {

        productDetailsUseCase.execute(
            { view?.showContent(it) },
            { resolveError(it) },
            productId
        )
    }

    fun addProductToCart(
        productVariant: ProductVariant,
        productTitle: String,
        currency: String,
        quantityStr: String
    ) {

        val quantity = quantityStr.toIntOrNull() ?: 0
        if (quantity <= 0) {
            view?.showMessage(R.string.quantity_warning_message)
        } else {
            val cartProduct = CartProduct(productVariant, productTitle, currency, quantity)
            cartAddItemUseCase.execute(
                { view?.productAddedToCart() },
                { resolveError(it) },
                cartProduct
            )
        }
    }
}