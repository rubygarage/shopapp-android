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

    var errorTarget: String? = null

    init {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        View.inflate(context, R.layout.view_lce_error, this)
    }

    override fun changeState(state: LceLayout.LceState) {
        visibility = if (state is LceLayout.LceState.ErrorState) {
            configureErrorView(state.error)
        } else {
            View.GONE
        }
    }

    private fun configureErrorView(error: Error): Int {
        when (error) {
            is Error.Content -> {
                if (error.isNetworkError) {
                    errorImage.setImageResource(R.drawable.ic_no_signal)
                    errorMessage.setText(R.string.network_connection_error)
                } else {
                    errorImage.setImageResource(R.drawable.ic_sentiment_very_dissatisfied)
                    errorMessage.setText(R.string.default_error)
                }
                tryAgainButton.visibility = View.VISIBLE
                backButton.visibility = View.GONE
                return View.VISIBLE
            }
            is Error.Critical -> {
                errorMessage.text = if (errorTarget != null)
                    context.getString(R.string.сould_not_find_with_placeholder, errorTarget)
                else
                    context.getString(R.string.сould_not_find)
                errorImage.setImageResource(R.drawable.ic_categories_empty)
                tryAgainButton.visibility = View.GONE
                backButton.visibility = View.VISIBLE
                return View.VISIBLE
            }
            is Error.NonCritical -> {
                return View.GONE
            }
        }
    }
}