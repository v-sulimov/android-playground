package com.vsulimov.playground.state.event

/**
 * Represents the state of an event with its details and timestamp.
 *
 * @property details A string describing the event.
 * @property timestamp The time the event occurred, represented as a Unix timestamp in milliseconds.
 */
data class EventState(
    val details: String,
    val timestamp: Long,
    val formattedTimestamp: String,
)
