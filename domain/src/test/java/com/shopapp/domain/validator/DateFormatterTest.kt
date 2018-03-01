package com.shopapp.domain.validator

import com.shopapp.domain.formatter.DateFormatter
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.text.SimpleDateFormat
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class DateFormatterTest {

    private val formatter = DateFormatter()

    @Test
    fun dateFormatter() {

        val dateList = getDateList()

        assertEquals("Sunday, 17 Dec, 2017", formatter.format(dateList[0]))
        assertEquals("Monday, 17 Jul, 2017", formatter.format(dateList[1]))
        assertEquals("Monday, 13 Nov, 2017", formatter.format(dateList[2]))
        assertEquals("Monday, 01 Jan, 2018", formatter.format(dateList[3]))
        assertEquals("Tuesday, 17 Oct, 2017", formatter.format(dateList[4]))
        assertEquals("Monday, 18 Dec, 2017", formatter.format(dateList[5]))
        assertEquals("Tuesday, 19 Dec, 2017", formatter.format(dateList[6]))
    }

    private fun getDateList(): List<Date> {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        return listOf(
            simpleDateFormat.parse("17/12/2017"),
            simpleDateFormat.parse("17/07/2017"),
            simpleDateFormat.parse("13/11/2017"),
            simpleDateFormat.parse("01/01/2018"),
            simpleDateFormat.parse("17/10/2017"),
            simpleDateFormat.parse("18/12/2017"),
            simpleDateFormat.parse("19/12/2017"))
    }
}