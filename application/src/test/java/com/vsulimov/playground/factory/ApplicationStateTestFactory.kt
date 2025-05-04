package com.vsulimov.playground.factory

import com.vsulimov.navigation.state.NavigationState
import com.vsulimov.navigation.state.ScreenState
import com.vsulimov.playground.state.ApplicationOverlayState
import com.vsulimov.playground.state.ApplicationScreenState
import com.vsulimov.playground.state.ApplicationState
import com.vsulimov.stack.CopyOnWriteStack

/**
 * Factory object for creating [ApplicationState] instances for testing purposes.
 * Provides a convenient way to construct [ApplicationState] with customizable navigation state.
 */
object ApplicationStateTestFactory {
    /**
     * Creates an [ApplicationState] with the specified navigation state.
     *
     * @param applicationScreenState The current screen state, defaults to [ApplicationScreenState.Onboarding].
     * @param backStack The navigation back stack, defaults to an empty [CopyOnWriteStack].
     * @param overlayState The overlay state, defaults to [ApplicationOverlayState.Dialog]. Can be null for no overlay.
     * @return An [ApplicationState] instance with the configured [NavigationState].
     */
    fun createApplicationState(
        applicationScreenState: ApplicationScreenState = ApplicationScreenState.Onboarding(),
        backStack: CopyOnWriteStack<ScreenState> = CopyOnWriteStack(),
        overlayState: ApplicationOverlayState? = ApplicationOverlayState.Dialog.DeleteEvent(timestamp = 0L),
    ) = ApplicationState(
        navigationState =
            NavigationState(
                screen = applicationScreenState,
                backStack = backStack,
                overlay = overlayState,
            ),
    )
}
