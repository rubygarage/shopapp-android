package com.client.shop.ui.cart.contract

import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.domain.entity.CartProduct
import com.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface CartView : BaseMvpView<List<CartProduct>> {

}

class CartPresenter @Inject constructor(repository: Repository) :
        BasePresenter<List<CartProduct>, CartView>(repository) {

    fun loadCartItems() {

        val cartDisposable = repository.getCartProductList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            if (result.isNotEmpty()) {
                                view?.showContent(result)
                            } else {
                                view?.showEmptyState()
                            }
                        },
                        { error -> error.printStackTrace() })
        disposables.add(cartDisposable)
    }

    fun removeProduct(productVariantId: String) {

        val call = repository.deleteProductFromCart(productVariantId)

        call?.let {
            val deleteDisposable = it.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {},
                            { error -> error.printStackTrace() }
                    )
            disposables.add(deleteDisposable)
        }
    }

    fun changeProductQuantity(productVariantId: String, newQuantity: Int) {

        val call = repository.changeCartProductQuantity(productVariantId, newQuantity)

        call?.let {
            val quantityDisposable = it.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {},
                            { error -> error.printStackTrace() }
                    )
            disposables.add(quantityDisposable)
        }
    }
}