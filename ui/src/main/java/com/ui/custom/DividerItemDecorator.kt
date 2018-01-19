package com.ui.custom

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView


class DividerItemDecorator(private val mDivider: Drawable) : RecyclerView.ItemDecoration() {

    override fun onDraw(canvas: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
        parent?.let {
            val dividerLeft = 0
            val dividerRight = it.width

            (0 until it.childCount)
                .map { parent.getChildAt(it) }
                .filter { parent.getChildLayoutPosition(it) - 1 != parent.adapter.itemCount - 1 }
                .forEach {
                    val params = it.layoutParams as RecyclerView.LayoutParams

                    val dividerTop = it.bottom + params.bottomMargin
                    val dividerBottom = dividerTop + mDivider.intrinsicHeight

                    mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                    mDivider.draw(canvas)
                }
        }

    }
}