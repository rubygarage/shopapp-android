package com.shopify.ui.checkout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.shopify.ShopifyWrapper
import com.shopify.api.R
import com.shopify.ui.checkout.contract.CheckoutPresenter
import com.shopify.ui.checkout.contract.CheckoutView
import com.shopify.ui.checkout.di.CheckoutModule
import com.ui.browser.BrowserActivity
import com.ui.lce.BaseActivity
import kotlinx.android.synthetic.main.activity_checkout.*
import javax.inject.Inject

class CheckoutActivity : BaseActivity<Pair<String, String>, CheckoutView, CheckoutPresenter>(), CheckoutView {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, CheckoutActivity::class.java)
    }

    @Inject lateinit var checkoutPresenter: CheckoutPresenter
    private var checkoutId: String? = null
    private var webUrl: String? = null

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        webPaymentButton.setOnClickListener { startWebPaymentFlow() }

        loadData(false)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //INIT

    override fun inject() {
        ShopifyWrapper.component
                .attachCheckoutComponent(CheckoutModule())
                .inject(this)
    }

    override fun getContentView() = R.layout.activity_checkout

    override fun createPresenter() = checkoutPresenter

    //SETUP

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.createCheckout()
    }

    override fun showContent(data: Pair<String, String>) {
        super.showContent(data)
        checkoutId = data.first
        webUrl = data.second
    }

    //CALLBACK

    private fun startWebPaymentFlow() {
        webUrl?.let {
            startActivity(BrowserActivity.getStartIntent(this, it, getString(R.string.checkout)))
        }
    }
}