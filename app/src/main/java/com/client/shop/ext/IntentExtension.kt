package com.client.shop.ext

import android.content.Context
import android.content.Intent

fun Context.shareText(text: String, title: String) {
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.putExtra(Intent.EXTRA_TEXT, text)
    intent.type = "text/plain"
    startActivity(Intent.createChooser(intent, title))
}