package com.shopapp.domain.interactor.cart

import com.shopapp.domain.interactor.base.CompletableUseCase
import com.shopapp.domain.repository.CartRepository
import com.shopapp.domain.repository.ProductRepository
import io.reactivex.Completable
import javax.inject.Inject

class CartValidationUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository
) :
    CompletableUseCase<Unit>() {

    override fun buildUseCaseCompletable(params: Unit): Completable {
        return cartRepository.getCartProductList()
            .map { it.map { it.productVariant.id } }
            .first(listOf())
            .flatMapCompletable {
                if (it.isEmpty()) {
                    Completable.complete()
                } else {
                    filterProductList(it)
                }
            }
    }

    private fun filterProductList(productIdList: List<String>): Completable {
        return productRepository.getProductVariantList(productIdList)
            .flatMapCompletable { productList ->
                val result = productIdList.filter { id -> productList.find { it.id == id } == null }
                if (result.isNotEmpty()) {
                    cartRepository.deleteProductListFromCart(result)
                } else {
                    Completable.complete()
                }
            }
    }

}