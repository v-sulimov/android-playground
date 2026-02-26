package com.vsulimov.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vsulimov.playground.ui.screens.ConfigurationScreen
import com.vsulimov.playground.ui.screens.OnboardingScreen
import com.vsulimov.playground.ui.theme.PlaygroundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlaygroundTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { contentPadding ->
                    AppNavigation(contentPadding)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(contentPadding: PaddingValues) {
    val navController = rememberNavController()
    val startRoute = "onboarding"

    NavHost(
        navController = navController,
        startDestination = startRoute
    ) {
        composable("onboarding") {
            OnboardingScreen(
                contentPadding = contentPadding,
                onNavigateToConfigurationScreen = {
                    navController.navigate("configuration")
                }
            )
        }

        composable("configuration") {
            ConfigurationScreen(
                contentPadding = contentPadding
            )
        }
    }
}
