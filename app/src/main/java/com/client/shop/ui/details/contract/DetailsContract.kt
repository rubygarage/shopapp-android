package com.client.shop.ui.details.contract

import com.client.shop.R
import com.domain.entity.CartProduct
import com.domain.entity.Product
import com.domain.entity.ProductVariant
import com.domain.interactor.details.DetailsCartUseCase
import com.domain.interactor.details.DetailsProductUseCase
import com.ui.base.contract.BasePresenter
import com.ui.base.contract.BaseView
import javax.inject.Inject

interface DetailsView : BaseView<Product> {

    fun productAddedToCart()
}

class DetailsPresenter @Inject constructor(
        private val detailsProductUseCase: DetailsProductUseCase,
        private val cartUseCase: DetailsCartUseCase
) : BasePresenter<Product, DetailsView>(arrayOf(detailsProductUseCase, cartUseCase)) {

    fun loadProductDetails(productId: String) {

        detailsProductUseCase.execute(
                { view?.showContent(it) },
                { resolveError(it) },
                productId
        )
    }

    fun addProductToCart(productVariant: ProductVariant, productId: String, currency: String, quantityStr: String) {

        val quantity = quantityStr.toIntOrNull() ?: 0
        if (quantity <= 0) {
            view?.showMessage(R.string.quantity_warning_message)
        } else {
            val cartProduct = CartProduct(productVariant, productId, currency, quantity)
            cartUseCase.execute(
                    { view?.productAddedToCart() },
                    { resolveError(it) },
                    cartProduct
            )
        }
    }
}