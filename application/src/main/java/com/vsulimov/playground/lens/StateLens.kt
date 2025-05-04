package com.vsulimov.playground.lens

import com.vsulimov.playground.state.ApplicationState
import com.vsulimov.playground.state.OverlayState
import com.vsulimov.playground.state.ScreenState

/**
 * Converts the [ApplicationState] to its navigation state.
 *
 * This function extracts the navigation state, which contains information about the current screen,
 * overlay, and back stack, from the [ApplicationState].
 *
 * @return The navigation state contained within the [ApplicationState].
 */
fun ApplicationState.toNavigationState() = navigationState

/**
 * Converts the [ApplicationState] to its current screen state.
 *
 * This function retrieves the current screen state (e.g., [ScreenState.EventsList], [ScreenState.Onboarding])
 * from the navigation state of the [ApplicationState].
 *
 * @return The screen state from the navigation state of the [ApplicationState].
 */
fun ApplicationState.toScreenState() = navigationState.screen

/**
 * Converts the [ApplicationState] to its current overlay state.
 *
 * This function extracts the current overlay state (e.g., [OverlayState.Dialog]) from the navigation state
 * of the [ApplicationState]. The overlay state represents any active dialogs or overlays displayed on the screen.
 *
 * @return The overlay state from the navigation state of the [ApplicationState].
 */
fun ApplicationState.toOverlayState() = navigationState.overlay

/**
 * Retrieves the navigation back stack from the [ApplicationState].
 *
 * This function returns the list of screens in the navigation back stack, which represents the history
 * of screens the user has navigated through.
 *
 * @return The list of screens in the navigation back stack.
 */
fun ApplicationState.toNavigationBackStack() = navigationState.backStack

/**
 * Converts the [ApplicationState] to the events list screen state, if applicable.
 *
 * This function checks if the current screen state is [ScreenState.EventsList] and returns it if true.
 * If the screen state is not [ScreenState.EventsList], it returns null.
 *
 * @return The [ScreenState.EventsList] if the current screen state is an events list, or null otherwise.
 */
fun ApplicationState.toEventsListScreenState() = toScreenState() as? ScreenState.EventsList

/**
 * Retrieves the list of events from the events list screen state, if applicable.
 *
 * This function returns the list of events from the [ScreenState.EventsList] if the current screen state
 * is an events list. If the screen state is not [ScreenState.EventsList], it returns null.
 *
 * @return The list of events from the [ScreenState.EventsList], or null if the current screen state is not an events list.
 */
fun ApplicationState.toEventsListScreenStateEvents() = toEventsListScreenState()?.events

/**
 * Converts the [ApplicationState] to the delete event overlay state.
 *
 * This function assumes the current overlay state is [OverlayState.Dialog.DeleteEvent] and casts it accordingly.
 * If the overlay state is not [OverlayState.Dialog.DeleteEvent], this will throw a [ClassCastException].
 *
 * @return The [OverlayState.Dialog.DeleteEvent] overlay state from the navigation state.
 * @throws ClassCastException if the current overlay state is not [OverlayState.Dialog.DeleteEvent].
 */
fun ApplicationState.toDeleteEventOverlay() = toOverlayState() as OverlayState.Dialog.DeleteEvent
