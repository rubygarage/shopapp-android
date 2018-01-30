package com.shopify.ui.address

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.domain.entity.Address
import com.domain.validator.FieldValidator
import com.shopify.ShopifyWrapper
import com.shopify.api.R
import com.shopify.constant.Extra
import com.shopify.ui.address.contract.CheckoutUnAuthAddressPresenter
import com.shopify.ui.address.di.AddressModule
import com.ui.module.address.BaseAddressActivity
import com.ui.module.address.contract.AddressView
import javax.inject.Inject

class CheckoutUnAuthAddressActivity : BaseAddressActivity<AddressView, CheckoutUnAuthAddressPresenter>() {

    companion object {

        private const val CHECKOUT_ID = "checkout_id"
        private const val ADDRESS = "address"
        private const val IS_SHIPPING = "is_shipping"

        fun getStartIntent(
            context: Context,
            checkoutId: String,
            address: Address? = null,
            isShipping: Boolean = false
        ): Intent {
            val intent = Intent(context, CheckoutUnAuthAddressActivity::class.java)
            intent.putExtra(CHECKOUT_ID, checkoutId)
            intent.putExtra(ADDRESS, address)
            intent.putExtra(IS_SHIPPING, isShipping)
            return intent
        }
    }

    @Inject
    lateinit var fieldValidator: FieldValidator
    private lateinit var checkoutId: String
    private var isShippingAddress = false

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkoutId = intent.getStringExtra(CHECKOUT_ID)
        isShippingAddress = intent.getBooleanExtra(IS_SHIPPING, false)
    }

    //INIT

    override fun inject() {
        ShopifyWrapper.component.attachAddressComponent(AddressModule()).inject(this)
    }

    //SETUP

    override fun submitAddress() {
        val address = getAddress()
        if (isShippingAddress) {
            if (isEditMode) {
                presenter.editShippingAddress(checkoutId, address)
            } else {
                presenter.submitShippingAddress(checkoutId, address)
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
        setResult(Activity.RESULT_OK, result)
        finish()
    }
}