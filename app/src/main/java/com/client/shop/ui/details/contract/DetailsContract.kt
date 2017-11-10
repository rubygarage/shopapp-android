package com.client.shop.ui.details.contract

import com.client.shop.R
import com.client.shop.ui.base.contract.BasePresenter
import com.client.shop.ui.base.contract.BaseView
import com.client.shop.ui.base.contract.SingleUseCase
import com.domain.entity.CartProduct
import com.domain.entity.Product
import com.domain.entity.ProductVariant
import com.repository.CartRepository
import com.repository.ProductRepository
import io.reactivex.Single
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

class DetailsProductUseCase @Inject constructor(private val productRepository: ProductRepository) :
        SingleUseCase<Product, String>() {

    override fun buildUseCaseSingle(params: String): Single<Product> {
        return productRepository.getProduct(params)
    }
}

class DetailsCartUseCase @Inject constructor(private val cartRepository: CartRepository) :
        SingleUseCase<CartProduct, CartProduct>() {

    override fun buildUseCaseSingle(params: CartProduct): Single<CartProduct> {
        return cartRepository.addCartProduct(params)
    }
}