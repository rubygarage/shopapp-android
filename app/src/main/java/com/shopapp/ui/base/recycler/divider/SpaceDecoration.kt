package com.shopapp.ui.base.recycler.divider

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class SpaceDecoration(private val leftSpace: Int = 0,
                      private val topSpace: Int = 0,
                      private val rightSpace: Int = 0,
                      private val bottomSpace: Int = 0,
                      private val skipFirst: Boolean = true
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if ((!skipFirst && position == 0) || position > 0) {
            outRect.set(leftSpace, topSpace, rightSpace, bottomSpace)
        }
    }
}