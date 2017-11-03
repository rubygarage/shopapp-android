package com.client.shop.ui.cart.adapter

import android.content.Context
import android.view.View
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.client.shop.ui.base.ui.recycler.adapter.BaseRecyclerAdapter
import com.client.shop.ui.item.CartItem
import com.domain.entity.CartProduct

class CartAdapter(dataList: List<CartProduct>, onItemClickListener: OnItemClickListener<CartProduct>) :
        BaseRecyclerAdapter<CartProduct>(dataList, onItemClickListener) {

    var actionListener: CartItem.ActionListener? = null

    override fun bindData(itemView: View, data: CartProduct, position: Int) {
        if (itemView is CartItem) {
            itemView.setCartProduct(data)
            itemView.actionListener = actionListener
        }
    }

    override fun getItemView(context: Context, viewType: Int): View {
        return CartItem(context)
    }

    override fun getItemId(position: Int): Long {
        return dataList[position].productVariant.id.hashCode().toLong()
    }
}