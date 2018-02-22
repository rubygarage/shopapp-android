package com.shopapp.ui.base.recycler.divider

import android.graphics.Rect
import android.support.annotation.DrawableRes
import android.support.v7.widget.RecyclerView
import android.view.View

class BackgroundItemDecoration(@DrawableRes private val backgroundRes: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect?, view: View, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        view.setBackgroundResource(backgroundRes)
    }
}