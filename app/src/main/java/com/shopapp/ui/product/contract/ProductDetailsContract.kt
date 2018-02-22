package com.shopapp.ui.product.contract

import com.shopapp.gateway.entity.CartProduct
import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.ProductVariant
import com.shopapp.domain.interactor.details.DetailsCartUseCase
import com.shopapp.domain.interactor.details.DetailsProductUseCase
import com.shopapp.R
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import javax.inject.Inject

interface DetailsView : BaseLceView<Product> {

    fun productAddedToCart()
}

class DetailsPresenter @Inject constructor(
    private val detailsProductUseCase: DetailsProductUseCase,
    private val cartUseCase: DetailsCartUseCase
) : BaseLcePresenter<Product, DetailsView>(detailsProductUseCase, cartUseCase) {

    fun loadProductDetails(productId: String) {

        detailsProductUseCase.execute(
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
            cartUseCase.execute(
                { view?.productAddedToCart() },
                { resolveError(it) },
                cartProduct
            )
        }
    }
}