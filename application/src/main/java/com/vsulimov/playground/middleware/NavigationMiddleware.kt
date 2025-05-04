package com.vsulimov.playground.middleware

import android.util.Log
import com.vsulimov.navigation.action.NavigationAction
import com.vsulimov.playground.state.ApplicationState
import com.vsulimov.redux.Action
import com.vsulimov.redux.TypedMiddleware
import kotlinx.coroutines.CoroutineScope

/**
 * Middleware that processes [NavigationAction.GoBack] actions to handle back navigation logic.
 *
 * This class intercepts [NavigationAction.GoBack] actions and determines whether to finish the activity
 * or allow the action to proceed based on the current navigation state. It is part of the application's
 * middleware chain, ensuring proper handling of back navigation in the context of screens and overlays.
 *
 * @param finishActivityFunction A lambda function to finish the activity when back navigation requires it.
 * @see NavigationAction.GoBack
 * @see ApplicationState
 * @see TypedMiddleware
 */
class NavigationMiddleware(
    private val finishActivityFunction: () -> Unit,
) : TypedMiddleware<NavigationAction.GoBack, ApplicationState>(NavigationAction.GoBack::class.java) {
    /**
     * Processes a [NavigationAction.GoBack] action and decides the appropriate navigation behavior.
     *
     * This method checks the current [ApplicationState]'s navigation state. If no overlay is present and
     * the back stack is empty, it invokes the [finishActivityFunction] to close the activity.
     * Otherwise, no action is taken, allowing the middleware chain to continue.
     * The action is then passed to the next middleware in the chain.
     *
     * @param action The [NavigationAction.GoBack] action to process.
     * @param state The current [ApplicationState] containing the navigation state.
     * @param next A function to pass the action to the next middleware in the chain.
     * @param dispatch A function to dispatch new actions to the state management system.
     */
    override fun invokeTyped(
        action: NavigationAction.GoBack,
        state: ApplicationState,
        next: (Action) -> Unit,
        dispatch: (Action) -> Unit,
        scope: CoroutineScope,
    ) {
        if (state.toOverlayState() == null && state.toNavigationBackStack().isEmpty()) {
            Log.d(TAG, "Received GoBack action. Overlay is null and backStack is empty. Resolution: Finish activity.")
            finishActivityFunction.invoke()
        } else {
            Log.d(TAG, "Received GoBack action. Overlay is present or backStack is not empty. Resolution: Do nothing.")
        }
        next(action)
    }

    companion object {
        /**
         * Tag used for logging navigation-related events.
         */
        private const val TAG = "NavigationMiddleware"
    }
}
