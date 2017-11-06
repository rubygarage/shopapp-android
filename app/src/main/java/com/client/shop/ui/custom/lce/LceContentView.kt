package com.client.shop.ui.custom.lce

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.client.shop.R

class LceContentView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    LceView {

    init {
        View.inflate(context, R.layout.view_lce_content, this)
    }

    override fun changeState(state: LceLayout.LceState) {
        visibility = if (state == LceLayout.LceState.ContentState) View.VISIBLE else View.GONE
    }
}