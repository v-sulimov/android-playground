package com.vsulimov.playground.factory

/**
 * A utility object that defines type identifiers for overlay states.
 * These identifiers are used to distinguish different types of overlays in the application.
 */
object TypeIds {
    /**
     * The type identifier for the onboarding screen.
     */
    const val TYPE_ID_SCREEN_ONBOARDING = "Onboarding"

    /**
     * The type identifier for the events list screen.
     */
    const val TYPE_ID_SCREEN_EVENTS_LIST = "Events List"

    /**
     * The type identifier for an overlay that represents the "Create Event" dialog.
     */
    const val TYPE_ID_OVERLAY_CREATE_EVENT = "Create Event"

    /**
     * The type identifier for an overlay that represents the "Delete Event" dialog.
     */
    const val TYPE_ID_OVERLAY_DELETE_EVENT = "Delete Event"
}
