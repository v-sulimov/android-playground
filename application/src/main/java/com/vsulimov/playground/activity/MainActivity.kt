package com.vsulimov.playground.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vsulimov.playground.R
import com.vsulimov.playground.extensions.getApplicationState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var counterTextView: TextView
    private lateinit var incrementFloatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewsById()
        subscribeToStateChanges()
        setOnClickListeners()
    }

    private fun findViewsById() {
        counterTextView = findViewById(R.id.counter_text_view)
        incrementFloatingActionButton = findViewById(R.id.increment_fab)
    }

    private fun subscribeToStateChanges() {
        lifecycleScope.launch {
            getApplicationState().counter.onEach { counterTextView.text = it.toString() }.collect()
        }
    }

    private fun setOnClickListeners() {
        incrementFloatingActionButton.setOnClickListener {
            getApplicationState().counter.update { value -> value + 1 }
        }
    }
}
