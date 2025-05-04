package com.vsulimov.playground.fragment.screen

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.vsulimov.navigation.action.NavigationAction.NavigateTo
import com.vsulimov.playground.R
import com.vsulimov.playground.state.ApplicationScreenState.EventsList
import com.vsulimov.playground.util.dispatch

/**
 * A fragment that displays the onboarding screen for the application.
 *
 * This fragment is responsible for presenting the initial onboarding UI, including a title, description,
 * and a "Get Started" button.
 *
 * @see Fragment
 */
class OnboardingScreenFragment : Fragment(R.layout.screen_onboarding) {
    /**
     * TextView displaying the onboarding screen's title.
     */
    private lateinit var titleTextView: TextView

    /**
     * Button that triggers the "Get Started" action.
     */
    private lateinit var getStartedButton: Button

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        findViewsById(view)
        setOnClickListeners()
        applyWindowInsets()
    }

    /**
     * Locates and initializes the UI components from the fragment's view.
     *
     * @param view The fragment's inflated view.
     */
    private fun findViewsById(view: View) {
        titleTextView = view.findViewById(R.id.onboarding_title)
        getStartedButton = view.findViewById(R.id.onboarding_get_started_button)
    }

    /**
     * Sets up click listeners for interactive UI components.
     */
    private fun setOnClickListeners() {
        getStartedButton.setOnClickListener {
            dispatch(NavigateTo(EventsList(), clearBackStack = true))
        }
    }

    /**
     * Applies window insets to UI components to handle system bar overlaps.
     *
     * This method sets listeners for the title and "Get Started" button to adjust their margins based on
     * the system bar insets (e.g., status and navigation bars). This ensures the views are properly
     * positioned and not obscured by system UI elements.
     */
    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(titleTextView) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            v.updatePadding(
                left = bars.left,
                top = bars.top,
                right = bars.right,
                bottom = bars.bottom,
            )
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(getStartedButton) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
                bottomMargin = insets.bottom
                leftMargin = insets.left
                rightMargin = insets.right
            }
            windowInsets
        }
    }
}
