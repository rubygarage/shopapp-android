package com.shopify.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class WrapContentViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    private var mCurrentPagePosition = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var newHeightMeasureSpec = 1
        try {
            val child = getChildAt(mCurrentPagePosition)
            if (child != null) {
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
                val h = child.measuredHeight
                newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean = false

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean = false
}