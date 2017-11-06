package com.client.shop.ui.details.contract

import com.client.shop.R
import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.domain.entity.CartProduct
import com.domain.entity.Product
import com.domain.entity.ProductVariant
import com.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface DetailsView : BaseMvpView<Product> {

    fun productAddedToCart()
}

class DetailsPresenter @Inject constructor(repository: Repository) : BasePresenter<Product, DetailsView>(repository) {

    fun loadProductDetails(productId: String) {

        val detailsDisposable = repository.getProduct(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> view?.showContent(result) },
                        { e -> resolveError(e) }
                )

        disposables.add(detailsDisposable)
    }

    fun addProductToCart(productVariant: ProductVariant, productId: String, currency: String, quantityStr: String) {

        val quantity = quantityStr.toIntOrNull() ?: 0
        if (quantity <= 0) {
            view?.showMessage(R.string.quantity_warning_message)
        } else {
            val cartProduct = CartProduct(productVariant, productId, currency, quantity)
            val addToCartDisposable = repository.addCartProduct(cartProduct)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { view?.productAddedToCart() },
                            { e -> resolveError(e) }
                    )
            disposables.add(addToCartDisposable)
        }
    }
}