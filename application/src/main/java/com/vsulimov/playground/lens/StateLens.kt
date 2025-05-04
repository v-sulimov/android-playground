package com.vsulimov.playground.lens

import com.vsulimov.playground.state.ApplicationOverlayState
import com.vsulimov.playground.state.ApplicationScreenState
import com.vsulimov.playground.state.ApplicationState

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
 * Converts the [ApplicationState] to the events list screen state, if applicable.
 *
 * This function checks if the current screen state is [ApplicationScreenState.EventsList] and returns it if true.
 * If the screen state is not [ApplicationScreenState.EventsList], it returns null.
 *
 * @return The [ApplicationScreenState.EventsList] if the current screen state is an events list, or null otherwise.
 */
fun ApplicationState.toEventsListScreenState() = toScreenState() as? ApplicationScreenState.EventsList

/**
 * Retrieves the list of events from the events list screen state, if applicable.
 *
 * This function returns the list of events from the [ApplicationScreenState.EventsList] if the current screen state
 * is an events list. If the screen state is not [ApplicationScreenState.EventsList], it returns null.
 *
 * @return The list of events from the [ApplicationScreenState.EventsList], or null if the current screen state is not an events list.
 */
fun ApplicationState.toEventsListScreenStateEvents() = toEventsListScreenState()?.events

/**
 * Converts the [ApplicationState] to the delete event overlay state.
 *
 * This function assumes the current overlay state is [ApplicationOverlayState.Dialog.DeleteEvent] and casts it accordingly.
 * If the overlay state is not [ApplicationOverlayState.Dialog.DeleteEvent], this will throw a [ClassCastException].
 *
 * @return The [ApplicationOverlayState.Dialog.DeleteEvent] overlay state from the navigation state.
 * @throws ClassCastException if the current overlay state is not [ApplicationOverlayState.Dialog.DeleteEvent].
 */
fun ApplicationState.toDeleteEventOverlay() = toOverlayState() as ApplicationOverlayState.Dialog.DeleteEvent
