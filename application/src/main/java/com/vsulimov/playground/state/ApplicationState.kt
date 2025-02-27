package com.vsulimov.playground.state

import kotlinx.coroutines.flow.MutableStateFlow

data class ApplicationState(
    val counter: MutableStateFlow<Int> = MutableStateFlow(0)
)
