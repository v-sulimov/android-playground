package com.vsulimov.playground.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.vsulimov.playground.R
import com.vsulimov.playground.action.EventsAction
import com.vsulimov.playground.lens.toDeleteEventOverlay
import com.vsulimov.playground.util.dispatch
import com.vsulimov.playground.util.getStateFlow

/**
 * A dialog fragment for confirming the deletion of an event.
 *
 * This class extends [AbstractDialogFragment] to display a dialog that prompts the user to confirm
 * the deletion of an event. The dialog includes a title, message, and two buttons: one to confirm
 * the deletion and another to cancel the action. Upon confirming, it dispatches a [EventsAction.DeleteEvent]
 * action with the event's timestamp.
 */
class DeleteEventDialogFragment : AbstractDialogFragment() {
    /**
     * Creates and configures the dialog for event deletion.
     *
     * This method sets up an [AlertDialog] with a title, message, and positive and negative buttons.
     * The positive button triggers the deletion of the event by dispatching an [EventsAction.DeleteEvent]
     * action with the event's timestamp. The negative button dismisses the dialog.
     *
     * @param savedInstanceState The saved instance state bundle, if available.
     * @return The configured [Dialog] instance.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = requireActivity()
        val builder = AlertDialog.Builder(activity)
        with(builder) {
            setTitle(R.string.overlay_delete_event_title)
            setMessage(R.string.overlay_delete_event_message)
            setPositiveButton(R.string.overlay_delete_event_action_delete) { dialog, id ->
                val eventTimestamp = getStateFlow().value.toDeleteEventOverlay().timestamp
                dispatch(EventsAction.DeleteEvent(eventTimestamp))
            }
            setNegativeButton(android.R.string.cancel) { dialog, id ->
                dismiss()
            }
        }
        return builder.create()
    }
}
