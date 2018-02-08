package com.shopify.util

import android.content.Context

object AssetsReader {

    fun read(filename: String, context: Context): String {
        val input = context.assets.open(filename)
        val byteArray = ByteArray(input.available())
        input.read(byteArray)
        input.close()
        return String(byteArray)
    }
}