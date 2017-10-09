package com.client.shop.ui.search.adapter

import android.content.Context
import android.view.View
import com.client.shop.ui.base.ui.recycler.BaseRecyclerAdapter
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.client.shop.ui.item.ProductItem
import com.shopapicore.entity.Product

class SearchAdapter(productList: List<Product>, onItemClickListener: OnItemClickListener<Product>) :
        BaseRecyclerAdapter<Product>(productList, onItemClickListener) {

    override fun getItemView(context: Context, viewType: Int): View {
        return ProductItem(context)
    }

    override fun bindData(itemView: View, data: Product, position: Int) {
        if (itemView is ProductItem) {
            itemView.setProduct(data)
        }
    }
}