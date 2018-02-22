package com.shopapp.ui.item

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.shopapp.gateway.entity.Address
import com.shopapp.R
import com.shopapp.ui.address.base.adapter.AddressListAdapter
import kotlinx.android.synthetic.main.item_address.view.*

@Suppress("LeakingThis")
open class AddressItem(context: Context) : ConstraintLayout(context) {

    var actionListener: AddressListAdapter.ActionButtonListener? = null
    protected lateinit var address: Address

    init {
        View.inflate(context, getLayoutId(), this)
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        setBackgroundColor(Color.WHITE)
        editButton.setOnClickListener { actionListener?.onEditButtonClicked(address) }
        deleteButton.setOnClickListener { actionListener?.onDeleteButtonClicked(address) }
        defaultButton.setOnClickListener { actionListener?.onDefaultButtonClicked(address) }
    }

    protected open fun getLayoutId() = R.layout.item_address

    open fun setAddress(address: Address, defaultAddress: Address?) {
        this.address = address
        addressContent.setAddress(address)
        val isDefault = address.id == defaultAddress?.id || address == defaultAddress
        deleteButton.isEnabled = !isDefault
        defaultButton.isEnabled = !isDefault
    }
}