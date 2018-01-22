package com.ui.view

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.Button

class UnderlineButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Button(context, attrs, defStyleAttr) {

    init {
        background = null
        paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }
}