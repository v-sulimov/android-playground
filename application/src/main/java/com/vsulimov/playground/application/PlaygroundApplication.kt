package com.vsulimov.playground.application

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.vsulimov.playground.state.ApplicationState

class PlaygroundApplication : Application() {

    val applicationState = ApplicationState()

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
