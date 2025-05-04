package com.vsulimov.playground.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.vsulimov.navigation.NavigationController
import com.vsulimov.playground.R
import com.vsulimov.playground.action.ActivityLifecycleAction.OnDestroy
import com.vsulimov.playground.factory.OverlayFragmentFactory
import com.vsulimov.playground.factory.ScreenFragmentFactory
import com.vsulimov.playground.lens.toNavigationState
import com.vsulimov.playground.middleware.NavigationMiddleware
import com.vsulimov.playground.state.ApplicationOverlayState
import com.vsulimov.playground.state.ApplicationScreenState
import com.vsulimov.playground.util.addMiddleware
import com.vsulimov.playground.util.dispatch
import com.vsulimov.playground.util.getStateFlow
import com.vsulimov.playground.util.removeMiddleware
import kotlinx.coroutines.flow.map

/**
 * The main activity of the application, serving as the entry point and container for UI.
 *
 * This activity is responsible for:
 * - Setting up the user interface, including enabling edge-to-edge display.
 * - Initializing and managing the [NavigationController] for screen and overlay transitions.
 * - Integrating [NavigationMiddleware] to handle navigation logic and lifecycle events.
 *
 * It uses a [ScreenFragmentFactory] and [OverlayFragmentFactory] to create fragment instances
 * for different navigation destinations. The navigation state is derived from the application's
 * overall state and observed via a [kotlinx.coroutines.flow.Flow].
 *
 * @see NavigationController Manages the navigation flow within the application.
 * @see NavigationMiddleware Handles navigation actions and lifecycle events.
 * @see ScreenFragmentFactory Creates instances of screen fragments.
 * @see OverlayFragmentFactory Creates instances of overlay fragments.
 * @see ApplicationScreenState Represents the state of the current screen.
 * @see ApplicationOverlayState Represents the state of any active overlays.
 */
class MainActivity : AppCompatActivity() {
    /**
     * Middleware responsible for handling navigation actions and integrating with the activity's
     * lifecycle. It can trigger activity-level actions like finishing the activity.
     */
    private lateinit var navigationMiddleware: NavigationMiddleware

    /**
     * Controller that manages the navigation stack, handles fragment transactions for screens
     * and overlays, and observes changes in navigation state.
     */
    private lateinit var navigationController: NavigationController

    /**
     * Called when the activity is first created.
     *
     * This method performs the following actions:
     * - Enables edge-to-edge display for a modern look and feel.
     * - Sets the content view for the activity.
     * - Creates and registers the [NavigationMiddleware].
     * - Creates and initializes the [NavigationController], linking it to the UI container
     *   and providing necessary factories and state information.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     * shut down, this Bundle contains the data it most recently supplied in [onSaveInstanceState].
     * Otherwise, it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNavigationMiddleware()
        addMiddleware(navigationMiddleware)
        createNavigationController()
        navigationController.init()
    }

    /**
     * Called when the activity is being destroyed.
     *
     * This method performs cleanup by:
     * - Dispatching an [OnDestroy] action, indicating whether the activity is finishing.
     * - Removing the [NavigationMiddleware] to prevent memory leaks and further processing.
     */
    override fun onDestroy() {
        super.onDestroy()
        dispatch(OnDestroy(isFinishing))
        removeMiddleware(navigationMiddleware)
    }

    /**
     * Initializes the [navigationMiddleware].
     *
     * This middleware is provided with a function to finish the current activity,
     * allowing it to control activity-level navigation events (e.g., closing the app
     * or a specific flow).
     */
    private fun createNavigationMiddleware() {
        navigationMiddleware = NavigationMiddleware(finishActivityFunction = { finish() })
    }

    /**
     * Initializes and configures the [navigationController].
     *
     * This method sets up the [NavigationController] with:
     * - A dispatch function to send actions to the application's state management system.
     * - A [kotlinx.coroutines.flow.Flow] of the navigation state, derived from the application's global state.
     * - The current [AppCompatActivity] instance as the host.
     * - Factories for creating screen ([ScreenFragmentFactory]) and overlay ([OverlayFragmentFactory]) fragments.
     * - The ID of the container ([R.id.container]) where fragments will be displayed.
     */
    private fun createNavigationController() {
        val screenFactory = ScreenFragmentFactory()
        val overlayFactory = OverlayFragmentFactory()
        navigationController =
            NavigationController(
                dispatchFunction = { action -> dispatch(action) },
                navigationStateFlow = getStateFlow().map { it.toNavigationState() },
                activity = this,
                screenFragmentFactory = screenFactory,
                overlayFragmentFactory = overlayFactory,
                containerId = R.id.container,
            )
    }
}
