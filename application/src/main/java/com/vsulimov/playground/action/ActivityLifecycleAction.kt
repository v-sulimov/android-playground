package com.vsulimov.playground.action

import com.vsulimov.redux.Action

/**
 * A sealed class representing actions related to the lifecycle of an Android Activity.
 * This class serves as a base for specific lifecycle actions and extends the [Action] interface.
 */
sealed class ActivityLifecycleAction : Action {
    /**
     * Represents the `onDestroy` lifecycle event of an Android Activity.
     *
     * @property isFinishing Indicates whether the Activity is finishing when destroyed.
     */
    data class OnDestroy(
        val isFinishing: Boolean,
    ) : ActivityLifecycleAction()
}
