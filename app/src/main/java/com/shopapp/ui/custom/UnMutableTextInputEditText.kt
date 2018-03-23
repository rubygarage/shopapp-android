package com.shopapp.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.support.design.widget.TextInputEditText
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SoundEffectConstants

class UnMutableTextInputEditText(context: Context?, attrs: AttributeSet?) :
    TextInputEditText(context, attrs) {

    init {
        isEnabled = false
    }

    private var onClickListener: OnClickListener? = null

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        this.onClickListener = onClickListener
    }

    fun getOnClickListener() = onClickListener

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            playSoundEffect(SoundEffectConstants.CLICK)
            onClickListener?.onClick(this)
        }
        return true
    }
}