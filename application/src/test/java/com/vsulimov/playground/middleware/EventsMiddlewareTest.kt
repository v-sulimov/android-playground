package com.vsulimov.playground.middleware

import android.content.Context
import android.content.SharedPreferences
import com.vsulimov.navigation.action.NavigationAction.DismissOverlay
import com.vsulimov.playground.action.EventsAction
import com.vsulimov.playground.factory.ApplicationStateTestFactory
import com.vsulimov.playground.state.ApplicationScreenState
import com.vsulimov.playground.state.event.EventState
import com.vsulimov.redux.Action
import kotlinx.coroutines.CoroutineScope
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Unit tests for [EventsMiddleware], verifying the handling of [EventsAction] types.
 * Tests cover saving and deleting events in [SharedPreferences], updating the events list, and passing unhandled actions.
 */
class EventsMiddlewareTest {
    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var scope: CoroutineScope

    private lateinit var nextAction: MutableList<Action>
    private lateinit var dispatchAction: MutableList<Action>

    private lateinit var middleware: EventsMiddleware

    @BeforeTest
    fun setUp() {
        context = mock()
        sharedPreferences = mock()
        editor = mock()
        scope = mock()

        whenever(sharedPreferences.edit()).thenReturn(editor)
        whenever(editor.putString(any(), any())).thenReturn(editor)
        whenever(editor.remove(any())).thenReturn(editor)
        whenever(context.getString(any())).thenReturn("Today")

        nextAction = mutableListOf()
        dispatchAction = mutableListOf()
        middleware = EventsMiddleware(context, sharedPreferences)
    }

    /**
     * Tests that a [EventsAction.SaveEvent] action saves event details to [SharedPreferences]
     * and updates the events list by dispatching an [EventsAction.UpdateEventsList] action
     * when the application state contains an events list.
     */
    @Test
    fun `SaveEvent action saves to SharedPreferences and updates events list when state has events`() {
        val state =
            ApplicationStateTestFactory.createApplicationState(applicationScreenState = ApplicationScreenState.EventsList())
        val action = EventsAction.SaveEvent("Test Event")

        middleware.invokeTyped(
            action = action,
            state = state,
            next = { nextAction.add(it) },
            dispatch = { dispatchAction.add(it) },
            scope = scope,
        )

        val keyCaptor = ArgumentCaptor.forClass(String::class.java)
        verify(editor).putString(keyCaptor.capture(), eq("Test Event"))
        val capturedKey = keyCaptor.value

        assertEquals(expected = 1, actual = nextAction.size, message = "Next should be called once")
        assertEquals(
            expected = DismissOverlay,
            actual = nextAction[0],
            message = "Next should be called with DismissOverlay",
        )

        assertEquals(expected = 1, actual = dispatchAction.size, message = "Dispatch should be called once")
        val updateAction = dispatchAction[0] as EventsAction.UpdateEventsList
        assertEquals(
            expected = 1,
            actual = updateAction.newEvents.size,
            message = "Events list should contain one event",
        )

        val event = updateAction.newEvents[0]
        assertEquals(expected = "Test Event", actual = event.details, message = "Event details should match")
        assertEquals(
            expected = capturedKey.toLong(),
            actual = event.timestamp,
            message = "Event timestamp should match the saved key",
        )
        assertEquals(
            expected = "Today",
            actual = event.formattedTimestamp,
            message = "Formatted timestamp should be 'Today'",
        )
    }

    /**
     * Tests that a [EventsAction.SaveEvent] action saves event details to [SharedPreferences]
     * but does not call the next middleware or dispatch when the application state does not contain an events list.
     */
    @Test
    fun `SaveEvent action saves to SharedPreferences but does not call next when state has no events`() {
        val state =
            ApplicationStateTestFactory.createApplicationState(applicationScreenState = ApplicationScreenState.Onboarding())
        val action = EventsAction.SaveEvent("Test Event")

        middleware.invokeTyped(
            action = action,
            state = state,
            next = { nextAction.add(it) },
            dispatch = { dispatchAction.add(it) },
            scope = scope,
        )

        verify(editor).putString(anyString(), eq("Test Event"))
        assertTrue(actual = nextAction.isEmpty(), message = "Next should not be called")
        assertTrue(dispatchAction.isEmpty(), "Dispatch should not be called")
    }

