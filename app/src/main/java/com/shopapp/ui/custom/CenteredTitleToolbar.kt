package com.shopapp.ui.custom

import android.content.Context
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.View
import com.shopapp.R
import kotlinx.android.synthetic.main.view_base_toolbar.view.*

class CenteredTitleToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_base_toolbar, this)
    }

    fun setTitle(title: String) {
        toolbarTitle.text = title
    }

    @Deprecated("Use an overloaded function instead", ReplaceWith("setTitle(title: String)"))
    override fun setTitle(resId: Int) {
        super.setTitle(resId)
    }

    @Deprecated("Use an overloaded function instead", ReplaceWith("setTitle(title: String)"))
    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
    }
}