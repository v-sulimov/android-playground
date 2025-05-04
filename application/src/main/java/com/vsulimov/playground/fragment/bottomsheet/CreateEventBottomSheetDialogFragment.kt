package com.vsulimov.playground.fragment.bottomsheet

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.vsulimov.playground.R
import com.vsulimov.playground.action.EventsAction.SaveEvent
import com.vsulimov.playground.util.dispatch

/**
 * A bottom sheet dialog fragment for creating a new event.
 *
 * This fragment provides a UI for users to input event details and save them via a [SaveEvent] action.
 * It controls dismissal behavior by setting [shouldDispatchOnDismiss] to false when saving an event,
 * preventing automatic dispatching of [DismissOverlay] to allow the middleware to handle dismissal.
 * The close button triggers a direct [dismiss] call, which may dispatch [DismissOverlay] based on
 * [shouldDispatchOnDismiss].
 */
class CreateEventBottomSheetDialogFragment : AbstractBottomSheetDialogFragment(R.layout.overlay_create_event) {
    private lateinit var closeButton: ImageButton
    private lateinit var eventDetailsEditText: EditText
    private lateinit var saveButton: Button

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        findViewsById(view)
        setOnClickListeners()
    }

    override fun onStart() {
        super.onStart()
        eventDetailsEditText.requestFocus()
    }

    /**
     * Locates and initializes the UI components from the fragment's view.
     *
     * @param view The fragment's inflated view.
     */
    private fun findViewsById(view: View) {
        closeButton = view.findViewById(R.id.event_close_button)
        eventDetailsEditText = view.findViewById(R.id.event_details_edit_text)
        saveButton = view.findViewById(R.id.event_save_button)
    }

    /**
     * Sets up click listeners for interactive UI components.
     */
    private fun setOnClickListeners() {
        closeButton.setOnClickListener { dismiss() }
        saveButton.setOnClickListener {
            shouldDispatchOnDismiss = false
            dispatch(SaveEvent(eventDetails = eventDetailsEditText.text.toString()))
        }
    }
}
