package com.vsulimov.playground.fragment.screen

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vsulimov.navigation.action.NavigationAction.ShowOverlay
import com.vsulimov.playground.R
import com.vsulimov.playground.adapter.EventsListAdapter
import com.vsulimov.playground.lens.toEventsListScreenStateEvents
import com.vsulimov.playground.state.ApplicationOverlayState.BottomSheet.CreateEvent
import com.vsulimov.playground.util.dispatch
import com.vsulimov.playground.util.subscribeToStateChanges

/**
 * A [Fragment] that displays a list of events in a RecyclerView and provides a floating action button
 * to create new events. This fragment is associated with the layout defined in [R.layout.screen_events_list].
 */
class EventsListScreenFragment : Fragment(R.layout.screen_events_list) {
    /**
     * Toolbar that displays the screen title.
     */
    private lateinit var toolbar: View

    /**
     * TextView displayed when there are no events to show.
     */
    private lateinit var noEventsPlaceholder: TextView

    /**
     * RecyclerView that displays the list of events.
     */
    private lateinit var eventsListRecyclerView: RecyclerView

    /**
     * Adapter for the RecyclerView to manage event data.
     */
    private lateinit var eventsListAdapter: EventsListAdapter

    /**
     * The [FloatingActionButton] used to trigger the creation of a new event.
     */
    private lateinit var createEventFloatingActionButton: FloatingActionButton

    /**
     * Called after the fragment's view has been created. Initializes UI components, sets up the RecyclerView,
     * applies window insets, configures click listeners, and subscribes to state changes.
     *
     * @param view The root [View] of the fragment's layout.
     * @param savedInstanceState If non-null, the fragment is being re-constructed from a previous saved state.
     */
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        findViewsById(view)
        initializeRecyclerView()
        applyWindowInsets(view)
        setOnClickListeners()
        subscribeToStateChanges(
            scope = lifecycleScope,
            lens = { it.toEventsListScreenStateEvents() },
            onStateChange = { events ->
                noEventsPlaceholder.isVisible = events == null || events.isEmpty()
                eventsListRecyclerView.isVisible = events != null && events.isNotEmpty()
                events?.let { eventsListAdapter.submitEvents(it) }
            },
        )
    }

    /**
     * Locates and initializes the UI components from the fragment's view by their IDs.
     *
     * @param view The fragment's inflated view.
     */
    private fun findViewsById(view: View) {
        toolbar = view.findViewById(R.id.events_list_toolbar)
        noEventsPlaceholder = view.findViewById(R.id.no_events_placeholder)
        eventsListRecyclerView = view.findViewById(R.id.events_recycler_view)
        createEventFloatingActionButton = view.findViewById(R.id.create_event_fab)
    }

    /**
     * Initializes the RecyclerView with a LinearLayoutManager and the [EventsListAdapter].
     */
    private fun initializeRecyclerView() {
        eventsListAdapter = EventsListAdapter()
        eventsListRecyclerView.layoutManager = LinearLayoutManager(context)
        eventsListRecyclerView.adapter = eventsListAdapter
    }

    /**
     * Applies window insets to the RecyclerView and [createEventFloatingActionButton] to ensure proper
     * padding and margins relative to system bars (e.g., status bar, navigation bar).
     *
     * @param view The root [View] of the fragment's layout.
     */
    private fun applyWindowInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(toolbar) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            v.updatePadding(
                left = bars.left,
                top = bars.top,
                right = bars.right,
            )
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(noEventsPlaceholder) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            v.updatePadding(
                left = bars.left,
                top = bars.top,
                right = bars.right,
                bottom = bars.bottom,
            )
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(eventsListRecyclerView) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            v.updatePadding(
                left = bars.left,
                right = bars.right,
                bottom = bars.bottom,
            )
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(createEventFloatingActionButton) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top + view.resources.getDimensionPixelSize(R.dimen.default_padding)
                bottomMargin = insets.bottom + view.resources.getDimensionPixelSize(R.dimen.default_padding)
                leftMargin = insets.left + view.resources.getDimensionPixelSize(R.dimen.default_padding)
                rightMargin = insets.right + view.resources.getDimensionPixelSize(R.dimen.default_padding)
            }
            WindowInsetsCompat.CONSUMED
        }
        view.requestApplyInsets()
    }

    /**
     * Sets up click listeners for interactive UI components.
     */
    private fun setOnClickListeners() {
        createEventFloatingActionButton.setOnClickListener {
            dispatch(ShowOverlay(CreateEvent()))
        }
    }
}
