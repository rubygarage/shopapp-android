package com.shopapp.ui.base.lce.view

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.shopapp.R
import kotlinx.android.synthetic.main.view_lce_empty.view.*

class LceEmptyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr),
    LceView {

    init {
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        View.inflate(context, R.layout.view_lce_empty, this)
    }

    override fun changeState(state: LceLayout.LceState) {
        visibility = if (state == LceLayout.LceState.EmptyState) View.VISIBLE else View.GONE
    }

    fun customiseEmptyImage(@DrawableRes image: Int) {
        emptyImage.setImageResource(image)
    }

    fun customiseEmptyMessage(@StringRes message: Int) {
        emptyMessage.setText(message)
    }

    fun customiseEmptyButtonVisibility(isButtonVisible: Boolean = false) {
        emptyButton.visibility = if (isButtonVisible) View.VISIBLE else View.GONE
    }

    fun customiseEmptyButtonText(@StringRes text: Int) {
        emptyButton.setText(text)
    }
}