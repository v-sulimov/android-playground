package com.vsulimov.playground.state

import com.vsulimov.navigation.state.OverlayState
import com.vsulimov.playground.factory.TypeIds.TYPE_ID_OVERLAY_CREATE_EVENT
import com.vsulimov.playground.factory.TypeIds.TYPE_ID_OVERLAY_DELETE_EVENT

/**
 * A sealed class representing the possible states of overlays in the application.
 *
 * This class defines the different types of overlay UI components that can be displayed, such as dialogs
 * or bottom sheets.
 *
 * @see Dialog
 * @see BottomSheet
 */
sealed class ApplicationOverlayState : OverlayState {
    /**
     * Represents a dialog overlay state.
     *
     * This sealed class defines the different types of dialogs that can be displayed as overlays.
     * Each subclass of [Dialog] represents a specific dialog type with its own data.
     *
     * @see DeleteEvent
     */
    sealed class Dialog : ApplicationOverlayState() {
        /**
         * A dialog state for confirming the deletion of an event.
         *
         * This data class represents a dialog overlay that prompts the user to confirm the deletion of an event
         * identified by a timestamp. It includes a unique type identifier for distinguishing this dialog type.
         *
         * @param typeId A unique identifier for this dialog type, defaults to [TYPE_ID_OVERLAY_DELETE_EVENT].
         * @param timestamp The timestamp of the event to be deleted, used to identify the specific event.
         */
        data class DeleteEvent(
            override val typeId: String = TYPE_ID_OVERLAY_DELETE_EVENT,
            val timestamp: Long,
        ) : Dialog()
    }

    /**
     * Represents the state of a bottom sheet overlay in the application.
     *
     * A [BottomSheet] is a type of [ApplicationOverlayState] used to manage the display of bottom sheet UI components.
     * This sealed class defines the possible states a bottom sheet can have, ensuring type safety and
     * enabling exhaustive handling of all bottom sheet states in when expressions.
     *
     * Bottom sheets are typically used to present temporary, modal content that slides up from the bottom
     * of the screen, such as forms, menus, or informational panels.
     *
     * @see CreateEvent
     */
    sealed class BottomSheet : ApplicationOverlayState() {
        /**
         * Represents the state for displaying a bottom sheet to create a new event.
         *
         * When this state is active, the application should show a bottom sheet containing a form or
         * interface for creating a new event. This is a singleton object, as it does not require additional
         * data to define the state, other than its unique type identifier. The `typeId` is used to
         * differentiate this bottom sheet from other potential bottom sheets.
         * @param typeId A unique identifier for this bottom sheet type, defaults to [TYPE_ID_OVERLAY_CREATE_EVENT].
         */
        data class CreateEvent(
            override val typeId: String = TYPE_ID_OVERLAY_CREATE_EVENT,
        ) : BottomSheet()
    }
}
