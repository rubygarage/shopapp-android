package com.shopapp.ui.custom

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.util.AttributeSet

class UpperCaseHintTextInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    TextInputLayout(context, attrs, defStyleAttr) {

    override fun setHint(hint: CharSequence?) {
        super.setHint(hint?.toString()?.toUpperCase())
    }
}