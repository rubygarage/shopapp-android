package com.client.shop.ui.home.adapter

import android.content.Context
import android.view.View
import com.client.shop.R
import com.client.shop.ui.base.ui.recycler.adapter.BaseRecyclerAdapter
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.client.shop.ui.item.CategoryItem
import com.client.shop.ui.item.DrawerItem
import com.domain.entity.Category

class CategoriesAdapter(categories: List<Category>, onItemClickListener: OnItemClickListener<Category>) :
        BaseRecyclerAdapter<Category>(categories, onItemClickListener) {

    init {
        withHeader = true
    }

    override fun getItemView(context: Context, viewType: Int) = CategoryItem(context)

    override fun bindData(itemView: View, data: Category, position: Int) {
        if (itemView is CategoryItem) {
            itemView.setCategory(data)
        }
    }

    override fun getHeaderView(context: Context): View {
        val header = DrawerItem(context)
        header.setTitle(context.getString(R.string.category))
        header.setBackgroundResource(R.color.selectedColor)
        return header
    }
}