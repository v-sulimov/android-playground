package com.vsulimov.playground.state

import com.vsulimov.navigation.state.ScreenState
import com.vsulimov.playground.factory.TypeIds.TYPE_ID_SCREEN_EVENTS_LIST
import com.vsulimov.playground.factory.TypeIds.TYPE_ID_SCREEN_ONBOARDING
import com.vsulimov.playground.state.event.EventState

/**
 * Represents the possible states of screens in the application.
 *
 * This sealed class defines the different types of screens that can be displayed.
 * Each subclass represents a specific screen state and its associated data.
 * @see Onboarding
 * @see EventsList
 */
sealed class ApplicationScreenState : ScreenState {
    /**
     * Represents the onboarding screen state.
     *
     * This data class indicates that the onboarding screen should be displayed.
     * @property typeId The unique identifier for this screen state, defaulting to [TYPE_ID_SCREEN_ONBOARDING].
     */
    data class Onboarding(
        override val typeId: String = TYPE_ID_SCREEN_ONBOARDING,
    ) : ApplicationScreenState()

    /**
     * Represents the state of the events list screen.
     *
     * This data class holds the necessary information for displaying the events list screen,
     * including a list of events.
     * @property typeId The unique identifier for this screen state, defaulting to [TYPE_ID_SCREEN_EVENTS_LIST].
     * @property events A list of [EventState] objects representing the events to be displayed.
     */
    data class EventsList(
        override val typeId: String = TYPE_ID_SCREEN_EVENTS_LIST,
        val events: List<EventState> = emptyList(),
    ) : ApplicationScreenState()
}
