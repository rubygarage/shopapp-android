package com.shopify.ui.address.adapter

import android.content.Context
import android.view.View
import com.domain.entity.Address
import com.shopify.ui.item.AddressItem
import com.ui.base.recycler.adapter.BaseRecyclerAdapter

class AddressListAdapter(dataList: List<Address>,
                         private val actionListener: AddressItem.ActionListener) :
        BaseRecyclerAdapter<Address>(dataList) {

    var defaultAddress: Address? = null

    override fun getItemView(context: Context, viewType: Int) = AddressItem(context)

    override fun bindData(itemView: View, data: Address, position: Int) {
        if (itemView is AddressItem) {
            itemView.setAddress(data, defaultAddress)
            itemView.actionListener = actionListener
        }
    }
}