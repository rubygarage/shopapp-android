package com.shopify.ui.shipping

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.domain.entity.Address
import com.shopify.ShopifyWrapper
import com.shopify.api.R
import com.shopify.entity.Checkout
import com.shopify.entity.ShippingRate
import com.shopify.ui.payment.android.AndroidPaymentActivity
import com.shopify.ui.payment.card.CardPaymentActivity
import com.shopify.ui.shipping.contract.ShippingPresenter
import com.shopify.ui.shipping.contract.ShippingView
import com.shopify.ui.shipping.di.ShippingModule
import com.ui.base.lce.BaseActivity
import com.ui.base.lce.view.LceLayout
import kotlinx.android.synthetic.main.activity_shipping.*
import javax.inject.Inject

class ShippingActivity : BaseActivity<List<ShippingRate>, ShippingView, ShippingPresenter>(),
        ShippingView,
        ShippingBottomSheet.OnShippingRateSelectListener {

    @Inject lateinit var shippingPresenter: ShippingPresenter
    private lateinit var checkout: Checkout
    private lateinit var payment: String
    private lateinit var email: String
    private lateinit var billingAddress: Address
    private var shippingAddress: Address? = null

    companion object {
        private const val CARD_PAYMENT = "card_payment"
        private const val ANDROID_PAYMENT = "android_payment"
        private const val CHECKOUT = "checkout"
        private const val PAYMENT = "payment"

        fun getStartIntentWithCard(context: Context, checkout: Checkout): Intent =
                getStartIntent(context, CARD_PAYMENT, checkout)

        fun getStartIntentWithAndroid(context: Context, checkout: Checkout): Intent =
                getStartIntent(context, ANDROID_PAYMENT, checkout)

        private fun getStartIntent(context: Context, payment: String, checkout: Checkout): Intent {
            val intent = Intent(context, ShippingActivity::class.java)
            intent.putExtra(PAYMENT, payment)
            intent.putExtra(CHECKOUT, checkout)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(getString(R.string.checkout))
        checkout = intent.getParcelableExtra(CHECKOUT)
        payment = intent.getStringExtra(PAYMENT)

        setupViews()
    }

    //INIT

    override fun inject() {
        ShopifyWrapper.component.attachShippingComponent(ShippingModule()).inject(this)
    }

    override fun getContentView() = R.layout.activity_shipping

    override fun createPresenter() = shippingPresenter

    //SETUP

    private fun setupViews() {
        val adapter = AddressPageAdapter(this, sameAddressCheckBox.isChecked, supportFragmentManager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        sameAddressCheckBox.visibility = if (payment == ANDROID_PAYMENT) View.GONE else View.VISIBLE

        sameAddressCheckBox.setOnCheckedChangeListener { _, isChecked ->
            adapter.sameAddress = isChecked
            adapter.notifyDataSetChanged()
            viewPager.currentItem = 0
            tabLayout.visibility = if (isChecked) View.GONE else View.VISIBLE
        }

        applyAddressButton.setOnClickListener {

            val billingAddressFragment = adapter.getBillingFragment(viewPager)
            val shippingAddressFragment = adapter.getShippingFragment(viewPager)

            if (billingAddressFragment != null && shippingAddressFragment != null) {
                val email = billingAddressFragment.getValidEmail()
                val billingAddress = billingAddressFragment.getValidAddress()
                val shippingAddress = shippingAddressFragment.getValidAddress()
                if ((email != null && billingAddress != null)) {
                    this.email = email
                    this.billingAddress = billingAddress
                    if (!sameAddressCheckBox.isChecked && shippingAddress == null) {
                        showMessage(R.string.shipping_empty_fields_error)
                    } else {
                        this.shippingAddress = shippingAddress
                        loadData()
                    }
                } else {
                    showMessage(R.string.address_empty_fields_error)
                }
            }
        }
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.getShippingRates(checkout.checkoutId, email, shippingAddress ?: billingAddress)
    }

    override fun showContent(data: List<ShippingRate>) {
        super.showContent(data)
        if (data.isNotEmpty()) {
            ShippingBottomSheet(this, data, this).show()
        } else {
            showMessage(R.string.empty_shipping_rates_message)
        }
    }

    override fun shippingRateSelected(checkout: Checkout) {
        changeState(LceLayout.LceState.ContentState)
        when (payment) {
            CARD_PAYMENT -> {
                startActivity(CardPaymentActivity.getStartIntent(this, checkout, billingAddress))
            }
            ANDROID_PAYMENT -> {
                startActivity(AndroidPaymentActivity.getStartIntent(this, checkout))
            }
        }
    }

    //CALLBACK

    override fun onShippingRateSelected(shippingRate: ShippingRate) {
        changeState(LceLayout.LceState.LoadingState)
        presenter?.selectShippingRate(checkout.checkoutId, shippingRate)
    }
}