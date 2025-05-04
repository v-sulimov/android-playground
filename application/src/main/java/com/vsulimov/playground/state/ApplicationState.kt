package com.vsulimov.playground.state

import com.vsulimov.navigation.state.ApplicationState
import com.vsulimov.navigation.state.NavigationState
import com.vsulimov.navigation.state.OverlayState
import com.vsulimov.navigation.state.ScreenState
import com.vsulimov.stack.CopyOnWriteStack

/**
 * Represents the overall state of the application.
 *
 * This data class encapsulates the [NavigationState], providing access to the
 * navigation back stack, the current overlay state, and the current screen state.
 *
 * @property navigationState The current state of navigation within the application.
 *                           It holds information about the screen back stack,
 *                           the currently displayed screen, and any active overlay.
 *
 * @see NavigationState
 * @see ScreenState
 * @see OverlayState
 */
data class ApplicationState(
    val navigationState: NavigationState,
) : ApplicationState {
    /**
     * Retrieves the navigation back stack from the current [NavigationState].
     *
     * The back stack is a [CopyOnWriteStack] of [ScreenState] objects,
     * representing the history of screens the user has navigated through.
     *
     * @return A [CopyOnWriteStack] containing the [ScreenState] objects
     *         representing the navigation history.
     */
    override fun toNavigationBackStack(): CopyOnWriteStack<ScreenState> = navigationState.backStack

    /**
     * Retrieves the current [OverlayState] from the [NavigationState].
     *
     * An overlay is a UI element that is displayed on top of the current screen,
     * such as a dialog or a bottom sheet. This method returns `null` if no
     * overlay is currently active.
     *
     * @return The current [OverlayState] if an overlay is active, or `null` otherwise.
     */
    override fun toOverlayState(): OverlayState? = navigationState.overlay

    /**
     * Retrieves the current [ScreenState] from the [NavigationState].
     *
     * The screen state represents the screen that is currently visible to the user.
     *
     * @return The current [ScreenState].
     */
    override fun toScreenState(): ScreenState = navigationState.screen
}
