package com.vsulimov.playground.factory

import com.vsulimov.navigation.state.NavigationState
import com.vsulimov.playground.state.ApplicationState
import com.vsulimov.playground.state.OverlayState
import com.vsulimov.playground.state.ScreenState
import com.vsulimov.stack.CopyOnWriteStack

/**
 * Factory object for creating [ApplicationState] instances for testing purposes.
 * Provides a convenient way to construct [ApplicationState] with customizable navigation state.
 */
object ApplicationStateTestFactory {

    /**
     * Creates an [ApplicationState] with the specified navigation state.
     *
     * @param screenState The current screen state, defaults to [ScreenState.Onboarding].
     * @param backStack The navigation back stack, defaults to an empty [CopyOnWriteStack].
     * @param overlayState The overlay state, defaults to [OverlayState.Dialog]. Can be null for no overlay.
     * @return An [ApplicationState] instance with the configured [NavigationState].
     */
    fun createApplicationState(
        screenState: ScreenState = ScreenState.Onboarding(),
        backStack: CopyOnWriteStack<ScreenState> = CopyOnWriteStack(),
        overlayState: OverlayState? = OverlayState.Dialog.DeleteEvent(timestamp = 0L)
    ) = ApplicationState(
        navigationState = NavigationState<ScreenState, OverlayState>(
            screen = screenState,
            backStack = backStack,
            overlay = overlayState
        )
    )
}
