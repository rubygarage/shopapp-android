package com.shopapp.domain.interactor.checkout

import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.AuthRepository
import com.shopapp.domain.repository.CartRepository
import com.shopapp.domain.repository.CheckoutRepository
import com.shopapp.gateway.entity.CartProduct
import com.shopapp.gateway.entity.Checkout
import com.shopapp.gateway.entity.Customer
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class SetupCheckoutUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val checkoutRepository: CheckoutRepository,
    private val authRepository: AuthRepository
) :
    SingleUseCase<Triple<List<CartProduct>, Checkout, Customer?>, Unit>() {

    override fun buildUseCaseSingle(params: Unit): Single<Triple<List<CartProduct>, Checkout, Customer?>> {
        return createCheckout().flatMap { combineWithCustomer(it) }
            .flatMap { setShippingAddress(it) }
    }

    private fun createCheckout(): Single<Pair<List<CartProduct>, Checkout>> {
        return cartRepository.getCartProductList()
            .first(emptyList())
            .flatMap {
                Single.zip(Single.just(it), checkoutRepository.createCheckout(it),
                    BiFunction<List<CartProduct>, Checkout,
                            Pair<List<CartProduct>, Checkout>> { products, checkout -> Pair(products, checkout) })
            }
    }

    private fun combineWithCustomer(checkoutData: Pair<List<CartProduct>, Checkout>): Single<Triple<List<CartProduct>, Checkout, Customer?>> {
        return Single.zip(Single.just(checkoutData), getCustomer(),
            BiFunction<Pair<List<CartProduct>, Checkout>, Any?,
                    Triple<List<CartProduct>, Checkout, Customer?>> { pair, customerOpt ->
                val customer = customerOpt as? Customer
                Triple(pair.first, pair.second, customer)
            })
    }

    private fun getCustomer(): Single<Any?> {
        return authRepository.isLoggedIn().flatMap {
            if (it) {
                return@flatMap authRepository.getCustomer()
            } else {
                return@flatMap Single.just(Any())
            }
        }
    }

    private fun setShippingAddress(data: Triple<List<CartProduct>, Checkout, Customer?>): Single<Triple<List<CartProduct>, Checkout, Customer?>> {
        val (_, checkout, customer) = data
        val defaultAddress = customer?.defaultAddress
        return if (defaultAddress != null) {
            checkoutRepository.setShippingAddress(checkout.checkoutId, defaultAddress)
                .map { Triple(data.first, it, data.third) }
                .onErrorResumeNext(Single.just(data))
        } else {
            Single.just(data)
        }
    }
}