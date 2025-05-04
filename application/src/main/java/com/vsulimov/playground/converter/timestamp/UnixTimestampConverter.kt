package com.vsulimov.playground.converter.timestamp

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Utility object for converting Unix timestamps to human-readable date strings with customizable labels.
 */
object UnixTimestampConverter {
    /**
     * Converts a Unix timestamp to a human-readable date string.
     *
     * This function takes a Unix timestamp (in seconds) and converts it to a human-readable format.
     * If the timestamp corresponds to the current day, it returns the provided `todayString`.
     * If it corresponds to the previous day, it returns the provided `yesterdayString`.
     * Otherwise, it formats the date as "EEE, MMM d" (e.g., "Wed, Oct 25").
     *
     * @param timestamp The Unix timestamp in seconds.
     * @param currentTime The reference calendar for determining the current time. Defaults to the system's current time.
     * @param todayString The string to return if the timestamp is for the current day.
     * @param yesterdayString The string to return if the timestamp is for the previous day.
     * @return A human-readable string representing the date.
     */
    fun timestampToHumanReadable(
        timestamp: Long,
        currentTime: Calendar = Calendar.getInstance(),
        todayString: String,
        yesterdayString: String,
    ): String {
        val timeZone = currentTime.timeZone
        val date = Date(timestamp) // Convert seconds to milliseconds
        val calendar = Calendar.getInstance(timeZone).apply { time = date }

        val currentCalendar = currentTime.clone() as Calendar

        val isSameDay =
            calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR)

        if (isSameDay) {
            return todayString
        }

        val yesterdayCalendar = currentCalendar.clone() as Calendar
        yesterdayCalendar.add(Calendar.DAY_OF_YEAR, -1)

        val isYesterday =
            calendar.get(Calendar.YEAR) == yesterdayCalendar.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == yesterdayCalendar.get(Calendar.DAY_OF_YEAR)

        if (isYesterday) {
            return yesterdayString
        }

        val formatter = SimpleDateFormat("EEE, MMM d", Locale.ENGLISH)
        formatter.timeZone = timeZone
        return formatter.format(date)
    }
}
