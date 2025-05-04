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
import com.vsulimov.playground.state.OverlayState
import com.vsulimov.playground.state.ScreenState
import com.vsulimov.playground.util.addMiddleware
import com.vsulimov.playground.util.dispatch
import com.vsulimov.playground.util.getStateFlow
import com.vsulimov.playground.util.removeMiddleware
import kotlinx.coroutines.flow.map

/**
 * The main entry point of the application, responsible for initializing and managing navigation.
 *
 * This activity sets up the primary UI container, initializes the navigation middleware, and configures
 * the navigation controller to handle screen and overlay transitions.
 *
 * @see NavigationController
 * @see NavigationMiddleware
 * @see ScreenState
 * @see OverlayState
 */
class MainActivity : AppCompatActivity() {

    /**
     * Middleware responsible for handling navigation events and coordinating with the activity lifecycle.
     */
    private lateinit var navigationMiddleware: NavigationMiddleware

    /**
     * Controller that manages navigation state, screen transitions, and overlay displays.
     */
    private lateinit var navigationController: NavigationController<ScreenState, OverlayState>

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNavigationMiddleware()
        addMiddleware(navigationMiddleware)
        createNavigationController()
        navigationController.init()
    }

    override fun onDestroy() {
        super.onDestroy()
        dispatch(OnDestroy(isFinishing))
        removeMiddleware(navigationMiddleware)
    }

    /**
     * Creates and configures the navigation middleware.
     *
     * Initializes the [navigationMiddleware] with a function to finish the activity when required,
     * enabling the middleware to handle navigation events that involve closing the activity.
     */
    private fun createNavigationMiddleware() {
        navigationMiddleware = NavigationMiddleware(finishActivityFunction = { finish() })
    }

    /**
     * Creates and configures the navigation controller.
     *
     * Initializes the [navigationController] with dependencies required for navigation, including
     * a dispatch function, navigation state flow, fragment factories for screens and overlays,
     * and the container ID. This sets up the controller to manage screen and overlay transitions
     * within the activity.
     */
    private fun createNavigationController() {
        val screenFactory = ScreenFragmentFactory()
        val overlayFactory = OverlayFragmentFactory()
        navigationController = NavigationController(
            dispatchFunction = { action -> dispatch(action) },
            navigationStateFlow = getStateFlow().map { it.toNavigationState() },
            activity = this,
            screenFragmentFactory = screenFactory,
            overlayFragmentFactory = overlayFactory,
            containerId = R.id.container
        )
    }
}
