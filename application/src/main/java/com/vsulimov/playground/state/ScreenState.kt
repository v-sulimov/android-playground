package com.vsulimov.playground.state

import com.vsulimov.navigation.state.NavigationComponent
import com.vsulimov.playground.factory.TypeIds.TYPE_ID_SCREEN_EVENTS_LIST
import com.vsulimov.playground.factory.TypeIds.TYPE_ID_SCREEN_ONBOARDING
import com.vsulimov.playground.state.event.EventState

/**
 * A sealed class representing the possible states of screens in the application.
 *
 * This class defines the different types of screens that can be displayed in the application. Each
 * subclass represents a specific screen state.
 *
 * @see Onboarding
 */
sealed class ScreenState : NavigationComponent {

    /**
     * Represents the onboarding screen state.
     *
     * This object is used to indicate that the onboarding screen should be displayed in the application.
     */
    data class Onboarding(override val typeId: String = TYPE_ID_SCREEN_ONBOARDING) : ScreenState()

    /**
     * Represents the state of the events list screen.
     *
     * This data class holds the state for displaying the events list screen in the application,
     * containing a list of events to be shown.
     *
     * @property events A list of [EventState] objects representing the events to be displayed.
     */
    data class EventsList(
        override val typeId: String = TYPE_ID_SCREEN_EVENTS_LIST,
        val events: List<EventState> = emptyList()
    ) : ScreenState()
}
