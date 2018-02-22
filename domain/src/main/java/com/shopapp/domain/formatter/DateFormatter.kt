package com.shopapp.domain.formatter

import java.text.SimpleDateFormat
import java.util.*

class DateFormatter {

    private val dateFormat = SimpleDateFormat("EEEE, dd MMM, yyyy", Locale.ENGLISH)

    fun format(date: Date): String = dateFormat.format(date)
}