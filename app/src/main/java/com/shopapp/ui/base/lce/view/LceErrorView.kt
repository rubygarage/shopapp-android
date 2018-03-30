package com.shopapp.ui.base.lce.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.shopapp.R
import com.shopapp.gateway.entity.Error
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
            when (state.error) {
                is Error.Content -> {
                    if (state.error.isNetworkError) {
                        errorImage.setImageResource(R.drawable.ic_no_signal)
                        errorMessage.setText(R.string.network_connection_error)
                    } else {
                        errorImage.setImageResource(R.drawable.ic_sentiment_very_dissatisfied)
                        errorMessage.setText(R.string.default_error)
                    }
                    tryAgainButton.visibility = View.VISIBLE
                    backButton.visibility = View.GONE
                }
                is Error.Critical -> {
                    errorImage.setImageResource(R.drawable.ic_categories_empty)
                    errorMessage.setText(R.string.сould_not_find_it)
                    tryAgainButton.visibility = View.GONE
                    backButton.visibility = View.VISIBLE
                }
            }
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}