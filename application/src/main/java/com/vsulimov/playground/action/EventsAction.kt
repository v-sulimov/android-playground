package com.vsulimov.playground.action

import com.vsulimov.playground.state.event.EventState
import com.vsulimov.redux.Action

/**
 * Sealed class representing actions related to events in the application.
 *
 * This class serves as a base for all event-related actions that can be dispatched to modify the application's
 * event state.
 */
sealed class EventsAction : Action {
    /**
     * Action to save a new event with the provided details.
     *
     * This action is dispatched to create and store a new event in the application. The event details are
     * provided as a string.
     *
     * @property eventDetails A string containing the details of the event to be saved.
     */
    data class SaveEvent(
        val eventDetails: String,
    ) : EventsAction()

    /**
     * Action to delete an existing event identified by its timestamp.
     *
     * This action is dispatched to remove an event from the application's event state. The event is
     * identified by its unique timestamp, which is used to locate and delete the corresponding event.
     *
     * @property eventTimestamp The timestamp of the event to be deleted.
     */
    data class DeleteEvent(
        val eventTimestamp: Long,
    ) : EventsAction()

    /**
     * Action to update the list of events in the application state.
     *
     * This action is dispatched to replace the current list of events with a new list. It is typically used
     * to refresh the event state after modifications, such as adding or removing events.
     *
     * @property newEvents The updated list of [EventState] objects representing the current events.
     */
    data class UpdateEventsList(
        val newEvents: List<EventState>,
    ) : EventsAction()
}
