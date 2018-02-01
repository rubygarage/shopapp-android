package com.shopify.repository.impl

import com.data.rx.RxCallbackSingle
import com.domain.entity.Address
import com.domain.entity.Card
import com.domain.entity.CartProduct
import com.domain.entity.Order
import com.google.android.gms.wallet.FullWallet
import com.shopify.api.ShopifyApi
import com.shopify.buy3.pay.PayCart
import com.shopify.entity.Checkout
import com.shopify.entity.ShippingRate
import com.shopify.repository.CheckoutRepository
import io.reactivex.Single

class CheckoutRepositoryImpl(private val api: ShopifyApi) : CheckoutRepository {

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

    override fun createPayCart(checkout: Checkout, cartProductList: List<CartProduct>): Single<PayCart> {
        return Single.create<PayCart> {
            api.createAndroidPayCart(checkout, cartProductList, RxCallbackSingle<PayCart>(it))
        }
    }

    override fun completeCheckoutByAndroidPay(checkout: Checkout, payCart: PayCart,
                                              fullWallet: FullWallet): Single<Boolean> {
        return Single.create<Boolean> {
            api.completeCheckoutByAndroid(checkout, payCart, fullWallet, RxCallbackSingle<Boolean>(it))
        }
    }

}