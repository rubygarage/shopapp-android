package com.shopapp.ui.order.list.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.shopapp.gateway.entity.Order
import com.shopapp.domain.formatter.DateFormatter
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.base.recycler.adapter.BaseRecyclerAdapter
import com.shopapp.ui.item.OrderItem

class OrderListAdapter(
    productList: List<Order>,
    private val onOrderItemClickListener: OnOrderItemClickListener
) :
    BaseRecyclerAdapter<Order>(productList, null) {

    private val numberFormatter = NumberFormatter()
    private val dateFormatter = DateFormatter()

    override fun getItemView(context: Context, viewType: Int): View {
        return OrderItem(context, dateFormatter, numberFormatter)
    }

    override fun getItemHolder(context: Context, viewType: Int): RecyclerView.ViewHolder {
        return OrderViewHolder(getItemView(context, viewType) as OrderItem, onOrderItemClickListener)
    }

    override fun bindData(itemView: View, data: Order, position: Int) {
        if (itemView is OrderItem) {
            itemView.setOrder(data)
        }
    }

    private class OrderViewHolder(orderView: OrderItem, onOrderItemClickListener: OnOrderItemClickListener?) :
        RecyclerView.ViewHolder(orderView) {

        init {
            orderView.setOnClickListener {
                onOrderItemClickListener?.onItemClicked(adapterPosition)
            }
            orderView.setOnProductVariantClickListener(object : OrderItem.OnProductVariantClickListener {
                override fun onProductVariantClicked(position: Int) {
                    onOrderItemClickListener?.onProductVariantClicked(adapterPosition, position)
                }
            })
        }
    }

    interface OnOrderItemClickListener : OnItemClickListener {

        fun onProductVariantClicked(orderPosition: Int, productPosition: Int)
    }
}