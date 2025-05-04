package com.vsulimov.playground.factory

import com.vsulimov.navigation.state.NavigationState
import com.vsulimov.playground.state.ApplicationState
import com.vsulimov.playground.state.OverlayState
import com.vsulimov.playground.state.ScreenState
import com.vsulimov.stack.CopyOnWriteStack

/**
 * A factory object responsible for creating initial states for the application, including
 * [ApplicationState] and [NavigationState].
 */
object ApplicationStateFactory {

    /**
     * Creates the initial [ApplicationState] for the store.
     *
     * This method constructs an [ApplicationState] with an initial [NavigationState] to set up the
     * application's starting state.
     *
     * @return The initial [ApplicationState].
     */
    fun createInitialApplicationState() =
        ApplicationState(
            navigationState = createInitialNavigationState()
        )

    /**
     * Creates the initial [NavigationState] for the application.
     *
     * This method constructs a [NavigationState] with the onboarding screen as the initial screen,
     * an empty back stack, and no overlay.
     *
     * @return The initial [NavigationState] with [ScreenState] and [OverlayState].
     */
    private fun createInitialNavigationState(): NavigationState<ScreenState, OverlayState> = NavigationState(
        screen = ScreenState.Onboarding(),
        backStack = CopyOnWriteStack<ScreenState>(),
        overlay = null
    )
}
