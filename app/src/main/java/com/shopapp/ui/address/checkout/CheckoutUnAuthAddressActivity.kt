package com.shopapp.ui.address.checkout

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.domain.validator.FieldValidator
import com.shopapp.gateway.entity.Address
import com.shopapp.ui.address.base.BaseAddressActivity
import com.shopapp.ui.address.base.contract.AddressView
import com.shopapp.ui.address.checkout.contract.CheckoutUnAuthAddressPresenter
import com.shopapp.ui.const.Extra
import javax.inject.Inject

class CheckoutUnAuthAddressActivity : BaseAddressActivity<AddressView, CheckoutUnAuthAddressPresenter>() {

    companion object {

        fun getStartIntent(
            context: Context,
            checkoutId: String? = null,
            address: Address? = null,
            isShipping: Boolean = false
        ): Intent {
            val intent = Intent(context, CheckoutUnAuthAddressActivity::class.java)
            intent.putExtra(Extra.CHECKOUT_ID, checkoutId)
            intent.putExtra(Extra.ADDRESS, address)
            intent.putExtra(Extra.IS_SHIPPING, isShipping)
            return intent
        }

    }

    @Inject
    lateinit var fieldValidator: FieldValidator
    private var checkoutId: String? = null
    private var isShippingAddress = false

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkoutId = intent.getStringExtra(Extra.CHECKOUT_ID)
        isShippingAddress = intent.getBooleanExtra(Extra.IS_SHIPPING, false)
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachCheckoutAddressComponent().inject(this)
    }

    //SETUP

    override fun submitAddress() {
        val address = getAddress()
        if (isShippingAddress) {
            checkoutId?.let {
                presenter.submitShippingAddress(it, address)
            }
        } else {
            if (fieldValidator.isAddressValid(address)) {
                addressChanged(address)
            } else {
                showMessage(R.string.invalid_address)
            }
        }
    }

    //LCE

    override fun addressChanged(address: Address) {
        val result = Intent()
        result.putExtra(Extra.ADDRESS, address)
        result.putExtra(Extra.IS_ADDRESS_CHANGED, true)
        setResult(Activity.RESULT_OK, result)
        finish()
    }
}