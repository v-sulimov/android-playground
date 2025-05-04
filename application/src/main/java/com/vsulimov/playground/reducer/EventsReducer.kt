package com.vsulimov.playground.reducer

import com.vsulimov.navigation.state.ScreenState
import com.vsulimov.playground.action.EventsAction
import com.vsulimov.playground.state.ApplicationScreenState
import com.vsulimov.redux.TypedReducer

/**
 * Reducer for `EventsAction` actions.
 *
 * This reducer handles actions related to the events feature of the application,
 * modifying the `ApplicationScreenState` based on the dispatched `EventsAction`.
 */
object EventsReducer : TypedReducer<EventsAction, ScreenState>(EventsAction::class.java) {
    /**
     * Updates the `ScreenState` based on the provided `EventsAction`.
     *
     * This function processes specific `EventsAction` types to modify the state.
     * Currently, it handles:
     * - `EventsAction.UpdateEventsList`: If the current state is `ApplicationScreenState.EventsList`,
     *   it updates the `events` property with the `newEvents` from the action.
     *
     * If the action is not recognized or not applicable to the current state, the original
     * state is returned unchanged.
     *
     * @param action The `EventsAction` to be processed.
     * @param state The current `ScreenState` of the application.
     * @return The new `ScreenState` after applying the action, or the original state if
     * the action is not applicable.
     */
    override fun reduceTyped(
        action: EventsAction,
        state: ScreenState,
    ): ScreenState =
        when (action) {
            is EventsAction.UpdateEventsList -> {
                when (state) {
                    is ApplicationScreenState.EventsList -> state.copy(events = action.newEvents)
                    else -> state // If the state is not EventsList, return it unchanged.
                }
            }

            else -> state // Implicitly handled by the when expression returning state for unhandled actions.
        }
}
