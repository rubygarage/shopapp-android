package com.client.shop.ui.custom

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent


class ClickableViewPager : ViewPager {

    private var mOnItemClickListener: OnItemClickListener? = null

    constructor(context: Context?) : super(context) {
        setup()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        setup()
    }

    private fun setup() {
        val tapGestureDetector = GestureDetector(context, TapGestureListener())
        setOnTouchListener { _, event ->
            tapGestureDetector.onTouchEvent(event)
            false
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private inner class TapGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            mOnItemClickListener?.onItemClick(currentItem)
            return true
        }
    }
}