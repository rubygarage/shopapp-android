package com.client.shop.ui.recent.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.client.shop.ui.base.ui.recycler.adapter.BaseRecyclerAdapter
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.client.shop.ui.item.MoreItem
import com.client.shop.ui.item.RecentItem
import com.domain.entity.Product

class RecentAdapter(productList: List<Product>, onItemClickListener: OnItemClickListener<Product>,
                    private val moreButtonListener: View.OnClickListener) :
        BaseRecyclerAdapter<Product>(productList, onItemClickListener) {

    override fun getItemView(context: Context, viewType: Int): View {
        return RecentItem(context)
    }

    override fun bindData(itemView: View, data: Product, position: Int) {
        if (itemView is RecentItem) {
            itemView.setProduct(data)
        }
    }

    override fun getFooterView(context: Context): View? {
        val footer = MoreItem(context, width = ViewGroup.LayoutParams.WRAP_CONTENT)
        footer.moreButton.setOnClickListener(moreButtonListener)
        return footer
    }

}