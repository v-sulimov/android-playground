package com.vsulimov.playground.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vsulimov.navigation.action.NavigationAction.ShowOverlay
import com.vsulimov.playground.R
import com.vsulimov.playground.state.ApplicationOverlayState.Dialog.DeleteEvent
import com.vsulimov.playground.state.event.EventState
import com.vsulimov.playground.util.dispatch

/**
 * An adapter for displaying a list of events in a RecyclerView, utilizing DiffUtil for efficient updates.
 */
class EventsListAdapter : RecyclerView.Adapter<EventsListAdapter.ViewHolder>() {
    /**
     * DiffUtil callback to compute differences between old and new event lists for efficient updates.
     */
    private val diffUtil =
        object : DiffUtil.ItemCallback<EventState>() {
            /**
             * Checks if two items represent the same event based on their timestamp.
             *
             * @param oldItem The old event item.
             * @param newItem The new event item.
             * @return True if the items have the same timestamp, false otherwise.
             */
            override fun areItemsTheSame(
                oldItem: EventState,
                newItem: EventState,
            ): Boolean = oldItem.timestamp == newItem.timestamp

            /**
             * Checks if two items have identical content.
             *
             * @param oldItem The old event item.
             * @param newItem The new event item.
             * @return True if the items are equal, false otherwise.
             */
            override fun areContentsTheSame(
                oldItem: EventState,
                newItem: EventState,
            ): Boolean = oldItem == newItem
        }

    /**
     * Manages the list of events with asynchronous diffing for smooth UI updates.
     */
    private val asyncListDiffer = AsyncListDiffer<EventState>(this, diffUtil)

    /**
     * A ViewHolder that holds references to the views for each event item in the RecyclerView.
     *
     * @param view The view representing a single event item.
     */
    class ViewHolder(
        view: View,
    ) : RecyclerView.ViewHolder(view) {
        /**
         * TextView displaying the event details.
         */
        val eventText: TextView = view.findViewById(R.id.event_text)

        /**
         * TextView displaying the event timestamp.
         */
        val eventTime: TextView = view.findViewById(R.id.event_time)
    }

    /**
     * Creates a new ViewHolder by inflating the event item layout.
     *
     * @param viewGroup The parent ViewGroup into which the new view will be added.
     * @param viewType The type of the new view.
     * @return A new [ViewHolder] instance.
     */
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int,
    ): ViewHolder = ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_event, viewGroup, false))

    /**
     * Binds the event data at the specified position to the ViewHolder.
     *
     * @param viewHolder The [ViewHolder] to bind the data to.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        position: Int,
    ) {
        val event = asyncListDiffer.currentList[position]
        viewHolder.itemView.setOnLongClickListener {
            dispatch(ShowOverlay(DeleteEvent(timestamp = event.timestamp)))
            true
        }
        viewHolder.eventText.text = event.details
        viewHolder.eventTime.text = event.formattedTimestamp
    }

    /**
     * Returns the total number of items in the current event list.
     *
     * @return The size of the event list.
     */
    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    /**
     * Submits a new list of events to the adapter, triggering a diff calculation for efficient updates.
     *
     * @param events The new list of [EventState] objects to display.
     */
    fun submitEvents(events: List<EventState>) {
        asyncListDiffer.submitList(events)
    }
}
