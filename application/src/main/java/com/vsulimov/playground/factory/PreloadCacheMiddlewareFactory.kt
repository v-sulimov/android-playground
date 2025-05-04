package com.vsulimov.playground.factory

import android.content.Context
import com.vsulimov.playground.middleware.PreloadCacheMiddleware

/**
 * A factory object responsible for creating instances of [PreloadCacheMiddleware].
 * Provides a centralized way to construct [PreloadCacheMiddleware] with the necessary dependencies.
 */
object PreloadCacheMiddlewareFactory {
    /**
     * Creates an instance of [PreloadCacheMiddleware] with the provided context.
     *
     * @param context The Android context used to initialize the shared preferences for event caching.
     * @return A new instance of [PreloadCacheMiddleware] configured with shared preferences.
     */
    fun create(context: Context): PreloadCacheMiddleware {
        val sharedPreferences = SharedPreferencesFactory.createEventsSharedPreferences(context)
        return PreloadCacheMiddleware(context, sharedPreferences)
    }
}
