package com.shopapp.ui.product.adapter

import android.content.Context
import android.view.View
import com.shopapp.gateway.entity.Product
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.base.recycler.adapter.BaseRecyclerAdapter
import com.shopapp.ui.item.ProductItem

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