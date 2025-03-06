package com.vsulimov.playground.activity

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
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
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewsById()
        applyWindowInsets()
        subscribeToStateChanges()
        setOnClickListeners()
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(incrementFloatingActionButton) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Apply the insets as a margin to the view. This solution sets
            // only the bottom, left, and right dimensions, but you can apply whichever
            // insets are appropriate to your layout. You can also update the view padding
            // if that's more appropriate.
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = insets.left
                bottomMargin = insets.bottom
                rightMargin = insets.right
            }

            // Return CONSUMED if you don't want want the window insets to keep passing
            // down to descendant views.
            WindowInsetsCompat.CONSUMED
        }
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
