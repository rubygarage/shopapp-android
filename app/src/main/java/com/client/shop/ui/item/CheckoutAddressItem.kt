package com.client.shop.ui.item

import android.content.Context
import android.widget.CompoundButton
import com.client.shop.R
import com.client.shop.ui.address.checkout.adapter.CheckoutAddressListAdapter
import com.client.shop.gateway.entity.Address
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