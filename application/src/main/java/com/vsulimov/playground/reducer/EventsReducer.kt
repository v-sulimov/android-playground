package com.vsulimov.playground.reducer

import com.vsulimov.playground.action.EventsAction
import com.vsulimov.playground.state.ScreenState
import com.vsulimov.redux.TypedReducer

/**
 * A reducer responsible for handling [EventsAction] actions and updating the [ScreenState] accordingly.
 * This reducer processes actions related to event management, such as updating the events list.
 */
object EventsReducer : TypedReducer<EventsAction, ScreenState>(EventsAction::class.java) {

    /**
     * Reduces the provided [EventsAction] to update the [ScreenState].
     *
     * @param action The [EventsAction] to process, which determines how the state should be updated.
     * @param state The current [ScreenState] of the application screen.
     * @return The updated [ScreenState] after applying the action, or the unchanged state if the action is not applicable.
     */
    override fun reduceTyped(
        action: EventsAction,
        state: ScreenState
    ): ScreenState {
        return when (action) {
            is EventsAction.UpdateEventsList -> {
                when (state) {
                    is ScreenState.EventsList -> state.copy(events = action.newEvents)
                    else -> state
                }
            }

            else -> state
        }
    }
}
