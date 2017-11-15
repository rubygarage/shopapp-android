package com.client.shop.ui.home.adapter

import android.content.Context
import android.view.View
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.client.shop.ui.base.ui.recycler.adapter.BaseRecyclerAdapter
import com.client.shop.ui.item.CategoryItem
import com.domain.entity.Category

class CategoriesAdapter(categories: List<Category>, onItemClickListener: OnItemClickListener) :
        BaseRecyclerAdapter<Category>(categories, onItemClickListener) {

    override fun getItemView(context: Context, viewType: Int) = CategoryItem(context)

    override fun bindData(itemView: View, data: Category, position: Int) {
        if (itemView is CategoryItem) {
            itemView.setCategory(data)
        }
    }
}