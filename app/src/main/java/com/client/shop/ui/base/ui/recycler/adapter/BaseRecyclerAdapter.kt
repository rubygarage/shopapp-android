package com.client.shop.ui.base.ui.recycler.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.client.shop.ui.base.ui.recycler.OnItemClickListener

abstract class BaseRecyclerAdapter<in T>(private val dataList: List<T>,
                                         private val onItemClickListener: OnItemClickListener<T>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var withHeader = false
    var withFooter = false

    companion object {
        private const val DEFAULT_TYPE = 100
        private const val HEADER_TYPE = 200
        private const val FOOTER_TYPE = 300
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        return when (viewType) {
            HEADER_TYPE -> HeaderViewHolder(getHeaderView(context))
            FOOTER_TYPE -> HeaderViewHolder(getFooterView(context))
            else -> DefaultViewHolder(getItemView(context, viewType))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val itemPosition = position - if (withHeader) 1 else 0
        if (holder != null && itemPosition >= 0 && dataList.size > itemPosition) {
            val itemView = holder.itemView
            val data = dataList[itemPosition]
            itemView.setOnClickListener({ onItemClickListener.onItemClicked(data, itemPosition) })
            bindData(itemView, data, itemPosition)
        }
    }

    override fun getItemCount(): Int {
        val headerOffset = if (withHeader) 1 else 0
        val footerOffset = if (withFooter) 1 else 0
        return dataList.size + headerOffset + footerOffset
    }

    override fun getItemViewType(position: Int): Int {
        return if (withHeader && position == 0) {
            HEADER_TYPE
        } else if (withFooter && position == itemCount - 1) {
            FOOTER_TYPE
        } else {
            DEFAULT_TYPE
        }
    }

    abstract fun getItemView(context: Context, viewType: Int): View

    abstract fun bindData(itemView: View, data: T, position: Int)

    protected open fun getHeaderView(context: Context): View? {
        return null
    }

    protected open fun getFooterView(context: Context): View? {
        return null
    }

    class DefaultViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    class HeaderViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    class FooterViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}