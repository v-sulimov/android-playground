package com.vsulimov.playground.factory

import androidx.fragment.app.Fragment
import com.vsulimov.navigation.factory.ScreenFragmentFactory
import com.vsulimov.playground.factory.TypeIds.TYPE_ID_SCREEN_EVENTS_LIST
import com.vsulimov.playground.factory.TypeIds.TYPE_ID_SCREEN_ONBOARDING
import com.vsulimov.playground.fragment.screen.EventsListScreenFragment
import com.vsulimov.playground.fragment.screen.OnboardingScreenFragment
import com.vsulimov.playground.state.ScreenState

/**
 * A factory class responsible for creating screen fragments based on the provided [ScreenState].
 *
 * This class implements the [ScreenFragmentFactory] interface to instantiate [Fragment] instances
 * corresponding to specific screen states. It is used within the navigation system to create screen
 * UI components dynamically.
 *
 * @see ScreenFragmentFactory
 * @see ScreenState
 * @see Fragment
 */
class ScreenFragmentFactory : ScreenFragmentFactory<ScreenState> {

    /**
     * Creates a [Fragment] for the specified [ScreenState].
     *
     * This method maps the provided [screen] state to an appropriate [Fragment] instance.
     *
     * @param screen The [ScreenState] defining the screen to be created.
     * @return A [Fragment] corresponding to the specified screen state.
     */
    override fun createScreenFragment(screen: ScreenState): Fragment = when (screen) {
        is ScreenState.Onboarding -> OnboardingScreenFragment()
        is ScreenState.EventsList -> EventsListScreenFragment()
    }

    /**
     * Retrieves the state type identifier for a given [Fragment].
     *
     * This method maps a [Fragment] instance to a unique string identifier representing its corresponding
     * [ScreenState]. The identifier is used to associate fragments with their respective screen states
     * in the navigation system.
     *
     * @param fragment The [Fragment] for which to retrieve the state type identifier.
     * @return A [String] representing the state type identifier for the fragment.
     * @throws IllegalArgumentException If the provided fragment does not correspond to a known [ScreenState].
     */
    override fun getStateTypeIdForScreen(fragment: Fragment): String {
        return when (fragment) {
            is OnboardingScreenFragment -> TYPE_ID_SCREEN_ONBOARDING
            is EventsListScreenFragment -> TYPE_ID_SCREEN_EVENTS_LIST
            else -> throw IllegalArgumentException("Unknown fragment type: ${fragment::class.java.name}")
        }
    }
}
