package com.shopify.ui.payment.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import com.shopify.ShopifyWrapper
import com.shopify.api.BuildConfig
import com.shopify.api.R
import com.shopify.entity.Checkout
import com.shopify.ui.payment.android.contract.AndroidPaymentPresenter
import com.shopify.ui.payment.android.contract.AndroidPaymentView
import com.shopify.ui.payment.android.di.AndroidPaymentModule
import com.ui.base.lce.BaseActivity
import kotlinx.android.synthetic.main.activity_android_payment.*
import javax.inject.Inject


class AndroidPaymentActivity :
        BaseActivity<Boolean, AndroidPaymentView, AndroidPaymentPresenter>(),
        AndroidPaymentView,
        GoogleApiClient.ConnectionCallbacks {

    @Inject lateinit var androidPaymentPresenter: AndroidPaymentPresenter
    private lateinit var checkout: Checkout
    private lateinit var googleApiClient: GoogleApiClient
    private var environment: Int = WalletConstants.ENVIRONMENT_TEST

    companion object {
        private const val CHECKOUT = "checkout"

        fun getStartIntent(context: Context, checkout: Checkout): Intent {
            val intent = Intent(context, AndroidPaymentActivity::class.java)
            return intent
        }
    }

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.checkout))

        environment = if (BuildConfig.DEBUG) {
            WalletConstants.ENVIRONMENT_TEST
        } else {
            WalletConstants.ENVIRONMENT_PRODUCTION
        }

        connectGoogleApiClient()
        payButton.setOnClickListener { loadData() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.handleMaskedWalletResponse(requestCode, resultCode, data)
    }

    override fun onStop() {
        super.onStop()
        if (googleApiClient.isConnected) {
            googleApiClient.disconnect()
        }
    }

    //INIT

    override fun inject() {
        ShopifyWrapper.component.attachAndroidPaymentComponent(AndroidPaymentModule()).inject(this)
    }

    override fun getContentView() = R.layout.activity_android_payment

    override fun createPresenter() = androidPaymentPresenter

    //SETUP

    private fun connectGoogleApiClient() {

        googleApiClient = GoogleApiClient.Builder(this)
                .addApi<Wallet.WalletOptions>(Wallet.API, Wallet.WalletOptions.Builder()
                        .setEnvironment(environment)
                        .setTheme(WalletConstants.THEME_DARK)
                        .build())
                .addConnectionCallbacks(this)
                .build()
        googleApiClient.connect()
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.createPayCart(googleApiClient, checkout)
    }

    override fun showContent(data: Boolean) {
        super.showContent(data)
        if (data) {
            //TODO add checkout polling
        } else {
            showMessage(R.string.payment_canceled)
            finish()
        }
    }

    override fun paymentCanceled() {
        showMessage(R.string.payment_canceled)
        finish()
    }

    //CALLBACK

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }
}