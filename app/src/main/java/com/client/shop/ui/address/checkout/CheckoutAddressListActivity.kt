package com.client.shop.ui.address.checkout

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.address.base.BaseAddressListActivity
import com.client.shop.ui.address.checkout.adapter.CheckoutAddressListAdapter
import com.client.shop.ui.address.checkout.contract.CheckoutAddressListPresenter
import com.client.shop.ui.address.checkout.contract.CheckoutAddressListView
import com.client.shop.ui.address.checkout.di.CheckoutAddressModule
import com.client.shop.ui.base.lce.view.LceLayout
import com.client.shop.ui.const.RequestCode
import com.client.shop.getaway.entity.Address
import com.shopify.constant.Extra

class CheckoutAddressListActivity :
    BaseAddressListActivity<CheckoutAddressListAdapter, CheckoutAddressListView, CheckoutAddressListPresenter>(),
    CheckoutAddressListView,
    CheckoutAddressListAdapter.AddressSelectListener {

    companion object {

        private const val CHECKOUT_ID = "checkout_id"
        private const val SELECTED_ADDRESS = "selected_address"
        private const val IS_SHIPPING = "is_shipping"
        private const val SHIPPING_ADDRESS = "shipping_address"
        private const val BILLING_ADDRESS = "billing_address"

        fun getStartIntent(
            context: Context,
            checkoutId: String? = null,
            selectedAddress: Address? = null,
            isShippingAddress: Boolean,
            shippingAddress: Address?,
            billingAddress: Address?
        ): Intent {
            val intent = Intent(context, CheckoutAddressListActivity::class.java)
            intent.putExtra(CHECKOUT_ID, checkoutId)
            intent.putExtra(IS_SHIPPING, isShippingAddress)
            intent.putExtra(SELECTED_ADDRESS, selectedAddress)
            intent.putExtra(SHIPPING_ADDRESS, shippingAddress)
            intent.putExtra(BILLING_ADDRESS, billingAddress)
            return intent
        }
    }

    private var checkoutId: String? = null
    private var isShipping = true
    private var selectedAddress: Address? = null
    private var shippingAddress: Address? = null
    private var billingAddress: Address? = null
    private val resultIntent = Intent()

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkoutId = intent.getStringExtra(CHECKOUT_ID)
        isShipping = intent.getBooleanExtra(IS_SHIPPING, true)
        shippingAddress = intent.getParcelableExtra(SHIPPING_ADDRESS)
        billingAddress = intent.getParcelableExtra(BILLING_ADDRESS)

        setTitle(getString(if (isShipping) R.string.shipping_address else R.string.billing_address))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val isAddAddressRequest = requestCode == RequestCode.ADD_SHIPPING_ADDRESS
        val isEditAddressRequest = requestCode == RequestCode.EDIT_SHIPPING_ADDRESS
        val isResultOk = resultCode == Activity.RESULT_OK

        if (isAddAddressRequest && isResultOk) {
            loadData()
        } else if (isEditAddressRequest && isResultOk) {
            val address: Address? = data?.getParcelableExtra(Extra.ADDRESS)
            val isSelectedAddress = data?.getBooleanExtra(Extra.IS_SELECTED_ADDRESS, false) == true
            if (address != null && isSelectedAddress) {
                onAddressSelected(address)
            }
            loadData()
        }

        if ((isAddAddressRequest || isEditAddressRequest) && isResultOk) {
            loadData()
        }
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachCheckoutAddressComponent().inject(this)
    }

    override fun getAdapter(): CheckoutAddressListAdapter {
        selectedAddress = intent.getParcelableExtra(SELECTED_ADDRESS)
        val adapter = CheckoutAddressListAdapter(dataList, this, this, this)
        adapter.selectedAddress = selectedAddress
        return adapter
    }

    //SETUP

    override fun deleteAddress(address: Address) {
        if (selectedAddress == address) {
            selectedAddress = defaultAddress
            selectedAddress?.let { onAddressSelected(it) }
        }
        super.deleteAddress(address)
    }

    private fun doAction(address: Address, isEdit: Boolean, action: () -> Unit) {
        if (shippingAddress == address || billingAddress == address) {
            val messageRes: Int
            val positiveButtonRes: Int

            if (isEdit) {
                messageRes = R.string.edit_address_message
                positiveButtonRes = R.string.edit
            } else {
                messageRes = R.string.delete_address_message
                positiveButtonRes = R.string.delete
            }

            AlertDialog.Builder(this)
                .setMessage(messageRes)
                .setNegativeButton(R.string.cancel_button, { dialog, _ ->
                    dialog.dismiss()
                })
                .setPositiveButton(positiveButtonRes, { dialog, _ ->
                    dialog.dismiss()
                    val name = if (isShipping) Extra.CLEAR_BILLING else Extra.CLEAR_SHIPPING
                    resultIntent.putExtra(name, true)
                    setResult(Activity.RESULT_OK, resultIntent)
                    action()
                })
                .create()
                .show()
        } else {
            action()
        }
    }

    //LCE

    override fun selectedAddressChanged(address: Address) {
        changeState(LceLayout.LceState.ContentState)
        resultIntent.putExtra(Extra.ADDRESS, address)
        resultIntent.putExtra(Extra.IS_ADDRESS_CHANGED, true)
        setResult(Activity.RESULT_OK, resultIntent)
    }

    //CALLBACK

    override fun onEditButtonClicked(address: Address) {
        doAction(address, true, {
            val isSelectedAddress = address == selectedAddress
            startActivityForResult(
                CheckoutAddressActivity.getStartIntent(this, address, isSelectedAddress),
                RequestCode.EDIT_SHIPPING_ADDRESS
            )
        })
    }

    override fun onDeleteButtonClicked(address: Address) {
        doAction(address, false, {
            deleteAddress(address)
        })
    }

    override fun onAddressSelected(address: Address) {
        selectedAddress = address
        addressListAdapter.selectedAddress = selectedAddress
        addressListAdapter.notifyDataSetChanged()
        if (isShipping) {
            changeState(LceLayout.LceState.LoadingState(true))
            checkoutId?.let { presenter.setShippingAddress(it, address) }
        } else {
            selectedAddressChanged(address)
        }
    }

    override fun onClick(v: View?) {
        startActivityForResult(
            CheckoutAddressActivity.getStartIntent(this), RequestCode.ADD_SHIPPING_ADDRESS
        )
    }
}