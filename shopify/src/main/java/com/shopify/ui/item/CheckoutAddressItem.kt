package com.shopify.ui.item

import android.content.Context
import android.widget.CompoundButton
import com.domain.entity.Address
import com.shopify.api.R
import com.shopify.ui.address.adapter.CheckoutAddressListAdapter
import com.ui.item.AddressItem
import kotlinx.android.synthetic.main.item_checkout_address.view.*

class CheckoutAddressItem(context: Context) :
    AddressItem(context),
    CompoundButton.OnCheckedChangeListener {

    var addressSelectListener: CheckoutAddressListAdapter.AddressSelectListener? = null

    override fun getLayoutId() = R.layout.item_checkout_address

    fun setAddress(address: Address, defaultAddress: Address?, selectedAddress: Address?) {
        setAddress(address, defaultAddress)
        selectedAddressRadioButton.setOnCheckedChangeListener(null)
        selectedAddressRadioButton.isChecked = address.id == selectedAddress?.id || address == selectedAddress
        selectedAddressRadioButton.setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            addressSelectListener?.onAddressSelected(address)
        }
    }
}