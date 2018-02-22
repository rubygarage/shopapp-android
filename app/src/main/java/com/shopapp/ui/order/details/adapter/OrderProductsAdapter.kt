package com.shopapp.ui.order.details.adapter

import android.content.Context
import android.view.View
import com.shopapp.gateway.entity.OrderProduct
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.base.recycler.adapter.BaseRecyclerAdapter
import com.shopapp.ui.item.OrderProductItem

class OrderProductsAdapter(
    productList: List<OrderProduct>,
    onItemClickListener: OnItemClickListener,
    private val currency: String
) :
    BaseRecyclerAdapter<OrderProduct>(productList, onItemClickListener) {

    private val numberFormatter = NumberFormatter()

    override fun getItemView(context: Context, viewType: Int): View {
        return OrderProductItem(context, numberFormatter)
    }

    override fun bindData(itemView: View, data: OrderProduct, position: Int) {
        if (itemView is OrderProductItem) {
            itemView.setOrderProduct(data, currency)
        }
    }
}