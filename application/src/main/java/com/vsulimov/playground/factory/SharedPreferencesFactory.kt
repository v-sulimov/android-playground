package com.vsulimov.playground.factory

import android.content.Context
import android.content.SharedPreferences

/**
 * A factory object responsible for creating [SharedPreferences] instances for event-related data.
 * Provides a centralized way to initialize shared preferences with consistent configurations.
 */
object SharedPreferencesFactory {
    /**
     * The name of the [SharedPreferences] file used to store event data.
     */
    private const val SHARED_PREFERENCES_EVENTS = "events"

    /**
     * Creates a [SharedPreferences] instance for storing event data.
     *
     * @param context The Android context used to access the shared preferences.
     * @return A [SharedPreferences] instance configured for private access to event data.
     */
    fun createEventsSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_EVENTS, Context.MODE_PRIVATE)
}
