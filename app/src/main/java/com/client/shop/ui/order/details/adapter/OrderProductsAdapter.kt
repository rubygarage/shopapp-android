package com.client.shop.ui.order.details.adapter

import android.content.Context
import android.view.View
import com.client.shop.ui.item.OrderItem
import com.client.shop.ui.item.OrderProductItem
import com.domain.entity.OrderProduct
import com.domain.formatter.DateFormatter
import com.domain.formatter.NumberFormatter
import com.ui.base.recycler.OnItemClickListener
import com.ui.base.recycler.adapter.BaseRecyclerAdapter

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