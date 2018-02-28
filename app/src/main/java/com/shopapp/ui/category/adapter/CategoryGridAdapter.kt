package com.shopapp.ui.category.adapter

import android.content.Context
import android.view.View
import com.shopapp.gateway.entity.Category
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.base.recycler.adapter.BaseRecyclerAdapter
import com.shopapp.ui.item.CategoryItem

class CategoryGridAdapter(categories: List<Category>, onItemClickListener: OnItemClickListener) :
    BaseRecyclerAdapter<Category>(categories, onItemClickListener) {

    override fun getItemView(context: Context, viewType: Int): View {
        return CategoryItem(context)
    }

    override fun bindData(itemView: View, data: Category, position: Int) {
        if (itemView is CategoryItem) {
            itemView.setCategory(data)
        }
    }
}