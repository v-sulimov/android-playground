package com.vsulimov.playground.converter.timestamp

import com.vsulimov.playground.converter.timestamp.UnixTimestampConverter.timestampToHumanReadable
import java.util.Calendar
import java.util.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals

class UnixTimestampConverterTest {
    private val zoneId = "Europe/Paris"

    private fun createCalendar(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        second: Int,
    ): Calendar {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone(zoneId))
        calendar.set(year, month - 1, day, hour, minute, second) // Months are 0-based in Calendar
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar
    }

    @Test
    fun testToday() {
        val currentCalendar = createCalendar(2025, 5, 14, 16, 58, 0)
        val timestampCalendar = createCalendar(2025, 5, 14, 10, 0, 0)
        val timestamp = timestampCalendar.timeInMillis
        val result = timestampToHumanReadable(timestamp, currentCalendar, TODAY_STRING, YESTERDAY_STRING)
        assertEquals(TODAY_STRING, result)
    }

    @Test
    fun testYesterday() {
        val currentCalendar = createCalendar(2025, 5, 14, 16, 58, 0)
        val timestampCalendar = createCalendar(2025, 5, 13, 10, 0, 0)
        val timestamp = timestampCalendar.timeInMillis
        val result = timestampToHumanReadable(timestamp, currentCalendar, TODAY_STRING, YESTERDAY_STRING)
        assertEquals(YESTERDAY_STRING, result)
    }

    @Test
    fun testOtherDayPast() {
        val currentCalendar = createCalendar(2025, 5, 14, 16, 58, 0)
        val timestampCalendar = createCalendar(2025, 5, 12, 10, 0, 0)
        val timestamp = timestampCalendar.timeInMillis
        val expected = "Mon, May 12"
        val result = timestampToHumanReadable(timestamp, currentCalendar, TODAY_STRING, YESTERDAY_STRING)
        assertEquals(expected, result)
    }

    @Test
    fun testFutureDay() {
        val currentCalendar = createCalendar(2025, 5, 14, 16, 58, 0)
        val timestampCalendar = createCalendar(2025, 5, 15, 10, 0, 0)
        val timestamp = timestampCalendar.timeInMillis
        val expected = "Thu, May 15"
        val result = timestampToHumanReadable(timestamp, currentCalendar, TODAY_STRING, YESTERDAY_STRING)
        assertEquals(expected, result)
    }

    companion object {
        private const val TODAY_STRING = "Today"
        private const val YESTERDAY_STRING = "Yesterday"
    }
}
