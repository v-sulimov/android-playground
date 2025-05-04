package com.vsulimov.playground.reducer

import com.vsulimov.navigation.action.NavigationAction
import com.vsulimov.navigation.reducer.NavigationReducer
import com.vsulimov.playground.action.ActivityLifecycleAction
import com.vsulimov.playground.action.EventsAction
import com.vsulimov.playground.lens.toNavigationState
import com.vsulimov.playground.state.ApplicationState
import com.vsulimov.redux.Action
import com.vsulimov.redux.Reducer

/**
 * A reducer responsible for handling actions that update the [ApplicationState].
 *
 * This class implements the [Reducer] interface to process actions and produce a new [ApplicationState].
 * It delegates navigation-related actions to a [NavigationReducer] to update the [NavigationState]
 * and activity lifecycle actions to [ActivityLifecycleReducer], while leaving other actions unchanged.
 *
 * @see Reducer
 * @see NavigationReducer
 * @see ActivityLifecycleReducer
 * @see ApplicationState
 * @see NavigationAction
 * @see ActivityLifecycleAction
 */
class RootReducer : Reducer<ApplicationState> {
    /**
     * A reducer for handling navigation-specific actions and updating the [NavigationState].
     */
    private val navigationReducer = NavigationReducer()

    /**
     * Processes an action to produce a new [ApplicationState].
     *
     * This method examines the provided [action] and updates the [state] accordingly. If the action is
     * a [NavigationAction], it delegates to the [navigationReducer] to update the [NavigationState].
     * If the action is an [ActivityLifecycleAction], it delegates to the [ActivityLifecycleReducer].
     * For all other actions, the current state is returned unchanged.
     *
     * @param action The [Action] to process.
     * @param state The current [ApplicationState] to update.
     * @return The new [ApplicationState] after applying the action.
     */
    override fun reduce(
        action: Action,
        state: ApplicationState,
    ): ApplicationState =
        when (action) {
            is ActivityLifecycleAction -> {
                ActivityLifecycleReducer.reduce(action, state)
            }

            is NavigationAction -> {
                val navigationState = state.toNavigationState()
                val reducedNavigationState = navigationReducer.reduce(action, navigationState)
                state.copy(navigationState = reducedNavigationState)
            }

            is EventsAction -> {
                val screenState = state.toScreenState()
                val reducedScreenState = EventsReducer.reduce(action, screenState)
                val reducedNavigationState = state.toNavigationState().copy(screen = reducedScreenState)
                state.copy(navigationState = reducedNavigationState)
            }

            else -> state
        }
}
