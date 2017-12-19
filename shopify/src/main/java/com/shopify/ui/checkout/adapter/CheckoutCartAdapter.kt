package com.shopify.ui.checkout.adapter

import android.content.Context
import android.view.View
import com.domain.entity.CartProduct
import com.shopify.ui.item.CheckoutCartItem
import com.ui.base.recycler.OnItemClickListener
import com.ui.base.recycler.adapter.BaseRecyclerAdapter

class CheckoutCartAdapter(
        dataList: List<CartProduct>,
        onItemClickListener: OnItemClickListener
) :
        BaseRecyclerAdapter<CartProduct>(dataList, onItemClickListener) {

    override fun getItemView(context: Context, viewType: Int) = CheckoutCartItem(context)

    override fun bindData(itemView: View, data: CartProduct, position: Int) {
        if (itemView is CheckoutCartItem) {
            itemView.setImageURI(data.productVariant.image?.src)
        }
    }
}