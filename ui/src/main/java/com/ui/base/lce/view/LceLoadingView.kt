package com.ui.base.lce.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.ui.R

class LceLoadingView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
        LceView {

    companion object {
        private const val MIN_SHOW_TIME = 500L
    }

    init {
        View.inflate(context, R.layout.view_lce_loading, this)
        setBackgroundResource(R.color.colorBackgroundLight)
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

    override fun changeState(state: LceLayout.LceState) {
        post {
            if (state == LceLayout.LceState.LoadingState) {
                mDismissed = false
                mPostedShow = false
                mStartTime = System.currentTimeMillis()
                visibility = View.VISIBLE
            } else {
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
        }
    }
}