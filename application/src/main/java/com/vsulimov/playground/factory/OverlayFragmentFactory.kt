package com.vsulimov.playground.factory

import androidx.fragment.app.DialogFragment
import com.vsulimov.navigation.factory.OverlayFragmentFactory
import com.vsulimov.playground.factory.TypeIds.TYPE_ID_OVERLAY_CREATE_EVENT
import com.vsulimov.playground.fragment.bottomsheet.CreateEventBottomSheetDialogFragment
import com.vsulimov.playground.fragment.dialog.DeleteEventDialogFragment
import com.vsulimov.playground.state.OverlayState

/**
 * A factory class responsible for creating overlay fragments based on the provided [OverlayState].
 *
 * This class implements the [OverlayFragmentFactory] interface to generate [DialogFragment] instances
 * corresponding to specific overlay states. It is used within the navigation system to instantiate
 * overlay UI components dynamically.
 *
 * @see OverlayFragmentFactory
 * @see OverlayState
 * @see DialogFragment
 */
class OverlayFragmentFactory : OverlayFragmentFactory<OverlayState> {

    /**
     * Creates a [DialogFragment] for the specified [OverlayState].
     *
     * @param overlay The [OverlayState] defining the overlay to be created.
     * @return A [DialogFragment] corresponding to the specified overlay state.
     */
    override fun createOverlayFragment(overlay: OverlayState): DialogFragment = when (overlay) {
        is OverlayState.BottomSheet.CreateEvent -> CreateEventBottomSheetDialogFragment()
        is OverlayState.Dialog.DeleteEvent -> DeleteEventDialogFragment()
    }

    override fun getStateTypeIdForOverlay(dialogFragment: DialogFragment): String {
        return when (dialogFragment) {
            is CreateEventBottomSheetDialogFragment -> TYPE_ID_OVERLAY_CREATE_EVENT
            is DeleteEventDialogFragment -> TypeIds.TYPE_ID_OVERLAY_DELETE_EVENT
            else -> throw IllegalAccessException("Unknown overlay type ${dialogFragment::class.simpleName}")
        }
    }
}
