package com.ui.base.picker

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

class BottomSheetPickerAdapter(val dataList: List<String>) :
        RecyclerView.Adapter<BottomSheetPickerAdapter.ItemViewHolder>(),
        View.OnClickListener {

    var selectedItemData: String? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = BottomSheetPickerItem(parent.context)
        itemView.setOnClickListener(this)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (holder.itemView is BottomSheetPickerItem) {
            holder.itemView.bindData(dataList[position], selectedItemData)
        }
    }

    override fun getItemCount() = dataList.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onClick(view: View) {
        if (view is BottomSheetPickerItem) {
            selectedItemData = view.data
            notifyDataSetChanged()
        }
    }
}