package com.shopapp.data.impl

import com.shopapp.gateway.Api
import com.shopapp.gateway.entity.*
import com.shopapp.data.rx.RxCallbackSingle
import com.shopapp.domain.repository.CheckoutRepository
import io.reactivex.Single

class CheckoutRepositoryImpl(private val api: Api) : CheckoutRepository {

    override fun createCheckout(cartProductList: List<CartProduct>): Single<Checkout> {
        return Single.create<Checkout> {
            api.createCheckout(cartProductList, RxCallbackSingle<Checkout>(it))
        }
    }

    override fun getCheckout(checkoutId: String): Single<Checkout> {
        return Single.create<Checkout> {
            api.getCheckout(checkoutId, RxCallbackSingle<Checkout>(it))
        }
    }

    override fun setShippingAddress(checkoutId: String, address: Address): Single<Checkout> {
        return Single.create {
            api.setShippingAddress(checkoutId, address, RxCallbackSingle<Checkout>(it))
        }
    }

    override fun getShippingRates(checkoutId: String): Single<List<ShippingRate>> {
        return Single.create<List<ShippingRate>> {
            api.getShippingRates(checkoutId, RxCallbackSingle<List<ShippingRate>>(it))
        }
    }

    override fun selectShippingRate(checkoutId: String, shippingRate: ShippingRate): Single<Checkout> {
        return Single.create<Checkout> {
            api.selectShippingRate(checkoutId, shippingRate, RxCallbackSingle<Checkout>(it))
        }
    }

    override fun getAcceptedCardTypes(): Single<List<CardType>> {
        return Single.create<List<CardType>> {
            api.getAcceptedCardTypes(RxCallbackSingle(it))
        }
    }

    override fun getCardToken(card: Card): Single<String> {
        return Single.create<String> {
            api.getCardToken(card, RxCallbackSingle<String>(it))
        }
    }

    override fun completeCheckoutByCard(checkout: Checkout, email: String, address: Address, creditCardVaultToken: String): Single<Order> {
        return Single.create<Order> {
            api.completeCheckoutByCard(checkout, email, address, creditCardVaultToken, RxCallbackSingle<Order>(it))
        }
    }
}