package com.client.shop.ui.recent.adapter

import android.content.Context
import android.view.View
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.client.shop.ui.base.ui.recycler.adapter.BaseRecyclerAdapter
import com.client.shop.ui.item.RecentItem
import com.domain.entity.Product
import com.domain.formatter.NumberFormatter

class RecentAdapter(productList: List<Product>, onItemClickListener: OnItemClickListener) :
        BaseRecyclerAdapter<Product>(productList, onItemClickListener) {

    private val formatter = NumberFormatter()

    override fun getItemView(context: Context, viewType: Int): View {
        return RecentItem(context, formatter)
    }

    override fun bindData(itemView: View, data: Product, position: Int) {
        if (itemView is RecentItem) {
            itemView.setProduct(data)
        }
    }
}