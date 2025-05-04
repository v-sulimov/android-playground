package com.vsulimov.playground.factory

import android.content.Context
import com.vsulimov.playground.middleware.EventsMiddleware

/**
 * A factory object responsible for creating instances of [EventsMiddleware].
 * Provides a centralized way to construct [EventsMiddleware] with the necessary dependencies.
 */
object EventsMiddlewareFactory {
    /**
     * Creates an instance of [EventsMiddleware] with the provided context.
     *
     * @param context The Android context used to initialize the shared preferences.
     * @return A new instance of [EventsMiddleware] configured with shared preferences.
     */
    fun create(context: Context): EventsMiddleware {
        val sharedPreferences = SharedPreferencesFactory.createEventsSharedPreferences(context)
        return EventsMiddleware(context, sharedPreferences)
    }
}
