package com.client.shop.ui.category.adapter

import android.content.Context
import android.view.View
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.client.shop.ui.base.ui.recycler.adapter.BaseRecyclerAdapter
import com.client.shop.ui.item.ProductItem
import com.domain.entity.Product

class CategoryAdapter(productList: List<Product>, onItemClickListener: OnItemClickListener) :
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