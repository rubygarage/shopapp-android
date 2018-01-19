package com.shopify.ui.item

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.CompoundButton
import com.domain.entity.Address
import com.shopify.api.R
import kotlinx.android.synthetic.main.item_address.view.*

class AddressItem(context: Context) : ConstraintLayout(context),
    CompoundButton.OnCheckedChangeListener {

    var actionListener: ActionListener? = null
    private lateinit var address: Address

    init {
        View.inflate(context, R.layout.item_address, this)
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        setBackgroundColor(Color.WHITE)
        defaultAddressButton.setOnCheckedChangeListener(this)
        editButton.setOnClickListener { actionListener?.onEditButtonClicked(address) }
        deleteButton.setOnClickListener { actionListener?.onDeleteButtonClicked(address) }
    }

    fun setAddress(address: Address, defaultAddress: Address?, withoutActions: Boolean) {
        this.address = address
        addressContent.setAddress(address)
        defaultAddressButton.isChecked = address.id == defaultAddress?.id || address == defaultAddress
        actionGroup.visibility = if (withoutActions) View.GONE else View.VISIBLE
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        deleteButton.isEnabled = !isChecked
        if (isChecked) {
            actionListener?.onAddressChecked(address)
        }
    }

    interface ActionListener {

        fun onEditButtonClicked(address: Address)

        fun onDeleteButtonClicked(address: Address)

        fun onAddressChecked(address: Address)
    }
}