package com.shopify.ui.address

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.domain.entity.Address
import com.shopify.ShopifyWrapper
import com.shopify.api.R
import com.shopify.constant.Extra
import com.shopify.ui.address.adapter.CheckoutAddressListAdapter
import com.shopify.ui.address.contract.CheckoutAddressListPresenter
import com.shopify.ui.address.contract.CheckoutAddressListView
import com.shopify.ui.address.di.AddressModule
import com.ui.base.lce.view.LceLayout
import com.ui.const.RequestCode
import com.ui.module.address.BaseAddressListActivity

class CheckoutAddressListActivity :
    BaseAddressListActivity<CheckoutAddressListAdapter, CheckoutAddressListView, CheckoutAddressListPresenter>(),
    CheckoutAddressListView,
    CheckoutAddressListAdapter.AddressSelectListener {

    companion object {

        private const val CHECKOUT_ID = "checkout_id"
        private const val SELECTED_ADDRESS = "selected_address"
        private const val IS_SHIPPING = "is_shipping"

        fun getStartIntent(
            context: Context,
            checkoutId: String? = null,
            isShipping: Boolean,
            selectedAddress: Address? = null
        ): Intent {
            val intent = Intent(context, CheckoutAddressListActivity::class.java)
            intent.putExtra(CHECKOUT_ID, checkoutId)
            intent.putExtra(IS_SHIPPING, isShipping)
            intent.putExtra(SELECTED_ADDRESS, selectedAddress)
            return intent
        }
    }

    private var checkoutId: String? = null
    private var isShipping = true
    private var selectedAddress: Address? = null

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkoutId = intent.getStringExtra(CHECKOUT_ID)
        isShipping = intent.getBooleanExtra(IS_SHIPPING, true)

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
        ShopifyWrapper.component.attachAddressComponent(AddressModule()).inject(this)
    }

    override fun getAdapter(): CheckoutAddressListAdapter {
        selectedAddress = intent.getParcelableExtra(SELECTED_ADDRESS)
        val adapter = CheckoutAddressListAdapter(dataList, this, this, this)
        adapter.selectedAddress = selectedAddress
        return adapter
    }

    //LCE

    override fun selectedAddressChanged(address: Address) {
        changeState(LceLayout.LceState.ContentState)
        val resultIntent = Intent()
        resultIntent.putExtra(Extra.ADDRESS, address)
        setResult(Activity.RESULT_OK, resultIntent)
    }

    //CALLBACK

    override fun onEditButtonClicked(address: Address) {
        val isSelectedAddress = address == selectedAddress
        startActivityForResult(
            CheckoutAddressActivity.getStartIntent(this, address, isSelectedAddress),
            RequestCode.EDIT_SHIPPING_ADDRESS
        )
    }

    override fun onDeleteButtonClicked(address: Address) {
        if (selectedAddress == address) {
            selectedAddress = defaultAddress
            selectedAddress?.let { onAddressSelected(it) }
        }
        super.onDeleteButtonClicked(address)
    }

    override fun onAddressSelected(address: Address) {
        selectedAddress = address
        addressListAdapter.selectedAddress = selectedAddress
        addressListAdapter.notifyDataSetChanged()
        if (isShipping) {
            changeState(LceLayout.LceState.LoadingState)
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