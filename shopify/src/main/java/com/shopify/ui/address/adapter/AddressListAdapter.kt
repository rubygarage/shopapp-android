package com.shopify.ui.address.adapter

import android.content.Context
import android.view.View
import com.domain.entity.Address
import com.shopify.ui.item.AddressHeaderItem
import com.shopify.ui.item.AddressItem
import com.ui.base.recycler.adapter.BaseRecyclerAdapter

class AddressListAdapter(dataList: List<Address>,
                         private val withoutActions: Boolean,
                         private val actionListener: AddressItem.ActionListener,
                         private val headerOnClickListener: View.OnClickListener
) :
    BaseRecyclerAdapter<Address>(dataList) {

    var defaultAddress: Address? = null

    init {
        withHeader = true
    }

    override fun getItemView(context: Context, viewType: Int) = AddressItem(context)

    override fun getHeaderView(context: Context): View? {
        val headerView = AddressHeaderItem(context)
        headerView.headerClickListener = headerOnClickListener
        return headerView
    }

    override fun bindData(itemView: View, data: Address, position: Int) {
        if (itemView is AddressItem) {
            itemView.setAddress(data, defaultAddress, withoutActions)
            itemView.actionListener = actionListener
        }
    }
}