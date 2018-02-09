package com.client.shop.ui.product.contract

import com.client.shop.R
import com.client.shop.getaway.entity.CartProduct
import com.client.shop.getaway.entity.Product
import com.client.shop.getaway.entity.ProductVariant
import com.domain.interactor.details.DetailsCartUseCase
import com.domain.interactor.details.DetailsProductUseCase
import com.client.shop.ui.base.contract.BaseLcePresenter
import com.client.shop.ui.base.contract.BaseLceView
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