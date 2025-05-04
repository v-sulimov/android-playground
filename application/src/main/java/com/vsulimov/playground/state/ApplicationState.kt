package com.vsulimov.playground.state

import com.vsulimov.navigation.state.NavigationState

/**
 * Represents the overall state of the application.
 *
 * @property navigationState The state of the navigation, including the current screen and any active overlays.
 *
 * @see NavigationState
 * @see ScreenState
 * @see OverlayState
 */
data class ApplicationState(
    val navigationState: NavigationState<ScreenState, OverlayState>
)
