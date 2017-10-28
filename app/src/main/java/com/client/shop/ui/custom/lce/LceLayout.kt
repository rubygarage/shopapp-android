package com.client.shop.ui.custom.lce

import android.content.Context
import android.support.annotation.LayoutRes
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.client.shop.R
import kotlinx.android.synthetic.main.layout_lce.view.*

class LceLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.layout_lce, this)
    }

    fun setupContentLayout(@LayoutRes layout: Int) {
        contentView.removeAllViews()
        View.inflate(context, layout, contentView)
    }

    fun changeState(state: LceState) {
        when (state) {
            is LceState.LoadingState -> {
                loadingView.show()
                errorView.hide()
                contentView.hide()
            }
            is LceState.ContentState -> {
                loadingView.hide()
                errorView.hide()
                contentView.show()
            }
            is LceState.ErrorState -> {
                loadingView.hide()
                errorView.show(state.isNetworkError)
                contentView.hide()
            }
        }
    }

    sealed class LceState {
        object LoadingState : LceState()
        object ContentState : LceState()
        class ErrorState(val isNetworkError: Boolean) : LceState()
    }
}