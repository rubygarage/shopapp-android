package com.shopapp.ui.base.lce.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.shopapp.R

class LceLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    LceView {

    companion object {
        private const val DEFAULT_START_TIME = -1L
        private const val DEFAULT_MIN_DELAY = 50L
        private const val DEFAULT_MIN_SHOW_TIME = 500L
    }

    private var startTime: Long = DEFAULT_START_TIME
    private var postedHide = false
    private var postedShow = false
    private var dismissed = false
    var minShowTime = DEFAULT_MIN_SHOW_TIME

    init {
        View.inflate(context, R.layout.view_lce_loading, this)
        isClickable = true
    }

    private val delayedHide = Runnable {
        postedHide = false
        startTime = DEFAULT_START_TIME
        visibility = View.GONE
    }

    private val delayedShow = Runnable {
        postedShow = false
        if (!dismissed) {
            startTime = System.currentTimeMillis()
            visibility = View.VISIBLE
        }
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
        removeCallbacks(delayedHide)
        removeCallbacks(delayedShow)
    }

    override fun changeState(state: LceLayout.LceState) {
        post {
            if (state is LceLayout.LceState.LoadingState) {
                startTime = DEFAULT_START_TIME
                dismissed = false
                removeCallbacks(delayedHide)
                postedHide = false
                if (!postedShow) {
                    val delay = if (state.useDelay) DEFAULT_MIN_DELAY else 0
                    postDelayed(delayedShow, delay)
                    postedShow = true
                }

                val background = if (state.isTranslucent) {
                    R.color.colorBackgroundLightTranslucent
                } else {
                    R.color.colorBackgroundLight
                }
                setBackgroundResource(background)
            } else {
                dismissed = true
                val diff = System.currentTimeMillis() - startTime
                if (diff >= minShowTime || startTime == DEFAULT_START_TIME) {
                    visibility = View.GONE
                } else {
                    if (!postedHide) {
                        postDelayed(delayedHide, minShowTime - diff)
                        postedHide = true
                    }
                }
            }
        }
    }
}