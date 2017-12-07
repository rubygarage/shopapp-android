package com.client.shop.ext

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.support.annotation.StringRes
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager


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

fun Context.getUpperCaseString(@StringRes resId: Int): String {
    return getString(resId).toUpperCase()
}