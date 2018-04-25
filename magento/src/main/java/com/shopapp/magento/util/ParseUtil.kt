package com.shopapp.magento.util

import android.text.Html

@Suppress("DEPRECATION")
fun removeTagsFromHtml(htmlText: String): String {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(htmlText)
    }.toString()
}