    /**
     * Tests that a [EventsAction.DeleteEvent] action removes the event from [SharedPreferences]
     * and updates the events list by dispatching an [EventsAction.UpdateEventsList] action
     * when the application state contains an events list.
     */
    @Test
    fun `DeleteEvent action removes from SharedPreferences and updates events list when state has events`() {
        val event1 = EventState(details = "Event 1", timestamp = 1000, formattedTimestamp = "Today")
        val event2 = EventState(details = "Event 2", timestamp = 2000, formattedTimestamp = "Today")
        val events = listOf(event1, event2)
        val applicationScreenState = ApplicationScreenState.EventsList(events = events)
        val state = ApplicationStateTestFactory.createApplicationState(applicationScreenState = applicationScreenState)
        val action = EventsAction.DeleteEvent(eventTimestamp = 1000)

        middleware.invokeTyped(
            action = action,
            state = state,
            next = { nextAction.add(it) },
            dispatch = { dispatchAction.add(it) },
            scope = scope,
        )

        verify(editor).remove("1000")
        assertEquals(1, nextAction.size, "Next should be called once")
        assertEquals(DismissOverlay, nextAction[0], "Next should be called with DismissOverlay")
        assertEquals(1, dispatchAction.size, "Dispatch should be called once")
        val updateAction = dispatchAction[0] as EventsAction.UpdateEventsList
        assertEquals(1, updateAction.newEvents.size, "Events list should contain one event")
        assertEquals(event2, updateAction.newEvents[0], "Remaining event should be event2")
    }

    /**
     * Tests that a [EventsAction.DeleteEvent] action removes the event from [SharedPreferences]
     * but does not call the next middleware or dispatch when the application state does not contain an events list.
     */
    @Test
    fun `DeleteEvent action removes from SharedPreferences but does not call next when state has no events`() {
        val state =
            ApplicationStateTestFactory.createApplicationState(applicationScreenState = ApplicationScreenState.Onboarding())
        val action = EventsAction.DeleteEvent(eventTimestamp = 1000)

        middleware.invokeTyped(
            action = action,
            state = state,
            next = { nextAction.add(it) },
            dispatch = { dispatchAction.add(it) },
            scope = scope,
        )

        verify(editor).remove("1000")
        assertTrue(nextAction.isEmpty(), "Next should not be called")
        assertTrue(dispatchAction.isEmpty(), "Dispatch should not be called")
    }

    /**
     * Tests that a [EventsAction.DeleteEvent] action for a non-existent event still calls remove on [SharedPreferences]
     * and updates the events list to remain unchanged when the application state contains an events list.
     */
    @Test
    fun `DeleteEvent action for non-existent event calls remove and keeps events list unchanged`() {
        val event1 = EventState(details = "Event 1", timestamp = 1000, formattedTimestamp = "Today")
        val events = listOf(event1)
        val applicationScreenState = ApplicationScreenState.EventsList(events = events)
        val state = ApplicationStateTestFactory.createApplicationState(applicationScreenState = applicationScreenState)
        val action = EventsAction.DeleteEvent(eventTimestamp = 2000)

        middleware.invokeTyped(
            action = action,
            state = state,
            next = { nextAction.add(it) },
            dispatch = { dispatchAction.add(it) },
            scope = scope,
        )

        verify(editor).remove("2000")
        assertEquals(1, nextAction.size, "Next should be called once")
        assertEquals(DismissOverlay, nextAction[0], "Next should be called with DismissOverlay")
        assertEquals(1, dispatchAction.size, "Dispatch should be called once")
        val updateAction = dispatchAction[0] as EventsAction.UpdateEventsList
        assertEquals(1, updateAction.newEvents.size, "Events list should contain one event")
        assertEquals(event1, updateAction.newEvents[0], "Events list should remain unchanged")
    }

    /**
     * Tests that any [EventsAction] other than [EventsAction.SaveEvent] or [EventsAction.DeleteEvent]
     * is passed to the next middleware unchanged, without interacting with [SharedPreferences].
     */
    @Test
    fun `Other EventsAction is passed to next unchanged`() {
        val state = ApplicationStateTestFactory.createApplicationState()
        val action = EventsAction.UpdateEventsList(emptyList())

        middleware.invokeTyped(
            action = action,
            state = state,
            next = { nextAction.add(it) },
            dispatch = { dispatchAction.add(it) },
            scope = scope,
        )

        assertEquals(expected = 1, actual = nextAction.size, message = "Next should be called once")
        assertEquals(expected = action, actual = nextAction[0], message = "Action should be passed unchanged")
        verify(sharedPreferences, never()).edit()
        assertTrue(dispatchAction.isEmpty(), "Dispatch should not be called")
    }
}
