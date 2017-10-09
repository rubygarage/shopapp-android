package com.client.shop.ui.modal

import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.util.TypedValue
import android.widget.TextView
import com.client.shop.R
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.shopapicore.entity.SortType
import kotlinx.android.synthetic.main.bottom_sheet_sort.*

class SortBottomSheet(context: Context, listener: OnItemClickListener<SortType>) : BottomSheetDialog(context) {

    init {
        setContentView(R.layout.bottom_sheet_sort)
        for (value in SortType.values()) {
            val itemView = layoutInflater.inflate(R.layout.item_sort, containerLayout, false) as TextView
            itemView.text = value.value
            itemView.setOnClickListener {
                listener.onItemClicked(value, -1)
                dismiss()
            }
            containerLayout.addView(itemView)
        }
    }

    fun show(selectedSortType: SortType) {
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)

        (1..containerLayout.childCount)
                .map { containerLayout.getChildAt(it) }
                .forEach {
                    if (it is TextView && it.text == selectedSortType.value) {
                        it.setBackgroundResource(R.color.selectedColor)
                    } else if (it is TextView) {
                        it.setBackgroundResource(outValue.resourceId)
                    }
                }
        super.show()
    }

    @Deprecated("Use an overloaded method", ReplaceWith("show(selectedSortType: SortType)", "android.support.design.widget.BottomSheetDialog"))
    override fun show() {
        super.show()
    }
}