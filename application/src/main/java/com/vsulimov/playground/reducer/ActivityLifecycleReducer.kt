package com.vsulimov.playground.reducer

import com.vsulimov.playground.action.ActivityLifecycleAction
import com.vsulimov.playground.factory.ApplicationStateFactory
import com.vsulimov.playground.state.ApplicationState
import com.vsulimov.redux.TypedReducer

/**
 * A reducer responsible for handling [ActivityLifecycleAction] and updating the [ApplicationState].
 * This object implements the [TypedReducer] interface, specifically processing actions of type [ActivityLifecycleAction].
 */
object ActivityLifecycleReducer :
    TypedReducer<ActivityLifecycleAction, ApplicationState>(ActivityLifecycleAction::class.java) {
    /**
     * Reduces the given [action] and current [state] to produce a new [ApplicationState].
     *
     * @param action The [ActivityLifecycleAction] to process.
     * @param state The current [ApplicationState] before reduction.
     * @return The updated [ApplicationState] after applying the action.
     */
    override fun reduceTyped(
        action: ActivityLifecycleAction,
        state: ApplicationState,
    ): ApplicationState =
        when (action) {
            is ActivityLifecycleAction.OnDestroy -> {
                if (action.isFinishing) {
                    ApplicationStateFactory.createInitialApplicationState()
                } else {
                    state
                }
            }
        }
}
