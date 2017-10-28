package com.client.shop.ui.custom.lce

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.client.shop.R
import kotlinx.android.synthetic.main.view_lce_error.view.*

class LceErrorView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_lce_error, this)
    }

    fun show(isNetworkError: Boolean) {
        visibility = View.VISIBLE

        errorMessage.text = if (isNetworkError) "NO INTERNET" else "SOMETHING WENT WRONG"
    }

    fun hide() {
        visibility = View.GONE
    }
}