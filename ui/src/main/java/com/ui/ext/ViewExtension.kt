package com.ui.ext

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


fun Context.getScreenSize(): Point {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val point = Point()
    wm.defaultDisplay.getSize(point)
    return point
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun EditText.getTrimmedString(): String {
    return text.trim().toString()
}

fun EditText.setTextWhenDisable(src: String) {
    isEnabled = true
    setText(src)
    isEnabled = false
}