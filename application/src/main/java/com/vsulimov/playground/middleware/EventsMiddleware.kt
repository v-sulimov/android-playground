package com.vsulimov.playground.middleware

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.vsulimov.navigation.action.NavigationAction.DismissOverlay
import com.vsulimov.playground.R
import com.vsulimov.playground.action.EventsAction
import com.vsulimov.playground.converter.timestamp.UnixTimestampConverter.timestampToHumanReadable
import com.vsulimov.playground.lens.toEventsListScreenStateEvents
import com.vsulimov.playground.state.ApplicationState
import com.vsulimov.playground.state.event.EventState
import com.vsulimov.redux.Action
import com.vsulimov.redux.TypedMiddleware
import kotlinx.coroutines.CoroutineScope

/**
 * Middleware for handling [EventsAction] actions, managing event-related operations such as saving and deleting events.
 *
 * This middleware interacts with [SharedPreferences] to persist event data and updates the application state
 * with new or modified events lists when the current screen state is [ScreenState.EventsList].
 *
 * @property context The [Context] used to access string resources for formatting timestamps.
 * @property sharedPreferences The [SharedPreferences] instance used to store and retrieve event data.
 */
class EventsMiddleware(
    private val context: Context,
    private val sharedPreferences: SharedPreferences,
) : TypedMiddleware<EventsAction, ApplicationState>(EventsAction::class.java) {
    /**
     * Processes the provided [EventsAction] and determines the appropriate action to take.
     * It handles [EventsAction.SaveEvent] by saving the event to [SharedPreferences] and updating the events list if applicable,
     * handles [EventsAction.DeleteEvent] by removing the event from [SharedPreferences] and updating the events list if applicable,
     * and passes all other actions to the next middleware unchanged.
     *
     * @param action The [EventsAction] to process.
     * @param state The current [ApplicationState] of the application.
     * @param next The function to pass actions to the next middleware or reducer.
     * @param dispatch The function to dispatch new actions to the application state.
     */
    override fun invokeTyped(
        action: EventsAction,
        state: ApplicationState,
        next: (Action) -> Unit,
        dispatch: (Action) -> Unit,
        scope: CoroutineScope,
    ) {
        when (action) {
            is EventsAction.SaveEvent -> handleSaveEventAction(action, state, next, dispatch)
            is EventsAction.DeleteEvent -> handleDeleteEventAction(action, state, next, dispatch)
            else -> next(action)
        }
    }

    /**
     * Handles the [EventsAction.SaveEvent] action by saving the event details to [SharedPreferences].
     * If the current screen state is [ScreenState.EventsList], it updates the events list by adding the new event,
     * passes [DismissOverlay] to the next middleware to close the overlay, and dispatches an
     * [EventsAction.UpdateEventsList] with the updated events list. If the screen state is not [ScreenState.EventsList],
     * it only saves the event to [SharedPreferences] without further action.
     *
     * @param action The [EventsAction.SaveEvent] containing the event details to save.
     * @param state The current [ApplicationState] of the application.
     * @param next The function to pass the [DismissOverlay] action to the next middleware or reducer.
     * @param dispatch The function to dispatch the [EventsAction.UpdateEventsList] action.
     */
    private fun handleSaveEventAction(
        action: EventsAction.SaveEvent,
        state: ApplicationState,
        next: (Action) -> Unit,
        dispatch: (Action) -> Unit,
    ) {
        val timestamp = System.currentTimeMillis()
        sharedPreferences.edit { putString(timestamp.toString(), action.eventDetails) }
        state.toEventsListScreenStateEvents()?.let { currentEvents ->
            val updatedEvents =
                currentEvents.toMutableList().also {
                    it.add(
                        EventState(
                            details = action.eventDetails,
                            timestamp = timestamp,
                            formattedTimestamp =
                                timestampToHumanReadable(
                                    timestamp = timestamp,
                                    todayString = context.getString(R.string.today),
                                    yesterdayString = context.getString(R.string.yesterday),
                                ),
                        ),
                    )
                }
            next(DismissOverlay)
            dispatch(EventsAction.UpdateEventsList(updatedEvents))
        }
    }

    /**
     * Handles the [EventsAction.DeleteEvent] action by removing the event from [SharedPreferences] using its timestamp.
     * If the current screen state is [ScreenState.EventsList], it updates the events list by removing the event with
     * the matching timestamp, passes [DismissOverlay] to the next middleware to close the overlay, and dispatches an
     * [EventsAction.UpdateEventsList] with the updated events list. If the screen state is not [ScreenState.EventsList],
     * it only removes the event from [SharedPreferences] without further action.
     *
     * @param action The [EventsAction.DeleteEvent] containing the timestamp of the event to delete.
     * @param state The current [ApplicationState] of the application.
     * @param next The function to pass the [DismissOverlay] action to the next middleware or reducer.
     * @param dispatch The function to dispatch the [EventsAction.UpdateEventsList] action.
     */
    private fun handleDeleteEventAction(
        action: EventsAction.DeleteEvent,
        state: ApplicationState,
        next: (Action) -> Unit,
        dispatch: (Action) -> Unit,
    ) {
        sharedPreferences.edit { remove(action.eventTimestamp.toString()) }
        state.toEventsListScreenStateEvents()?.let { currentEvents ->
            val updatedEvents =
                currentEvents.toMutableList().also {
                    it.removeIf { it.timestamp == action.eventTimestamp }
                }
            next(DismissOverlay)
            dispatch(EventsAction.UpdateEventsList(updatedEvents))
        }
    }
}
