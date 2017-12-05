package com.client.shop.ui.base.ui.recycler.divider

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class LeftSpaceDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position != 0) {
            outRect.set(space, 0, 0, 0)
        }
    }
}