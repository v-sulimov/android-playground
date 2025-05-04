package com.vsulimov.playground.reducer

import com.vsulimov.playground.action.EventsAction
import com.vsulimov.playground.state.ApplicationScreenState
import com.vsulimov.playground.state.event.EventState
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Test class for [EventsReducer], ensuring it correctly processes [EventsAction] actions
 * and updates the [ApplicationScreenState] accordingly.
 */
class EventsReducerTest {
    /**
     * Tests that the [EventsReducer] updates the events list in [ApplicationScreenState.EventsList]
     * when handling an [EventsAction.UpdateEventsList] action.
     */
    @Test
    fun `reduceTyped updates events list when state is EventsList and action is UpdateEventsList`() {
        val initialEvents =
            listOf(
                EventState(details = "Event 1", timestamp = 1L, formattedTimestamp = "Today"),
                EventState(details = "Event 2", timestamp = 2L, formattedTimestamp = "Today"),
            )
        val state = ApplicationScreenState.EventsList(events = initialEvents)
        val newEvents =
            listOf(
                EventState(details = "Event 3", timestamp = 3L, formattedTimestamp = "Today"),
                EventState(details = "Event 4", timestamp = 4L, formattedTimestamp = "Today"),
            )
        val action = EventsAction.UpdateEventsList(newEvents)

        val newState = EventsReducer.reduceTyped(action, state)

        assertTrue(newState is ApplicationScreenState.EventsList)
        val eventsListState = newState
        assertEquals(newEvents, eventsListState.events)
    }

    /**
     * Tests that the [EventsReducer] returns the current [ApplicationScreenState] unchanged
     * when the state is not [ApplicationScreenState.EventsList] and the action is [EventsAction.UpdateEventsList].
     */
    @Test
    fun `reduceTyped returns same state when state is not EventsList and action is UpdateEventsList`() {
        val state = ApplicationScreenState.Onboarding()
        val newEvents =
            listOf(
                EventState(details = "Event 3", timestamp = 3L, formattedTimestamp = "Today"),
                EventState(details = "Event 4", timestamp = 4L, formattedTimestamp = "Today"),
            )
        val action = EventsAction.UpdateEventsList(newEvents)

        val newState = EventsReducer.reduceTyped(action, state)

        assertSame(state, newState)
    }

    /**
     * Tests that the [EventsReducer] returns the current [ApplicationScreenState] unchanged
     * when the action is not [EventsAction.UpdateEventsList].
     */
    @Test
    fun `reduceTyped returns same state when action is not UpdateEventsList`() {
        val initialEvents = listOf(EventState(details = "Event 1", timestamp = 1L, formattedTimestamp = "Today"))
        val state = ApplicationScreenState.EventsList(events = initialEvents)
        val action = EventsAction.SaveEvent(eventDetails = "New Event")

        val newState = EventsReducer.reduceTyped(action, state)

        assertSame(state, newState)
    }
}
