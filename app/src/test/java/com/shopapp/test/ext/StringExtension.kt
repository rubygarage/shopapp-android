package com.shopapp.test.ext

fun String.replaceCommandSymbols() = filter { it != ' ' && it != '\n' && it != '\r' }