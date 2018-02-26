package com.shopapp.ui.base.recycler.divider

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView


class DividerItemDecorator(context: Context?, @DrawableRes drawable: Int) : RecyclerView.ItemDecoration() {

    private val drawable: Drawable? = context?.let { ContextCompat.getDrawable(it, drawable) }

    override fun onDraw(canvas: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {

        if (parent != null && drawable != null) {
            val dividerLeft = 0
            val dividerRight = parent.width

            (0 until parent.childCount)
                .map { parent.getChildAt(it) }
                .filter { parent.getChildLayoutPosition(it) - 1 != parent.adapter.itemCount - 1 }
                .forEach {
                    val params = it.layoutParams as RecyclerView.LayoutParams

                    val dividerTop = it.bottom + params.bottomMargin
                    val dividerBottom = dividerTop + drawable.intrinsicHeight

                    drawable.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                    drawable.draw(canvas)
                }
        }
    }
}