package com.shopify.repository.impl

import com.domain.entity.Card
import com.domain.entity.CartProduct
import com.repository.rx.RxCallback
import com.shopify.api.ShopifyApi
import com.shopify.entity.Checkout
import com.shopify.repository.CheckoutRepository
import io.reactivex.Single

class CheckoutRepositoryImpl(private val api: ShopifyApi): CheckoutRepository {

    override fun createCheckout(cartProductList: List<CartProduct>): Single<Checkout> {
        return Single.create<Checkout> { emitter ->
            api.createCheckout(cartProductList, RxCallback<Checkout>(emitter))
        }
    }

    override fun payByCard(card: Card): Single<String> {
        return Single.create<String> { emitter ->
            api.payByCard(card, RxCallback<String>(emitter))
        }
    }

    override fun completeCheckoutByCard(checkoutId: String, creditCardVaultToken: String): Single<Boolean> {
        return Single.create<Boolean> { emitter ->
            api.completeCheckoutByCard(checkoutId, creditCardVaultToken, RxCallback<Boolean>(emitter))
        }
    }
}