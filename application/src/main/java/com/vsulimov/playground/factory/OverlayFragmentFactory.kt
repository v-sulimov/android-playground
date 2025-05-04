package com.vsulimov.playground.factory

import androidx.fragment.app.DialogFragment
import com.vsulimov.navigation.factory.OverlayFragmentFactory
import com.vsulimov.navigation.state.OverlayState
import com.vsulimov.playground.factory.TypeIds.TYPE_ID_OVERLAY_CREATE_EVENT
import com.vsulimov.playground.fragment.bottomsheet.CreateEventBottomSheetDialogFragment
import com.vsulimov.playground.fragment.dialog.DeleteEventDialogFragment
import com.vsulimov.playground.state.ApplicationOverlayState

/**
 * A factory class responsible for creating overlay fragments and determining their type IDs based on the provided [ApplicationOverlayState].
 *
 * This class implements the [OverlayFragmentFactory] interface to:
 * 1. Generate [DialogFragment] instances corresponding to specific overlay states.
 * 2. Provide a unique type ID string for a given [DialogFragment] instance.
 *
 * It is used within the navigation system to instantiate overlay UI components dynamically and to identify them.
 *
 * @see OverlayFragmentFactory
 * @see ApplicationOverlayState
 * @see DialogFragment
 */
class OverlayFragmentFactory : OverlayFragmentFactory {
    /**
     * Creates a [DialogFragment] for the specified [ApplicationOverlayState].
     *
     * This function takes an [OverlayState], casts it to [ApplicationOverlayState], and then,
     * based on the specific type of [ApplicationOverlayState], instantiates and returns the
     * corresponding [DialogFragment].
     *
     * @param overlay The [OverlayState] (expected to be an [ApplicationOverlayState]) defining the overlay to be created.
     * @return A [DialogFragment] instance corresponding to the provided [overlay] state.
     *         For example, if `overlay` is [ApplicationOverlayState.BottomSheet.CreateEvent],
     *         it returns an instance of [CreateEventBottomSheetDialogFragment].
     */
    override fun createOverlayFragment(overlay: OverlayState): DialogFragment =
        when (overlay as ApplicationOverlayState) {
            is ApplicationOverlayState.BottomSheet.CreateEvent -> CreateEventBottomSheetDialogFragment()
            is ApplicationOverlayState.Dialog.DeleteEvent -> DeleteEventDialogFragment()
        }

    /**
     * Retrieves a unique type ID string for the given [DialogFragment].
     *
     * This function inspects the type of the provided [dialogFragment] and returns a
     * predefined string constant that uniquely identifies that type of overlay. These type IDs
     * are defined in the [TypeIds] object.
     *
     * @param dialogFragment The [DialogFragment] instance for which the type ID is required.
     * @return A string representing the type ID of the [dialogFragment].
     *         For example, if `dialogFragment` is an instance of [CreateEventBottomSheetDialogFragment],
     *         it returns [TYPE_ID_OVERLAY_CREATE_EVENT].
     * @throws IllegalAccessException if the provided [dialogFragment] is of an unknown or unsupported type.
     */
    override fun getStateTypeIdForOverlay(dialogFragment: DialogFragment): String =
        when (dialogFragment) {
            is CreateEventBottomSheetDialogFragment -> TYPE_ID_OVERLAY_CREATE_EVENT
            is DeleteEventDialogFragment -> TypeIds.TYPE_ID_OVERLAY_DELETE_EVENT
            else -> throw IllegalAccessException("Unknown overlay type ${dialogFragment::class.simpleName}")
        }
}
