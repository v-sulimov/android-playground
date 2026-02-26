package com.vsulimov.playground.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vsulimov.playground.R
import com.vsulimov.playground.ui.theme.PlaygroundTheme

@Composable
fun OnboardingScreen(contentPadding: PaddingValues, onNavigateToConfigurationScreen: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            Text(
                text = stringResource(R.string.onboarding_title),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.onboarding_description),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = onNavigateToConfigurationScreen,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = stringResource(R.string.action_continue), fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenPreview() {
    PlaygroundTheme {
        OnboardingScreen(
            onNavigateToConfigurationScreen = {},
            contentPadding = PaddingValues(0.dp)
        )
    }
}
