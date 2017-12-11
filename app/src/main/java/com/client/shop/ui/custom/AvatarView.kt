package com.client.shop.ui.custom

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import com.client.shop.R

class AvatarView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    init {
        setBackgroundResource(R.drawable.background_circle)
        setTextColor(ContextCompat.getColor(context, R.color.white))
        gravity = Gravity.CENTER
    }

    fun setName(name: String) {
        if (name.isNotBlank()) {
            text = ""
            val parts = name.split(Character.SPACE_SEPARATOR.toChar())
            for (part in parts) {
                append(part.first().toUpperCase().toString())
            }
        }
    }
}