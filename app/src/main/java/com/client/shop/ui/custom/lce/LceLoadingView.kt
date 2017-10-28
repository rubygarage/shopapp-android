package com.client.shop.ui.custom.lce

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.client.shop.R

class LceLoadingView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val MIN_SHOW_TIME = 500L
    }

    init {
        View.inflate(context, R.layout.view_lce_loading, this)
        setBackgroundResource(R.color.white)
    }

    private var mStartTime: Long = -1

    private var mPostedHide = false

    private var mPostedShow = false

    private var mDismissed = false

    private val mDelayedHide = Runnable {
        mPostedHide = false
        mStartTime = -1
        visibility = View.GONE
    }


    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        removeCallbacks()
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks()
    }

    private fun removeCallbacks() {
        removeCallbacks(mDelayedHide)
    }

    fun hide() {
        mDismissed = true
        val diff = System.currentTimeMillis() - mStartTime
        if (diff >= MIN_SHOW_TIME || mStartTime == -1L) {
            visibility = View.GONE
        } else {
            if (!mPostedHide) {
                postDelayed(mDelayedHide, MIN_SHOW_TIME - diff)
                mPostedHide = true
            }
        }
    }

    fun show() {
        mDismissed = false
        mPostedShow = false
        mStartTime = System.currentTimeMillis()
        visibility = View.VISIBLE
    }
}