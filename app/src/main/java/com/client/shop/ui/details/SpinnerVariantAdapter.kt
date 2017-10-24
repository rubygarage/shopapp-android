package com.client.shop.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.client.shop.R
import com.domain.entity.ProductVariant


class SpinnerVariantAdapter(private val variants: List<ProductVariant>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        val rowItem = getItem(position)
        val holder: viewHolder
        var itemView: View? = convertView
        if (itemView == null) {
            holder = viewHolder()
            itemView = inflater.inflate(R.layout.item_variant, parent, false)
            holder.txtTitle = itemView as TextView
            itemView.tag = holder
        } else {
            holder = itemView.tag as viewHolder
        }

        holder.txtTitle?.let {
            it.text = rowItem.title
            it.alpha = if (rowItem.isAvailable) 1f else 0.5f
        }
        return itemView
    }

    override fun isEnabled(position: Int) = variants[position].isAvailable

    override fun getItem(position: Int) = variants[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = variants.size

    private inner class viewHolder {
        internal var txtTitle: TextView? = null
    }
}