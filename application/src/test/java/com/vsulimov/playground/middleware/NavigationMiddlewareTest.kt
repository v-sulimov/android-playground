package com.vsulimov.playground.middleware

import com.vsulimov.navigation.action.NavigationAction.GoBack
import com.vsulimov.playground.factory.ApplicationStateTestFactory
import com.vsulimov.playground.state.ApplicationScreenState
import com.vsulimov.playground.state.ApplicationState
import com.vsulimov.redux.Action
import com.vsulimov.stack.CopyOnWriteStack
import kotlinx.coroutines.CoroutineScope
import org.mockito.Mockito.mock
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Unit tests for the [NavigationMiddleware] class using the kotlin.test framework.
 *
 * This test suite verifies the behavior of [NavigationMiddleware] when processing [NavigationAction.GoBack]
 * actions under different navigation states. It ensures the middleware correctly invokes the
 * [finishActivityFunction] when appropriate and passes the action to the next middleware in the chain.
 *
 * @see NavigationMiddleware
 * @see NavigationAction.GoBack
 * @see ApplicationState
 */
class NavigationMiddlewareTest {
    private var finishActivityCalled: Boolean = false
    private lateinit var nextAction: MutableList<Action>
    private lateinit var dispatchAction: MutableList<Action>
    private lateinit var middleware: NavigationMiddleware
    private lateinit var scope: CoroutineScope

    /**
     * Sets up the test environment before each test.
     *
     * Initializes the [finishActivityCalled] flag, [nextAction] and [dispatchAction] lists, and creates
     * a new [NavigationMiddleware] instance with a mock finish activity function.
     */
    @BeforeTest
    fun setUp() {
        finishActivityCalled = false
        nextAction = mutableListOf()
        dispatchAction = mutableListOf()
        middleware = NavigationMiddleware(finishActivityFunction = { finishActivityCalled = true })
        scope = mock()
    }

    /**
     * Tests that [finishActivityFunction] is called when the overlay is null and the back stack is empty.
     *
     * Verifies that when a [NavigationAction.GoBack] action is processed with an empty back stack and no
     * overlay, the [finishActivityFunction] is invoked, and the action is passed to the next middleware.
     */
    @Test
    fun `should call finishActivityFunction when overlay is null and backStack is empty`() {
        val state = ApplicationStateTestFactory.createApplicationState(overlayState = null)
        val action = GoBack

        middleware.invokeTyped(
            action = action,
            state = state,
            next = { nextAction.add(it) },
            dispatch = { dispatchAction.add(it) },
            scope = scope,
        )

        assertTrue(finishActivityCalled, "finishActivityFunction should be called")
        assertEquals<List<Action>>(listOf(action), nextAction, "Action should be passed to next middleware")
        assertEquals(emptyList(), dispatchAction, "No actions should be dispatched")
    }

    /**
     * Tests that [finishActivityFunction] is not called when an overlay is present.
     *
     * Verifies that when a [NavigationAction.GoBack] action is processed with an overlay present,
     * the [finishActivityFunction] is not invoked, and the action is passed to the next middleware.
     */
    @Test
    fun `should not call finishActivityFunction when overlay is present`() {
        val state = ApplicationStateTestFactory.createApplicationState()
        val action = GoBack

        middleware.invokeTyped(
            action = action,
            state = state,
            next = { nextAction.add(it) },
            dispatch = { dispatchAction.add(it) },
            scope = scope,
        )

        assertFalse(finishActivityCalled, "finishActivityFunction should not be called")
        assertEquals<List<Action>>(listOf(action), nextAction, "Action should be passed to next middleware")
        assertEquals(emptyList(), dispatchAction, "No actions should be dispatched")
    }

    /**
     * Tests that [finishActivityFunction] is not called when the back stack is not empty.
     *
     * Verifies that when a [NavigationAction.GoBack] action is processed with a non-empty back stack,
     * the [finishActivityFunction] is not invoked, and the action is passed to the next middleware.
     */
    @Test
    fun `should not call finishActivityFunction when backStack is not empty`() {
        val state =
            ApplicationStateTestFactory.createApplicationState(
                backStack = CopyOnWriteStack(listOf(ApplicationScreenState.Onboarding())),
            )
        val action = GoBack

        middleware.invokeTyped(
            action = action,
            state = state,
            next = { nextAction.add(it) },
            dispatch = { dispatchAction.add(it) },
            scope = scope,
        )

        assertFalse(finishActivityCalled, "finishActivityFunction should not be called")
        assertEquals<List<Action>>(listOf(action), nextAction, "Action should be passed to next middleware")
        assertEquals(emptyList(), dispatchAction, "No actions should be dispatched")
    }
}
