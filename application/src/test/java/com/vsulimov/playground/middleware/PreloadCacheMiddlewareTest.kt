package com.vsulimov.playground.middleware

import android.content.Context
import android.content.SharedPreferences
import com.vsulimov.navigation.action.NavigationAction
import com.vsulimov.navigation.action.NavigationAction.GoBack
import com.vsulimov.navigation.action.NavigationAction.NavigateTo
import com.vsulimov.playground.factory.ApplicationStateTestFactory
import com.vsulimov.playground.state.ApplicationScreenState
import com.vsulimov.playground.state.ApplicationScreenState.EventsList
import com.vsulimov.redux.Action
import kotlinx.coroutines.CoroutineScope
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Unit tests for [PreloadCacheMiddleware], verifying the handling of [NavigationAction] types.
 * Tests cover preloading cached events when navigating to [EventsList] and passing unhandled actions.
 */
class PreloadCacheMiddlewareTest {
    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var nextAction: MutableList<Action>
    private lateinit var dispatchAction: MutableList<Action>
    private lateinit var middleware: PreloadCacheMiddleware
    private lateinit var scope: CoroutineScope

    @Before
    fun setUp() {
        context = mock()
        sharedPreferences = mock(SharedPreferences::class.java)
        nextAction = mutableListOf()
        dispatchAction = mutableListOf()
        middleware = PreloadCacheMiddleware(context, sharedPreferences)
        scope = mock()

        whenever(context.getString(any())).thenReturn("Today")
    }

    /**
     * Tests that a [NavigateTo] action targeting [EventsList] preloads cached events from [SharedPreferences]
     * and updates the [EventsList] with the cached events before passing the action to the next middleware.
     */
    @Test
    fun `NavigateTo EventsList preloads cached events and updates the screen state`() {
        val cachedEvents =
            mapOf(
                "1000" to "Event 1",
                "2000" to "Event 2",
            )
        `when`(sharedPreferences.all).thenReturn(cachedEvents)

        val action = NavigateTo(newScreen = EventsList(), clearBackStack = false)
        val state = ApplicationStateTestFactory.createApplicationState()

        middleware.invokeTyped(
            action = action,
            state = state,
            next = { nextAction.add(it) },
            dispatch = { dispatchAction.add(it) },
            scope = scope,
        )

        verify(sharedPreferences).all
        assertEquals(1, nextAction.size, "Next should be called once")
        val updatedAction = nextAction[0] as NavigateTo
        assertTrue(updatedAction.newScreen is EventsList)
        val eventsList = updatedAction.newScreen as EventsList
        assertEquals(2, eventsList.events.size, "Should have two preloaded events")
        assertEquals("Event 1", eventsList.events[0].details)
        assertEquals(1000L, eventsList.events[0].timestamp)
        assertEquals("Event 2", eventsList.events[1].details)
        assertEquals(2000L, eventsList.events[1].timestamp)
        assertEquals(action.clearBackStack, updatedAction.clearBackStack, "clearBackStack should remain unchanged")

        assertTrue(dispatchAction.isEmpty(), "Dispatch should not be called")
    }

    /**
     * Tests that a [NavigateTo] action targeting a screen other than [EventsList] is passed to the next middleware
     * unchanged, without interacting with [SharedPreferences].
     */
    @Test
    fun `NavigateTo other screen passes action unchanged without accessing SharedPreferences`() {
        val action = NavigateTo(newScreen = ApplicationScreenState.Onboarding(), clearBackStack = true)
        val state = ApplicationStateTestFactory.createApplicationState()

        middleware.invokeTyped(
            action = action,
            state = state,
            next = { nextAction.add(it) },
            dispatch = { dispatchAction.add(it) },
            scope = scope,
        )

        assertEquals(1, nextAction.size, "Next should be called once")
        assertEquals(action, nextAction[0], "Action should be passed unchanged")

        verify(sharedPreferences, never()).all

        assertTrue(dispatchAction.isEmpty(), "Dispatch should not be called")
    }

    /**
     * Tests that any [NavigationAction] other than [NavigateTo] is passed to the next middleware unchanged,
     * without interacting with [SharedPreferences].
     */
    @Test
    fun `Other NavigationAction is passed to next unchanged`() {
        val action = GoBack
        val state = ApplicationStateTestFactory.createApplicationState()

        middleware.invokeTyped(
            action = action,
            state = state,
            next = { nextAction.add(it) },
            dispatch = { dispatchAction.add(it) },
            scope = scope,
        )

        assertEquals(1, nextAction.size, "Next should be called once")
        assertEquals(action, nextAction[0], "Action should be passed unchanged")

        verify(sharedPreferences, never()).all

        assertTrue(dispatchAction.isEmpty(), "Dispatch should not be called")
    }
}
