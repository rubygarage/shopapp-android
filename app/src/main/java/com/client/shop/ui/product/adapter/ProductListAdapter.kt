package com.client.shop.ui.product.adapter

import android.content.Context
import android.view.View
import com.client.shop.ui.item.ProductItem
import com.client.shop.gateway.entity.Product
import com.domain.formatter.NumberFormatter
import com.client.shop.ui.base.recycler.OnItemClickListener
import com.client.shop.ui.base.recycler.adapter.BaseRecyclerAdapter

class ProductListAdapter(
    private val itemWidth: Int,
    private val itemHeight: Int,
    productList: List<Product>,
    onItemClickListener: OnItemClickListener
) :
    BaseRecyclerAdapter<Product>(productList, onItemClickListener) {

    private val formatter = NumberFormatter()

    override fun getItemView(context: Context, viewType: Int): View {
        return ProductItem(context, itemWidth, itemHeight, formatter)
    }

    override fun bindData(itemView: View, data: Product, position: Int) {
        if (itemView is ProductItem) {
            itemView.setProduct(data)
        }
    }
}