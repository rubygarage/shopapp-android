package com.shopapp.ext

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.net.Uri
import android.support.annotation.DimenRes
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.shopapp.R

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

fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val focus = currentFocus
    focus?.let {
        imm.hideSoftInputFromWindow(focus.windowToken, 0)
    }
}

fun EditText.getTrimmedString(): String {
    return text.trim().toString()
}

fun EditText.setTextWhenDisable(src: String) {
    isEnabled = true
    setText(src)
    isEnabled = false
}

fun SimpleDraweeView.setResizedImageUri(
    context: Context,
    src: String?,
    @DimenRes widthRes: Int = R.dimen.default_image_size,
    @DimenRes heightRes: Int = R.dimen.default_image_size
) {
    src?.let {
        val width = context.resources.getDimensionPixelSize(widthRes)
        val height = context.resources.getDimensionPixelSize(heightRes)
        val request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(it))
            .setResizeOptions(ResizeOptions(width, height))
            .build()
        controller = Fresco.newDraweeControllerBuilder()
            .setOldController(controller)
            .setImageRequest(request)
            .build()
    }
}