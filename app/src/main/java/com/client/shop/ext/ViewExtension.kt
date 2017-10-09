package com.client.shop.ext

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

fun Context.getScreenSize(): Point {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val point = Point()
    wm.defaultDisplay.getSize(point)
    return point
}