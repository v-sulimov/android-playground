package com.vsulimov.playground.middleware

import android.content.Context
import android.content.SharedPreferences
import com.vsulimov.navigation.action.NavigationAction
import com.vsulimov.navigation.action.NavigationAction.NavigateTo
import com.vsulimov.playground.R
import com.vsulimov.playground.converter.timestamp.UnixTimestampConverter
import com.vsulimov.playground.state.ApplicationScreenState.EventsList
import com.vsulimov.playground.state.ApplicationState
import com.vsulimov.playground.state.event.EventState
import com.vsulimov.redux.Action
import com.vsulimov.redux.TypedMiddleware
import kotlinx.coroutines.CoroutineScope

/**
 * Middleware for preloading cached events when navigating to the EventsList screen.
 *
 * This middleware intercepts [NavigationAction] actions and preloads cached event data from
 * [SharedPreferences] when navigating to [EventsList], updating the screen state with the cached events.
 *
 * Note: For large datasets, consider asynchronous loading or pagination to avoid performance issues
 * when accessing [SharedPreferences].
 *
 * @property sharedPreferences The [SharedPreferences] instance used to access cached event data.
 */
class PreloadCacheMiddleware(
    private val context: Context,
    private val sharedPreferences: SharedPreferences,
) : TypedMiddleware<NavigationAction, ApplicationState>(NavigationAction::class.java) {
    /**
     * Processes the provided [NavigationAction] and determines whether to preload cached events.
     *
     * @param action The [NavigationAction] to process.
     * @param state The current [ApplicationState] of the application.
     * @param next The function to pass the action to the next middleware or reducer.
     * @param dispatch The function to dispatch a new action.
     */
    override fun invokeTyped(
        action: NavigationAction,
        state: ApplicationState,
        next: (Action) -> Unit,
        dispatch: (Action) -> Unit,
        scope: CoroutineScope,
    ) {
        when (action) {
            is NavigateTo -> {
                when (action.newScreen) {
                    is EventsList -> preloadCachedEventsList(action, next)
                    else -> next(action)
                }
            }

            else -> next(action)
        }
    }

    /**
     * Preloads cached events from [SharedPreferences] and updates the [EventsList] screen with the events.
     *
     * @param originalAction The original [NavigateTo] action targeting the [EventsList] screen.
     * @param next The function to pass the updated action to the next middleware or reducer.
     */
    private fun preloadCachedEventsList(
        originalAction: NavigateTo,
        next: (Action) -> Unit,
    ) {
        val savedEvents =
            sharedPreferences.all.toSortedMap().map {
                EventState(
                    timestamp = it.key.toLong(),
                    details = it.value.toString(),
                    formattedTimestamp =
                        UnixTimestampConverter.timestampToHumanReadable(
                            timestamp = it.key.toLong(),
                            todayString = context.getString(R.string.today),
                            yesterdayString = context.getString(R.string.yesterday),
                        ),
                )
            }
        next(NavigateTo(newScreen = EventsList(events = savedEvents), clearBackStack = originalAction.clearBackStack))
    }
}
