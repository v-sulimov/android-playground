package com.vsulimov.playground.factory

import com.vsulimov.navigation.state.NavigationState
import com.vsulimov.playground.state.ApplicationOverlayState
import com.vsulimov.playground.state.ApplicationScreenState
import com.vsulimov.playground.state.ApplicationState
import com.vsulimov.stack.CopyOnWriteStack

/**
 * Factory for creating initial application states.
 *
 * This object provides methods to create the initial [ApplicationState] and
 * [NavigationState] for the application.
 */
object ApplicationStateFactory {
    /**
     * Creates the initial [ApplicationState].
     *
     * The initial application state is composed of an initial [NavigationState].
     *
     * @return The newly created initial [ApplicationState].
     */
    fun createInitialApplicationState(): ApplicationState =
        ApplicationState(
            navigationState = createInitialNavigationState(),
        )

    /**
     * Creates the initial [NavigationState].
     *
     * The initial navigation state is configured with:
     * - The [ApplicationScreenState.Onboarding] screen as the starting screen.
     * - An empty [CopyOnWriteStack] for the back stack.
     * - No initial [ApplicationOverlayState] (overlay is `null`).
     *
     * @return The newly created initial [NavigationState] for [ApplicationScreenState] and [ApplicationOverlayState].
     */
    private fun createInitialNavigationState(): NavigationState =
        NavigationState(
            screen = ApplicationScreenState.Onboarding(),
            backStack = CopyOnWriteStack(),
            overlay = null,
        )
}
