package com.shopapp.ui.base.picker

import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import com.shopapp.R
import kotlinx.android.synthetic.main.item_bottom_sheet_picker.view.*

class BottomSheetPickerItem(context: Context) : FrameLayout(context) {

    init {
        View.inflate(context, R.layout.item_bottom_sheet_picker, this)
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    var data: String? = null

    fun bindData(data: String, selectedItemData: String?) {
        this.data = data
        titleView.text = data
        if (data == selectedItemData) {
            setTextStyle(R.drawable.background_selected_picker_item, R.color.textColorPrimary)
        } else {
            setTextStyle(R.color.transparent, R.color.textColorSecondary)
        }
    }

    private fun setTextStyle(@DrawableRes background: Int, @ColorRes texColorRes: Int) {
        titleView.setBackgroundResource(background)
        titleView.setTextColor(ContextCompat.getColor(context, texColorRes))
    }
}