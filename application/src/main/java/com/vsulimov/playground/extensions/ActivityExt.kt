package com.vsulimov.playground.extensions

import android.app.Activity
import com.vsulimov.playground.application.PlaygroundApplication

fun Activity.getPlaygroundApplication() = application as PlaygroundApplication

fun Activity.getApplicationState() = getPlaygroundApplication().applicationState
