package com.shopapp.ui.base.picker

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.shopapp.ui.base.recycler.OnItemClickListener

abstract class BottomSheetPickerAdapter<T> :
    RecyclerView.Adapter<BottomSheetPickerAdapter.ItemViewHolder>(),
    OnItemClickListener {

    var selectedItemData: T? = null
    var dataList: MutableList<T> = mutableListOf()

    init {
        this.setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(BottomSheetPickerItem(parent.context), this)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindData(
            convertModel(dataList[position]),
            selectedItemData?.let { convertModel(it) }
        )
    }

    abstract fun convertModel(it: T): String

    override fun getItemCount() = dataList.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setSelected(title: String): Int {
        val index = dataList.map { convertModel(it) }.indexOf(title)
        if (index >= 0) {
            selectedItemData = dataList[index]
        }
        return index
    }

    class ItemViewHolder(itemView: View,
                         private val clickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        fun bindData(data: String, selectedItemData: String?) {
            if (itemView is BottomSheetPickerItem) {
                itemView.bindData(data, selectedItemData)
            }
            itemView.setOnClickListener { clickListener.onItemClicked(adapterPosition) }
        }

    }

    override fun onItemClicked(position: Int) {
        selectedItemData = dataList[position]
        notifyDataSetChanged()
    }

}