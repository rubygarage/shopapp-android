package com.shopapp.util.ext

fun String.replaceCommandSymbols() = filter { it != ' ' && it != '\n' && it != '\r' }