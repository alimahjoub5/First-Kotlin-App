package com.example.advancedstopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StopwatchScreen(
    viewModel: StopwatchViewModel = viewModel(),
    navController: NavController,  // Ajout du paramètre navController
    onSpeechRecognition: () -> Unit
)  {
    val elapsedTime by viewModel.elapsedTime.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val laps by viewModel.laps.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display the elapsed time with animation
        AnimatedContent(
            targetState = elapsedTime,
            transitionSpec = {
                slideInVertically { it } + fadeIn() with
                        slideOutVertically { -it } + fadeOut()
            }
        ) { time ->
            Text(
                text = formatTime(time),
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.semantics {
                    contentDescription = "Elapsed time: ${formatTime(time)}"
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Controls for start/pause, reset, and lap buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AnimatedStopwatchButton(
                onClick = { viewModel.toggleStopwatch() },
                text = if (isRunning) "Pause" else "Start",
                contentDescription = if (isRunning) "Pause stopwatch" else "Start stopwatch"
            )

            AnimatedStopwatchButton(
                onClick = { viewModel.resetStopwatch() },
                text = "Reset",
                contentDescription = "Reset stopwatch"
            )

            AnimatedStopwatchButton(
                onClick = { viewModel.lapTime() },
                text = "Lap",
                enabled = isRunning,
                contentDescription = "Record lap time"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display list of lap times
        AnimatedVisibility(
            visible = laps.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            LazyColumn {
                items(laps.reversed()) { lapTime ->
                    Text(
                        text = formatTime(lapTime),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .semantics {
                                contentDescription = "Lap time: ${formatTime(lapTime)}"
                            }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Composable
fun AnimatedStopwatchButton(
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true,
    contentDescription: String
) {
    var clicked by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (clicked) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Button(
        onClick = {
            clicked = true
            onClick()
        },
        modifier = Modifier
            .size(80.dp)
            .scale(scale)
            .semantics {
                this.contentDescription = contentDescription
            },
        enabled = enabled
    ) {
        Text(text)
    }

    LaunchedEffect(clicked) {
        if (clicked) {
            delay(100)
            clicked = false
        }
    }
}

fun formatTime(timeMillis: Long): String {
    val minutes = (timeMillis / 1000) / 60
    val seconds = (timeMillis / 1000) % 60
    val milliseconds = (timeMillis % 1000) / 10 // Keep only two digits

    return String.format("%02d:%02d:%02d", minutes, seconds, milliseconds)
}
