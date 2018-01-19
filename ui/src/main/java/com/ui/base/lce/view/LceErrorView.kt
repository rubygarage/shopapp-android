package com.ui.base.lce.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.ui.R
import kotlinx.android.synthetic.main.view_lce_error.view.*

class LceErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr),
    LceView {

    init {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        View.inflate(context, R.layout.view_lce_error, this)
    }

    override fun changeState(state: LceLayout.LceState) {
        visibility = if (state is LceLayout.LceState.ErrorState) {
            if (state.isNetworkError) {
                errorImage.setImageResource(R.drawable.ic_no_signal)
                errorMessage.setText(R.string.network_connection_error)
            } else {
                errorImage.setImageResource(R.drawable.ic_sentiment_very_dissatisfied)
                errorMessage.setText(R.string.default_error)
            }
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}