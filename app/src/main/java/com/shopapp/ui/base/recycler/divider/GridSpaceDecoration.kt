package com.shopapp.ui.base.recycler.divider

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class GridSpaceDecoration(private val space: Int, private val spanCount: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val position = parent.getChildAdapterPosition(view) + 1
        val rem = space % 2
        val rowPosition = position % spanCount
        val halfSpace = space / 2
        val topSpace = if (position > spanCount) 0 else space

        when (rowPosition) {
            1 -> outRect.set(space, topSpace, halfSpace, space)
            0 -> outRect.set(halfSpace, topSpace, space, space)
            else -> outRect.set(halfSpace + rem, topSpace, halfSpace + rem, space)
        }
    }
}