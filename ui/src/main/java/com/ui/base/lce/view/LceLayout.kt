package com.ui.base.lce.view

import android.content.Context
import android.support.annotation.LayoutRes
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.ui.R
import kotlinx.android.synthetic.main.layout_lce.view.*
import kotlinx.android.synthetic.main.view_lce_empty.view.*
import kotlinx.android.synthetic.main.view_lce_error.view.*

class LceLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
        LceView {

    var tryAgainButtonClickListener: OnClickListener? = null
        set(value) {
            value?.let { tryAgainButton.setOnClickListener(it) }
        }

    var emptyButtonClickListener: OnClickListener? = null
        set(value) {
            value?.let { emptyButton.setOnClickListener(it) }
        }

    init {
        View.inflate(context, R.layout.layout_lce, this)
    }

    fun setupContentLayout(@LayoutRes layout: Int) {
        contentView.removeAllViews()
        View.inflate(context, layout, contentView)
    }

    override fun changeState(state: LceState) {
        loadingView.changeState(state)
        contentView.changeState(state)
        emptyView.changeState(state)
        errorView.changeState(state)
    }

    sealed class LceState {
        object LoadingState : LceState()
        object ContentState : LceState()
        object EmptyState : LceState()
        class ErrorState(val isNetworkError: Boolean) : LceState()
    }

}