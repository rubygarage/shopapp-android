package com.client.shop.ui.product.adapter

import android.content.Context
import android.view.View
import com.client.shop.getaway.entity.ProductVariant
import com.client.shop.ui.base.recycler.OnItemClickListener
import com.client.shop.ui.base.recycler.adapter.BaseRecyclerAdapter
import com.client.shop.ui.item.ProductVariantItem

class ProductVariantAdapter(
    dataList: List<ProductVariant>,
    onItemClickListener: OnItemClickListener
) :
    BaseRecyclerAdapter<ProductVariant>(dataList, onItemClickListener) {

    override fun getItemView(context: Context, viewType: Int) = ProductVariantItem(context)

    override fun bindData(itemView: View, data: ProductVariant, position: Int) {
        if (itemView is ProductVariantItem) {
            val imageURI: String? = data.image?.src ?: data.productImage?.src
            itemView.setImageURI(imageURI)
        }
    }
}