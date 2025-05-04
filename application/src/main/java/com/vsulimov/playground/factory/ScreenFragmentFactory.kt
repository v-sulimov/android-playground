package com.vsulimov.playground.factory

import androidx.fragment.app.Fragment
import com.vsulimov.navigation.factory.ScreenFragmentFactory
import com.vsulimov.navigation.state.ScreenState
import com.vsulimov.playground.factory.TypeIds.TYPE_ID_SCREEN_EVENTS_LIST
import com.vsulimov.playground.factory.TypeIds.TYPE_ID_SCREEN_ONBOARDING
import com.vsulimov.playground.fragment.screen.EventsListScreenFragment
import com.vsulimov.playground.fragment.screen.OnboardingScreenFragment
import com.vsulimov.playground.state.ApplicationScreenState

/**
 * A factory for creating screen [Fragment] instances based on the provided [ScreenState].
 *
 * This factory implements the [ScreenFragmentFactory] interface to handle the instantiation
 * of fragments that represent different screens within the application. It uses the
 * [ApplicationScreenState] to determine which specific fragment to create.
 *
 * It also provides a mechanism to retrieve a unique type identifier for a given fragment,
 * which can be useful for state restoration or other navigation-related tasks.
 */
class ScreenFragmentFactory : ScreenFragmentFactory {
    /**
     * Creates and returns a [Fragment] instance corresponding to the given [screen] state.
     *
     * This function performs a type check on the `screen` parameter, expecting it to be an
     * instance of [ApplicationScreenState]. Based on the specific subclass of `ApplicationScreenState`,
     * it instantiates and returns the appropriate screen fragment.
     *
     * @param screen The [ScreenState] that defines the screen to be created. This must be
     *               an instance of [ApplicationScreenState].
     * @return A [Fragment] instance representing the UI for the specified `screen`.
     *         - [OnboardingScreenFragment] if `screen` is [ApplicationScreenState.Onboarding].
     *         - [EventsListScreenFragment] if `screen` is [ApplicationScreenState.EventsList].
     */
    override fun createScreenFragment(screen: ScreenState): Fragment =
        when (screen as ApplicationScreenState) {
            is ApplicationScreenState.Onboarding -> OnboardingScreenFragment()
            is ApplicationScreenState.EventsList -> EventsListScreenFragment()
        }

    /**
     * Retrieves a unique string identifier for the state type associated with the given [fragment].
     *
     * This function maps a specific [Fragment] instance to a predefined type identifier string.
     * These identifiers are defined in the [TypeIds] object.
     *
     * @param fragment The [Fragment] for which to retrieve the state type identifier.
     * @return A [String] representing the unique type identifier for the screen state
     *         associated with the `fragment`.
     *         - [TYPE_ID_SCREEN_ONBOARDING] if `fragment` is an instance of [OnboardingScreenFragment].
     *         - [TYPE_ID_SCREEN_EVENTS_LIST] if `fragment` is an instance of [EventsListScreenFragment].
     * @throws IllegalArgumentException If the provided `fragment` type is not recognized by this factory.
     */
    override fun getStateTypeIdForScreen(fragment: Fragment): String =
        when (fragment) {
            is OnboardingScreenFragment -> TYPE_ID_SCREEN_ONBOARDING
            is EventsListScreenFragment -> TYPE_ID_SCREEN_EVENTS_LIST
            else -> throw IllegalArgumentException("Unknown fragment type: ${fragment::class.java.name}")
        }
}
