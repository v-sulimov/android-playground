package com.vsulimov.playground.reducer

import com.vsulimov.navigation.action.NavigationAction
import com.vsulimov.navigation.state.NavigationState
import com.vsulimov.navigation.state.ScreenState
import com.vsulimov.navigation.state.TransitionType
import com.vsulimov.playground.action.ActivityLifecycleAction
import com.vsulimov.playground.factory.ApplicationStateFactory
import com.vsulimov.playground.state.ApplicationScreenState
import com.vsulimov.playground.state.ApplicationState
import com.vsulimov.redux.Action
import com.vsulimov.stack.CopyOnWriteStack
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

/**
 * Test class for [RootReducer], ensuring it correctly processes various actions
 * and updates the [ApplicationState] as expected.
 */
class RootReducerTest {
    private val rootReducer = RootReducer()

    /**
     * Tests that the [RootReducer] correctly handles a [NavigationAction.NavigateTo] by delegating
     * to the [NavigationReducer] and updating the [ApplicationState]'s [NavigationState] with the new screen
     * and updated back stack.
     */
    @Test
    fun `reduce handles NavigationAction PushScreen by updating navigation state`() {
        val initialState =
            ApplicationState(
                navigationState =
                    NavigationState(
                        screen = ApplicationScreenState.Onboarding(),
                        backStack = CopyOnWriteStack<ScreenState>(),
                        overlay = null,
                    ),
            )

        val action = NavigationAction.NavigateTo(ApplicationScreenState.EventsList())

        val newState = rootReducer.reduce(action, initialState)

        val expectedBackStack = CopyOnWriteStack<ScreenState>(listOf(ApplicationScreenState.Onboarding()))
        val expectedNavigationState =
            NavigationState(
                screen = ApplicationScreenState.EventsList(),
                backStack = expectedBackStack,
                overlay = null,
                transitionType = TransitionType.FORWARD,
            )
        assertEquals(expectedNavigationState, newState.navigationState)
        assertEquals(initialState.copy(navigationState = expectedNavigationState), newState)
    }

    /**
     * Tests that the [RootReducer] correctly handles an [ActivityLifecycleAction.OnDestroy] with
     * isFinishing = true by delegating to the [ActivityLifecycleReducer] and resetting the
     * [ApplicationState] to its initial default state.
     */
    @Test
    fun `reduce handles ActivityLifecycleAction OnDestroy with isFinishing true by resetting state`() {
        val initialState =
            ApplicationState(
                navigationState =
                    NavigationState(
                        screen = ApplicationScreenState.EventsList(),
                        backStack = CopyOnWriteStack<ScreenState>().apply { push(ApplicationScreenState.Onboarding()) },
                        overlay = null,
                    ),
            )
        val action = ActivityLifecycleAction.OnDestroy(isFinishing = true)

        val newState = rootReducer.reduce(action, initialState)

        val expectedState = ApplicationStateFactory.createInitialApplicationState()
        assertEquals(expectedState, newState)
    }

    /**
     * Tests that the [RootReducer] returns the current [ApplicationState] unchanged when processing
     * an action that is neither a [NavigationAction] nor an [ActivityLifecycleAction].
     */
    @Test
    fun `reduce returns unchanged state for unhandled actions`() {
        val initialState =
            ApplicationState(
                navigationState =
                    NavigationState(
                        screen = ApplicationScreenState.Onboarding(),
                        backStack = CopyOnWriteStack(),
                        overlay = null,
                    ),
            )

        val action = object : Action {}

        val newState = rootReducer.reduce(action, initialState)

        assertSame(initialState, newState)
    }
}
