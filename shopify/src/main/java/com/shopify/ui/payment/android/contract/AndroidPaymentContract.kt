package com.shopify.ui.payment.android.contract

import android.app.Activity
import android.content.Intent
import com.domain.interactor.base.SingleUseCase
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wallet.FullWallet
import com.google.android.gms.wallet.MaskedWallet
import com.repository.CartRepository
import com.shopify.ShopifyWrapper
import com.shopify.buy3.pay.PayCart
import com.shopify.buy3.pay.PayHelper
import com.shopify.entity.Checkout
import com.shopify.repository.CheckoutRepository
import com.ui.base.contract.BasePresenter
import com.ui.base.contract.BaseView
import io.reactivex.Single
import javax.inject.Inject


interface AndroidPaymentView : BaseView<Boolean> {

    fun paymentCanceled()
}

class AndroidPaymentPresenter constructor(
        private val androidPayCartUseCase: AndroidPayCartUseCase,
        private val androidPaymentCompleteUseCase: AndroidPaymentCompleteUseCase
) :
        BasePresenter<Boolean, AndroidPaymentView>(arrayOf(androidPayCartUseCase)) {

    private var googleApiClient: GoogleApiClient? = null
    private var payCart: PayCart? = null
    private var checkout: Checkout? = null

    fun createPayCart(googleApiClient: GoogleApiClient, checkout: Checkout) {
        this.googleApiClient = googleApiClient
        this.checkout = checkout
        androidPayCartUseCase.execute(
                {
                    this.payCart = it
                    confirmCheckout(googleApiClient, it)
                },
                { resolveError(it) },
                checkout
        )
    }

    private fun confirmCheckout(googleApiClient: GoogleApiClient, payCart: PayCart) {
        PayHelper.requestMaskedWallet(googleApiClient, payCart, ShopifyWrapper.ANDROID_PAY_PUBLIC_KEY)
    }

    private fun completeCheckout(fullWallet: FullWallet) {
        val checkoutIdValue = checkout
        val payCartValue = payCart

        if (checkoutIdValue != null && payCartValue != null) {
            androidPaymentCompleteUseCase.execute(
                    { view?.showContent(it) },
                    { resolveError(it) },
                    AndroidPaymentCompleteUseCase.Params(checkoutIdValue, payCartValue, fullWallet)
            )
        }
    }

    fun handleMaskedWalletResponse(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_FIRST_USER || resultCode == Activity.RESULT_CANCELED) {
            view?.paymentCanceled()
        } else {
            PayHelper.handleWalletResponse(requestCode, resultCode, data, object : PayHelper.WalletResponseHandler() {
                override fun onWalletError(requestCode: Int, errorCode: Int) {
                    view?.paymentCanceled()
                }

                override fun onMaskedWallet(maskedWallet: MaskedWallet) {
                    requestFullWallet(maskedWallet)
                }

                override fun onFullWallet(fullWallet: FullWallet) {
                    super.onFullWallet(fullWallet)
                    completeCheckout(fullWallet)
                }
            })
        }
    }

    private fun requestFullWallet(maskedWallet: MaskedWallet) {
        if (googleApiClient != null && payCart != null) {
            PayHelper.requestFullWallet(googleApiClient, payCart, maskedWallet)
        }
    }
}

class AndroidPayCartUseCase @Inject constructor(
        private val cartRepository: CartRepository,
        private val checkoutRepository: CheckoutRepository
) :
        SingleUseCase<PayCart, Checkout>() {

    override fun buildUseCaseSingle(params: Checkout): Single<PayCart> {
        return cartRepository.getCartProductList()
                .first(listOf())
                .flatMap { checkoutRepository.createPayCart(params, it) }
    }
}

class AndroidPaymentCompleteUseCase @Inject constructor(
        private val checkoutRepository: CheckoutRepository
) :
        SingleUseCase<Boolean, AndroidPaymentCompleteUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<Boolean> {
        return with(params) {
            checkoutRepository.completeCheckoutByAndroidPay(checkout, payCart, fullWallet)
        }
    }

    class Params(
            val checkout: Checkout,
            val payCart: PayCart,
            val fullWallet: FullWallet
    )
}