package com.shopapp.ui.custom

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.Gravity
import com.shopapp.R

class AvatarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        setBackgroundResource(R.drawable.background_circle)
        setTextColor(ContextCompat.getColor(context, R.color.white))
        gravity = Gravity.CENTER
    }

    fun setName(name: String) {
        if (name.isNotBlank()) {
            text = ""
            val parts = name.split("\\s".toRegex())
            for (part in parts) {
                append(part.first().toUpperCase().toString())
            }
        }
    }
}