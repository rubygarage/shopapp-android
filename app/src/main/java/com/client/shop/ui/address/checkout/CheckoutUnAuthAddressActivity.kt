package com.client.shop.ui.address.checkout

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.address.base.BaseAddressActivity
import com.client.shop.ui.address.base.contract.AddressView
import com.client.shop.ui.address.checkout.contract.CheckoutUnAuthAddressPresenter
import com.client.shop.gateway.entity.Address
import com.domain.validator.FieldValidator
import com.client.shop.ui.Extra
import javax.inject.Inject

class CheckoutUnAuthAddressActivity : BaseAddressActivity<AddressView, CheckoutUnAuthAddressPresenter>() {

    companion object {

        private const val CHECKOUT_ID = "checkout_id"
        private const val ADDRESS = "address"
        private const val IS_SHIPPING = "is_shipping"

        fun getStartIntent(
            context: Context,
            checkoutId: String? = null,
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
    private var checkoutId: String? = null
    private var isShippingAddress = false

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkoutId = intent.getStringExtra(CHECKOUT_ID)
        isShippingAddress = intent.getBooleanExtra(IS_SHIPPING, false)
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
                if (isEditMode) {
                    presenter.editShippingAddress(it, address)
                } else {
                    presenter.submitShippingAddress(it, address)
                }
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