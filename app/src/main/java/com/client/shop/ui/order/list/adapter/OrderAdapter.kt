package com.client.shop.ui.order.list.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.client.shop.ui.item.OrderItem
import com.client.shop.gateway.entity.Order
import com.domain.formatter.DateFormatter
import com.domain.formatter.NumberFormatter
import com.client.shop.ui.base.recycler.OnItemClickListener
import com.client.shop.ui.base.recycler.adapter.BaseRecyclerAdapter

class OrderAdapter(
    productList: List<Order>,
    private val onOrderItemClickListener: OnOrderItemClickListener
) :
    BaseRecyclerAdapter<Order>(productList, null) {

    private val numberFormatter = NumberFormatter()
    private val dateFormatter = DateFormatter()

    override fun getItemView(context: Context, viewType: Int): View {
        return OrderItem(context,
            dateFormatter,
            numberFormatter)
    }

    override fun getItemHolder(context: Context, viewType: Int): RecyclerView.ViewHolder {
        return OrderViewHolder(getItemView(context, viewType) as OrderItem, onOrderItemClickListener)
    }

    override fun bindData(itemView: View, data: Order, position: Int) {
        if (itemView is OrderItem) {
            itemView.setOrder(data)
        }
    }

    class OrderViewHolder(orderView: OrderItem, onOrderItemClickListener: OnOrderItemClickListener?) :
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