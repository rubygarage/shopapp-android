package com.shopapp.ui.address.base.adapter

import android.content.Context
import android.view.View
import com.shopapp.gateway.entity.Address
import com.shopapp.ui.item.AddressHeaderItem
import com.shopapp.ui.item.AddressItem
import com.shopapp.ui.base.recycler.adapter.BaseRecyclerAdapter

open class AddressListAdapter(
    dataList: List<Address>,
    private val actionListener: ActionButtonListener,
    private val headerOnClickListener: View.OnClickListener
) :
    BaseRecyclerAdapter<Address>(dataList) {

    var defaultAddress: Address? = null

    init {
        withHeader = true
    }

    override fun getItemView(context: Context, viewType: Int): View {
        val item = AddressItem(context)
        item.actionListener = actionListener
        return item
    }

    override fun getHeaderView(context: Context): View? {
        val headerView = AddressHeaderItem(context)
        headerView.headerClickListener = headerOnClickListener
        return headerView
    }

    override fun bindData(itemView: View, data: Address, position: Int) {
        if (itemView is AddressItem) {
            itemView.setAddress(data, defaultAddress)
        }
    }

    interface ActionButtonListener {

        fun onEditButtonClicked(address: Address)

        fun onDeleteButtonClicked(address: Address)

        fun onDefaultButtonClicked(address: Address)
    }
}